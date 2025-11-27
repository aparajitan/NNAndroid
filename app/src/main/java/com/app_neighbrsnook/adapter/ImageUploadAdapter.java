package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.app_neighbrsnook.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.ViewHolder> {
    Dialog dialog;
    List<Uri> imageList = new ArrayList<>();
    String from;
    ImageRequest imageRequest;
    public ImageUploadAdapter(Dialog dialog, List<Uri> imageList,  ImageRequest imageRequest,String from) {
        this.dialog = dialog;
        this.imageList = imageList;
        this.from = from;
        this.imageRequest = imageRequest;
    }
   /* public ImageUploadAdapter(Dialog _dialog, ArrayList<Uri> imageList, ImageRequest imageRequest, String from) {
        dialog = _dialog;
        this.imageList = imageList;
        this.imageRequest = imageRequest;
        this.from = from;
    }*/
    @NonNull
    @Override
    public ImageUploadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_image_upload_3, parent, false);
        return new ImageUploadAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ImageUploadAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        holder.iv_uploaded_image.setImageURI(imageList.get(position).);

        Picasso.get().load(imageList.get(position)).fit().centerInside()
                .into(holder.iv_uploaded_image);
        holder.iv_uploaded_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageRequest.onImageClick(position);
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(from.equals("img")) {
                    imageRequest.onCrossClick(position, from);
                }
                else {
                    imageRequest.onCrossClick1(position, from);
                }
                Log.d("pos---", String.valueOf(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_uploaded_image, iv_remove_image;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            iv_uploaded_image= itemView.findViewById(R.id.iv_uploaded_image);
            iv_remove_image = itemView.findViewById(R.id.iv_remove_image);
        }
    }

    public interface ImageRequest {
        void onImageClick(int pos);
        void onCrossClick(int pos, String from);
        void onCrossClick1(int pos, String from);
    }

}
