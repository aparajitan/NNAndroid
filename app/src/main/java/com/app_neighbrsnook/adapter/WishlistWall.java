package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.intreface.TodayCallBack;
import com.app_neighbrsnook.marketPlace.MarketPlace;
import com.app_neighbrsnook.model.PopularModel;
import com.app_neighbrsnook.model.SellModel;
import com.app_neighbrsnook.model.SellParenetModel;
import com.app_neighbrsnook.pojo.marketPlacePojo.WishListPojo;
import com.app_neighbrsnook.SalesModuleMarketPlace.SellDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WishlistWall extends RecyclerView.Adapter<WishlistWall.BankViewHolder> {
    public static final int POPULAR_CAT = 1;
    public static final int TODAY_LIST = 2;
    public static final int WISH_LIST = 3;
    public static final int YOUR_ITEMS = 4;
    private List mList;
    private List<WishListPojo> listData ;
    WallChildAdapter.ImageCallBack icb;


    MarketPlace sellFragment;
    Context mcon;

    public WishlistWall(List<WishListPojo> listData, Context mcon, WallChildAdapter.ImageCallBack icb) {
        this.listData = listData;
        this.sellFragment = sellFragment;
        this.mcon = mcon;
        this.activity = activity;

    }
    List<SellModel> todaySellList = new ArrayList<>();
    List<SellParenetModel> saleSellList = new ArrayList<>();
    List<PopularModel> popularList = new ArrayList<>();
    Activity activity;
    TodayCallBack tcb;
    @NonNull
    @Override
    public BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_row,parent,false);
        mcon = parent.getContext();
        return new WishlistWall.BankViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WishlistWall.BankViewHolder holder, @SuppressLint("RecyclerView") int position) {
        WishListPojo marketPlaceTodayListPojo =listData.get(position);
        holder.item_name.setText(marketPlaceTodayListPojo.getpTitle());
        holder.time.setText(marketPlaceTodayListPojo.getCreatedTime());
        holder.nebhourhood_tv.setText(marketPlaceTodayListPojo.getNeighborhoodName());
     //   holder.item_price.setText("Rs. "+marketPlaceTodayListPojo.getSalePrice());
        holder.saveImag.setVisibility(View.GONE);
        Integer pStatus = marketPlaceTodayListPojo.getpStatus();
        if (pStatus != null && pStatus.equals(2)) {
            holder.soldFrm.setVisibility(View.VISIBLE);
        } else if (pStatus != null && pStatus.equals(0)) {
            holder.soldFrm.setVisibility(View.GONE);
        } else if (pStatus != null && pStatus.equals(1)) {
            holder.soldFrm.setVisibility(View.GONE);
        } else {
            // Handle the null case if needed
            holder.soldFrm.setVisibility(View.GONE); // Default behavior
        }
        if (marketPlaceTodayListPojo.getpImages() != null && !marketPlaceTodayListPojo.getpImages().isEmpty()) {
            Picasso.get()
                    .load(marketPlaceTodayListPojo.getpImages())
                    .fit()
                    .centerCrop()
                    .error(R.drawable.marketplace_ppl)
                    .placeholder(R.drawable.marketplace_ppl)
                    .into(holder.porduct_image);
        } else {
            holder.porduct_image.setImageResource(R.drawable.marketplace_ppl);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailScreen = new Intent(mcon, SellDetailActivity.class);
                detailScreen.putExtra("id",Integer.parseInt(String.valueOf(marketPlaceTodayListPojo.getProductId())));
                detailScreen.putExtra("type","detail");
                mcon.startActivity(detailScreen);
            }
        });

        String salePrice = marketPlaceTodayListPojo.getSalePrice();
        if (salePrice != null && salePrice.equals("0")) {
            holder.item_price.setText("Free");
        } else if (salePrice != null) {
            holder.item_price.setText("Rs. " + salePrice);
        } else {
            holder.item_price.setText("Price not available"); // Default case for null value
        }


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

        TextView name,item_name,time, item_price,product_location, nebhourhood_tv;
        ImageView porduct_image,saveImag;
        LinearLayout root;
        CardView cardView;
        FrameLayout soldFrm;





        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            saveImag = itemView.findViewById(R.id.saveImag);

            soldFrm = itemView.findViewById(R.id.soldFrm);
            nebhourhood_tv = itemView.findViewById(R.id.nebhourhood_tv);
            product_location = itemView.findViewById(R.id.product_location);
            item_name = itemView.findViewById(R.id.item_name);
            item_price = itemView.findViewById(R.id.item_price);
            porduct_image = itemView.findViewById(R.id.porduct_image);
            //root = itemView.findViewById(R.id.root);
            time = itemView.findViewById(R.id.post_time);

        }

    }



}
