package com.app_neighbrsnook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.adapter.ChatAdapter;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.fragment.ChatUserListFragment;
import com.app_neighbrsnook.intreface.BuyCallBack;
import com.app_neighbrsnook.model.BankListGetterSetter;
import com.app_neighbrsnook.model.ChatModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ConfirmActivity extends AppCompatActivity implements  BuyCallBack {
    TextView  item_price, accept_btn, negotiate_tv, accept_temp_tv, t1, t2,t3, price_confirmation_tv;
    EditText bid_amount_et, msg_et;
    RecyclerView chat_rv,recyclerView;
    TextView titleTv, rupee_tv, pay_tv, free_item_tv, buy_textview;
    ImageView searchImageView, addImageView, back_btn, send_msg_iv;
    List<ChatModel> list = new ArrayList<>();
    DAO dao;
    RadioButton wallet, upi, debitCard, creditCard, netBanking, payOnDelivery;
    ChatUserListFragment chatUserListFragment;
    boolean isDigit = false;
    RelativeLayout chat_rv_rl;
    LinearLayout chat_et_ll, pay_or_negotiate_ll, tempate_ll;
    NestedScrollView nestedScrollView;
    String type, subject;
    BuyCallBack bcb = this;
    List<BankListGetterSetter> storesList = new ArrayList<>();
    private TextView tv_add_detail, tv_no_data, wallet_pay, upi_pay;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        dao = Database.createDBInstance(ConfirmActivity.this).getDao();
        Intent i = getIntent();
        subject = i.getStringExtra("subject");

        chat_rv_rl = findViewById(R.id.chat_rv_rl);
        item_price = findViewById(R.id.item_price);
        chat_et_ll = findViewById(R.id.chat_et_ll);
        item_price.setText("\u20B9"+i.getStringExtra("price"));
        bid_amount_et = findViewById(R.id.bid_amount_et);
        accept_temp_tv = findViewById(R.id.accept_temp_tv);
        ReadData readPollData = new ReadData();
        readPollData.execute();
        accept_btn = findViewById(R.id.accept_btn);
        tempate_ll = findViewById(R.id.tempate_ll);
        negotiate_tv = findViewById(R.id.negotiate_tv);
        titleTv = findViewById(R.id.title);
        price_confirmation_tv = findViewById(R.id.price_confirmation_tv);
        price_confirmation_tv.setText("Is \u20B9 "+i.getStringExtra("price")+ "is ok for you");
        pay_or_negotiate_ll = findViewById(R.id.pay_or_negotiate_ll);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("d MMM, HH:mm");
                Date date = new Date();
                ChatModel commentModel = new ChatModel();
                commentModel.setComment_msg(t1.getText().toString());
                commentModel.setTime(formatter.format(date));
                commentModel.setSender_or_reciever(ChatModel.SENDER);
                InsertCommentData readData = new InsertCommentData();
                readData.execute(commentModel);
                msg_et.setText(" ");
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("d MMM, HH:mm");
                Date date = new Date();
                ChatModel commentModel = new ChatModel();
                commentModel.setComment_msg(t2.getText().toString());
                commentModel.setTime(formatter.format(date));
                commentModel.setSender_or_reciever(ChatModel.SENDER);
                InsertCommentData readData = new InsertCommentData();
                readData.execute(commentModel);
                msg_et.setText(" ");
            }
        });

        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("d MMM, HH:mm");
                Date date = new Date();
                ChatModel commentModel = new ChatModel();
                commentModel.setComment_msg(t3.getText().toString());
                commentModel.setTime(formatter.format(date));
                commentModel.setSender_or_reciever(ChatModel.SENDER);
//                commentModel.setSender_or_reciever(ChatModel.RECIVER);
                InsertCommentData readData = new InsertCommentData();
                readData.execute(commentModel);
                msg_et.setText(" ");
            }
        });


        back_btn = findViewById(R.id.back_btn);
        back_btn.setVisibility(View.VISIBLE);
        rupee_tv = findViewById(R.id.rupee_tv);
        buy_textview = findViewById(R.id.buy_textview);
        pay_tv = findViewById(R.id.pay_tv);
        searchImageView = findViewById(R.id.search_imageview);
        addImageView = findViewById(R.id.add_imageview);
        searchImageView.setVisibility(View.GONE);
        addImageView.setVisibility(View.GONE);
        msg_et = findViewById(R.id.item_input);
        type = i.getStringExtra("type");

        if(i.getStringExtra("subject") !=null)
        {
            msg_et.setText("Subject : "+subject +"\n");
            msg_et.setFocusable(true);
        }

        if(type.equals("seller"))
        {
            titleTv.setText(i.getStringExtra("buyer_name"));
            pay_or_negotiate_ll.setVisibility(View.GONE);
            chat_et_ll.setVisibility(View.VISIBLE);
            chat_rv_rl.setVisibility(View.VISIBLE);
           // nestedScrollView.setVisibility(View.GONE);
            negotiate_tv.setTextColor(getResources().getColor(R.color.white));
            negotiate_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_btn));

        }
        else
        {
            titleTv.setText(i.getStringExtra("name"));
            if(i.getStringExtra("question").equals("question"))
            {

                pay_or_negotiate_ll.setVisibility(View.GONE);
                chat_et_ll.setVisibility(View.VISIBLE);
                chat_rv_rl.setVisibility(View.VISIBLE);
                negotiate_tv.setTextColor(getResources().getColor(R.color.white));
                negotiate_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_btn));

            }


        }
        chat_rv = findViewById(R.id.chat_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        chat_rv.setLayoutManager(linearLayoutManager);
        send_msg_iv = findViewById(R.id.send_msg_iv);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        buy_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatUserListFragment = new ChatUserListFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, chatUserListFragment).commit();

            }
        });

        negotiate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetColor();
                chat_et_ll.setVisibility(View.VISIBLE);
                chat_rv_rl.setVisibility(View.VISIBLE);
                tempate_ll.setVisibility(View.GONE);
                negotiate_tv.setTextColor(getResources().getColor(R.color.white));
                negotiate_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_btn));

