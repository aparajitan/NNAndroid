package com.app_neighbrsnook.adapter;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.MarketPlaceDetailsImagePojo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MarketPlaceDetailsChildPhotoView extends RecyclerView.Adapter<MarketPlaceDetailsChildPhotoView.ViewHolder> {
    Dialog dialog;
    String from;
    List<ImagePojo> imageList = new ArrayList<>();
    MarketPlaceDetailsChildPhotoView.ImageRequest imageRequest;
    public MarketPlaceDetailsChildPhotoView(Dialog _dialog, List<ImagePojo> imageList) {
        dialog = _dialog;
        this.imageList = imageList;
    }

    public MarketPlaceDetailsChildPhotoView(Dialog _dialog, List<ImagePojo> imageList, String from, int pos) {
        dialog = _dialog;
        this.imageList = imageList;
        this.from = from;
    }
    @NonNull
    @Override
    public MarketPlaceDetailsChildPhotoView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_image_upload, parent, false);
        return new MarketPlaceDetailsChildPhotoView.ViewHolder(view); }
    @Override
    public void onBindViewHolder(@NonNull MarketPlaceDetailsChildPhotoView.ViewHolder holder, int position) {
//        holder.iv_uploaded_image.setImageURI(imageList.get(position).);
if(from.equals("detail"))
{
    holder.cardView.setVisibility(View.GONE);
}
        Picasso.get().load(imageList.get(position).getImg()).fit().centerInside().into(holder.iv_uploaded_image);
        holder.iv_uploaded_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                imageRequest.onImageClick(position);
            }
        });

        holder.iv_remove_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               imageRequest.onCrossClick(position);
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

            iv_uploaded_image= itemView.findViewById(R.id.iv_uploaded_image);
            iv_remove_image = itemView.findViewById(R.id.iv_remove_image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    public interface ImageRequest {
        void onImageClick(int pos);
        void onCrossClick(int pos);
    }

}
