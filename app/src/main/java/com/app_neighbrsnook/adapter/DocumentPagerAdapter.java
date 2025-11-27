package com.app_neighbrsnook.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.app_neighbrsnook.fragment.DocumentPagerFragment;
import com.app_neighbrsnook.pojo.ImagePojo;

import java.util.List;

public class DocumentPagerAdapter extends FragmentStateAdapter {

    private final List<ImagePojo> documentList;

    public DocumentPagerAdapter(@NonNull FragmentActivity fa, List<ImagePojo> documentList) {
        super(fa);
        this.documentList = documentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return DocumentPagerFragment.newInstance(documentList.get(position).getImg());
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }
}
