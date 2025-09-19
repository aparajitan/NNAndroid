package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.EmojiModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class EmojiAdapter1 extends RecyclerView.Adapter<EmojiAdapter1.ViewHolder> {
    List<EmojiModel> emojiList = new ArrayList<>();
    HashSet<EmojiModel> hset;
    EmojiInterface emojiInterface;

    int[] images_emoji = {R.drawable.ic_happy, R.drawable.ic_heart, R.drawable.ic_like, R.drawable.ic_angry};

    public EmojiAdapter1(List<EmojiModel> emojiList, EmojiInterface emojiInterface) {
        this.emojiList = emojiList;
        this.emojiInterface = emojiInterface;
    }

    @NonNull
    @Override
    public EmojiAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emoji_image_row, parent, false);
        return new EmojiAdapter1.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EmojiAdapter1.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.image_view.setImageResource(emojiList.get(position).getEmoji());
        holder.image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiInterface.onEmojiClickListener(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_view= itemView.findViewById(R.id.image_view);
        }
    }


    public interface EmojiInterface
    {
        void onEmojiClickListener(int pos);
    }


}
