package com.app_neighbrsnook.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.ImageSliderAdapter1;
import com.app_neighbrsnook.adapter.PostAdapter;
import com.app_neighbrsnook.adapter.WallChildAdapter;
import com.app_neighbrsnook.fragment.EmojiBottomSheetFragment;
import com.app_neighbrsnook.fragment.FavoriteBottomSheetFragment;
import com.app_neighbrsnook.model.post.Listdatum;
import com.app_neighbrsnook.model.post.PostPojo;
import com.app_neighbrsnook.model.wall.Emojilistdatum;
import com.app_neighbrsnook.model.wall.PostEmojiListModel;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.EmojiPojo;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.post.CreatePostActivity;

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

public class PostActivityMyNeighbrhood extends AppCompatActivity implements PostAdapter.PostRequest , WallChildAdapter.ImageCallBack {
    RecyclerView rv_post;
    SharedPrefsManager sm;
    HashMap<String, Object> hm = new HashMap<>();
    List<Listdatum> listdata = new ArrayList<>();
    ImageView searchImageView, addImageView, back_btn;
    TextView titleTv;
    Dialog image_dialog;
    String source;
    WallChildAdapter.ImageCallBack imageCallBack = this;
    int fromuserid;
    String nbr;
    ProgressDialog dialog;
    PostEmojiListModel postEmojiListModel;
    boolean isVerifiedUser =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        sm = new SharedPrefsManager(this);
        rv_post = findViewById(R.id.rv_post);
        searchImageView = findViewById(R.id.search_imageview);
        searchImageView.setVisibility(View.GONE);
        addImageView = findViewById(R.id.add_imageview);
        addImageView.setVisibility(View.GONE);
        back_btn = findViewById(R.id.back_btn);
        rv_post.setLayoutManager(new LinearLayoutManager(this));
        titleTv = findViewById(R.id.title);
        titleTv.setText("Post");
        Bundle intent = getIntent().getExtras();
        nbr = intent.getString("neighbrhood");
        Bundle extras = getIntent().getExtras();
        if (intent != null) {
            source = extras.getString("neighbrhood");
        }
            postListApi();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PostActivityMyNeighbrhood.this, CreatePostActivity.class);
                startActivity(i);
            }
        });
        Log.d("ssfsfsfs",nbr);
    }

    private void postListApi() {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        hm.put("userid", sm.getString("user_id"));
        if (nbr!=null) {
            if (nbr.equals(sm.getString("neighbrhood"))) {
                hm.put("neighbrhood", sm.getString("neighbrhood"));
            }
        }
        Call<PostPojo> call = service.PostPojoList("postlist", hm);
        call.enqueue(new Callback<PostPojo>() {
            @Override
            public void onResponse(Call<PostPojo> call, Response<PostPojo> response) {
                listdata = response.body().getListdata();

                String status = response.body().getStatus();
                if (response.body().getVerfiedMsg().equals("User Verification is not completed!")) {
                    isVerifiedUser = false;
                }
                if (status.equals("success")) {
                        rv_post.setAdapter(new PostAdapter(listdata, PostActivityMyNeighbrhood.this, "wall",PostActivityMyNeighbrhood.this,isVerifiedUser));

                }

            }

            @Override
            public void onFailure(Call<PostPojo> call, Throwable t) {
//                    Toast.makeText(PostPojoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });


    }

    @Override
    public void onDeletePost(int pos) {
        confirmDeleteDialog(pos);
    }

    @Override
    public void threeDot(int pos,int postId) {

    }

    private void deletePostApi(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<EmojiPojo> call = service.deletePostApi("deletepostbyuser", hm);
        call.enqueue(new Callback<EmojiPojo>() {
            @Override
            public void onResponse(Call<EmojiPojo> call, Response<EmojiPojo> response) {

                if(response.body().getMessage().equals("success"))
                {
                    postListApi();
                    Log.d("response emoji---", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<EmojiPojo> call, Throwable t) {
                Toast.makeText(PostActivityMyNeighbrhood.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }

    @Override
    public void onSetEmoji(int pos, String postlike) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid",Integer.parseInt(sm.getString("user_id")));
        hm.put("postid", pos);
        hm.put("likestatus", postlike);
        emojiApi(hm);

    }

    @Override
    public void onReportSelect(List<Listdatum> mList, int pos) {

    }

    @Override
    public void onClickEmoji(List<Emojilistdatum> pos) {
        EmojiBottomSheetFragment emogiBottomSheetDialog = new EmojiBottomSheetFragment(pos);
        emogiBottomSheetDialog.show(this.getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    @Override
    public void onClickComment(List<Listdatum> mList, String postId, int pos) {
      /* 20 march 24 rgn  postlist crash Intent i = new Intent(PostActivity.this, CommentActivity.class);
        i.putExtra("pos", pos);
        startActivity(i);*/
    }

    @Override
    public void onClickShare(int pos) {
        String imageUrl = listdata.get(pos).getPostImages().get(0).getImg();
        downloadAndShareImage(imageUrl, pos);
    }


    private void emojiApi(HashMap<String, Object> hm) {

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<EmojiPojo> call = service.emojiApi("userpostlikes", hm);
        call.enqueue(new Callback<EmojiPojo>() {
            @Override
            public void onResponse(Call<EmojiPojo> call, Response<EmojiPojo> response) {
                postListApi();
                if(response.body().getMessage().equals("success"))
                {
//                    postListApi();
                    Log.d("response emoji---", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<EmojiPojo> call, Throwable t) {
                Toast.makeText(PostActivityMyNeighbrhood.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });

    }

    @Override
    public void onImageClick(List<ImagePojo> list, int pos) {
        imageDialog(list, pos);
    }
   /* public void threeDot(int pos) {
//        BottomSheetPollFragment bottomSheetDialog = new BottomSheetPollFragment(listdata, pos, "poll");
        BottomSheetPollFragment bottomSheetPoll = new BottomSheetPollFragment(
                listdata.get(pos).getFavouritstatus(),
                listdata.get(pos).getCreatedby(),
                listdata.get(pos).getNeighborhood(),
               // listdata.get(pos).getPollQues(),
               // listdata.get(pos).getPostid()

                "poll");
        bottomSheetPoll.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment1");

//        BottomSheetFragment bottomSheetBusiness = new BottomSheetFragment(businessList, pos, "business");
//        bottomSheetBusiness.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

    }

    */

    private void imageDialog(List<ImagePojo> img, int pos) {
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
//        if(type.equals("single"))
//        {
//           card.setVisibility(View.VISIBLE);
//            iv_uploaded_image.
//
//        }
        rv.setLayoutManager(new LinearLayoutManager(PostActivityMyNeighbrhood.this, LinearLayoutManager.HORIZONTAL, false));
        ImageSliderAdapter1 imageUploadAdapter = new ImageSliderAdapter1(image_dialog, img, "detail", pos);
        rv.setAdapter(imageUploadAdapter);
        rv.scrollToPosition(pos);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.cancel();
            }
        });

        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(PostActivityMyNeighbrhood.this, android.R.color.transparent)));
        image_dialog.setCancelable(true);
        image_dialog.show();
//        Window window = image_dialog.getWindow();
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    public void confirmDeleteDialog(int pos) {
        Dialog dialog = new Dialog(PostActivityMyNeighbrhood.this);
        dialog.setContentView(R.layout.post_delete_pop_up);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(PostActivityMyNeighbrhood.this, android.R.color.transparent)));
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
                hm.put("userid",Integer.parseInt(sm.getString("user_id")));
                hm.put("postid", pos);
                deletePostApi(hm);
                postListApi();
            }
        });

    }
    @Override
    public void onSetEmoji1(int pos, String likepost, String reactionCode, int likePos) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("postid", pos);
        hm.put("likestatus", likepost);
        hm.put("emojiunicode", reactionCode);
        emojiApi(hm);
    }
    @Override
    public void onClickEmoji1(int postid) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("postid", postid);
        getPostEmojiList(hm);
}

    @Override
    public void onDmSelect(List<Listdatum> mList, int pos) {

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
                    favoriteBottomSheetFragment.show(PostActivityMyNeighbrhood.this.getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

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
    public void onFavSelect(int postid, String neighbourhood, int favoriteStatus, int postPos){

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

    }
}