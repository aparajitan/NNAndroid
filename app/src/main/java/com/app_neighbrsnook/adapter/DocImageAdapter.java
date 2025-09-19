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
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DocImageAdapter extends RecyclerView.Adapter<DocImageAdapter.ViewHolder> {
    Dialog dialog;
    List<Uri> imageList = new ArrayList<>();
    DocRequest imageRequest;

    public DocImageAdapter(Dialog _dialog, ArrayList<Uri> imageList, DocRequest imageRequest) {
        dialog = _dialog;
        this.imageList = imageList;
        this.imageRequest = imageRequest;
    }

    @NonNull
    @Override
    public DocImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_image_upload, parent, false);
        return new DocImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocImageAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        holder.iv_uploaded_image.setImageURI(imageList.get(position).);

        Picasso.get().load(imageList.get(position)).fit().centerCrop()
                .into(holder.iv_uploaded_image);
        holder.iv_uploaded_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageRequest.onDocClick(position);
            }
        });
        holder.iv_remove_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageRequest.onDocCrossClick(position);
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_uploaded_image = itemView.findViewById(R.id.iv_uploaded_image);
            iv_remove_image = itemView.findViewById(R.id.iv_remove_image);
        }
    }

    public interface DocRequest {
        void onDocClick(int pos);

        void onDocCrossClick(int pos);
    }

}

