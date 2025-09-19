package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.intreface.BuyCallBack;
import com.app_neighbrsnook.model.ChatModel;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {
    public static final int SENDER = 1;
    public static final int RECIVER = 2;
    List<ChatModel> commentList = new ArrayList<>();
    String type;
    BuyCallBack bcb;
    public ChatAdapter(List<ChatModel> commentList, String type, BuyCallBack bcb) {
        this.commentList = commentList;
        this.type = type;
        this.bcb = bcb;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {

            case SENDER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_sender_row, parent, false);
                return new SenderHolder(view);

            case RECIVER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_reciever_row, parent, false);
                return new RecieverHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ChatModel object = commentList.get(position);
        if (object != null) {
            switch (object.getSender_or_reciever()) {

                case SENDER:

//                        ((SenderHolder) holder).message_tv.setText(object.getComment_msg());
//                        ((SenderHolder) holder).time.setText(object.getTime());
//
                      //  ((SenderHolder) holder).message_tv.setText(object.getComment_msg());
                        //  ((SenderHolder) holder).time.setText(object.getTime());
                      // ((SenderHolder)holder).confirmation_ll.setVisibility(View.VISIBLE);
                //    b9c9db6 (12-14-)
                    break;

                case RECIVER:

//                    ((RecieverHolder) holder).message_tv.setText(object.getComment_msg());
//                    ((RecieverHolder) holder).time.setText(object.getTime());

            }
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (commentList != null) {
            ChatModel object = commentList.get(position);
            if (object != null) {
                return object.getSender_or_reciever();
            }
        }
        return 0;
    }

    private class SenderHolder extends RecyclerView.ViewHolder {
        TextView message_tv, time, pay, negotiate_tv;
        LinearLayout tempate_ll, confirmation_ll;
        public SenderHolder(View view) {
            super(view);
            message_tv = view.findViewById(R.id.message_tv);
            time = view.findViewById(R.id.time);
            tempate_ll = view.findViewById(R.id.tempate_ll);
            confirmation_ll = view.findViewById(R.id.confirmation_ll);
        }
    }

    private class RecieverHolder extends RecyclerView.ViewHolder {
        TextView message_tv, time, accept, cong_tv, t1, t2, t3;
        LinearLayout tempate_ll, confirmation_ll;
        public RecieverHolder(View view) {
            super(view);
            message_tv = view.findViewById(R.id.message_tv);
            time = view.findViewById(R.id.time);
        }
    }


}
