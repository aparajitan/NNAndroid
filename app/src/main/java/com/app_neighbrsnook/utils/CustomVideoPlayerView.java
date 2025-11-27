package com.app_neighbrsnook.utils;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import java.io.IOException;

public class CustomVideoPlayerView extends TextureView implements TextureView.SurfaceTextureListener {
    private MediaPlayer mediaPlayer;
    private Uri videoUri;
    private boolean isMuted = true;
    private boolean isPrepared = false;
    private MediaPlayer.OnPreparedListener onPreparedListener;
    private MediaPlayer.OnCompletionListener onCompletionListener;

    public CustomVideoPlayerView(Context context) {
        super(context);
        init();
    }

    public CustomVideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomVideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setSurfaceTextureListener(this);
    }

    public void setVideoUri(Uri uri) {
        this.videoUri = uri;
        if (isAvailable()) {
            prepareMediaPlayer();
        }
    }

    private void prepareMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getContext(), videoUri);
            mediaPlayer.setSurface(new Surface(getSurfaceTexture()));
            mediaPlayer.setOnVideoSizeChangedListener((mp, width, height) -> adjustAspectRatio(width, height));
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                isPrepared = true;
                if (onPreparedListener != null) {
                    onPreparedListener.onPrepared(mp);
                }
                mp.seekTo(1);  // This ensures that the first frame is shown immediately after preparation
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                if (onCompletionListener != null) {
                    onCompletionListener.onCompletion(mp);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adjustAspectRatio(int videoWidth, int videoHeight) {
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        // Calculate aspect ratio of the video and the view
        float videoAspectRatio = (float) videoWidth / videoHeight;
        float viewAspectRatio = (float) viewWidth / viewHeight;

        float scaleX, scaleY;
        if (videoAspectRatio > viewAspectRatio) {
            // Video is wider than the view, scale based on height
            scaleX = viewHeight / (float) videoHeight * videoWidth / viewWidth;
            scaleY = 1f;
        } else {
            // Video is taller than the view, scale based on width
            scaleX = 1f;
            scaleY = viewWidth / (float) videoWidth * videoHeight / viewHeight;
        }

        // Scale and center the video
        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY, viewWidth / 2f, viewHeight / 2f);
        setTransform(matrix);
        invalidate();
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
        this.onPreparedListener = listener;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        this.onCompletionListener = listener;
    }

    public void start() {
        if (isPrepared && mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public int getDuration() {
        return mediaPlayer != null ? mediaPlayer.getDuration() : 0;
    }

    public void seekTo(int millis) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(millis);
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public int getCurrentPosition() {
        return mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0;
    }

    public void setMute(boolean mute) {
        if (mediaPlayer != null) {
            float volume = mute ? 0f : 1f;
            mediaPlayer.setVolume(volume, volume);
            isMuted = mute;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (videoUri != null) {
            prepareMediaPlayer();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {}

}


