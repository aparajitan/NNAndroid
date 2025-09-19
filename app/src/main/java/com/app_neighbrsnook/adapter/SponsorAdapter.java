package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.intreface.ImageCallBack;


public class SponsorAdapter extends RecyclerView.Adapter<SponsorAdapter.ViewHolder> {

    Context context;
    ImageCallBack imc;
    int[] images = {R.drawable.background_fifth, R.drawable.background_image, R.drawable.background_image_testing, R.drawable.background_photo_testing,
            R.drawable.background_img_second, R.drawable.background_img_user};
    public SponsorAdapter(int[] images, Context mContext, ImageCallBack imc) {
        this.context = mContext;
        this.images = images;
        this.imc = imc;
    }


    @NonNull
    @Override
    public SponsorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sponsor_rv_row, parent, false);
        return new SponsorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SponsorAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Uri video = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        holder.videoView.setVideoURI(video);

        holder.videoView.start();

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image_view;
        RelativeLayout root;
        VideoView videoView;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_view = itemView.findViewById(R.id.image_view);
            videoView = itemView.findViewById(R.id.wall_image_imageview);

        }
    }


}

