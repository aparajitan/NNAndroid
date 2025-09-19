package com.app_neighbrsnook.adapter;

import static android.view.View.GONE;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.DetailsImagesArrayShowMlt;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageSlideAdapterEventMltpl extends RecyclerView.Adapter<ImageSlideAdapterEventMltpl.MyViewHolder> {
    List<ImagePojo> dataList;
    List<ImagePojo> imageList = new ArrayList<>();
    ImgRequest imgRequest;
    Context context;
    SharedPrefsManager sharedPrefsManager;
    public ImageSlideAdapterEventMltpl(List<ImagePojo> imageList) {
        this.imageList = imageList;
    }
    public ImageSlideAdapterEventMltpl(List<ImagePojo>  dataList, ImgRequest imgRequest ) {
        this.dataList = dataList;
        this.imgRequest = imgRequest;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_image_slider, parent, false);
        context = parent.getContext();
        if (sharedPrefsManager==null){
            sharedPrefsManager=new SharedPrefsManager(context);
        }
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(position + 1 +"/"+dataList.size());
        Picasso.get().load(dataList.get(position).getImg()).placeholder(R.drawable.ic_baseline_report_gmailerrorred_24)
                .into(holder.imageView);
        if (sharedPrefsManager.getString("user_id").equals(dataList.get(position).getUserid()) ||  dataList.get(position).isOwner()){
            holder.iv_remove_image.setVisibility(View.VISIBLE);

        }else {
            holder.iv_remove_image.setVisibility(GONE);

        }
        Log.d("user_id",sharedPrefsManager.getString("user_id"));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgRequest.onClickImgView(dataList);
            }
        });
        holder.iv_remove_image.setOnClickListener(view ->
                imgRequest.onImageRemove(dataList.get(position)));
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        ImageView imageView,iv_remove_image;
        FrameLayout root;
        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.image_count);
            iv_remove_image = view.findViewById(R.id.iv_remove_image);
            imageView = view.findViewById(R.id.imageview);
           // imageview1 = view.findViewById(R.id.imageview1);
            root = view.findViewById(R.id.root);
        }
    }

    public interface ImgRequest
    {
        void onClickImgSec(List<DetailsImagesArrayShowMlt> img);
        void onImageRemove(ImagePojo model);
        void onClickImgView(List<ImagePojo> img);

        }

}