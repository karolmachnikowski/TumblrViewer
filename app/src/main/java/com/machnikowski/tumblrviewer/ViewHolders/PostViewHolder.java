package com.machnikowski.tumblrviewer.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;


import org.json.JSONObject;


public abstract class PostViewHolder extends RecyclerView.ViewHolder {


    public PostViewHolder(View view) {
        super(view);


    }

    public abstract void bindData(JSONObject post);
}