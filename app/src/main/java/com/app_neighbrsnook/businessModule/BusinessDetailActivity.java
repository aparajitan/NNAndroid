package com.app_neighbrsnook.businessModule;

import static android.Manifest.permission.CALL_PHONE;
import static android.view.View.VISIBLE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.adapter.DocumentPagerAdapter;
import com.app_neighbrsnook.adapter.ImageSliderDialogAdapter;
import com.app_neighbrsnook.adapter.MediaSliderAdapter;
import com.app_neighbrsnook.adapter.WallChildAdapter;
import com.app_neighbrsnook.profile.MyProfileOtherUser;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.gson.JsonElement;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.ReviewBottomSheetFragment;
import com.app_neighbrsnook.adapter.DocViewAdapter;
import com.app_neighbrsnook.adapter.DocumentUploadAdapter1;
import com.app_neighbrsnook.adapter.ImageSlideAdapter;
import com.app_neighbrsnook.adapter.ReviewAdapter;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.BusinessDetailPojo;
import com.app_neighbrsnook.pojo.DocPojo;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.pojo.RateNowPojo;
import com.app_neighbrsnook.pojo.ReviewPojo;
import com.app_neighbrsnook.pojo.Reviewdatum;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessDetailActivity extends AppCompatActivity implements DocumentUploadAdapter1.DocRequest, ImageSlideAdapter.ImgRequest, WallChildAdapter.ImageCallBack {
    String businessName, category, desc, tagline, type, doc_type, business_address, open_time, close_time, week_off, web, phone, telephone, add1, add2, pin, city, state, country, rating, review, neighbrhood, userpic, email, image, countryid, cityid, stateid, neighbrhood1;
    TextView businessTitle, email_tv, business_name, business_tagline, business_desc, doc_type_tv, business_category, rate_now_textview, user_tv,
            doc_count_tv, owner_name_tv, write_review_textview, read_review_textview, business_review, view_doc_tv, address_tv, open_close_time_tv, weekly_off_tv, web_tv, phone_tv;
    RecyclerView for_you_rv, rv_doc, review_rv;
    LinearLayoutManager layoutManager, layoutManager1;
    List<JsonElement> imageList = new ArrayList<>();
    RelativeLayout mobile_relativelayout, web_relativelayout, review_rl, rating_rl;
    LinearLayout detail_ll, reviews_ll, display_review_ll;
    EditText write_review_et;
    DocumentUploadAdapter1 imageUploadAdapter;
    ImageSlideAdapter imageSlideAdapter;
    ImageView back_btn, send_imageview, searchImageView, addImageView, owner_imageview;
    Dialog rating_dialog, image_dialog;
    PDFView pdfView, pdfView1;
    SharedPrefsManager sm;
    int id;
    List<ImagePojo> ar = new ArrayList<>();
    Dialog phone_dialog;
    WallChildAdapter.ImageCallBack imageCallBack = this;
    String catid;
    String ratingStatus;
    ReviewAdapter reviewAdapter;
    RatingBar ratingBar, display_rating;
    TextView totalRatingOfBusiness, review_quantity_tv;
    HashMap<String, Object> hm = new HashMap<>();
    HashMap<String, Object> reviewList = new HashMap<>();
    HashMap<String, Object> skip = new HashMap<>();
    String pdfurl;
    String business_id, userid, rateno;
    ArrayList<ImagePojo> ar1 = new ArrayList<>();
    ArrayList<DocPojo> docList = new ArrayList<>();
    List<ImagePojo> arDoc = new ArrayList<>();
    RelativeLayout profile_rl;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail);
        Intent intent = getIntent();
        sm = new SharedPrefsManager(this);
        id = intent.getIntExtra("id", 0);
        type = intent.getStringExtra("type");
        item();
        reviews_ll.setVisibility(View.GONE);
        // businessTitle.setText("Business Detail");


        searchImageView.setVisibility(View.GONE);
        addImageView.setVisibility(View.GONE);
        businessDetailApi(id, "");
        searchImageView.setImageResource(R.drawable.ic_trash_icon);
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete();

            }
        });

        addImageView.setPadding(5, 5, 5, 5);
        addImageView.setImageResource(R.drawable.edit_pancil);
        addImageView.setBackgroundResource(R.drawable.circle_white_solid);
        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit
                Intent i = new Intent(BusinessDetailActivity.this, CreateBusinessPageActivity.class);
                Bundle extras = new Bundle();
                extras.putString("businessName", businessName);
                extras.putString("tagline", tagline);
                extras.putString("desc", desc);
                extras.putString("open_time", open_time);
                extras.putString("close_time", close_time);
                extras.putString("web", web);
                extras.putString("phone", phone);
                extras.putString("telephone", telephone);
                extras.putString("rating", rating);
                extras.putString("review", review);
                extras.putString("email", email);
                extras.putString("neighbrhood", neighbrhood);
                extras.putString("business_page_type", "edit");
                extras.putString("image", image);
                extras.putString("category", category);
                extras.putString("catid", catid);
                extras.putString("week_off", week_off);
