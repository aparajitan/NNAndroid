package com.app_neighbrsnook;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app_neighbrsnook.adapter.MediaSliderAdapter;
import com.app_neighbrsnook.adapter.PostAdapter;
import com.app_neighbrsnook.adapter.WallChildAdapter;
import com.app_neighbrsnook.fragment.EmojiBottomSheetFragment;
import com.app_neighbrsnook.fragment.FavoriteBottomSheetFragment;
import com.app_neighbrsnook.model.post.Listdatum;
import com.app_neighbrsnook.model.post.PostPojo;
import com.app_neighbrsnook.model.wall.Emojilistdatum;
import com.app_neighbrsnook.model.wall.PostEmojiListModel;
import com.app_neighbrsnook.model.wall.WallPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;

import com.app_neighbrsnook.pojo.EmojiPojo;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.app_neighbrsnook.post.CreatePostActivity;
import com.app_neighbrsnook.SalesModuleMarketPlace.SellerChatActivity;
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

public class PostActivity extends AppCompatActivity implements PostAdapter.PostRequest, WallChildAdapter.ImageCallBack {
    RecyclerView rv_post;
    SharedPrefsManager sm;
    HashMap<String, Object> hm = new HashMap<>();
    List<Listdatum> listdata = new ArrayList<>();
    ImageView searchImageView, addImageView, back_btn, cancel_iv;
    PostAdapter postAdapter;
    RelativeLayout search_rl;
    EditText search_et;
    TextView titleTv;
    Dialog image_dialog;
    String source, neighbourSource, neighbourId;
    int fromuserid;
    ProgressDialog dialog;
    PostEmojiListModel postEmojiListModel;
    boolean isVerifiedUser = true;
    List<ImagePojo> postImages = new ArrayList();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        sm = new SharedPrefsManager(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            source = extras.getString("source");
        }
        Intent i = getIntent();
        neighbourSource = i.getStringExtra("source");
        fromuserid = i.getIntExtra("user_id", 0);
        neighbourId = i.getStringExtra("neighbrhood");

        rv_post = findViewById(R.id.rv_post);
        searchImageView = findViewById(R.id.search_imageview);
        cancel_iv = findViewById(R.id.cancel_iv);
        search_rl = findViewById(R.id.search_rl);
        addImageView = findViewById(R.id.add_imageview);
        search_et = findViewById(R.id.search_et);
        back_btn = findViewById(R.id.back_btn);
        titleTv = findViewById(R.id.title);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        searchImageView.setVisibility(View.VISIBLE);
        addImageView.setVisibility(View.VISIBLE);
        rv_post.setLayoutManager(new LinearLayoutManager(this));
        titleTv.setText("Post");

