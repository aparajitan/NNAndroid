package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.event.ImageShowActivity;
import com.app_neighbrsnook.model.ImagePOJO;

import java.util.ArrayList;

public class ImageUploadAdapter1 extends RecyclerView.Adapter<ImageUploadAdapter1.ImageUploadHolder> {
    Activity activity;
    public static ArrayList<ImagePOJO> imageList;
    ImageItemClick itemClick;

   // ImageRequest imageRequest;

    public ImageUploadAdapter1(Activity activity, ArrayList<ImagePOJO> bitmapList, ImageItemClick imageItemClick) {
        this.activity = activity;
        imageList = bitmapList;
        this.itemClick=imageItemClick;
    }
    public interface ImageItemClick
    {
        void removeImage(int position);
         //void imageRequest(int position);
    }
    @NonNull
    @Override
    public ImageUploadHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(activity).inflate(R.layout.inflate_image_upload,viewGroup,false);
        return new ImageUploadHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ImageUploadHolder holder, @SuppressLint("RecyclerView") final int i) {
        try {
            if (imageList.get(i).videoUri!=null){
                holder.iv_image.setVisibility(View.GONE);
                holder.videoView.setVisibility(View.VISIBLE);
                holder.videoView.setVideoURI(imageList.get(i).videoUri);
                holder.videoView.requestFocus();
                holder.videoView.start();

            }else {
                holder.iv_image.setVisibility(View.VISIBLE);
                holder.videoView.setVisibility(View.GONE);
                holder.iv_image.setImageBitmap(imageList.get(i).bitmap);
            }
            holder.iv_remove_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.removeImage(i);
                }
            });
            holder.itemView.setOnClickListener(view -> {
                Intent intent=new Intent(activity, ImageShowActivity.class);
                intent.putExtra("ImageUpload",i);
                activity.startActivity(intent);
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageUploadHolder extends RecyclerView.ViewHolder {
        ImageView iv_remove_image;
        ImageView iv_image;
        VideoView videoView;
        public ImageUploadHolder(@NonNull View itemView) {
            super(itemView);
            iv_remove_image=itemView.findViewById(R.id.iv_remove_image);
            iv_image=itemView.findViewById(R.id.iv_uploaded_image);
            videoView=itemView.findViewById(R.id.videoView);

        }
    }


}