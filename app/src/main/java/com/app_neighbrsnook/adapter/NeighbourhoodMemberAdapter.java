package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.neighborhoodMember.Listdatum;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NeighbourhoodMemberAdapter extends RecyclerView.Adapter<NeighbourhoodMemberAdapter.ViewHolder> {
    List<Listdatum> nearbyList = new ArrayList<>();
    Context mcon;
    NeighbourhoodMemberAdapter.UserCallBack ucb;

    public NeighbourhoodMemberAdapter(List<Listdatum> nearbyList, UserCallBack ucb) {
        this.nearbyList = nearbyList;
        this.ucb = ucb;
    }

    @NonNull
    @Override
    public NeighbourhoodMemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.neighbourhood_member_item, parent, false);
        mcon = parent.getContext();
        return new NeighbourhoodMemberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NeighbourhoodMemberAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title_tv.setText(nearbyList.get(position).getFullname());
        holder.desc_tv.setText(nearbyList.get(position).getNeighbrhood());
        if (nearbyList.get(position).getUserpic().equals("")) {
        } else {
            Picasso.get().load(nearbyList.get(position).getUserpic()).into(holder.profile_imageview);
        }
        if (position == nearbyList.size() - 1) {
            holder.divider.setVisibility(View.GONE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
        }
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = Integer.parseInt(nearbyList.get(position).getId());
                String name = nearbyList.get(position).getFullname();
                String userPic = nearbyList.get(position).getUserpic();
                int isBlocked = nearbyList.get(position).getIs_blocked();  // <-- yeh line important hai
                ucb.onUserClick(id, name, userPic, isBlocked);
            }
        });
        holder.block_toggle.setOnCheckedChangeListener(null); // Remove previous listener
        int isBlocked = nearbyList.get(position).getIs_blocked(); // int value (0 = unblocked, 1 = blocked)
        holder.block_toggle.setChecked(isBlocked == 1); // 1 means checked (blocked), 0 means unchecked (unblocked)
        holder.block_toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // BLOCK
                new AlertDialog.Builder(mcon)
                        .setTitle("Confirm Block")
                        .setMessage("Are you sure you want to block " + holder.title_tv.getText().toString() + "?")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> {
                            Toast.makeText(mcon, holder.title_tv.getText().toString() + " is blocked", Toast.LENGTH_SHORT).show();
                            ucb.onUserBlock(nearbyList.get(position).getId(), "block");
                            nearbyList.get(position).setIs_blocked(1); // update locally
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                            holder.block_toggle.setOnCheckedChangeListener(null);
                            holder.block_toggle.setChecked(false); // revert toggle
                            notifyItemChanged(holder.getAdapterPosition());
                        })
                        .show();
            } else {
                // UNBLOCK
                Toast.makeText(mcon, holder.title_tv.getText().toString() + " is unblocked", Toast.LENGTH_SHORT).show();

                ucb.onUserBlock(nearbyList.get(position).getId(), "unblock");
                nearbyList.get(position).setIs_blocked(0); // update locally
            }
        });
    }
    @Override
    public int getItemCount() {
        return nearbyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv, desc_tv;
        LinearLayout root;
        CircleImageView profile_imageview;
        View divider;
        SwitchCompat block_toggle;
        TextView block_status_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            desc_tv = itemView.findViewById(R.id.desc_tv);
            root = itemView.findViewById(R.id.root);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            divider = itemView.findViewById(R.id.vLineChatList);
            block_toggle = itemView.findViewById(R.id.block_toggle);
            block_status_tv = itemView.findViewById(R.id.block_status_tv);
        }
    }

    public interface UserCallBack {
        void onUserClick(int id, String name, String userPic, int isBlocked);
        void onUserBlock(String memberUserId, String flag);
    }

    public void filterList(List<Listdatum> filterdNames) {
        this.nearbyList = filterdNames;
        notifyDataSetChanged();
    }

}
