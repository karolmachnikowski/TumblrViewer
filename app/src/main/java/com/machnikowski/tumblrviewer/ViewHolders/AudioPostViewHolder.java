package com.machnikowski.tumblrviewer.ViewHolders;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.machnikowski.tumblrviewer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

public class AudioPostViewHolder extends PostViewHolder {

    private Button playButton;
    private Button stopButton;
    private SeekBar scrubberSeekBar;
    private TextView captionTextView;
    private MediaPlayer player;
    private String url = null;
    private String caption = null;
    private static int NOTPREPARED = 0;
    private static int PREPARED = 1;
    private static int FAILED = 2;
    private int state = NOTPREPARED;


    public AudioPostViewHolder(View view) {
        super(view);
        playButton = (Button) view.findViewById(R.id.b_audiopost_playbutton);
        stopButton = (Button) view.findViewById(R.id.b_audiopost_stopbutton);
        scrubberSeekBar = (SeekBar) view.findViewById(R.id.sb_audiopost_seekbar);
        captionTextView = (TextView) view.findViewById(R.id.tv_audiopost_caption);


    }

    @Override
    public void bindData(JSONObject post) {
        if (post == null)
            return;
        try {
            getData(post);
        } catch (JSONException | UnsupportedEncodingException e) {
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

            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnPreparedListener(playerOnPreparedListener);
            try {
                player.setDataSource(url);
                player.prepareAsync();

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            state = FAILED;
        }

        playButton.setOnClickListener(playButtonListener);
        stopButton.setOnClickListener(stopButtonListener);


    }

    private void getData(JSONObject post) throws JSONException, UnsupportedEncodingException {
        if (!post.isNull("audio-player"))
            url = post.getString("audio-player");
        url = url.split("audio_file=")[1];
        url = url.split("\"")[0];
        url = java.net.URLDecoder.decode(url, "UTF-8");
        if (!post.isNull("audio-caption"))
            caption = post.getString("audio-caption");
    }

    private View.OnClickListener playButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (state == PREPARED) {
                player.start();
            } else if (state == NOTPREPARED) {
                Toast.makeText(v.getContext(), "Preparing... click again after a second", Toast.LENGTH_SHORT).show();
            } else if (state == FAILED) {
                Toast.makeText(v.getContext(), "Failed to load data, try to restart application", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener stopButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (state == PREPARED && player.isPlaying()) {
                player.pause();
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser)
                player.seekTo(progress);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private MediaPlayer.OnPreparedListener playerOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {

            scrubberSeekBar.setMax(player.getDuration());
            scrubberSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    scrubberSeekBar.setProgress(player.getCurrentPosition());
                }
            }, 0, 100);

            state = PREPARED;

        }
    };


}





