package com.app_neighbrsnook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.BankListGetterSetter;

import java.util.List;

public class BankListAdapter extends RecyclerView.Adapter<BankListAdapter.BankViewHolder> {

    private final List<BankListGetterSetter> BankLists;

    public BankListAdapter(List<BankListGetterSetter> lists) {
        this.BankLists = lists;
    }

    @NonNull
    @Override
    public BankListAdapter.BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_list,parent,false);
        return new BankListAdapter.BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankListAdapter.BankViewHolder holder, int position) {

        BankListGetterSetter bankListGetterSetter = BankLists.get(position);
        holder.BankName.setText(bankListGetterSetter.getBankName());
        holder.BankImage.setImageResource(bankListGetterSetter.BankImage);

    }

    @Override
    public int getItemCount() {
        return BankLists.size();
    }

    public class BankViewHolder extends RecyclerView.ViewHolder{

        private final TextView BankName;
        private final ImageView BankImage;



        public BankViewHolder(@NonNull View itemView) {
            super(itemView);

            BankName= itemView.findViewById(R.id.bankName);
            BankImage=itemView.findViewById(R.id.bankImage);

        }

    }

}