package com.app_neighbrsnook.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.R;
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

public class GroupViewMembers extends AppCompatActivity implements BuyCallBack {
    ImageView img_edit_icon, delete_icon, img_back;
    FrameLayout frm_view_members, frm_discussion;
    LinearLayout lnr_view_members;
    FrameLayout lnr_discussion;
    Boolean is_view_members = false;
    Boolean is_discussion = false;
    String propert_type = "";
    TextView tv_members, tv_discussion;
    String property_type_second = "";
    String property_third = "";
    NestedScrollView nestedScrollView;
    String type;
    BuyCallBack bcb = this;
    List<BankListGetterSetter> storesList = new ArrayList<>();
    DAO dao;
    List<ChatModel> list = new ArrayList<>();
    RecyclerView chat_rv;
    LinearLayout chat_et_ll, pay_or_negotiate_ll, tempate_ll;
    RelativeLayout chat_rv_rl;
    TextView item_price, accept_btn, negotiate_tv, accept_temp_tv, t1, t2, t3, price_confirmation_tv;
    EditText bid_amount_et, msg_et;
    TextView titleTv, rupee_tv, pay_tv, buy_textview;
    ChatUserListFragment chatUserListFragment;
    ImageView send_msg_iv;
    boolean isDigit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view_members);
        dao = Database.createDBInstance(GroupViewMembers.this).getDao();
        Intent i = getIntent();
        item_price = findViewById(R.id.item_price);
        accept_btn = findViewById(R.id.accept_btn);
        negotiate_tv = findViewById(R.id.negotiate_tv);
        accept_temp_tv = findViewById(R.id.accept_temp_tv);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        buy_textview = findViewById(R.id.buy_textview);
        titleTv = findViewById(R.id.title);
        rupee_tv = findViewById(R.id.rupee_tv);
        pay_tv = findViewById(R.id.pay_tv);
        send_msg_iv = findViewById(R.id.send_msg_iv);
        img_edit_icon = findViewById(R.id.edit_icon_group);
        delete_icon = findViewById(R.id.delete_icon_img);
        frm_view_members = findViewById(R.id.frm_view_members);
        frm_discussion = findViewById(R.id.frm_discussio);
        lnr_view_members = findViewById(R.id.lnr_view_members_layout);
        lnr_discussion = findViewById(R.id.lnr_discussion);
        img_back = findViewById(R.id.img_back);
        tv_members = findViewById(R.id.tv_view_members);
        tv_discussion = findViewById(R.id.tv_discussion);
        chat_et_ll = findViewById(R.id.chat_et_ll);
        pay_or_negotiate_ll = findViewById(R.id.pay_or_negotiate_ll);
        tempate_ll = findViewById(R.id.tempate_ll);
        chat_rv_rl = findViewById(R.id.chat_rv_rl);
        bid_amount_et = findViewById(R.id.bid_amount_et);
        item_price.setText("\u20B9" + i.getStringExtra("price"));
        price_confirmation_tv = findViewById(R.id.price_confirmation_tv);
        msg_et = findViewById(R.id.item_input);
        chat_rv = findViewById(R.id.chat_rv);
        price_confirmation_tv.setText("Is \u20B9 " + i.getStringExtra("price") + "is ok for you");
        ReadData readPollData = new ReadData();
        readPollData.execute();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("d MMM, HH:mm");
                Date date = new Date();
                ChatModel commentModel = new ChatModel();
                commentModel.setComment_msg(t1.getText().toString());
                commentModel.setTime(formatter.format(date));
                commentModel.setSender_or_reciever(ChatModel.SENDER);
