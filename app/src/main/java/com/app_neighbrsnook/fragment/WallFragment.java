package com.app_neighbrsnook.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.ReportActivity;
import com.app_neighbrsnook.adapter.BusinessWallChildAdapter;
import com.app_neighbrsnook.adapter.MediaSliderAdapter;
import com.app_neighbrsnook.event.UserViewEvent;
import com.app_neighbrsnook.model.wall.PostEmojiListModel;
import com.app_neighbrsnook.model.wall.WelcomeEmojiListModel;
import com.app_neighbrsnook.pojo.RateNowPojo;
import com.app_neighbrsnook.pojo.ReviewPojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.app_neighbrsnook.profile.MyProfile;
import com.app_neighbrsnook.SalesModuleMarketPlace.SellerChatActivity;
import com.app_neighbrsnook.profile.ProfileUpdateDocumentUser;
import com.app_neighbrsnook.registration.SecondPageUserLocationRegisteration;
import com.app_neighbrsnook.utils.DeviceUtils;
import com.app_neighbrsnook.utils.EndlessRecyclerViewScrollListener;
import com.app_neighbrsnook.utils.VerifiedPopup;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.app_neighbrsnook.CommentActivity;
import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.EmojiAdapter;
import com.app_neighbrsnook.adapter.NotificationAdapter;
import com.app_neighbrsnook.adapter.ReactionAdapter;
import com.app_neighbrsnook.adapter.ViewPagerAdapter;
import com.app_neighbrsnook.adapter.WallAdapter;
import com.app_neighbrsnook.adapter.WallChildAdapter;
import com.app_neighbrsnook.businessModule.BusinessDetailActivity;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.group.GroupActivity;
import com.app_neighbrsnook.model.EmojiModel;
import com.app_neighbrsnook.model.Reaction;
import com.app_neighbrsnook.model.notification.NotificationModel;
import com.app_neighbrsnook.model.PollModel;
import com.app_neighbrsnook.model.PollPercentageModel;
import com.app_neighbrsnook.model.wall.Emojilistdatum;
import com.app_neighbrsnook.model.wall.Listdatum;
import com.app_neighbrsnook.model.wall.WallPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.EmojiPojo;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.pollModule.PollDetailActivity;
import com.app_neighbrsnook.post.CreatePostActivity;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WallFragment extends Fragment implements BusinessWallChildAdapter.ImageCallBack, WallChildAdapter.ImageCallBack, WallAdapter.WallRequest, EmojiAdapter.EmojiCallBack, ReactionAdapter.ReactionInterface {
    RecyclerView wall_rv;
    RecyclerView mRecyclerView;
    NotificationAdapter notificationAdapter;
    List<NotificationModel> list = new ArrayList<>();
    List<EmojiModel> emojiList = new ArrayList<>();
    TextView member_number_tv, title, title1;
    ImageView searchImageView, addImageView, img_back, wall_imageview, cross_imageview;
    MainActivity activity;
    Dialog mail_dialog;
    ViewPager mViewPager;
    Map<Integer, String> mapdata;
    ViewPagerAdapter mViewPagerAdapter;
    PostEmojiListModel postEmojiListModel;
    RelativeLayout image_relativelayout;
    WelcomeEmojiListModel welcomeEmojiListModel;
    WallChildAdapter.ImageCallBack imageCallBack = this;
    //    EmojiCallBack emojiCallBack = this;
    Dialog rating_dialog;
    RelativeLayout search_rl;
    EditText search_et;
    ImageView cancel_iv;
    List<PollPercentageModel> stringList = new ArrayList<>();
    List<PollModel> pollList = new ArrayList<>();
    SharedPrefsManager sm;
    WallAdapter adapter;
    WallPojo rootObject;
    private DAO myDao;
    EmojiAdapter.EmojiCallBack ecb = this;
    ReactionAdapter.ReactionInterface reactionInterface = this;
    Dialog image_dialog;
    LinearLayout root;
    boolean isUserVerified;
    LinearLayout announcement_ll;
    int postId;
    int position;
    List<Reaction> reactionList1 = new ArrayList<>();
    List<ImagePojo> postImages = new ArrayList();
    List<Emojilistdatum> emojilistdata = new ArrayList<>();
    List<Listdatum> listdata = new ArrayList<>();
    List<Listdatum> listdata1 = new ArrayList<>();
    List<Listdatum> searchList = new ArrayList<>();
    WallPostDetailFragment wallPostDetailFragment;
    TextView announcement_tv, post_msg_textview;
    String emoji;
    BusinessWallChildAdapter.ImageCallBack callBack = this;
    int count = 1;
    private int itemCount = 20;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    //        int page = 1;
    private NestedScrollView nestedSV;
    private EndlessRecyclerViewScrollListener scrollListener;
    ProgressDialog dialog;
    LinearLayoutManager linearLayoutManager;
    Boolean isScrolling = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    FrameLayout frmLogoWall;
    RatingBar ratingBar;
    int currentItems, totalItemCount, scrollOutItems;
    private OnFragmentInteractionListener mListener;
    String   stState,stCity,stPincode, dob,address, addressOne, addressTwo, genderProfile, whatDoYou,location_neighbrhood,stUploadDocument;
    HashMap<String, Object> hashMap;

    //    int[] images = {R.drawable.background_fifth, R.drawable.background_image, R.drawable.background_image_testing, R.drawable.background_photo_testing,
//            R.drawable.background_img_second, R.drawable.background_img_user};

    //no use images
    int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.background_photo_testing,
            R.drawable.background_img_second, R.drawable.background_img_user};
    Uri uri = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
    public static WallFragment wallFragment;

    public WallFragment(int id) {
        this.postId = id;
//        if(postId == 0)
//        {
//
//        }
//        else {
////            getPostion(postId);
//        }
    }

