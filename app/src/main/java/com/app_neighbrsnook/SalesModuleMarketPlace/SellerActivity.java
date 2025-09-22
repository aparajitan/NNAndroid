package com.app_neighbrsnook.SalesModuleMarketPlace;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.ViewAdapter;
import com.app_neighbrsnook.fragment.ImageSellerFragment;
import com.app_neighbrsnook.fragment.UserListBottomSheetFragment;
import com.app_neighbrsnook.intreface.ImageCallBackSellerDetail;
import com.app_neighbrsnook.model.SellModel;

import java.util.ArrayList;
import java.util.List;

public class SellerActivity extends AppCompatActivity implements ImageCallBackSellerDetail {
    TextView item_category, item_name,titleTv,item_price,time,buy_textview, question_tv, created_date_tv, item_discription, item_post_time, nebgourhood_tv, item_posted_by;
    ImageView profile_imageview,mail_iv, back_btn, share_imaeview, save_iv, search_imageview,add_imageview;
    RecyclerView similar_item_rv;
    List<SellModel> todayListModelList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    private View notificationBadge;
    ViewPager mViewPager;
    ViewAdapter mViewPagerAdapter;
    ImageSellerFragment imageSellerFragment;
    ImageCallBackSellerDetail icb= this;
    int[] images = {};
    Uri uri = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
    String name,price ;
           String desc= "A flowerpot, planter, planterette or plant pot, is a container in which flowers and other plants are cultivated and displayed. Historically, and still to a significant extent today, they are made from plain terracotta with no ceramic glaze, with a round shape, tapering inwards.";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        Intent i = getIntent();
        item_name = findViewById(R.id.item_name);
        item_price = findViewById(R.id.item_price);
        item_category = findViewById(R.id.item_category);
        profile_imageview = findViewById(R.id.profile_imageview);
        buy_textview = findViewById(R.id.buy_textview);
        created_date_tv = findViewById(R.id.created_date_tv);
        item_discription = findViewById(R.id.item_desc);
        created_date_tv = findViewById(R.id.created_date_tv);
        share_imaeview = findViewById(R.id.share_imageview);
        save_iv = findViewById(R.id.save_iv);
        mail_iv = findViewById(R.id.mail_iv);
        nebgourhood_tv = findViewById(R.id.nebgourhood_tv);
        item_posted_by = findViewById(R.id.item_posted_by);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mViewPager = findViewById(R.id.viewPagerMain);
        mViewPagerAdapter = new ViewAdapter(this, images, uri, icb);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(0);
        item_price.setText("\u20B9"+i.getStringExtra("price"));
//        item_name.setText(i.getStringExtra("distance"));
        item_name.setText(i.getStringExtra("price"));
        item_posted_by.setText("Sudhanshu kumar");
        nebgourhood_tv.setText("Noida sector 16" +","+ " 2 kms");

        item_name.setText(i.getStringExtra("item_name"));
        name = i.getStringExtra("item_name");
        price = i.getStringExtra("price");
        item_discription.setText(desc);
        question_tv = findViewById(R.id.question_tv);
        titleTv = findViewById(R.id.title);
        titleTv.setText("Your item");
        add_imageview = findViewById(R.id.add_imageview);
        add_imageview.setVisibility(VISIBLE);
        add_imageview.setPadding(5,5,5,5);

        add_imageview.setImageResource(R.drawable.edit_pancil);
        add_imageview.setBackgroundResource(R.drawable.circle_white_solid);
//

        search_imageview = findViewById(R.id.search_imageview);
        search_imageview.setVisibility(VISIBLE);
        search_imageview.setImageResource(R.drawable.ic_trash_icon);

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mail_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserListBottomSheetFragment emogiBottomSheetDialog = UserListBottomSheetFragment.newInstance(price, name);
                emogiBottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

            }
        });

        add_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerActivity.this, CreateSellActivity.class);
                i.putExtra("item_name",name);
                i.putExtra("distance","Noida sector 16" +","+ " 2 kms");
                i.putExtra("price",price);
                i.putExtra("desc",desc);
                i.putExtra("type","edit");
                startActivity(i);

            }
        });

    }

    @Override
    public void onImageClick(int pos) {
        imageSellerFragment = new ImageSellerFragment();
        Bundle args = new Bundle();
        args.putInt("YourKey", pos);
        imageSellerFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, imageSellerFragment).addToBackStack("image_s").commit();
        mViewPagerAdapter = new ViewAdapter(this, images, uri, icb);

    }
}