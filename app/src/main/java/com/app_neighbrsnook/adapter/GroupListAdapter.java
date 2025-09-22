package com.app_neighbrsnook.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.group.GroupActivity;
import com.app_neighbrsnook.group.GroupDetailActivity;
import com.app_neighbrsnook.nearBy.NearByBusinessActivity;
import com.app_neighbrsnook.pojo.GroupListPojo;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.BankViewHolder> {
    private List<GroupListPojo> groupListPojos;
    NewRequest newRequest;
    Context mcon;
    int id;
    Boolean isUserVerified;

    public GroupListAdapter(List<GroupListPojo> lists, NewRequest newRequest, boolean isUserVerified) {
        this.groupListPojos = lists;
        this.newRequest = newRequest;
        this.isUserVerified = isUserVerified;
    }

    @NonNull
    @Override
    public GroupListAdapter.BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_adapter_layout, parent, false);
        mcon = parent.getContext();
        return new GroupListAdapter.BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.BankViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GroupListPojo businessModel = groupListPojos.get(position);
        holder.tv_user_name.setText(businessModel.getUsername());
        holder.groupName.setText(businessModel.getGroupname());
        holder.join_type.setText(businessModel.getGroupType());
        holder.tv_rqst_pending.setText(businessModel.getPendingRequestCount());
        holder.tv_memebers.setText(String.valueOf(businessModel.getMembercount()));
        holder.user_neighbrhood.setText(businessModel.getNeighbrhood());

        if (PrefMananger.GetLoginData(mcon).getId().equals(businessModel.getUserid())) {
            holder.frm_exit.setVisibility(View.GONE);
            holder.frm_join.setVisibility(View.GONE);
            holder.frm_approval_pending.setVisibility(GONE);
            holder.frm_owner.setVisibility(VISIBLE);
        } else if (businessModel.getGetjoin().equals("join")) {
            holder.frm_exit.setVisibility(GONE);
            holder.frm_join.setVisibility(VISIBLE);
            holder.frm_reqest_pending.setVisibility(GONE);
            holder.frm_owner.setVisibility(GONE);
        } else if (businessModel.getGetjoin().equals("joined")) {
            holder.frm_exit.setVisibility(VISIBLE);
            holder.frm_join.setVisibility(GONE);
            holder.frm_owner.setVisibility(GONE);
            holder.frm_reqest_pending.setVisibility(GONE);
        }
        if (businessModel.getGroupType().equals("Private")) {
            // exitLayout();
            if (businessModel.getGetjoin().equals("pending")) {
                holder.frm_approval_pending.setVisibility(VISIBLE);
                holder.frm_join.setVisibility(GONE);
                holder.frm_exit.setVisibility(GONE);
                holder.frm_reqest_pending.setVisibility(GONE);
                holder.frm_owner.setVisibility(GONE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            } else if (businessModel.getGetjoin().equals("joined")) {
                holder.frm_exit.setVisibility(VISIBLE);
                holder.frm_join.setVisibility(GONE);
                holder.frm_reqest_pending.setVisibility(GONE);
                holder.frm_approval_pending.setVisibility(GONE);
                holder.frm_owner.setVisibility(GONE);
            } else if (PrefMananger.GetLoginData(mcon).getId().equals(businessModel.getUserid())) {
                holder.frm_approval_pending.setVisibility(GONE);
                holder.frm_join.setVisibility(GONE);
                holder.frm_owner.setVisibility(VISIBLE);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent detailScreen = new Intent(mcon, GroupDetailActivity.class);
                        detailScreen.putExtra("id", Integer.parseInt(businessModel.getGroupid()));
                        //  detailScreen.putExtra("data",businessModel);
                        detailScreen.putExtra("type", "detail");
                        mcon.startActivity(detailScreen);
                    }
                });
            } else {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isUserVerified) {
                            privateJoinAlert();
                        }
                    }
                });
            }
            if (PrefMananger.GetLoginData(mcon).getId().equals(businessModel.getUserid())) {
                if (businessModel.getPendingRequestCount().equals("0")) {
                    holder.frm_reqest_pending.setVisibility(GONE);
                    holder.frm_owner.setVisibility(VISIBLE);
                } else {
                    holder.frm_reqest_pending.setVisibility(VISIBLE);
                    holder.frm_owner.setVisibility(GONE);
                }
            }
        }
        if (businessModel.getImage().isEmpty()) {
            holder.profile_imageview.setImageResource(R.drawable.group_img_app);
        } else {
            Picasso.get().load(businessModel.getImage()).fit().into(holder.profile_imageview);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
                    Intent detailScreen = new Intent(mcon, GroupDetailActivity.class);
                    detailScreen.putExtra("id", Integer.parseInt(businessModel.getGroupid()));
                    detailScreen.putExtra("data", businessModel.getGroupType());
                    detailScreen.putExtra("type", "detail");
                    mcon.startActivity(detailScreen);
                } else {
                    GlobalMethods.getInstance(mcon).globalDialog(mcon, mcon.getString(R.string.unverified_msg));
                }
            }
        });

        holder.frm_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRequest.onexit(Integer.parseInt(businessModel.getGroupid()));
                if (groupListPojos.get(position).getStatus().equals("success")) {
                    holder.frm_exit.setVisibility(GONE);
                    holder.frm_join.setVisibility(VISIBLE);
                    holder.frm_owner.setVisibility(GONE);
                }
            }
        });

        holder.frm_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
                    if (mcon instanceof GroupActivity) {
                        newRequest.onApproval(position, businessModel.getUsername(), businessModel.getGroupname());
                    } else {
                        newRequest.onApproval(Integer.parseInt(businessModel.getGroupid()), businessModel.getUsername(), businessModel.getGroupname());
                    }
                    if (groupListPojos.get(position).getStatus().equals("joined")) {
                        holder.frm_join.setVisibility(GONE);
                        holder.frm_approval_pending.setVisibility(GONE);
                        holder.frm_exit.setVisibility(VISIBLE);
                    } else if (groupListPojos.get(position).getStatus().equals("pending")) {
                        holder.frm_exit.setVisibility(GONE);
                        holder.frm_approval_pending.setVisibility(VISIBLE);
                        holder.frm_join.setVisibility(GONE);
                        holder.frm_owner.setVisibility(GONE);
                    }
                } else {
                    GlobalMethods.getInstance(mcon).globalDialog(mcon, mcon.getString(R.string.unverified_msg));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupListPojos.size();
    }

    public class BankViewHolder extends RecyclerView.ViewHolder {
        ImageView threedot_imageview, porduct_image, profile_imageview;
        TextView tv_user_name, groupName, user_neighbrhood, join_type, tv_memebers, offers_textview, user_tv, created_date_tv;
        FrameLayout frm_reqest_pending, frm_join, frm_approval_pending, frm_exit, frm_owner;
        LinearLayout root;
        TextView tv_rqst_pending;
        int id;

        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_rqst_pending = itemView.findViewById(R.id.tv_request_count);
            tv_user_name = itemView.findViewById(R.id.amar_dubey);
            frm_exit = itemView.findViewById(R.id.frm_exit);
            frm_approval_pending = itemView.findViewById(R.id.frm_approval_pending);
            frm_join = itemView.findViewById(R.id.frm_join_id);
            frm_reqest_pending = itemView.findViewById(R.id.frm_request_pending);
            tv_memebers = itemView.findViewById(R.id.tv_all_members);
            join_type = itemView.findViewById(R.id.who_can_join);
            user_neighbrhood = itemView.findViewById(R.id.neighbrhood_id);
            groupName = itemView.findViewById(R.id.group_name_id);
            profile_imageview = itemView.findViewById(R.id.id_one_image);
            root = itemView.findViewById(R.id.lnr_root);
            frm_owner = itemView.findViewById(R.id.frm_owner_id);
            Intent i = new Intent(mcon, NearByBusinessActivity.class);
            id = i.getIntExtra("id", 0);
        }
    }

    public interface NewRequest {
        void onClick(int pos);

        void onexit(int groupid);

        void onApproval(int groupid, String getUsername, String getGroupname);

        void onClickDetail(int pos, int id);
    }

    public void privateJoinAlert() {
        Dialog dialog = new Dialog(mcon);
        dialog.setContentView(R.layout.group_private_when_not_send_rqst);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(mcon, android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        FrameLayout tv_no = dialog.findViewById(R.id.post_frm);
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void filterList(ArrayList<GroupListPojo> filteredList) {
        this.groupListPojos = filteredList;
        notifyDataSetChanged();
    }
}