        // Set up swipe refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postListApi(); // Refresh data
            }
        });

        // Load data initially
        postListApi();

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
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
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

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rv_post.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Show contact when scrolling stops
                    postAdapter.setShowContact(true);
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    // Hide contact while scrolling
                    postAdapter.setShowContact(false);
                }
            }
        });

        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVerifiedUser) {
                    Intent i = new Intent(PostActivity.this, CreatePostActivity.class);
                    startActivity(i);
                } else {
                    GlobalMethods.getInstance(PostActivity.this).globalDialog(PostActivity.this, getString(R.string.unverified_msg));
                }
            }
        });
    }

    private void postListApi() {
        // Only show progress dialog if not refreshing (swipe refresh has its own indicator)
        if (!swipeRefreshLayout.isRefreshing()) {
            dialog.show();
        }

        hm.put("userid", sm.getString("user_id"));
        if (source.equals("wall")) {
            hm.put("postlist", "postlist");
        } else if (source.equals("other")) {
            hm.put("userid", fromuserid);
        } else if (source.equals("myneighbrhood")) {
            hm.put("neighbrhood", neighbourId);
        } else if (source.equals("profile")) {
            hm.put("userid", fromuserid);
        }
        Log.d("sfsfs", source);

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<PostPojo> call = service.PostPojoList("postlist", hm);
        call.enqueue(new Callback<PostPojo>() {
            @Override
            public void onResponse(Call<PostPojo> call, Response<PostPojo> response) {
                // Hide refresh indicator when response is received
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                // Dismiss progress dialog if shown
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (response.isSuccessful() && response.body() != null) {
                    listdata = response.body().getListdata();

                    String status = response.body().getStatus();
                    if (status.equals("success")) {
                        if (response.body().getVerfiedMsg().equals("User Verification is not completed!")) {
                            isVerifiedUser = false;
                        }

                        // Update adapter with new data
                        if (postAdapter == null) {
                            if (source.equals("wall")) {
                                postAdapter = new PostAdapter(listdata, PostActivity.this, "wall", PostActivity.this, isVerifiedUser);
                            } else if (source.equals("myneighbrhood")) {
                                postAdapter = new PostAdapter(listdata, PostActivity.this, "myneighbrhood", PostActivity.this, isVerifiedUser);
                            } else {
                                postAdapter = new PostAdapter(listdata, PostActivity.this, "profile", PostActivity.this, isVerifiedUser);
                            }
                            rv_post.setAdapter(postAdapter);
                        } else {
                            postAdapter.updateData(listdata);
                            postAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(PostActivity.this, "Failed to load posts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostPojo> call, Throwable t) {
                // Hide refresh indicator on failure
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                // Dismiss progress dialog if shown
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                Log.d("res", t.getMessage());
                Toast.makeText(PostActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeletePost(int postId) {
        confirmDeleteDialog(postId);
    }

    @Override
    public void threeDot(int pos, int postId) {
        confirmDeleteDialog(postId);
    }

    private void deletePostApi(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<EmojiPojo> call = service.deletePostApi("deletepostbyuser", hm);
        call.enqueue(new Callback<EmojiPojo>() {
            @Override
            public void onResponse(Call<EmojiPojo> call, Response<EmojiPojo> response) {
                if (response.body().getMessage().equals("success")) {
                    postListApi();
                    Log.d("response emoji---", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<EmojiPojo> call, Throwable t) {
                Toast.makeText(PostActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }

    @Override
    public void onSetEmoji(int pos, String postlike) {
        //Shubham Upadte
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("postid", pos);
        hm.put("likestatus", postlike);
//        emojiApi(hm);
    }

    @Override
    public void onReportSelect(List<Listdatum> mList, int pos) {
        if (mList.get(pos).getPostImages() != null && !mList.get(pos).getPostImages().isEmpty()) {
            postImages.clear();
            postImages.addAll(mList.get(pos).getPostImages());
        }

        Intent i = new Intent(PostActivity.this, ReportActivity.class);
        i.putParcelableArrayListExtra("postImages", new ArrayList<>(postImages));
        i.putExtra("userName", mList.get(pos).getUsername());
        i.putExtra("profilePhoto", mList.get(pos).getUserpic());
        i.putExtra("neighborhood", mList.get(pos).getNeighborhood());
        i.putExtra("post_type", mList.get(pos).getPostType());
        i.putExtra("post_id", mList.get(pos).getPostid());
        i.putExtra("subject", mList.get(pos).getPostMessage());
        startActivity(i);
    }

    @Override
    public void onClickEmoji(List<Emojilistdatum> pos) {
        EmojiBottomSheetFragment emogiBottomSheetDialog = new EmojiBottomSheetFragment(pos);
        emogiBottomSheetDialog.show(this.getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    @Override
    public void onClickComment(List<Listdatum> mList, String postId, int pos) {
        Intent i = new Intent(PostActivity.this, CommentActivity.class);
        if (neighbourSource.equals("myneighbrhood")) {
            i.putExtra("source", "myneighbrhood");
            i.putExtra("neighbrhood", neighbourId);
        } else if (neighbourSource.equals("other")) {
            i.putExtra("source", "other");
            i.putExtra("user_id", fromuserid);
        } else {
            i.putExtra("source", "post");
        }
        i.putExtra("postid", postId);
        i.putExtra("pos", 0);
        startActivity(i);
    }
    @Override
    public void onClickShare(int pos) {
        String imageUrl = listdata.get(pos).getPostImages().get(0).getImg();
        downloadAndShareImage(imageUrl, pos);
    }

    private void emojiApi(HashMap<String, Object> hm, int likePos) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<EmojiPojo> call = service.emojiApi("userpostlikes", hm);
        call.enqueue(new Callback<EmojiPojo>() {
            @Override
            public void onResponse(Call<EmojiPojo> call, Response<EmojiPojo> response) {
                assert response.body() != null;
                if (response.body().getStatus().equals("success")) {

                    int totalLike = response.body().getTotal_like();
                    String likeStatus = String.valueOf(response.body().getLikestatus());
                    String emojiUnicode = String.valueOf(response.body().getEmojiunicode());

                    postAdapter.updateItemText(likePos, totalLike, likeStatus, emojiUnicode);
                }
            }

            @Override
            public void onFailure(Call<EmojiPojo> call, Throwable t) {
                Toast.makeText(PostActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }

    @Override
    public void onImageClick(List<ImagePojo> list, int pos) {
        imageDialog(list, pos);
    }

    private void imageDialog(List<ImagePojo> img, int pos) {
        RecyclerView rv;
        ImageView cancel;
        CardView cv_dialog_exit;

        // Use the custom fullscreen style
        image_dialog = new Dialog(PostActivity.this, R.style.FullscreenDialog);
        image_dialog.setContentView(R.layout.open_image_dialog);

        rv = image_dialog.findViewById(R.id.rv_image);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        cv_dialog_exit = image_dialog.findViewById(R.id.cv_dialog_exit);
        rv.setLayoutManager(new LinearLayoutManager(PostActivity.this, LinearLayoutManager.HORIZONTAL, false));
        MediaSliderAdapter mediaSliderAdapter = new MediaSliderAdapter(image_dialog, img, "detail", pos, PostActivity.this);
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

        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(PostActivity.this, android.R.color.black)));

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(image_dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        image_dialog.getWindow().setAttributes(layoutParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image_dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            image_dialog.getWindow().setStatusBarColor(ContextCompat.getColor(PostActivity.this, android.R.color.black));
        }

        AppCompatActivity activity = (AppCompatActivity) PostActivity.this;
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(PostActivity.this, android.R.color.black)));
        }

        image_dialog.setCancelable(true);
        image_dialog.show();
    }

    public void confirmDeleteDialog(int post) {
        Dialog dialog = new Dialog(PostActivity.this);
        dialog.setContentView(R.layout.post_delete_pop_up);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(PostActivity.this, android.R.color.transparent)));
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
                postListApi();
            }
        });
    }

    private void filter(String text) {
        List<Listdatum> filteredList = new ArrayList<>();
        for (Listdatum item : listdata) {
            if (item.getPostMessage().toLowerCase().contains(text.toLowerCase()) ||
                    item.getUsername().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        postAdapter.filterList(filteredList);
    }

    @Override
    public void onSetEmoji1(int pos, String likepost, String reactionCode, int likePos) {
        //Shubham Upadte
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("postid", pos);
        hm.put("likestatus", likepost);
        hm.put("emojiunicode", reactionCode);
        emojiApi(hm, likePos);
    }

    @Override
    public void onClickEmoji1(int postid) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("postid", postid);
        getPostEmojiList(hm);
    }

    @Override
    public void onDmSelect(List<Listdatum> mList, int pos) {
        int id = Integer.parseInt(sm.getString("user_id"));
        if (id == Integer.parseInt(mList.get(pos).getCreatedby())) {
        } else {
            Intent i = new Intent(PostActivity.this, SellerChatActivity.class);
            i.putExtra("eventId", Integer.parseInt(mList.get(pos).getCreatedby()));
            i.putExtra("userName", mList.get(pos).getUsername());
            i.putExtra("subject", mList.get(pos).getPostType());
            i.putExtra("pic", mList.get(pos).getUserpic());
            startActivity(i);
            dialog.dismiss();
        }
    }

    private void getPostEmojiList(HashMap<String, Object> hm) {
        dialog.show();
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if (isInternetConnection) {
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<PostEmojiListModel> call = service.postEmojiListApi("userpostemojilist", hm);
            call.enqueue(new Callback<PostEmojiListModel>() {
                @Override
                public void onResponse(Call<PostEmojiListModel> call, Response<PostEmojiListModel> response) {

                    postEmojiListModel = response.body();

                    FavoriteBottomSheetFragment favoriteBottomSheetFragment = new FavoriteBottomSheetFragment(postEmojiListModel.getEmojilistdata());
                    favoriteBottomSheetFragment.show(PostActivity.this.getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<PostEmojiListModel> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        } else {
            GlobalMethods.getInstance(this).globalDialog(this, "     No internet connection.");
        }
    }

    @Override
    public void onFavSelect(int postid, String neighbourhood, int favoriteStatus, int postPos) {
        //Shubham Upadte
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

                    postAdapter.updatePostItem(postPos, favoriteStatus);

                    Toast.makeText(PostActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "shared_image.jpg");
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
                runOnUiThread(() -> Toast.makeText(this, "Error downloading image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void shareImage(File imageFile, int pos) {
        try {
            Uri imageUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", imageFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Neighbrsnook post");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this post on Neighbrsnook,\n" +
                    "\n" + listdata.get(pos).getPostType() +
                    "\n" + listdata.get(pos).getPostMessage() +
                    "\n" + listdata.get(pos).getNeighborhood() +
                    "\n\nhttps://neighbrsnook.com/open-app" +
                    "\n\nhttps://neighbrsnook.com/");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            runOnUiThread(() -> startActivity(Intent.createChooser(shareIntent, "Share via")));
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(this, "Error sharing image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void shareText(int pos) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Neighbrsnook post");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this post on Neighbrsnook,\n" +
                    "\n" + listdata.get(pos).getPostType() +
                    "\n" + listdata.get(pos).getPostMessage() +
                    "\n" + listdata.get(pos).getNeighborhood() +
                    "\n\nhttps://neighbrsnook.com/open-app" +
                    "\n\nhttps://neighbrsnook.com/");
            runOnUiThread(() -> startActivity(Intent.createChooser(shareIntent, "Share via")));
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(this, "Error sharing text: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                    }
                } else {
                    // Log.e(TAG, "Token Update Failed: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CommonPojoSuccess> call, Throwable t) {
                //  Log.e(TAG, "API Failure: " + t.getMessage());
            }
        });
    }
}