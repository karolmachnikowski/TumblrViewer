package com.machnikowski.tumblrviewer.ViewHolders;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.machnikowski.tumblrviewer.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegularPostViewHolder extends PostViewHolder {
    private TextView titleTextView;
    private TextView bodyTextView;
    private String title = null;
    private String body = null;

    public RegularPostViewHolder(View view) {
        super(view);
        titleTextView = (TextView) view.findViewById(R.id.tv_regularpost_title);
        bodyTextView = (TextView) view.findViewById(R.id.tv_regularpost_body);

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

        if (body != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                bodyTextView.setText(Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY));
            } else {
                bodyTextView.setText(Html.fromHtml(body));
            }
            bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            bodyTextView.setVisibility(View.GONE);
        }

        if (title != null) {
            titleTextView.setText(title);
        } else {
            titleTextView.setVisibility(View.GONE);
        }
    }

    private void getData(JSONObject post) throws JSONException {
        if(!post.isNull("regular-title"))
            title = post.getString("regular-title");
        if(!post.isNull("regular-body"))
            body = post.getString("regular-body");
    }

}