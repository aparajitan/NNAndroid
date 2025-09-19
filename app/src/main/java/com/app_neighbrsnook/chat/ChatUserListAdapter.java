package com.app_neighbrsnook.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;

import java.util.ArrayList;


public class ChatUserListAdapter extends RecyclerView.Adapter<ChatUserListAdapter.ViewHolder> {

    ArrayList<User> nearbyList;// = new ArrayList<>();
    Context mcon;
    Activity activity;
    UserCallBack ucb;
    public ChatUserListAdapter(ArrayList <User> nearbyList, UserCallBack ucb) {
        this.nearbyList = nearbyList;
        Log.d("position", nearbyList.toString());
        this.ucb = ucb;
    }

    @NonNull
    @Override
    public ChatUserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_row, parent, false);
        mcon = parent.getContext();
        return new ChatUserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.title_tv.setText(nearbyList.get(position).name);
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
