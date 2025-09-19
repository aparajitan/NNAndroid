package com.app_neighbrsnook;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.adapter.ImageSliderAdapter1;
import com.app_neighbrsnook.adapter.PostAdapter;
import com.app_neighbrsnook.adapter.PostAdapterOther;
import com.app_neighbrsnook.adapter.WallChildAdapter;
import com.app_neighbrsnook.fragment.EmojiBottomSheetFragment;
import com.app_neighbrsnook.model.post.Listdatum;
import com.app_neighbrsnook.model.post.PostPojo;
import com.app_neighbrsnook.model.wall.Emojilistdatum;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.EmojiPojo;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.post.CreatePostActivity;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivityOther extends AppCompatActivity implements PostAdapterOther.PostRequest , WallChildAdapter.ImageCallBack {
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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            source = extras.getString("source");
        }
        Intent i = getIntent();

        fromuserid = i.getIntExtra("user_id",0);

            postListApi();


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
                Intent i = new Intent(PostActivityOther.this, CreatePostActivity.class);
                startActivity(i);
            }
        });
    }

    private void postListApi() {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        hm.put("userid", sm.getString("user_id"));
        if(source.equals("wall"))
        {
            hm.put("postlist", "postlist");
        }else if (source.equals("other")){
            hm.put("userid", fromuserid);

        }else if (source.equals("source")){
            hm.put("userid", fromuserid);

        }
        Call<PostPojo> call = service.PostPojoList("postlist", hm);
        call.enqueue(new Callback<PostPojo>() {
            @Override
            public void onResponse(Call<PostPojo> call, Response<PostPojo> response) {
                listdata = response.body().getListdata();

                String status = response.body().getStatus();
                if (status.equals("success")) {
                    if(source.equals("wall"))
                    {
                        rv_post.setAdapter(new PostAdapterOther(listdata, PostActivityOther.this, "wall"));
                    }
                    else {
                        rv_post.setAdapter(new PostAdapterOther(listdata, PostActivityOther.this, "profile"));
                    }

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
                Toast.makeText(PostActivityOther.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void onClickEmoji(List<Emojilistdatum> pos) {
        EmojiBottomSheetFragment emogiBottomSheetDialog = new EmojiBottomSheetFragment(pos);
        emogiBottomSheetDialog.show(this.getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    @Override
    public void onClickComment(List<Listdatum> mList, int pos) {
        Intent i = new Intent(PostActivityOther.this, CommentActivity.class);
        i.putExtra("pos", pos);
        startActivity(i);
    }

    @Override
    public void onClickShare(int pos) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, listdata.get(pos).getPostType() +"\n"+ listdata.get(pos).getPostMessage());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
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
                Toast.makeText(PostActivityOther.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        rv.setLayoutManager(new LinearLayoutManager(PostActivityOther.this, LinearLayoutManager.HORIZONTAL, false));
        ImageSliderAdapter1 imageUploadAdapter = new ImageSliderAdapter1(image_dialog, img, "detail", pos);
        rv.setAdapter(imageUploadAdapter);
        rv.scrollToPosition(pos);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.cancel();
            }
        });

        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(PostActivityOther.this, android.R.color.transparent)));
        image_dialog.setCancelable(true);
        image_dialog.show();
//        Window window = image_dialog.getWindow();
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    public void confirmDeleteDialog(int pos) {
        Dialog dialog = new Dialog(PostActivityOther.this);
        dialog.setContentView(R.layout.post_delete_pop_up);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(PostActivityOther.this, android.R.color.transparent)));
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

}