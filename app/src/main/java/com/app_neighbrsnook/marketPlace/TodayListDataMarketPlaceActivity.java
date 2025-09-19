package com.app_neighbrsnook.marketPlace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.TodaySellAdapter;
import com.app_neighbrsnook.adapter.WallChildAdapter;
import com.app_neighbrsnook.adapter.YourItemSellAdapter;
import com.app_neighbrsnook.model.wall.WallPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.YourItemsPojo;
import com.app_neighbrsnook.SalesModuleMarketPlace.CreateSellActivity;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayListDataMarketPlaceActivity extends AppCompatActivity implements YourItemSellAdapter.SellRequest,WallChildAdapter.ImageCallBack {
    RecyclerView item_list_rv;
    TodaySellAdapter todaySellAdapter;
    TextView titleTv;
    WallPojo rootObject;
    List<YourItemsPojo> listData = new ArrayList();
    YourItemSellAdapter sellAdapter;

    ImageView back_btn, search_imageview, add_imageview;
    SharedPrefsManager sm;
    Context context;
    Activity activity;
    boolean isVerifiedUser = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        sm = new SharedPrefsManager(this);
        context = activity = this;

        Intent i = getIntent();
        item_list_rv = findViewById(R.id.item_list_rv);
        titleTv = findViewById(R.id.title);
        add_imageview = findViewById(R.id.add_imageview);
        titleTv.setText(i.getStringExtra("title"));
        search_imageview = findViewById(R.id.search_imageview);
        search_imageview.setVisibility(View.VISIBLE);
       back_btn = findViewById(R.id.back_btn);
        add_imageview.setVisibility(View.VISIBLE);
        titleTv.setText("Latest Listings");
        getMarketPlaceWallApi();

        /*if(i.getStringExtra("title").equals("Your items"))
        {
            add_imageview.setVisibility(View.VISIBLE);
        }*/
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        add_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TodayListDataMarketPlaceActivity.this, CreateSellActivity.class);
                i.putExtra("type","add");
                startActivity(i);
            }
        });
        item_list_rv.setLayoutManager(new GridLayoutManager(this,2, RecyclerView.VERTICAL, false));
        item_list_rv.setAdapter(todaySellAdapter);
    }
    private void getMarketPlaceWallApi() {
//        dialog.show();
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if(isInternetConnection) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("user_id", Integer.parseInt(sm.getString("user_id")));
            // hm.put("page", count);
            APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
            Call<WallPojo> call = service.todayList(Integer.parseInt(PrefMananger.GetLoginData(this).getId()));
            call.enqueue(new Callback<WallPojo>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {
                    rootObject = response.body();
                    if(rootObject.getStatus().equals("200")) {
                        Log.d("sdsfs",rootObject.getStatus());
                        if (rootObject.getVerfied_msg().equals("User Verification is completed!")) {
                            isVerifiedUser = true;
                        }
                        //   rootObject.getToday_list();
                        listData = response.body().getProducttodaylist();
                        @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);
                        item_list_rv.setLayoutManager(linearLayoutManager);
                        item_list_rv.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
                        sellAdapter = new YourItemSellAdapter(isVerifiedUser, listData, context,  TodayListDataMarketPlaceActivity.this, TodayListDataMarketPlaceActivity.this);
                        //uaha dekhna
                        item_list_rv.setAdapter(sellAdapter);
                    }
                    else {
                    }
                    //dialog.dismiss();
                }
                @Override
                public void onFailure(Call<WallPojo> call, Throwable t) {
                    //  dialog.dismiss();
                }
            });
        }else {

            GlobalMethods.getInstance(context).globalDialog(this, "     No internet connection."     );
        }
    }
    @Override
    public void onDetailTodayListing(int pos) {
    }
    @Override
    public void onImageClick(List<ImagePojo> list, int pos) {

    }
}