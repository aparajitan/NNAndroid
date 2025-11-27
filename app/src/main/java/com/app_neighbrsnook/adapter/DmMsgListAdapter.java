package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.directmessagelist.Nbdatum;
import com.app_neighbrsnook.SalesModuleMarketPlace.SellerChatActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DmMsgListAdapter extends RecyclerView.Adapter<DmMsgListAdapter.ViewHolder> {

    List<Nbdatum> nearbyList = new ArrayList<>();
    Context mcon;
//    DmMsgListAdapter.UserCallBack ucb;
    public DmMsgListAdapter(List<Nbdatum> nearbyList) {
        this.nearbyList = nearbyList;
//        this.ucb = ucb;
    }

    @NonNull
    @Override
    public DmMsgListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_row, parent, false);
        mcon = parent.getContext();
        return new DmMsgListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DmMsgListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title_tv.setText(nearbyList.get(position).getUsername());
        try {
            Picasso.get().load(nearbyList.get(position).getUserpic()).into(holder.profile_imageview);
        }catch (Exception e)
        {

        }
        holder.desc_tv.setVisibility(View.VISIBLE);
        holder.desc_tv.setText(nearbyList.get(position).getDttime());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (mcon, SellerChatActivity.class);
                i.putExtra("eventId", Integer.parseInt(nearbyList.get(position).getId()));
                i.putExtra("userName", nearbyList.get(position).getUsername());
                i.putExtra("subject", " ");

                i.putExtra("pic", nearbyList.get(position).getUserpic());
                 mcon.startActivity(i);
            }
        });
        if (position == nearbyList.size() - 1) {
            holder.divider.setVisibility(View.GONE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);}
    }

    @Override
    public int getItemCount() {
        return nearbyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title_tv, desc_tv;
        LinearLayout root;
        CircleImageView profile_imageview;

        View divider;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            desc_tv = itemView.findViewById(R.id.desc_tv);
            root = itemView.findViewById(R.id.root);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            divider = itemView.findViewById(R.id.vLineChatList);
        }
    }


}