//                commentModel.setSender_or_reciever(ChatModel.RECIVER);
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
//                commentModel.setSender_or_reciever(ChatModel.RECIVER);
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
      //  type = i.getStringExtra("type");
        if (type.equals("seller")) {
            titleTv.setText(i.getStringExtra("buyer_name"));
            pay_or_negotiate_ll.setVisibility(View.GONE);
            chat_et_ll.setVisibility(View.VISIBLE);
            chat_rv_rl.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.GONE);
            negotiate_tv.setTextColor(getResources().getColor(R.color.white));
            negotiate_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_btn));
        } else {
            titleTv.setText(i.getStringExtra("name"));
            if (i.getStringExtra("question").equals("question")) {
                pay_or_negotiate_ll.setVisibility(View.GONE);
                chat_et_ll.setVisibility(View.VISIBLE);
                chat_rv_rl.setVisibility(View.VISIBLE);
                negotiate_tv.setTextColor(getResources().getColor(R.color.white));
                negotiate_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_btn));
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        chat_rv.setLayoutManager(linearLayoutManager);
        frm_view_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_discussion.setVisibility(View.GONE);
                if (is_view_members) {
                    is_view_members = false;
                    propert_type = "VIEW MEMBERS";
                    frm_view_members.setBackgroundResource(R.drawable.round_corner_dialog);
                    tv_members.setTextColor(getResources().getColorStateList(R.color.text_color));
                    lnr_view_members.setVisibility(View.GONE);
                } else {
                    is_view_members = true;
                    propert_type = "";
                    is_discussion = false;
                    frm_view_members.setBackgroundResource(R.drawable.button_back);
                    frm_discussion.setBackgroundResource(R.drawable.round_corner_dialog);
                    tv_discussion.setTextColor(getResources().getColorStateList(R.color.text_color));
                    tv_members.setTextColor(getResources().getColorStateList(R.color.white));
                    lnr_view_members.setVisibility(View.VISIBLE);
                }
            }
        });
        frm_discussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_view_members.setVisibility(View.GONE);
                if (is_discussion) {
                    is_discussion = false;
                    propert_type = "DISCUSSION";
                    frm_discussion.setBackgroundResource(R.drawable.round_corner_dialog);
                    tv_discussion.setTextColor(getResources().getColorStateList(R.color.text_color));
                    lnr_discussion.setVisibility(View.GONE);
                } else {
                    is_discussion = true;
                    propert_type = "";
                    is_view_members = false;
                    frm_discussion.setBackgroundResource(R.drawable.button_back);
                    lnr_discussion.setVisibility(View.VISIBLE);
                    frm_view_members.setBackgroundResource(R.drawable.round_corner_dialog);
                    tv_members.setTextColor(getResources().getColorStateList(R.color.text_color));
                    tv_discussion.setTextColor(getResources().getColorStateList(R.color.white));
                }
            }
        });
        delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(GroupViewMembers.this);
                dialog.setContentView(R.layout.delete_group_layout);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(GroupViewMembers.this, android.R.color.transparent)));
                dialog.getWindow().setAttributes(lp);
                dialog.show();
                ImageView img = dialog.findViewById(R.id.iv9);
                TextView tv_yes = dialog.findViewById(R.id.tv_yes_id);
                TextView tv_no = dialog.findViewById(R.id.tv_no);
                tv_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(GroupViewMembers.this, GroupsActivity.class));
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
                //resetColor();
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
                // resetColor();
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
                } else {
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
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    isDigit = (c >= '0' && c <= '9');
                    if (isDigit) {
                        if (s.length() == 0) {
                            rupee_tv.setVisibility(View.GONE);
                            pay_tv.setVisibility(View.GONE);
                            send_msg_iv.setVisibility(View.VISIBLE);
//
                        } else {
                            rupee_tv.setVisibility(View.VISIBLE);
                            pay_tv.setVisibility(View.VISIBLE);
                            send_msg_iv.setVisibility(View.GONE);
                        }
                    } else {
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
                commentModel.setSender_or_reciever(ChatModel.SENDER);
                //commentModel.setSender_or_reciever(ChatModel.RECIVER);
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
                    return Integer.compare(rhs.getId(), lhs.getId());
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (type.equals("seller")) {
                chat_rv.setAdapter(new ChatAdapter(list, "seller", GroupViewMembers.this));
            } else {
                chat_rv.setAdapter(new ChatAdapter(list, "buyer", GroupViewMembers.this));
            }
        }
    }
}