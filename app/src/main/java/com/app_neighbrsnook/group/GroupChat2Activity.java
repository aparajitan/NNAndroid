package com.app_neighbrsnook.group;

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
import com.app_neighbrsnook.adapter.GroupChatAdapter;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.intreface.BuyCallBack;
import com.app_neighbrsnook.model.ChatModel;
import com.app_neighbrsnook.model.SellerChatModel;
import com.app_neighbrsnook.model.groupChat.Datum;
import com.app_neighbrsnook.model.groupChat.GroupChatPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.dm.Nbdatum;
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

public class GroupChat2Activity extends AppCompatActivity implements View.OnClickListener, BuyCallBack{
    ImageView searchImageView, addImageView, back_btn, send_msg_iv, cancel_iv;
    CircleImageView profile_imageview;
    TextView item_price, accept_btn, subject_input;
    EditText bid_amount_et, msg_et;
    RecyclerView chat_rv,recyclerView;
    TextView titleTv, rupee_tv, pay_tv, free_item_tv, buy_textview;
    List<Datum> list = new ArrayList<>();
    List<Nbdatum> listOld = new ArrayList<>();
    DAO dao;
    RelativeLayout chat_rv_rl, search_rl;
    LinearLayout chat_et_ll, pay_or_negotiate_ll;
    NestedScrollView nestedScrollView;
    GroupChatAdapter sellerChatAdapter;
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
    ImageView search_imageview;
    LinearLayout lnrGone;

    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat2);
        dao = Database.createDBInstance(GroupChat2Activity.this).getDao();
        sm = new SharedPrefsManager(this);
        i = getIntent();
        hm.put("groupid", Integer.parseInt(i.getStringExtra("groupid")));
        hm.put("userid",Integer.parseInt(sm.getString("user_id")));
        showMsg(hm);
        apiRunnable.run();
        titleTv = findViewById(R.id.title);
        titleTv.setText(i.getStringExtra("groupName"));
        send_msg_iv = findViewById(R.id.send_msg_iv);
        back_btn = findViewById(R.id.back_btn);
        lnrGone = findViewById(R.id.lnrGone);
        search_imageview = findViewById(R.id.search_imageview);
        back_btn.setVisibility(View.VISIBLE);
        search_imageview.setVisibility(View.GONE);
        lnrGone.setVisibility(View.GONE);
        back_btn.setOnClickListener(this);
        profile_imageview = findViewById(R.id.profile_imageview);
        try {
        Picasso.get().load(i.getStringExtra("pic")).into(profile_imageview);
        }catch (Exception e)
        {

        }
        buy_textview = findViewById(R.id.buy_textview);
        searchImageView = findViewById(R.id.search_imageview);
        addImageView = findViewById(R.id.add_imageview);
        searchImageView.setVisibility(View.VISIBLE);
        addImageView.setVisibility(View.GONE);
        msg_et = findViewById(R.id.item_input);
        chat_rv = findViewById(R.id.chat_rv);
        search_et = findViewById(R.id.search_et);
        cancel_iv = findViewById(R.id.cancel_iv);
        search_rl = findViewById(R.id.search_rl);
//        subject_input = findViewById(R.id.subject_input);
//        if(subject.equals(""))
//        {
//            subject_input.setText(" ");
//        }else
//        {
//            subject_input.setText(subject);
//        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        chat_rv.setLayoutManager(linearLayoutManager);
        send_msg_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = msg_et.getText().toString();
                if(text != null && !text.isEmpty()) {
                    sendMsgApi(msg_et.getText().toString());
                }
                else {

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
        HashMap<String, Object> hm =  new HashMap<>();
        hm.put("groupid", Integer.parseInt(i.getStringExtra("groupid")));
        hm.put("userid",Integer.parseInt(sm.getString("user_id")));
        hm.put("message",msg);

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<GroupChatPojo> call = service.sendGroupMsgApi("groupchat", hm);
        call.enqueue(new Callback<GroupChatPojo>() {
            @Override
            public void onResponse(Call<GroupChatPojo> call, Response<GroupChatPojo> response) {

                String msg = response.body().getMessage();

                    String status = response.body().getStatus();
                    msg_et.setText("");
                hm.put("groupid", Integer.parseInt(i.getStringExtra("groupid")));
                hm.put("userid",Integer.parseInt(sm.getString("user_id")));
                showMsg(hm);


            }

            @Override
            public void onFailure(Call<GroupChatPojo> call, Throwable t) {
                Toast.makeText(GroupChat2Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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




    private void showMsg(HashMap<String,  Object> hm) {

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<GroupChatPojo> call = service.showGroupChatApi("showgroupchat", hm);
        call.enqueue(new Callback<GroupChatPojo>() {
            @Override
            public void onResponse(Call<GroupChatPojo> call, Response<GroupChatPojo> response) {

                String status = response.body().getStatus();
                if(status.equals("success")) {
                    GroupChatPojo GroupChatPojo = new GroupChatPojo();
                    list = response.body().getData();
                    Collections.reverse(list);
                    sellerChatAdapter = new GroupChatAdapter(list);
                    chat_rv.setAdapter(sellerChatAdapter);
                    sellerChatAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<GroupChatPojo> call, Throwable t) {
                Toast.makeText(GroupChat2Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
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
    } ;

    

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

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(GroupChat2Activity.this, android.R.color.transparent)));
        dialog.setCancelable(false);
        dialog.show();
    }


    private void filter(String text) {
        ArrayList<Datum> filteredlist = new ArrayList<Datum>();
        for (Datum item : list) {
            if ((item.getMessage().toLowerCase().contains(text.toLowerCase()))) {
                filteredlist.add(item);
            }
        }


        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            sellerChatAdapter.filterList(filteredlist);
        }
    }

}