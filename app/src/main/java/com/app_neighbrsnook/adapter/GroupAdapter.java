package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.GroupModelP;

import java.util.List;
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.BankViewHolder> {
    private final List<GroupModelP> groupList;
    GroupAdapter.NewRequest newRequest;
    public GroupAdapter(List<GroupModelP> lists, NewRequest newRequest) {
        this.groupList = lists;
        this.newRequest = newRequest;
    }
    @NonNull
    @Override
    public GroupAdapter.BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.group_adapter_layout,parent,false);
        return new GroupAdapter.BankViewHolder(view);
    }
    @Override
    public void onBindViewHolder(GroupAdapter.BankViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GroupModelP groupModelP = groupList.get(position);
//        holder.profile_imageview.setImageURI(PollModel.getB_name());
       // holder.user_tv.setText("Sudhanshu");
        holder.tv_amar.setText(groupModelP.getGroup_title());
        holder.tv_about.setText(groupModelP.getAbout_group());
        holder.lnr_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRequest.onDetailPage(position);
            }
        });
/*
        holder.voted_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                newRequest.onClick(position);
            }
        });

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRequest.onDetailPage(position);
            }
        });
*/
    }
    @Override
    public int getItemCount() {
        return groupList.size();
    }
    public class BankViewHolder extends RecyclerView.ViewHolder{
        TextView tv_amar,tv_about,tv_demo;
        LinearLayout lnr_root;
        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_amar=itemView.findViewById(R.id.amar_dubey);
            tv_about=itemView.findViewById(R.id.about);
            lnr_root=itemView.findViewById(R.id.lnr_root);
           // tv_demo=itemView.findViewById(R.id.member_demo);
        }
    }
    public interface NewRequest{
        void onClick(int pos);
        void onDetail(int pos);
        void threeDot(int pos);
        void onDetailPage(int pos);
    }
}
