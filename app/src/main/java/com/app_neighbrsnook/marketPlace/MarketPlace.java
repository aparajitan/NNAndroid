package com.app_neighbrsnook.marketPlace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app_neighbrsnook.CategoryListActivity;
import com.app_neighbrsnook.AllListing;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.AllDataWallShowAdapter;
import com.app_neighbrsnook.adapter.BusinessCategoryAdapter;
import com.app_neighbrsnook.adapter.CategoryAdapterMarketPlace;
import com.app_neighbrsnook.adapter.LatestAdapter;
import com.app_neighbrsnook.adapter.WallChildAdapter;
import com.app_neighbrsnook.adapter.WishListMarketAdapter;
import com.app_neighbrsnook.adapter.YourListMarketAdapter;
import com.app_neighbrsnook.model.wall.WallPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.AllDataShowPojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.CategoryMarketPlace;
import com.app_neighbrsnook.pojo.marketPlacePojo.MarketPlaceTodayListPojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.WishListPojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.YourItemsPojo;
import com.app_neighbrsnook.SalesModuleMarketPlace.CreateSellActivity;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketPlace extends AppCompatActivity implements View.OnClickListener, BusinessCategoryAdapter.CategoryInterface, LatestAdapter.SellRequest, WallChildAdapter.ImageCallBack,
        CategoryAdapterMarketPlace.SellRequest, WishListMarketAdapter.SellRequest, YourListMarketAdapter.SellRequest, AllDataWallShowAdapter.SellRequest {
    TextView titleTv, item_category, tvWishList, tvTodayListingPage, allListData, tvPopularCategory, tvMyProductList;
    ImageView searchImageView, addImageView, back_btn;
    RecyclerView rvAllData;
    LatestAdapter todayListingAdapter;
    CategoryAdapterMarketPlace categoryAdapter;
    WishListMarketAdapter wishListAdapter;
    YourListMarketAdapter yourListMarketAdapter;
    AllDataWallShowAdapter allDataWallShowAdapter;
    Dialog mail_dialog;
    SharedPrefsManager sm;
    WallPojo rootObject;
    List<MarketPlaceTodayListPojo> listData = new ArrayList();
    List<AllDataShowPojo> allDataWall = new ArrayList();
    List<CategoryMarketPlace> categoryListdata = new ArrayList();
    List<WishListPojo> wishListPojo = new ArrayList();
    List<YourItemsPojo> yourItemsPojos = new ArrayList();
    RecyclerView mRecyclerView, rv_your_items, rvPopularCategory, rvWhishlist;
    Context context;
    Activity activity;
    RelativeLayout rlWishListHome, rlYourList, rlAllData, rlWishList;
    boolean isVerifiedUser = true;
    LinearLayout lnrMarketPlaceParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place);

        sm = new SharedPrefsManager(this);
        context = activity = this;

        item();

        searchImageView.setVisibility(View.VISIBLE);
        addImageView.setVisibility(View.VISIBLE);
        titleTv.setText("Market");

        getMarketPlaceWallApi();

        item_category.setOnClickListener(this);
        addImageView.setOnClickListener(this);
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MarketPlace.this, MarketPlaceSearchFilter.class));
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvTodayListingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MarketPlace.this, TodayListDataMarketPlaceActivity.class));
            }
        });
        allListData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MarketPlace.this, AllListing.class));
            }
        });
        tvPopularCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MarketPlace.this, CategoryListActivity.class));
            }
        });
        tvWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //api lagani hai wishlist ki abhi
                Intent intent = new Intent(MarketPlace.this, YourItemListActivity.class);
                intent.putExtra("market_wishlist", "to_wishlist");
                startActivity(intent);
            }
        });
        tvMyProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MarketPlace.this, YourItemListActivity.class));
            }
        });
    }

    private void item() {
        tvPopularCategory = findViewById(R.id.tvPopularCategory);
        tvTodayListingPage = findViewById(R.id.view_all_today_listing);
        rvAllData = findViewById(R.id.rvAllData);
        rlWishListHome = findViewById(R.id.rlWishListHome);
        tvMyProductList = findViewById(R.id.myProductAll);
        rv_your_items = findViewById(R.id.rv_your_items);
        rvWhishlist = findViewById(R.id.rvWhishlist);
        rvPopularCategory = findViewById(R.id.rv_category);
        titleTv = findViewById(R.id.title);
        lnrMarketPlaceParent = findViewById(R.id.lnrMarketPlaceParent);
        back_btn = findViewById(R.id.back_btn);
        tvWishList = findViewById(R.id.tvWishList);
        searchImageView = findViewById(R.id.search_imageview);
        addImageView = findViewById(R.id.add_imageview);
        item_category = findViewById(R.id.item_category);
        allListData = findViewById(R.id.allListData);
        mRecyclerView = findViewById(R.id.rv_sell);
        rlYourList = findViewById(R.id.rlYourList);
        rlAllData = findViewById(R.id.rlAllData);
        rlWishList = findViewById(R.id.rlWishList);
    }

    private void getMarketPlaceWallApi() {
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if (isInternetConnection) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("user_id", Integer.parseInt(sm.getString("user_id")));
            APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
            Call<WallPojo> call = service.marketPlaceWall(Integer.parseInt(sm.getString("user_id")));
            call.enqueue(new Callback<WallPojo>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {
                    rootObject = response.body();
                    if (rootObject.getStatus().equals("200")) {
                        if (response.body().getVerfied_msg().equals("User Verification is not completed!")) {
                            isVerifiedUser = false;
                        }
                        lnrMarketPlaceParent.setVisibility(View.VISIBLE);

                        listData = response.body().getToday_list();
                        categoryListdata = response.body().getCategories();
                        wishListPojo = response.body().getWishlist();
                        yourItemsPojos = response.body().getYour_items();
                        allDataWall = response.body().getAll_products_list();

                        @SuppressLint("WrongConstant")
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);

                        if (listData.isEmpty()) {
                            rlWishListHome.setVisibility(View.GONE);
                        }
                        if (yourItemsPojos.isEmpty()) {
                            rlYourList.setVisibility(View.GONE);

                        }
                        if (allDataWall.isEmpty()) {
                            rlAllData.setVisibility(View.GONE);

                        }
                        if (wishListPojo.isEmpty()) {
                            rlWishList.setVisibility(View.GONE);
                        }

                        rv_your_items.setLayoutManager(linearLayoutManager);
                        rv_your_items.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                        yourListMarketAdapter = new YourListMarketAdapter(isVerifiedUser,yourItemsPojos, context, MarketPlace.this, MarketPlace.this);
                        rv_your_items.setAdapter(yourListMarketAdapter);

                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                        todayListingAdapter = new LatestAdapter(isVerifiedUser, listData, context, MarketPlace.this, MarketPlace.this);
                        mRecyclerView.setAdapter(todayListingAdapter);

                        rvAllData.setLayoutManager(linearLayoutManager);
                        rvAllData.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                        allDataWallShowAdapter = new AllDataWallShowAdapter(isVerifiedUser, allDataWall, context, MarketPlace.this, MarketPlace.this);
                        rvAllData.setAdapter(allDataWallShowAdapter);

                        rvWhishlist.setLayoutManager(linearLayoutManager);
                        rvWhishlist.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                        wishListAdapter = new WishListMarketAdapter(isVerifiedUser, wishListPojo, context, MarketPlace.this, MarketPlace.this);
                        rvWhishlist.setAdapter(wishListAdapter);

                        rvPopularCategory.setLayoutManager(linearLayoutManager);
                        rvPopularCategory.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                        categoryAdapter = new CategoryAdapterMarketPlace(categoryListdata, context, MarketPlace.this, MarketPlace.this);
                        rvPopularCategory.setAdapter(categoryAdapter);

                    } else {
                        lnrMarketPlaceParent.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<WallPojo> call, Throwable t) {
                    //  dialog.dismiss();
                }
            });
        } else {
            GlobalMethods.getInstance(context).globalDialog(this, "     No internet connection.");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_category:
                selectCategory();
                break;
            case R.id.add_imageview:
                if (isVerifiedUser) {
                    Intent i = new Intent(context, CreateSellActivity.class);
                    i.putExtra("type", "add");
                    startActivity(i);
                } else {
                    GlobalMethods.getInstance(MarketPlace.this).globalDialog(MarketPlace.this, getString(R.string.unverified_msg));
                }
                break;
        }

    }

    private void selectCategory() {
        RecyclerView rv;
        ImageView cancel;
        mail_dialog = new Dialog(context);
        mail_dialog.setContentView(R.layout.open_category_list);
        rv = mail_dialog.findViewById(R.id.rv_category);
//        confirm = mail_dialog.findViewById(R.id.cross_imageview);
        cancel = mail_dialog.findViewById(R.id.cross_imageview);

        rv.setLayoutManager(new LinearLayoutManager(context));
//        BusinessCategoryAdapter emailListAdapter = new BusinessCategoryAdapter(mail_dialog, this, emailsList);
//        rv.setAdapter(emailListAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_dialog.cancel();
            }
        });
        mail_dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
        mail_dialog.setCancelable(true);
        mail_dialog.show();
    }

    @Override
    public void onClick(String categoryName, String id) {
        item_category.setText(categoryName);
        mail_dialog.cancel();
    }

    public void onClickViewAllPopular(int pos, String title) {
        Intent i = new Intent(context, CategoryListActivity.class);
        i.putExtra("title", title);
        startActivity(i);
    }

    @Override
    public void onClickViewAllTodayListing(int pos, String title) {
        Intent i = new Intent(context, AllListing.class);
        i.putExtra("title", title);
        startActivity(i);
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

    @Override
    protected void onResume() {
        super.onResume();
        getMarketPlaceWallApi();
    }
}