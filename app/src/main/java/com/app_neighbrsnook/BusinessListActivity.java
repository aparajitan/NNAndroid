package com.app_neighbrsnook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_neighbrsnook.adapter.BusinessListAdapter;
import com.app_neighbrsnook.model.BusinessListModel;

import java.util.ArrayList;
import java.util.List;

public class BusinessListActivity extends AppCompatActivity {
    RecyclerView business_rv;
    BusinessListAdapter businessListAdapter;
    List<BusinessListModel> businesList = new ArrayList<>();
    ImageView back_btn,searchImageView, addImageView;
    TextView titleTv, submit_tv, report_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);
        init();
    }

    public void init(){
        business_rv = findViewById(R.id.category_rv);
        RecyclerView.LayoutManager layoutManagerOffer = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        business_rv.setLayoutManager(layoutManagerOffer);
        business_rv.setAdapter(new BusinessListAdapter(getBusinessListData()));

        titleTv = findViewById(R.id.title);
        titleTv.setText("Near By");
        searchImageView = findViewById(R.id.search_imageview);
        addImageView = findViewById(R.id.add_imageview);
        searchImageView.setVisibility(View.GONE);
        addImageView.setVisibility(View.GONE);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private List<BusinessListModel> getBusinessListData()
    {
        businesList.add(new BusinessListModel(R.drawable.saloon_img,"1 km","Tony & guy" ,"3.2", "KhanMarket, New Delhi", ""));
        businesList.add(new BusinessListModel(R.drawable.spa_img,"2.5 kms","Pearson Hair Salon" ,"5.5", "Cannought place, New Delhi", ""));
        businesList.add(new BusinessListModel(R.drawable.restaurent_img,"4.5 kms","Salon Opal" ,"4.5", "Lajpat Nagar, New Delhi", ""));
        businesList.add(new BusinessListModel(R.drawable.spa_img,"3.5 kms","The Hive" ,"5.1", "South Ex, New Delhi", ""));
        businesList.add(new BusinessListModel(R.drawable.saloon_img,"2.5 kms","Dreams" ,"3.5", "Tilak Nagar, New Delhi", ""));

        return businesList;
    }
}