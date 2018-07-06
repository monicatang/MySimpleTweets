package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Tweet tweet;

    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvScreenName) TextView tvScreenName;
    @BindView(R.id.tvRelativeTime) TextView tvRelativeTime;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.tvReply) TextView tvReply;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.ivRetweet) ImageView ivRetweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // resolve view objects
        ButterKnife.bind(this);

        // unwrap tweets
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        // set tweet details
        tvUserName.setText(tweet.user.name);
        tvScreenName.setText(tweet.user.screenName);
        tvRelativeTime.setText(tweet.relative_time);
        tvBody.setText(tweet.body);

        Drawable ic_vector_reTweet = getResources().getDrawable(R.drawable.ic_vector_retweet);
        ivRetweet.setImageDrawable(ic_vector_reTweet);

        int radius = 70; // corner radius, higher value = more rounded
        int margin = 10; // crop margin, set to 0 for corners with no crop

        Glide.with(this).load(tweet.user.profileImageUrl)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(radius, margin))).into(ivProfileImage);

        // set on click listeners
        tvReply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvReply:
                Intent i = new Intent(v.getContext(), ComposeActivity.class);
                i.putExtra("message", "@" + tvScreenName.getText().toString());
                v.getContext().startActivity(i);
                break;

            case R.id.ivRetweet:
                break;

            default:
                break;
                }
    }
}
