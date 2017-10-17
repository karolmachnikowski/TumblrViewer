package com.machnikowski.tumblrviewer.ViewHolders;


import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.machnikowski.tumblrviewer.R;

import org.json.JSONException;
import org.json.JSONObject;


public class VideoPostViewHolder extends PostViewHolder {

    private WebView webView;
    private TextView captionTextView;
    private String url = null;
    private String player = null;
    private String caption = null;


    public VideoPostViewHolder(View view) {
        super(view);

        webView = (WebView) view.findViewById(R.id.wv_videopost_videoview);
        captionTextView = (TextView) view.findViewById(R.id.tv_videoopost_caption);

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

        if (caption != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                captionTextView.setText(Html.fromHtml(caption, Html.FROM_HTML_MODE_LEGACY));
            } else {
                captionTextView.setText(Html.fromHtml(caption));
            }
            captionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            captionTextView.setVisibility(View.GONE);
        }


        if (url != null) {

            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setInitialScale(1);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.setScrollbarFadingEnabled(false);
            webView.loadDataWithBaseURL(url, player,
                    "text/html; charset=utf-8", "UTF-8", null);


        }


    }

    private void getData(JSONObject post) throws JSONException {
        if (!post.isNull("video-source"))
            url = post.getString("video-source");
        if (!post.isNull("video-caption"))
            caption = post.getString("video-caption");
        if (!post.isNull("video-player"))
            player = post.getString("video-player");
    }

}