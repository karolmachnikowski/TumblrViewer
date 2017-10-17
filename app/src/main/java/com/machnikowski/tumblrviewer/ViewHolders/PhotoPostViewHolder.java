package com.machnikowski.tumblrviewer.ViewHolders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.machnikowski.tumblrviewer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;


public class PhotoPostViewHolder extends PostViewHolder {

    private TextView captionTextView;
    private ImageView imageView;
    private String url = null;
    private String caption = null;

    public PhotoPostViewHolder(View view) {
        super(view);
        captionTextView = (TextView) view.findViewById(R.id.tv_photopost_caption);
        imageView = (ImageView) view.findViewById(R.id.iv_photopost_photo);

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
            new DownloadImageTask().execute(url);
        }


    }

    private void getData(JSONObject post) throws JSONException {
        if(!post.isNull("photo-url-1280"))
            url = post.getString("photo-url-1280");
        if(!post.isNull("photo-caption"))
            caption = post.getString("photo-caption");
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap img = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                img = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return img;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null)
                imageView.setImageBitmap(result);
        }
    }

}