//    private void getPostion(int postId) {
//         position = listdata.indexOf(postId);
//    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wall, container, false);
        myDao = Database.createDBInstance(getActivity()).getDao();
        img_back = view.findViewById(R.id.back_btn);
        title = view.findViewById(R.id.title);
        sm = new SharedPrefsManager(getActivity());
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        member_number_tv = view.findViewById(R.id.member_number_tv);

        member_number_tv.setText("4 Members");
        img_back.setVisibility(View.GONE);
        frmLogoWall = view.findViewById(R.id.logo);
        nestedSV = view.findViewById(R.id.idNestedSV);
        title1 = view.findViewById(R.id.title1);
        search_rl = view.findViewById(R.id.search_rl);
        search_et = view.findViewById(R.id.search_et);
        cancel_iv = view.findViewById(R.id.cancel_iv);
        searchImageView = view.findViewById(R.id.search_imageview);
        addImageView = view.findViewById(R.id.add_imageview);
        searchImageView.setVisibility(View.VISIBLE);
        addImageView.setVisibility(View.VISIBLE);
        frmLogoWall.setVisibility(View.VISIBLE);
        //7NOVE24
        title.setVisibility(View.GONE);
        title1.setVisibility(View.VISIBLE);
        member_number_tv.setVisibility(View.GONE);//END
        root = view.findViewById(R.id.root);
        announcement_ll = view.findViewById(R.id.announcement_ll);
        announcement_tv = view.findViewById(R.id.announcement_tv);
        post_msg_textview = view.findViewById(R.id.post_msg_textview);

        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                count = 1; // Reset to first page
                listdata.clear(); // Clear existing data
                getWallApi(); // Refresh data
            }
        });
        getWallApi();
        profileGetData();
        callDeviceInfoApi();
        mRecyclerView = view.findViewById(R.id.wall_rv);
    /*    linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new WallAdapter(listdata, getActivity(), imageCallBack, ecb, WallFragment.this, reactionInterface, isUserVerified);
        mRecyclerView.setAdapter(adapter);*/

//no use but depen
        reactionList1.add(new Reaction("LIKE", "\uD83D\uDC4D"));
        reactionList1.add(new Reaction("LOVE", "\uD83D\uDC96"));
        reactionList1.add(new Reaction("SMILE", "\uD83D\uDE04"));
        reactionList1.add(new Reaction("WOW", "\uD83D\uDE2E"));
        reactionList1.add(new Reaction("SAD", "\uD83D\uDE14"));
        reactionList1.add(new Reaction("ANGRY", "\uD83D\uDE21"));
