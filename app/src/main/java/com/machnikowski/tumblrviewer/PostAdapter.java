package com.machnikowski.tumblrviewer;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.machnikowski.tumblrviewer.Utils.Utils;
import com.machnikowski.tumblrviewer.Utils.NameValuePair;
import com.machnikowski.tumblrviewer.ViewHolders.AnswerPostViewHolder;
import com.machnikowski.tumblrviewer.ViewHolders.AudioPostViewHolder;
import com.machnikowski.tumblrviewer.ViewHolders.ButtonViewHolder;
import com.machnikowski.tumblrviewer.ViewHolders.ConversationPostViewHolder;
import com.machnikowski.tumblrviewer.ViewHolders.LinkPostViewHolder;
import com.machnikowski.tumblrviewer.ViewHolders.PhotoPostViewHolder;
import com.machnikowski.tumblrviewer.ViewHolders.PostViewHolder;
import com.machnikowski.tumblrviewer.ViewHolders.QuotePostViewHolder;
import com.machnikowski.tumblrviewer.ViewHolders.RegularPostViewHolder;
import com.machnikowski.tumblrviewer.ViewHolders.VideoPostViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private static final int REGULAR_POST = 0;
    private static final int LINK_POST = 1;
    private static final int QUOTE_POST = 2;
    private static final int PHOTO_POST = 3;
    private static final int CONVERSATION_POST = 4;
    private static final int VIDEO_POST = 5;
    private static final int AUDIO_POST = 6;
    private static final int ANSWER_POST = 7;
    private static final int BUTTON = 101;
    private View view;
    private Button loadMoreButton;
    private JSONArray posts;
    private String url;


    public PostAdapter(JSONArray posts, String url) {
        this.url = url;
        this.posts = posts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        switch (viewType) {
            case REGULAR_POST:
                return new RegularPostViewHolder(LayoutInflater.from(context).inflate(R.layout.regular_post, viewGroup, false));
            case LINK_POST:
                return new LinkPostViewHolder(LayoutInflater.from(context).inflate(R.layout.link_post, viewGroup, false));
            case QUOTE_POST:
                return new QuotePostViewHolder(LayoutInflater.from(context).inflate(R.layout.quote_post, viewGroup, false));
            case PHOTO_POST:
                return new PhotoPostViewHolder(LayoutInflater.from(context).inflate(R.layout.photo_post, viewGroup, false));
            case CONVERSATION_POST:
                return new ConversationPostViewHolder(LayoutInflater.from(context).inflate(R.layout.conversation_post, viewGroup, false));
            case VIDEO_POST:
                return new VideoPostViewHolder(LayoutInflater.from(context).inflate(R.layout.video_post, viewGroup, false));
            case AUDIO_POST:
                return new AudioPostViewHolder(LayoutInflater.from(context).inflate(R.layout.audio_post, viewGroup, false));
            case ANSWER_POST:
                return new AnswerPostViewHolder(LayoutInflater.from(context).inflate(R.layout.answer_post, viewGroup, false));
            case BUTTON:
                return new ButtonViewHolder(LayoutInflater.from(context).inflate(R.layout.button, viewGroup, false));
        }
        return null;

    }


    @Override
    public void onBindViewHolder(PostViewHolder postViewHolder, int position) {
        if (position != posts.length()) { // Any post
            JSONObject post = null;
            try {
                post = posts.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            postViewHolder.bindData(post);

        } else if (position == posts.length()) { // Button which will allow to load more posts

            ButtonViewHolder buttonViewHolder = (ButtonViewHolder) postViewHolder;
            loadMoreButton = buttonViewHolder.button;
            loadMoreButton.setOnClickListener(loadMoreListener);

        }

    }

    @Override
    public int getItemCount() {
        if (null == posts) {
            return 0;
        }
        return posts.length() + 1;

    }

    @Override
    public int getItemViewType(int position) {
        if (position == posts.length()) {
            return BUTTON;
        }
        String type = null;
        try {
            type = posts.getJSONObject(position).getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (type.matches("regular")) {
            return REGULAR_POST;
        } else if (type.matches("link")) {
            return LINK_POST;
        } else if (type.matches("quote")) {
            return QUOTE_POST;
        } else if (type.matches("photo")) {
            return PHOTO_POST;
        } else if (type.matches("conversation")) {
            return CONVERSATION_POST;
        } else if (type.matches("video")) {
            return VIDEO_POST;
        } else if (type.matches("audio")) {
            return AUDIO_POST;
        } else if (type.matches("answer")) {
            return ANSWER_POST;
        }

        return -1;

    }

    private View.OnClickListener loadMoreListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LinkedList<NameValuePair> paramList = new LinkedList<>(); // List of GET parameters
            String offset = Integer.toString(posts.length());
            NameValuePair pair = new NameValuePair("start", offset); // GET parameter
            paramList.add(pair);
            view = v;
            URL url = Utils.buildUrl(PostAdapter.this.url, paramList); // Adding parameters to url
            new LoadMorePostsDownloadTask().execute(url);


        }
    };


    public class LoadMorePostsDownloadTask extends AsyncTask<URL, Void, JSONArray> {


        @Override
        protected JSONArray doInBackground(URL... urls) {
            String data;
            JSONArray posts = null;
            try { // Get data
                data = Utils.getResponseFromHttpUrl(urls[0]);
                posts = Utils.getPostsArrayFromString(data);

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return posts;
        }


        @Override
        protected void onPostExecute(JSONArray nextPosts) {

            if (nextPosts == null) { // Invalid data
                Toast.makeText(view.getContext(), "Coud not load data, try again later", Toast.LENGTH_SHORT).show();
            } else if (nextPosts.length() == 0) { // Data without posts
                Toast.makeText(view.getContext(), "There is no more posts", Toast.LENGTH_SHORT).show();
                loadMoreButton.setVisibility(View.GONE);
            } else { // Data with posts
                for (int i = 0; i < nextPosts.length(); i++) { // Adding new posts
                    try {
                        posts.put(nextPosts.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                notifyDataSetChanged();


            }
        }

    }
}
