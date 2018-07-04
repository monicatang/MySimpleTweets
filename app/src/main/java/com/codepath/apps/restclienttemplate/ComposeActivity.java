package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    @BindView(R.id.etCompose) EditText etCompose;
    TwitterClient client;
    Tweet tweet;
    @BindView(R.id.tvCount) TextView tvCount;

    private final TextWatcher etWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textView to the current length
            tvCount.setText(String.valueOf(s.length()) + "/140");
        }

        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ButterKnife.bind(this);
        client = new TwitterClient(ComposeActivity.this);
        etCompose.addTextChangedListener(etWatcher);
    }

    public void onTweet(View v){
        Log.d("tweet", "Processing tweet");
        String message = etCompose.getText().toString();

        client.sendTweet(message, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Intent intent = new Intent();
                    tweet = Tweet.fromJSON(response);
                    intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
//
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("tweet", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("tweet", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("tweet", responseString);
            }
        });



    }

}
