package com.app_neighbrsnook.group;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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

import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.profile.MyProfile;
import com.app_neighbrsnook.profile.MyProfileOtherUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonElement;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.GroupDetailsJoinDetailsAdapter;
import com.app_neighbrsnook.chat.SharedPrefsManager;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.intreface.BuyCallBack;
import com.app_neighbrsnook.model.ChatModel;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.GroupDetailsPojo;
import com.app_neighbrsnook.pojo.GroupDetailsbyNamePojo;
import com.app_neighbrsnook.pojo.GroupDetailsbyNameResponse;
import com.app_neighbrsnook.pojo.GroupListPojo;
import com.app_neighbrsnook.utils.PrefMananger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDetailActivity extends AppCompatActivity implements BuyCallBack, GroupDetailsJoinDetailsAdapter.NewRequest {
    ImageView img_edit_icon, img_back;
    FrameLayout frm_view_members, frm_discussion;
    LinearLayout lnr_view_members;
    FrameLayout lnr_discussion, frm_edit_details;
    Boolean is_view_members = false;
    Boolean is_discussion = false;
    String propert_type = "";
    FrameLayout frm_amit_dubey, frm_vijay;
    ImageView img_amit, img_vijay, img_ysr;
    ImageView img_group_details;
    FrameLayout frm_approve, frm_declined, frm_yasar, delete_icon, frm_yasar_parent;
    LinearLayout lnr_approved_declined;
    private DAO myDao;
    TextView tv_members, tv_discussion;
    RecyclerView chat_rv;
    String st_user_id;
    int pos;
    List<ChatModel> list = new ArrayList<>();
    DAO dao;
    String groupName, groupPrivate, groupAbout, groupMembers, stNeighbrhood, stUserName;
    EditText bid_amount_et, msg_et;
    String groupType = "";
    LinearLayout chat_et_ll, pay_or_negotiate_ll, tempate_ll;
    RelativeLayout chat_rv_rl;
    TextView item_price, accept_btn, negotiate_tv, accept_temp_tv, t1, t2, t3, price_confirmation_tv;
    ImageView send_msg_iv;
    SharedPrefsManager sm;
    int id;
    Context context;
    Activity activity;
    TextView tv_group_name, tv_members_details, tv_about, tv_private;
    String group_id, userid, pic;
    RecyclerView rec_details;
    ImageView img_owner_profile;
    TextView tv_user_name, tv_members_type_pub_pri, tv_owner_neighbrhood;
    GroupListPojo groupListPojo;
    GroupDetailsbyNamePojo businessModel;
    GroupDetailsJoinDetailsAdapter groupListAdapter;
    LinearLayout lnrOwnerGroup;
    FrameLayout frm_join_id;
    List<GroupDetailsbyNamePojo> memberlist = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        setContentView(R.layout.activity_group_member_list_second);

        Intent intent = getIntent();
        sm = new SharedPrefsManager(this);
        id = intent.getIntExtra("id", 0);
//        groupListPojo = (GroupListPojo) getIntent().getStringExtra("");
//        groupType = groupListPojo.getGroupType();//intent.getStringExtra("groupType");
        groupType = intent.getStringExtra("data");
        // id = intent.getIntExtra("id",0)
        dao = Database.createDBInstance(GroupDetailActivity.this).getDao();
        rec_details = findViewById(R.id.rec_details_page);
        frm_edit_details = findViewById(R.id.frm_edit_details);
        tv_owner_neighbrhood = findViewById(R.id.tv_neighbrhood_id);
        img_owner_profile = findViewById(R.id.img_user_profile);
        tv_user_name = findViewById(R.id.tv_owner_name_id);
        tv_members_details = findViewById(R.id.tv_members_details);
        tv_group_name = findViewById(R.id.tv_group_name_details);
        tv_about = findViewById(R.id.tv_details_group);
        tv_private = findViewById(R.id.tv_join_type_details);
        chat_et_ll = findViewById(R.id.chat_et_ll);
        chat_rv_rl = findViewById(R.id.chat_rv_rl);
        bid_amount_et = findViewById(R.id.bid_amount_et);
        accept_temp_tv = findViewById(R.id.accept_temp_tv);
        send_msg_iv = findViewById(R.id.send_msg_iv);
        img_group_details = findViewById(R.id.img_details_group);
        msg_et = findViewById(R.id.item_input);
     /*   ReadData readPollData = new ReadData();
        readPollData.execute();*/
        // pos = getIntent().getExtras().getInt("positon");
        //myDao = Database.createDBInstance(this).getDao();
        img_edit_icon = findViewById(R.id.edit_icon_group);
        delete_icon = findViewById(R.id.delete_icon_img);
        frm_view_members = findViewById(R.id.frm_view_members);
        frm_discussion = findViewById(R.id.frm_discussio);
        lnr_view_members = findViewById(R.id.lnr_view_members_layout);
        lnr_discussion = findViewById(R.id.lnr_discussion);
        img_back = findViewById(R.id.img_back);
        frm_amit_dubey = findViewById(R.id.lnr_amit_duber_id);
        frm_vijay = findViewById(R.id.frm_vijay);
        img_vijay = findViewById(R.id.img_vijay);
        frm_approve = findViewById(R.id.frm_approve_id);
        frm_declined = findViewById(R.id.frm_decline_id);
        frm_yasar = findViewById(R.id.frm_arsad_parent);
        lnr_approved_declined = findViewById(R.id.lnr_approve_decline_layout);
        frm_yasar = findViewById(R.id.frm_arsad_parent);
        img_ysr = findViewById(R.id.img_arsad);
        img_amit = findViewById(R.id.img_amit_dubey_icon);
        tv_members = findViewById(R.id.tv_view_members);
        tv_discussion = findViewById(R.id.tv_discussion);
        lnrOwnerGroup = findViewById(R.id.lnrOwnerGroup);
        frm_join_id = findViewById(R.id.frm_join_id);
        chat_rv = findViewById(R.id.chat_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        chat_rv.setLayoutManager(linearLayoutManager);
        delete_icon.setVisibility(View.GONE);
        frm_edit_details.setVisibility(View.GONE);
        lnrOwnerGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (String.valueOf(Integer.parseInt(st_user_id)).equals(String.valueOf(Integer.parseInt((PrefMananger.GetLoginData(context).getId() + ""))))) {
                    Log.d("sdffsfsasfs", String.valueOf(Integer.parseInt((PrefMananger.GetLoginData(context).getId() + ""))));
                    //  if (st_user_id.equals(Integer.parseInt((PrefMananger.GetLoginData(context).getId() +"")))){
                    startActivity(new Intent(GroupDetailActivity.this, MyProfile.class));

                } else {
                    Intent i = new Intent(context, MyProfileOtherUser.class);
                    i.putExtra("user_id", Integer.parseInt(st_user_id));
                    context.startActivity(i);
                }


            }
        });

        frm_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frm_yasar.setVisibility(View.VISIBLE);
                lnr_approved_declined.setVisibility(View.GONE);
                img_ysr.setVisibility(View.VISIBLE);
            }
        });
        frm_declined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnr_approved_declined.setVisibility(View.GONE);
                frm_yasar.setVisibility(View.GONE);
            }
        });
        img_amit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCancel();
            }
        });
        businessDetailApi(id);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        frm_view_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_discussion.setVisibility(View.GONE);
                if (is_view_members) {
                    is_view_members = false;
                    propert_type = "VIEW MEMBERS";
                    frm_view_members.setBackgroundResource(R.drawable.round_corner_dialog);
                    lnr_view_members.setVisibility(View.GONE);
                    tv_members.setTextColor(getResources().getColorStateList(R.color.text_color));
                } else {
                    is_view_members = true;
                    propert_type = "";
                    is_discussion = false;
                    frm_view_members.setBackgroundResource(R.drawable.button_back);
                    frm_discussion.setBackgroundResource(R.drawable.round_corner_dialog);
                    lnr_view_members.setVisibility(View.VISIBLE);
                    tv_members.setTextColor(getResources().getColorStateList(R.color.white));
                    tv_discussion.setTextColor(getResources().getColorStateList(R.color.text_color));
                }
            }
        });
        frm_discussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frm_discussion.setBackgroundResource(R.drawable.round_corner_dialog);
                //  lnr_discussion.setVisibility(View.GONE);
                tv_discussion.setTextColor(getResources().getColorStateList(R.color.text_color));
                Intent i = new Intent(GroupDetailActivity.this, GroupChat2Activity.class);
                i.putExtra("groupid", group_id);
                i.putExtra("userid", userid);
                i.putExtra("groupName", groupName);
                i.putExtra("pic", pic);
                Log.d("grouo....", group_id);
                Log.d("userid....", userid);
                Log.d("groupName....", groupName);
                startActivity(i);


            }
        });

        delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(GroupDetailActivity.this);
                dialog.setContentView(R.layout.delete_group_layout);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(GroupDetailActivity.this, android.R.color.transparent)));
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
                        HashMap<String, Object> hm = new HashMap<>();
                        //  hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                        hm.put("userid", Integer.parseInt(PrefMananger.GetLoginData(context).getId() + ""));
                        hm.put("groupid", id);
                        apiDeleteBusiness(hm);


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

        img_edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailScreen = new Intent(GroupDetailActivity.this, UpdateCreateGroups.class);
                detailScreen.putExtra("id", id);
                detailScreen.putExtra("type", "detail");
                startActivity(detailScreen);
            }
        });

        frm_join_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userid", Integer.parseInt((PrefMananger.GetLoginData(context).getId() + "")));
                Log.d("ss", String.valueOf(Integer.parseInt((PrefMananger.GetLoginData(context).getId() + ""))));
                Log.d("dddddd", String.valueOf(id));
                Log.d("dddddd", groupName);
                Log.d("dddddd", stUserName);
                hm.put("groupid", String.valueOf(id));
                hm.put("username", stUserName);
                hm.put("groupname", groupName);
                joinGroupApi(hm);
                businessDetailApi(id);
            }
        });
    }

    public void dialogCancel() {
        Dialog dialog = new Dialog(GroupDetailActivity.this);
        dialog.setContentView(R.layout.group_pop_up_admin_when_remove);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(GroupDetailActivity.this, android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        TextView tv_no = dialog.findViewById(R.id.no_id);
        TextView tv_yes = dialog.findViewById(R.id.yes_id);
        ImageView img_cancel = dialog.findViewById(R.id.iv9);
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frm_amit_dubey.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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
     /*   @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("d MMM, HH:mm");
        Date date = new Date();
        ChatModel commentModel = new ChatModel();
        commentModel.setComment_msg(t);
        commentModel.setTime(formatter.format(date));
        commentModel.setSender_or_reciever(ChatModel.SENDER);
        InsertCommentData readData = new InsertCommentData();
        readData.execute(commentModel);*/
    }

    private void businessDetailApi(int id) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("groupid", id);
        hm.put("userid", Integer.parseInt((PrefMananger.GetLoginData(context).getId() + "")));
        //hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<GroupDetailsPojo> call = service.groupDetails("groupdetail", hm);
        call.enqueue(new Callback<GroupDetailsPojo>() {
            @Override
            public void onResponse(Call<GroupDetailsPojo> call, Response<GroupDetailsPojo> response) {
                GroupDetailsPojo rootObject = response.body();
                dialog.dismiss();
                // groupListPojo=rootObject;
                //JsonObject rootObject = businessDetailPojo.getAsJsonObject();
                try {
                    groupName = rootObject.getGroupname();
                    stUserName = rootObject.getUsername();
                    Log.d("sfsfds", stUserName);

                    groupMembers = String.valueOf(rootObject.getMembercount());
                    groupAbout = rootObject.getDescription();
                    groupPrivate = rootObject.getGroupType();
                    stNeighbrhood = rootObject.getNeighbrhood();
                    st_user_id = rootObject.getCreatedby();
                    if (groupType == null || groupType.isEmpty()) {
                        groupType = groupPrivate;
                    }
                    userid = rootObject.getCreatedby();
                    group_id = rootObject.getGroupid();
                    pic = rootObject.getImage();
                    tv_private.setText(groupPrivate);
                    tv_group_name.setText(groupName);
                    tv_members_details.setText(groupMembers);
                    tv_about.setText(groupAbout);
                    tv_owner_neighbrhood.setText(stNeighbrhood);

                    if (rootObject.getImage().isEmpty()) {
                        img_group_details.setImageResource(R.drawable.marketplace_white_background);
                    } else {
                        Glide.with(context)
                                .load(rootObject.getImage())
                                .apply(RequestOptions.centerCropTransform())
                                .into(img_group_details);
                        //  Picasso.get().load(rootObject.getCoverImage()).fit().into(event_cover_image);
                    }

                } catch (Exception e) {

                }
                if (rootObject.getMembJoinStatus().equals(1)) {
                    frm_join_id.setVisibility(View.GONE);
                } else if (rootObject.getMembJoinStatus().equals(0)) {
                    frm_join_id.setVisibility(VISIBLE);
                }

                if (PrefMananger.GetLoginData(context).getId().equals(rootObject.getCreatedby())) {
                    delete_icon.setVisibility(VISIBLE);
                    frm_edit_details.setVisibility(VISIBLE);
                    //frm_join_id.setVisibility(View.GONE);
                } else {
                    delete_icon.setVisibility(View.GONE);
                    frm_edit_details.setVisibility(View.GONE);
                }
                groupDetailsbyName();
            }

            @Override
            public void onFailure(Call<GroupDetailsPojo> call, Throwable t) {
                if (t.toString().contains("timeout")) {
                    Toast.makeText(GroupDetailActivity.this, "Something seems to have gone wrong.Please try again", Toast.LENGTH_SHORT).show();
                } else if (t.toString().contains("Unable to resolve host")) {
                    Toast.makeText(GroupDetailActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GroupDetailActivity.this, "Something seems to have gone wrong.Please try again", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void apiDeleteBusiness(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.deleteGroup("deletegroup", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //finishAffinity();
                Toast.makeText(GroupDetailActivity.this, "Group deleted successful", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(GroupDetailActivity.this, GroupActivity.class);
                i.putExtra("neighbrhood", "drawar");
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(GroupDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(int pos) {
    }

    @Override
    public void onexit(int position) {
        HashMap<String, Object> hm = new HashMap<>();
        // Log.d("sjfdk",String.valueOf(userid));
        //  hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        //hm.put("userid",Integer.parseInt(sm.getString("user_id")));
        hm.put("userid", memberlist.get(position).getUserid());
        hm.put("groupname", groupName);
        // hm.put("username", sm.getString("user_name"));
        userDeleteViewGroup(hm);
        // exitLayout(userid);
    }

    @Override
    public void onApproval(int position) {
        GroupDetailsbyNamePojo pojo = memberlist.get(position);
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", pojo.getUserid());
        hm.put("groupid", pojo.getGroupid());
        hm.put("type", "1");
        hm.put("owner", ownerId);
        hm.put("groupname", groupName);
        approvedOrDecline(hm);
    }

    @Override
    public void declined(int position) {
        GroupDetailsbyNamePojo pojo = memberlist.get(position);
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", pojo.getUserid());
        hm.put("groupid", pojo.getGroupid());
        hm.put("type", "2  ");
        hm.put("owner", ownerId);
        //  hm.put("username", );
        // Log.d("username---",sm.getString("user_name"));
        hm.put("groupname", groupName);
        declineUser(hm);
    }

    @Override
    public void onClickRating(int pos) {
    }

    @Override
    public void onClickWriteReview(int pos) {
    }

    @Override
    public void onclickReview(int pos) {
    }

    @Override
    public void onClickDetail(int pos, int id) {
    }

    String ownerId = "";

    private void groupDetailsbyName() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
//        AppCommon.getInstance(BusinessActivity.this).setNonTouchableFlags(BusinessActivity.this);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<GroupDetailsbyNameResponse> call = service.groupbyNameList("viewjoingroupnamelist", String.valueOf(id));
        call.enqueue(new Callback<GroupDetailsbyNameResponse>() {
            @Override
            public void onResponse(Call<GroupDetailsbyNameResponse> call, Response<GroupDetailsbyNameResponse> response) {
                String status = response.body().getStatus();
                if (status.equals("failed")) {
                    Toast.makeText(GroupDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    tv_user_name.setText(response.body().getGroupusername());
                    if (response.body().getGroupuserpic().isEmpty()) {
                        img_owner_profile.setImageResource(R.drawable.marketplace_white_background);
                    } else {
                        Picasso.get().load(response.body().getGroupuserpic()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.marketplace_white_background)
                                .into(img_owner_profile);
                    }
                    if (response.body().getMemberlist() != null) {
                        memberlist = response.body().getMemberlist();
                        //  Log.d("status----", "" + memberlist.toString());
                        rec_details.setLayoutManager(new LinearLayoutManager(context));
                        //  Log.e("fsdfas",new Gson().toJson(PrefMananger.GetLoginData(context)));
                        //  Log.e("fsdfas",response.body().getOwnerid());
                        ownerId = response.body().getOwnerid();
                        if (response.body().getOwnerid().equals(PrefMananger.GetLoginData(context).getId())) {
                            groupListAdapter = new GroupDetailsJoinDetailsAdapter(memberlist);
                            rec_details.setAdapter(groupListAdapter);
                            rec_details.setAdapter(new GroupDetailsJoinDetailsAdapter(memberlist, groupType, true, GroupDetailActivity.this));
                        } else {
                            rec_details.setAdapter(new GroupDetailsJoinDetailsAdapter(memberlist, groupType, false, GroupDetailActivity.this));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GroupDetailsbyNameResponse> call, Throwable t) {
                Toast.makeText(GroupDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void userDeleteViewGroup(HashMap<String, Object> hm) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.userDeleteinGroup("userdeletefromgroup", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                dialog.dismiss();
                //groupDetailsbyName();
                businessDetailApi(id);

                Toast.makeText(GroupDetailActivity.this, "Group user deleted Successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                // Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void approvedOrDecline(HashMap<String, Object> hm) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.approveDeclined("grouprequest", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                dialog.dismiss();
                //     Toast.makeText(context, "Request approved succesful" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void declineUser(HashMap<String, Object> hm) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.approveDeclined("grouprequest", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                dialog.dismiss();

          /*      Intent i = new Intent(GroupMemberListSecond.this, GroupActivityShow.class);
                startActivity(i);
                // finish();
                finishAffinity();*/
                Toast.makeText(context, "Joining request decline successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void joinGroupApi(HashMap<String, Object> hm) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<AddressResponse> call = service.userJoinGroup("userjoingroup", hm);
        call.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                dialog.dismiss();
                response.body().getStatus();
                if (response.body().getStatus().equals("success")) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    frm_join_id.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                Log.d("res", t.getMessage());
            }
        });
    }
}