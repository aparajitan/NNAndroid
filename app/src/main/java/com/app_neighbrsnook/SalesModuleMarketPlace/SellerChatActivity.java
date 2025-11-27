package com.app_neighbrsnook.SalesModuleMarketPlace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.SellerChatAdapter;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.intreface.BuyCallBack;
import com.app_neighbrsnook.model.ChatModel;
import com.app_neighbrsnook.model.SellerChatModel;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.dm.DmModel;
import com.app_neighbrsnook.pojo.dm.Nbdatum;
import com.app_neighbrsnook.profile.MyProfileOtherUser;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerChatActivity extends AppCompatActivity implements View.OnClickListener, BuyCallBack, SellerChatAdapter.OnDeleteMesaageClickListener {
    ImageView searchImageView, addImageView, back_btn, send_msg_iv, cancel_iv;
    CircleImageView profile_imageview;
    TextView item_price, accept_btn, subject_input;
    EditText bid_amount_et, msg_et;
    RecyclerView chat_rv, recyclerView;
    TextView titleTv, rupee_tv, pay_tv, free_item_tv, buy_textview;
    List<Nbdatum> list = new ArrayList<>();
    List<Nbdatum> listOld = new ArrayList<>();
    DAO dao;
    RelativeLayout chat_rv_rl, search_rl;
    LinearLayout chat_et_ll, pay_or_negotiate_ll;
    NestedScrollView nestedScrollView;
    SellerChatAdapter sellerChatAdapter;
    String type;
    int fromuserid;
    int touserid;
    BuyCallBack bcb = this;
    HashMap<String, Object> hm = new HashMap<>();
    Handler handler = new Handler();
    SharedPrefsManager sm;
    String subject;
    Context context;
    Dialog dialog;
    EditText search_et;
    LinearLayout profile_ll;
    DmModel dmModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_chat);
        dao = Database.createDBInstance(SellerChatActivity.this).getDao();
        sm = new SharedPrefsManager(this);
        Intent i = getIntent();
        fromuserid = Integer.parseInt(sm.getString("user_id"));
        subject = i.getStringExtra("subject");
        touserid = i.getIntExtra("eventId", 0);

        hm.put("fromuserid", fromuserid);
        hm.put("touserid", touserid);
        showMsg(hm);
        apiRunnable.run();
        titleTv = findViewById(R.id.title);
        titleTv.setText(i.getStringExtra("userName"));
        send_msg_iv = findViewById(R.id.send_msg_iv);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setVisibility(View.VISIBLE);
        back_btn.setOnClickListener(this);
        profile_imageview = findViewById(R.id.profile_imageview);
        Picasso.get().load(i.getStringExtra("pic")).into(profile_imageview);
        buy_textview = findViewById(R.id.buy_textview);
        profile_ll = findViewById(R.id.profile_ll);
        searchImageView = findViewById(R.id.search_imageview);
        addImageView = findViewById(R.id.add_imageview);
        searchImageView.setVisibility(View.VISIBLE);
        addImageView.setVisibility(View.GONE);
        msg_et = findViewById(R.id.item_input);
        chat_rv = findViewById(R.id.chat_rv);
        search_et = findViewById(R.id.search_et);
        cancel_iv = findViewById(R.id.cancel_iv);
        search_rl = findViewById(R.id.search_rl);
        subject_input = findViewById(R.id.subject_input);
        if (subject.equals("")) {
            subject_input.setText(" ");
        } else {
            subject_input.setText(subject);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        chat_rv.setLayoutManager(linearLayoutManager);
        send_msg_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (msg_et.getText().toString().equals("")) {

                } else {
                    sendMsgApi(msg_et.getText().toString());
                }
            }
        });

        profile_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SellerChatActivity.this, MyProfileOtherUser.class);
                i.putExtra("user_id", touserid);
                startActivity(i);
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
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
            }
        });
    }

    private void sendMsgApi(String msg) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("fromuserid", fromuserid);
        if ((subject_input.getText().toString()).equals(" ")) {
            hm.put("subject", " ");
        } else {
            hm.put("subject", subject_input.getText().toString());
        }
        hm.put("touserid", touserid);
        hm.put("textmessage", msg);

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<DmModel> call = service.sendMsgApi("userdirectmessage", hm);
        call.enqueue(new Callback<DmModel>() {
            @Override
            public void onResponse(Call<DmModel> call, Response<DmModel> response) {

                String msg = response.body().getMessage();
                msg_et.setText("");
                msg_et.setHint("Type a message");
                subject_input.setText("");

                if (msg.equals("Three consecutive messages only until response received.")) {
                    msgDialog(response.body().getMessage());
                } else {
                    String status = response.body().getStatus();
//                    msg_et.setHint("Type a message");
                    hm.put("fromuserid", fromuserid);
                    hm.put("touserid", touserid);
                    showMsg(hm);
                }
            }

            @Override
            public void onFailure(Call<DmModel> call, Throwable t) {
                Toast.makeText(SellerChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_btn) {
            onBackPressed();
        }
    }

    @Override
    public void onBuyClick(int pos) {

    }

    @Override
    public void onsellClick(int pos) {

    }

    @Override
    public void onTemplateClick(String t) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("d MMM, HH:mm");
        Date date = new Date();
        SellerChatModel commentModel = new SellerChatModel();
        commentModel.setComment_msg(t);
        commentModel.setTime(formatter.format(date));
        commentModel.setSender_or_reciever(ChatModel.SENDER);
    }


    private void showMsg(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<DmModel> call = service.showChatApi("showdirectmessage", hm);
        call.enqueue(new Callback<DmModel>() {
            @Override
            public void onResponse(Call<DmModel> call, Response<DmModel> response) {
                String status = response.body().getStatus();
                if (status.equals("success")) {
                    DmModel dmModel = new DmModel();
                    list = response.body().getNbdata();
                    Collections.reverse(list);
                    sellerChatAdapter = new SellerChatAdapter(list, SellerChatActivity.this);
                    chat_rv.setAdapter(sellerChatAdapter);
                    sellerChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<DmModel> call, Throwable t) {
                //Toast.makeText(SellerChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
              //  Log.d("res", t.getMessage());
            }
        });

    }

    private final Runnable apiRunnable = new Runnable() {
        @Override
        public void run() {
            hm.put("fromuserid", fromuserid);
            hm.put("touserid", touserid);
            showMsg(hm);

            handler.postDelayed(this, 10000);
        }
    };

    private void msgDialog(String str) {
        RecyclerView rv;
        TextView confirm, msg_textview;
        ImageView cancel;
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.msg_dialog);
        cancel = dialog.findViewById(R.id.cross_imageview);
        msg_textview = dialog.findViewById(R.id.msg_textview);
        msg_textview.setText(str);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SellerChatActivity.this, android.R.color.transparent)));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void filter(String text) {
        ArrayList<Nbdatum> filteredlist = new ArrayList<Nbdatum>();
        for (Nbdatum item : list) {
            if ((item.getMessage().toLowerCase().contains(text.toLowerCase()) ||
                    item.getSubject().toLowerCase().contains(text.toLowerCase()))) {
                filteredlist.add(item);
            }
        }

        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            sellerChatAdapter.filterList(filteredlist);
        }
    }

    private void commentDeleteApi(String toUserId, String msgId, int position, String deleteType) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", toUserId);
        hm.put("otherUserId", touserid);
        hm.put("msg_id", msgId);
        hm.put("delete_type", deleteType);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<DmModel> call = service.dmDeleteApi("deletemessage", hm);
        call.enqueue(new Callback<DmModel>() {
            @Override
            public void onResponse(Call<DmModel> call, Response<DmModel> response) {
                dmModel = response.body();
                if (dmModel.getStatus().equals("success")) {
                    recreate();
                }
            }

            @Override
            public void onFailure(Call<DmModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCommentDelete(String toUserId, String msgId, int position, String deleteType) {

        commentDeleteApi(toUserId, msgId, position, deleteType);
    }
}