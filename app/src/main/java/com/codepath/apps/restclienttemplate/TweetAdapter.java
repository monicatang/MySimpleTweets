package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{
    // pass in Tweets array in constructor

    private List<Tweet> mTweets;
    public Context context;
    private final ClickListener listener;
    private static Activity activity;
    public static final int COMPOSE_REQUEST_CODE = 20;

    public interface ClickListener {

        void onPositionClicked(int position);

        void onLongClicked(int position);
    }

    public TweetAdapter(List<Tweet> tweets, ClickListener listener, Activity activity){
        mTweets = tweets;
        this.listener = listener;
        this.activity = activity;
    }

    //for each row, inflate layout and cache references into ViewHolder
    //only invoked when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView, mTweets, listener);
        return viewHolder;
    }

    // bind values based on position of element
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = mTweets.get(position);

        //populate views
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvScreenName.setText("@" + tweet.user.screenName);
        holder.tvRelativeTime.setText(tweet.relative_time);

        int radius = 70; // corner radius, higher value = more rounded
        int margin = 10; // crop margin, set to 0 for corners with no crop

        Glide.with(context).load(tweet.user.profileImageUrl)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(radius, margin))).into(holder.ivProfileImage);


    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvUserName)
        TextView tvUsername;
        @BindView(R.id.tvBody)
        TextView tvBody;
        @BindView(R.id.tvScreenName)
        TextView tvScreenName;
        @BindView(R.id.tvRelativeTime)
        TextView tvRelativeTime;
        @BindView(R.id.tvReply)
        TextView tvReply;
        private List<Tweet> mTweets;
        private WeakReference<ClickListener> listenerRef;


        public ViewHolder(View itemView, List<Tweet> mTweets, ClickListener listener) {
            super(itemView);
            this.mTweets = mTweets;
            listenerRef = new WeakReference<>(listener);

            //resolve view lookups
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            tvReply.setOnClickListener(this);

        }

        //implement reply button
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tvReply) {
                Log.i("Details", "clicked reply");
                Intent i = new Intent(v.getContext(), ComposeActivity.class);
                i.putExtra("message", tvScreenName.getText().toString());
                activity.startActivityForResult(i, COMPOSE_REQUEST_CODE);
            } else {
                Log.i("Details", "clicked itemview");
                int position = getAdapterPosition();
                //ensure position is valid
                if (position != RecyclerView.NO_POSITION) {
                    //get tweet at the position
                    Tweet tweet = mTweets.get(position);
                    Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                    //serialize tweet using parceler
                    intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                    //show activity
                    activity.startActivity(intent);
                }
            }

             listenerRef.get().onPositionClicked(getAdapterPosition());
        }


    }
    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
    }

}
