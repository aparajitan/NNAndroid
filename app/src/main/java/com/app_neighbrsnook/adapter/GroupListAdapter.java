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
import com.app_neighbrsnook.pojo.GroupListPojo;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {
    private List<GroupListPojo> groupListPojos;
    private NewRequest newRequest;
    private Context mcon;
    private Boolean isUserVerified;

    public GroupListAdapter(List<GroupListPojo> lists, NewRequest newRequest, boolean isUserVerified) {
        this.groupListPojos = lists;
        this.newRequest = newRequest;
        this.isUserVerified = isUserVerified;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_adapter_layout, parent, false);
        mcon = parent.getContext();
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GroupListPojo group = groupListPojos.get(position);

        // Set basic data
        setBasicData(holder, group);

        // Handle visibility based on user role and group status
        handleViewVisibility(holder, group);

        // Set click listeners
        setClickListeners(holder, group, position);
    }

    private void setBasicData(GroupViewHolder holder, GroupListPojo group) {
        holder.tv_user_name.setText(group.getUsername());
        holder.groupName.setText(group.getGroupname());
        holder.join_type.setText(group.getGroupType());
        holder.tv_rqst_pending.setText(group.getPendingRequestCount());
        holder.tv_memebers.setText(String.valueOf(group.getMembercount()));
        holder.user_neighbrhood.setText(group.getNeighbrhood());

        if (group.getImage().isEmpty()) {
            holder.profile_imageview.setImageResource(R.drawable.group_img_app);
        } else {
            Picasso.get().load(group.getImage()).fit().into(holder.profile_imageview);
        }
    }

    private void handleViewVisibility(GroupViewHolder holder, GroupListPojo group) {
        // Reset all views first
        resetAllViews(holder);

        String currentUserId = PrefMananger.GetLoginData(mcon).getId();
        boolean isOwner = currentUserId.equals(group.getUserid());
        String joinStatus = group.getGetjoin();
        boolean isPrivate = "Private".equals(group.getGroupType());

        if (isOwner) {
            // OWNER LOGIC - frm_owner should always be visible for owner
            holder.frm_owner.setVisibility(VISIBLE);

            // Show request pending badge if there are pending requests
            if (!group.getPendingRequestCount().equals("0")) {
                holder.frm_reqest_pending.setVisibility(VISIBLE);
                holder.frm_owner.setVisibility(GONE);
            }
        } else if (isPrivate) {
            // PRIVATE GROUP LOGIC for non-owners
            handlePrivateGroupViews(holder, joinStatus);
        } else {
            // PUBLIC GROUP LOGIC for non-owners
            handlePublicGroupViews(holder, joinStatus);
        }
    }

    private void resetAllViews(GroupViewHolder holder) {
        holder.frm_owner.setVisibility(GONE);
        holder.frm_join.setVisibility(GONE);
        holder.frm_exit.setVisibility(GONE);
        holder.frm_approval_pending.setVisibility(GONE);
        holder.frm_reqest_pending.setVisibility(GONE);
    }

    private void handlePrivateGroupViews(GroupViewHolder holder, String joinStatus) {
        switch (joinStatus) {
            case "pending":
                holder.frm_approval_pending.setVisibility(VISIBLE);
                break;
            case "joined":
                holder.frm_exit.setVisibility(VISIBLE);
                break;
            case "join":
                holder.frm_join.setVisibility(VISIBLE);
                break;
        }
    }

    private void handlePublicGroupViews(GroupViewHolder holder, String joinStatus) {
        if ("join".equals(joinStatus)) {
            holder.frm_join.setVisibility(VISIBLE);
        } else if ("joined".equals(joinStatus)) {
            holder.frm_exit.setVisibility(VISIBLE);
        }
    }

    private void setClickListeners(GroupViewHolder holder, GroupListPojo group, int position) {
        // Root click listener
        holder.root.setOnClickListener(v -> handleRootClick(group));

        // Exit button click listener
        holder.frm_exit.setOnClickListener(v -> {
            newRequest.onexit(Integer.parseInt(group.getGroupid()));
        });

        // Join button click listener
        holder.frm_join.setOnClickListener(v -> {
            if (isUserVerified) {
                if (mcon instanceof GroupActivity) {
                    newRequest.onApproval(position, group.getUsername(), group.getGroupname());
                } else {
                    newRequest.onApproval(Integer.parseInt(group.getGroupid()), group.getUsername(), group.getGroupname());
                }
            } else {
                GlobalMethods.getInstance(mcon).globalDialog(mcon, mcon.getString(R.string.unverified_msg));
            }
        });
    }

    private void handleRootClick(GroupListPojo group) {
        if (!isUserVerified) {
            GlobalMethods.getInstance(mcon).globalDialog(mcon, mcon.getString(R.string.unverified_msg));
            return;
        }

        String currentUserId = PrefMananger.GetLoginData(mcon).getId();
        boolean isOwner = currentUserId.equals(group.getUserid());
        boolean isPrivate = "Private".equals(group.getGroupType());
        String joinStatus = group.getGetjoin();

        if (isOwner) {
            // Owner can always access group details
            openGroupDetails(group);
        } else if (isPrivate) {
            // Private group logic for non-owners
            if ("joined".equals(joinStatus)) {
                openGroupDetails(group);
            } else if ("join".equals(joinStatus)) {
                privateJoinAlert();
            }
            // For "pending" status, do nothing
        } else {
            // Public group - always open details for non-owners
            openGroupDetails(group);
        }
    }

    private void openGroupDetails(GroupListPojo group) {
        Intent detailScreen = new Intent(mcon, GroupDetailActivity.class);
        detailScreen.putExtra("id", Integer.parseInt(group.getGroupid()));
        detailScreen.putExtra("data", group.getGroupType());
        detailScreen.putExtra("type", "detail");
        mcon.startActivity(detailScreen);
    }

    private void privateJoinAlert() {
        Dialog dialog = new Dialog(mcon);
        dialog.setContentView(R.layout.group_private_when_not_send_rqst);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(mcon, android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);

        FrameLayout tv_no = dialog.findViewById(R.id.post_frm);
        tv_no.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return groupListPojos.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_imageview;
        TextView tv_user_name, groupName, user_neighbrhood, join_type, tv_memebers, tv_rqst_pending;
        FrameLayout frm_reqest_pending, frm_join, frm_approval_pending, frm_exit, frm_owner;
        LinearLayout root;

        public GroupViewHolder(@NonNull View itemView) {
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
        }
    }

    public interface NewRequest {
        void onClick(int pos);
        void onexit(int groupid);
        void onApproval(int groupid, String getUsername, String getGroupname);
        void onClickDetail(int pos, int id);
    }

    public void filterList(ArrayList<GroupListPojo> filteredList) {
        this.groupListPojos = filteredList;
        notifyDataSetChanged();
    }
}