//                extras.putString("document", document);
                extras.putString("add1", add1);
                extras.putString("add2", add2);
                extras.putString("country", country);
                extras.putString("city", city);
                extras.putString("state", state);
                extras.putString("pin", pin);
                extras.putString("cityid", cityid);
                extras.putString("stateid", stateid);
                extras.putString("countryid", countryid);
                extras.putString("business_id", business_id);
//                extras.putStringArrayList("myArrayList", ar1);
                i.putExtras(extras);
                startActivity(i);
            }
        });


        if (type.equals("write_a_review")) {
            detail_ll.setVisibility(View.GONE);
            write_review_et.requestFocus();
        }

        if (type.equals("review")) {
            detail_ll.setVisibility(View.GONE);
            reviewList = new HashMap<>();
            reviewList.put("business_id", id);
            reviewList.put("userid", Integer.parseInt(sm.getString("user_id")));
            reviewListApi(reviewList);

        }

        if (type.equals("detail")) {
            reviews_ll.setVisibility(View.GONE);

        }
        send_imageview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (CheckAllFields()) {

                    hm = new HashMap<>();
                    hm.put("business_id", Integer.parseInt(business_id));
                    hm.put("review", write_review_et.getText().toString());
                    hm.put("userid", Integer.parseInt(PrefMananger.GetLoginData(BusinessDetailActivity.this).getId()));
                    writeReviewApi(hm);
                }

            }
        });

        doc_count_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pdfDialog(arDoc);
            }
        });

        write_review_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                review_rl.setVisibility(VISIBLE);
                write_review_et.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(write_review_et, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        mobile_relativelayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                callDialog(phone);
            }
        });

        web_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(BusinessDetailActivity.this, WebActivity.class);
                webIntent.putExtra("web", web);
                startActivity(webIntent);
            }
        });

        profile_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BusinessDetailActivity.this, MyProfileOtherUser.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

        display_review_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (review.equals("0")) {

                } else {
                    sm.setInt("business_id", id);
                    ReviewBottomSheetFragment bottomSheetPoll = ReviewBottomSheetFragment.newInstance();
                    bottomSheetPoll.show(getSupportFragmentManager(), "Bottom Sheet Dialog Review");
                }

