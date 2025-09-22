package com.app_neighbrsnook.adapter;

import static com.app_neighbrsnook.R.color.them_color;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.SharedPreferencesHelper;
import com.app_neighbrsnook.model.neighbrhood.NearestNeighbrhood;
import com.app_neighbrsnook.myNeighbourhood.MyNeighbourhoodActivity;
import com.app_neighbrsnook.utils.GlobalMethods;

import java.util.ArrayList;
import java.util.List;

public class NeighbourhoodCategoryAdapter extends RecyclerView.Adapter<NeighbourhoodCategoryAdapter.ViewHolder> {

    List<NearestNeighbrhood> nearbyList = new ArrayList<>();
    NeighbrnookInterface subscriptionInterface;
    Context mcon;
    Boolean isUserVerified;
    AppCompatActivity activity;
    String ownerNeighbrhood;
    Boolean is_past = false;
    private int maxItemHeight = 0;

    public NeighbourhoodCategoryAdapter(Boolean is_past, Boolean is_current) {
        this.is_past = is_past;
        this.is_current = is_current;
    }

    Boolean is_current = false;


    public NeighbourhoodCategoryAdapter(List<NearestNeighbrhood> nearbyList, Boolean isUserVerified, NeighbrnookInterface subscriptionInterface) {
        this.nearbyList = nearbyList;
        this.subscriptionInterface = subscriptionInterface;
        this.isUserVerified = isUserVerified;
//        this.ownerNeighbrhood = ownerNeighbrhood;
    }

    @NonNull
    @Override
    public NeighbourhoodCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.neighbourhood_row, parent, false);
        mcon = parent.getContext();
        return new NeighbourhoodCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NeighbourhoodCategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nebourhood_name.setText(nearbyList.get(position).getName());
        holder.member_tv.setText(nearbyList.get(position).getMember() + " " + "Members");
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  holder.root.setBackgroundResource(R.drawable.round_corner_dialog_themcolor);
                //  holder.subscribe_tv.setBackgroundResource(R.drawable.unsubscribe_button);
                if (nearbyList.get(position).getStatus().equals("1")) {
                    //is_past=false;
                    subscriptionInterface.onNeighbrhoodDetail(nearbyList.get(position).getId(), nearbyList.get(position).getStatus());

                    // holder.root.setBackgroundResource(R.drawable.round_corner_dialog_themcolor);
                    //  holder.backgroundFollow.setVisibility(View.VISIBLE);
                    is_current = true;
                }
                if (nearbyList.get(position).getStatus().equals("2")) {
                    //holder.root.setBackgroundResource(R.drawable.round_corner_dialog_themcolor);
                    //holder.backgroundFollow.setVisibility(View.VISIBLE);

                    subscriptionInterface.onNeighbrhoodDetail(nearbyList.get(position).getId(), nearbyList.get(position).getStatus());
                }
                SharedPreferencesHelper.setValue(mcon, "pos", position);
            }
        });
        if (nearbyList.get(position).getStatus().equals("1")) {
//            if(holder.subscribe_tv.getText().equals("Subscribe")) {
            holder.nebourhood_name.setTextColor(mcon.getResources().getColor(them_color));
            holder.subscribe_tv.setText("Unfollow");
            holder.subscribe_tv.setBackgroundResource(R.drawable.unsubscribe_button);
//            }
        } else if (nearbyList.get(position).getStatus().equals("0")) {
//            holder.subscribe_tv.setTextColor(Color.GREEN);
            if (holder.subscribe_tv.getText().equals("Unsubscribe")) {
                holder.subscribe_tv.setText("Follow");
                holder.subscribe_tv.setBackgroundResource(R.drawable.round_corner_dialog_themcolor);
            }
        } else {
            holder.subscribe_tv.setText("Primary");
            holder.subscribe_tv.setBackgroundResource(R.drawable.green_background_button);
        }

        holder.subscribe_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
//                subscriptionInterface.onSubscriptionClick(position, holder.subscribe_tv.getText().toString());
                    if (holder.subscribe_tv.getText().equals("Primary")) {

                    } else {
                        final Dialog dialog = new Dialog(view.getContext()); // Context, this, etc.
                        dialog.setContentView(R.layout.subscribe_confirm_dialog);
                        TextView msg_textview = dialog.findViewById(R.id.msg_textview);
                        TextView cancle_textview = dialog.findViewById(R.id.cancel_textview);
                        msg_textview.setText("Are you sure you want to " + holder.subscribe_tv.getText().toString().toLowerCase() + "\n" + nearbyList.get(position).getName() + "?");
                        TextView ok = dialog.findViewById(R.id.confirm_textview);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (holder.subscribe_tv.getText().equals("Follow")) {
                                    holder.subscribe_tv.setText("Unfollow");
                                    holder.subscribe_tv.setBackgroundResource(R.drawable.round_corner_dialog_themcolor);
                                    subscriptionInterface.onSubscribeClick(nearbyList.get(position).getName());
                                } else if (holder.subscribe_tv.getText().equals("Unfollow")) {
                                    holder.subscribe_tv.setText("Follow");
                                    holder.subscribe_tv.setBackgroundResource(R.drawable.round_corner_dialog_themcolor);
                                    subscriptionInterface.onUnSubscribeClick(nearbyList.get(position).getName());
                                }
                                dialog.dismiss();

                            }

                        });
                        cancle_textview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
                        dialog.setCancelable(false);
                        dialog.show();
                    }

                } else {
                    GlobalMethods.getInstance(mcon).globalDialog(mcon, mcon.getString(R.string.unverified_msg));
                }

            }
        });
        int pos = SharedPreferencesHelper.getValue(mcon, "pos");
        if (pos == position) {
            holder.root.setBackgroundResource(R.drawable.card_bg_highlight);
            int row_index = -1;
            SharedPreferencesHelper.updateValue(mcon, "pos", row_index);
        } else {
            holder.root.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        if (maxItemHeight > 0) {
            ViewGroup.LayoutParams layoutParams = holder.nebourhood_name.getLayoutParams();
            layoutParams.height = maxItemHeight;
            holder.nebourhood_name.setLayoutParams(layoutParams);
        } else {
            holder.nebourhood_name.post(() -> {
                int height = holder.nebourhood_name.getHeight();
                if (height > maxItemHeight) {
                    maxItemHeight = height;
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return nearbyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView category_name_iv;
        TextView nebourhood_name, member_tv, subscribe_tv;
        CheckBox cb;
        LinearLayout root, backgroundFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nebourhood_name = itemView.findViewById(R.id.nebourhood_name);
            member_tv = itemView.findViewById(R.id.member_tv);
            subscribe_tv = itemView.findViewById(R.id.subscribe_tv);
            root = itemView.findViewById(R.id.root);
            // backgroundFollow = itemView.findViewById(R.id.backgroundFollow);
        }
    }

    public interface NeighbrnookInterface {
        void onSubscribeClick(String neighbrhood);

        void onUnSubscribeClick(String neighbrhood);

        void onNeighbrhoodDetail(String id, String status);
//        void onMyNeighbrhood(String id);
    }


}
