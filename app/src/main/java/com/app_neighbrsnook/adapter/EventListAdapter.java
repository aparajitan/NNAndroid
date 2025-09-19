package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.event.UserViewEvent;
import com.app_neighbrsnook.nearBy.NearByBusinessActivity;
import com.app_neighbrsnook.pojo.EventListPojoNbdata;
import com.app_neighbrsnook.pojo.GroupListPojo;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.BankViewHolder> {
    private List<EventListPojoNbdata> groupListPojos;
    NewRequest newRequest;
    Context mcon;

    boolean isVerifiedUser = true;

    int id;
    public EventListAdapter(List<EventListPojoNbdata> lists, NewRequest newRequest,boolean isVerifiedUser) {
        this.groupListPojos = lists;
        this.newRequest = newRequest;
        this.isVerifiedUser = isVerifiedUser;
    }
    @NonNull
    @Override
    public EventListAdapter.BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_layout,parent,false);
        mcon = parent.getContext();
        return new EventListAdapter.BankViewHolder(view);
    }
    public void updateList(List<EventListPojoNbdata> arrayList){
        groupListPojos=arrayList;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.BankViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EventListPojoNbdata businessModel = groupListPojos.get(position);
        holder.tv_title.setText(businessModel.getTitle());
        if (businessModel.getCoverImage().isEmpty()) {
            holder.img_event.setImageResource(R.drawable.event_for_app);
        } else{
           // Picasso.get().load(businessModel.getCoverImage()).fit().centerCrop().into(holder.img_event);
            Glide.with(mcon)
                    .load(businessModel.getCoverImage())
                    .apply(RequestOptions.centerCropTransform())
                    .into(holder.img_event);
        }
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVerifiedUser){
                    Intent detailScreen = new Intent(mcon, UserViewEvent.class);
                    detailScreen.putExtra("data",Integer.parseInt(groupListPojos.get(position).getId()));
                    Log.d("id", groupListPojos.get(position).getId());
                    mcon.startActivity(detailScreen);
                }else {
                    GlobalMethods.getInstance(mcon).globalDialog(mcon, mcon.getString(R.string.unverified_msg));

                }
            //}
            }
        });

    }
    @Override
    public int getItemCount() {
        return groupListPojos.size();
    }
    public class BankViewHolder extends RecyclerView.ViewHolder{
        LinearLayout root;
        TextView tv_title;
        int id;
        ImageView img_event;

        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.lnr_root);
            tv_title = itemView.findViewById(R.id.tv_tittle_event);
            img_event = itemView.findViewById(R.id.img_event_list);
            Intent i = new Intent(mcon, NearByBusinessActivity.class);
            id = i.getIntExtra("id",0);

        }
    }
    public interface NewRequest{


    }
    public void filterList(ArrayList<EventListPojoNbdata> filterdNames) {
        this.groupListPojos = filterdNames;
        notifyDataSetChanged();
    }


}
