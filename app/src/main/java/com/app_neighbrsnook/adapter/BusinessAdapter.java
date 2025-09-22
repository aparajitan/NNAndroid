package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.Listdatum;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BankViewHolder> {
    private List<Listdatum> businessList;
    private final BusinessWallChildAdapter.ImageCallBack bicb;
    NewRequest newRequest;
    Context mcon;
    String value;
    WallChildAdapter.ImageCallBack icb;

    public BusinessAdapter(List<Listdatum> lists, String value, NewRequest newRequest, WallChildAdapter.ImageCallBack icb, BusinessWallChildAdapter.ImageCallBack bicb) {
        this.businessList = lists;
        this.newRequest = newRequest;
        this.value = value;
        this.icb = icb;
        this.bicb = bicb;
    }

    @NonNull
    @Override
    public BusinessAdapter.BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_row, parent, false);
        mcon = parent.getContext();
        return new BusinessAdapter.BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessAdapter.BankViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Listdatum businessModel = businessList.get(position);
        holder.businessName.setText(businessModel.getName());
        holder.businessType.setText(businessModel.getCategory());
        holder.businessDescription.setText(businessModel.getDescription());
        holder.business_tagline.setText(businessModel.getTagline());
        holder.created_date_tv.setText(businessModel.neighborhood);
        holder.user_tv.setText(businessModel.username);

        if (!businessModel.getRating().equals("0.0")) {
            holder.rate_now_textview.setText(businessModel.getRating());
        } else {
            holder.rate_now_textview.setText("--");
        }

        if (businessModel.getBusinessstatus().equals("1")) {
            holder.rating_rl.setVisibility(View.VISIBLE);
            holder.approval_rl.setVisibility(View.GONE);
            holder.aproval_panding_textview.setVisibility(View.GONE);
        }

        if (businessModel.getBusinessstatus().equals("3")) {
            holder.rating_rl.setVisibility(View.GONE);
            holder.approval_rl.setVisibility(View.VISIBLE);
            holder.aproval_panding_textview.setText("Approval pending");
        }

        if (businessModel.getBusinessstatus().equals("4")) {
            holder.rating_rl.setVisibility(View.GONE);
            holder.approval_rl.setVisibility(View.VISIBLE);
            holder.aproval_panding_textview.setText("Rejected");
        }

        if (businessModel.userpic.isEmpty()) {
            holder.profile_imageview.setImageResource(R.drawable.profile);
        } else {
            Picasso.get().load(businessModel.userpic).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.profile_imageview);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mcon, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setHasFixedSize(false);

        if ((businessList.get(position).getImage() != null && businessList.get(position).getImage().size() != 0)) {
            BusinessWallChildAdapter businessWallChildAdapter = new BusinessWallChildAdapter(Integer.parseInt(businessList.get(position).getId()), businessList.get(position).getImage(), holder.recyclerView.getContext(), bicb);
            holder.recyclerView.setAdapter(businessWallChildAdapter);
        }

        holder.threedot_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRequest.onThreeDotClick(businessList, position);
            }
        });
        holder.rate_now_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRequest.onClickRating(businessList.get(position).getId());
            }
        });
        holder.profile_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRequest.onClick(Integer.parseInt(businessModel.getUserid()));
            }
        });
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRequest.onClickDetail(position, Integer.parseInt(businessModel.id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    public class BankViewHolder extends RecyclerView.ViewHolder {
        ImageView threedot_imageview, porduct_image, profile_imageview, imageview1;
        TextView businessName, offers_textview, user_tv, created_date_tv;
        TextView businessType, business_tagline, aproval_panding_textview;
        TextView businessDescription, rate_now_textview, write_review_textview, read_review_textview;
        LinearLayout root, profile_ll;
        RelativeLayout rating_rl, approval_rl;
        RecyclerView recyclerView;

        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.rv_business_list);
            offers_textview = itemView.findViewById(R.id.offers_textview);
            threedot_imageview = itemView.findViewById(R.id.threedot_imageview);
            businessName = itemView.findViewById(R.id.business_name_textview);
            businessType = itemView.findViewById(R.id.business_type_textview);
            businessDescription = itemView.findViewById(R.id.business_desc_textview);
            business_tagline = itemView.findViewById(R.id.business_tagline);
            rate_now_textview = itemView.findViewById(R.id.rate_now_textview);
            write_review_textview = itemView.findViewById(R.id.write_review_textview);
            porduct_image = itemView.findViewById(R.id.porduct_image);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            user_tv = itemView.findViewById(R.id.user_tv);
            created_date_tv = itemView.findViewById(R.id.created_date_tv);
            root = itemView.findViewById(R.id.root);
            imageview1 = itemView.findViewById(R.id.imageview1);
            approval_rl = itemView.findViewById(R.id.approval_rl);
            rating_rl = itemView.findViewById(R.id.rating_rl);
            aproval_panding_textview = itemView.findViewById(R.id.aproval_panding_textview);
            profile_ll = itemView.findViewById(R.id.profile_ll);
        }
    }

    public interface NewRequest {
        void onClick(int pos);

        void onClickRating(String businessId);

        void onClickWriteReview(int pos);

        void onclickReview(int pos);

        void onClickDetail(int pos, int id);

        void onThreeDotClick(List<Listdatum> businessList, int pos);
    }

    public void filterList(ArrayList<Listdatum> filterdNames) {
        this.businessList = filterdNames;
        notifyDataSetChanged();
    }

    public void updateData(List<Listdatum> newData) {
        this.businessList = newData;
    }
}
