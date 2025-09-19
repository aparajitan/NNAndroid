package com.app_neighbrsnook.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_neighbrsnook.CategoryListActivity;
import com.app_neighbrsnook.model.wall.WallPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.marketPlacePojo.MarketPlaceTodayListPojo;
import com.app_neighbrsnook.SalesModuleMarketPlace.CreateSellActivity;
import com.app_neighbrsnook.AllListing;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.BusinessCategoryAdapter;
import com.app_neighbrsnook.adapter.LatestAdapter;
import com.app_neighbrsnook.intreface.CategoryCallBack;
import com.app_neighbrsnook.intreface.TodayCallBack;
import com.app_neighbrsnook.model.PopularModel;
import com.app_neighbrsnook.model.SellParenetModel;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellFragment extends Fragment implements View.OnClickListener, BusinessCategoryAdapter.CategoryInterface, LatestAdapter.SellRequest , TodayCallBack, CategoryCallBack {
    TextView titleTv, item_category, sale_item_tv, free_item_tv;
    ImageView searchImageView, addImageView, back_btn;
    RecyclerView rv_sell;
    LatestAdapter latestAdapter;
   // List<MarketPlaceTodayListPojo> todayListModelList = new ArrayList<>();
    List<SellParenetModel> saleListModelList = new ArrayList<>();
    List<PopularModel> popularList = new ArrayList<>();
    TodayCallBack tbc = this;
    Dialog mail_dialog;
    ProgressDialog dialog;
    SharedPrefsManager sm;
    WallPojo rootObject;
     List<MarketPlaceTodayListPojo> listData = new ArrayList();
    RecyclerView mRecyclerView ;


    CategoryCallBack ccb= this;
    String[] days = {"Architeccture & Interiors","Automobile", "Bakery","Day Care", "Education",
            "Entertainment and Media", "Fruits & Vegetables","Garments","Entertainment and Media",
            "Fruits & Vegetables","Garments","Entertainment and Media", "Fruits & Vegetables","Garments",
            "Entertainment and Media", "Fruits & Vegetables","Garments","Entertainment and Media",
            "Fruits & Vegetables","Garments"};

    List<String> emailsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell, container, false);
        item(view);
        sm = new SharedPrefsManager(getActivity());

        titleTv.setText("Marketplace");
        emailsList = Arrays.asList(days);
        @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
         mRecyclerView =  view.findViewById(R.id.rv_sell);
