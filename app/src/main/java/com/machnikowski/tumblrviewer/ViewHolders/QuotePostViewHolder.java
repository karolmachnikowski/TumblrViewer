package com.machnikowski.tumblrviewer.ViewHolders;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.machnikowski.tumblrviewer.R;

import org.json.JSONException;
import org.json.JSONObject;

public class QuotePostViewHolder extends PostViewHolder {
    private TextView textTextView;
    private TextView sourceTextView;
    private String text = null;
    private String source = null;

    public QuotePostViewHolder(View view) {
        super(view);
        textTextView = (TextView) view.findViewById(R.id.tv_quotepost_text);
        sourceTextView = (TextView) view.findViewById(R.id.tv_quotepost_source);

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

        if (text != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                textTextView.setText(Html.fromHtml('"' + text + '"', Html.FROM_HTML_MODE_LEGACY));
            } else {
                textTextView.setText(Html.fromHtml('"' + text + '"'));
            }
            textTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            textTextView.setVisibility(View.GONE);
        }

        if (source != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                sourceTextView.setText(Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY));
            } else {
                sourceTextView.setText(Html.fromHtml(source));
            }
            sourceTextView.setMovementMethod(LinkMovementMethod.getInstance());

        } else {
            sourceTextView.setVisibility(View.GONE);
        }


    }

    private void getData(JSONObject post) throws JSONException {
        if (!post.isNull("quote-text"))
            text = post.getString("quote-text");
        if (!post.isNull("quote-source"))
            source = post.getString("quote-source");
    }

}