//                reviews_ll.setVisibility(VISIBLE);
//                reviewList = new HashMap<>();
//                reviewList.put("business_id", id);
//                reviewList.put("userid", Integer.parseInt(sm.getString("user_id")));
//                reviewListApi(reviewList);
            }
        });

        rate_now_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                starDialog();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                finish();
            }
        });

    }


    private void item() {
//        pdfView = findViewById(R.id.pdfView);
        doc_type_tv = findViewById(R.id.view_doc_tv);
        businessTitle = findViewById(R.id.title);
        searchImageView = findViewById(R.id.search_imageview);
        addImageView = findViewById(R.id.add_imageview);
        business_name = findViewById(R.id.business_name);
        business_tagline = findViewById(R.id.business_tagline);
        business_desc = findViewById(R.id.business_desc);
        business_category = findViewById(R.id.business_category);
        for_you_rv = findViewById(R.id.for_you_rv);
        write_review_et = findViewById(R.id.write_review_et);
        business_review = findViewById(R.id.business_review);
        back_btn = findViewById(R.id.back_btn);
        view_doc_tv = findViewById(R.id.view_doc_tv);
        doc_count_tv = findViewById(R.id.doc_count_tv);
        profile_rl = findViewById(R.id.profile_rl);
        totalRatingOfBusiness = findViewById(R.id.rating_tv);
        display_review_ll = findViewById(R.id.display_review_ll);
        review_quantity_tv = findViewById(R.id.review_quantity_tv);
        rate_now_textview = findViewById(R.id.rate_now_textview);
//        read_review_textview = findViewById(R.id.read_review_textview);
        write_review_textview = findViewById(R.id.write_review_textview);
        web_relativelayout = findViewById(R.id.web_relativelayout);
        review_rl = findViewById(R.id.review_rl);
        mobile_relativelayout = findViewById(R.id.mobile_relativelayout);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        for_you_rv.setLayoutManager(layoutManager);
        detail_ll = findViewById(R.id.detail_ll);
        reviews_ll = findViewById(R.id.reviews_ll);
        send_imageview = findViewById(R.id.send_imageview);
        address_tv = findViewById(R.id.address_tv);
        open_close_time_tv = findViewById(R.id.open_close_time_tv);
        weekly_off_tv = findViewById(R.id.weekly_off_tv);
        web_tv = findViewById(R.id.web_tv);
        email_tv = findViewById(R.id.email_tv);
        phone_tv = findViewById(R.id.phone_tv);
        user_tv = findViewById(R.id.user_tv);
        owner_name_tv = findViewById(R.id.owner_name_tv);
        owner_imageview = findViewById(R.id.owner_imageview);
        review_rv = findViewById(R.id.review_rv);
        layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        review_rv.setLayoutManager(layoutManager1);
    }


    @Override
    public void onDocClick(Uri pdf) {
//        documentDialog(pdf);
    }

    private void starDialog() {
        TextView confirm, cancel;
        rating_dialog = new Dialog(this);
        rating_dialog.setContentView(R.layout.open_star_rating_dialog);
        ratingBar = rating_dialog.findViewById(R.id.ratingBar);
        confirm = rating_dialog.findViewById(R.id.confirm_textview);
        cancel = rating_dialog.findViewById(R.id.cancel_textview);
//        float rat = ratingBar.getNumStars();
//        Log.d("rating", "" +rat);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hm = new HashMap<>();
                hm.put("business_id", Integer.parseInt(business_id));
//                hm.put("neighbrhood", neighbrhood);
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
    public void onClickImg(List<ImagePojo> img) {

        imageDialog(img);
    }

    private void pdfDialog(List<ImagePojo> pdfUrls) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.open_documents_dialog);

        CardView cancel = dialog.findViewById(R.id.cv_dialog_exit);
        ViewPager2 viewPager = dialog.findViewById(R.id.documentViewPager);
        viewPager.setOffscreenPageLimit(Math.min(3, pdfUrls.size()));

        DocumentPagerAdapter adapter = new DocumentPagerAdapter(this, pdfUrls);
        viewPager.setAdapter(adapter);

        cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);

        dialog.show();
    }

    private void imageDialog(List<ImagePojo> img) {
        RecyclerView rv;
        TextView confirm;
        ImageView cancel, iv_uploaded_image;
        CardView card;
        image_dialog = new Dialog(this);
        image_dialog.setContentView(R.layout.open_image_dialog);
        rv = image_dialog.findViewById(R.id.rv_image);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        card = image_dialog.findViewById(R.id.card);
        iv_uploaded_image = image_dialog.findViewById(R.id.iv_uploaded_image);

        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ImageSliderDialogAdapter imageUploadAdapter = new ImageSliderDialogAdapter(image_dialog, img, "detail", 1);
        rv.setAdapter(imageUploadAdapter);
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rv);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.cancel();
            }
        });
        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(BusinessDetailActivity.this, android.R.color.black)));

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(image_dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        image_dialog.getWindow().setAttributes(layoutParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image_dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            image_dialog.getWindow().setStatusBarColor(ContextCompat.getColor(BusinessDetailActivity.this, android.R.color.black));
        }

        AppCompatActivity activity = (AppCompatActivity) BusinessDetailActivity.this;
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(BusinessDetailActivity.this, android.R.color.black)));
        }

        image_dialog.setCancelable(true);
        image_dialog.show();
    }

    @Override
    public void onImageClick(List<ImagePojo> list, int pos) {
        mediaDialog(list, pos);
    }

    private class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        PDFView pdfView;

        RetrivePDFfromUrl(PDFView pdfView) {
            this.pdfView = pdfView;
        }

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
        }
    }

    private void apiDeleteBusiness(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.deleteBusiness("deletebusiness", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Intent i = new Intent(BusinessDetailActivity.this, BusinessActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(BusinessDetailActivity.this, "Business deleted Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(BusinessDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });

    }

    private void rateNow(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
//        Log.d("Response----" , hm.toString());
        Call<RateNowPojo> call = service.businessRating("businessuserrate", hm);
        call.enqueue(new Callback<RateNowPojo>() {
            @Override
            public void onResponse(Call<RateNowPojo> call, Response<RateNowPojo> response) {
                Toast.makeText(BusinessDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                businessDetailApi(id, "");
                rate_now_textview.setTextColor(getResources().getColor(R.color.them_color));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setTextViewDrawableColor(rate_now_textview, R.color.them_color);
                }
            }

            @Override
            public void onFailure(Call<RateNowPojo> call, Throwable t) {
                Toast.makeText(BusinessDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }

    private void businessDetailApi(int id, String neighbrhood) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("business_id", id);
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<BusinessDetailPojo> call = service.businessDetail("businessdetails", hm);
        call.enqueue(new Callback<BusinessDetailPojo>() {
            @Override
            public void onResponse(Call<BusinessDetailPojo> call, Response<BusinessDetailPojo> response) {
                BusinessDetailPojo rootObject = response.body();
                if (rootObject.getRatingStatus().equals("1")) {
                    rate_now_textview.setTextColor(getResources().getColor(R.color.them_color));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setTextViewDrawableColor(rate_now_textview, R.color.them_color);
                    }
                }

                user_id = Integer.parseInt(rootObject.getUserid());
                try {
                    ratingStatus = rootObject.getRatingStatus();
                    businessName = rootObject.getBusinessName();
                    if (rootObject.getTagline().equals("")) {
                        business_tagline.setVisibility(View.GONE);
                    } else {
                        tagline = rootObject.getTagline();
                    }

                    if (rootObject.getWeb().equals("")) {
                        web_relativelayout.setVisibility(View.GONE);
                    } else {
                        web_tv.setText(rootObject.getWeb());
                    }

                    if (rootObject.getMobile().equals("")) {
                        mobile_relativelayout.setVisibility(View.GONE);
                    } else {
                        phone_tv.setText(rootObject.getMobile());
                    }
                    desc = rootObject.getDescription();
                    open_time = rootObject.getFromtime();
                    close_time = rootObject.getTotime();
                    web = rootObject.getWeb();
                    phone = rootObject.getMobile();
                    telephone = rootObject.getTelephone();
                    rating = rootObject.getRating();
                    //totalRatingOfBusiness.setText(rating);
                    if (!rating.equals("0.0")) {
                        totalRatingOfBusiness.setText(rating);
                    } else {
                        totalRatingOfBusiness.setText("--");
                    }
                    review = rootObject.getReview();
                    business_id = rootObject.getId();
                    userid = rootObject.getUserid();
                    neighbrhood1 = rootObject.getNeighborhood();
                    email = rootObject.getEmail();
                    email_tv.setText(email);
                    week_off = rootObject.getWeeklyOff();
                    category = rootObject.getCategory();
                    add1 = rootObject.getAdd1();
                    add2 = rootObject.getAdd2();
                    city = rootObject.getCity();
                    pin = rootObject.getPincode();
                    catid = rootObject.getCatid();
                    countryid = rootObject.getCountryId();
                    cityid = rootObject.getCityId();
                    stateid = rootObject.getStateId();
                    try {
                        business_name.setText(businessName);
                        businessTitle.setText(businessName);
                        business_tagline.setText(tagline);
                        business_desc.setText(desc);
                        business_category.setText(category);
                        open_close_time_tv.setText(rootObject.getOpentime());
                        weekly_off_tv.setText("Closed on : " + week_off);
                        owner_name_tv.setText(rootObject.getUsername());
                        user_tv.setText(rootObject.getUsername());
                    } catch (Exception e) {
                    }
                    doc_type = rootObject.getDoctype();
                    business_address = rootObject.getBisaddress();
                    address_tv.setText(business_address);
                    doc_type_tv.setText(doc_type);
                    if (rootObject.getReview().equals("0")) {
                        review_quantity_tv.setText("No");
                    } else {
                        review_quantity_tv.setText(rootObject.getReview());
                    }
                    List<ImagePojo> ar = rootObject.getImage();
                    ar1 = new ArrayList<>(ar);
                    if ((rootObject.getImage() != null && rootObject.getImage().size() != 0)) {
                        WallChildAdapter childRecyclerViewAdapter = new WallChildAdapter(ar1, BusinessDetailActivity.this, imageCallBack);
                        for_you_rv.setAdapter(childRecyclerViewAdapter);
                    }

                    doc_count_tv.setText(rootObject.getDocument().size() + " Document View");
                    if (rootObject.getDocument().size() > 0) {
                        doc_count_tv.setVisibility(VISIBLE);
                    }
                    List<DocPojo> ar2 = rootObject.getDocument();
                    for (int i = 0; i <= ar2.size(); i++) {
                        ImagePojo imagePojo = new ImagePojo();
                        imagePojo.setImg(ar2.get(i).getDoc());
                        arDoc.add(imagePojo);
                    }

                } catch (Exception e) {

                }

                if (rootObject.getUserpic().isEmpty()) {
                    owner_imageview.setImageResource(R.drawable.profile);
                } else {
                    Picasso.get().load(rootObject.getUserpic()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile)
                            .into(owner_imageview);

                    Picasso.get().load(rootObject.getUserpic()).into(owner_imageview);
                }

                if (sm.getString("user_id").equals(rootObject.getUserid())) {
                    searchImageView.setVisibility(VISIBLE);
                    addImageView.setVisibility(VISIBLE);
                } else {
                    searchImageView.setVisibility(View.GONE);
                    addImageView.setVisibility(View.GONE);
                }

                reviewList = new HashMap<>();
                reviewList.put("business_id", Integer.parseInt(rootObject.getId()));
                reviewList.put("userid", Integer.parseInt(rootObject.getUserid()));
                reviewListApi(reviewList);
            }

            @Override
            public void onFailure(Call<BusinessDetailPojo> call, Throwable t) {
                Toast.makeText(BusinessDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void reviewListApi(HashMap<String, Object> reviewList) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<ReviewPojo> call = service.listReview("allreviewlist", reviewList);
        call.enqueue(new Callback<ReviewPojo>() {
            @Override
            public void onResponse(Call<ReviewPojo> call, Response<ReviewPojo> response) {
                List<Reviewdatum> reviewListData = response.body().getListdata();
//                Log.d("Response----" , reviewListData.toString());
                if (response.body().getMessage().equals("Data Not Found")) {
                    reviews_ll.setVisibility(View.GONE);
                } else {
                    ReviewAdapter reviewAdapter = new ReviewAdapter(reviewListData);
                    review_rv.setAdapter(reviewAdapter);
//                    reviews_ll.setVisibility(VISIBLE);
                }


            }

            @Override
            public void onFailure(Call<ReviewPojo> call, Throwable t) {
                Toast.makeText(BusinessDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }

    private void writeReviewApi(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<RateNowPojo> call = service.writeReview("businessreview", hm);
        call.enqueue(new Callback<RateNowPojo>() {
            @Override
            public void onResponse(Call<RateNowPojo> call, Response<RateNowPojo> response) {
                reviewListApi(reviewList);
                write_review_et.setText("");
                Toast.makeText(BusinessDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RateNowPojo> call, Throwable t) {
                Toast.makeText(BusinessDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }

    public void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete?");

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                hm.put("businessid", id);
                apiDeleteBusiness(hm);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView messageView = dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    private void callDialog(String s) {
        RecyclerView rv;
        TextView confirm, cancel, phone;
        phone_dialog = new Dialog(this);
        phone_dialog.setContentView(R.layout.cll_dialog);
        confirm = phone_dialog.findViewById(R.id.confirm_textview);
        cancel = phone_dialog.findViewById(R.id.cancel_textview);
        phone = phone_dialog.findViewById(R.id.phone);
        phone.setText(s);
        confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent sIntent = new Intent(Intent.ACTION_CALL);
                sIntent.setData(Uri.parse("tel:" + s));
                sIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(sIntent);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }
                phone_dialog.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_dialog.cancel();
            }
        });
        phone_dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
        phone_dialog.setCancelable(true);
        phone_dialog.show();


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(getColor(color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    private boolean CheckAllFields() {

        if (write_review_et.getText().toString().matches("")) {

            return GlobalMethods.setError(write_review_et, "Please enter review");
        }

        return true;
    }


    private void mediaDialog(List<ImagePojo> img, int pos) {
        RecyclerView rv;
        ImageView cancel;
        image_dialog = new Dialog(this);
        image_dialog.setContentView(R.layout.open_image_dialog);
        rv = image_dialog.findViewById(R.id.rv_image);
        cancel = image_dialog.findViewById(R.id.cross_imageview);

        rv.setLayoutManager(new LinearLayoutManager(BusinessDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        MediaSliderAdapter mediaSliderAdapter = new MediaSliderAdapter(image_dialog, img, "detail", pos, BusinessDetailActivity.this);
        rv.setAdapter(mediaSliderAdapter);
        rv.scrollToPosition(pos);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.cancel();
            }
        });

        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(BusinessDetailActivity.this, android.R.color.black)));
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent detailScreen = new Intent(this, MainActivity.class);
        startActivity(detailScreen);
    }

}


