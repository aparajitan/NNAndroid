package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.notification.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class SellerChatListAdapter extends RecyclerView.Adapter<SellerChatListAdapter.ViewHolder> {

    List<NotificationModel> nearbyList = new ArrayList<>();
    Context mcon;
    Activity activity;
    SellerChatListAdapter.UserCallBack ucb;
    public SellerChatListAdapter(List<NotificationModel> nearbyList, UserCallBack ucb) {
        this.nearbyList = nearbyList;
        this.ucb = ucb;
    }

    @NonNull
    @Override
    public SellerChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_chat_user_row, parent, false);
        mcon = parent.getContext();
        return new SellerChatListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerChatListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title_tv.setText(nearbyList.get(position).getTitle());

        holder.desc_tv.setText(nearbyList.get(position).getDesc());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ucb.onUserClick(position);
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            desc_tv = itemView.findViewById(R.id.desc_tv);
            root = itemView.findViewById(R.id.root);
        }
    }

    public interface UserCallBack{
        void onUserClick(int pos);
    }

}
