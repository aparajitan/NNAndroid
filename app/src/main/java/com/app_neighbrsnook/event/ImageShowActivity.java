package com.app_neighbrsnook.event;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

//import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.ImageUploadAdapter1;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.profile.MyProfile;
import com.app_neighbrsnook.utils.PrefMananger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageShowActivity extends AppCompatActivity implements View.OnTouchListener {
    private int previousFingerPosition = 0;
    private int baseLayoutPosition = 0;
    private int defaultViewHeight;
    private boolean isClosing = false;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;
    ImageView iv_show_image;
    LinearLayout baseLayout;
    private View view;
    private MotionEvent event;
    String url = "";
    ViewPager viewPager;
    HashMap<String, Object> hashMap;
    ImageView img_profile_view;

    ImageView img_back;
    ArrayList<String> imageList = new ArrayList<>();
    Context context;
    Activity activity;
    ImageView img_delete_view;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=activity=this;
        setContentView(R.layout.activity_image_show);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        //  iv_back=findViewById(R.id.iv_back);
        //tv_tool_title=findViewById(R.id.tv_tool_title);
        viewPager = findViewById(R.id.viewPager);
        img_delete_view = findViewById(R.id.img_delete_view);
        img_profile_view=findViewById(R.id.img_profile_view);
        iv_show_image = findViewById(R.id.iv_show_image);
        baseLayout = findViewById(R.id.view);
        img_back=findViewById(R.id.img_back);
        //  iv_show_image.setOnTouchListener(new ImageMatrixTouchHandler(this));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        try {
            if (getIntent().hasExtra("ImageUpload")){
                viewPager.setAdapter(new ImagesAdapter());
                iv_show_image.setVisibility(View.VISIBLE);
                iv_show_image.setImageBitmap(ImageUploadAdapter1.imageList.get(getIntent().getIntExtra("ImageUpload",0)).bitmap);
                viewPager.setVisibility(View.GONE);
                //  Log.e("dasffad","Aaa gya "+ImageUploadAdapter.imageList.size()+"\n"+getIntent().getIntExtra("ImageUpload",0));
                //  viewPager.setCurrentItem(getIntent().getIntExtra("ImageUpload",0));
            }

            Log.d("ImageUrl1", url);
            //  Glide.with(this).load(getIntent().getStringExtra("image")).apply(myOptions).diskCacheStrategy(DiskCacheStrategy.NONE)
            //   .skipMemoryCache(true).into(iv_show_image).onLoadStarted(getDrawable(R.drawable.ic_place_holder));
            // Log.d("ImageUrl2", getIntent().getStringExtra("image"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        littleMoreSkip();
        img_delete_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hm = new HashMap<>();

                //  hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                hm.put("userid",Integer.parseInt(PrefMananger.GetLoginData(context).getId() +""));
                apiDeleteBusiness(hm);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View view, MotionEvent event) {
        this.view = view;
        this.event = event;
        // Get finger position on screen
        final int Y = (int) event.getRawY();
        // Switch on motion event type
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // save default base layout height
                defaultViewHeight = baseLayout.getHeight();
                // Init finger and view position
                previousFingerPosition = Y;
                baseLayoutPosition = (int) baseLayout.getY();
                break;
            case MotionEvent.ACTION_UP:
                // If user was doing a scroll up
                if (isScrollingUp) {
                    // Reset baselayout position
                    baseLayout.setY(0);
                    // We are not in scrolling up mode anymore
                    isScrollingUp = false;
                }
                // If user was doing a scroll down
                if (isScrollingDown) {
                    // Reset baselayout position
                    baseLayout.setY(0);
                    // Reset base layout size
                    baseLayout.getLayoutParams().height = defaultViewHeight;
                    baseLayout.requestLayout();
                    // We are not in scrolling down mode anymore
                    isScrollingDown = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isClosing) {
                    int currentYPosition = (int) baseLayout.getY();
                    // If we scroll up
                    if (previousFingerPosition > Y) {
                        // First time android rise an event for "up" move
                        if (!isScrollingUp) {
                            isScrollingUp = true;
                        }
                        // Has user scroll down before -> view is smaller than it's default size -> resize it instead of change it position
                        if (baseLayout.getHeight() < defaultViewHeight) {
                            baseLayout.getLayoutParams().height = baseLayout.getHeight() - (Y - previousFingerPosition);
                            baseLayout.requestLayout();
                        } else {
                            // Has user scroll enough to "auto close" popup ?
                            if ((baseLayoutPosition - currentYPosition) > defaultViewHeight / 4) {
                                closeUpAndDismissDialog(currentYPosition);
                                return true;
                            }

                            //
                        }
                        baseLayout.setY(baseLayout.getY() + (Y - previousFingerPosition));
                    }
                    // If we scroll down
                    else {
                        // First time android rise an event for "down" move
                        if (!isScrollingDown) {
                            isScrollingDown = true;
                        }
                        // Has user scroll enough to "auto close" popup ?
                        if (Math.abs(baseLayoutPosition - currentYPosition) > defaultViewHeight / 2) {
                            closeDownAndDismissDialog(currentYPosition);
                            return true;
                        }
                        // Change base layout size and position (must change position because view anchor is top left corner)
                        baseLayout.setY(baseLayout.getY() + (Y - previousFingerPosition));
                        baseLayout.getLayoutParams().height = baseLayout.getHeight() - (Y - previousFingerPosition);
                        baseLayout.requestLayout();
                    }
                    // Update position
                    previousFingerPosition = Y;
                }
                break;
        }
        return true;
    }
    public void closeUpAndDismissDialog(int currentPosition) {
        isClosing = true;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(baseLayout, "y", currentPosition, -baseLayout.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                finish();
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        positionAnimator.start();
    }
    public void closeDownAndDismissDialog(int currentPosition) {
        isClosing = true;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(baseLayout, "y", currentPosition, screenHeight + baseLayout.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        positionAnimator.start();
    }

    public class ImagesAdapter extends PagerAdapter {
        LayoutInflater layoutInflater;
        public ImagesAdapter(){
            // layoutInflater=LayoutInflater.from(ImageShowActivity.this);
            layoutInflater = (LayoutInflater) ImageShowActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            if (getIntent().hasExtra("ImageUpload")){
                //  imageView.setImageBitmap(ImageUploadAdapter.imageList.get(position).bitmap);t
                return ImageUploadAdapter1.imageList.size();
            }
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view== object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View itemView = layoutInflater.inflate(R.layout.inflate_images, container, false);
            ImageView imageView=itemView.findViewById(R.id.iv_show_image);
            try {
                imageView.setOnTouchListener(new ImageMatrixTouchHandler(ImageShowActivity.this));

                //tv_tool_title.setText(imagesPOJO.());
                Log.d("ImageUrl1",  imageList.get(position));
                if (getIntent().hasExtra("ImageUpload")){
                    imageView.setImageBitmap(ImageUploadAdapter1.imageList.get(position).bitmap);
                }
/*
                Glide.with(ImageShowActivity.this).load(imageList.get(position)).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true).into(imageView);

                  Glide.with(this).load(getIntent().getStringExtra("image")).apply(myOptions).diskCacheStrategy(DiskCacheStrategy.NONE)
                                */
                //   .skipMemoryCache(true).into(iv_show_image).onLoadStarted(getDrawable(R.drawable.ic_place_holder));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //baseLayout=itemView.findViewById(R.id.view);
            //  baseLayout.setOnTouchListener(ImageShowActivity.this);
            container.addView(itemView);
            return itemView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    private void littleMoreSkip() {
//        if (UtilityFunction.isNetworkConnected(context)) {
//            UtilityFunction.showLoading(context, "Please wait...");
        hashMap = new HashMap<>();
        // hashMap.put("userid", PrefMananger.GetLoginData(context).getId() + "");

        hashMap.put("userid", Integer.parseInt(PrefMananger.GetLoginData(context).getId() +""));
        //  hashMap.put("emerphoneno", tv_emergency.getText().toString());//var
        Log.e("fdsfgsd", hashMap.toString());
        userProfile(hashMap);

//        } else {
//            Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show();
//        }
    }

    private void userProfile(HashMap<String, Object> hm) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
//        AppCommon.getInstance(LittleMoreAboutYouActivity.this).setNonTouchableFlags(LittleMoreAboutYouActivity.this);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.profileapisec("userprofile" ,Integer.parseInt(PrefMananger.GetLoginData(context).getId()),hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement jsonElement=response.body();
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                dialog.dismiss();

                try {

                    //profile data show all data show

                    // img_profile.setImageURI(bitmap);
                }catch (Exception e){

                }
                Picasso.get().load(jsonObject.get("userpic").getAsString()).into(img_profile_view);
                if (jsonObject.get("userpic").getAsString().isEmpty()) {

                    img_profile_view.setImageResource(R.drawable.marketplace_white_background);
                } else{
                    Picasso.get().load(jsonObject.get("userpic").getAsString()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.marketplace_white_background)
                            .into(img_profile_view);
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

//                AppCommon.getInstance(PlaceOrderActivity.this).clearNonTouchableFlags(PlaceOrderActivity.this);
                Toast.makeText(ImageShowActivity.this, "Data found", Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }
    private void apiDeleteBusiness(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.deleteProfileImage("deleteuserprofilephoto", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Intent i = new Intent(ImageShowActivity.this, MyProfile.class);
                startActivity(i);
                // finish();
                finishAffinity();
                Toast.makeText(ImageShowActivity.this, "Image deleted Successfully" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(ImageShowActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }


}