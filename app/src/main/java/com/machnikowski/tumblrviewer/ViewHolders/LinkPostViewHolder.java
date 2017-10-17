package com.machnikowski.tumblrviewer.ViewHolders;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.machnikowski.tumblrviewer.R;

import org.json.JSONException;
import org.json.JSONObject;


public class LinkPostViewHolder extends PostViewHolder {
    private TextView linkTextView;
    private TextView descriptionTextView;
    private String text = null;
    private String url = null;
    private String description = null;

    public LinkPostViewHolder(View view) {
        super(view);
        descriptionTextView = (TextView) view.findViewById(R.id.tv_linkpost_description);
        linkTextView = (TextView) view.findViewById(R.id.tv_linkpost_link);

    }

    @Override
    public void bindData(JSONObject post) {
        if (post == null)
            return;
        try {
            getData(post);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (text != null && url != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                linkTextView.setText(Html.fromHtml("<a href='" + url + "'> " + text + " </a>", Html.FROM_HTML_MODE_LEGACY));
            } else {
                linkTextView.setText(Html.fromHtml("<a href='" + url + "'> " + text + " </a>"));
            }
            linkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        } else if (text != null) {
            linkTextView.setText(text);
        } else if (url != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                linkTextView.setText(Html.fromHtml("<a href='" + url + "'> " + url + " </a>", Html.FROM_HTML_MODE_LEGACY));
            } else {
                linkTextView.setText(Html.fromHtml("<a href='" + url + "'> " + url + " </a>"));
            }
            linkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        } else

        {
            linkTextView.setVisibility(View.GONE);
        }

        if (description != null)

        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                descriptionTextView.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY));
            } else {
                descriptionTextView.setText(Html.fromHtml(description));
            }
            descriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } else

        {
            descriptionTextView.setVisibility(View.GONE);
        }

    }

    private void getData(JSONObject post) throws JSONException {
        if (!post.isNull("link-text"))
            text = post.getString("link-text");
        if (!post.isNull("link-url"))
            url = post.getString("link-url");
        if (!post.isNull("link-description"))
            description = post.getString("link-description");
    }


}