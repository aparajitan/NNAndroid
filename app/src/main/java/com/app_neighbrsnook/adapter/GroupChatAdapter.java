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
import com.app_neighbrsnook.model.groupChat.Datum;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatAdapter extends RecyclerView.Adapter{

    public static final int SENDER = 1;
    public static final int RECIVER = 2;
    List<Datum> commentList = new ArrayList<>();
    SharedPrefsManager sm ;
    String userid;
    Context mcon;
//    BuyCallBack bcb;

    public GroupChatAdapter(List<Datum> commentList) {
        this.commentList = commentList;
        this.userid = userid;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {

            case 10:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_sender_row, parent, false);
                mcon =  parent.getContext();
                return new GroupChatAdapter.SenderHolder(view);

            case 20:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_reciever_row, parent, false);
                mcon =  parent.getContext();
                return new GroupChatAdapter.RecieverHolder(view);


        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Datum object = commentList.get(position);
        if (object != null) {
            switch (object.getType()) {
                case "sender":
                    ((SenderHolder) holder).tv_subject.setVisibility(View.GONE);
                    ((SenderHolder) holder).tv_subject.setText(object.getName());
                    ((SenderHolder) holder).message_tv.setText(object.getMessage());
                    ((SenderHolder) holder).time.setText(object.getDate());
                    String userPicUrl = object.getUserpic();
                    if (userPicUrl != null && !userPicUrl.trim().isEmpty()) {
                        Picasso.get()
                                .load(userPicUrl)
                                .placeholder(R.drawable.profile_b1) // loading time image
                                .error(R.drawable.profile_b1)       // error par image
                                .into(((SenderHolder) holder).profile_imageview);
                    } else {
                        ((SenderHolder) holder).profile_imageview.setImageResource(R.drawable.profile_b1);
                    }
                    break;
                case "receiver":
                            ((RecieverHolder) holder).tv_subject.setVisibility(View.VISIBLE);
                            ((RecieverHolder) holder).tv_subject.setText(object.getName());
//                            ((RecieverHolder) holder).tv_subject.setText(Html.fromHtml(String.format(R.string.), ...)));
                        ((RecieverHolder) holder).message_tv.setText(object.getMessage());
                        ((RecieverHolder) holder).time.setText(object.getDate());
                        Picasso.get().load(object.getUserpic()).into(((RecieverHolder) holder).profile_imageview);
                    break;
            }
        }
    else
    {
    }


//        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return commentList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (commentList != null) {
            Datum object =  commentList.get(position);
            if (object != null) {
                if (commentList.get(position).getType().equals("sender"))
                    return 10;
                else if(commentList.get(position).getType().equals("receiver"))
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

    public void filterList(ArrayList<Datum> filterdNames) {
        this.commentList = filterdNames;
        notifyDataSetChanged();
    }


}
