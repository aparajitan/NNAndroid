package com.app_neighbrsnook.realStateModule;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.fragment.AddressFragment;
import com.app_neighbrsnook.fragment.BasicInfo;
import com.app_neighbrsnook.fragment.PhotosFragment;
import com.google.android.material.tabs.TabLayout;

public class PostPropertyRentSellPg extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener{
    FrameLayout frm_replace;
    ViewPager viewPager;
    public TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_property_rent_sell_pg);
        frm_replace=findViewById(R.id.frame_filter_id);
        viewPager=findViewById(R.id.filters_view_pager);
        tabLayout=findViewById(R.id.post_property_tab_layout);
       // tab_filters.addTab(tab_filters.newTab().setText("Basic Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Basic Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Address"));
        tabLayout.addTab(tabLayout.newTab().setText("Photos"));
        tabLayout.setOnTabSelectedListener(this);
        
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_filter_id, new BasicInfo()).commit();

        } else if (tab.getPosition() == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_filter_id, new AddressFragment()).commit();

        } else if (tab.getPosition() == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_filter_id, new PhotosFragment()).commit();
        }
    }
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }
    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}