//                chatUserListFragment = new ChatUserListFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, chatUserListFragment).commit();


            }
        });

        accept_temp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetColor();
                chat_rv_rl.setVisibility(View.VISIBLE);
                tempate_ll.setVisibility(View.VISIBLE);
                accept_temp_tv.setTextColor(getResources().getColor(R.color.white));
                accept_temp_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_btn));
            }
        });

        msg_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    rupee_tv.setVisibility(View.GONE);
                    pay_tv.setVisibility(View.GONE);
                    send_msg_iv.setVisibility(View.VISIBLE);

                }
                else {
                    rupee_tv.setVisibility(View.VISIBLE);
                    pay_tv.setVisibility(View.VISIBLE);
                    send_msg_iv.setVisibility(View.GONE);
                }



            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //Your code here
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Your code here
                for(int i = 0; i<s.length();i++) {
                    char c = s.charAt(i);
                    isDigit = (c >= '0' && c <= '9');
                    if(isDigit) {
                        if (s.length() == 0) {
                            rupee_tv.setVisibility(View.GONE);
                            pay_tv.setVisibility(View.GONE);
                            send_msg_iv.setVisibility(View.VISIBLE);
//

                        }
                        else {
                            rupee_tv.setVisibility(View.VISIBLE);
                            pay_tv.setVisibility(View.VISIBLE);
                            send_msg_iv.setVisibility(View.GONE);


                        }
                    }
                    else{

                        rupee_tv.setVisibility(View.GONE);
                        pay_tv.setVisibility(View.GONE);
                        send_msg_iv.setVisibility(View.VISIBLE);

                    }
                }

            }
        });


        send_msg_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("d MMM, HH:mm");
                Date date = new Date();
                ChatModel commentModel = new ChatModel();
                commentModel.setComment_msg(msg_et.getText().toString());
                commentModel.setTime(formatter.format(date));
//                commentModel.setSender_or_reciever(ChatModel.SENDER);
                commentModel.setSender_or_reciever(ChatModel.RECIVER);
                InsertCommentData readData = new InsertCommentData();
                readData.execute(commentModel);
                msg_et.setText(" ");
            }
        });
    }

    @Override
    public void onBuyClick(int pos) {
        chat_et_ll.setVisibility(View.GONE);
        chat_rv_rl.setVisibility(View.GONE);
        accept_btn.setTextColor(getResources().getColor(R.color.white));
        accept_btn.setBackground(getResources().getDrawable(R.drawable.round_corner_btn));

        Toast.makeText(this, "This is Buyer", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onsellClick(int pos) {
    }

    @Override
    public void onTemplateClick(String t) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("d MMM, HH:mm");
        Date date = new Date();
        ChatModel commentModel = new ChatModel();
        commentModel.setComment_msg(t);
        commentModel.setTime(formatter.format(date));
        commentModel.setSender_or_reciever(ChatModel.SENDER);
        InsertCommentData readData = new InsertCommentData();
        readData.execute(commentModel);

    }

    private void openDialog(String msg) {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.seller_accept);
        TextView msg_textview = dialog.findViewById(R.id.msg_textview);
        msg_textview.setText(msg);
        TextView ok = dialog.findViewById(R.id.confirm_textview);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();
    }

    private class InsertCommentData extends AsyncTask<ChatModel, Void, Void> {

        @Override
        protected Void doInBackground(ChatModel... investers) {
            dao.insertChatData(investers[0]);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ReadData readPollData = new ReadData();
            readPollData.execute();
        }
    }

    private class ReadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... recurrences) {
            list = new ArrayList<>();
            list = dao.getAllChatList();

            Collections.sort(list, new Comparator<ChatModel>() {
                @Override
                public int compare(ChatModel lhs, ChatModel rhs) {
                    return Integer.compare( rhs.getId(),lhs.getId());
                }
            });

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(type.equals("seller")) {
                chat_rv.setAdapter(new ChatAdapter(list, "seller", ConfirmActivity.this));
            }
            else {
                chat_rv.setAdapter(new ChatAdapter(list, "buyer",ConfirmActivity.this));
            }

        }

    }

    private void resetColor() {

        negotiate_tv.setTextColor(getResources().getColor(R.color.text_color));
        accept_btn.setTextColor(getResources().getColor(R.color.text_color));

        negotiate_tv.setBackground(getResources().getDrawable(R.drawable.add_comment_background));
        accept_btn.setBackground(getResources().getDrawable(R.drawable.add_comment_background));

    }

}