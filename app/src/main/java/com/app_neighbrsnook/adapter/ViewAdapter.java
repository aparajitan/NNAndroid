package com.app_neighbrsnook.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.intreface.ImageCallBackSellerDetail;
import com.app_neighbrsnook.model.ImagePOJO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewAdapter extends PagerAdapter {

    Context context;
    int[] images;
    Uri uri = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
    VideoView videoView;
    LayoutInflater mLayoutInflater;
    List<ImagePOJO> imagList = new ArrayList<>();
    ImageCallBackSellerDetail icb;


    // Viewpager Constructor
    public ViewAdapter(Context context, int[] images, Uri uri, ImageCallBackSellerDetail icb) {
        this.context = context;
        this.images = images;
        this.uri = uri;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.icb = icb;
    }

    public ViewAdapter(Context context, int[] images, ImageCallBackSellerDetail icb) {
        this.context = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.icb = icb;
    }



    @Override
    public int getCount() {
        // return the number of images
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.item_viewpager, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView = itemView.findViewById(R.id.image_view);
//        videoView = itemView.findViewById(R.id.wall_image_imageview);
        // setting the image in the imageView
        imageView.setImageResource(images[position]);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icb.onImageClick(position);
            }
        });
//        if(images[position]+1 == images.length)
//        {
//            final MediaController[] mediacontroller = {new MediaController(imageView.getContext())};
//            mediacontroller[0].setAnchorView(videoView);
//            videoView.requestFocus();
//            Uri uri = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
//            videoView.setVideoURI(uri);
//            videoView.start();
//        }

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((ConstraintLayout) object);
    }
}