// Add Scroll Listener
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Show contact when scrolling stops
                    adapter.setShowContact(true);
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    // Hide contact while scrolling
                    adapter.setShowContact(false);
                }
         }
});
        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    count++;
                    Log.d("count----", String.valueOf(count));
                    getWallApi();

                }
            }
        });

        RelativeLayout rootView = view.findViewById(R.id.content);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
                    startActivity(new Intent(getActivity(), CreatePostActivity.class));
                } else {

                    //  String s = "Thank You! \n Your business page has been updated successfully. It will be published soon after a quick review.";
                    GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), getString(R.string.unverified_msg));

                    //userNotVerifiedMsg();
                }
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
//                filter(editable.toString());
                getWallSearchApi(editable.toString());

            }
        });

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_rl.setVisibility(View.VISIBLE);
                search_et.requestFocus();
            }
        });

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_rl.setVisibility(View.GONE);
                search_et.setText("");
                search_et.setHint("Search...");
                hideKeyboard();
            }
        });
        wall_imageview = view.findViewById(R.id.wall_imageview);
        mViewPager = view.findViewById(R.id.viewPagerMain);
        cross_imageview = view.findViewById(R.id.cross_imageview);
        image_relativelayout = view.findViewById(R.id.image_relativelayout);
        cross_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_relativelayout.setVisibility(View.GONE);
            }
        });

        return view;
    }


    private void userNotVerifiedMsg() {
        Toast.makeText(getActivity(), "Your document verification is panding.", Toast.LENGTH_SHORT).show();
    }

    private void hideKeyboard() {
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
    }

 /*   private void getWallApi() {
        dialog.show();
        Boolean isInternetConnection = GlobalMethods.checkConnection(getActivity());
        if (isInternetConnection) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("userid", Integer.parseInt(sm.getString("user_id")));
            hm.put("page", count);
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<WallPojo> call = service.wallListApi("homepage", hm);
            call.enqueue(new Callback<WallPojo>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {
                    rootObject = response.body();
                    assert rootObject != null;
                    if (rootObject.getVerfied_msg().equals("User Verification is completed!")) {
                        if (rootObject.getPopup_verified_status().equals("0")) {
                            VerifiedPopup.showSuccessDialog(getActivity(), rootObject.getVerfied_msg(), Integer.parseInt(sm.getString("user_id")));
                        }
                    }
                    sm.setString("neighbrhood", rootObject.getMy_neighborhood());
                    sm.setString("neighbrhood_name", rootObject.getMy_neighborhood());
                    sm.setString("neighbrhood", rootObject.getMy_neighborhood_id());
                    title.setText(rootObject.getMy_neighborhood());
                    title1.setText(rootObject.getMy_neighborhood());
                    try {
                        if (rootObject.getStatus().equals("success")) {
                            if ((rootObject.getVerfied_msg()).equals("User Verification is completed!")) {
                                isUserVerified = true;
                            } else {
                                isUserVerified = false;
                            }
                            linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            adapter = new WallAdapter(listdata, getActivity(), imageCallBack, callBack, ecb, WallFragment.this, reactionInterface, isUserVerified);
                            mRecyclerView.setAdapter(adapter);
                            if ((rootObject.getVerified_status()).equals("2")) {
                                rejectedDialog();
                            } else if (rootObject.getAwait_status().equals("1")) {
                                // Response handle
                                if (rootObject.getMissmatch_status().equals("name")) {
                                    wrongDocDialog(rootObject.getMissmatch_remarks(), "name");
                                } else if (rootObject.getMissmatch_status().equals("address")) {
                                    wrongDocDialog(rootObject.getMissmatch_remarks(), "address");
                                } else {
                                    wrongDocDialog(rootObject.getMissmatch_remarks(), "others");
                                }

                            }
                            sendDataToActivity(isUserVerified);
                            mListener.onFragmentInteraction(isUserVerified);
                            if (rootObject.getAnnouncement().size() > 0) {
                                announcement_ll.setVisibility(View.VISIBLE);
                                announcement_tv.setText(rootObject.getAnnouncement().get(0).getType());
                                post_msg_textview.setText(rootObject.getAnnouncement().get(0).getMsg());
                            }
                            member_number_tv.setText(rootObject.getMember_count() + " " + "Members");
                            listdata1.clear();
                            listdata1 = rootObject.getListdata();
                            listdata.addAll(listdata1);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "Server error: Please try again later.", Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();

                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<WallPojo> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Failed to connect to server. Please check your internet or try again.", Toast.LENGTH_LONG).show();

                }
            });
        } else {
            dialog.show();
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), "     No internet connection.");

        }


    }*/



    private void getWallApi() {
        // Only show progress dialog if not refreshing (swipe refresh has its own indicator)
        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            dialog.show();
        }

        Boolean isInternetConnection = GlobalMethods.checkConnection(getActivity());
        if (isInternetConnection) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("userid", Integer.parseInt(sm.getString("user_id")));
            hm.put("page", count);
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<WallPojo> call = service.wallListApi("homepage", hm);
            call.enqueue(new Callback<WallPojo>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {
                    // In onResponse and onFailure methods, add null checks:
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    rootObject = response.body();
                    assert rootObject != null;
                    if (rootObject.getVerfied_msg().equals("User Verification is completed!")) {
                        if (rootObject.getPopup_verified_status().equals("0")) {
                            VerifiedPopup.showSuccessDialog(getActivity(), rootObject.getVerfied_msg(), Integer.parseInt(sm.getString("user_id")));
                        }
                    }
                    sm.setString("neighbrhood", rootObject.getMy_neighborhood());
                    sm.setString("neighbrhood_name", rootObject.getMy_neighborhood());
                    sm.setString("neighbrhood", rootObject.getMy_neighborhood_id());
                    title.setText(rootObject.getMy_neighborhood());
                    title1.setText(rootObject.getMy_neighborhood());
                    try {
                        if (rootObject.getStatus().equals("success")) {
                            if ((rootObject.getVerfied_msg()).equals("User Verification is completed!")) {
                                isUserVerified = true;
                            } else {
                                isUserVerified = false;
                            }

                            // Only setup adapter if it's the first page (refresh)
                            if (count == 1) {
                                linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                                mRecyclerView.setLayoutManager(linearLayoutManager);
                                adapter = new WallAdapter(listdata, getActivity(), imageCallBack, callBack, ecb, WallFragment.this, reactionInterface, isUserVerified);
                                mRecyclerView.setAdapter(adapter);
                            }

                            if ((rootObject.getVerified_status()).equals("2")) {
                                rejectedDialog();
                            } else if (rootObject.getAwait_status().equals("1")) {
                                // Response handle
                                if (rootObject.getMissmatch_status().equals("name")) {
                                    wrongDocDialog(rootObject.getMissmatch_remarks(), "name");
                                } else if (rootObject.getMissmatch_status().equals("address")) {
                                    wrongDocDialog(rootObject.getMissmatch_remarks(), "address");
                                } else {
                                    wrongDocDialog(rootObject.getMissmatch_remarks(), "others");
                                }
                            }

                            sendDataToActivity(isUserVerified);
                            mListener.onFragmentInteraction(isUserVerified);

                            if (rootObject.getAnnouncement().size() > 0) {
                                announcement_ll.setVisibility(View.VISIBLE);
                                announcement_tv.setText(rootObject.getAnnouncement().get(0).getType());
                                post_msg_textview.setText(rootObject.getAnnouncement().get(0).getMsg());
                            }

                            member_number_tv.setText(rootObject.getMember_count() + " " + "Members");

                            // Clear data only on first page load (refresh)
                            if (count == 1) {
                                listdata.clear();
                            }

                            listdata1 = rootObject.getListdata();
                            listdata.addAll(listdata1);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "Server error: Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    }

                    // Dismiss progress dialog if shown
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<WallPojo> call, Throwable t) {
                    // In onResponse and onFailure methods, add null checks:
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    // Dismiss progress dialog if shown
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    Toast.makeText(getActivity(), "Failed to connect to server. Please check your internet or try again.", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // Hide refresh indicator if no internet
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), "     No internet connection.");
        }
    }

    private void getWelcomeEmojiList(HashMap<String, Object> hm, String str) {
        dialog.show();
        Boolean isInternetConnection = GlobalMethods.checkConnection(getActivity());
        if (isInternetConnection) {
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<WelcomeEmojiListModel> call = service.welEmojiListApi("userwelemojilist", hm);
            call.enqueue(new Callback<WelcomeEmojiListModel>() {
                @Override
                public void onResponse(Call<WelcomeEmojiListModel> call, Response<WelcomeEmojiListModel> response) {

                    welcomeEmojiListModel = response.body();

                    if (str.equals("Like")) {
                        Log.d("sdfsfs", str);
                        LikeBottomSheetAdapter likeBottomSheetAdapter = new LikeBottomSheetAdapter(welcomeEmojiListModel.getLikeDataModel());
                        likeBottomSheetAdapter.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                    } else {
                        BouquetBottomSheetAdapter bouquetBottomSheetAdapter = new BouquetBottomSheetAdapter(welcomeEmojiListModel.getBouquetDataModel());
                        bouquetBottomSheetAdapter.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<WelcomeEmojiListModel> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        } else {

            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), "     No internet connection.");
        }
    }

    public void wrongDocDialog(String msg, String type) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.wrong_doc_pop_up);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                ContextCompat.getColor(getActivity(), android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        dialog.setCancelable(false);
        TextView tv_no = dialog.findViewById(R.id.no_id);
        TextView tv_yes = dialog.findViewById(R.id.yes_id);
        TextView msgTv = dialog.findViewById(R.id.msg);
        TextView msg_title = dialog.findViewById(R.id.msg_title);
        ImageView img_cancel = dialog.findViewById(R.id.iv9);

        msg_title.setText("Mismatched Documents");
        msgTv.setText(msg);

        tv_no.setOnClickListener(view -> dialog.dismiss());

        tv_yes.setOnClickListener(view -> {
            Intent intent;
            switch (type) {
                case "name":
                    intent = new Intent(getActivity(), MyProfile.class);
                    intent.putExtra("source", "await");
                    break;
                case "address":
                    intent = new Intent(getActivity(), SecondPageUserLocationRegisteration.class);
                    intent.putExtra("addressone", addressOne);
                    intent.putExtra("locationNeighbrhood", location_neighbrhood);
                    intent.putExtra("stUploadDocument", stUploadDocument);
                    intent.putExtra("city", stCity);
                    intent.putExtra("state", stState);
                    intent.putExtra("pincode", stPincode);
                    intent.putExtra("source", "wall");
                    break;

                default:
                    intent = new Intent(getActivity(), ProfileUpdateDocumentUser.class);
                    intent.putExtra("dobb", dob);
                    intent.putExtra("gender", genderProfile);
                    intent.putExtra("stUploadDocument", stUploadDocument);
                    intent.putExtra("source", "wall");
                    break;
            }
            startActivity(intent);
            dialog.dismiss();
        });
    }



    private void callDeviceInfoApi() {
        String deviceId = DeviceUtils.getDeviceId(requireContext());
        //  String imei = DeviceUtils.getIMEI(requireContext());
        String modelName = DeviceUtils.getModelName();

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("user_id", Integer.parseInt(sm.getString("user_id")));
        hm.put("device_id", deviceId);
        hm.put("device_model", modelName);
        // hm.put("device_imei", imei);
        hm.put("device_platform", "Android");

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<ReviewPojo> call = service.deviceInfoApi("deviceinfo", hm);
        call.enqueue(new Callback<ReviewPojo>() {
            @Override
            public void onResponse(Call<ReviewPojo> call, Response<ReviewPojo> response) {

                Log.d("res", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ReviewPojo> call, Throwable t) {
                Toast.makeText(getActivity(), "Device Id Api Error", Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void rejectedDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.rejected_pop_up);
        dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        TextView tv_yes = dialog.findViewById(R.id.yes_id);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ProfileUpdateDocumentUser.class);
                startActivity(i);
                dialog.dismiss();
            }
        });
    }

    private void getWallSearchApi(String search) {
        Boolean isInternetConnection = GlobalMethods.checkConnection(getActivity());
        if (isInternetConnection) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("userid", Integer.parseInt(sm.getString("user_id")));
//            hm.put("page", count);
            hm.put("search", search);
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<WallPojo> call = service.wallListApi("homepage", hm);
            call.enqueue(new Callback<WallPojo>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {

                    rootObject = response.body();
                    searchList = rootObject.getListdata();
                    if (searchList.size() > 0) {
                        adapter.filterList(searchList);

                    } else {
                        Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
                        adapter.filterList(searchList);
                    }

                }

                @Override
                public void onFailure(Call<WallPojo> call, Throwable t) {

                }
            });
        } else {

            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), "     No internet connection.");

        }


    }

    @Override
    public void onEmojiClick(List<Emojilistdatum> pos) {
        EmojiBottomSheetFragment bottomSheetDialog = new EmojiBottomSheetFragment(pos);
        bottomSheetDialog.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    public void updateUi(String itemName) {
        getWallApi();
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onVideoClick(int bid, List<ImagePojo> list, int pos) {
        if (isUserVerified) {
            Intent detailScreen = new Intent(getActivity(), BusinessDetailActivity.class);
            detailScreen.putExtra("id", bid);
            detailScreen.putExtra("type", "detail");
            startActivity(detailScreen);
        } else {
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), getString(R.string.unverified_msg));
        }
    }
    //

   /* @Override
    public void onResume() {
        super.onResume();
        // Your refresh logic goes here
       // getWallApi();
    }*/
   @Override
   public void onResume() {
       super.onResume();
       getWallApi();
       /*boolean isVisible = sm.getBoolean("wrong_doc_visible", false);
       if (isVisible) {
           String type = sm.getString("wrong_doc_type");
           String msg = sm.getString("wrong_doc_msg");
           wrongDocDialog(msg, type);
       }*/
   }

    public interface BottomSheetListener {
        void onDataReceived(String data);
    }

    @Override
