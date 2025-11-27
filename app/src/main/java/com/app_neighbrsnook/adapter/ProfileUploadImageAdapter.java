package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.ImagePOJO;

import java.util.ArrayList;

public class ProfileUploadImageAdapter extends RecyclerView.Adapter<ProfileUploadImageAdapter.ImageUploadHolder> {
    Activity activity;
    ArrayList<ImagePOJO> imageList;
    ImageItemClick itemClick;


    public ProfileUploadImageAdapter(Activity activity, ArrayList<ImagePOJO> bitmapList, ImageItemClick imageItemClick) {
        this.activity = activity;
        this.imageList = bitmapList;
        this.itemClick=imageItemClick;
    }
    public interface ImageItemClick
    {
        void removeImage(int position);
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
            holder.iv_image.setImageBitmap(imageList.get(i).bitmap);
            holder.iv_remove_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.removeImage(i);
                }
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
        public ImageUploadHolder(@NonNull View itemView) {
            super(itemView);
            iv_remove_image=itemView.findViewById(R.id.iv_remove_image);
            iv_image=itemView.findViewById(R.id.iv_uploaded_image);

        }
    }
}
