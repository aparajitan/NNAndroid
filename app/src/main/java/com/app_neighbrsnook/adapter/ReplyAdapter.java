package com.app_neighbrsnook.adapter;

import android.app.AlertDialog;
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
import com.app_neighbrsnook.model.postComment.ReplyDatum;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    List<ReplyDatum> replyList;
    private OnReplyButtonClickListener listener;
    Context mcon;
    SharedPrefsManager sm;
    int commentPotion;

    public ReplyAdapter(int commentPotion, List<ReplyDatum> replyList, OnReplyButtonClickListener listener) {
        this.replyList = replyList;
        this.listener = listener;
        this.commentPotion = commentPotion;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_row, parent, false);
        mcon = parent.getContext();
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        ReplyDatum reply = replyList.get(position);

        //Like Option Not Proper work this reason Like Image and Count Gone
        holder.tv_total_like.setVisibility(View.GONE);
        holder.iv_comment_like.setVisibility(View.GONE);

        holder.reply_name_tv.setText(reply.getUsername());
        holder.reply_time_tv.setText(reply.getCreateon());
        holder.reply_message_tv.setText(reply.getCommenttext());
        holder.reply_username.setText(reply.getTop_level_username());

        if (reply.getUserpic().isEmpty()) {
            holder.reply_profile_image.setImageResource(R.drawable.profile);
        } else {
            Picasso.get().load(reply.getUserpic())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.profile_g1)
                    .error(R.drawable.profile)
                    .into(holder.reply_profile_image);
        }

        holder.tv_total_like.setOnClickListener(v -> {
            if (listener != null) {
                if (!reply.getTotal_likes().equals("0")) {
                    listener.onCommentLikeListClick(reply.getPcId());
                }
            }
        });

        holder.reply_btn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReplyButtonClick(reply.getUserid(), reply.getUsername(), reply.getPcId());
            }
        });

        holder.reply_username.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOtherUserClick(Integer.parseInt(reply.getTop_level_userid()));
            }
        });

        // Comment delete functionality
        sm = new SharedPrefsManager(mcon);
        String id = sm.getString("user_id");

        holder.reply_message_tv.setOnLongClickListener(v -> {
            if (reply.getUserid().equals(id)) { // Only allow options for the comment owner
                showBottomSheetOptions(position, reply.getPcId(), reply.getPostid(), reply);
                return true; // Return true to indicate the long click is consumed
            }
            return false; // Return false if the user is not the owner
        });

    }

    private void showBottomSheetOptions(int position, String commentId, String postId, ReplyDatum reply) {
        // Inflate the BottomSheet layout
        View bottomSheetView = LayoutInflater.from(mcon).inflate(R.layout.bottom_sheet_comment_options, null);

        // Create the BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mcon, R.style.BottomSheetDialogTheme); // Apply custom theme
        bottomSheetDialog.setContentView(bottomSheetView);

        // Set rounded corners background
        if (bottomSheetView.getParent() != null) {
            ((ViewGroup) bottomSheetView.getParent()).setBackgroundResource(android.R.color.transparent); // Remove default background
        }

        bottomSheetView.setBackgroundResource(R.drawable.rounded_bottom_sheet_background); // Apply custom background

        // Find views in the BottomSheet
        LinearLayout deleteOption = bottomSheetView.findViewById(R.id.delete_option);
        LinearLayout editOption = bottomSheetView.findViewById(R.id.edit_option);
        LinearLayout replyOption = bottomSheetView.findViewById(R.id.reply_option);
        LinearLayout likeOption = bottomSheetView.findViewById(R.id.like_option);
        ImageView likeImage = bottomSheetView.findViewById(R.id.like_image);
        TextView likeText = bottomSheetView.findViewById(R.id.like_text);

        likeOption.setVisibility(View.GONE);
        editOption.setVisibility(View.GONE);

        if (reply.getUser_like_status().equals("1")) {
            likeImage.setImageResource(R.drawable.ic_baseline_favorite_red_24);
            likeText.setText("Unlike");
        } else {
            likeImage.setImageResource(R.drawable.ic_outline_favorite_border_24);
            likeText.setText("Like");
        }

        // Handle Delete Option
        deleteOption.setOnClickListener(v -> {
            bottomSheetDialog.dismiss(); // Dismiss the BottomSheet
            if (listener != null) {
                listener.onCommentReplyDelete(commentId, postId, position, commentPotion); // Notify the listener
            }
//            showDeleteDialog(position, commentId, postId); // Show the delete confirmation dialog
        });

        // Handle Edit Option
        editOption.setOnClickListener(v -> {
            bottomSheetDialog.dismiss(); // Dismiss the BottomSheet
            // Add your edit comment logic here
        });

        // Handle Reply Option
        replyOption.setOnClickListener(v -> {
            bottomSheetDialog.dismiss(); // Dismiss the BottomSheet
            if (listener != null) {
                listener.onReplyButtonClick(reply.getUserid(), reply.getUsername(), reply.getPcId());
            }
        });

        // Show the BottomSheet
        bottomSheetDialog.show();
    }

    private void showDeleteDialog(int position, String commentId, String postId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcon);
        builder.setTitle("Delete Comment")
                .setMessage("Are you sure you want to delete this comment?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (listener != null) {
                        listener.onCommentReplyDelete(commentId, postId, position, commentPotion); // Notify the listener
                    }
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

    class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView reply_name_tv, reply_message_tv, reply_time_tv, viewRepliesText;
        CircleImageView reply_profile_image;
        RecyclerView repliesRecyclerView;
        TextView reply_btn, reply_username, tv_total_like;
        ImageView iv_comment_like;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            reply_name_tv = itemView.findViewById(R.id.reply_name_tv);
            reply_time_tv = itemView.findViewById(R.id.reply_time_tv);
            reply_message_tv = itemView.findViewById(R.id.reply_message_tv);
            reply_profile_image = itemView.findViewById(R.id.reply_profile_image);
            repliesRecyclerView = itemView.findViewById(R.id.nested_replies_recycler_view);
            viewRepliesText = itemView.findViewById(R.id.view_replies_tv);
            reply_btn = itemView.findViewById(R.id.reply_btn);
            reply_username = itemView.findViewById(R.id.reply_username);
            iv_comment_like = itemView.findViewById(R.id.iv_comment_like);
            tv_total_like = itemView.findViewById(R.id.tv_total_like);
        }
    }

    public void updateCommentLikeReplyItem(int position, String totalLike, String likeStatus) {
        replyList.get(position).setTotal_likes(totalLike);
        replyList.get(position).setUser_like_status(likeStatus);
        notifyItemChanged(position);
    }

    public void removeComment(int position, int commentPosition) {
//        if (commentPotion == commentPosition) {
            replyList.remove(position);
            notifyItemRemoved(position);
//        }
//        notifyDataSetChanged();
    }

    public interface OnReplyButtonClickListener {
        void onReplyButtonClick(String userId, String username, String pcId);

        void onOtherUserClick(int otherUserId);

        void onReplyLikeButtonClick(String postId, String pcId, String likeStatus, int pos);

        void onCommentLikeListClick(String pcId);

        void onCommentReplyDelete(String commentId, String postId, int position, int commentPotion);
    }
}