//    public void onThreeDotClick(int pos, String type, String subject, String title, String userPic, String msgSubject, int fabStatus, String bid) {
    public void onThreeDotClick(List<Listdatum> mList, int pos) {
        if (isUserVerified) {
            switch (mList.get(pos).getType()) {
                case "Post":
                    BottomSheetWallFragment bottomSheetWall = new BottomSheetWallFragment(getActivity(), mList, pos);
                    bottomSheetWall.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

                    break;

                case "Business":
                    BottomSheetBusinessFragment bottomSheetBusiness = new BottomSheetBusinessFragment(getActivity(),
                            mList.get(pos).getBusinessImage().get(0).getImg(),
                            mList.get(pos).getFavouritstatus(),
                            mList.get(pos).getBusinessName(),
                            mList.get(pos).getbId(),
                            mList.get(pos).getBusinessTagline(),
                            mList.get(pos).getUsername(),
                            mList.get(pos).getUserpic(),
                            mList.get(pos).getCreatedby(),
                            mList.get(pos).getCategory(),
                            mList.get(pos).getNeighborhood(),
                            "wall");
//                    BottomSheetFragment bottomSheetBusiness = new BottomSheetFragment(mList, pos, "wall", "");
                    bottomSheetBusiness.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                    break;

                case "Poll":
//                    BottomSheetPollFragment bottomSheetPoll = new BottomSheetPollFragment(mList, pos, "wall","");
                    BottomSheetPollFragment bottomSheetPoll = new BottomSheetPollFragment(getActivity(),
                            mList.get(pos).getFavouritstatus(),
                            mList.get(pos).getUsername(),
                            mList.get(pos).getNeighborhood(),
                            mList.get(pos).getPollQuestion(),
                            mList.get(pos).getPollid(),
                            mList.get(pos).getCreatedby(),
                            "wall");
                    bottomSheetPoll.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                    break;

                case "Event":
                    BottomSheetEventFragment bottomSheetEvent = new BottomSheetEventFragment(getActivity(), mList, pos);
                    bottomSheetEvent.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                    break;

                case "Group":
                    BottomSheetGroupFragment bottomSheetGroupFragment = new BottomSheetGroupFragment(getActivity(), mList, pos);
                    bottomSheetGroupFragment.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                    break;

                default:
                    // code block
                    break;

            }
//
        } else {
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), "You have limited access till verification is complete.");
        }


    }

    @Override
    public void onDetailPage(int position, int pos, String isVoted, String ispollrunning) {
        if (isUserVerified) {
            if (ispollrunning.equals("1")) {
                if (isVoted.equals("1")) {
                    Intent pollDetailIntent = new Intent(getActivity(), PollDetailActivity.class);
                    pollDetailIntent.putExtra("id", pos);
                    startActivity(pollDetailIntent);
                } else {
                    Intent pollDetailIntent = new Intent(getActivity(), PollDetailActivity.class);
                    pollDetailIntent.putExtra("id", pos);
//                    pollDetailIntent.putExtra("pollclosed", "pollclosed");
                    startActivity(pollDetailIntent);
                }

            } else if (ispollrunning.equals("2")) {
                if (isVoted.equals("1")) {
                    Intent pollDetailIntent = new Intent(getActivity(), PollDetailActivity.class);
                    pollDetailIntent.putExtra("id", pos);
                    startActivity(pollDetailIntent);
                } else if ((rootObject.getListdata().get(position).getUsername().equals(sm.getString("user_name")))) {
                    Intent pollDetailIntent = new Intent(getActivity(), PollDetailActivity.class);
                    pollDetailIntent.putExtra("id", pos);
                    startActivity(pollDetailIntent);
                } else {
                    GlobalMethods.getInstance(getActivity()).globalDialogCenter(getActivity(), "Polling is closed.");

//                    Toast.makeText(getActivity(), "Poll closed", Toast.LENGTH_SHORT).show();
                }
            } else if (ispollrunning.equals("0")) {
                Intent pollDetailIntent = new Intent(getActivity(), PollDetailActivity.class);
                pollDetailIntent.putExtra("id", pos);
                startActivity(pollDetailIntent);

//                GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(),"Voting opens when the poll starts " + rootObject.getListdata().get(position).getPollStartDate());

            } else {
                //
            }

        } else {
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), getString(R.string.unverified_msg));
        }
    }


    @Override
    public void onClick(int pos, String emojiName) {
        emoji = emojiName;

    }

    private void openReactionDialog(int pos) {
        ImageView cancel;
        mail_dialog = new Dialog(getActivity());
        mail_dialog.setContentView(R.layout.open_reaction_list);
        RecyclerView rv = mail_dialog.findViewById(R.id.rv_category);
        cancel = mail_dialog.findViewById(R.id.cross_imageview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(linearLayoutManager);
        ReactionAdapter businessCategoryAdapter = new ReactionAdapter(reactionList1, reactionInterface);
        rv.setAdapter(businessCategoryAdapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_dialog.cancel();
            }
        });
        mail_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.transparent)));
        mail_dialog.setCancelable(true);
        mail_dialog.show();
    }

    private void emojiApi(HashMap<String, Object> hm, int likePos) {

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<EmojiPojo> call = service.emojiApi("userpostlikes", hm);
        call.enqueue(new Callback<EmojiPojo>() {
            @Override
            public void onResponse(Call<EmojiPojo> call, Response<EmojiPojo> response) {

                assert response.body() != null;
                if (response.body().getStatus().equals("success")) {
                    String totalLike = String.valueOf(response.body().getTotal_like());
                    String likeStatus = String.valueOf(response.body().getLikestatus());
                    String emojiUnicode = String.valueOf(response.body().getEmojiunicode());

                    adapter.updateItemText(likePos, totalLike, likeStatus, emojiUnicode);
                }
            }

            @Override
            public void onFailure(Call<EmojiPojo> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });

    }

    @Override
    public void onBusinessDetail(int pos, int bid) {
        if (isUserVerified) {
            Intent detailScreen = new Intent(getActivity(), BusinessDetailActivity.class);
            detailScreen.putExtra("id", bid);
            detailScreen.putExtra("type", "detail");
            startActivity(detailScreen);
        } else {
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), getString(R.string.unverified_msg));

        }

    }

    @Override
    public void onGroupClick(int id, String groupType, String getjoin, String userId, String neighbourhood) {

       /* if((groupType.equals("Public"))) {
            Intent groupIntent = new Intent(getActivity(), GroupActivity.class);
            groupIntent.putExtra("neighbrhood", "");
            startActivity(groupIntent);
        }

        else if(groupType.equals("Private")) {
            if(getjoin.equals("join"))
            {
                Intent groupIntent = new Intent(getActivity(), GroupActivity.class);
                groupIntent.putExtra("neighbrhood", "");
                startActivity(groupIntent);
            }
            else if(getjoin.equals("pending"))
            {
                Intent groupIntent = new Intent(getActivity(), GroupActivity.class);
                groupIntent.putExtra("neighbrhood", "");
                startActivity(groupIntent);
            }

            else {
                Intent groupIntent = new Intent(getActivity(), GroupActivity.class);
                groupIntent.putExtra("neighbrhood", "");
                startActivity(groupIntent);
            }


        }
*/


        if (isUserVerified) {
            if ((groupType.equals("Public"))) {
                Intent groupIntent = new Intent(getActivity(), GroupActivity.class);
                groupIntent.putExtra("neighbrhood", "");
                startActivity(groupIntent);
            } else if (groupType.equals("Private")) {
                if (getjoin.equals("join")) {
                    Intent groupIntent = new Intent(getActivity(), GroupActivity.class);
                    groupIntent.putExtra("neighbrhood", "");
                    startActivity(groupIntent);
                } else if (getjoin.equals("pending")) {
                    Intent groupIntent = new Intent(getActivity(), GroupActivity.class);
                    groupIntent.putExtra("neighbrhood", "");
                    startActivity(groupIntent);
                } else {
                    Intent groupIntent = new Intent(getActivity(), GroupActivity.class);
                    groupIntent.putExtra("neighbrhood", "");
                    startActivity(groupIntent);
                }
            }
        } else {
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), getString(R.string.unverified_msg));
        }

    }

    @Override
    public void onEventClick(int pos) {

        if (isUserVerified) {
            Intent intent = new Intent(getActivity(), UserViewEvent.class);
            intent.putExtra("data", pos);
            startActivity(intent);
        } else {
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), getString(R.string.unverified_msg));
        }


    }

    @Override
    public void onSetEmoji(int pos, String likepost, String reactionCode, int likePos) {
        if (isUserVerified) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("userid", Integer.parseInt(sm.getString("user_id")));
            hm.put("postid", pos);
            hm.put("likestatus", likepost);
            hm.put("emojiunicode", reactionCode);
            emojiApi(hm, likePos);
        } else {
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), getString(R.string.unverified_msg));

        }
    }
    /*@Override
    public void onSetEmoji(int pos, String likepost) {
        if (isUserVerified) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("userid", Integer.parseInt(sm.getString("user_id")));
            hm.put("postid", pos);
            hm.put("likestatus", likepost);
            emojiApi(hm);
        } else {
            //if user not verified
        }


    }*/


    private void joinRequest() {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.group_private_when_not_send_rqst);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        FrameLayout tv_no = dialog.findViewById(R.id.post_frm);
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupIntent = new Intent(getActivity(), GroupActivity.class);
                groupIntent.putExtra("neighbrhood", sm.getString("neighbrhood"));
                startActivity(groupIntent);
                dialog.dismiss();
            }
        });


    }

   /* @Override 04-04-2024
    public void onClickEmoji(List<Emojilistdatum> pos) {
        EmojiBottomSheetFragment emogiBottomSheetDialog = new EmojiBottomSheetFragment(pos);
        emogiBottomSheetDialog.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

    }*/

    @Override
    public void onClickComment(List<Listdatum> mList1, String postId, int pos) {
        if (isUserVerified) {
            Intent i = new Intent(getActivity(), CommentActivity.class);
            i.putExtra("pos", 0);
            i.putExtra("postid", postId);
            i.putExtra("source", "wall");
            startActivity(i);
        } else {
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), getString(R.string.unverified_msg));
        }
    }

    @Override
    public void onClickSponser(int sponserId) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("sponsor_id", sponserId);
        Log.d("sponsor_id...", String.valueOf(sponserId));
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("event", "click");
        countClickApi(hm);
    }
