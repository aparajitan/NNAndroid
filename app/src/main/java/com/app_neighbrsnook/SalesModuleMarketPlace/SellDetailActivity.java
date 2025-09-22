package com.app_neighbrsnook.SalesModuleMarketPlace;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.MarketPlaceDetailImgAdapter;
import com.app_neighbrsnook.adapter.MediaSliderAdapter;
import com.app_neighbrsnook.adapter.LatestAdapter;
import com.app_neighbrsnook.adapter.ViewAdapter;
import com.app_neighbrsnook.adapter.WallChildAdapter;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.apiService.UrlClass;
import com.app_neighbrsnook.fragment.ImageSellerFragment;
import com.app_neighbrsnook.intreface.ImageCallBackSellerDetail;
import com.app_neighbrsnook.intreface.TodayCallBack;
import com.app_neighbrsnook.marketPlace.ChatListMarketPlaceActivity;
import com.app_neighbrsnook.marketPlace.MarketPlace;
import com.app_neighbrsnook.marketPlace.MarketPlaceChatActivity;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.GroupJoinPojo;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.app_neighbrsnook.pojo.marketPlacePojo.DeleteMarketPlaceProduct;
import com.app_neighbrsnook.pojo.marketPlacePojo.DetailsPojoChild;
import com.app_neighbrsnook.pojo.marketPlacePojo.DetailsPojoMarketPlaceParent;
import com.app_neighbrsnook.pojo.marketPlacePojo.MarketPlaceTodayListPojo;
import com.app_neighbrsnook.profile.MyProfileOtherUser;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellDetailActivity extends AppCompatActivity implements TodayCallBack, ImageCallBackSellerDetail,MarketPlaceDetailImgAdapter.ImgRequest, LatestAdapter.SellRequest,
        WallChildAdapter.ImageCallBack {
    TextView item_category,tvOwnerChat,quantityItme,saleType, item_name,titleTv,item_price,time,item_post_time,buy_textview, question_tv, created_date_tv, item_discription, brand_tv, nebgourhood_tv, item_posted_by;
    ImageView profile_imageview, back_btn, share_imaeview, save_iv,saveFill;
    RecyclerView similar_item_rv;
    int product_id;
    int stCreated;
    HashMap<String, Object> hm = new HashMap<>();
    RecyclerView.LayoutManager layoutManager;
    TodayCallBack tcb = this;
    LatestAdapter latestAdapter;
    Dialog  image_dialog;
    WallChildAdapter wallChildAdapter;
    CommonPojoSuccess wishlistResponse;
    ArrayList<ImagePojo> ar1 = new ArrayList<>();
    MarketPlaceDetailImgAdapter imageSlideAdapter;
    RecyclerView rvMarketPlaceDetails;
    ViewAdapter mViewPagerAdapter;
    ImageSellerFragment imageSellerFragment;
    String  stProductName, stMsgCount,sellerName,stCategory,stItemPrice,stQuantity,stUserPic,stSoldStatus,stType,stNeighbrhoodName,stAboutUs,stItemPostedBy,stCreateTime,stWishListStatus;
    ImageCallBackSellerDetail icb= this;
    int[] images = {};
    Uri uri = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
    String name,price;
    int touserid;
    boolean isSaved = true;
    Context context;
    Activity activity;
    List<MarketPlaceTodayListPojo> listDatapojo = new ArrayList();
    SharedPrefsManager sm;
    FrameLayout editDetial,soldFrm,deletePage,frmOwner,frm_request_pending,lnr_owner;
    ImageView tvSearchImageview,tvAddImageview;
    TextView tvQuantitygone,tvSimilarItem,tv_request_count,tvChatOwner,tvRsGone,tvcategroy,soldTv;
    WallChildAdapter.ImageCallBack imageCallBack=this;
    Boolean isUserVerified = true;
    LinearSnapHelper snapHelper = new LinearSnapHelper();
    String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_detail);
        context=activity=this;
        sm = new SharedPrefsManager(this);

        Intent intent = getIntent();
        product_id = intent.getIntExtra("id",0);
        Log.d("sdfsfdsda", String.valueOf(product_id));
        touserid = intent.getIntExtra("sender_id",0);

        Intent i = getIntent();
        tvcategroy = findViewById(R.id.tvcategroy);
        tv_request_count = findViewById(R.id.tv_request_count);
        tvChatOwner = findViewById(R.id.tvChatOwner);
        saveFill = findViewById(R.id.saveFill);
        editDetial = findViewById(R.id.editDetails);
        tvSearchImageview = findViewById(R.id.search_imageview);
        tvAddImageview = findViewById(R.id.add_imageview);
        deletePage = findViewById(R.id.deletePage);
        item_name = findViewById(R.id.item_name);
        item_price = findViewById(R.id.item_price);
        item_category = findViewById(R.id.item_category);
        profile_imageview = findViewById(R.id.profile_imageview);
        lnr_owner = findViewById(R.id.lnr_owner);
        frmOwner = findViewById(R.id.frmOwner);
        buy_textview = findViewById(R.id.buy_textview);
