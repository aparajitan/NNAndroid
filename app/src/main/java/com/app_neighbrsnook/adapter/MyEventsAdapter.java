package com.app_neighbrsnook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.CreateEventModule;

import java.util.List;

public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.Holder> {
    public MyEventsAdapter(List<CreateEventModule> createEventModuleList, NewRequest newRequest) {
        this.createEventModuleList = createEventModuleList;
        this.newRequest = newRequest;
    }
    private final List<CreateEventModule>createEventModuleList;
     MyEventsAdapter.NewRequest newRequest;
    @NonNull
    @Override
    public MyEventsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_adapter_layout,parent,false);
        return new MyEventsAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyEventsAdapter.Holder holder, int position) {
        CreateEventModule createEventModel = createEventModuleList.get(position);
//        holder.profile_imageview.setImageURI(PollModel.getB_name());
        // holder.user_tv.setText("Sudhanshu");

//        holder.profile_imageview.setImageURI(PollModel.getB_name());
        //holder.img_db.setImageURI(Uri.parse(new File(createEventModel.getContactPhoto()).toString()));
        holder.tv_title.setText(createEventModel.getEvent_title());
        holder.tv_description.setText(createEventModel.getEvent_description());
        holder.tv_start_date.setText(createEventModel.getEvent_start_date());
        holder.tv_end_date.setText(createEventModel.getEvent_end_date());
    }
    @Override
    public int getItemCount() {
        return createEventModuleList.size();
    }
    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_description,tv_start_date,tv_end_date,tv_address_one,tv_address_two;
        ImageView img_db;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_event_title);
            tv_start_date=itemView.findViewById(R.id.tv_event_start_date);
            tv_description=itemView.findViewById(R.id.tv_description);
            tv_end_date=itemView.findViewById(R.id.tv_event_end_date);
        }
    }
    public interface NewRequest{
        void onClick(int pos);
        void onDetail(int pos);
        void threeDot(int pos);
        void onDetailPage(int pos);
    }
}
