package com.machnikowski.tumblrviewer.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.machnikowski.tumblrviewer.R;

import org.json.JSONException;
import org.json.JSONObject;


public class ConversationPostViewHolder extends PostViewHolder {
    private TextView titleTextView;
    private TextView conversationTextView;
    private String title = null;
    private String conversation = null;
    public ConversationPostViewHolder(View view) {
        super(view);
        titleTextView = (TextView) view.findViewById(R.id.tv_conversationpost_title);
        conversationTextView = (TextView) view.findViewById(R.id.tv_conversationpost_conversation);

    }

    @Override
    public void bindData(JSONObject post) {
        if(post == null)
            return;
        try {
            getData(post);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(title!=null)
            titleTextView.setText(title);
        else{
            titleTextView.setVisibility(View.GONE);
        }

        if(conversation!=null) {
            conversation = conversation.replace("\n", "\n\n");
            conversationTextView.setText(conversation);
        }
        else{
            conversationTextView.setVisibility(View.GONE);
        }

    }

    private void getData(JSONObject post) throws JSONException {
        if(!post.isNull("conversation-title"))
            title = post.getString("conversation-title");
        if(!post.isNull("conversation-text"))
            conversation = post.getString("conversation-text");
    }

}