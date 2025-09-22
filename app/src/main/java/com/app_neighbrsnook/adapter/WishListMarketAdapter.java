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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.marketPlace.MarketPlace;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.marketPlacePojo.WishListPojo;
import com.app_neighbrsnook.SalesModuleMarketPlace.SellDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WishListMarketAdapter extends RecyclerView.Adapter<WishListMarketAdapter.BankViewHolder> {
    public static final int POPULAR_CAT = 1;
    public static final int TODAY_LIST = 2;
    public static final int WISH_LIST = 3;
    public static final int YOUR_ITEMS = 4;
    private List<WishListPojo> listData;
    MarketPlace sellFragment;
    Context mcon;
    SellRequest sellRequest;
    Activity activity;
    boolean isVerifiedUser;

    public WishListMarketAdapter(boolean isVerifiedUser, List<WishListPojo> listData, Context mcon, SellRequest sellRequest, WallChildAdapter.ImageCallBack icb) {
        this.isVerifiedUser = isVerifiedUser;
        this.listData = listData;
        this.sellFragment = sellFragment;
        this.mcon = mcon;
        this.sellRequest = sellRequest;
        this.activity = activity;
    }

    @NonNull
    @Override
    public BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_row, parent, false);
        mcon = parent.getContext();
        return new WishListMarketAdapter.BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListMarketAdapter.BankViewHolder holder, @SuppressLint("RecyclerView") int position) {
        WishListPojo marketPlaceTodayListPojo = listData.get(position);
        holder.item_name.setText(marketPlaceTodayListPojo.getpTitle());
        holder.time.setText(marketPlaceTodayListPojo.getCreatedTime());
        holder.nebhourhood_tv.setText(marketPlaceTodayListPojo.getNeighborhoodName());

        if (marketPlaceTodayListPojo.getpStatus().equals(2)) {
            holder.soldFrm.setVisibility(View.VISIBLE);
            if (marketPlaceTodayListPojo.getSaleType().equals("Donate")) {
                holder.soldTv.setText("GIVEN");
            } else {
                holder.soldTv.setText("SOLD");
            }
        } else if (marketPlaceTodayListPojo.getpStatus().equals(0)) {
            holder.soldFrm.setVisibility(View.GONE);
        } else if (marketPlaceTodayListPojo.getpStatus().equals(1)) {
            holder.soldFrm.setVisibility(View.GONE);
        }

        if (marketPlaceTodayListPojo.getSaleType().equals("Donate")) {
            holder.item_price.setText("Free");
        } else {
            holder.item_price.setText("Rs. " + marketPlaceTodayListPojo.getSalePrice());
        }

        if (marketPlaceTodayListPojo.getpImages().isEmpty()) {
            holder.porduct_image.setImageResource(R.drawable.marketplace_ppl);
        } else {
            Picasso.get().load(marketPlaceTodayListPojo.getpImages()).into(holder.porduct_image);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVerifiedUser) {
                    Intent detailScreen = new Intent(mcon, SellDetailActivity.class);
                    detailScreen.putExtra("id", Integer.parseInt(String.valueOf(marketPlaceTodayListPojo.getId())));
                    Log.d("sdfsdfsfs", String.valueOf(Integer.parseInt(String.valueOf(marketPlaceTodayListPojo.getId()))));
                    detailScreen.putExtra("type", "detail");
                    mcon.startActivity(detailScreen);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public class BankViewHolder extends RecyclerView.ViewHolder {
        TextView item_name, time, item_price, product_location, nebhourhood_tv, soldTv;
        ImageView porduct_image;
        LinearLayout root;
        ImageView saveFill;
        FrameLayout soldFrm;

        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            saveFill = itemView.findViewById(R.id.saveImag);
            soldFrm = itemView.findViewById(R.id.soldFrm);
            nebhourhood_tv = itemView.findViewById(R.id.nebhourhood_tv);
            product_location = itemView.findViewById(R.id.product_location);
            item_name = itemView.findViewById(R.id.item_name);
            item_price = itemView.findViewById(R.id.item_price);
            porduct_image = itemView.findViewById(R.id.porduct_image);
            root = itemView.findViewById(R.id.root);
            time = itemView.findViewById(R.id.post_time);
            soldTv = itemView.findViewById(R.id.soldTv);
        }
    }

    public interface SellRequest {
        void onClickViewAllPopular(int pos, String title);

        void onClickViewAllTodayListing(int pos, String title);

        void onDetailPopular(int pos);

        void onDetailTodayListing(int pos);
    }
}