//        user_tv = findViewById(R.id.user_tv);
        soldTv = findViewById(R.id.soldTv);
        rvMarketPlaceDetails = findViewById(R.id.rvMarketPlaceDetails);
        tvOwnerChat = findViewById(R.id.tvOwnerChat);
        created_date_tv = findViewById(R.id.created_date_tv);
        item_discription = findViewById(R.id.item_desc);
        created_date_tv = findViewById(R.id.created_date_tv);
        share_imaeview = findViewById(R.id.share_imageview);
        brand_tv = findViewById(R.id.brand_tv);
        save_iv = findViewById(R.id.save_iv);
        quantityItme=findViewById(R.id.quantityItem);
        saleType=findViewById(R.id.saleType);
        item_post_time = findViewById(R.id.item_post_time);
        nebgourhood_tv = findViewById(R.id.nebgourhood_tv);
        similar_item_rv = findViewById(R.id.similar_item_rv);
        item_posted_by = findViewById(R.id.item_posted_by);
        tvQuantitygone = findViewById(R.id.idQuantity);
        tvSimilarItem = findViewById(R.id.tvSimilarItem);
        tvRsGone = findViewById(R.id.tvRsGone);
        soldFrm = findViewById(R.id.soldFrm);
        frm_request_pending = findViewById(R.id.frm_request_pending);
        item_price.setText("\u20B9"+i.getStringExtra("price"));
        item_name.setText(i.getStringExtra("distance"));
        item_name.setText(i.getStringExtra("price"));
        item_posted_by.setText("Sudhanshu kumar");
        nebgourhood_tv.setText("Noida sector 16");
        item_name.setText(i.getStringExtra("item_name"));
        name = i.getStringExtra("item_name");
        price = i.getStringExtra("price");
        question_tv = findViewById(R.id.question_tv);
        productDetail();
        brand_tv.setText(i.getStringExtra("brand"));
        titleTv = findViewById(R.id.title);
        titleTv.setText("Item details");
        back_btn = findViewById(R.id.back_btn);
        tvSearchImageview.setVisibility(View.GONE);
        tvAddImageview.setVisibility(View.GONE);
        String verifiedValue = PrefMananger.GetLoginData(this).getVerified();
        Log.d("LoginData", "Verified value from preferences: " + verifiedValue);

        if (verifiedValue != null && !verifiedValue.isEmpty()) {
            try {
                int verifiedInt = Integer.parseInt(verifiedValue);
                if (verifiedInt == 0) {
                    isUserVerified = false;
                } else {
                    isUserVerified = true;
                }
                Log.d("LoginData", "Parsed verified value: " + verifiedInt);
            } catch (NumberFormatException e) {
                Log.e("LoginData", "Error parsing verified value: " + verifiedValue, e);
                isUserVerified = false; // Default value in case of error
            }
        } else {
            Log.w("LoginData", "Verified value is null or empty. Setting default to false.");
            isUserVerified = false;
        }
        lnr_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isUserVerified) {
                    Intent i = new Intent(SellDetailActivity.this, MyProfileOtherUser.class);
                    i.putExtra("user_id", stCreated);
                    startActivity(i);
                } else {
                    GlobalMethods.getInstance(SellDetailActivity.this).globalDialog(SellDetailActivity.this, getString(R.string.unverified_msg));
                }

            }
        });

        frmOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserVerified) {
                    Intent detailScreen = new Intent(SellDetailActivity.this, ChatListMarketPlaceActivity.class);
                    detailScreen.putExtra("id", product_id);
                    detailScreen.putExtra("type", "detail");
                    startActivity(detailScreen);
                } else {
                    GlobalMethods.getInstance(SellDetailActivity.this).globalDialog(SellDetailActivity.this, getString(R.string.unverified_msg));
                }
            }
        });
        frm_request_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserVerified) {
                    Intent detailScreen = new Intent(SellDetailActivity.this, MarketPlaceChatActivity.class);
                    detailScreen.putExtra("id", product_id);
                    detailScreen.putExtra("sender_id", stCreated);
                    detailScreen.putExtra("pic", stUserPic);
                    detailScreen.putExtra("userName", sellerName);
                    detailScreen.putExtra("type", "detail");
                    startActivity(detailScreen);
                } else {
                    GlobalMethods.getInstance(SellDetailActivity.this).globalDialog(SellDetailActivity.this, getString(R.string.unverified_msg));
 }
            }
        });
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvMarketPlaceDetails.setLayoutManager(layoutManager);
        editDetial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserVerified) {
                    Intent detailScreen = new Intent(SellDetailActivity.this, EditProduct.class);
                    detailScreen.putExtra("id", product_id);
                    detailScreen.putExtra("type", "detail");
                    startActivity(detailScreen);
                } else {
                    GlobalMethods.getInstance(SellDetailActivity.this).globalDialog(SellDetailActivity.this, getString(R.string.unverified_msg));
                }
            }
        });

        deletePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(SellDetailActivity.this);
                dialog.setContentView(R.layout.delete_event_layout);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SellDetailActivity.this, android.R.color.transparent)));
                dialog.getWindow().setAttributes(lp);
                dialog.show();
                ImageView img = dialog.findViewById(R.id.iv9);
               TextView productDlt = dialog.findViewById(R.id.productDlt);
                TextView tv_yes = dialog.findViewById(R.id.tv_yes_id);
                TextView tv_no = dialog.findViewById(R.id.tv_no);
                productDlt.setText("Product");
                tv_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // HashMap<String, Object> hm = new HashMap<>();

                        deleteItem();
                        dialog.dismiss();


                    }
                });
                img.setOnClickListener(new View.OnClickListener() {/**/
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        share_imaeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
                    String shareBody = String.format(
                        "Checkout this item on Neighbrsnook marketplace,\n\n" +
                                "Item Name: %s\n" +
                                "Item Price: ₹%s\n" +
                                "%s\n\n" +
                                "https://neighbrsnook.com/open-app\n\n" +
                                "https://neighbrsnook.com/",
                        stProductName, stItemPrice, stNeighbrhoodName
                );

                    downloadAndShareImage(imageUrl, shareBody);
                } else {
                    GlobalMethods.getInstance(SellDetailActivity.this).globalDialog(SellDetailActivity.this, getString(R.string.unverified_msg));
                }
            }
        });

        save_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //if(isSaved)

                if (isUserVerified) {
                    if (stSoldStatus.equals("1")){
                        Log.d("sdsfsdddddeee",stSoldStatus);

                        addWishListApi();
                        FancyToast.makeText(getApplicationContext(), "Added to wishlist", Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        save_iv.setImageResource(R.drawable.save_black);
                        save_iv.setColorFilter(Color.argb(255, 255, 255, 255));
                        isSaved = false;
                    } else if (stSoldStatus.equals("0") ||stSoldStatus.equals("2")){
                        FancyToast.makeText(getApplicationContext(), "This product not active", Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        Log.d("sdsfsdddddeee",stSoldStatus);
                    }
                } else {
                    GlobalMethods.getInstance(SellDetailActivity.this).globalDialog(SellDetailActivity.this, getString(R.string.unverified_msg));
 }

            }
        });

        saveFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isUserVerified) {
                    deleteWishlist();
                  /*  FancyToast.makeText(getApplicationContext(), "Added to wishlist", Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    save_iv.setImageResource(R.drawable.save_black);
                    save_iv.setColorFilter(Color.argb(255, 255, 255, 255));*/
                    saveFill.setVisibility(View.GONE);
                    save_iv.setVisibility(VISIBLE);
                } else {
                    GlobalMethods.getInstance(SellDetailActivity.this).globalDialog(SellDetailActivity.this, getString(R.string.unverified_msg));
 }

            }
        });
    }
    @Override
    public void onTodayItemClick(int str, String title) {
    }
    @Override
    public void onSellerItemClick(int str, String title) {

    }

    @Override
    public void onImageClick(int pos) {
        imageSellerFragment = new ImageSellerFragment();
        Bundle args = new Bundle();
        args.putInt("YourKey", pos);
        imageSellerFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, imageSellerFragment).addToBackStack("image_seller").commit();
        mViewPagerAdapter = new ViewAdapter(this, images, uri, icb);
    }
    private void productDetail() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("product_id", Integer.parseInt(String.valueOf((product_id))));
        hm.put("user_id", Integer.parseInt(sm.getString("user_id")));
        APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
        Call<DetailsPojoMarketPlaceParent> call = service.marketPlaceDetailsApi(product_id,(Integer.parseInt(PrefMananger.GetLoginData(this).getId())));
        call.enqueue(new Callback<DetailsPojoMarketPlaceParent>() {
            @Override
            public void onResponse(Call<DetailsPojoMarketPlaceParent> call, Response<DetailsPojoMarketPlaceParent> response) {
                DetailsPojoMarketPlaceParent rootObject= response.body();
                rootObject.getStatus().equals("200");


                   List<DetailsPojoChild> reviewListData = response.body().getProductdetail();
                   listDatapojo = response.body().getSimilarproducts();
                   //  listData=response.body().getProductdetail();

                String jsonString = "{\"chatmesg\":0}";

                   try {
                       @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);
                       similar_item_rv.setLayoutManager(linearLayoutManager);
                       similar_item_rv.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                       latestAdapter = new LatestAdapter(isUserVerified, listDatapojo, context, SellDetailActivity.this, SellDetailActivity.this);
                       similar_item_rv.setAdapter(latestAdapter);
                       JSONObject jsonObject = new JSONObject(jsonString);

                       Object value2 = jsonObject.get("chatmesg");
                       if (value2 instanceof String) {
                           System.out.println("key2 is a String");
                       } else if (value2 instanceof Integer) {
                           System.out.println("key2 is an Integer");
                       }
                       stProductName = reviewListData.get(0).getpTitle();
                       stItemPrice = reviewListData.get(0).getSalePrice();
                       stNeighbrhoodName = reviewListData.get(0).getNeighborhoodName();
                       stAboutUs = reviewListData.get(0).getpDescription();
                       stItemPostedBy = reviewListData.get(0).getSellerName();
                       stCreateTime = reviewListData.get(0).getCreatedTime();
                       stQuantity = String.valueOf(reviewListData.get(0).getpQuantity());
                       stType = reviewListData.get(0).getSaleType();
                       stMsgCount = (reviewListData.get(0).getReadCount().toString());
                       stUserPic=reviewListData.get(0).getUserpic();
                       stCreated = reviewListData.get(0).getCreatedBy();
                       sellerName=reviewListData.get(0).getSellerName();
                       stCategory=reviewListData.get(0).getCatName();
                       stSoldStatus= String.valueOf(reviewListData.get(0).getpStatus());
                       Log.d("sdsfsdeee",stSoldStatus);
                       tv_request_count.setText(stMsgCount);
                       item_name.setText(stProductName);
                       item_price.setText(stItemPrice);
                       nebgourhood_tv.setText(stNeighbrhoodName);
                       item_discription.setText(stAboutUs);
                       item_posted_by.setText(stItemPostedBy);
                       quantityItme.setText(stQuantity);
                       saleType.setText(stType);
                       tvcategroy.setText(stCategory);
                       item_post_time.setText(stCreateTime);
                       //int visibility = reviewListData.get(0).getpStatus().equals(2) ? View.VISIBLE : View.GONE;
                    //   soldFrm.setVisibility(visibility);
                    /*   if (reviewListData.get(0).getSalePrice().equals("0")){
                           item_price.setText("Free");
                           tvRsGone.setVisibility(View.GONE);
                       }*/

                       if (reviewListData.get(0).getpStatus().equals(2)) {
                           soldFrm.setVisibility(View.VISIBLE);
                           if (reviewListData.get(0).getSaleType().equals("Donate")) {
                               soldTv.setText("GIVEN");
                           } else {
                               soldTv.setText("SOLD");
                           }
                       } else if (reviewListData.get(0).getpStatus().equals(0)) {
                           soldFrm.setVisibility(View.GONE);
                       } else if (reviewListData.get(0).getpStatus().equals(1)) {
                           soldFrm.setVisibility(View.GONE);
                       }
                       if (reviewListData.get(0).getSaleType().equals("Donate")) {
                           item_price.setText("Free");
                           tvRsGone.setVisibility(View.GONE);
                       }
                       if (reviewListData.get(0).getCreatedBy().equals(Integer.parseInt(sm.getString("user_id")))) {
                           editDetial.setVisibility(VISIBLE);
                           deletePage.setVisibility(View.VISIBLE);
                      /*     saveFill.setVisibility(View.GONE);
                           save_iv.setVisibility(View.GONE);*/

                           frmOwner.setVisibility(View.VISIBLE);
                           frm_request_pending.setVisibility(View.GONE);
                       }
                       if (reviewListData.get(0).getReadCount().equals("0")) {
                           tv_request_count.setVisibility(View.GONE);
                       }else {
                           tv_request_count.setVisibility(VISIBLE);
                       }
                       if (response.body().getSimilarproducts().isEmpty()) {
                           tvSimilarItem.setVisibility(View.GONE);
                       } else {
                           tvSimilarItem.setVisibility(VISIBLE);}
                       if (reviewListData.get(0).getWishlistStatus().equals(1)) {
                           saveFill.setVisibility(View.VISIBLE);
                           save_iv.setVisibility(View.GONE);
                       } else if (reviewListData.get(0).getWishlistStatus().equals(0)) {
                           if (reviewListData.get(0).getCreatedBy().equals(Integer.parseInt(sm.getString("user_id")))) {
                               saveFill.setVisibility(View.GONE);
                               save_iv.setVisibility(View.GONE);
                           } else {
                               saveFill.setVisibility(View.GONE);
                               save_iv.setVisibility(View.VISIBLE);
                           }
                       }
                       imageUrl = reviewListData.get(0).getpImages().get(0).getImg();
                       List<ImagePojo> ar = reviewListData.get(0).getpImages();
                       ar1 = new ArrayList<>(ar);
                       wallChildAdapter = new WallChildAdapter(ar1, SellDetailActivity.this, imageCallBack);
                       rvMarketPlaceDetails.setAdapter(wallChildAdapter);
                       snapHelper.attachToRecyclerView(rvMarketPlaceDetails);
                       if (reviewListData.get(0).getUserpic().isEmpty()) {
                           profile_imageview.setImageResource(R.drawable.business_bg);
                       } else {
                           Picasso.get().load(reviewListData.get(0).getUserpic()).fit().fit().error(R.drawable.business_bg).placeholder(R.drawable.business_bg)
                                   .into(profile_imageview);
                       }


                   } catch (Exception e) {
                   }


                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<DetailsPojoMarketPlaceParent> call, Throwable t) {


                dialog.dismiss();
                Log.d("res", t.getMessage());
            }
        });
    }
    private void addWishListApi() {
//        dialog.show();
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if(isInternetConnection) {
            hm.put("user_id", Integer.parseInt(sm.getString("user_id")));
            hm.put("product_id", Integer.parseInt(String.valueOf((product_id))));
            Log.d("product_id", String.valueOf(product_id));
            Log.d("user_id", String.valueOf(product_id));
              APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
            Call<CommonPojoSuccess> call = service.addWishList(hm);
            call.enqueue(new Callback<CommonPojoSuccess>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<CommonPojoSuccess> call, Response<CommonPojoSuccess> response) {
                    wishlistResponse=response.body();
                   try {
                       if(response.body().getStatus().equals("success")) {
                           //    FancyToast.makeText(getApplicationContext(), "Your item has been saved.", Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                         //  startActivity(new Intent(SellDetailActivity.this,MarketPlace.class));

                       }
                       else if (wishlistResponse.getStatus().equals("error")){
                           FancyToast.makeText(getApplicationContext(), "Your item has been failed.", Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();


                       }
                   }catch (Exception e){

                   }

                    //dialog.dismiss();

                }

                @Override
                public void onFailure(Call<CommonPojoSuccess> call, Throwable t) {
                    //  dialog.dismiss();
                }
            });
        }else {
            GlobalMethods.getInstance(context).globalDialog(this, "     No internet connection."     );
        }

    }
    @Override
    public void onClickViewAllPopular(int pos, String title) {
    }
    @Override
    public void onClickViewAllTodayListing(int pos, String title) {
    }
    @Override
    public void onDetailPopular(int pos) {
    }
    @Override
    public void onDetailTodayListing(int pos) {
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Call your refresh function here
        productDetail();
    }
  /*  @Override
    public void onImageClick(List<ImagePojo> list, int pos) {
    }*/
    private void deleteItem() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        String url = "";//UrlClass.SELECT_NEIGHBRHOOD+"&lati="+latitude+"&longi="+longitude+"&areas="+address;
        Boolean isInternetConnection = GlobalMethods.checkConnection(context);
        if(isInternetConnection){
            HashMap<String, Object> hm =  new HashMap<>();
            // hm.put("p_id", Integer.parseInt(String.valueOf((id))));
            url = UrlClass.MARKET_PLACE_DLT+(product_id);
          /*  Log.d("sfsfs", String.valueOf(product_id));
            Log.d("sfsfs", PrefMananger.GetLoginData(context).getId());
*/

            ApiExecutor.getApiService().getDeleteProduct(url).enqueue(new Callback<DeleteMarketPlaceProduct>() {
                @Override
                public void onResponse(Call<DeleteMarketPlaceProduct> call, Response<DeleteMarketPlaceProduct> response) {

                    try {
                        dialog.dismiss();
                        if(response.body().getStatus().equals(200)) {
                            Toast.makeText(SellDetailActivity.this, "Product deleted successful" , Toast.LENGTH_SHORT).show(); Intent i = new Intent(SellDetailActivity.this, MarketPlace.class);
                            startActivity(i);
                        }
                        else if (wishlistResponse.getStatus().equals("error")){
                            FancyToast.makeText(getApplicationContext(), "Your item has been failed.", Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        }
                    }catch (Exception e){

                    }
                }

                @Override
                public void onFailure(Call<DeleteMarketPlaceProduct> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("res", t.getMessage());
                }
            });

        }else {

            GlobalMethods.getInstance(context).globalDialog(this, "     No internet connection."     );

        }

    }
    @Override
    public void onImageClick(List<ImagePojo> list, int pos) {
        mediaDialog(list,pos);
}
    private void mediaDialog(List<ImagePojo> img, int pos) {
        RecyclerView rv;
        ImageView cancel;
        image_dialog = new Dialog(this);
        image_dialog.setContentView(R.layout.open_image_dialog);
        rv = image_dialog.findViewById(R.id.rv_image);
        cancel = image_dialog.findViewById(R.id.cross_imageview);

        rv.setLayoutManager(new LinearLayoutManager(SellDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        MediaSliderAdapter mediaSliderAdapter = new MediaSliderAdapter(image_dialog, img, "detail", pos, SellDetailActivity.this);
        rv.setAdapter(mediaSliderAdapter);
        rv.scrollToPosition(pos);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.cancel();
            }
        });

        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SellDetailActivity.this, android.R.color.black)));
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
    public void onClickImg(List<ImagePojo> img) {

    }


    private void deleteWishlist() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        String url = "";
        Boolean isInternetConnection = GlobalMethods.checkConnection(context);
        if(isInternetConnection){
            url = UrlClass.MARKET_PLACE_DELETE_WISHLIST+(product_id);
            ApiExecutor.getApiService().getDeleteWishlist(url).enqueue(new Callback<GroupJoinPojo>() {
                @Override
                public void onResponse(Call<GroupJoinPojo> call, Response<GroupJoinPojo> response) {
                    try {
                        dialog.dismiss();
                        if(response.body().getStatus().equals("success")) {
                            Toast.makeText(SellDetailActivity.this, "Remove from wishlist" , Toast.LENGTH_SHORT).show();
                            //productDetail();
                          /*  Intent i = new Intent(SellDetailActivity.this, MarketPlace.class);
                           startActivity(i);*/
                        }
                        else if (wishlistResponse.getMessage().equals("Product not found in wishlist")){
                            FancyToast.makeText(getApplicationContext(), "Your item has been failed.", Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        }
                    }catch (Exception e){
                    }
                }

                @Override
                public void onFailure(Call<GroupJoinPojo> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("res", t.getMessage());
                }
            });
        }else {
            GlobalMethods.getInstance(context).globalDialog(this, "     No internet connection."     );
        }
    }
    private void downloadAndShareImage(String imageUrl, String shareBody) {
        if (imageUrl == null || imageUrl.isEmpty() || imageUrl.contains("video")) {
            shareText(shareBody);
            return;
        }

        new Thread(() -> {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "shared_image.jpg");
                FileOutputStream outputStream = new FileOutputStream(imageFile);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                shareImage(imageFile, shareBody);
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error downloading image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void shareImage(File imageFile, String shareBody) {
        try {
            Uri imageUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", imageFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            runOnUiThread(() -> startActivity(Intent.createChooser(shareIntent, "Share via")));
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(this, "Error sharing image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void shareText(String shareBody) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

            runOnUiThread(() -> startActivity(Intent.createChooser(shareIntent, "Share via")));
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(this, "Error sharing text: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}