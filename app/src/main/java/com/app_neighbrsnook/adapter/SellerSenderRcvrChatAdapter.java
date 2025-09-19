package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.dm.Nbdatum;
import com.app_neighbrsnook.pojo.marketPlacePojo.ChatWithSenderRcvr;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerSenderRcvrChatAdapter extends RecyclerView.Adapter {


    public static final int SENDER = 1;
    public static final int RECIVER = 2;
    List<ChatWithSenderRcvr> commentList = new ArrayList<>();



    SharedPrefsManager sharedPrefsManager ;
    String type;

    public SellerSenderRcvrChatAdapter(PrefMananger prefMananger) {
        this.prefMananger = prefMananger;
    }

    PrefMananger prefMananger;

    public SellerSenderRcvrChatAdapter(Context mcon) {
        this.mcon = mcon;
    }

    Context mcon;
//    BuyCallBack bcb;
    public SellerSenderRcvrChatAdapter(Context context,List<ChatWithSenderRcvr> commentList) {
        this.commentList = commentList;
        this.mcon=context;
        sharedPrefsManager=new SharedPrefsManager(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {

            case 10:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_sender_row, parent, false);
                mcon =  parent.getContext();
                return new SenderHolder(view);

            case 20:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_reciever_row, parent, false);
                mcon =  parent.getContext();
                return new RecieverHolder(view);


        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ChatWithSenderRcvr object = commentList.get(position);
        if (object != null) {

            switch (object.getSendertype()) {

                case "sender":
                    try {
                        ((SenderHolder) holder).message_tv.setText(object.getMessage());
                        ((SenderHolder) holder).time.setText(object.getCreatedAts());

                        Picasso.get().load(object.getSenderUserpic()).into(((SenderHolder) holder).profile_imageview);

                    }
                    catch (Exception e)
                    {

                    }
                    break;

                case "receiver":

                    try {

                        ((RecieverHolder) holder).message_tv.setText(object.getMessage());
                        ((RecieverHolder) holder).time.setText(object.getCreatedAts());

                        Picasso.get().load(object.getSenderUserpic()).into(((RecieverHolder) holder).profile_imageview);

                    }catch (Exception e)
                    {

                    }
                    break;
            }




//        notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {

        if (commentList == null)
        {
            return 0;
        } else {
            return commentList.size();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (commentList != null) {
            ChatWithSenderRcvr object =  commentList.get(position);
            if (object != null) {
                if (commentList.get(position).getSendertype().equals("sender"))
                    return 10;
                else if(commentList.get(position).getSendertype().equals("receiver"))
                    return 20;

            }
        }
        return 0;
    }

    private class SenderHolder extends RecyclerView.ViewHolder {
        TextView message_tv, time, pay, tv_subject;
        LinearLayout tempate_ll;
        CircleImageView profile_imageview;
        public SenderHolder(View view) {
            super(view);
            message_tv = view.findViewById(R.id.tvMessage);
            time = view.findViewById(R.id.time);
            tempate_ll = view.findViewById(R.id.tempate_ll);
            tv_subject = view.findViewById(R.id.tv_subject);
            profile_imageview = view.findViewById(R.id.profile_imageview);
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

    public void filterList(ArrayList<ChatWithSenderRcvr> filterdNames) {
        this.commentList = filterdNames;
        notifyDataSetChanged();
    }


}
