package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.ImagePOJO;

import java.util.ArrayList;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ImageUploadHolder> {
    Activity activity;
    public static ArrayList<ImagePOJO> imageList;
    public PreviewAdapter(Activity activity, ArrayList<ImagePOJO> bitmapList) {
        this.activity = activity;
        imageList = bitmapList;
    }
    public interface ImageItemClick
    {
        void removeImage(int position);
    }
    @NonNull
    @Override
    public PreviewAdapter.ImageUploadHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(activity).inflate(R.layout.preview_row,viewGroup,false);
        return new PreviewAdapter.ImageUploadHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull PreviewAdapter.ImageUploadHolder holder, @SuppressLint("RecyclerView") final int i) {
        try {
                holder.iv_image.setImageBitmap(imageList.get(i).bitmap);
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
            iv_image=itemView.findViewById(R.id.image_view);

        }
    }

}