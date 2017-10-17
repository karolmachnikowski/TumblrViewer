package com.machnikowski.tumblrviewer;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.machnikowski.tumblrviewer.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;


public class DisplayPostsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String data = intent.getStringExtra("jsonArray");
        String url = intent.getStringExtra("url");
        JSONArray posts = null;
        try {
            posts = Utils.getPostsArrayFromString(data);
        } catch (JSONException e) { // Data is already validated so it should not happen, but just to be sure
            Intent pickUserIntent = new Intent(this, PickUserActivity.class);
            startActivity(pickUserIntent);
            Toast.makeText(this, "Coud not load data, correct username or try again later", Toast.LENGTH_SHORT).show();
            finish();
        }

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_posts);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        PostAdapter mPostAdapter = new PostAdapter(posts, url);

        mRecyclerView.setAdapter(mPostAdapter);


    }


}
