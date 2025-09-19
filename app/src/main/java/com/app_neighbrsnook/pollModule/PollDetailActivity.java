package com.app_neighbrsnook.pollModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.profile.MyProfileOtherUser;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.VoteAdapter;
import com.app_neighbrsnook.model.pollDetailResponce.Option;
import com.app_neighbrsnook.model.pollDetailResponce.PollDetailPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollDetailActivity extends AppCompatActivity implements VoteAdapter.NewRequest {

    RecyclerView vote_rv;
    List<Option> opt = new ArrayList<>();
    ImageView back_btn, profile_imageview, search_imageview, add_imageview;
    TextView title, vote_textview, poll_title_value_tv, start_date_value_tv, end_date_value_tv, question_tv, created_date_tv, user_tv, vote_count_tv, noActionBtn;
    String option, pollStartDate, pollclose, willPollStarted, pollId, editPollStatus;
    int poll_id;
    Dialog dialog;
    SharedPrefsManager sm;
    LinearLayout profile_ll;
    ProgressDialog progressDialog;
    FrameLayout deletePage, editDetails;
    int user_id;
    Boolean isConditionTrue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_detail);

        sm = new SharedPrefsManager(this);
        Intent i = getIntent();
        poll_id = getIntent().getExtras().getInt("id");
        pollclose = getIntent().getExtras().getString("pollclosed");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        pollDetilApi();

        vote_rv = findViewById(R.id.vote_rv);
        add_imageview = findViewById(R.id.add_imageview);
        deletePage = findViewById(R.id.deletePage);
        editDetails = findViewById(R.id.editDetails);
        title = findViewById(R.id.title);
        back_btn = findViewById(R.id.back_btn);
        vote_textview = findViewById(R.id.vote_textview);
        poll_title_value_tv = findViewById(R.id.poll_title_value_tv);
        created_date_tv = findViewById(R.id.created_date_tv);
        end_date_value_tv = findViewById(R.id.end_date_value_tv);
        question_tv = findViewById(R.id.question_tv);
        user_tv = findViewById(R.id.user_tv);
        vote_count_tv = findViewById(R.id.vote_count_tv);
        profile_imageview = findViewById(R.id.profile_imageview);
        start_date_value_tv = findViewById(R.id.start_date_value_tv);
        noActionBtn = findViewById(R.id.noActionBtn);
        profile_ll = findViewById(R.id.profile_ll);
        search_imageview = findViewById(R.id.search_imageview);
        search_imageview.setVisibility(View.GONE);
        add_imageview.setVisibility(View.GONE);
        Drawable backgroundDrawable = noActionBtn.getBackground();
        if (backgroundDrawable != null) {
            backgroundDrawable.setColorFilter(getResources().getColor(R.color.text_color), PorterDuff.Mode.SRC_IN);
        }

        title.setText("Poll Details");
        pollPercentageApi();
        vote_rv.setLayoutManager(new LinearLayoutManager(this));

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        vote_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if (willPollStarted.equals("0")) {
                    progressDialog.dismiss();
                    GlobalMethods.getInstance(PollDetailActivity.this).globalDialog(PollDetailActivity.this, "   Polling starts on " + pollStartDate + "   ");
                } else {
                    progressDialog.show();
                    pollVote();
                }
            }
        });
        profile_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PollDetailActivity.this, MyProfileOtherUser.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });
        deletePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(PollDetailActivity.this);
                dialog.setContentView(R.layout.delete_event_layout);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(PollDetailActivity.this, android.R.color.transparent)));
                dialog.getWindow().setAttributes(lp);
                dialog.show();
                ImageView img = dialog.findViewById(R.id.iv9);
                TextView tvEvent = dialog.findViewById(R.id.productDlt);
                TextView tv_yes = dialog.findViewById(R.id.tv_yes_id);
                TextView tv_no = dialog.findViewById(R.id.tv_no);
                tvEvent.setText("poll ?");
                tv_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, Object> hm = new HashMap<>();
                        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                        hm.put("pollid", poll_id);
                        apiDeletePoll(hm);
                    }
                });
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPollStatus.equals("0")){
                    Intent createBusinessIntent = new Intent(PollDetailActivity.this, CreatePollActivity.class);
                    createBusinessIntent.putExtra("from", "PollEdit");
                    createBusinessIntent.putExtra("pollId", pollId);
                    startActivity(createBusinessIntent);
                } else {
                    GlobalMethods.getInstance(PollDetailActivity.this).globalDialog(PollDetailActivity.this, "Poll can’t be edited once voting has started. You may delete it.");
                }

            }
        });
    }

    private void pollPercentageApi() {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("pollid", poll_id);
    }

    private void pollDetilApi() {
        progressDialog.show();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("poll_id", poll_id);
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<PollDetailPojo> call = service.pollLDetail("polldetail", hm);
        call.enqueue(new Callback<PollDetailPojo>() {
            @Override
            public void onResponse(Call<PollDetailPojo> call, Response<PollDetailPojo> response) {
                PollDetailPojo rootObject = response.body();
                try {
                    poll_title_value_tv.setText(rootObject.getTitle());
                    end_date_value_tv.setText(rootObject.getEndDate());
                    question_tv.setText(rootObject.getPollQues());
                    created_date_tv.setText(rootObject.getNeighborhood() + ", " + rootObject.getCreatedDate());
                    user_tv.setText(rootObject.getCreatedBy());
                    vote_count_tv.setText(rootObject.getTotal());
                    start_date_value_tv.setText(rootObject.getStartDate());
                    pollStartDate = rootObject.getStartDate();
                    opt = rootObject.getOptions();
                    user_id = Integer.parseInt(rootObject.getUserid());
                    pollId = rootObject.getpId();
                    editPollStatus = rootObject.getEdit_poll_status();

                    vote_rv.setAdapter(new VoteAdapter(opt, rootObject.getIsvoted(), PollDetailActivity.this));

                    switch (rootObject.getPollRunningStatus()) {
                        case "0":
                            willPollStarted = "0";
                            break;

                        case "1":
                            if (rootObject.getIsvoted().equals("0") && (rootObject.getIsvoted().equals("0"))) {
                                vote_textview.setVisibility(View.VISIBLE);
                                noActionBtn.setVisibility(View.GONE);
                                willPollStarted = "1";
                            } else {
                                vote_textview.setVisibility(View.GONE);
                                noActionBtn.setVisibility(View.VISIBLE);
                            }
                            break;

                        case "2":
                            vote_textview.setVisibility(View.GONE);
                            noActionBtn.setVisibility(View.VISIBLE);
                            break;
                    }

                } catch (Exception e) {

                }

                if (rootObject.getUserid().equals(sm.getString("user_id"))) {
                    deletePage.setVisibility(View.VISIBLE);
                    editDetails.setVisibility(View.VISIBLE);
                } else {
                    deletePage.setVisibility(View.GONE);
                    editDetails.setVisibility(View.GONE);
                }
                if (rootObject.getUserpic().isEmpty()) {
                    profile_imageview.setImageResource(R.drawable.profile);
                } else {
                    Picasso.get().load(rootObject.getUserpic()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile).into(profile_imageview);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PollDetailPojo> call, Throwable t) {
                Toast.makeText(PollDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void popupDialog() {
        TextView confirm, msg_textview;
        ImageView cancel;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_poll);

        confirm = dialog.findViewById(R.id.confirm_textview);
        cancel = dialog.findViewById(R.id.cross_imageview);
        msg_textview = dialog.findViewById(R.id.msg_textview);
        msg_textview.setText("   Polling starts on " + pollStartDate + "   ");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(PollDetailActivity.this, android.R.color.transparent)));
        dialog.setCancelable(true);
        dialog.show();
    }

    private void pollVote() {
        progressDialog.show();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("pollid", poll_id);
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("polloption", option);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.pollLVote("pollvoted", hm);
        Log.d("obj", hm.toString());
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                JsonObject obj = response.body().getAsJsonObject();
                Log.d("obj", obj.toString());
                if (obj.get("status").getAsString().equals("success")) {
                    isConditionTrue = true;
                    vote_textview.setVisibility(View.GONE);
                    noActionBtn.setVisibility(View.VISIBLE);
                    pollDetilApi();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(PollDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Log.d("res", t.getMessage());
            }
        });
    }

    @Override
    public void onOptionClick(String option) {
        this.option = option;
        Log.d("option", option);
    }

    private void apiDeletePoll(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.deletePoll("deletepoll", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Intent i = new Intent(PollDetailActivity.this, PollActivity.class);
                i.putExtra("neighbrhood", "drawar");
                startActivity(i);
                onBackPressed();
                Toast.makeText(PollDetailActivity.this, "Poll deleted successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(PollDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isConditionTrue) {
            Intent i = new Intent(PollDetailActivity.this, MainActivity.class);
            startActivity(i);
        } else {
            super.onBackPressed();
        }
    }
}