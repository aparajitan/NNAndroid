package com.app_neighbrsnook;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.app_neighbrsnook.adapter.ReviewAdapter;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.ReviewPojo;
import com.app_neighbrsnook.pojo.Reviewdatum;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewBottomSheetFragment extends BottomSheetDialogFragment {
    RecyclerView review_rv;
    LinearLayoutManager layoutManager1;
    HashMap<String, Object> reviewList = new HashMap<>();
    SharedPrefsManager sm;
    public static ReviewBottomSheetFragment newInstance() {
        ReviewBottomSheetFragment fragment = new ReviewBottomSheetFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet_review, null);
        review_rv = contentView.findViewById(R.id.review_rv);
        sm = new SharedPrefsManager(getActivity());
        layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        review_rv.setLayoutManager(layoutManager1);
        reviewList = new HashMap<>();
        reviewList.put("business_id", sm.getInt("business_id"));
        reviewList.put("userid", Integer.parseInt(sm.getString("user_id")));
        reviewListApi(reviewList);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }
    private void reviewListApi(HashMap<String, Object> reviewList) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<ReviewPojo> call = service.listReview("allreviewlist", reviewList );
        call.enqueue(new Callback<ReviewPojo>() {
            @Override
            public void onResponse(Call<ReviewPojo> call, Response<ReviewPojo> response) {
                List<Reviewdatum> reviewListData =  response.body().getListdata();
                if(response.body().getMessage().equals("Data Not Found"))
                {
//                    reviews_ll.setVisibility(View.GONE);
                }
                else
                {
                    ReviewAdapter reviewAdapter= new ReviewAdapter(reviewListData);
                    review_rv.setAdapter(reviewAdapter);
                }


            }

            @Override
            public void onFailure(Call<ReviewPojo> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }

    @Override public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }



}