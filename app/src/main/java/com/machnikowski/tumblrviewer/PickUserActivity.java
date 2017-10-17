package com.machnikowski.tumblrviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.machnikowski.tumblrviewer.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class PickUserActivity extends AppCompatActivity {

    private Button findButton;
    private EditText mEditText;
    private ProgressBar mLoadingIndicator;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_user);

        findButton = (Button) findViewById(R.id.b_find_button);
        mEditText = (EditText) findViewById(R.id.et_edit_text);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        // For better user experience
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        mEditText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                            findButton.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

    }

    // Triggers when findButton is clicked
    public void findUserClicked(View view) {

        if (mEditText.getText().toString().matches("")) // Check if edittext is empty
            return;
        if(Utils.checkConnection(this)){ // Check internet connection
            findButton.setEnabled(false); // To prevent from starting many asynctasks
            String username = mEditText.getText().toString();
            url = getResources().getString(R.string.https) + username + getResources().getString(R.string.url); // Create url
            new GetPostsDownloadTask().execute(url);
        }
        else{
            Toast.makeText(PickUserActivity.this, "Internet connection is required", Toast.LENGTH_SHORT).show();
        }



    }

    private class GetPostsDownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);

        }


        @Override
        protected String doInBackground(String... urls) {

            URL url;
            String data = null;
            JSONArray posts = null;
            try {
                url = new URL(urls[0]);
                data = Utils.getResponseFromHttpUrl(url);
                posts = Utils.getPostsArrayFromString(data); // Data validation

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            if (posts == null) // Data invalid
                return null;
            else if(posts.length() == 0) // Valid data without posts
                return "0";
            else // Valid data with posts
                return data;
        }

        @Override
        protected void onPostExecute(String data) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            findButton.setEnabled(true);
            if (data == null){
                Toast.makeText(PickUserActivity.this, "Could not load data, correct username or try again later", Toast.LENGTH_SHORT).show();
            }
            else if (data.matches("0")){
                Toast.makeText(PickUserActivity.this, "This user has no posts", Toast.LENGTH_SHORT).show();
            }
            else { // Start DisplayPostsActivity and pass data
                Intent intent = new Intent(PickUserActivity.this, DisplayPostsActivity.class);
                intent.putExtra("jsonArray", data);
                intent.putExtra("url", url);
                startActivity(intent);
            }

        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
