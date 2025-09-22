package com.app_neighbrsnook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_neighbrsnook.adapter.AllCategoryListMarketPlace;
import com.app_neighbrsnook.adapter.PopularItemAdapter;
import com.app_neighbrsnook.adapter.TodaySellAdapter;
import com.app_neighbrsnook.adapter.WallChildAdapter;
import com.app_neighbrsnook.intreface.CategoryCallBack;
import com.app_neighbrsnook.model.PopularModel;
import com.app_neighbrsnook.model.wall.WallPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.CategoryMarketPlace;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListActivity extends AppCompatActivity implements CategoryCallBack,AllCategoryListMarketPlace.SellRequest, WallChildAdapter.ImageCallBack {
    RecyclerView item_list_rv;
    TodaySellAdapter todaySellAdapter;
    List<PopularModel> popularList = new ArrayList<>();
    TextView titleTv;
    ImageView back_btn,searchImg,addIconImg;
    CategoryCallBack ccb = this;
    WallPojo rootObject;
    AllCategoryListMarketPlace categoryAdapterMarketPlace;
    List<CategoryMarketPlace> listData = new ArrayList();

    SharedPrefsManager sm;
    Context context;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        sm = new SharedPrefsManager(this);
        context = activity = this;
        getMarketPlaceWallApi();

        Intent i = getIntent();


        item_list_rv = findViewById(R.id.item_list_rv);
        titleTv = findViewById(R.id.title);
        titleTv.setText("Categories");

        back_btn = findViewById(R.id.back_btn);
        searchImg = findViewById(R.id.search_imageview);
        addIconImg = findViewById(R.id.add_imageview);
        addIconImg.setVisibility(View.GONE);
        searchImg.setVisibility(View.GONE);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void categoryOnClick(int pos, String title) {
    /*    Intent i = new Intent(this, ItemListActivity.class);
        i.putExtra("title", title);
        startActivity(i);*/
    }
    private void getMarketPlaceWallApi() {
//        dialog.show();
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if(isInternetConnection) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("user_id", Integer.parseInt(sm.getString("user_id")));
            // hm.put("page", count);
            APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
            Call<WallPojo> call = service.categoryList();
            call.enqueue(new Callback<WallPojo>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {
                    rootObject = response.body();
                    if(rootObject.getStatus().equals("200")) {
                        Log.d("sdsfs",rootObject.getStatus());
                        //   rootObject.getToday_list();
                        listData = response.body().getCategory();
                        @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);
                        item_list_rv.setLayoutManager(linearLayoutManager);

                        item_list_rv.setLayoutManager(new GridLayoutManager(context, 4, RecyclerView.VERTICAL, false));
                        categoryAdapterMarketPlace = new AllCategoryListMarketPlace(listData, context,  CategoryListActivity.this, CategoryListActivity.this);
                        //uaha dekhna
                        item_list_rv.setAdapter(categoryAdapterMarketPlace);
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
}