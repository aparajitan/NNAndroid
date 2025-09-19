package com.app_neighbrsnook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.abusive.BadWordFilter;
import com.app_neighbrsnook.adapter.CommentAdapter;
import com.app_neighbrsnook.adapter.EmojiAdapter;
import com.app_neighbrsnook.adapter.MediaSliderAdapter;
import com.app_neighbrsnook.adapter.ReplyAdapter;
import com.app_neighbrsnook.adapter.WallChildAdapter;
import com.app_neighbrsnook.fragment.CommentLikeBottomSheetFragment;
import com.app_neighbrsnook.fragment.EmojiBottomSheetFragment;
import com.app_neighbrsnook.fragment.FavoriteBottomSheetFragment;
import com.app_neighbrsnook.model.post.Listdatum;
import com.app_neighbrsnook.model.post.PostPojo;
import com.app_neighbrsnook.model.postComment.CommentLikePojo;
import com.app_neighbrsnook.model.postComment.CommentPojo;
import com.app_neighbrsnook.model.postComment.Postlistdatum;
import com.app_neighbrsnook.model.wall.Emojilistdatum;
import com.app_neighbrsnook.model.wall.PostEmojiListModel;
import com.app_neighbrsnook.model.wall.WallPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.EmojiPojo;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.profile.MyProfileOtherUser;
import com.app_neighbrsnook.utils.EmojiUtil;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener, EmojiAdapter.EmojiCallBack, WallChildAdapter.ImageCallBack, CommentAdapter.OnReplyButtonClickListener, ReplyAdapter.OnReplyButtonClickListener {
    TextView member_number_tv, title, reaction_textview, user_tv, post_msg_textview, post_type_tv, comment_textview, created_date_tv;
    ImageView img_back, wall_imageview, send_imageview, searchImageView, addImageView, profile_imageview, iv_favorite;
    EditText msg_et;
    RecyclerView comment_rv;
    LinearLayout rv_ll;
    SharedPrefsManager sm;
    ImageView share_imageview;
    LinearLayout comment_ll, ll_favorite;
    ImageView reactButton;
    Context context;
    RecyclerView wall_image_rv;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout emoji_ll;
    WallPojo rootObject;
    CommentPojo commentPojo;
    List<Listdatum> listdata1 = new ArrayList<>();
    List<Postlistdatum> commentList = new ArrayList<>();
    Dialog image_dialog;
    int postId, fromUserId, favoriteStatus;
    WallChildAdapter.ImageCallBack imageCallBack = this;
    WallChildAdapter childRecyclerViewAdapter;
    int pos;
    ProgressDialog progressDialog;
    String postlike, source, neighbourId, strNeighbourhood, strPostId;
    HashMap<String, Object> hm = new HashMap<>();
    LinearLayout root, ll_reactButton, ll_reactCount;
    CardView reactions_card;
    ImageView reactionLike, reactionLove, reactionHaha, reactionWow, reactionSad, reactionAngry;
    boolean reactionsVisible = false;
    PostEmojiListModel postEmojiListModel;
    LinearSnapHelper snapHelper = new LinearSnapHelper();
    LinearLayout ll_reply_comment_name;
    TextView txtv_reply_comment_name;
    ImageView cross_imageview, send_reply_imageview;
    String postCommentId, topLevelUserId, topLevelUsername;
    CommentLikePojo commentLikePojo;
    CommentAdapter adapter;
    String strPostType, strPostMsg, imageUrl;
    private Set<String> expandedCommentIds = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        items();

        sm = new SharedPrefsManager(this);
        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", 0);
        neighbourId = intent.getStringExtra("neighbrhood");
        fromUserId = intent.getIntExtra("user_id", 0);
        source = intent.getStringExtra("source");
        strPostId = intent.getStringExtra("postid");

        if (!source.equals("Notification")) {
            msg_et.requestFocus();
            showKeyboard(msg_et);
        }

        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        wall_image_rv.setLayoutManager(layoutManager);
        wall_image_rv.setHasFixedSize(false);
        title.setText("Post");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");

        getPostDetailApi();

        share_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadAndShareImage(imageUrl, pos);
            }
        });

        ll_reactCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("postid", postId);
                getPostEmojiList(hm);
            }
        });

        comment_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_imageview.setVisibility(View.VISIBLE);
                send_reply_imageview.setVisibility(View.GONE);
                ll_reply_comment_name.setVisibility(View.GONE);
                msg_et.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(msg_et, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        cross_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_imageview.setVisibility(View.VISIBLE);
                send_reply_imageview.setVisibility(View.GONE);
                ll_reply_comment_name.setVisibility(View.GONE);
                msg_et.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(msg_et, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reactions_card.setVisibility(View.GONE);
            }
        });

        ll_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                hm.put("postid", postId);
                hm.put("type", "Post");

                if (favoriteStatus == 0) {
                    hm.put("neighbrhood", strNeighbourhood);
                    favApi("userfavouritespost", hm);

                } else {
                    favApi("userunfavouritepost", hm);
                }            }
        });

        ll_reactButton.setOnTouchListener(new View.OnTouchListener() {
            //Shubham Upadte
            private boolean isLongPress = false;
            private Handler handler = new Handler();
            private Runnable longPressRunnable = new Runnable() {
                @Override
                public void run() {
                    isLongPress = true;
                    reactions_card.setVisibility(View.VISIBLE);
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isLongPress = false;
                        handler.postDelayed(longPressRunnable, 500);
                        return true;

                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacks(longPressRunnable);
                        if (!isLongPress) {
                            switch (postlike) {
                                case "0":
                                    hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                                    hm.put("postid", postId);
                                    hm.put("likestatus", "1");
                                    hm.put("emojiunicode", "");
                                    emojiApi(hm);
                                    break;
                                case "1":
                                    hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                                    hm.put("postid", postId);
                                    hm.put("likestatus", "0");
                                    hm.put("emojiunicode", "");
                                    emojiApi(hm);
                                    break;
                                default:
                            }
                        } else {
                            reactionsVisible = true;
                        }

                        return true;
                }
                return false;
            }
        });

        setReactionClickListener(reactionLike, "👍");
        setReactionClickListener(reactionLove, "❤");
        setReactionClickListener(reactionHaha, "😂");
        setReactionClickListener(reactionWow, "😮");
        setReactionClickListener(reactionSad, "🥳");
        setReactionClickListener(reactionAngry, "😎");
    }

    private void items() {
        ll_reply_comment_name = findViewById(R.id.ll_reply_comment_name);
        txtv_reply_comment_name = findViewById(R.id.txtv_reply_comment_name);
        cross_imageview = findViewById(R.id.cross_imageview);
        send_reply_imageview = findViewById(R.id.send_reply_imageview);
        send_reply_imageview.setOnClickListener(this);
        wall_image_rv = findViewById(R.id.wall_image_rv);
        img_back = findViewById(R.id.back_btn);
        title = findViewById(R.id.title);
        member_number_tv = findViewById(R.id.member_number_tv);
        wall_imageview = findViewById(R.id.wall_imageview);
        send_imageview = findViewById(R.id.send_imageview);
        msg_et = findViewById(R.id.msg_et);
        comment_rv = findViewById(R.id.comment_rv);
        profile_imageview = findViewById(R.id.profile_imageview);
        comment_rv.setLayoutManager(new LinearLayoutManager(this));
        img_back.setOnClickListener(this);
        send_imageview.setOnClickListener(this);
        post_msg_textview = findViewById(R.id.post_msg_textview);
        post_type_tv = findViewById(R.id.post_type_tv);
        comment_textview = findViewById(R.id.comment_textview);
        rv_ll = findViewById(R.id.rv_ll);
        reactions_card = findViewById(R.id.reactions_card);
        reactionLike = findViewById(R.id.reaction_like);
        reactionLove = findViewById(R.id.reaction_love);
        reactionHaha = findViewById(R.id.reaction_haha);
        reactionWow = findViewById(R.id.reaction_wow);
        reactionSad = findViewById(R.id.reaction_sad);
        reactionAngry = findViewById(R.id.reaction_angry);
        root = findViewById(R.id.root);
        ll_reactButton = findViewById(R.id.ll_react_button);
        ll_reactCount = findViewById(R.id.ll_react_count);
        comment_ll = findViewById(R.id.comment_ll);
        reactButton = findViewById(R.id.reactButton);
        emoji_ll = findViewById(R.id.emoji_ll);
        wall_image_rv = findViewById(R.id.wall_image_rv);
        user_tv = findViewById(R.id.user_tv);
        created_date_tv = findViewById(R.id.created_date_tv);
        share_imageview = findViewById(R.id.share_imageview);
        reaction_textview = findViewById(R.id.reaction_count);
        addImageView = findViewById(R.id.add_imageview);
        searchImageView = findViewById(R.id.search_imageview);
        iv_favorite = findViewById(R.id.iv_favorite);
        ll_favorite = findViewById(R.id.ll_favorite);
        searchImageView.setVisibility(View.GONE);
        addImageView.setVisibility(View.GONE);
    }

    private void getPostDetailApi() {
        progressDialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("postid", strPostId);
        Call<PostPojo> call = service.getPostDetail("postdetails", hm);
        call.enqueue(new Callback<PostPojo>() {
            @Override
            public void onResponse(Call<PostPojo> call, Response<PostPojo> response) {
                listdata1 = response.body().getListdata();
                String status = response.body().getStatus();
                if (status.equals("success")) {
                    try {
                        postId = Integer.parseInt(listdata1.get(pos).getPostid());
                        strNeighbourhood = listdata1.get(pos).getNeighborhood();
                        favoriteStatus = listdata1.get(pos).getFavouritstatus();
                        strPostType = listdata1.get(pos).getPostType();
                        strPostMsg = listdata1.get(pos).getPostMessage();
                        imageUrl = listdata1.get(pos).getPostImages().get(0).getImg();

                        Log.d("pos....", listdata1.get(pos).getPostid());
                    } catch (Exception e) {
                    }

                    comment_textview.setText(String.valueOf(listdata1.get(pos).getTotcomment()));
                    reaction_textview.setText(String.valueOf(listdata1.get(pos).getTotalEmojis()));
                    user_tv.setText(listdata1.get(pos).getUsername());
                    post_msg_textview.setText(listdata1.get(pos).getPostMessage());
                    post_type_tv.setText(listdata1.get(pos).getPostType());
                    created_date_tv.setText(listdata1.get(pos).getNeighborhood() + ", " + listdata1.get(pos).getCreatedOn());
                    postlike = listdata1.get(pos).getPostlike();

                    if ((listdata1.get(pos).getPostImages() != null && listdata1.get(pos).getPostImages().size() != 0)) {
                        childRecyclerViewAdapter = new WallChildAdapter(listdata1.get(pos).getPostImages(), CommentActivity.this, imageCallBack);
                        wall_image_rv.setAdapter(childRecyclerViewAdapter);
                        snapHelper.attachToRecyclerView(wall_image_rv);
                    }
                    if (listdata1.get(pos).getUserpic().isEmpty()) {
                        profile_imageview.setImageResource(R.drawable.profile);
                    } else {
                        Picasso.get().load(listdata1.get(pos).getUserpic()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile)
                                .into(profile_imageview);
                    }
                    Log.e("dsfadfs", "Position : " + pos + "Response" + new Gson().toJson(listdata1));
                    Log.e("dsfadfs", "Position : " + pos + "Response" + new Gson().toJson(listdata1.get(pos)));

                    if (listdata1.get(pos).getPostlike().equals("1")) {
                        if (listdata1.get(pos).getPostemojiunicode() != null && !listdata1.get(pos).getPostemojiunicode().equals("")) {
                            Bitmap emojiBitmap = EmojiUtil.getEmojiBitmap(getApplicationContext(), listdata1.get(pos).getPostemojiunicode(), 32);
                            reactButton.setImageBitmap(emojiBitmap);
                        } else {
                            reactButton.setImageResource(R.drawable.baseline_favorite_24);
                        }
                    } else {
                        reactButton.setImageResource(R.drawable.baseline_favorite_border_24);
                    }

                    //issue in total like in api when create post then total like will be 0 in int but when like post like has been string
                    Log.e("dsfadfs", "Position : " + pos + "Response" + new Gson().toJson(listdata1));

                    if ((listdata1.get(pos).getTotallike()) == 0) {
                        reaction_textview.setText("");
                    } else {
                        reaction_textview.setText(String.valueOf(listdata1.get(pos).getTotallike()));
                    }

                    if (listdata1.get(pos).getTotcomment().equals("0")) {
                        comment_textview.setText("");
                    } else {
                        comment_textview.setText(String.valueOf(listdata1.get(pos).getTotcomment()));
                    }

                    if (listdata1.get(pos).getFavouritstatus()== 0){
                        iv_favorite.setImageResource(R.drawable.svg_bookmark_outline);
                    } else {
                        iv_favorite.setImageResource(R.drawable.svg_bookmark_fill);
                    }

                    commentListApi();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<PostPojo> call, Throwable t) {
                Log.d("res", t.getMessage());
            }
        });
    }

    private void commentListApi() {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("postid", postId);
        Log.d("postid....commentApi", String.valueOf(postId));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<CommentPojo> call = service.commentListApi("postcommentslist", hm);
        call.enqueue(new Callback<CommentPojo>() {
            @Override
            public void onResponse(Call<CommentPojo> call, Response<CommentPojo> response) {
                CommentPojo commentPojo = response.body();

                if (commentPojo.getStatus().equals("success")) {
                    commentList = commentPojo.getPostlistdata();
                    adapter = new CommentAdapter(commentList,CommentActivity.this, expandedCommentIds);
                    comment_rv.setAdapter(adapter);
                    comment_rv.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
                    if (commentList.size() == 0) {
                        rv_ll.setVisibility(View.GONE);
                    } else {
                        rv_ll.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentPojo> call, Throwable t) {

            }
        });
    }

    private void commentApi() {
        progressDialog.show();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("postid", postId);
        hm.put("commenttext", msg_et.getText().toString());
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<WallPojo> call = service.commentApi("postcomments", hm);
        call.enqueue(new Callback<WallPojo>() {
            @Override
            public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {
                rootObject = response.body();
                if (rootObject.getStatus().equals("success")) {

                    msg_et.setText("");
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(msg_et.getWindowToken(), 0);
                    getPostDetailApi();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<WallPojo> call, Throwable t) {

            }
        });
    }

    private void commentReplyApi() {
        progressDialog.show();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("postid", postId);
        hm.put("commenttext", msg_et.getText().toString());
        hm.put("parent_id", postCommentId);
        hm.put("top_level_username", topLevelUsername);
        hm.put("top_level_userid", topLevelUserId);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<WallPojo> call = service.commentApi("postcomments", hm);
        call.enqueue(new Callback<WallPojo>() {
            @Override
            public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {
                rootObject = response.body();
                if (rootObject.getStatus().equals("success")) {
                    send_imageview.setVisibility(View.VISIBLE);
                    send_reply_imageview.setVisibility(View.GONE);
                    ll_reply_comment_name.setVisibility(View.GONE);
                    msg_et.setText("");
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(msg_et.getWindowToken(), 0);
                    commentListApi();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<WallPojo> call, Throwable t) {

            }
        });
    }

    private void emojiApi(HashMap<String, Object> hm) {
        //Shubham Upadte
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<EmojiPojo> call = service.emojiApi("userpostlikes", hm);
        call.enqueue(new Callback<EmojiPojo>() {
            @Override
            public void onResponse(Call<EmojiPojo> call, Response<EmojiPojo> response) {

                assert response.body() != null;
                if (response.body().getStatus().equals("success")) {
                    getPostDetailApi();
                }
            }

            @Override
            public void onFailure(Call<EmojiPojo> call, Throwable t) {
                Toast.makeText(CommentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });

    }

    public void favApi(String type, HashMap<String, Object> hm) {
        progressDialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<WallPojo> call = service.favApi(type, hm);
        call.enqueue(new Callback<WallPojo>() {
            @Override
            public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {
                if (response.body().getStatus().equals("success")) {
                    getPostDetailApi();
                    Toast.makeText(CommentActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<WallPojo> call, Throwable t) {

            }
        });
    }

    private void commentDeleteApi(String commentId, String postId, int position, int commentPosition, String type) {
        progressDialog.show();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("comment_id", commentId);
        hm.put("postid", postId);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<CommentPojo> call = service.commentDeleteApi("deletecomment", hm);
        call.enqueue(new Callback<CommentPojo>() {
            @Override
            public void onResponse(Call<CommentPojo> call, Response<CommentPojo> response) {
                commentPojo = response.body();
                if (commentPojo.getStatus().equals("success")) {
                    if (type.equals("reply")){
                        recreate();
                    } else {
                        adapter.removeComment(position, commentPosition, type);
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CommentPojo> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(CommentActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back_btn:
                onBackPressed();
                break;

            case R.id.send_imageview:
                if (CheckAllFields()) {
                    progressDialog.show();
                    commentApi();
                }
                break;

            case R.id.send_reply_imageview:
                if (CheckAllFields()) {
                    progressDialog.show();
                    commentReplyApi();
                }
                break;
        }
    }

    @Override
    public void onReplyButtonClick(String userId, String username, String pcId) {
        postCommentId = pcId;
        topLevelUsername = username;
        topLevelUserId = userId;
        msg_et.requestFocus();
        showKeyboard(msg_et);
        send_imageview.setVisibility(View.GONE);
        send_reply_imageview.setVisibility(View.VISIBLE);
        ll_reply_comment_name.setVisibility(View.VISIBLE);
        txtv_reply_comment_name.setText(username);
//        Toast.makeText(this, "Username: " + username, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCommentLikeListClick(String pcId) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("commentid", pcId);
        getCommentLikeList(hm);
    }

    @Override
    public void onCommentReplyDelete(String commentId, String postId, int position, int commentPosition) {
        commentDeleteApi(commentId, postId, position, commentPosition, "reply");
    }

    @Override
    public void onCommentDelete(String commentId, String postId, int position) {
        commentDeleteApi(commentId, postId, position, 0, "comment");
    }

    @Override
    public void onReplyLikeButtonClick(String postId, String pcId, String likeStatus, int pos) {
        progressDialog.show();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("postid", postId);
        hm.put("commentid", pcId);
        hm.put("like_status", likeStatus);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<CommentLikePojo> call = service.commentLikeApi("likecomment", hm);
        call.enqueue(new Callback<CommentLikePojo>() {
            @Override
            public void onResponse(Call<CommentLikePojo> call, Response<CommentLikePojo> response) {
                commentLikePojo = response.body();
                if (commentLikePojo.getStatus().equals("success")) {

                    adapter.updateCommentLikeItem(pos, commentLikePojo.getTotal_likes(), commentLikePojo.getUser_like_status(), "reply");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CommentLikePojo> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCommentLikeButtonClick(String postId, String pcId, String likeStatus, int pos) {
        progressDialog.show();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("postid", postId);
        hm.put("commentid", pcId);
        hm.put("like_status", likeStatus);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<CommentLikePojo> call = service.commentLikeApi("likecomment", hm);
        call.enqueue(new Callback<CommentLikePojo>() {
            @Override
            public void onResponse(Call<CommentLikePojo> call, Response<CommentLikePojo> response) {
                commentLikePojo = response.body();
                if (commentLikePojo.getStatus().equals("success")) {

                    adapter.updateCommentLikeItem(pos, commentLikePojo.getTotal_likes(), commentLikePojo.getUser_like_status(), "comment");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CommentLikePojo> call, Throwable t) {

            }
        });
    }

    @Override
    public void onOtherUserClick(int userId) {
        Intent i = new Intent(CommentActivity.this, MyProfileOtherUser.class);
        i.putExtra("user_id", userId);
        startActivity(i);
    }

    private void showKeyboard(EditText editText) {
        editText.post(() -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    private boolean CheckAllFields() {
        String message = msg_et.getText().toString().trim();
        if (message.isEmpty()) {
            return GlobalMethods.setError(msg_et, "Please enter comment");
        } else if (BadWordFilter.containsBadWord(message)) {
            GlobalMethods.getInstance(CommentActivity.this)
                    .globalDialogAbusiveWord(CommentActivity.this, getString(R.string.abusive_msg));
            return false;
        }
        return true;
    }

    private void getPostEmojiList(HashMap<String, Object> hm) {
        //Shubham Upadte
        progressDialog.show();
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if (isInternetConnection) {
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<PostEmojiListModel> call = service.postEmojiListApi("userpostemojilist", hm);
            call.enqueue(new Callback<PostEmojiListModel>() {
                @Override
                public void onResponse(Call<PostEmojiListModel> call, Response<PostEmojiListModel> response) {

                    postEmojiListModel = response.body();

                    FavoriteBottomSheetFragment favoriteBottomSheetFragment = new FavoriteBottomSheetFragment(postEmojiListModel.getEmojilistdata());
                    favoriteBottomSheetFragment.show(CommentActivity.this.getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<PostEmojiListModel> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        } else {
            GlobalMethods.getInstance(this).globalDialog(this, "     No internet connection.");
        }
    }

    private void getCommentLikeList(HashMap<String, Object> hm) {
        //Shubham Upadte
        progressDialog.show();
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if (isInternetConnection) {
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<PostEmojiListModel> call = service.postCommentLikeListApi("likecommentlist", hm);
            call.enqueue(new Callback<PostEmojiListModel>() {
                @Override
                public void onResponse(Call<PostEmojiListModel> call, Response<PostEmojiListModel> response) {

                    postEmojiListModel = response.body();

                    CommentLikeBottomSheetFragment commentLikeBottomSheetFragment = new CommentLikeBottomSheetFragment(postEmojiListModel.getLike_list());
                    commentLikeBottomSheetFragment.show(CommentActivity.this.getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<PostEmojiListModel> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        } else {
            GlobalMethods.getInstance(this).globalDialog(this, "     No internet connection.");
        }
    }

    @Override
    public void onEmojiClick(List<Emojilistdatum> pos) {
        //Shubham Upadte
        EmojiBottomSheetFragment bottomSheetDialog = new EmojiBottomSheetFragment(pos);
        bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    @Override
    public void onImageClick(List<ImagePojo> list, int pos) {
        //Shubham Upadte
        imageDialog(list, pos);
    }

    private void imageDialog(List<ImagePojo> img, int pos) {
        //Shubham Upadte
        RecyclerView rv;
        ImageView cancel;
        image_dialog = new Dialog(this);
        image_dialog.setContentView(R.layout.open_image_dialog);
        rv = image_dialog.findViewById(R.id.rv_image);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        rv.setLayoutManager(new LinearLayoutManager(CommentActivity.this, LinearLayoutManager.HORIZONTAL, false));
        MediaSliderAdapter mediaSliderAdapter = new MediaSliderAdapter(image_dialog, img, "detail", pos, CommentActivity.this);
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

        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(CommentActivity.this, android.R.color.black)));
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

    private void setReactionClickListener(ImageView reaction, final String reactionCode) {
        reaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reactions_card.setVisibility(View.GONE);
                reactionsVisible = false;
                hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                hm.put("postid", postId);
                hm.put("likestatus", "1");
                hm.put("emojiunicode", reactionCode);
                emojiApi(hm);
            }
        });
    }

    private void downloadAndShareImage(String imageUrl, int pos) {
        if (imageUrl == null || imageUrl.isEmpty() || imageUrl.contains("video")) {
            shareText();
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

                shareImage(imageFile);
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error downloading image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void shareImage(File imageFile) {
        try {
            Uri imageUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", imageFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Neighbrsnook post");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this post on Neighbrsnook,\n" +
                    "\n" + strPostType +
                    "\n" + strPostMsg +
                    "\n" + strNeighbourhood +
                    "\n\nhttps://neighbrsnook.com/open-app" +
                    "\n\nhttps://neighbrsnook.com/");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            runOnUiThread(() -> startActivity(Intent.createChooser(shareIntent, "Share via")));
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(this, "Error sharing image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void shareText() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Neighbrsnook post");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this post on Neighbrsnook,\n" +
                    "\n" + strPostType +
                    "\n" + strPostMsg +
                    "\n" + strNeighbourhood +
                    "\n\nhttps://neighbrsnook.com/open-app" +
                    "\n\nhttps://neighbrsnook.com/");
            runOnUiThread(() -> startActivity(Intent.createChooser(shareIntent, "Share via")));
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(this, "Error sharing text: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}