package com.app_neighbrsnook.adapter;


import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.utils.CustomVideoView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MediaViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_IMAGE = 1;
    private static final int VIEW_TYPE_VIDEO = 2;
    private Context context;
    private Dialog dialog;
    private ArrayList<Uri> mediaList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Uri uri);
        void onRemoveClick(int position, String type);
    }

    public MediaViewAdapter(Context context, Dialog dialog, ArrayList<Uri> mediaList, OnItemClickListener listener) {
        this.context = context;
        this.dialog = dialog;
        this.mediaList = mediaList;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        Uri uri = mediaList.get(position);
        String mimeType = getMimeType(uri);
        return (mimeType != null && mimeType.startsWith("video")) ? VIEW_TYPE_VIDEO : VIEW_TYPE_IMAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_VIDEO) {
            View view = inflater.inflate(R.layout.inflate_video_upload, parent, false);
            return new VideoViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.inflate_image_upload_2, parent, false);
            return new ImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Uri uri = mediaList.get(position);
        if (holder instanceof VideoViewHolder) {
            ((VideoViewHolder) holder).bind(uri, listener, position);
        } else if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).bind(uri, listener, position);
        }
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    private String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView btnRemove;
        CardView cardView;
        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_uploaded_image);
            cardView = itemView.findViewById(R.id.cardView);
            btnRemove = itemView.findViewById(R.id.iv_remove_image);
        }

        void bind(Uri uri, OnItemClickListener listener, int position) {
            Glide.with(imageView.getContext()).load(uri).fitCenter().into(imageView);
            itemView.setOnClickListener(v -> listener.onItemClick(uri));
            cardView.setOnClickListener(v -> listener.onRemoveClick(position,"Image"));
        }
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        CustomVideoView videoView;
        ImageView btnRemove, btnPlay, btnPlay2, imageMute;
        boolean isPlaying = false;
        SeekBar videoSeekbar;
        TextView tvCurrentTime, tvTotalDuration;
        CardView cardView;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.iv_uploaded_video);
            btnRemove = itemView.findViewById(R.id.iv_remove_image);
            btnPlay = itemView.findViewById(R.id.iv_play_pouse);
            btnPlay2 = itemView.findViewById(R.id.iv_play_pouse2);
            imageMute = itemView.findViewById(R.id.iv_mute_image);
            videoSeekbar = itemView.findViewById(R.id.video_seekbar);
            tvCurrentTime = itemView.findViewById(R.id.tv_current_time);
            cardView = itemView.findViewById(R.id.cardView);
            tvTotalDuration = itemView.findViewById(R.id.tv_total_duration);
            videoSeekbar.setVisibility(View.GONE);
            tvCurrentTime.setVisibility(View.GONE);
            tvTotalDuration.setVisibility(View.GONE);

            btnPlay2.setOnClickListener(v -> btnPlay.performClick());
            btnPlay.setOnClickListener(v -> togglePlayback());
            imageMute.setOnClickListener(v -> toggleMute());
        }

        void bind(Uri uri, OnItemClickListener listener, int position) {
            videoView.setVideoURI(uri);
            isPlaying = false;
            videoView.pause();

            btnPlay.setImageResource(R.drawable.baseline_play_arrow_24);
            cardView.setOnClickListener(v -> listener.onRemoveClick(position,"Video"));
        }

        private void togglePlayback() {
            if (isPlaying) {
                videoView.pause();
                btnPlay.setImageResource(R.drawable.baseline_play_arrow_24);
                btnPlay2.setImageResource(R.drawable.baseline_play_arrow_24);
                btnPlay.setVisibility(View.VISIBLE);
            } else {
                videoView.start();
                btnPlay.setImageResource(R.drawable.baseline_pause_24);
                btnPlay2.setImageResource(R.drawable.baseline_pause_24);
                btnPlay.setVisibility(View.GONE);
            }
            isPlaying = !isPlaying;
        }

        private void toggleMute() {
            if (videoView.isMuted()) {
                videoView.setVolume(1, 1);
                imageMute.setImageResource(R.drawable.baseline_volume_up_24);
            } else {
                videoView.setVolume(0, 0);
                imageMute.setImageResource(R.drawable.baseline_volume_off_24);
            }
        }
    }
}