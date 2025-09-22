package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.PollListPojo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.BankViewHolder> {
    private List<PollListPojo> pollList;
    NewRequest newRequest;
    Context mcon;

    public PollAdapter(List<PollListPojo> lists, PollAdapter.NewRequest newRequest) {
        this.pollList = lists;
        this.newRequest = newRequest;
    }

    @NonNull
    @Override
    public PollAdapter.BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_row, parent, false);
        mcon = parent.getContext();
        return new PollAdapter.BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PollAdapter.BankViewHolder holder, @SuppressLint("RecyclerView") int position) {

        PollListPojo pollListPojo = pollList.get(position);
        holder.user_tv.setText(pollListPojo.getCreatedBy());
        holder.created_date_tv.setText(pollListPojo.getNeighborhood() + ", " + pollListPojo.getCreatedDate());
        holder.end_date_value_tv.setText(pollListPojo.getEndDate());
        holder.start_date_value_tv.setText(pollListPojo.getStartDate());
        holder.question_tv.setText(pollListPojo.getPollQues());
        holder.vote_count_tv.setText(pollListPojo.getTotalvote());

        if (pollListPojo.getIspollrunning().equals("1")) {
            if (pollListPojo.getIsvoted().equals("1")) {
                holder.voted_tv.setVisibility(View.GONE);
                holder.voted1_tv.setVisibility(View.VISIBLE);
            } else {
                holder.voted_tv.setVisibility(View.VISIBLE);
                holder.voted1_tv.setVisibility(View.GONE);
            }
        }

        try {
            Picasso.get().load(pollListPojo.getUserpic()).into(holder.profile_imageview);
            if (pollListPojo.getUserpic().isEmpty()) {
                holder.profile_imageview.setImageResource(R.drawable.profile);
            } else {
                Picasso.get().load(pollListPojo.getUserpic()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.profile_imageview);
            }
        } catch (Exception e) {
        }

        holder.profile_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRequest.onDetail(Integer.parseInt(pollListPojo.getUserid()));
            }
        });

        holder.threedot_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRequest.threeDot(position);
            }
        });

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRequest.onDetailPage(position, Integer.parseInt(pollListPojo.getpId()), pollListPojo.getIsvoted(), pollListPojo.getIspollrunning());
            }
        });
    }

    @Override
    public int getItemCount() {
        return pollList.size();
    }

    public class BankViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_imageview, threedot_imageview;
        TextView user_tv, created_date_tv, end_date_value_tv, question_tv, vote_count_tv, voted_tv, voted1_tv, start_date_value_tv;
        LinearLayout root, profile_ll;

        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            threedot_imageview = itemView.findViewById(R.id.threedot_imageview);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            user_tv = itemView.findViewById(R.id.user_tv);
            created_date_tv = itemView.findViewById(R.id.created_date_tv);
            end_date_value_tv = itemView.findViewById(R.id.end_date_value_tv);
            question_tv = itemView.findViewById(R.id.question_tv);
            vote_count_tv = itemView.findViewById(R.id.vote_count_tv);
            voted_tv = itemView.findViewById(R.id.voted_tv);
            root = itemView.findViewById(R.id.root);
            voted1_tv = itemView.findViewById(R.id.voted1_tv);
            start_date_value_tv = itemView.findViewById(R.id.start_date_value_tv);
            profile_ll = itemView.findViewById(R.id.profile_ll);
        }
    }

    public interface NewRequest {
        void onClick(int pos);

        void onDetail(int pos);

        void threeDot(int pos);

        void onDetailPage(int position, int pos, String isVoted, String ispollrunning);
    }

    public void filterList(ArrayList<PollListPojo> filterdNames) {
        this.pollList = filterdNames;
        notifyDataSetChanged();
    }
}
