package com.app_neighbrsnook.adapter;

import android.app.Dialog;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ImageSliderDialogAdapter extends RecyclerView.Adapter<ImageSliderDialogAdapter.ViewHolder> {
    Dialog dialog;
    String from;
    List<ImagePojo> imageList;
    ImageSliderDialogAdapter.ImageRequest imageRequest;
    public  ImageSliderDialogAdapter(Dialog _dialog, List<ImagePojo> imageList) {
        dialog = _dialog;
        this.imageList = imageList;
    }

    public  ImageSliderDialogAdapter(Dialog _dialog, List<ImagePojo> imageList, String from, int pos) {
        dialog = _dialog;
        this.imageList = imageList;
        this.from = from;
    }
    @NonNull
    @Override
    public ImageSliderDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_image_upload_3, parent, false);
        return new ImageSliderDialogAdapter.ViewHolder(view); }
    @Override
    public void onBindViewHolder(@NonNull ImageSliderDialogAdapter.ViewHolder holder, int position) {
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


//public class ImageSliderDialogAdapter extends RecyclerView.Adapter<ImageSliderDialogAdapter.ViewHolder> {
//    Dialog dialog;
//    String from;
//    List<ImagePojo> imageList;
//    ImageSliderDialogAdapter.ImageRequest imageRequest;
//
//    public ImageSliderDialogAdapter(Dialog _dialog, List<ImagePojo> imageList, String from, int pos) {
//        dialog = _dialog;
//        this.imageList = imageList;
//        this.from = from;
//    }
//
//    @NonNull
//    @Override
//    public ImageSliderDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_media_upload, parent, false);
//        return new ImageSliderDialogAdapter.ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ImageSliderDialogAdapter.ViewHolder holder, int position) {
//        ImagePojo item = imageList.get(position);
//
//        if (from != null && from.equals("detail")) {
//            holder.cardView.setVisibility(View.GONE);
//        }
//
//        if (item.getVideo() != null && !item.getVideo().isEmpty()) {
//
//            holder.imageView.setVisibility(View.GONE);
//            holder.videoView.setVisibility(View.VISIBLE);
//            holder.videoView.setVideoURI(Uri.parse(item.getVideo()));
//            holder.videoView.seekTo(1);
//
//            MediaController mediaController = new MediaController(holder.videoView.getContext());
//            mediaController.setAnchorView(holder.videoView);
//            holder.videoView.setMediaController(mediaController);
//
//        } else {
//            holder.videoView.setVisibility(View.GONE);
//            holder.imageView.setVisibility(View.VISIBLE);
//            Picasso.get().load(item.getImg()).fit().centerInside().into(holder.imageView);
//        }
//
//        holder.imageView.setOnClickListener(view -> {
//            // Handle image click
//            // imageRequest.onImageClick(position);
//        });
//
//        holder.imageTrash.setOnClickListener(view -> {
//            // Handle image remove click
//            // imageRequest.onCrossClick(position);
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return imageList.size();
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView, imageTrash;
//        VideoView videoView;
//        CardView cardView;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.iv_image);
//            imageTrash = itemView.findViewById(R.id.iv_trash);
//            videoView = itemView.findViewById(R.id.vv_video);
//            cardView = itemView.findViewById(R.id.card_view);
//        }
//    }
//
//    public interface ImageRequest {
//        void onImageClick(int pos);
//        void onCrossClick(int pos);
//    }
//}
