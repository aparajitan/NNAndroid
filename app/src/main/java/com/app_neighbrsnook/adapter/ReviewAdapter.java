package com.app_neighbrsnook.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.businessModule.BusinessDetailActivity;
import com.app_neighbrsnook.model.postComment.CommentPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.Reviewdatum;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Reviewdatum> reviewList = new ArrayList<>();
    Context mcon;
    SharedPrefsManager sm;
    CommentPojo commentPojo;

    public ReviewAdapter(List<Reviewdatum> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);
        mcon = parent.getContext();
        return new ReviewAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {

        holder.user_tv.setText(reviewList.get(position).getUsername());
        holder.created_date_tv.setText(reviewList.get(position).getNeighbrhood());
        holder.business_review.setText(reviewList.get(position).getReview());
        holder.timeReview.setText(reviewList.get(position).getReviewDate());
        if (reviewList.get(position).getUserpic().isEmpty()) {
            holder.profile_imageview.setImageResource(R.drawable.profile);
        } else {
            Picasso.get().load(reviewList.get(position).getUserpic()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile)
                    .into(holder.profile_imageview);
        }
        sm = new SharedPrefsManager(mcon);
        String id = sm.getString("user_id");
        holder.business_review.setOnLongClickListener(v -> {
            if (reviewList.get(position).getUserid().equals(id)) {
                showDeleteDialog(position, reviewList.get(position).getId());
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView threedot_imageview, profile_imageview;
        TextView user_tv, timeReview, created_date_tv, business_review;
        LinearLayout root;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            threedot_imageview = itemView.findViewById(R.id.threedot_imageview);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            user_tv = itemView.findViewById(R.id.user_tv);
            timeReview = itemView.findViewById(R.id.timeReview);
            created_date_tv = itemView.findViewById(R.id.created_date_tv);
            business_review = itemView.findViewById(R.id.business_review);
            root = itemView.findViewById(R.id.root);
        }
    }

    private void showDeleteDialog(int position, String reviewId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcon);
        builder.setTitle("Delete Review")
                .setMessage("Are you sure you want to delete this review?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    commentDeleteApi(position, reviewId);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void commentDeleteApi(int position, String reviwId) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("review_id", reviwId);
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<CommentPojo> call = service.reviewDeleteApi("deleteBusinessReview", hm);
        call.enqueue(new Callback<CommentPojo>() {
            @Override
            public void onResponse(Call<CommentPojo> call, Response<CommentPojo> response) {
                commentPojo = response.body();
                if (commentPojo.getStatus().equals("success")) {
                    sm = new SharedPrefsManager(mcon);
                    int id = sm.getInt("business_id");
                    Intent intent = new Intent(mcon, BusinessDetailActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", "detail");
                    mcon.startActivity(intent);
//                    reviewList.remove(position);
//                    notifyItemRemoved(position);
                }
            }

            @Override
            public void onFailure(Call<CommentPojo> call, Throwable t) {

            }
        });
    }

}
