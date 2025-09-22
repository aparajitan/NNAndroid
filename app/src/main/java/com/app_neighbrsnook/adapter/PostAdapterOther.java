package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.post.Listdatum;
import com.app_neighbrsnook.model.wall.Emojilistdatum;
import com.app_neighbrsnook.libraries.readmoretextview.ReadMoreTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostAdapterOther extends RecyclerView.Adapter<PostAdapterOther.BankViewHolder> {
//    private final List<Listdatum> pollList;
    boolean isvoted = false;
    Context mcon;
    WallChildAdapter.ImageCallBack icb;
    PostRequest newRequest;
    private List<Listdatum> mList = new ArrayList();
    String source;

    public PostAdapterOther(List<Listdatum> lists, PostRequest newRequest, String source) {
        this.mList = lists;
        this.newRequest = newRequest;
        this.source = source;
//        this.icb = icb;
    }

//    public PostAdapter(List<Listdatum> lists , PostRequest newRequest) {
//        this.mList = lists;
//        this.newRequest = newRequest;
//        this.icb = icb;
//    }

    @NonNull
    @Override
    public PostAdapterOther.BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_row_post_list, parent, false);
        mcon = parent.getContext();
        return new PostAdapterOther.BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostAdapterOther.BankViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            holder.post_type.setText(mList.get(position).getPostType());
            holder.post_msg.setText(mList.get(position).getPostMessage());
            holder.reaction_count.setText(String.valueOf(mList.get(position).getTotallike() + " Like"));
            holder.comment_textview.setText(String.valueOf(mList.get(position).getTotcomment()+ " Comment"));
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mcon, LinearLayoutManager.HORIZONTAL, false);
            holder.wall_image_rv.setLayoutManager(layoutManager);
            holder.wall_image_rv.setHasFixedSize(false);
            if(source.equals("profile")) {
                holder.threedot_imageview.setImageResource(R.drawable.ic_trash_icon);
                holder.threedot_imageview.setVisibility(View.GONE);
            }
            else {
                holder.threedot_imageview.setVisibility(View.GONE);
            }
//            holder.threedot_imageview.

            if ((mList.get(position).getPostImages().size() != 0)) {
                WallChildAdapter childRecyclerViewAdapter = new WallChildAdapter(mList.get(position).getPostImages(), holder.wall_image_rv.getContext(), icb);
                holder.wall_image_rv.setAdapter(childRecyclerViewAdapter);
            }
            holder.user_tv.setText(mList.get(position).getUsername());
            holder.created_date_tv.setText(mList.get(position).getNeighborhood() + ", " + mList.get(position).getCreatedOn());
        if (mList.get(position).getUserpic().isEmpty()) {
             holder.profile_imageview.setImageResource(R.drawable.profile);
        } else {
            Picasso.get().load(mList.get(position).getUserpic()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile)
                    .into( holder.profile_imageview);
        }
        }catch (Exception e)
        {

        }

        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                                            newRequest.onClickImage(position);
            }
        });

        holder.comment_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRequest.onClickComment(mList, position);
            }
        });

        holder.emoji_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRequest.onClickEmoji(mList.get(position).getEmojilistdata());


            }
        });

        holder.threedot_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRequest.onDeletePost(Integer.parseInt(mList.get(position).getPostid()));
            }
        });

//        holder.share_imageview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                newRequest.onClickShare(position);
//            }
//        });


        if (mList.get(position).getEmojiStatus().equals("1")) {
            holder.reactButton.setImageResource(R.drawable.baseline_favorite_24);

        }
        else {
            holder.reactButton.setImageResource(R.drawable.baseline_favorite_border_24);
//                        ((WallViewHolder) holder).reactButton.setTint

        }

        holder.reactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                holder.reactButton.setImageResource(R.drawable.baseline_favorite_24);
//                newRequest.onSetEmoji(Integer.parseInt(mList.get(position).getPostid()), mList.get(position).getPostlike());


                if(mList.get(position).getPostlike().equals("0")) {

                    newRequest.onSetEmoji(Integer.parseInt(mList.get(position).getPostid()), "1");
                    holder.reactButton.setImageResource(R.drawable.baseline_favorite_24);
                }
                else if(mList.get(position).getPostlike().equals("1")) {

                    newRequest.onSetEmoji(Integer.parseInt(mList.get(position).getPostid()), "0");
                    holder.reactButton.setImageResource(R.drawable.baseline_favorite_border_24);

                }
                else {
                    newRequest.onSetEmoji(Integer.parseInt(mList.get(position).getPostid()), "0");
                    holder.reactButton.setImageResource(R.drawable.baseline_favorite_border_24);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class BankViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_imageview, threedot_imageview, reactButton;
        TextView post_type, created_date_tv, like_textview, comment_textview, user_tv, reaction_count;
        ReadMoreTextView post_msg;
        LinearLayout root, comment_ll, emoji_ll, profile_ll;
        RelativeLayout rl;
        RecyclerView wall_image_rv, emoji_rv;

        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            user_tv = itemView.findViewById(R.id.user_tv);
            post_type = itemView.findViewById(R.id.post_type_tv);
            post_msg = itemView.findViewById(R.id.post_msg_textview);
            created_date_tv = itemView.findViewById(R.id.created_date_tv);
            threedot_imageview = itemView.findViewById(R.id.threedot_imageview);
            comment_textview = itemView.findViewById(R.id.comment_textview);
            comment_ll = itemView.findViewById(R.id.comment_ll);
//            like_imageview = itemView.findViewById(R.id.like_imageview);
//            share_imageview = itemView.findViewById(R.id.share_imageview);
            emoji_rv = itemView.findViewById(R.id.emoji_rv);
            emoji_ll = itemView.findViewById(R.id.like_ll);
            rl = itemView.findViewById(R.id.rl);
            wall_image_rv = itemView.findViewById(R.id.wall_image_rv);
            reactButton = itemView.findViewById(R.id.reactButton);
//            emoji_image = itemView.findViewById(R.id.emojiButton);
            reaction_count = itemView.findViewById(R.id.reaction_count);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            profile_ll = itemView.findViewById(R.id.profile_ll);
        }

    }

    public interface PostRequest {

        void onDeletePost(int pos);

        void onSetEmoji(int pos, String postlike);

        void onClickEmoji(List<Emojilistdatum> pos);

        void onClickComment(List<Listdatum> mList, int pos);

        void onClickShare(int pos);


    }
}