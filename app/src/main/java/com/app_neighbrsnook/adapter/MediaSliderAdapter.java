package com.app_neighbrsnook.adapter;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.utils.CustomVideoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;
import android.net.Uri;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.MediaController;

public class MediaSliderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_IMAGE = 1;
    private static final int VIEW_TYPE_VIDEO = 2;

    Context context;
    Dialog dialog;
    String from;
    List<ImagePojo> imageList;
    MediaSliderAdapter.ImageRequest imageRequest;

    public MediaSliderAdapter(Dialog _dialog, List<ImagePojo> imageList, String from, int pos, Context mContext) {
        this.dialog = _dialog;
        this.imageList = imageList;
        this.from = from;
        this.context = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        ImagePojo item = imageList.get(position);
        if (item.getVideo() != null && !item.getVideo().isEmpty()) {
            return VIEW_TYPE_VIDEO;
        } else {
            return VIEW_TYPE_IMAGE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_VIDEO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_video_upload, parent, false);
            return new VideoViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_image_upload_2, parent, false);
            return new ImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImagePojo item = imageList.get(position);

        if (from != null && from.equals("detail")) {
            if (holder instanceof VideoViewHolder) {
                ((VideoViewHolder) holder).cardView.setVisibility(View.GONE);
            } else if (holder instanceof ImageViewHolder) {
                ((ImageViewHolder) holder).cardView.setVisibility(View.GONE);
            }
        }

        if (holder instanceof VideoViewHolder) {
            VideoViewHolder videoHolder = (VideoViewHolder) holder;
            videoHolder.videoView.setVideoURI(Uri.parse(item.getVideo()));
            videoHolder.videoView.seekTo(1);

            videoHolder.videoView.setOnPreparedListener(mp -> {
                int duration = mp.getDuration();
                videoHolder.video_seekbar.setMax(duration);
                videoHolder.tvTotalDuration.setText(formatTime(duration));
                videoHolder.videoView.setVolume(0, 0);
                videoHolder.imageMute.setImageResource(R.drawable.baseline_volume_off_24);
                updateSeekBar(videoHolder);
            });

            videoHolder.video_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        videoHolder.videoView.seekTo(progress);
                        videoHolder.tvCurrentTime.setText(formatTime(progress));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    videoHolder.videoView.pause();
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    videoHolder.videoView.start();
                    videoHolder.imagePlay.setImageResource(R.drawable.baseline_pause_24);
                }
            });

            videoHolder.videoView.setOnClickListener(view -> {
                if (videoHolder.videoView.isPlaying()) {
                    videoHolder.videoView.pause();
                    videoHolder.imagePlay.setImageResource(R.drawable.baseline_play_arrow_24);
                    videoHolder.imagePlay2.setImageResource(R.drawable.baseline_play_arrow_24);
                    videoHolder.imagePlay.setVisibility(View.VISIBLE);
                } else {
                    videoHolder.videoView.start();
                    videoHolder.imagePlay.setImageResource(R.drawable.baseline_pause_24);
                    videoHolder.imagePlay2.setImageResource(R.drawable.baseline_pause_24);
                    videoHolder.imagePlay.setVisibility(View.GONE);
                    updateSeekBar(videoHolder);
                }
            });

            videoHolder.imagePlay2.setOnClickListener(view -> {
                videoHolder.videoView.performClick();
            });

            videoHolder.imageMute.setOnClickListener(view -> {
                if (videoHolder.videoView.isMuted()) {
                    videoHolder.videoView.setVolume(1, 1);
                    videoHolder.imageMute.setImageResource(R.drawable.baseline_volume_up_24);
                } else {
                    videoHolder.videoView.setVolume(0, 0);
                    videoHolder.imageMute.setImageResource(R.drawable.baseline_volume_off_24);
                }
            });

        } else if (holder instanceof ImageViewHolder) {
            ImageViewHolder imageHolder = (ImageViewHolder) holder;
           // Picasso.get().load(item.getImg()).fit().centerInside().into(imageHolder.imageView);
            Glide.with(context)
                    .load(item.getImg())
                    .apply(RequestOptions.centerInsideTransform()) // Use centerInside
                    .into(imageHolder.imageView);

            imageHolder.imageView.setOnClickListener(view -> {
                // Handle image click
                // imageRequest.onImageClick(position);
            });

            imageHolder.imageTrash.setOnClickListener(view -> {
                // Handle image remove click
                // imageRequest.onCrossClick(position);
            });
        }
    }

    private void updateSeekBar(VideoViewHolder holder) {
        holder.updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (holder.videoView != null && holder.videoView.isPlaying()) {
                    int currentPosition = holder.videoView.getCurrentPosition();
                    holder.video_seekbar.setProgress(currentPosition);
                    holder.tvCurrentTime.setText(formatTime(currentPosition));
                    holder.handler.postDelayed(this, 100);
                }
            }
        };
        holder.handler.post(holder.updateSeekBar);
    }

    private String formatTime(int millis) {
        int seconds = (millis / 1000) % 60;
        int minutes = (millis / (1000 * 60)) % 60;
        int hours = (millis / (1000 * 60 * 60)) % 24;
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, imageTrash;
        CardView cardView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_uploaded_image);
            imageTrash = itemView.findViewById(R.id.iv_remove_image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageTrash, imagePlay, imagePlay2, imageMute;
        CustomVideoView videoView;
        CardView cardView;
        SeekBar video_seekbar;
        TextView tvCurrentTime, tvTotalDuration;
        Handler handler = new Handler();
        Runnable updateSeekBar;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePlay = itemView.findViewById(R.id.iv_play_pouse);
            imagePlay2 = itemView.findViewById(R.id.iv_play_pouse2);
            imageMute = itemView.findViewById(R.id.iv_mute_image);
            imageTrash = itemView.findViewById(R.id.iv_remove_image);
            videoView = itemView.findViewById(R.id.iv_uploaded_video);
            cardView = itemView.findViewById(R.id.cardView);
            video_seekbar = itemView.findViewById(R.id.video_seekbar);
            tvCurrentTime = itemView.findViewById(R.id.tv_current_time);
            tvTotalDuration = itemView.findViewById(R.id.tv_total_duration);
        }
    }

    public interface ImageRequest {
        void onImageClick(int pos);
        void onCrossClick(int pos);
    }
}
