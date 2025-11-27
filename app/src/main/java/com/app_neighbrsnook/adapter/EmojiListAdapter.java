package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
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
import com.app_neighbrsnook.model.wall.Emojilistdatum;

import java.util.ArrayList;
import java.util.List;

public class EmojiListAdapter extends RecyclerView.Adapter<EmojiListAdapter.ViewHolder> {
    Context context;
//    List<EmojiModel> emojiList = new ArrayList<>();
    List<Emojilistdatum> emojiList = new ArrayList<>();
    EmojiCallBack ecb;
    int[] images_emoji = {R.drawable.ic_happy, R.drawable.ic_heart, R.drawable.ic_like, R.drawable.ic_angry};

    public EmojiListAdapter(List<Emojilistdatum> emojiList) {
        this.emojiList = emojiList;
        this.ecb = ecb;
    }

    @NonNull
    @Override
    public EmojiListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.likes_list_low, parent, false);
        return new EmojiListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EmojiListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //holder.image_view.setText(emojiList.get(position).getEmoji());
       holder.user_name_tv.setText(emojiList.get(position).getUsername());
       holder.image_view.setImageResource(R.drawable.baseline_favorite_24);

    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image_view;
        TextView user_name_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_view= itemView.findViewById(R.id.image_view);
            user_name_tv = itemView.findViewById(R.id.user_name_tv);
        }
    }


}