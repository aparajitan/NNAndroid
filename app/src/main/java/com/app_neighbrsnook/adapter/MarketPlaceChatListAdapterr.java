package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.marketPlace.MarketPlaceChatActivity;
import com.app_neighbrsnook.pojo.marketPlacePojo.ChatMarketPlacePojo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MarketPlaceChatListAdapterr extends RecyclerView.Adapter<MarketPlaceChatListAdapterr.ViewHolder> {

    List<ChatMarketPlacePojo> nearbyList = new ArrayList<>();
    Context mcon;
    NewRequest newRequest;



//    DmMsgListAdapter.UserCallBack ucb;
    public MarketPlaceChatListAdapterr(List<ChatMarketPlacePojo> nearbyList,NewRequest newRequest) {
        this.newRequest = newRequest;
        this.nearbyList = nearbyList;
//        this.ucb = ucb;
    }

    @NonNull
    @Override
    public MarketPlaceChatListAdapterr.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_row, parent, false);
        mcon = parent.getContext();
        return new MarketPlaceChatListAdapterr.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketPlaceChatListAdapterr.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ChatMarketPlacePojo businessModel = nearbyList.get(position);
        holder.title_tv.setText(nearbyList.get(position).getSenderName());
        holder.desc_tv.setText(nearbyList.get(position).getNeighborhood());
        if (position == nearbyList.size()-1) {
            holder.vLineChatList.setVisibility(View.GONE);
        }
        try {
            Picasso.get().load(nearbyList.get(position).getSenderUserpic()).into(holder.profile_imageview);
        }catch (Exception e)
        {

        }

      /*  holder.category_name_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRequest.onClickDetail(position);

            }
        });*/
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //newRequest.onClickDetail( businessModel.getSenderId(),nearbyList.get(position).getProductId());
                newRequest.onClickDetail(position);

                Intent i = new Intent (mcon, MarketPlaceChatActivity.class);
                i.putExtra("id",(nearbyList.get(position).getProductId()) );
                i.putExtra("sender_id", (nearbyList.get(position).getSenderId()));
                i.putExtra("userName", nearbyList.get(position).getSenderName());
                i.putExtra("pic", nearbyList.get(position).getSenderUserpic());
                 mcon.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nearbyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title_tv, desc_tv;
        LinearLayout root;
        RelativeLayout category_name_iv;
        View vLineChatList;
        CircleImageView profile_imageview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vLineChatList = itemView.findViewById(R.id.vLineChatList);
            title_tv = itemView.findViewById(R.id.title_tv);
            category_name_iv = itemView.findViewById(R.id.category_name_iv5);
            desc_tv = itemView.findViewById(R.id.desc_tv);
            root = itemView.findViewById(R.id.root);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
        }
    }

    public interface NewRequest{
        void onClickDetail(int position);
    }
}
