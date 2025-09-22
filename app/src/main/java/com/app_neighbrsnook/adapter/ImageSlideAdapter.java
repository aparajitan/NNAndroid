package com.app_neighbrsnook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageSlideAdapter extends RecyclerView.Adapter<ImageSlideAdapter.MyViewHolder> {
    List<ImagePojo> dataList;
    ImgRequest imgRequest;
    Context context;


    public ImageSlideAdapter(List<ImagePojo>  dataList,ImgRequest imgRequest ) {
        this.dataList = dataList;
        this.imgRequest = imgRequest;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_row, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(position + 1 +"/"+dataList.size());

        Picasso.get().load(dataList.get(position).getImg()).placeholder(R.drawable.ic_baseline_report_gmailerrorred_24)
                .into(holder.imageView);

        Picasso.get().load(dataList.get(position).getImg()).fit().centerInside()
                .into(holder.imageview1);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgRequest.onClickImg(dataList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        ImageView imageView,imageview1;
        FrameLayout root;
        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.image_count);
            imageView = view.findViewById(R.id.imageview);
            imageview1 = view.findViewById(R.id.imageview1);
            root = view.findViewById(R.id.root);
        }
    }

    public interface ImgRequest
    {
        void onClickImg(List<ImagePojo> img);
    }

}