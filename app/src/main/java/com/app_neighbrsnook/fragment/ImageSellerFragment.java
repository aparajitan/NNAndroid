package com.app_neighbrsnook.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.ViewPagerAdapter;

public class ImageSellerFragment extends Fragment {
    ImageView ic_back,cross_imageview;
    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;
    int[] images = {};
    Uri uri = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_seller, container, false);
        int value = getArguments().getInt("YourKey");
        ic_back = view.findViewById(R.id.cross_imageview);
        mViewPager = view.findViewById(R.id.viewPagerMain);
        mViewPagerAdapter = new ViewPagerAdapter(getActivity(), images, uri);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(value);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }

        });

        return view;
    }
}