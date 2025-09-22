package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.intreface.TodayCallBack;
import com.app_neighbrsnook.marketPlace.MarketPlace;
import com.app_neighbrsnook.marketPlace.MarketPlaceSearchFilter;
import com.app_neighbrsnook.model.PopularModel;
import com.app_neighbrsnook.model.SellModel;
import com.app_neighbrsnook.model.SellParenetModel;
import com.app_neighbrsnook.pojo.marketPlacePojo.CategoryMarketPlace;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AllCategoryListMarketPlace extends RecyclerView.Adapter<AllCategoryListMarketPlace.BankViewHolder> {
    private List<CategoryMarketPlace> listData ;
    WallChildAdapter.ImageCallBack icb;
    Context mcon;
    SellRequest sellRequest;

    public AllCategoryListMarketPlace(List<CategoryMarketPlace> listData, Context mcon, SellRequest sellRequest, WallChildAdapter.ImageCallBack icb) {
        this.listData = listData;
        this.mcon = mcon;
        this.sellRequest = sellRequest;
        this.activity = activity;

    }

    Activity activity;
    @NonNull
    @Override
    public BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_row,parent,false);
        mcon = parent.getContext();
        return new AllCategoryListMarketPlace.BankViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AllCategoryListMarketPlace.BankViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CategoryMarketPlace marketPlaceTodayListPojo =listData.get(position);
        holder.name.setText(marketPlaceTodayListPojo.getCatTitle());

        try {
            if (marketPlaceTodayListPojo.getCatIamge().isEmpty()) {
                holder.image.setImageResource(R.drawable.marketplace_white_background);
            } else{
                Picasso.get().load(marketPlaceTodayListPojo.getCatIamge()).fit().centerCrop().error(R.drawable.marketplace_white_background).into(holder.image);
            }
        }catch ( Exception e){

        }

        holder.rootFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailScreen = new Intent(mcon, MarketPlaceSearchFilter.class);
                detailScreen.putExtra("id",Integer.parseInt(String.valueOf(marketPlaceTodayListPojo.getId())));
                detailScreen.putExtra("categorie",marketPlaceTodayListPojo.getCatTitle());
                detailScreen.putExtra("type","detail");
                mcon.startActivity(detailScreen);
            }
        });

    }
    @Override
    public int getItemCount() {

        return listData.size();

    }


    @Override
    public int getItemViewType(int position) {
   /*     if (saleSellList != null) {
            SellParenetModel object = saleSellList.get(position);
            if (object != null) {
                return object.getType();
            }
        }*/
        return 0;
    }


    public class BankViewHolder extends RecyclerView.ViewHolder {

        TextView name, product_name;
        ImageView image;
        RelativeLayout root;
        CardView cardView;
        FrameLayout rootFrame;



        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            rootFrame = itemView.findViewById(R.id.rootFrm);

            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);
            root = itemView.findViewById(R.id.root);
        }

    }


    public interface SellRequest
    {
        void onClickViewAllPopular(int pos, String title);
        void onClickViewAllTodayListing(int pos, String title);
        void onDetailPopular(int pos);
        void onDetailTodayListing(int pos);
    }

}
