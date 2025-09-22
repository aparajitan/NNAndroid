package com.app_neighbrsnook.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app_neighbrsnook.R;

import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    // Context object
    Context context;

    // Array of images
    int[] images;
    Uri uri = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
    VideoView videoView;

    // Layout Inflater
    LayoutInflater mLayoutInflater;


    // Viewpager Constructor
    public ViewPagerAdapter(Context context, int[] images, Uri uri) {
        this.context = context;
        this.images = images;
        this.uri = uri;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ViewPagerAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View itemView = mLayoutInflater.inflate(R.layout.item, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView = itemView.findViewById(R.id.imageViewMain);
        videoView = itemView.findViewById(R.id.wall_image_imageview);
        // setting the image in the imageView
        imageView.setImageResource(images[position]);
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

        container.removeView((LinearLayout) object);
    }
}