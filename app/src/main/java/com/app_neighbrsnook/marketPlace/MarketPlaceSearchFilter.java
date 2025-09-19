package com.app_neighbrsnook.marketPlace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.CategoryListActivity;
import com.app_neighbrsnook.AllListing;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.AllDataWallShowAdapter;
import com.app_neighbrsnook.adapter.BusinessCategoryAdapter;
import com.app_neighbrsnook.adapter.CategoryAdapterMarketPlace;
import com.app_neighbrsnook.adapter.MarketPlaceFilterAdapter;
import com.app_neighbrsnook.adapter.LatestAdapter;
import com.app_neighbrsnook.adapter.WallChildAdapter;
import com.app_neighbrsnook.adapter.WishListMarketAdapter;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.MarketPlaceSearchFilterPojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.MarketPlaceWallPojo;
import com.app_neighbrsnook.SalesModuleMarketPlace.CreateSellActivity;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketPlaceSearchFilter extends AppCompatActivity implements  View.OnClickListener, BusinessCategoryAdapter.CategoryInterface, LatestAdapter.SellRequest,WallChildAdapter.ImageCallBack,
        CategoryAdapterMarketPlace.SellRequest, WishListMarketAdapter.SellRequest,AllDataWallShowAdapter.SellRequest,MarketPlaceFilterAdapter.SellRequest {
    TextView titleTv, item_category, sale_item_tv, tvWishList,free_item_tv, tvTodayListingPage,allListData,tvPopularCategory, tvMyProductList;
    ImageView searchImageView, cancel_iv,addImageView, back_btn;
    RecyclerView rv_sell,rvAllData;
    MarketPlaceFilterAdapter yourListMarketAdapter;
    //TodayCallBack tbc = this;
    Dialog mail_dialog;
    ProgressDialog dialog;
    RelativeLayout  search_rl;
    String stCatName,catTitile;

    SharedPrefsManager sm;
    MarketPlaceWallPojo rootObject;
    List<MarketPlaceSearchFilterPojo> yourItemsPojos = new ArrayList();
    RecyclerView mRecyclerView, rv_your_items,rv_category,rvWhishlist;
    Context context;
    Activity activity;
    RelativeLayout rlWishListHome,rlYourList,rlAllData,rlWishList;
    EditText search_et;
    int id;
    boolean isVerifiedUser = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_search_filte);
        sm = new SharedPrefsManager(this);
        context = activity = this;
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        catTitile =intent.getStringExtra("categorie");
      //  dobString = i.getStringExtra("dobb");
        Log.d("sfsfdsfs", String.valueOf(id));
        tvTodayListingPage = findViewById(R.id.view_all_today_listing);
        search_et = findViewById(R.id.search_et);
        rvAllData = findViewById(R.id.rvAllData);
        tvMyProductList = findViewById(R.id.myProductAll);
        rv_your_items = findViewById(R.id.rv_your_items);
        rv_category = findViewById(R.id.rv_category);
        titleTv = findViewById(R.id.title);
        back_btn = findViewById(R.id.back_btn);
        searchImageView = findViewById(R.id.search_imageview);
        addImageView = findViewById(R.id.add_imageview);
        searchImageView.setVisibility(View.VISIBLE);
        item_category = findViewById(R.id.item_category);
        sale_item_tv = findViewById(R.id.sale_item_tv);
        allListData = findViewById(R.id.allListData);
        free_item_tv = findViewById(R.id.free_item_tv);
        rlYourList = findViewById(R.id.rlYourList);
        rlAllData = findViewById(R.id.rlAllData);
        rlWishList = findViewById(R.id.rlWishList);
        item_category.setOnClickListener(this);
        cancel_iv = findViewById(R.id.cancel_iv);
        search_rl = findViewById(R.id.search_rl);
        search_rl.setVisibility(View.GONE);
        addImageView.setVisibility(View.GONE);
        search_rl.setVisibility(View.GONE );
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Search layout see layout
                search_rl.setVisibility(View.VISIBLE);

                // EditText
                search_et.requestFocus();

                // open the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(search_et, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(search_et, InputMethodManager.SHOW_IMPLICIT);
        @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        mRecyclerView =  findViewById(R.id.rv_sell);
        getMarketPlaceWallApi();
        EditText finalSearch_et = search_et;
        titleTv.setText(catTitile);
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                finalSearch_et.setFocusable(true);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());

            }
        });
        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_rl.setVisibility(View.GONE);
                search_et.setText("");
                search_et.setHint("Search...");
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
            }
        });

    }
    private void getMarketPlaceWallApi() {
//        dialog.show();
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if(isInternetConnection) {
            APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
            Call<MarketPlaceWallPojo> call = service.marketPlaceFilterfilters(Integer.parseInt(PrefMananger.GetLoginData(this).getId()),id);
            call.enqueue(new Callback<MarketPlaceWallPojo>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<MarketPlaceWallPojo> call, Response<MarketPlaceWallPojo> response) {
                    rootObject = response.body();

                    try {
                        if(rootObject.getStatus().equals(200) ) {
                            if (rootObject.getVerfiedMsg().equals("User Verification is completed!")) {
                                isVerifiedUser = true;
                            }
                            yourItemsPojos=response.body().getProducthomelist();
                            stCatName=response.body().getProducthomelist().get(0).getCatName();
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);
                            rv_your_items.setLayoutManager(linearLayoutManager);
                            rv_your_items.setLayoutManager(new GridLayoutManager(context,2,RecyclerView.VERTICAL, false));
                            yourListMarketAdapter = new MarketPlaceFilterAdapter(isVerifiedUser, yourItemsPojos, context, MarketPlaceSearchFilter.this, MarketPlaceSearchFilter.this);
                            rv_your_items.setAdapter(yourListMarketAdapter);
                            //titleTv.setText(stCatName);
                            rlYourList.setVisibility(View.VISIBLE);
                        }
                        else if (rootObject.getStatus()!=null){
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);
                            rv_your_items.setLayoutManager(linearLayoutManager);
                            rv_your_items.setLayoutManager(new GridLayoutManager(context,2,RecyclerView.VERTICAL, false));
                            yourListMarketAdapter = new MarketPlaceFilterAdapter(isVerifiedUser,yourItemsPojos, context, MarketPlaceSearchFilter.this, MarketPlaceSearchFilter.this);
                            rv_your_items.setAdapter(yourListMarketAdapter);
                        }
                    }catch (Exception e){

                    }
                    //dialog.dismiss();
                }
                @Override
                public void onFailure(Call<MarketPlaceWallPojo> call, Throwable t) {
                    //  dialog.dismiss();
                }
            });
        }else {
            GlobalMethods.getInstance(context).globalDialog(this, "     No internet connection."     );
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case  R.id.item_category:
                selectCategory();
                break;
            case   R.id.add_imageview:
                Intent i = new Intent(context, CreateSellActivity.class);
                i.putExtra("type","add");
                startActivity(i);
                break;
            case R.id.sale_item_tv:
                resetColor();
                sale_item_tv.setTextColor(getResources().getColor(R.color.white));
                sale_item_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_btn));
                break;

            case R.id.free_item_tv:
                resetColor();
                free_item_tv.setTextColor(getResources().getColor(R.color.white));
                free_item_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_btn));
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

    private void resetColor() {

        sale_item_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_white));
        free_item_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_white));
        sale_item_tv.setTextColor(getResources().getColor(R.color.text_color));
        free_item_tv.setTextColor(getResources().getColor(R.color.text_color));
    }

    @Override
    public void onClick(String categoryName, String id) {
        item_category.setText(categoryName);
        mail_dialog.cancel();
    }


    public void onClickViewAllPopular(int pos, String title) {
        Intent i = new Intent(context , CategoryListActivity.class);
        i.putExtra("title",title);
        startActivity(i);
    }
    @Override
    public void onClickViewAllTodayListing(int pos, String title) {
        Intent i = new Intent(context , AllListing.class);
        i.putExtra("title",title);
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
    private void filter(String text) {
        ArrayList<MarketPlaceSearchFilterPojo> filteredlist = new ArrayList<MarketPlaceSearchFilterPojo>();
        for (MarketPlaceSearchFilterPojo item : yourItemsPojos) {
            if ((item.getpTitle().toLowerCase().contains(text.toLowerCase()) ||
                    item.getpDescription().toLowerCase().contains(text.toLowerCase()))) {
                filteredlist.add(item);
            }

        }

        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            yourListMarketAdapter.filterList(filteredlist);
        }
    }
}