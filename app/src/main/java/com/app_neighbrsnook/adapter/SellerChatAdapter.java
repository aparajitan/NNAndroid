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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.dm.Nbdatum;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerChatAdapter extends RecyclerView.Adapter {

    public static final int SENDER = 1;
    public static final int RECIVER = 2;
    List<Nbdatum> commentList = new ArrayList<>();
    SharedPrefsManager sm;
    String type;
    Context mcon;
//    BuyCallBack bcb;
    private OnDeleteMesaageClickListener listener;

    public SellerChatAdapter(List<Nbdatum> commentList, OnDeleteMesaageClickListener listener) {
        this.commentList = commentList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {

            case 10:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_sender_row, parent, false);
                mcon = parent.getContext();
                return new SenderHolder(view);

            case 20:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_reciever_row, parent, false);
                mcon = parent.getContext();
                return new RecieverHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Nbdatum object = commentList.get(position);
        if (object != null) {
            switch (object.getType()) {

                case "sender":
                    try {
                        if (object.getSubject().equals(" ")) {
                            ((SenderHolder) holder).tv_subject.setVisibility(View.GONE);
                        } else {
                            ((SenderHolder) holder).tv_subject.setVisibility(View.GONE);
                            ((SenderHolder) holder).tv_subject.setText("Subject : " + object.getSubject());
                        }
                        ((SenderHolder) holder).message_tv.setText(object.getMessage());
                        ((SenderHolder) holder).time.setText(object.getDttime());

                        Picasso.get().load(object.getUserpic()).into(((SenderHolder) holder).profile_imageview);

                    } catch (Exception e) {

                    }

                    ((SenderHolder) holder).root.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            showBottomSheetOptions(position, object.getId(), object.getMsg_id(), object.getDttime());
                            return true;
                        }
                    });

                    break;

                case "receiver":
                    try {
                        if (object.getSubject().equals(" ")) {

                        } else {
                            ((RecieverHolder) holder).tv_subject.setVisibility(View.GONE);
                            ((RecieverHolder) holder).tv_subject.setText("Subject : " + object.getSubject());
//                            ((RecieverHolder) holder).tv_subject.setText(Html.fromHtml(String.format(R.string.), ...)));
                        }

                        ((RecieverHolder) holder).message_tv.setText(object.getMessage());
                        ((RecieverHolder) holder).time.setText(object.getDttime());

                        Picasso.get().load(object.getUserpic()).into(((RecieverHolder) holder).profile_imageview);

                    } catch (Exception e) {

                    }
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (commentList == null) {
            return 0;
        } else {
            return commentList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (commentList != null) {
            Nbdatum object = commentList.get(position);
            if (object != null) {
                if (commentList.get(position).getType().equals("sender"))
                    return 10;
                else if (commentList.get(position).getType().equals("receiver"))
                    return 20;
            }
        }
        return 0;
    }

    private class SenderHolder extends RecyclerView.ViewHolder {
        TextView message_tv, time, pay, tv_subject;
        LinearLayout tempate_ll;
        CircleImageView profile_imageview;
        RelativeLayout root;

        public SenderHolder(View view) {
            super(view);
            message_tv = view.findViewById(R.id.tvMessage);
            time = view.findViewById(R.id.time);
            tempate_ll = view.findViewById(R.id.tempate_ll);
            tv_subject = view.findViewById(R.id.tv_subject);
            profile_imageview = view.findViewById(R.id.profile_imageview);
            root = view.findViewById(R.id.root);
        }
    }

    private class RecieverHolder extends RecyclerView.ViewHolder {
        TextView message_tv, time, accept, tv_subject;
        CircleImageView profile_imageview;
        LinearLayout tempate_ll;

        public RecieverHolder(View view) {
            super(view);
            message_tv = view.findViewById(R.id.tvMessage);
            time = view.findViewById(R.id.time);
            profile_imageview = view.findViewById(R.id.profile_imageview);
            tv_subject = view.findViewById(R.id.tv_subject);
        }
    }

    public void filterList(ArrayList<Nbdatum> filterdNames) {
        this.commentList = filterdNames;
        notifyDataSetChanged();
    }

    private void showBottomSheetOptions(int position, String toUserId, String msgId, String msgTime) {
        View bottomSheetView = LayoutInflater.from(mcon).inflate(R.layout.bottom_sheet_comment_options, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mcon, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(bottomSheetView);

        if (bottomSheetView.getParent() != null) {
            ((ViewGroup) bottomSheetView.getParent()).setBackgroundResource(android.R.color.transparent);
        }

        bottomSheetView.setBackgroundResource(R.drawable.rounded_bottom_sheet_background);

        LinearLayout deleteOption = bottomSheetView.findViewById(R.id.delete_option);
        LinearLayout editOption = bottomSheetView.findViewById(R.id.edit_option);
        LinearLayout replyOption = bottomSheetView.findViewById(R.id.reply_option);
        LinearLayout likeOption = bottomSheetView.findViewById(R.id.like_option);
        ImageView likeImage = bottomSheetView.findViewById(R.id.like_image);
        TextView likeText = bottomSheetView.findViewById(R.id.like_text);

        likeOption.setVisibility(View.GONE);
        replyOption.setVisibility(View.GONE);
        editOption.setVisibility(View.GONE);

        deleteOption.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            if (shouldShowDeleteDialog(msgTime)) {
                showBottomSheetDeleteOptions(position, toUserId, msgId);
            } else {
                if (listener != null) {
                    listener.onCommentDelete(toUserId, msgId, position, "me");
                }
            }
        });

        editOption.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Toast.makeText(mcon, "You clicked edit option", Toast.LENGTH_SHORT).show();
            // Add your edit comment logic here
        });

        bottomSheetDialog.show();
    }

    private boolean shouldShowDeleteDialog(String msgTime) {
        if (msgTime.equalsIgnoreCase("now")) {
            return true;
        } else if (msgTime.endsWith("m")) {
            try {
                int minutes = Integer.parseInt(msgTime.replace("m", ""));
                return minutes <= 15;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void showBottomSheetDeleteOptions(int position, String toUserId, String msgId) {
        View bottomSheetView = LayoutInflater.from(mcon).inflate(R.layout.bottom_sheet_dm_option, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mcon, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(bottomSheetView);

        if (bottomSheetView.getParent() != null) {
            ((ViewGroup) bottomSheetView.getParent()).setBackgroundResource(android.R.color.transparent);
        }

        bottomSheetView.setBackgroundResource(R.drawable.rounded_bottom_sheet_background);

        LinearLayout everyoneOption = bottomSheetView.findViewById(R.id.everyone_option);
        LinearLayout meOption = bottomSheetView.findViewById(R.id.me_option);

        everyoneOption.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            if (listener != null) {
                listener.onCommentDelete(toUserId, msgId, position, "everyone");
            }
        });

        meOption.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            if (listener != null) {
                listener.onCommentDelete(toUserId, msgId, position, "me");
            }
        });

        bottomSheetDialog.show();
    }

    public interface OnDeleteMesaageClickListener {

        void onCommentDelete(String toUserId, String msgId, int position, String deleteType);
    }

}
