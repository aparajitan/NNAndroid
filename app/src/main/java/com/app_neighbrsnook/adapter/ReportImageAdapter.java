package com.app_neighbrsnook.adapter;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.app_neighbrsnook.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ReportImageAdapter extends RecyclerView.Adapter<ReportImageAdapter.ViewHolder> {
    Dialog dialog;
    String from;
    Context context;
    List<String> imageList = new ArrayList<>();
//    ReportImageAdapter.ImageRequest imageRequest;

    public  ReportImageAdapter(List<String> imageList) {
        this.imageList = imageList;

    }
    @NonNull
    @Override
    public ReportImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_image_row, parent, false);
        context = parent.getContext();
        return new ReportImageAdapter.ViewHolder(view); }
    @Override
    public void onBindViewHolder(@NonNull ReportImageAdapter.ViewHolder holder, int position) {
        Picasso.get().load(imageList.get(position)).fit().centerCrop().into(holder.image_view);
        Glide.with(context).load(imageList.get(position))
                .apply(bitmapTransform(new BlurTransformation(80)))
                .into(holder.imageview1);
        holder.count.setText(position + 1 + "/" + imageList.size());
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        ImageView image_view, imageview1;
        TextView count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_view = itemView.findViewById(R.id.image_view);
            count = itemView.findViewById(R.id.image_count);
            imageview1 = itemView.findViewById(R.id.imageview1);
        }
    }

//    public interface ImageRequest {
//        void onImageClick(int pos);
//        void onCrossClick(int pos);
//    }

}
