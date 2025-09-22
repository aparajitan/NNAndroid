package com.app_neighbrsnook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.libraries.slider.SliderViewAdapter;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderViewHolder> {
    private final int[] images;
    private final String[] text;
    public SliderAdapterExample(int[] images, String[] text) {
        this.images = images;
        this.text = text;
    }
    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item_layout,null);
        return new SliderViewHolder(view);
    }
    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        viewHolder.imageView.setImageResource(images[position]);
        viewHolder.textView.setText(text[position]);
    }
    @Override
    public int getCount() {
        return images.length;
    }
    public class SliderViewHolder extends ViewHolder {
        private final ImageView imageView;
        private final TextView textView;
        public SliderViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            textView=itemView.findViewById(R.id.textdescription);
        }
    }
}