//
//    @Override
//    public void onClickRateNow(int pos) {
//        starDialog();
//    }
//
//    @Override
//    public void onClickWriteReview(int pos) {
//
//        Intent detailScreen = new Intent(getActivity(), BusinessDetailActivity.class);
//        detailScreen.putExtra("name", "ABC Business");
//        detailScreen.putExtra("category","Bekery");
//        detailScreen.putExtra("desc", "This is abc business");
//        detailScreen.putExtra("tagline","ABC tagline");
//        detailScreen.putExtra("type","write_a_review");
//        startActivity(detailScreen);
//
//    }
//
//    @Override
//    public void onClickReadReview(int pos) {
//
//        Intent detailScreen = new Intent(getActivity(), BusinessDetailActivity.class);
//        detailScreen.putExtra("name", "ABC Business");
//        detailScreen.putExtra("category","Bekery");
//        detailScreen.putExtra("desc", "This is abc business");
//        detailScreen.putExtra("tagline","ABC tagline");
//        detailScreen.putExtra("type","review");
//        startActivity(detailScreen);
//
//    }
//
//

    private void openWebview(int pos) {
        wallPostDetailFragment = new WallPostDetailFragment();
        Bundle args = new Bundle();
        args.putInt("YourKey", pos);
        wallPostDetailFragment.setArguments(args);
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, wallPostDetailFragment).addToBackStack("image").commit();
        mViewPagerAdapter = new ViewPagerAdapter(getActivity(), images, uri);

    }

    private void starDialog(String businessId) {
        RecyclerView rv;
        TextView confirm, cancel;
        rating_dialog = new Dialog(getActivity());
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

    private void rateNow(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Log.d("businessuserrate" , hm.toString());
        Call<RateNowPojo> call = service.businessRating("businessuserrate", hm);
        call.enqueue(new Callback<RateNowPojo>() {
            @Override
            public void onResponse(Call<RateNowPojo> call, Response<RateNowPojo> response) {
                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }

            @Override
            public void onFailure(Call<RateNowPojo> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
 });
}

    private void imageDialog(List<ImagePojo> img, int pos) {
        RecyclerView rv;
        ImageView cancel;
        CardView cv_dialog_exit;

        // Use the custom fullscreen style
        image_dialog = new Dialog(getActivity(), R.style.FullscreenDialog);
        image_dialog.setContentView(R.layout.open_image_dialog);

        rv = image_dialog.findViewById(R.id.rv_image);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        cv_dialog_exit = image_dialog.findViewById(R.id.cv_dialog_exit);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        MediaSliderAdapter mediaSliderAdapter = new MediaSliderAdapter(image_dialog, img, "detail", pos, getActivity());
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

        cv_dialog_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.cancel();
            }
        });

        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.black)));

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(image_dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        image_dialog.getWindow().setAttributes(layoutParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image_dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            image_dialog.getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), android.R.color.black));
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.black)));
        }

        image_dialog.setCancelable(true);
        image_dialog.show();
    }

   /* private void imageDialog(List<ImagePojo> img, int pos) {
        RecyclerView rv;
        TextView confirm;
        ImageView cancel, iv_uploaded_image;
        CardView card;
        image_dialog = new Dialog(getActivity());
        image_dialog.setContentView(R.layout.open_image_dialog);
        rv = image_dialog.findViewById(R.id.rv_image);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        card = image_dialog.findViewById(R.id.card);
        iv_uploaded_image = image_dialog.findViewById(R.id.iv_uploaded_image);
//        if(type.equals("single"))
//        {
//           card.setVisibility(View.VISIBLE);
//            iv_uploaded_image.
//
//        }
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        ImageSliderAdapter1 imageUploadAdapter = new ImageSliderAdapter1(image_dialog, img, "detail", pos);
        rv.setAdapter(imageUploadAdapter);
        rv.scrollToPosition(pos);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.cancel();
            }
        });

        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.transparent)));
        image_dialog.setCancelable(true);
        image_dialog.show();
//        Window window = image_dialog.getWindow();
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }*/

    /*   @Override
       public void onImageClick(List<ImagePojo> list, int pos) {
           imageDialog(list, pos);
       }

   */

    private void filter(String text) {

        if (searchList.isEmpty()) {
            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(searchList);
        }
    }

    private void countClickApi(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.sponserClickApi("sponsorevent", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonObject obj = response.body().getAsJsonObject();
                if (obj.get("status").getAsString().equals("success")) {
//                    Log.d("response emoji---", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("res---", t.getMessage());
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Boolean isVerifiedUser);
    }

    private void sendDataToActivity(Boolean data) {
        if (mListener != null) {
            mListener.onFragmentInteraction(data);
        }
    }
    @Override
    public void onClickRateNow(String businessId) {
        starDialog(businessId);
}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Ensure the hosting activity implements the interface
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void welcomeApi(HashMap<String, Object> hm, int pos, String strCheck) {
        //Shubham Upadte
        dialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<EmojiPojo> call = service.thumsUpEmojiApi("userwellikes", hm);
        call.enqueue(new Callback<EmojiPojo>() {
            @Override
            public void onResponse(Call<EmojiPojo> call, Response<EmojiPojo> response) {

                assert response.body() != null;
                if (response.body().getStatus().equals("success")) {

                    if (strCheck.equals("thumsup")) {
                        String totalLike = String.valueOf(response.body().getTotal_like());
                        String likeStatus = String.valueOf(response.body().getLike_status());
                        adapter.updateWelcomeThumsup(pos, totalLike, likeStatus);

                    } else {
                        String likeBokay = String.valueOf(response.body().getTotal_bokay());
                        String bokayStatus = String.valueOf(response.body().getBokay_status());
                        adapter.updateWelcomeBokay(pos, likeBokay, bokayStatus);
                    }

                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<EmojiPojo> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }

    @Override
    public void onSetThumsUp(int welid, int weluserid, String likepost, int pos) {
        //Shubham Upadte
        if (isUserVerified) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("weluserid", weluserid);
            hm.put("userid", Integer.parseInt(sm.getString("user_id")));
            hm.put("welcomeid", welid);
            hm.put("likestatus", likepost);
            welcomeApi(hm, pos, "thumsup");
        } else {
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), getString(R.string.unverified_msg));
        }
    }

    @Override
    public void onSetFlower(int welid, int weluserid, String likepost, int pos) {
        //Shubham Upadte
        if (isUserVerified) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("weluserid", weluserid);
            hm.put("userid", Integer.parseInt(sm.getString("user_id")));
            hm.put("welcomeid", welid);
            hm.put("bokaystatus", likepost);
            welcomeApi(hm, pos, "bokay");
        } else {
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), getString(R.string.unverified_msg));
        }
    }

    @Override
    public void onClickEmoji(int postid) {
        if (isUserVerified) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("postid", postid);
            getPostEmojiList(hm);
        } else {
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), getString(R.string.unverified_msg));

        }
    }


    @Override
    public void onClickLike(int weluserid, String str) {
        if (isUserVerified) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("weluserid", weluserid);
            getWelcomeEmojiList(hm, str);
        } else {
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), getString(R.string.unverified_msg));

        }
    }

    @Override
    public void onClickBouquet(int weluserid, String str) {
        if (isUserVerified) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("weluserid", weluserid);
            getWelcomeEmojiList(hm, str);
        } else {
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), getString(R.string.unverified_msg));

        }
    }

    private void getPostEmojiList(HashMap<String, Object> hm) {
        dialog.show();
        Boolean isInternetConnection = GlobalMethods.checkConnection(getActivity());
        if (isInternetConnection) {
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<PostEmojiListModel> call = service.postEmojiListApi("userpostemojilist", hm);
            call.enqueue(new Callback<PostEmojiListModel>() {
                @Override
                public void onResponse(Call<PostEmojiListModel> call, Response<PostEmojiListModel> response) {

                    postEmojiListModel = response.body();

                    FavoriteBottomSheetFragment favoriteBottomSheetFragment = new FavoriteBottomSheetFragment(postEmojiListModel.getEmojilistdata());
                    favoriteBottomSheetFragment.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<PostEmojiListModel> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        } else {

            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), "     No internet connection.");
        }
    }


    @Override
    public void onClickShare(int pos) {
        if (isUserVerified) {
            String imageUrl = listdata.get(pos).getPostImages().get(0).getImg();
            downloadAndShareImage(imageUrl, pos);;
        } else {
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), getString(R.string.unverified_msg));

        }
    }

    @Override
    public void onDeletePost(int postId) {
        confirmDeleteDialog(postId);
    }

    @Override
    public void onReportSelect(List<Listdatum> mList, int pos) {

        if (mList.get(pos).getPostImages() != null && !mList.get(pos).getPostImages().isEmpty()) {
            postImages.clear();
            postImages.addAll(mList.get(pos).getPostImages());
        }

        Intent i = new Intent(getActivity(), ReportActivity.class);
        i.putParcelableArrayListExtra("postImages", new ArrayList<>(postImages));
        i.putExtra("userName", mList.get(pos).getUsername());
        i.putExtra("profilePhoto", mList.get(pos).getUserpic());
        i.putExtra("neighborhood", mList.get(pos).getNeighborhood());
        i.putExtra("post_type", mList.get(pos).getPost_type());
        i.putExtra("post_id", mList.get(pos).getPostid());
        i.putExtra("subject", mList.get(pos).getPost_message());
        startActivity(i);
    }

    @Override
    public void onDmSelect(List<Listdatum> mList, int pos) {
        int id = Integer.parseInt(sm.getString("user_id"));

        if (id == Integer.parseInt(mList.get(pos).getCreatedby())) {

        } else {
            Intent i = new Intent(getActivity(), SellerChatActivity.class);
            i.putExtra("eventId", Integer.parseInt(mList.get(pos).getCreatedby()));
            i.putExtra("userName", mList.get(pos).getUsername());
            i.putExtra("subject", mList.get(pos).getPost_type());
            i.putExtra("pic", mList.get(pos).getUserpic());
            startActivity(i);
            dialog.dismiss();
        }
    }

    public void confirmDeleteDialog(int post) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.post_delete_pop_up);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        TextView tv_no = dialog.findViewById(R.id.no_id);
        TextView tv_yes = dialog.findViewById(R.id.yes_id);
        ImageView img_cancel = dialog.findViewById(R.id.iv9);
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                hm.put("postid", post);
                deletePostApi(hm);
            }
        });
    }

    private void deletePostApi(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<EmojiPojo> call = service.deletePostApi("deletepostbyuser", hm);
        call.enqueue(new Callback<EmojiPojo>() {
            @Override
            public void onResponse(Call<EmojiPojo> call, Response<EmojiPojo> response) {
                if (response.body().getStatus().equals("success")) {
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                    Log.d("response emoji---", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<EmojiPojo> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }


    @Override
    public void onImageClick(List<ImagePojo> list, int pos) {
        imageDialog(list, pos);
    }


    @Override
    public void onFavSelect(int postid, String neighbourhood, int favoriteStatus, int postPos) {
        //Shubham Upadte

        if (isUserVerified) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("userid", Integer.parseInt(sm.getString("user_id")));
            hm.put("postid", postid);
            hm.put("type", "Post");

            if (favoriteStatus == 0) {
                hm.put("neighbrhood", neighbourhood);
                favApi("userfavouritespost", hm, postPos);

            } else {
                favApi("userunfavouritepost", hm, postPos);
            }
        } else {
            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), getString(R.string.unverified_msg));

        }
    }


    public void favApi(String type, HashMap<String, Object> hm, int postPos) {
        dialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<WallPojo> call = service.favApi(type, hm);
        call.enqueue(new Callback<WallPojo>() {
            @Override
            public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {

                if (response.body().getStatus().equals("success")) {

                    int favoriteStatus = response.body().getFavouritstatus();

                    adapter.updatePostItem(postPos, favoriteStatus);

                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<WallPojo> call, Throwable t) {

            }
        });

    }
    private void downloadAndShareImage(String imageUrl, int pos) {
        if (imageUrl == null || imageUrl.isEmpty() || imageUrl.contains("video")) {
            shareText(pos);
            return;
        }

        new Thread(() -> {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                File imageFile = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "shared_image.jpg");
                FileOutputStream outputStream = new FileOutputStream(imageFile);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                shareImage(imageFile, pos);
            } catch (Exception e) {
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Error downloading image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void shareImage(File imageFile, int pos) {
        try {
            Uri imageUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileprovider", imageFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Neighbrsnook post");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this post on Neighbrsnook,\n" +
                    "\n" + listdata.get(pos).getPost_type() +
                    "\n" + listdata.get(pos).getPost_message() +
                    "\n" + listdata.get(pos).getNeighborhood() +
                    "\n\nhttps://neighbrsnook.com/open-app" +
                    "\n\nhttps://neighbrsnook.com/");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            getActivity().runOnUiThread(() -> startActivity(Intent.createChooser(shareIntent, "Share via")));
        } catch (Exception e) {
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Error sharing image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void shareText(int pos) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Neighbrsnook post");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this post on Neighbrsnook,\n" +
                    "\n" + listdata.get(pos).getPost_type() +
                    "\n" + listdata.get(pos).getPost_message() +
                    "\n" + listdata.get(pos).getNeighborhood() +
                    "\n\nhttps://neighbrsnook.com/open-app" +
                    "\n\nhttps://neighbrsnook.com/");

            getActivity().runOnUiThread(() -> startActivity(Intent.createChooser(shareIntent, "Share via")));
        } catch (Exception e) {
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Error sharing text: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void profileGetData() {
        hashMap = new HashMap<>();
        hashMap.put("loggeduser", sm.getString("user_id"));
        userProfileGetData(hashMap);
    }
    private void userProfileGetData(HashMap<String, Object> hm) {
        Boolean isInternetConnection = GlobalMethods.checkConnection(getActivity());
        if (isInternetConnection) {
            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            //step2 rgn Call<JsonElement> call = service.profileapisec("userprofile", Integer.parseInt(PrefMananger.GetLoginData(context).getId()), hm);
            Call<JsonElement> call = service.profileapisec("userprofile", Integer.parseInt(sm.getString("user_id")), hm);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    dialog.dismiss();
                    JsonElement jsonElement = response.body();
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    try {
                        sm.setString("userphoto", jsonObject.get("userpic").getAsString());
                        location_neighbrhood = jsonObject.get("neighborhood").getAsString();
                        address = jsonObject.get("address").getAsString();
                       addressOne = jsonObject.get("addressone").getAsString();
                        stState = jsonObject.get("state").getAsString();
                      stCity = jsonObject.get("city").getAsString();
                       stPincode = jsonObject.get("pincode").getAsString();
                        dob = jsonObject.get("dob").getAsString();
                        genderProfile = jsonObject.get("gender").getAsString();
                     //   whatDoYou = jsonObject.get("nbrs_type").getAsString();
                        stUploadDocument = jsonObject.get("uploaded_doc").getAsString();
                      /*  Log.d("sdfsaee",stState);
                        Log.d("sdfsaee",stPincode);
                        Log.d("sdfsaee",stCity );*/


                    } catch (Exception e) {
                    }
                }
                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), "Something seems to have gone wrong.Please try again");
                    dialog.dismiss();
                    Log.d("res", t.getMessage());
                }
            });
        }
    }
    @Override
    public void onClickBlock(int pos) {
        String fromUserId = listdata.get(pos).getCreatedby();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("blocker_userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("blocked_userid", fromUserId);
        hm.put("action", "block");

        APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
        Call<CommonPojoSuccess> call = service.userBlockApi(hm);

        call.enqueue(new Callback<CommonPojoSuccess>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<CommonPojoSuccess> call, Response<CommonPojoSuccess> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CommonPojoSuccess commonPojoSuccess = response.body();
                    if ("success".equals(commonPojoSuccess.getStatus())) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                } else {
                  //  Log.e(TAG, "Token Update Failed: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CommonPojoSuccess> call, Throwable t) {
              //  Log.e(TAG, "API Failure: " + t.getMessage());
            }
        });
    }

}

