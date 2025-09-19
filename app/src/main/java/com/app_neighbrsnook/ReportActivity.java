package com.app_neighbrsnook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.adapter.MediaSliderAdapter;
import com.app_neighbrsnook.adapter.ReportTypeAdapter;
import com.app_neighbrsnook.adapter.WallChildAdapter;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.pojo.reportModule.Listdatum;
import com.app_neighbrsnook.pojo.reportModule.ReportTypePojo;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity implements ReportTypeAdapter.CategoryInterface, WallChildAdapter.ImageCallBack {
    TextView titleTv, submit_tv, report_tv, report_category, user_tv, neighbrhood_name_tv, source_tv, desc_tv;
    Spinner spinner;
    Dialog preview_dialog;
    ImageView back_btn;
    ImageView searchImageView, addImageView, profile_imageview;
    List<Listdatum> stateList = new ArrayList<>();
    ReportTypeAdapter reportTypeAdapter;
    Dialog mail_dialog;
    RecyclerView rv;
    EditText desc_et;
    int categoryId;
    SharedPrefsManager sm;
    Intent intent;
    RecyclerView report_rv;
    RelativeLayout root;
    ArrayList<String> postImg = new ArrayList<>();
    String strUsername, strUserPic, strNeighbrhood, strPostType, strSubject, postId;
    WallChildAdapter childRecyclerViewAdapter;
    WallChildAdapter.ImageCallBack imageCallBack = this;
    LinearSnapHelper snapHelper = new LinearSnapHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report2);

        sm = new SharedPrefsManager(this);
        intent = getIntent();

        item();
        reportType();

        titleTv.setText("Report");
        searchImageView.setVisibility(View.GONE);
        addImageView.setVisibility(View.GONE);

        List<ImagePojo> postImages = getIntent().getParcelableArrayListExtra("postImages");
        postImg = intent.getStringArrayListExtra("data");
        strUsername = intent.getStringExtra("userName");
        strUserPic = intent.getStringExtra("profilePhoto");
        strNeighbrhood = intent.getStringExtra("neighborhood");
        strPostType = intent.getStringExtra("post_type");
        strSubject = intent.getStringExtra("subject");
        postId = intent.getStringExtra("post_id");

        user_tv.setText(strUsername);
        neighbrhood_name_tv.setText(strNeighbrhood);
        source_tv.setText(strPostType);
        desc_tv.setText(strSubject);
        Picasso.get().load(strUserPic).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile)
                .into(profile_imageview);

        if ((postImages != null && postImages.size() != 0)) {
            report_rv.setLayoutManager(new LinearLayoutManager(ReportActivity.this, LinearLayoutManager.HORIZONTAL, false));
            childRecyclerViewAdapter = new WallChildAdapter(postImages, ReportActivity.this, imageCallBack);
            report_rv.setAdapter(childRecyclerViewAdapter);
            snapHelper.attachToRecyclerView(report_rv);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stateList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard1();
                return false;
            }
        });
        report_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory();
            }
        });
        submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!report_category.getText().toString().equals("Select category")) {
                    if (!desc_et.getText().toString().isEmpty()) {
                        reportApi();
                        onBackPressed();
                    } else {
                        desc_et.setError("Please state your concern.");
                    }
                } else {
                    report_category.setError("Please select category first.");
}
            }
        });
    }

    private void item() {
        titleTv = findViewById(R.id.title);
        report_category = findViewById(R.id.business_category);
        submit_tv = findViewById(R.id.submit_tv);
        report_tv = findViewById(R.id.report_tv);
        searchImageView = findViewById(R.id.search_imageview);
        addImageView = findViewById(R.id.add_imageview);
        report_rv = findViewById(R.id.report_rv);
        profile_imageview = findViewById(R.id.profile_imageview);
        user_tv = findViewById(R.id.user_tv);
        desc_et = findViewById(R.id.desc_et);
        neighbrhood_name_tv = findViewById(R.id.neighbrhood_name_tv);
        source_tv = findViewById(R.id.source_tv);
        desc_tv = findViewById(R.id.desc_tv);
        root = findViewById(R.id.root);
        back_btn = findViewById(R.id.back_btn);
    }

    private void reportApi() {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("neighbrhood", sm.getString("neighbrhood"));
        hm.put("report_id", Integer.parseInt(intent.getStringExtra("post_id"))); //id of the post/poll/business
        hm.put("type", "Post");
        hm.put("description", desc_et.getText().toString());
        hm.put("tags", categoryId);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<ReportTypePojo> call = service.reportApi("doreport", hm);
        call.enqueue(new Callback<ReportTypePojo>() {
            @Override
            public void onResponse(Call<ReportTypePojo> call, Response<ReportTypePojo> response) {
                if (response.body().getStatus().equals("success")) {
                    Toast.makeText(ReportActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportTypePojo> call, Throwable t) {
//                AppCommon.getInstance(PlaceOrderActivity.this).clearNonTouchableFlags(PlaceOrderActivity.this);
                Toast.makeText(ReportActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void reportType() {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<ReportTypePojo> call = service.reportTypeApi("reportitems");
        call.enqueue(new Callback<ReportTypePojo>() {
            @Override
            public void onResponse(Call<ReportTypePojo> call, Response<ReportTypePojo> response) {
                if (response.body().getStatus().equals("success")) {
                    stateList = response.body().getListdata();
                }
            }

            @Override
            public void onFailure(Call<ReportTypePojo> call, Throwable t) {
                Toast.makeText(ReportActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void selectCategory() {
        ImageView cancel;
        mail_dialog = new Dialog(this);
        mail_dialog.setContentView(R.layout.open_category_list);
        rv = mail_dialog.findViewById(R.id.rv_category);
        cancel = mail_dialog.findViewById(R.id.cross_imageview);
        TextView tv_itm = mail_dialog.findViewById(R.id.tv_itm);
        tv_itm.setText("Choose report category");
        rv.setLayoutManager(new LinearLayoutManager(this));
        reportTypeAdapter = new ReportTypeAdapter(ReportActivity.this, stateList);
        rv.setAdapter(reportTypeAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_dialog.cancel();
            }
        });
        mail_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ReportActivity.this, android.R.color.transparent)));
        mail_dialog.setCancelable(true);
        mail_dialog.show();
    }

    @Override
    public void onClick(String categoryName, String id) {
        report_category.setText(categoryName);
        categoryId = Integer.parseInt(id);
        mail_dialog.dismiss();
    }

    private void hideKeyboard1() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    @Override
    public void onImageClick(List<ImagePojo> list, int pos) {
        imageDialog(list, pos);
    }

    private void imageDialog(List<ImagePojo> img, int pos) {
        //Shubham Upadte
        RecyclerView rv;
        ImageView cancel;
        Dialog image_dialog = new Dialog(this);
        image_dialog.setContentView(R.layout.open_image_dialog);
        rv = image_dialog.findViewById(R.id.rv_image);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        rv.setLayoutManager(new LinearLayoutManager(ReportActivity.this, LinearLayoutManager.HORIZONTAL, false));
        MediaSliderAdapter mediaSliderAdapter = new MediaSliderAdapter(image_dialog, img, "detail", pos, ReportActivity.this);
        rv.setAdapter(mediaSliderAdapter);
        rv.scrollToPosition(pos);
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rv);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.cancel();
            }
        });

        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ReportActivity.this, android.R.color.black)));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(image_dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        image_dialog.getWindow().setAttributes(layoutParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image_dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            image_dialog.getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, android.R.color.black)));
        }
        image_dialog.setCancelable(true);
        image_dialog.show();
    }
}