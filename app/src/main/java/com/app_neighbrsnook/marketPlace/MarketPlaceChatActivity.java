package com.app_neighbrsnook.marketPlace;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.MarketPlaceChatListAdapterr;
import com.app_neighbrsnook.adapter.SellerChatAdapter;
import com.app_neighbrsnook.adapter.SellerSenderRcvrChatAdapter;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.apiService.UrlClass;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.intreface.BuyCallBack;
import com.app_neighbrsnook.model.ChatModel;
import com.app_neighbrsnook.model.SellerChatModel;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.dm.DmModel;
import com.app_neighbrsnook.pojo.dm.Nbdatum;
import com.app_neighbrsnook.pojo.marketPlacePojo.ChatMarketPlacePojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.ChatWithSenderRcvr;
import com.app_neighbrsnook.pojo.marketPlacePojo.MarketPlaceSendMsgPojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.ParentChatWithSenderRcvr;
import com.app_neighbrsnook.pojo.marketPlacePojo.UsersListChatsPojo;
import com.app_neighbrsnook.profile.MyProfileOtherUser;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
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

public class MarketPlaceChatActivity extends AppCompatActivity implements View.OnClickListener, BuyCallBack{
    ImageView searchImageView, addImageView, back_btn, send_msg_iv, cancel_iv;
    CircleImageView profile_imageview;
    TextView  subject_input;
    EditText  msg_et;
    RecyclerView chat_rv;
    TextView titleTv, buy_textview;
    DAO dao;
    RelativeLayout  search_rl;

    SellerSenderRcvrChatAdapter sellerChatAdapter;
    int fromuserid;
    List<ChatWithSenderRcvr> list1 = new ArrayList<>();

    int touserid;

    BuyCallBack bcb = this;
    HashMap<String, Object> hm = new HashMap<>();
    Handler handler = new Handler();
    SharedPrefsManager sm;
    String subject;
    Activity activity;
    Context context;
    Dialog dialog;
    EditText search_et;
    LinearLayout profile_ll,subjectId;
    View viewId;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_chat);
        context=activity=this;

        dao = Database.createDBInstance(MarketPlaceChatActivity.this).getDao();
        sm = new SharedPrefsManager(this);
        Log.d("sdfdss", String.valueOf(id));

        Intent i = getIntent();
        fromuserid = Integer.parseInt(sm.getString("user_id"));
        subject = i.getStringExtra("subject");
        touserid = i.getIntExtra("sender_id",0);
      //  to_user_id = i.getIntExtra("to_user_id",0);
        id = i.getIntExtra("id", 0);
        showChatSenderRcvr();
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

        viewId = findViewById(R.id.viewId);
        subjectId = findViewById(R.id.subjectId);
        search_et = findViewById(R.id.search_et);
        cancel_iv = findViewById(R.id.cancel_iv);
        search_rl = findViewById(R.id.search_rl);
        subject_input = findViewById(R.id.subject_input);
        subjectId.setVisibility(View.GONE);
        viewId.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        chat_rv.setLayoutManager(linearLayoutManager);
        send_msg_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(msg_et.getText().toString().equals(""))
                {

                }
                else {
                    sendMsgApi(msg_et.getText().toString());
                }
            }
        });
        profile_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MarketPlaceChatActivity.this, MyProfileOtherUser.class);
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
        HashMap<String, Object> hm =  new HashMap<>();
        hm.put("sender_id", fromuserid);
        hm.put("receiver_id", touserid);
        hm.put("p_id", String.valueOf(id));
        hm.put("message",msg);
        APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
        Call<MarketPlaceSendMsgPojo> call = service.sendMsgMarketPlaceApi(hm);
        call.enqueue(new Callback<MarketPlaceSendMsgPojo>() {
            @Override
            public void onResponse(Call<MarketPlaceSendMsgPojo> call, Response<MarketPlaceSendMsgPojo> response) {
                String msg = response.body().getMessage();
                msg_et.setText("");
                msg_et.setHint("Type a message");
             //   subject_input.setText("");
                if(msg.equals("Message sent successfully."))
                {
                    msgDialog(response.body().getMessage());
                } else {
                   /* String status = response.body().getMessage();
                    hm.put("sender_id", fromuserid);
                    hm.put("receiver_id", touserid);*/
                    showChatSenderRcvr();
                }

            }

            @Override
            public void onFailure(Call<MarketPlaceSendMsgPojo> call, Throwable t) {
                Toast.makeText(MarketPlaceChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
    private void showChatSenderRcvr() {

        String url =
                "";//UrlClass.SELECT_NEIGHBRHOOD+"&lati="+latitude+"&longi="+longitude+"&areas="+address;
    //    Boolean isInternetConnection = GlobalMethods.checkConnection(context);

            HashMap<String, Object> hm =  new HashMap<>();
            // hm.put("p_id", Integer.parseInt(String.valueOf((id))));
            url = UrlClass.CHAT_SENDER_RCVR_MARKET_PLACE+ PrefMananger.GetLoginData(context).getId()+"/"+touserid+"?product_id="+id;
            ApiExecutor.getApiService().getChatSederRcvr(url).enqueue(new Callback<ParentChatWithSenderRcvr>() {
                @Override
                public void onResponse(Call<ParentChatWithSenderRcvr> call, Response<ParentChatWithSenderRcvr> response) {
                        ParentChatWithSenderRcvr dmModel = new ParentChatWithSenderRcvr();
                        list1 = response.body().getMessages();
                        Collections.reverse(list1);
                        sellerChatAdapter = new SellerSenderRcvrChatAdapter(context,list1);
                        chat_rv.setAdapter(sellerChatAdapter);
                        sellerChatAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ParentChatWithSenderRcvr> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("res", t.getMessage());
                }
            });

        }


    private final Runnable apiRunnable = new Runnable() {
        @Override
        public void run() {
           /* hm.put("fromuserid", fromuserid);
            hm.put("touserid", touserid);*/
           // showMsg(hm);
            showChatSenderRcvr();
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

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(MarketPlaceChatActivity.this, android.R.color.transparent)));
        dialog.setCancelable(false);
        dialog.show();
    }


    private void filter(String text) {
        ArrayList<ChatWithSenderRcvr> filteredlist = new ArrayList<ChatWithSenderRcvr>();
        for (ChatWithSenderRcvr item : list1) {
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

}