/*
        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new WallAdapter(listdata, getActivity(), imageCallBack, ecb, WallFragment.this, reactionInterface, isUserVerified);
        mRecyclerView.setAdapter(adapter);

*/
        getMarketPlaceWallApi();
        return view;
    }
    private void item(@NonNull View  view) {
        titleTv = view.findViewById(R.id.title);
        back_btn = view.findViewById(R.id.back_btn);
        back_btn.setVisibility(View.GONE);
        searchImageView = view.findViewById(R.id.search_imageview);
        addImageView = view.findViewById(R.id.add_imageview);
        searchImageView.setVisibility(View.VISIBLE);
        addImageView.setVisibility(View.VISIBLE);
        item_category = view.findViewById(R.id.item_category);
        sale_item_tv = view.findViewById(R.id.sale_item_tv);
        free_item_tv = view.findViewById(R.id.free_item_tv);
        item_category.setOnClickListener(this);
        addImageView.setOnClickListener(this);
        sale_item_tv.setOnClickListener(this);
        free_item_tv.setOnClickListener(this);
    }

    private List<MarketPlaceTodayListPojo> getTodayData()
    {
     /*   todayListModelList.add(new SellModel(SellModel.TODAY_LIST,"Sudhanshu", "Ceramic antique flower Pot",
                "Noida 104","2.4 km",R.drawable.flower_pot, SellModel.TODAY_LIST, "1000", "JS 12"));
        todayListModelList.add(new SellModel(SellModel.TODAY_LIST,"Arsad", "Table", "Noida 102",
                "5 km",R.drawable.table_image, SellModel.TODAY_LIST, "2000(Fixed price)", "aaa"));
        todayListModelList.add(new SellModel(SellModel.TODAY_LIST,"Amit", "Chair", "Noida 16","2 km",R.drawable.chair_image, SellModel.TODAY_LIST, "3000", ""));
        todayListModelList.add(new SellModel(SellModel.TODAY_LIST,"Raj", "Watch", "Noida 1","10 km",R.drawable.watch_image, SellModel.TODAY_LIST,"1500", ""));
//        todayListModelList.add(new SellModel(SellModel.TODAY_LIST,"Sudhanshu", "Painting", "Noida 2","5 km",R.drawable.painting_image, SellModel.TODAY_LIST,"9000",""));
//        todayListModelList.add(new SellModel(SellModel.WISH_LIST,"Sudhanshu", "Flower Pot", "Noida 104","2.4 km",R.drawable.flower_pot, SellModel.WISH_LIST, "1000"));
//        todayListModelList.add(new SellModel(SellModel.WISH_LIST,"Arsad", "Table", "Noida 102","5 km",R.drawable.table_image, SellModel.WISH_LIST, "2000"));
//        todayListModelList.add(new SellModel(SellModel.WISH_LIST,"Amit", "Chair", "Noida 16","2 km",R.drawable.chair_image, SellModel.WISH_LIST, "3000"));

        return todayListModelList;*/
        return listData;

    }

    private List<SellParenetModel> getSaleData()
    {
        saleListModelList.add(new SellParenetModel("Today's Listing", SellParenetModel.TODAY_LIST));
        saleListModelList.add(new SellParenetModel("Popular Category", SellParenetModel.POPULAR_CAT));
        saleListModelList.add(new SellParenetModel("Wish List", SellParenetModel.WISH_LIST));
        saleListModelList.add(new SellParenetModel("Your Items", SellParenetModel.YOUR_ITEMS));
        return saleListModelList;
    }

    private List<PopularModel> getpopularData()
    {
        popularList.add(new PopularModel("Furniture",R.drawable.furniture, R.color.event));
      //  popularList.add(new PopularModel("Clothing",R.drawable.cloth, R.color.text_color));
        popularList.add(new PopularModel("Toy/Gift",R.drawable.gift, R.color.poll));
        popularList.add(new PopularModel("Books",R.drawable.book,R.color.them_color));
        return popularList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case  R.id.item_category:
                    selectCategory();
                break;
            case   R.id.add_imageview:
                Intent i = new Intent(getActivity(), CreateSellActivity.class);
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
        mail_dialog = new Dialog(getActivity());
        mail_dialog.setContentView(R.layout.open_category_list);
        rv = mail_dialog.findViewById(R.id.rv_category);
//        confirm = mail_dialog.findViewById(R.id.cross_imageview);
        cancel = mail_dialog.findViewById(R.id.cross_imageview);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
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
    public void onClickViewAllPopular(int pos, String title) {
        Intent i = new Intent(getActivity() , CategoryListActivity.class);
        i.putExtra("title",title);
        startActivity(i);
    }
    @Override
    public void onClickViewAllTodayListing(int pos, String title) {
        Intent i = new Intent(getActivity() , AllListing.class);
        i.putExtra("title",title);
        startActivity(i);
    }
    @Override
    public void onDetailPopular(int pos) {
    }
    @Override
    public void onDetailTodayListing(int pos) {
//        Intent i = new Intent(getActivity() , ItemListActivity.class);
//        startActivity(i);
    }

    private void resetColor() {

        sale_item_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_white));
        free_item_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_white));
        sale_item_tv.setTextColor(getResources().getColor(R.color.text_color));
        free_item_tv.setTextColor(getResources().getColor(R.color.text_color));
    }

    @Override
    public void onTodayItemClick(int str, String title) {
        /*Intent i = new Intent (getActivity(), SellDetailActivity.class);
        i.putExtra("name",getTodayData().get(str).getName());
        i.putExtra("item_name",getTodayData().get(str).getProduct_name());
        i.putExtra("distance",getTodayData().get(str).getDistance());
        i.putExtra("price",getTodayData().get(str).getPrice());
        i.putExtra("time",getTodayData().get(str).getTime());
        i.putExtra("brand",getTodayData().get(str).getBrand());
        i.putExtra("buyer","buyer");

        startActivity(i);
        */
    }

    @Override
    public void onSellerItemClick(int str, String title) {
        /*Intent i = new Intent (getActivity(), SellerActivity.class);
        i.putExtra("name",getTodayData().get(str).getName());
        i.putExtra("item_name",getTodayData().get(str).getProduct_name());
        i.putExtra("distance",getTodayData().get(str).getDistance());
        i.putExtra("price",getTodayData().get(str).getPrice());
        i.putExtra("time",getTodayData().get(str).getTime());
        i.putExtra("brand",getTodayData().get(str).getBrand());
        startActivity(i);
        */
    }

    @Override
    public void categoryOnClick(int pos, String title) {
        Intent i = new Intent (getActivity(), AllListing.class);
        i.putExtra("title",title);
        startActivity(i);
    }

    @Override
    public void onClick(String categoryName, String id) {
        item_category.setText(categoryName);
        mail_dialog.cancel();
    }

    private void getMarketPlaceWallApi() {
//        dialog.show();
        Boolean isInternetConnection = GlobalMethods.checkConnection(getActivity());
        if(isInternetConnection) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("user_id", Integer.parseInt(sm.getString("user_id")));
           // hm.put("page", count);
            APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
            Call<WallPojo> call = service.marketPlaceWall(Integer.parseInt(PrefMananger.GetLoginData(getContext()).getId()));
            call.enqueue(new Callback<WallPojo>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {
                    rootObject = response.body();
                    if(rootObject.getStatus().equals("200")) {
                        Log.d("sdsfs",rootObject.getStatus());
                     //   rootObject.getToday_list();
                        listData = response.body().getToday_list();
                        @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
                        mRecyclerView.setLayoutManager(linearLayoutManager);

                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),  RecyclerView.HORIZONTAL, false));
                       // sellAdapter = new SellAdapter(listData,getActivity(),SellFragment.this);
                        mRecyclerView.setAdapter(latestAdapter);

                        /*adapter = new EventListAdapter(listPojoNbdata1, EventAllListCurrentData.this,isVerifiedUser);
                        recy_my_events.setAdapter(adapter);
                        */

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

            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), "     No internet connection."     );

        }


    }


}