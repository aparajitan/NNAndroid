package com.app_neighbrsnook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.postComment.Postlistdatum;
import com.app_neighbrsnook.pojo.eventDetails.EventListComentPojo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventCommentAdapter extends RecyclerView.Adapter<EventCommentAdapter.ViewHolder> {

    List<EventListComentPojo> commentList = new ArrayList<>();
    Context mcon;

    public EventCommentAdapter(List<EventListComentPojo> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public EventCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        mcon = parent.getContext();
        return new EventCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventCommentAdapter.ViewHolder holder, int position) {
        holder.message_tv.setText(commentList.get(position).getCommenttext());
        holder.name_tv.setText(commentList.get(position).getUsername());
        //holder.location_tv.setText(commentList.get(position).getNeighbrhood()+", "+commentList.get(position).getCreateon());
        if (commentList.get(position).getUserpic().isEmpty()) {
            holder.profile_imageview.setImageResource(R.drawable.profile);
        } else{
            Picasso.get().load(commentList.get(position).getUserpic()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile)
                    .into(holder.profile_imageview);
        }
        if (position == commentList.size()-1) {
            //holder.vwLine.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView message_tv,name_tv, time_tv, location_tv;
        View vwLine;
        ImageView profile_imageview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message_tv = itemView.findViewById(R.id.message_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            time_tv = itemView.findViewById(R.id.time_tv);
           // location_tv = itemView.findViewById(R.id.location_tv);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
        //    vwLine =itemView.findViewById(R.id.wv_line);
        }
    }

}
