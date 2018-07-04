package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{
    // pass in Tweets array in constructor

    private List<Tweet> mTweets;
    Context context;

    public TweetAdapter(List<Tweet> tweets){
        mTweets = tweets;
    }

    //for each row, inflate layout and cache references into ViewHolder
    //only invoked when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvUserName) TextView tvUsername;
        @BindView(R.id.tvBody) TextView tvBody;
        @BindView(R.id.tvScreenName) TextView tvScreenName;
        @BindView(R.id.tvRelativeTime) TextView tvRelativeTime;
//

        public ViewHolder(View itemView) {
            super(itemView);

            //resolve view lookups
            ButterKnife.bind(this, itemView);

        }
    }


}
