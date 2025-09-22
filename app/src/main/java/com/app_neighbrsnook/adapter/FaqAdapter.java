package com.app_neighbrsnook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.faq.Faqdatum;

import java.util.ArrayList;
import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {

    List<Faqdatum> nearbyList = new ArrayList<>();
    Context mcon;
    boolean show= true;

    public FaqAdapter(List<Faqdatum> nearbyList) {
        this.nearbyList = nearbyList;
    }

    @NonNull
    @Override
    public FaqAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_row, parent, false);
        mcon = parent.getContext();
        return new FaqAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqAdapter.ViewHolder holder, int position) {
        holder.title_tv.setText(nearbyList.get(position).getQuestion());
        holder.desc_tv.setText(nearbyList.get(position).getAnswer());
        holder.down_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    holder.ans_layout.setVisibility(View.VISIBLE);
                    holder.up_iv.setVisibility(View.VISIBLE);
                    holder.down_iv.setVisibility(View.GONE);

            }
        });

        holder.up_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    holder.ans_layout.setVisibility(View.GONE);
                holder.down_iv.setVisibility(View.VISIBLE);
                holder.up_iv.setVisibility(View.GONE);

            }
        });
        holder.show_hide_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(show) {
                    holder.ans_layout.setVisibility(View.VISIBLE);
                    holder.up_iv.setVisibility(View.VISIBLE);
                    holder.down_iv.setVisibility(View.GONE);
                    show = false;
                }
                else if(!show)
                {
                    holder.ans_layout.setVisibility(View.GONE);
                    holder.down_iv.setVisibility(View.VISIBLE);
                    holder.up_iv.setVisibility(View.GONE);
                    show = true;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return nearbyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title_tv, desc_tv;
        LinearLayout root, ans_layout;
        ImageView down_iv, up_iv;
        RelativeLayout show_hide_rl;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.question_tv);
            desc_tv = itemView.findViewById(R.id.ans_tv);
            root = itemView.findViewById(R.id.root);
            down_iv = itemView.findViewById(R.id.down_iv);
            ans_layout = itemView.findViewById(R.id.ans_layout);
            up_iv = itemView.findViewById(R.id.up_iv);
            show_hide_rl = itemView.findViewById(R.id.show_hide_rl);

        }
    }

}