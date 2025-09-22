package com.app_neighbrsnook.adapter;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.utils.CustomVideoPlayerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class BusinessWallChildAdapter extends RecyclerView.Adapter<BusinessWallChildAdapter.ViewHolder> {
    Context context;
    ImageCallBack imc;
    List<ImagePojo> list;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;
    int bid;

    public BusinessWallChildAdapter(int bid,List<ImagePojo> list, Context mContext, ImageCallBack imc) {
        this.context = mContext;
        this.list = list;
        this.imc = imc;
        this.bid = bid;
    }

    @NonNull
    @Override
    public BusinessWallChildAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item1, parent, false);
        context = parent.getContext();
        return new BusinessWallChildAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessWallChildAdapter.ViewHolder holder, int position) {
        ImagePojo item = list.get(position);
        String image = item.getImg();
        String video = item.getVideo();

        if ((image == null || image.isEmpty()) && (video == null || video.isEmpty())) {
            Log.e("WallChildAdapter", "Both image and video URIs are null or empty for position: " + position);
            return;
        }

        if (video != null && !video.isEmpty()) {
            holder.image_view.setVisibility(View.GONE);
            holder.imageview1.setVisibility(View.GONE);
            holder.video_view.setVisibility(View.VISIBLE);
            holder.mute_button.setVisibility(View.VISIBLE);
            holder.play_button.setVisibility(View.VISIBLE);
            holder.video_seekbar.setVisibility(View.GONE);
            holder.video_view.setVideoUri(Uri.parse(video));

            holder.video_view.setOnPreparedListener(mp -> {
                mp.setVolume(0, 0); // Mute by default
                holder.mute_button.setImageResource(R.drawable.baseline_volume_off_24);
                holder.isMuted = true;

                holder.mute_button.setOnClickListener(view -> {
                    if (holder.isMuted) {
                        mp.setVolume(1, 1);
                        holder.mute_button.setImageResource(R.drawable.baseline_volume_up_24);
                    } else {
                        mp.setVolume(0, 0);
                        holder.mute_button.setImageResource(R.drawable.baseline_volume_off_24);
                    }
                    holder.isMuted = !holder.isMuted;
                });

                holder.play_button.setOnClickListener(v -> {
                    if (holder.isPlaying) {
                        holder.video_view.pause();
                        holder.play_button.setImageResource(R.drawable.baseline_play_arrow_24);
                    } else {
                        holder.video_view.start();
                        holder.play_button.setImageResource(R.drawable.baseline_pause_24);
                    }
                    holder.isPlaying = !holder.isPlaying;
                    updateSeekBar(holder);
                });

                holder.video_seekbar.setMax(mp.getDuration());

                mp.setOnCompletionListener(mediaPlayer -> {
                    holder.play_button.setImageResource(R.drawable.baseline_play_arrow_24);
                    holder.isPlaying = false;
                });
            });

            holder.video_view.seekTo(1);

            holder.video_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        holder.video_view.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    holder.video_view.pause();
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    holder.video_view.start();
                }
            });

            holder.video_view.setOnClickListener(v -> {
                imc.onVideoClick(bid, list, position);
                holder.video_view.pause();
                holder.play_button.setImageResource(R.drawable.baseline_play_arrow_24);
            });

        } else {
            holder.video_view.setVisibility(View.GONE);
            holder.mute_button.setVisibility(View.GONE);
            holder.play_button.setVisibility(View.GONE);
            holder.video_seekbar.setVisibility(View.GONE);
            holder.image_view.setVisibility(View.VISIBLE);
            holder.imageview1.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(image)
                    .apply(RequestOptions.centerCropTransform())
                    .into(holder.image_view);
        }

        if (list.size() == 1) {
            holder.count.setVisibility(View.GONE);
        } else {
            holder.count.setText(position + 1 + "/" + list.size());
        }

        holder.image_view.setOnClickListener(v -> {
            imc.onVideoClick(bid, list, position);
        });
    }

    private void updateSeekBar(BusinessWallChildAdapter.ViewHolder holder) {
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (holder.video_view != null && holder.video_view.isPlaying()) {
                    holder.video_seekbar.setProgress(holder.video_view.getCurrentPosition());
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(updateSeekBar);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view, imageview1, mute_button, play_button, full_sreen;
        FrameLayout videoFrame, imageFrame;
        TextView count;
        CustomVideoPlayerView video_view;
        SeekBar video_seekbar;
        boolean isPlaying = false;
        boolean isMuted = true;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_view = itemView.findViewById(R.id.image_view);
            count = itemView.findViewById(R.id.image_count);
            imageview1 = itemView.findViewById(R.id.imageview1);
            video_view = itemView.findViewById(R.id.video_view);
            mute_button = itemView.findViewById(R.id.iv_mute_image);
            play_button = itemView.findViewById(R.id.iv_play_pouse);
            full_sreen = itemView.findViewById(R.id.image_full_sreen);
            videoFrame = itemView.findViewById(R.id.fl_video_frame);
            imageFrame = itemView.findViewById(R.id.fl_image_frame);
            video_seekbar = itemView.findViewById(R.id.video_seekbar);
        }
    }

    public interface ImageCallBack {
        void onVideoClick(int bid, List<ImagePojo> list, int pos);
    }
}












