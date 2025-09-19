package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.pollDetailResponce.Option;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.List;

public class VoteAdapter extends Adapter<VoteAdapter.BankViewHolder> {

    List<Option> optionList = new ArrayList<>();
        NewRequest newRequest;
        int questionQuantity;
        double total = 0;
        double per;
        String isVoted, userid;
        private int checkedPosition = 0;
        SharedPrefsManager sm;
        int pos;

        public VoteAdapter(List<Option> lists, String isVoted, NewRequest newRequest) {
        this.optionList = lists;
        this.isVoted = isVoted;
        this.newRequest = newRequest;
//        this.userid = userid;

        }


    @NonNull
    @Override
    public BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_row,parent,false);
        return new BankViewHolder(view);
    }




    @Override
    public void onBindViewHolder(BankViewHolder holder, @SuppressLint("RecyclerView") int position) {
//    questionQuantity = optionList.size();
    if(isVoted.equals("1"))
    {
        holder.option_tv1.setText(optionList.get(position).getOption());
        holder.progress_bar.setProgress(Math.round(Float.parseFloat(optionList.get(position).getPercentage())));
        holder.percentage_tv.setText(optionList.get(position).getPercentage()+ " %");
        holder.option.setVisibility(View.GONE);
        holder.voting_result_rl.setVisibility(View.VISIBLE);

    }
    else {
        holder.option_tv.setText(optionList.get(position).getOption());
        holder.option.setVisibility(View.VISIBLE);
        holder.voting_result_rl.setVisibility(View.GONE);
        holder.bind(optionList.get(position));
    }
    }

@Override
public int getItemCount()
    {
    return optionList.size();
    }

public class BankViewHolder extends RecyclerView.ViewHolder {
    ImageView profile_imageview, threedot_imageview;
    TextView option_tv, percentage_tv, option_tv1;
    RelativeLayout root, option, voting_result_rl, rl;
    LinearLayout ll;
    ProgressBar progress_bar;


    public BankViewHolder(@NonNull View itemView) {
        super(itemView);
        option_tv = itemView.findViewById(R.id.option_tv);
        percentage_tv = itemView.findViewById(R.id.percentage_tv);
        root = itemView.findViewById(R.id.root);
        progress_bar = itemView.findViewById(R.id.progress_bar);
        option = itemView.findViewById(R.id.option);
        option_tv1 = itemView.findViewById(R.id.option_tv1);
        voting_result_rl = itemView.findViewById(R.id.voting_result_rl);
        ll = itemView.findViewById(R.id.ll);
    }
        void bind(Option option){
            if (checkedPosition == -1) {
                root.setBackgroundResource(R.drawable.round_corner_vote);
                option_tv.setTextColor(Color.parseColor("#636262"));
            } else {
                if (checkedPosition == getAdapterPosition()) {

                } else {
                    root.setBackgroundResource(R.drawable.round_corner_vote);
                    option_tv.setTextColor(Color.parseColor("#636262"));
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newRequest.onOptionClick(optionList.get(getAdapterPosition()).getOption());
                    root.setBackgroundResource(R.drawable.round_corner_option_selected);
                    option_tv.setTextColor(Color.WHITE);
                    if (checkedPosition != getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
        }


    }

    public Option getSelected () {
        if (checkedPosition != -1) {
            return optionList.get(checkedPosition);
        }
        return null;
    }



        public interface NewRequest {
            void onOptionClick(String option);

        }



}
