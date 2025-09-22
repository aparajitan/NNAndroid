package com.app_neighbrsnook.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.GroupDetailsbyNamePojo;
import com.app_neighbrsnook.profile.MyProfileOtherUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupDetailsJoinDetailsAdapter extends RecyclerView.Adapter<GroupDetailsJoinDetailsAdapter.BankViewHolder> {
    private final List<GroupDetailsbyNamePojo> groupListPojos;
    NewRequest newRequest;
    Context mcon;
    int id;
    String type;
    boolean isOwnerSelf;
    public GroupDetailsJoinDetailsAdapter(List<GroupDetailsbyNamePojo> lists, String type,boolean isOwnerSelf,NewRequest newRequest) {
        this.groupListPojos = lists;
        this.type = type;
        this.isOwnerSelf = isOwnerSelf;
        this.newRequest = newRequest;
    }

    public GroupDetailsJoinDetailsAdapter(List<GroupDetailsbyNamePojo> memberlist) {
        this.groupListPojos = memberlist;

    }

    @NonNull
    @Override
    public GroupDetailsJoinDetailsAdapter.BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.group_details_user_list,parent,false);
        mcon = parent.getContext();
        return new GroupDetailsJoinDetailsAdapter.BankViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull GroupDetailsJoinDetailsAdapter.BankViewHolder holder, int position) {
        GroupDetailsbyNamePojo businessModel = groupListPojos.get(position);
        holder.tv_neighbr_already_member.setText(businessModel.getNeighbrhood());
        holder.tv_pri_neighbrh_send_request.setText(businessModel.getNeighbrhood());
        holder.tv_already_member_user.setText(businessModel.getUsername());
        holder.tv_pri_user_send_reques.setText(businessModel.getUsername());
        if(isOwnerSelf) {
            if (type.equals("Public")){
                holder.l2_already_member_lyt.setVisibility(VISIBLE);
                holder.l3_request_send_lyt.setVisibility(GONE);
                holder.img_delete_group_by_owener.setVisibility(VISIBLE);
            }else {
                if (businessModel.getStatus().equals("1")) {
                    holder.l2_already_member_lyt.setVisibility(VISIBLE);
                    holder.l3_request_send_lyt.setVisibility(GONE);
                    holder.img_delete_group_by_owener.setVisibility(VISIBLE);
                } else {
                    holder.l2_already_member_lyt.setVisibility(GONE);
                    holder.img_delete_group_by_owener.setVisibility(GONE);
                    holder.l3_request_send_lyt.setVisibility(VISIBLE);
                }
            }
        }else {
            holder.l2_already_member_lyt.setVisibility(VISIBLE);
            holder.img_delete_group_by_owener.setVisibility(GONE);
            holder.l3_request_send_lyt.setVisibility(View.GONE);
        }
        if (businessModel.getUserphoto().isEmpty()) {
            holder.img_already_member_user.setImageResource(R.drawable.marketplace_white_background);
        } else{
            Picasso.get().load(businessModel.getUserphoto()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.marketplace_white_background)
                    .into(holder.img_already_member_user);
        }
        if (businessModel.getUserphoto().isEmpty()) {
            holder.img_pri_send_rqst_pic.setImageResource(R.drawable.marketplace_white_background);
        } else{
            Picasso.get().load(businessModel.getUserphoto()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.marketplace_white_background)
                    .into(holder.img_pri_send_rqst_pic);
        }
        holder.lnrClickProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // newRequest.onClickDetail(position, Integer.parseInt(businessModel.getUserid()));
                Intent detailScreen = new Intent(mcon, MyProfileOtherUser.class);
                detailScreen.putExtra("user_id",Integer.parseInt(businessModel.getUserid()));
                mcon.startActivity(detailScreen);
            }
        });
        holder.img_already_member_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // newRequest.onClickDetail(position, Integer.parseInt(businessModel.getUserid()));
                Intent detailScreen = new Intent(mcon, MyProfileOtherUser.class);
                detailScreen.putExtra("user_id",Integer.parseInt(businessModel.getUserid()));
                mcon.startActivity(detailScreen);
            }
        });
        holder.l3_request_send_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // newRequest.onClickDetail(position, Integer.parseInt(businessModel.getUserid()));
                Intent detailScreen = new Intent(mcon, MyProfileOtherUser.class);
                detailScreen.putExtra("user_id",Integer.parseInt(businessModel.getUserid()));
                mcon.startActivity(detailScreen);
            }
        });
        holder.img_delete_group_by_owener.setOnClickListener(view -> {
            exitLayout(position);
        });
        holder.frm_approved.setOnClickListener(view -> {
            newRequest.onApproval(position);
            groupListPojos.get(position).setStatus("1");
            notifyDataSetChanged();
            /*groupListPojos
            holder.l2_already_member_lyt.setVisibility(VISIBLE);
            new Handler(Looper.getMainLooper()).postDelayed(()->{
                groupListPojos.remove(position);
                notifyDataSetChanged();
            },1000);*/
        });
        holder.frm_declined.setOnClickListener(view -> {
            newRequest.declined(position);
            groupListPojos.get(position).setStatus("2");
           // notifyDataSetChanged();
            new Handler(Looper.getMainLooper()).postDelayed(()->{
                groupListPojos.remove(position);
                notifyDataSetChanged();
            },1000);
        });
    }
    @Override
    public int getItemCount() {
        return groupListPojos.size();
    }
    public class BankViewHolder extends RecyclerView.ViewHolder{
        CircularImageView img_owner_profile,img_already_member_user,img_pri_send_rqst_pic;
        LinearLayout root,lnrClickProfile;
        ImageView img_delete_group_by_owener;
        FrameLayout  l2_already_member_lyt,l3_request_send_lyt;
        FrameLayout frm_approved,frm_declined;
        TextView tv_neighbr_already_member,tv_already_member_user,tv_pri_user_send_reques,tv_pri_neighbrh_send_request;
        int id;
        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            lnrClickProfile = itemView.findViewById(R.id.lnrClickProfile);
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
            /*Intent i = new Intent(mcon, NearByBusinessActivity.class);
            id = i.getIntExtra("id",0);
*/
        }
    }
    public interface NewRequest{
        void onClick(int pos);
        void onexit(int groupid);
        void onApproval(int position);
        void declined(int position);
        void onClickRating(int pos);
        void onClickWriteReview(int pos);
        void onclickReview(int pos);
        void onClickDetail(int pos, int id);
    }
    public void exitLayout(int position){
        Dialog dialog = new Dialog(mcon);
        dialog.setContentView(R.layout.group_delete_by_admin);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(mcon, android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        TextView tv_no=dialog.findViewById(R.id.no_id);
        FrameLayout tv_yes=dialog.findViewById(R.id.yes_id);
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRequest.onexit(position);
                new Handler(Looper.getMainLooper()).postDelayed(()->{
                    groupListPojos.remove(position);
                    notifyDataSetChanged();
                },1000);
              //  HashMap<String, Object> hm = new HashMap<>();
                // Log.d("sjfdk",String.valueOf(userid));
                //  hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                //hm.put("userid",Integer.parseInt(sm.getString("user_id")));
             //   hm.put("userid", userid);
               // hm.put("groupname",groupListPojo.getGroupname() );
                // hm.put("username", sm.getString("user_name"));
               // userDeleteViewGroup(hm);
                dialog.dismiss();
                //businessDetailApi(id);
            }

        });
    }
}
