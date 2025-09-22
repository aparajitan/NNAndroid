package com.app_neighbrsnook.businessModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.adapter.BusinessWallChildAdapter;
import com.app_neighbrsnook.adapter.MediaSliderAdapter;
import com.app_neighbrsnook.adapter.WallChildAdapter;
import com.app_neighbrsnook.fragment.BottomSheetBusinessFragment;
import com.app_neighbrsnook.intreface.OnUpdateFavListenerBusiness;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.pojo.RateNowPojo;
import com.app_neighbrsnook.profile.MyProfileOtherUser;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.BusinessAdapter;
import com.app_neighbrsnook.adapter.BusinessCategoryAdapter;
import com.app_neighbrsnook.model.BusinessModel;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.BusinessListPojo;
import com.app_neighbrsnook.pojo.Listdatum;
import com.app_neighbrsnook.pojo.Nbdatum;
import com.app_neighbrsnook.pojo.StatePojo;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessActivity extends AppCompatActivity implements View.OnClickListener, BusinessCategoryAdapter.CategoryInterface, BusinessAdapter.NewRequest,
        CreateBusinessPageActivity.Router, OnUpdateFavListenerBusiness, WallChildAdapter.ImageCallBack, BusinessWallChildAdapter.ImageCallBack {
    TextView titleTv, business_category;
    ImageView searchImageView, addImageView, back_btn, cancel_iv;
    SharedPrefsManager sm;
    RecyclerView rv_business;
    List<BusinessModel> businessList = new ArrayList<>();
    List<Listdatum> listdata = new ArrayList<>();
    Dialog rating_dialog, mail_dialog, dialog;
    RelativeLayout search_rl;
    EditText search_et;
    BusinessAdapter businessAdapter;
    List<Nbdatum> categorylist = new ArrayList<>();
    HashMap<String, Object> hm = new HashMap<>();
    String nbr, value, type;
    RatingBar ratingBar;
    int userid_businesslist, fromuserid;
    boolean isVerifiedUser = true;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        sm = new SharedPrefsManager(this);
        Intent i = getIntent();
        fromuserid = i.getIntExtra("user_id", 0);
        type = i.getStringExtra("type");
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("title")) {
            value = extras.getString("title");
            if (value.equals("createbusiness")) {
                userid_businesslist = Integer.parseInt(sm.getString("user_id"));
                hm.put("businessuserlist", userid_businesslist);
                globalDialog("Business created succesfully. Wait for approval");
            } else if (value.equals(sm.getString("neighbrhood"))) {
                nbr = value;
                hm.put("neighbrhood", nbr);
            } else if (value.equals("profile")) {
                userid_businesslist = Integer.parseInt(sm.getString("user_id"));
                hm.put("businessuserlist", userid_businesslist);
            } else if (value.equals("Businesses")) {

            } else {
                nbr = value;
                hm.put("neighbrhood", nbr);
            }
        } else {
            hm.put("userid", fromuserid);
            hm.put("businessuserlist", fromuserid);
        }
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));

        titleTv = findViewById(R.id.title);
        searchImageView = findViewById(R.id.search_imageview);
        addImageView = findViewById(R.id.add_imageview);
        back_btn = findViewById(R.id.back_btn);
        business_category = findViewById(R.id.business_category);
        cancel_iv = findViewById(R.id.cancel_iv);
        rv_business = findViewById(R.id.rv_business);
        search_rl = findViewById(R.id.search_rl);
        search_et = findViewById(R.id.search_et);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);

        // Set up swipe refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                businessListApi(hm); // Refresh data
            }
        });

        categoryApi();
        businessListApi(hm);

        rv_business.setLayoutManager(new LinearLayoutManager(this));
        searchImageView.setVisibility(View.VISIBLE);
        addImageView.setVisibility(View.VISIBLE);
        titleTv.setText("Businesses");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_rl.setVisibility(View.VISIBLE);
                search_et.requestFocus();
            }
        });

        EditText finalSearch_et = search_et;
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                finalSearch_et.setFocusable(true);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_rl.setVisibility(View.GONE);
                search_et.setText("");
                search_et.setHint("Search...");
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
            }
        });

        business_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory();
            }

            private void selectCategory() {
                RecyclerView rv;
                ImageView cancel;
                mail_dialog = new Dialog(BusinessActivity.this);
                mail_dialog.setContentView(R.layout.open_category_list);
                rv = mail_dialog.findViewById(R.id.rv_category);
                cancel = mail_dialog.findViewById(R.id.cross_imageview);
                TextView tv = mail_dialog.findViewById(R.id.tv_itm);
                tv.setText("Business categories");

                rv.setLayoutManager(new LinearLayoutManager(BusinessActivity.this));
                BusinessCategoryAdapter emailListAdapter = new BusinessCategoryAdapter(BusinessActivity.this, categorylist);
                rv.setAdapter(emailListAdapter);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mail_dialog.cancel();
                    }
                });
                mail_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(BusinessActivity.this, android.R.color.transparent)));
                mail_dialog.setCancelable(true);
                mail_dialog.show();
            }
        });

        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVerifiedUser) {
                    Intent createBusinessIntent = new Intent(BusinessActivity.this, CreateBusinessPageActivity.class);
                    createBusinessIntent.putExtra("business_page_type", "new");
                    startActivity(createBusinessIntent);
                } else {
                    GlobalMethods.getInstance(BusinessActivity.this).globalDialog(BusinessActivity.this, getString(R.string.unverified_msg));
                }
            }
        });
    }


    private void businessListApi(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<BusinessListPojo> call = service.businesslist("businesslist", hm);
        call.enqueue(new Callback<BusinessListPojo>() {
            @Override
            public void onResponse(Call<BusinessListPojo> call, Response<BusinessListPojo> response) {
                // Hide refresh indicator when response is received
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                // Safety check - ensure response body is not null
                if (response.body() == null) {
                    Toast.makeText(BusinessActivity.this, "No data received", Toast.LENGTH_SHORT).show();
                    return;
                }

                String status = response.body().getStatus();
                if (response.body().getVerfied_msg().equals("User Verification is not completed!")) {
                    isVerifiedUser = false;
                }

                if (status.equals("failed")) {
//                    Toast.makeText(BusinessActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                } else {
                    listdata = response.body().getListdata();
                    sm.setString("pincode", response.body().getPin());
                    sm.setString("addlinetwo", response.body().getAddlinetwo());
                    sm.setString("state", response.body().getState_name());
                    sm.setString("city", response.body().getCity_name());

                    // Update adapter with new data
                    if (businessAdapter == null) {
                        businessAdapter = new BusinessAdapter(listdata, value, BusinessActivity.this, BusinessActivity.this, BusinessActivity.this);
                        rv_business.setAdapter(businessAdapter);
                    } else {
                        businessAdapter.updateData(listdata);
                        businessAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<BusinessListPojo> call, Throwable t) {
                // Hide refresh indicator on failure
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                Toast.makeText(BusinessActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.radio_all_day_open) {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(int userId) {
        if (isVerifiedUser) {
            Intent i = new Intent(BusinessActivity.this, MyProfileOtherUser.class);
            i.putExtra("user_id", userId);
            startActivity(i);
        } else {
            GlobalMethods.getInstance(BusinessActivity.this).globalDialog(BusinessActivity.this, getString(R.string.unverified_msg));
        }
    }

    @Override
    public void onClickRating(String businessId) {
        if (isVerifiedUser) {
            starDialog(businessId);
        } else {
            GlobalMethods.getInstance(BusinessActivity.this).globalDialog(BusinessActivity.this, getString(R.string.unverified_msg));
        }
    }

    private void rateNow(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Log.d("businessuserrate", hm.toString());
        Call<RateNowPojo> call = service.businessRating("businessuserrate", hm);
        call.enqueue(new Callback<RateNowPojo>() {
            @Override
            public void onResponse(Call<RateNowPojo> call, Response<RateNowPojo> response) {
                Toast.makeText(BusinessActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BusinessActivity.this, BusinessActivity.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<RateNowPojo> call, Throwable t) {
                Toast.makeText(BusinessActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }

    @Override
    public void onClickWriteReview(int pos) {
        Intent detailScreen = new Intent(this, BusinessDetailActivity.class);
        detailScreen.putExtra("name", businessList.get(pos).getBusinessname());
        detailScreen.putExtra("category", businessList.get(pos).getCat());
        detailScreen.putExtra("desc", businessList.get(pos).getDescription());
        detailScreen.putExtra("tagline", businessList.get(pos).getTagline());
        detailScreen.putExtra("type", "write_a_review");
        startActivity(detailScreen);
        finish();
    }

    private void starDialog(String businessId) {
        RecyclerView rv;
        TextView confirm, cancel;
        rating_dialog = new Dialog(this);
        rating_dialog.setContentView(R.layout.open_star_rating_dialog);
        ratingBar = rating_dialog.findViewById(R.id.ratingBar);
        confirm = rating_dialog.findViewById(R.id.confirm_textview);
        cancel = rating_dialog.findViewById(R.id.cancel_textview);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("business_id", Integer.parseInt(businessId));
                hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                hm.put("rateno", ratingBar.getRating());
                rateNow(hm);
                rating_dialog.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating_dialog.cancel();
            }
        });
        rating_dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
        rating_dialog.setCancelable(false);
        rating_dialog.show();
    }

    @Override
    public void onclickReview(int pos) {

    }

    @Override
    public void onClickDetail(int pos, int id) {
        if (isVerifiedUser) {
            Intent detailScreen = new Intent(this, BusinessDetailActivity.class);
            detailScreen.putExtra("id", id);
            detailScreen.putExtra("type", "detail");
            startActivity(detailScreen);
        } else {
            GlobalMethods.getInstance(BusinessActivity.this).globalDialog(BusinessActivity.this, getString(R.string.unverified_msg));
        }
    }

    @Override
    public void onThreeDotClick(List<Listdatum> mList, int pos) {
        if (isVerifiedUser) {
            BottomSheetBusinessFragment bottomSheetBusiness = new BottomSheetBusinessFragment(BusinessActivity.this,
                    mList.get(pos).getImage().get(0).getImg(),
                    mList.get(pos).getFastatus(),
                    mList.get(pos).getName(),
                    mList.get(pos).getId(),
                    mList.get(pos).getTagline(),
                    mList.get(pos).getUsername(),
                    mList.get(pos).getUserpic(),
                    mList.get(pos).getUserid(),
                    mList.get(pos).getCategory(),
                    mList.get(pos).getNeighborhood(),
                    "business");
            bottomSheetBusiness.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
        } else {
            GlobalMethods.getInstance(BusinessActivity.this).globalDialog(BusinessActivity.this, getString(R.string.unverified_msg));
        }
    }

    @Override
    public void onClick(String name, String id) {
        business_category.setText(name);
        filter(name);
        mail_dialog.cancel();
    }

    public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setLayout(200, 150);
        dialog.setContentView(view);
        dialog.show();
    }

    private void starDialog() {
        RecyclerView rv;
        TextView confirm, cancel;
        rating_dialog = new Dialog(this);
        rating_dialog.setContentView(R.layout.open_star_rating_dialog);
        confirm = rating_dialog.findViewById(R.id.confirm_textview);
        cancel = rating_dialog.findViewById(R.id.cancel_textview);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating_dialog.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating_dialog.cancel();
            }
        });
        rating_dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
        rating_dialog.setCancelable(false);
        rating_dialog.show();
    }

    public void globalDialog(String str) {
        TextView msg, ok;
        ImageView cancel, iv_uploaded_image;
        CardView card;
        dialog = new Dialog(BusinessActivity.this);
        dialog.setContentView(R.layout.global_dialog);
        ok = dialog.findViewById(R.id.ok_textview);
        msg = dialog.findViewById(R.id.msg);
        msg.setText(str);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(BusinessActivity.this, android.R.color.transparent)));
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void businessSuccess(String msg) {
        globalDialog(msg);
    }

    private void filter(String text) {
        ArrayList<Listdatum> filteredlist = new ArrayList<Listdatum>();
        for (Listdatum item : listdata) {
            if ((item.getCategory().toLowerCase().contains(text.toLowerCase()) ||
                    item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase()))) {
                filteredlist.add(item);
            }
        }

        if (filteredlist.isEmpty()) {
//            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            businessAdapter.filterList(filteredlist);
        }
    }

    private void categoryApi() {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<StatePojo> call = service.businessCategoryList("businesstype");
        call.enqueue(new Callback<StatePojo>() {
            @Override
            public void onResponse(Call<StatePojo> call, Response<StatePojo> response) {
                categorylist = response.body().getNbdata();
            }

            @Override
            public void onFailure(Call<StatePojo> call, Throwable t) {
                Toast.makeText(BusinessActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }

    @Override
    public void onListItemClicked(String itemName) {
        businessListApi(hm);
    }

    @Override
    public void onImageClick(List<ImagePojo> list, int pos) {
        imageDialog(list, pos);
    }


    private void imageDialog(List<ImagePojo> img, int pos) {
        RecyclerView rv;
        ImageView cancel;
        CardView cv_dialog_exit;
        Dialog image_dialog;

        image_dialog = new Dialog(BusinessActivity.this, R.style.FullscreenDialog);
        image_dialog.setContentView(R.layout.open_image_dialog);
        rv = image_dialog.findViewById(R.id.rv_image);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        cv_dialog_exit = image_dialog.findViewById(R.id.cv_dialog_exit);
        rv.setLayoutManager(new LinearLayoutManager(BusinessActivity.this, LinearLayoutManager.HORIZONTAL, false));
        MediaSliderAdapter mediaSliderAdapter = new MediaSliderAdapter(image_dialog, img, "detail", pos, BusinessActivity.this);
        rv.setAdapter(mediaSliderAdapter);
        rv.scrollToPosition(pos);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.cancel();
            }
        });

        cv_dialog_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.cancel();
            }
        });

        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(BusinessActivity.this, android.R.color.black)));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(image_dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        image_dialog.getWindow().setAttributes(layoutParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image_dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            image_dialog.getWindow().setStatusBarColor(ContextCompat.getColor(BusinessActivity.this, android.R.color.black));
        }
        AppCompatActivity activity = (AppCompatActivity) BusinessActivity.this;
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(BusinessActivity.this, android.R.color.black)));
        }

        image_dialog.setCancelable(true);
        image_dialog.show();
    }

    @Override
    public void onVideoClick(int bid, List<ImagePojo> list, int pos) {
        if (isVerifiedUser) {
            Intent detailScreen = new Intent(BusinessActivity.this, BusinessDetailActivity.class);
            detailScreen.putExtra("id", bid);
            detailScreen.putExtra("type", "detail");
            startActivity(detailScreen);
        } else {
            GlobalMethods.getInstance(BusinessActivity.this).globalDialog(BusinessActivity.this, getString(R.string.unverified_msg));
        }
    }
}