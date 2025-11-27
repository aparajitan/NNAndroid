package com.app_neighbrsnook.adapter;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.app_neighbrsnook.fragment.UriPagerFragment;

import java.util.ArrayList;

public class UriPagerAdapter extends FragmentStateAdapter {

    private final ArrayList<Uri> uriList;
    private final ArrayList<String> typeList;

    public UriPagerAdapter(@NonNull FragmentActivity fa, ArrayList<Uri> uris, ArrayList<String> types) {
        super(fa);
        this.uriList = uris;
        this.typeList = types;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return UriPagerFragment.newInstance(uriList.get(position), typeList.get(position));
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }
}

