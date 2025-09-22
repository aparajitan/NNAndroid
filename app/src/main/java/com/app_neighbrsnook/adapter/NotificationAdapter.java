package com.app_neighbrsnook.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.notification.Nbdatum;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    List<Nbdatum> nearbyList = new ArrayList<>();
    Context mcon;
    NotificationOnClick notificationOnClick;
    public NotificationAdapter(List<Nbdatum> nearbyList, NotificationOnClick notificationOnClick) {
        this.nearbyList = nearbyList;
        this.notificationOnClick = notificationOnClick;
    }
    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, parent, false);
        mcon = parent.getContext();
        return new NotificationAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        switch (nearbyList.get(position).getNotificationType()) {
            case "Business":
                setData(nearbyList.get(position).getTitle(), nearbyList.get(position).getMessage(), nearbyList.get(position).getCreateon(), R.drawable.round_corner_business, holder, R.color.gradient_business, "Business", position);
                break;

            case "Group":
                setData(nearbyList.get(position).getTitle(), nearbyList.get(position).getMessage(), nearbyList.get(position).getCreateon(), R.drawable.group_round_corner, holder, R.color.gradient_group, "Group", position);
                break;

            case "Poll":
                setData(nearbyList.get(position).getTitle(), nearbyList.get(position).getMessage(), nearbyList.get(position).getCreateon(), R.drawable.poll_round_corner, holder, R.color.gradient_poll, "Poll", position);
                break;

            case "Post":
                setData(nearbyList.get(position).getTitle(), nearbyList.get(position).getMessage(), nearbyList.get(position).getCreateon(), R.drawable.post_round_corner, holder, R.color.gradient_post, "Post", position);
                break;

            case "Event":
                setData(nearbyList.get(position).getTitle(), nearbyList.get(position).getMessage(), nearbyList.get(position).getCreateon(), R.drawable.event_round_corner, holder, R.color.gradient_event, "Event", position);
                break;

            case "Announcement":
                setData(nearbyList.get(position).getTitle(), nearbyList.get(position).getMessage(), nearbyList.get(position).getCreateon(), R.drawable.announcement_round_corner, holder, R.color.gradient_announcement, "Post", position);
                break;

            case "Directmessage":
                setData(nearbyList.get(position).getTitle(), nearbyList.get(position).getMessage(), nearbyList.get(position).getCreateon(), R.drawable.dm_round_corner, holder, R.color.gradient_dm, "Directmessage", position);
                break;

            case "Welcome":
                setData(nearbyList.get(position).getTitle(), nearbyList.get(position).getMessage(),
                        nearbyList.get(position).getCreateon(), R.drawable.post_round_corner, holder, R.color.gradient_post, "Welcome", position);
                break;
            case "Groupchat":
                setData(nearbyList.get(position).getTitle(), nearbyList.get(position).getMessage(), nearbyList.get(position).getCreateon(), R.drawable.group_round_corner,
                        holder, R.color.gradient_group, "Groupchat", position);

                break;
            case "Marketplacechat":
                setData(nearbyList.get(position).getTitle(), nearbyList.get(position).getMessage(), nearbyList.get(position).getCreateon(), R.drawable.favorite_round_corner,
                        holder, R.color.gradient_favorite, "Marketplacechat", position);
                break;
        }

    }

    private void setData(String title, String message, String time, int poll_round_corner, ViewHolder holder, int color, String type, int position) {
        holder.title_tv.setText(title);
        holder.desc_tv.setText(message);
        holder.root.setBackgroundResource(poll_round_corner);
        holder.time_tv.setText(time);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.title_tv.setTextColor(mcon.getColor(color));
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type) {
                    case "Business":
                        notificationOnClick.onClickBusiness(Integer.parseInt(nearbyList.get(position).getId()), Integer.parseInt(nearbyList.get(position).getNotificationId()));
                        break;

                    case "Group":
                        if ((nearbyList.get(position).getTitle()).equals("Group Delete")) {

                        } else {
                            notificationOnClick.onClickGroup(position, nearbyList);
                        }
                        break;

                    case "Poll":
                        notificationOnClick.onClickPoll(Integer.parseInt(nearbyList.get(position).getId()), Integer.parseInt(nearbyList.get(position).getNotificationId()));
                        break;

                    case "Post":
                        if (nearbyList.get(position).getTitle().equals("Comment on Post")) {
                            notificationOnClick.onClickPostComment(nearbyList.get(position).getId(), nearbyList.get(position).getTitle(), Integer.parseInt(nearbyList.get(position).getNotificationId()));
                        } else {
                            notificationOnClick.onClickPost(Integer.parseInt(nearbyList.get(position).getId()), nearbyList.get(position).getTitle(), Integer.parseInt(nearbyList.get(position).getNotificationId()));
                        }
                        break;

                    case "Announcement":
                        notificationOnClick.onClickPost(Integer.parseInt(nearbyList.get(position).getId()), "", Integer.parseInt(nearbyList.get(position).getNotificationId()));
                        break;

                    case "Event":
                        notificationOnClick.onClickEvent(Integer.parseInt(nearbyList.get(position).getId()), Integer.parseInt(nearbyList.get(position).getNotificationId()));
                        break;

                    case "Directmessage":
                        notificationOnClick.onClickDm(Integer.parseInt(nearbyList.get(position).getCreateOwner()), Integer.parseInt(nearbyList.get(position).getNotificationId()), nearbyList.get(position).getOwnerName(), nearbyList.get(position).getOwnerPic());
                        break;

                    case "Welcome":
                        notificationOnClick.onClickWelcom(Integer.parseInt(nearbyList.get(position).getId()), Integer.parseInt(nearbyList.get(position).getNotificationId()));
                        break;
                    case "Groupchat":
                        notificationOnClick.onClickGroupChat(position, nearbyList);
                        break;

                    case "Marketplacechat":
                        notificationOnClick.onClickMarketPlaceChat(Integer.parseInt(nearbyList.get(position).getId()), Integer.parseInt(nearbyList.get(position).getNotificationId()));
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return nearbyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv, desc_tv, time_tv;
        LinearLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            desc_tv = itemView.findViewById(R.id.desc_tv);
            root = itemView.findViewById(R.id.root);
            time_tv = itemView.findViewById(R.id.time_tv);
        }
    }

    public interface NotificationOnClick {
        void onClickBusiness(int id, int notificationId);

        void onClickGroup(int pos, List<Nbdatum> nearbyList);

        void onClickPoll(int id, int notificationId);

        void onClickPost(int id, String title, int notificationId);

        void onClickEvent(int id, int notificationId);

        void onClickWelcom(int id, int notificationId);

        void onClickDm(int id, int notificationId, String userName, String userImg);

        void onClickGroupChat(int pos, List<Nbdatum> nearbyList);

        void onClickMarketPlaceChat(int id, int notificationId);

        void onClickPostComment(String postId, String title, int notificationId);
    }

    public void updateData(List<Nbdatum> newData) {
        this.nearbyList = newData;
    }
}