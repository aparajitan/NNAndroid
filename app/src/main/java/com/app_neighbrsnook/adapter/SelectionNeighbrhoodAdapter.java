package com.app_neighbrsnook.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.StateDropdownPojo;

import java.util.ArrayList;

public class SelectionNeighbrhoodAdapter extends RecyclerView.Adapter<SelectionNeighbrhoodAdapter.Holder> {

    public SelectionNeighbrhoodAdapter(Context context,ArrayList<StateDropdownPojo> firtlist,ItemClick itemClick) {
        this.firtlist = firtlist;
        this.itemClick = itemClick;
        this.context = context;
    }
    ArrayList<StateDropdownPojo> firtlist;
    ItemClick itemClick;
    Context context;
    public interface ItemClick{
        void onItemClick(int position);
    }
    @NonNull
    @Override
    public SelectionNeighbrhoodAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.neighbrhood_select_or_list, parent, false);
        Holder holder= new Holder(view);
        return holder;
    }
    public int selected=-1;
    @Override
    public void onBindViewHolder(@NonNull SelectionNeighbrhoodAdapter.Holder holder, int position) {
        holder.tv_list_neighbrhood.setText(firtlist.get(position).getNbd_name());
        if (selected == position){
            holder.radioButton.setImageResource(R.drawable.unchecked_checkbox);
        }
        else {
            holder.radioButton.setImageResource(R.drawable.checked_checkbox);
        }
        holder.itemView.setOnClickListener(view -> {
                selected=position;
            new Handler().postDelayed(()->{
                itemClick.onItemClick(position);
                notifyDataSetChanged();
            },100);
        });
        if (selected == firtlist.size()-1) {
            holder.uVLine.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return firtlist.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_list_neighbrhood,uVLine;
        ImageView radioButton;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_list_neighbrhood=itemView.findViewById(R.id.sector_ngh_list_edt);
            radioButton=itemView.findViewById(R.id.radio_button);
            uVLine=itemView.findViewById(R.id.UvLine);
        }
    }
}
