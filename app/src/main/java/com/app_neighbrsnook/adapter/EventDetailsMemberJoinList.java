package com.app_neighbrsnook.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.EventDetailsUserListPojo;
import com.app_neighbrsnook.profile.MyProfileOtherUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventDetailsMemberJoinList extends RecyclerView.Adapter<EventDetailsMemberJoinList.BankViewHolder> {
    private final List<EventDetailsUserListPojo> groupListPojos;
    Dialog newRequest;
    Context mcon;
    int id;


    public EventDetailsMemberJoinList(List<EventDetailsUserListPojo> lists,  Dialog newRequest) {
        this.groupListPojos = lists;
        //this.type = type;
        this.newRequest = newRequest;

    }
    @NonNull
    @Override
    public EventDetailsMemberJoinList.BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_memberlist_details,parent,false);
        mcon = parent.getContext();
        return new EventDetailsMemberJoinList.BankViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull EventDetailsMemberJoinList.BankViewHolder holder, int position) {
        EventDetailsUserListPojo eventjoinuserlist = groupListPojos.get(position);
        holder.tv_neighbrhood_event.setText(eventjoinuserlist.getNeigh());
        holder.tv_username_event.setText(eventjoinuserlist.getName());

        if (eventjoinuserlist.getImg().isEmpty()) {
            holder.img_event_userp_pic.setImageResource(R.drawable.profile);
        } else{
            Picasso.get().load(eventjoinuserlist.getImg()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile)
                    .into(holder.img_event_userp_pic);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // newRequest.onClickDetail(position, Integer.parseInt(eventjoinuserlist.getUserid()));
              /*  Intent detailScreen = new Intent(mcon, MyProfileOtherUser.class);
                detailScreen.putExtra("id",Integer.parseInt(eventjoinuserlist.geteId()));*/
                Intent detailScreen = new Intent(mcon, MyProfileOtherUser.class);
                detailScreen.putExtra("user_id",Integer.parseInt(eventjoinuserlist.getUserid()));
          //      detailScreen.putExtra("groupId",eventjoinuserlist.getGroupid());
                detailScreen.putExtra("type","detail");
                mcon.startActivity(detailScreen);
            }
        });
    }
    @Override
    public int getItemCount() {
        return groupListPojos.size();
    }
    public class BankViewHolder extends RecyclerView.ViewHolder{
        CircularImageView img_owner_profile,img_already_member_user,img_pri_send_rqst_pic;
        LinearLayout root;
        ImageView img_delete_group_by_owener;
        FrameLayout  l2_already_member_lyt,l3_request_send_lyt;
        FrameLayout frm_approved,frm_declined;
        TextView tv_neighbr_already_member,tv_already_member_user,tv_pri_user_send_reques,tv_pri_neighbrh_send_request;
        TextView tv_username_event,tv_neighbrhood_event;
        CircularImageView img_event_userp_pic;
        int id;

        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username_event = itemView.findViewById(R.id.tv_username_event);
            tv_neighbrhood_event = itemView.findViewById(R.id.tv_neighbrhood_event);
            img_event_userp_pic = itemView.findViewById(R.id.img_event_userp_pic);
            frm_approved = itemView.findViewById(R.id.frm_approve_id);
            frm_declined = itemView.findViewById(R.id.frm_decline_id);
            img_delete_group_by_owener = itemView.findViewById(R.id.img_amit_dubey_icon);

            l2_already_member_lyt = itemView.findViewById(R.id.l2_already_member_lyt);
            tv_neighbr_already_member = itemView.findViewById(R.id.tv_neighbr_already_member);
            tv_already_member_user = itemView.findViewById(R.id.tv_already_member_user);
            img_already_member_user = itemView.findViewById(R.id.img_already_member_user);
            l3_request_send_lyt = itemView.findViewById(R.id.l3_request_send_lyt);
            tv_pri_user_send_reques = itemView.findViewById(R.id.tv_send_pending_user_reques_id);
            tv_pri_neighbrh_send_request = itemView.findViewById(R.id.tv_neighbrh_send_request);
            img_pri_send_rqst_pic = itemView.findViewById(R.id.img_rst_send_pic);

             root = itemView.findViewById(R.id.lnr_root);

        }
    }
    public interface NewRequest{
        void onClick(int pos);
    }
}
