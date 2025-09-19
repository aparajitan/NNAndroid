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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.postComment.Postlistdatum;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    ReplyAdapter replyAdapter;
    List<Postlistdatum> commentList;
    Context mcon;
    private OnReplyButtonClickListener listener;
    SharedPrefsManager sm;
    int comPos;
    private Set<String> expandedCommentIds;


    public CommentAdapter(List<Postlistdatum> commentList, OnReplyButtonClickListener listener, Set<String> expandedCommentIds) {
        this.commentList = commentList;
        this.listener = listener;
        this.expandedCommentIds = expandedCommentIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        mcon = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Postlistdatum postComment = commentList.get(position);
        comPos = position;

        // Comment delete functionality
        sm = new SharedPrefsManager(mcon);
        String id = sm.getString("user_id");

        holder.message_tv.setText(postComment.getCommenttext());
        holder.name_tv.setText(postComment.getUsername());
        holder.time_tv.setText(postComment.getCreateon());
        if (postComment.getUserpic().isEmpty()) {
            holder.profile_imageview.setImageResource(R.drawable.profile);
        } else {
            Picasso.get().load(postComment.getUserpic()).fit().centerCrop().placeholder(R.drawable.profile_g1).error(R.drawable.profile).into(holder.profile_imageview);
        }

        if (!postComment.getTotal_likes().equals("0")) {
            holder.tv_total_like.setVisibility(View.VISIBLE);
            holder.tv_total_like.setText(postComment.getTotal_likes());
        } else {
            holder.tv_total_like.setVisibility(View.GONE);
            holder.tv_total_like.setText(postComment.getTotal_likes());
        }

        if (postComment.getUser_like_status().equals("1")) {
            holder.iv_comment_like.setImageResource(R.drawable.ic_baseline_favorite_red_24);
        } else {
            holder.iv_comment_like.setImageResource(R.drawable.ic_outline_favorite_border_24);
        }

        replyAdapter = new ReplyAdapter(position, postComment.getReplies(), listener);
        holder.repliesRecyclerView.setLayoutManager(new LinearLayoutManager(mcon));
        holder.repliesRecyclerView.setAdapter(replyAdapter);

        if (!postComment.getReplies().isEmpty()) {
            holder.viewRepliesText.setVisibility(View.VISIBLE);

            boolean isExpanded = expandedCommentIds.contains(postComment.getPcId());
            holder.repliesRecyclerView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.viewRepliesText.setText(isExpanded ? "Hide replies" : "View " + postComment.getTotal_comments() + " replies");
        } else {
            holder.viewRepliesText.setVisibility(View.GONE);
            holder.repliesRecyclerView.setVisibility(View.GONE);
        }

        holder.viewRepliesText.setOnClickListener(v -> {
            boolean nowExpanded = holder.repliesRecyclerView.getVisibility() == View.GONE;
            if (nowExpanded) {
                expandedCommentIds.add(postComment.getPcId());
                holder.repliesRecyclerView.setVisibility(View.VISIBLE);
                holder.viewRepliesText.setText("Hide replies");
            } else {
                expandedCommentIds.remove(postComment.getPcId());
                holder.repliesRecyclerView.setVisibility(View.GONE);
                holder.viewRepliesText.setText("View " + postComment.getTotal_comments() + " replies");
            }
        });

        holder.iv_comment_like.setOnClickListener(v -> {
            if (listener != null) {
                if (postComment.getUser_like_status().equals("1")) {
                    listener.onCommentLikeButtonClick(postComment.getPostid(), postComment.getPcId(), "0", position);
                } else {
                    listener.onCommentLikeButtonClick(postComment.getPostid(), postComment.getPcId(), "1", position);
                }
            }
        });

        holder.tv_total_like.setOnClickListener(v -> {
            if (listener != null) {
                if (!postComment.getTotal_likes().equals("0")) {
                    listener.onCommentLikeListClick(postComment.getPcId());
                }
            }
        });

        holder.reply_btn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReplyButtonClick(postComment.getUserid(), postComment.getUsername(), postComment.getPcId());
            }
        });

        holder.message_tv.setOnLongClickListener(v -> {
            if (postComment.getUserid().equals(id)) { // Only allow options for the comment owner
                showBottomSheetOptions(position, postComment.getPcId(), postComment.getPostid(), postComment);
                return true; // Return true to indicate the long click is consumed
            }
            return false; // Return false if the user is not the owner
        });
    }

    private void showBottomSheetOptions(int position, String commentId, String postId, Postlistdatum postComment) {
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

        editOption.setVisibility(View.GONE);

        if (postComment.getUser_like_status().equals("1")) {
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
                listener.onCommentDelete(commentId, postId, position); // Notify the listener
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
                listener.onReplyButtonClick(postComment.getUserid(), postComment.getUsername(), postComment.getPcId());
            }
        });

        // Handle Like Option
        likeOption.setOnClickListener(v -> {
            bottomSheetDialog.dismiss(); // Dismiss the BottomSheet
            if (listener != null) {
                if (postComment.getUser_like_status().equals("1")) {
                    listener.onCommentLikeButtonClick(postComment.getPostid(), postComment.getPcId(), "0", position);
                } else {
                    listener.onCommentLikeButtonClick(postComment.getPostid(), postComment.getPcId(), "1", position);
                }
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
                        listener.onCommentDelete(commentId, postId, position); // Notify the listener
                    }
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView message_tv, name_tv, time_tv, viewRepliesText;
        CircleImageView profile_imageview;
        RecyclerView repliesRecyclerView;
        TextView reply_btn, tv_total_like;
        ImageView iv_comment_like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message_tv = itemView.findViewById(R.id.message_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            time_tv = itemView.findViewById(R.id.time_tv);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            repliesRecyclerView = itemView.findViewById(R.id.replies_recycler_view);
            viewRepliesText = itemView.findViewById(R.id.view_replies_tv);
            reply_btn = itemView.findViewById(R.id.reply_btn);
            iv_comment_like = itemView.findViewById(R.id.iv_comment_like);
            tv_total_like = itemView.findViewById(R.id.tv_total_like);
        }
    }

    public void updateCommentLikeItem(int position, String totalLike, String likeStatus, String value) {
        if (value.equals("comment")) {
            commentList.get(position).setTotal_likes(totalLike);
            commentList.get(position).setUser_like_status(likeStatus);
            notifyItemChanged(position);
        } else {
            replyAdapter.updateCommentLikeReplyItem(position, totalLike, likeStatus);
        }
    }

    public void removeComment(int position, int commentPosition, String type) {
        if (type.equals("reply")){
            if (commentPosition == comPos){
                replyAdapter.removeComment(position, commentPosition);
            }
        } else {
            commentList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public interface OnReplyButtonClickListener extends ReplyAdapter.OnReplyButtonClickListener {

        void onReplyButtonClick(String userId, String username, String pcId);

        void onCommentLikeButtonClick(String postId, String pcId, String likeStatus, int pos);

        void onCommentLikeListClick(String pcId);

        void onCommentDelete(String commentId, String postId, int position);
    }
}