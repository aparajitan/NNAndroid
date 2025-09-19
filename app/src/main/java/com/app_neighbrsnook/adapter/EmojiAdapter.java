package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.EmojiModel;
import com.app_neighbrsnook.model.wall.Emojilistdatum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {
    Context context;
    List<Emojilistdatum> emojiList = new ArrayList<>();
    HashSet<EmojiModel> hset;
    EmojiCallBack ecb;
    int[] images_emoji = {R.drawable.ic_happy, R.drawable.ic_heart, R.drawable.ic_like, R.drawable.ic_angry};

    public EmojiAdapter(List<Emojilistdatum> emojiList, Context mContext, EmojiCallBack ecb) {
        this.emojiList = emojiList;
        this.context = mContext;
        this.ecb = ecb;
    }

    @NonNull
    @Override
    public EmojiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emoji_image_row, parent, false);
        return new EmojiAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EmojiAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.image_view.setText(emojiList.get(position).getEmoji());
        holder.image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ecb.onEmojiClick(emojiList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView image_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_view= itemView.findViewById(R.id.image_view);
        }
    }
    public interface EmojiCallBack {
        void onEmojiClick(List<Emojilistdatum> pos);
    }

}