package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.intreface.TodayCallBack;
import com.app_neighbrsnook.model.SellModel;
import com.app_neighbrsnook.pojo.marketPlacePojo.MarketPlaceTodayListPojo;

import java.util.ArrayList;
import java.util.List;

public class TodaySellAdapter extends RecyclerView.Adapter<TodaySellAdapter.ViewHolder> {

    List<MarketPlaceTodayListPojo> nearbyList = new ArrayList<>();
    Context mcon;
    TodayCallBack tcb;
    String userType;

    public TodaySellAdapter(List<MarketPlaceTodayListPojo> nearbyList, Context mContext, TodayCallBack tcb) {
        this.nearbyList = nearbyList;
        this.mcon = mContext;
        this.tcb = tcb;
    }

    public TodaySellAdapter(List<MarketPlaceTodayListPojo> nearbyList, Context mContext, TodayCallBack tcb, String userType) {
        this.nearbyList = nearbyList;
        this.mcon = mContext;
        this.tcb = tcb;
        this.userType = userType;
    }

    @NonNull
    @Override
    public TodaySellAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_today_row, parent, false);
        mcon = parent.getContext();
        return new TodaySellAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodaySellAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.item_name.setText(nearbyList.get(position).getCatName());
            holder.nebhourhood_tv.setText(nearbyList.get(position).getCatName());
            holder.item_price.setText("\u20B9" + nearbyList.get(position).getCatName());
            holder.porduct_image.setBackgroundResource(nearbyList.get(position).getpQuantity());
          /*  holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(userType.equals("seller")) {
                        tcb.onSellerItemClick(position, nearbyList.get(position).getProduct_name());
                    }
                    else {
                        tcb.onTodayItemClick(position, nearbyList.get(position).getProduct_name() );
                    }
                }
            });*/


    }

    @Override
    public int getItemCount() {
        return nearbyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,item_name, item_price,product_location, nebhourhood_tv;
        ImageView porduct_image;
        RelativeLayout root;
        CardView cardView;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nebhourhood_tv = itemView.findViewById(R.id.nebhourhood_tv);
            product_location = itemView.findViewById(R.id.product_location);
            item_name = itemView.findViewById(R.id.item_name);
            item_price = itemView.findViewById(R.id.item_price);
            porduct_image = itemView.findViewById(R.id.porduct_image);
            root = itemView.findViewById(R.id.root);
        }
    }



}
