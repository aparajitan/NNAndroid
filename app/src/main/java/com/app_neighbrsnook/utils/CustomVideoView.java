package com.app_neighbrsnook.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.VideoView;

import java.lang.reflect.Field;

public class CustomVideoView extends VideoView {

    private boolean isMuted = true;

    public CustomVideoView(Context context) {
        super(context);
        init();
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOnPreparedListener(mp -> {
            setVolume(0, 0);
        });
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setVolume(float leftVolume, float rightVolume) {
        MediaPlayer mediaPlayer;
        try {
            Field mMediaPlayerField = VideoView.class.getDeclaredField("mMediaPlayer");
            mMediaPlayerField.setAccessible(true);
            mediaPlayer = (MediaPlayer) mMediaPlayerField.get(this);
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(leftVolume, rightVolume);
                isMuted = leftVolume == 0 && rightVolume == 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
