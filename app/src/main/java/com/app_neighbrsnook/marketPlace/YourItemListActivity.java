package com.app_neighbrsnook.marketPlace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.app_neighbrsnook.adapter.WishListMarketAdapter;
import com.app_neighbrsnook.adapter.WishlistWall;
import com.app_neighbrsnook.adapter.YourItemSellAdapter;
import com.app_neighbrsnook.model.SellModel;
import com.app_neighbrsnook.model.wall.WallPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.WishListPojo;
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

public class YourItemListActivity extends AppCompatActivity implements YourItemSellAdapter.SellRequest, WishListMarketAdapter.SellRequest,WallChildAdapter.ImageCallBack {
    RecyclerView item_list_rv;
    TodaySellAdapter todaySellAdapter;
    List<SellModel> todayListModelList = new ArrayList<>();
    TextView titleTv;
    WallPojo rootObject;
    List<YourItemsPojo> listData = new ArrayList();
    List<WishListPojo> wishListPojos = new ArrayList();
    YourItemSellAdapter sellAdapter;
    WishlistWall wishlistWall;
    ImageView back_btn, search_imageview, add_imageview;

    SharedPrefsManager sm;
    Context context;
    Activity activity;
    Boolean checkSource = false;
    int fromuserid;
    String stWishlist;
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
        //titleTv.setText("My Items");
        stWishlist = i.getStringExtra("market_wishlist");
        fromuserid = i.getIntExtra("user_id", 0);
        checkSource = i.getBooleanExtra("checkSource", false);

// Null check before comparing stWishlist
        if (checkSource) {
            getMarketPlaceWallApiOthers();
            titleTv.setText("My Items");

        } else if ("to_wishlist".equals(stWishlist)) {  // Use safe comparison
            myWishListApi();
            titleTv.setText("Wish List ");

        } else {
            myItemsListApi();
            titleTv.setText("My Items");

        }
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        add_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent i = new Intent(YourItemListActivity.this, CreateSellActivity.class);
                i.putExtra("type","add");
                startActivity(i);*/
                if (isVerifiedUser) {
                    Intent i = new Intent(YourItemListActivity.this, CreateSellActivity.class);
                    i.putExtra("type", "add");
                    startActivity(i);
                } else {
                    GlobalMethods.getInstance(YourItemListActivity.this).globalDialog(YourItemListActivity.this, getString(R.string.unverified_msg));

                }
            }
        });
        item_list_rv.setLayoutManager(new GridLayoutManager(this,2, RecyclerView.VERTICAL, false));
        //arshitodaySellAdapter = new TodaySellAdapter(getTodayData(),this, tcb, "buyer");
        item_list_rv.setAdapter(todaySellAdapter);
    }
    private void myItemsListApi() {
//        dialog.show();
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if(isInternetConnection) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("user_id", Integer.parseInt(sm.getString("user_id")));
            // hm.put("page", count);
            APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
            Call<WallPojo> call = service.myProductList(Integer.parseInt(PrefMananger.GetLoginData(this).getId()));
            call.enqueue(new Callback<WallPojo>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {
                    rootObject = response.body();
                    if (rootObject.getVerfied_msg().equals("User Verification is completed!")) {
                        isVerifiedUser = true;
                    }
                 try {
                     if(rootObject.getStatus().equals("200")) {
                         //  if ((mList.get(position).getPostImages() != null && mList.get(position).getPostImages().size() != 0)) {

                      //   Log.d("sdsfs",rootObject.getStatus());
                         //   rootObject.getToday_list();
                         listData = response.body().getMyproductlist();
                         @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);
                         item_list_rv.setLayoutManager(linearLayoutManager);
                         item_list_rv.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
                         if(listData != null || !listData.isEmpty()){
                             sellAdapter = new YourItemSellAdapter(isVerifiedUser,listData, context, YourItemListActivity.this, YourItemListActivity.this);
                             item_list_rv.setAdapter(sellAdapter);

                         }
                     }
                 }catch (Exception e){

                 }

                }
                @Override
                public void onFailure(Call<WallPojo> call, Throwable t) {
                }
            });
        }else {
            GlobalMethods.getInstance(context).globalDialog(this, "     No internet connection."     );
        }

    }

    @Override
    public void onClickViewAllPopular(int pos, String title) {

    }

    @Override
    public void onClickViewAllTodayListing(int pos, String title) {

    }

    @Override
    public void onDetailPopular(int pos) {

    }

    @Override
    public void onDetailTodayListing(int pos) {
    }
    @Override
    public void onImageClick(List<ImagePojo> list, int pos) {

    }
    private void getMarketPlaceWallApiOthers() {
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if (isInternetConnection) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("user_id", fromuserid);
            // hm.put("page", count);
            APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
            Call<WallPojo> call = service.myProductList(fromuserid);
            call.enqueue(new Callback<WallPojo>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {
                    rootObject = response.body();
                    if (rootObject.getStatus().equals("200")) {
                        if (rootObject.getVerfied_msg().equals("User Verification is completed!")) {
                            isVerifiedUser = true;
                        }
                        listData = response.body().getMyproductlist();
                        @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);
                        item_list_rv.setLayoutManager(linearLayoutManager);
                        item_list_rv.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
                        sellAdapter = new YourItemSellAdapter(isVerifiedUser,listData, context, YourItemListActivity.this, YourItemListActivity.this);
                        item_list_rv.setAdapter(sellAdapter);
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<WallPojo> call, Throwable t) {

                }
            });
        } else {
            GlobalMethods.getInstance(context).globalDialog(this, "     No internet connection.");
        }

    }

    private void myWishListApi() {
        // Check internet connection
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if (isInternetConnection) {
            // Get user ID from shared preferences
            int userId = Integer.parseInt(PrefMananger.GetLoginData(this).getId());
            // Create Retrofit service
            APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);

            // Make API call
            Call<WallPojo> call = service.myWishList(userId);
            call.enqueue(new Callback<WallPojo>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {
                    rootObject = response.body();
                    try {
                        if (rootObject.getStatus().equals("success")) {
                            wishListPojos = response.body().getWishlist();
                            @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);
                            item_list_rv.setLayoutManager(linearLayoutManager);

                            item_list_rv.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
                            wishlistWall = new WishlistWall(wishListPojos, context, YourItemListActivity.this);
                            item_list_rv.setAdapter(wishlistWall);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<WallPojo> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            // Show no internet connection dialog
            GlobalMethods.getInstance(context).globalDialog(this, "No internet connection.");
        }
    }


}