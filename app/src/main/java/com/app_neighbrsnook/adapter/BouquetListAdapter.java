package com.app_neighbrsnook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.intreface.EmojiCallBack;
import com.app_neighbrsnook.model.wall.BouquetDataModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BouquetListAdapter extends RecyclerView.Adapter<BouquetListAdapter.ViewHolder>{

    Context context;
    List<BouquetDataModel> bouquetDataModels = new ArrayList<>();
    EmojiCallBack ecb;
    int[] images_emoji = {R.drawable.ic_happy, R.drawable.ic_heart, R.drawable.ic_like, R.drawable.ic_angry};

    public BouquetListAdapter(List<BouquetDataModel> bouquetDataModels) {
        this.bouquetDataModels = bouquetDataModels;
        this.ecb = ecb;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.likes_list_low, parent, false);
        return new BouquetListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image_view.setImageResource(R.drawable.flower);
        holder.user_name_tv.setText(bouquetDataModels.get(position).getUsername());

        if (bouquetDataModels.get(position).getUserpic().isEmpty()) {
            holder.userLikesPhoto.setImageResource(R.drawable.profile);
        } else {
            Picasso.get().load(bouquetDataModels.get(position).getUserpic()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.marketplace_white_background)
                    .into(holder.userLikesPhoto);
        }

        if (position == bouquetDataModels.size() - 1) {
            holder.divider.setVisibility(View.GONE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
}
    }

    @Override
    public int getItemCount() {
        return bouquetDataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View divider;
        TextView user_name_tv;
        ImageView image_view;
        CircularImageView userLikesPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.view_divider);
            image_view= itemView.findViewById(R.id.image_view);
            userLikesPhoto= itemView.findViewById(R.id.userLikesPhoto);
            user_name_tv = itemView.findViewById(R.id.user_name_tv);
        }
    }
}
