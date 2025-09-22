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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.OrientationHelper;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.CommentActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.BusinessWallChildAdapter;
import com.app_neighbrsnook.adapter.EmojiAdapter;
import com.app_neighbrsnook.adapter.FavAdapter;
import com.app_neighbrsnook.adapter.MediaSliderAdapter;
import com.app_neighbrsnook.adapter.WallChildAdapter;
import com.app_neighbrsnook.businessModule.BusinessDetailActivity;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.event.UserViewEvent;
import com.app_neighbrsnook.group.GroupDetailActivity;
import com.app_neighbrsnook.model.wall.Emojilistdatum;
import com.app_neighbrsnook.model.wall.Listdatum;
import com.app_neighbrsnook.model.wall.PostEmojiListModel;
import com.app_neighbrsnook.model.wall.WallPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.EmojiPojo;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.pojo.RateNowPojo;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavFragment extends Fragment implements BusinessWallChildAdapter.ImageCallBack, WallChildAdapter.ImageCallBack, FavAdapter.WallRequest, EmojiAdapter.EmojiCallBack {
    RecyclerView mRecyclerView;
    TextView member_number_tv, title;
    ImageView searchImageView, addImageView, img_back, wall_imageview, cross_imageview;
    Dialog mail_dialog;
    ProgressDialog dialog;
    ViewPager mViewPager;
    RelativeLayout image_relativelayout, rootView;
    WallChildAdapter.ImageCallBack imageCallBack = this;
    Dialog rating_dialog;
    RelativeLayout search_rl;
    EditText search_et;
    ImageView cancel_iv;
    SharedPrefsManager sm;
    FavAdapter adapter;
    WallPojo rootObject;
    private DAO myDao;
    EmojiAdapter.EmojiCallBack ecb = this;
    Dialog image_dialog;
    LinearLayout root;
    List<Listdatum> listdata = new ArrayList<>();
    PostEmojiListModel postEmojiListModel;
    BusinessWallChildAdapter.ImageCallBack callBack = this;
    RatingBar ratingBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wall, container, false);

        myDao = Database.createDBInstance(getActivity()).getDao();
        sm = new SharedPrefsManager(getActivity());

        img_back = view.findViewById(R.id.back_btn);
        title = view.findViewById(R.id.title);
        member_number_tv = view.findViewById(R.id.member_number_tv);
        mRecyclerView = view.findViewById(R.id.wall_rv);
        search_rl = view.findViewById(R.id.search_rl);
        search_et = view.findViewById(R.id.search_et);
        cancel_iv = view.findViewById(R.id.cancel_iv);
        searchImageView = view.findViewById(R.id.search_imageview);
        addImageView = view.findViewById(R.id.add_imageview);
        root = view.findViewById(R.id.root);
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        wall_imageview = view.findViewById(R.id.wall_imageview);
        mViewPager = view.findViewById(R.id.viewPagerMain);
        cross_imageview = view.findViewById(R.id.cross_imageview);
        image_relativelayout = view.findViewById(R.id.image_relativelayout);
        rootView = view.findViewById(R.id.content);

        title.setText("Favourite");
        member_number_tv.setVisibility(View.GONE);
        member_number_tv.setText("4 Members");
        searchImageView.setVisibility(View.GONE);
        addImageView.setVisibility(View.GONE);
        img_back.setVisibility(View.GONE);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        @SuppressLint("WrongConstant")
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new FavAdapter(new ArrayList<>(), getActivity(), imageCallBack, callBack, ecb, FavFragment.this);
        mRecyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWallApi();
            }
        });

        getWallApi();

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
                startActivity(new Intent(getActivity(), CreatePostActivity.class));
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

        cross_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_relativelayout.setVisibility(View.GONE);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    adapter.setShowContact(true);
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    adapter.setShowContact(false);
                }
            }
        });

        return view;
    }

    private void hideKeyboard() {
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
    }

    private void getWallApi() {
        // Only show progress dialog if not refreshing (swipe refresh has its own indicator)
        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            dialog.show();
        }

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<WallPojo> call = service.favistApi("userfavouritelisting", hm);
        call.enqueue(new Callback<WallPojo>() {
            @Override
            public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {
                // Hide refresh indicator when response is received
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                // Dismiss progress dialog if shown
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (response.isSuccessful() && response.body() != null) {
                    rootObject = response.body();
                    listdata = rootObject.getListdata();

                    if (member_number_tv != null) {
                        member_number_tv.setText(rootObject.getMember_count() + " " + "Members");
                    }

                    // Update adapter with new data
                    if (adapter != null) {
                        adapter.updateData(listdata);
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter = new FavAdapter(listdata, getActivity(), imageCallBack, callBack, ecb, FavFragment.this);
                        mRecyclerView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WallPojo> call, Throwable t) {
                // Hide refresh indicator on failure
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                // Dismiss progress dialog if shown
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                Toast.makeText(getActivity(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FavFragment", "API call failed: " + t.getMessage());
            }
        });
    }

    @Override
    public void onEmojiClick(List<Emojilistdatum> pos) {
        EmojiBottomSheetFragment bottomSheetDialog = new EmojiBottomSheetFragment(pos);
        bottomSheetDialog.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    @Override
    public void onVideoClick(int bid, List<ImagePojo> list, int pos) {
        Intent detailScreen = new Intent(getActivity(), BusinessDetailActivity.class);
        detailScreen.putExtra("id", bid);
        detailScreen.putExtra("type", "detail");
        startActivity(detailScreen);
    }

    @Override
    public void onThreeDotClick(List<Listdatum> mList, int pos) {
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
                        "fav");
                bottomSheetBusiness.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                break;

            case "Poll":
                BottomSheetPollFragment bottomSheetPoll = new BottomSheetPollFragment(getActivity(),
                        mList.get(pos).getFavouritstatus(),
                        mList.get(pos).getUsername(),
                        mList.get(pos).getNeighborhood(),
                        mList.get(pos).getPollQuestion(),
                        mList.get(pos).getPollid(),
                        mList.get(pos).getCreatedby(),
                        "fav");
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
    }

    @Override
    public void onDetailPage(int position, int pos, String isVoted, String ispollrunning) {
        if (ispollrunning.equals("1")) {
            if (isVoted.equals("1")) {
                Intent pollDetailIntent = new Intent(getActivity(), PollDetailActivity.class);
                pollDetailIntent.putExtra("id", pos);
                startActivity(pollDetailIntent);
            } else {
                Intent pollDetailIntent = new Intent(getActivity(), PollDetailActivity.class);
                pollDetailIntent.putExtra("id", pos);
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
            }
        } else if (ispollrunning.equals("0")) {
            Intent pollDetailIntent = new Intent(getActivity(), PollDetailActivity.class);
            pollDetailIntent.putExtra("id", pos);
            startActivity(pollDetailIntent);
        } else {

        }
    }

    @Override
    public void onSetEmoji(int pos, String likepost, String reactionCode, int likePos) {
//        if (isUserVerified) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("postid", pos);
        hm.put("likestatus", likepost);
        hm.put("emojiunicode", reactionCode);
        emojiApi(hm, likePos);
//        }
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
        Intent detailScreen = new Intent(getActivity(), BusinessDetailActivity.class);
        detailScreen.putExtra("id", bid);
        detailScreen.putExtra("type", "detail");
        startActivity(detailScreen);
    }

    @Override
    public void onGroupClick(int id, String type, String getjoin) {
        Intent detailScreen = new Intent(getActivity(), GroupDetailActivity.class);
        detailScreen.putExtra("id", id);
        detailScreen.putExtra("data", type);
        detailScreen.putExtra("type", "wall");
        startActivity(detailScreen);
    }

    @Override
    public void onEventClick(int pos) {
        Intent intent = new Intent(getActivity(), UserViewEvent.class);
        intent.putExtra("data", pos);
        startActivity(intent);
    }

    @Override
    public void onClickEmoji(int postid) {
        //Shubham Upadte
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("postid", postid);
        getPostEmojiList(hm);
    }

    @Override
    public void onClickRateNow(String businessId) {
        starDialog(businessId);
    }

    private void getPostEmojiList(HashMap<String, Object> hm) {
        //Shubham Upadte
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
        Log.d("businessuserrate", hm.toString());
        Call<RateNowPojo> call = service.businessRating("businessuserrate", hm);
        call.enqueue(new Callback<RateNowPojo>() {
            @Override
            public void onResponse(Call<RateNowPojo> call, Response<RateNowPojo> response) {
                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .detach(FavFragment.this)
                        .attach(FavFragment.this)
                        .commit();
            }

            @Override
            public void onFailure(Call<RateNowPojo> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }

    @Override
    public void onClickComment(List<Listdatum> mList1, String postId, int pos) {
        Intent i = new Intent(getActivity(), CommentActivity.class);
        i.putExtra("pos", 0);
        i.putExtra("postid", postId);
        i.putExtra("source", "Favourite");
        startActivity(i);
    }

    @Override
    public void onClickShare(int pos) {

        String imageUrl = listdata.get(pos).getPostImages().get(0).getImg();
        downloadAndShareImage(imageUrl, pos);
    }

    private void imageDialog(List<ImagePojo> img, int pos) {
        //Shubham Upadte
        RecyclerView rv;
        ImageView cancel;
        CardView cv_dialog_exit;

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

    private void selectImage(int pos) {
        ImageView cancel, wall_image_imageview;
        mail_dialog = new Dialog(getActivity());
        mail_dialog.setContentView(R.layout.open_image);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mail_dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        mail_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.transparent)));
        mail_dialog.getWindow().setAttributes(lp);
        cancel = mail_dialog.findViewById(R.id.cross_imageview);
        wall_image_imageview = mail_dialog.findViewById(R.id.wall_image_imageview);
        wall_image_imageview.setBackgroundResource(pos);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_dialog.cancel();
            }
        });

        mail_dialog.setCancelable(true);

        mail_dialog.show();
    }

    @Override
    public void onImageClick(List<ImagePojo> list, int pos) {
        imageDialog(list, pos);
    }

    private void filter(String text) {
        List<Listdatum> filteredlist = new ArrayList<>();
        for (Listdatum item : listdata) {
            if (item.getUsername().toLowerCase().contains(text.toLowerCase()) ||
                    item.getType().toLowerCase().contains(text.toLowerCase()) ||
                    (item.getBusinessName() != null && item.getBusinessName().toLowerCase().contains(text.toLowerCase())) ||
                    (item.getBusinessDesc() != null && item.getBusinessDesc().toLowerCase().contains(text.toLowerCase())) ||
                    (item.getBusinessTagline() != null && item.getBusinessTagline().toLowerCase().contains(text.toLowerCase())) ||
                    (item.getPost_message() != null && item.getPost_message().toLowerCase().contains(text.toLowerCase())) ||
                    (item.getPost_type() != null && item.getPost_type().toLowerCase().contains(text.toLowerCase())) ||
                    (item.getEvent_name() != null && item.getEvent_name().toLowerCase().contains(text.toLowerCase())) ||
                    (item.getEvent_description() != null && item.getEvent_description().toLowerCase().contains(text.toLowerCase())) ||
                    (item.getPollTitle() != null && item.getPollTitle().toLowerCase().contains(text.toLowerCase())) ||
                    (item.getPollQuestion() != null && item.getPollQuestion().toLowerCase().contains(text.toLowerCase())) ||
                    (item.getPost_type() != null && item.getPost_type().toLowerCase().contains(text.toLowerCase()) ||
                            (item.getGroup_name() != null && item.getGroup_name().toLowerCase().contains(text.toLowerCase())) ||
                            (item.getGroup_description() != null && item.getGroup_description().toLowerCase().contains(text.toLowerCase())) ||
                            (item.getGroup_type() != null && item.getGroup_type().toLowerCase().contains(text.toLowerCase())))
            ) {
                filteredlist.add(item);
            }
        }


        if (filteredlist.isEmpty()) {
            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(filteredlist);
        }
    }

    @Override
    public void onFavSelect(int postid, String neighbourhood, int favoriteStatus, int postPos) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("postid", postid);
        hm.put("type", "Post");
        favApi("userunfavouritepost", hm, postPos);
    }

    public void favApi(String type, HashMap<String, Object> hm, int postPos) {
        dialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<WallPojo> call = service.favApi(type, hm);
        call.enqueue(new Callback<WallPojo>() {
            @Override
            public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {

                if (response.body().getStatus().equals("success")) {
                    getWallApi();
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
                    "\n\nhttp://bit.ly/4amRbZL" +
                    "\n\nhttps://neighbrsnook.com/open-app");

            getActivity().runOnUiThread(() -> startActivity(Intent.createChooser(shareIntent, "Share via")));
        } catch (Exception e) {
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Error sharing text: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}
