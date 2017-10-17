package com.machnikowski.tumblrviewer.ViewHolders;
import android.view.View;
import android.widget.Button;

import com.machnikowski.tumblrviewer.R;

import org.json.JSONObject;


public class ButtonViewHolder extends PostViewHolder {

    public Button button;
    public ButtonViewHolder(View view) {
        super(view);
        button = (Button) view.findViewById(R.id.b_loadmore);
    }

    @Override
    public void bindData(JSONObject post) {

    }


}
