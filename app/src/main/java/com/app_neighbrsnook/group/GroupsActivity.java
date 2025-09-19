package com.app_neighbrsnook.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.GroupListAdapter;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.GroupListPojo;
import com.app_neighbrsnook.pojo.GroupResponseListPojo;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.app_neighbrsnook.utils.UtilityFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupsActivity extends AppCompatActivity implements GroupListAdapter.NewRequest{
    ImageView img_create_group,img_back;
    LinearLayout lnr_join_group;
    FrameLayout lnr_third_card,lnr_group_six;
    FrameLayout frm_group_hide,frm_show_layout,frm_join_nd_apprv,frm_apprvl_pending;
    ImageView img_search,img_cancel;
    TextView tv_join_group,tv_approval,tv_approval_pending;
    TextView tv_sector_first,tv_sector_second,amar_profile;
    Context context;
    Activity activity;
    RecyclerView rv_poll;
    List<Integer> list;
    int businessId;
    SharedPrefsManager sm;


    FrameLayout frm_one,frm_exit,lnr_four_card_join,frm_second_image;
    Boolean isOnePressed = false,
            isSecondPlace = false;
    FrameLayout frm_join_btn,frm_parent_card;
    FrameLayout frm_last_approval;
    TextView tv_amar_second,tv_group;
    FrameLayout lnr_group_first;
    FrameLayout frm_exit_group_second;
    FrameLayout frm_owner_second;
    List<GroupListPojo> listdata = new ArrayList<>();

    GroupListAdapter groupListAdapter;
    boolean isVerifiedUser = true;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=activity=this;
        setContentView(R.layout.activity_groups);
        tv_group=findViewById(R.id.group_id);

        lnr_group_six=findViewById(R.id.lnr_group_join_six);
       // tv_amar_second=findViewById(R.id.amar_dubey_second);
        frm_join_btn=findViewById(R.id.frm_join_id_last);
        //frm_parent_card=findViewById(R.id.lnr_last_card);
        lnr_group_first=findViewById(R.id.lnr_group_join);
        lnr_join_group=findViewById(R.id.lnr_join);
        //amar_profile=findViewById(R.id.amar_dubey);
        frm_last_approval=findViewById(R.id.frm_approval_last);
        img_create_group=findViewById(R.id.create_group_id);
        lnr_third_card=findViewById(R.id.lnr_third_join);
        tv_approval_pending=findViewById(R.id.tv_approval_pending);
        //img_cancel=findViewById(R.id.id_cancel);
        frm_apprvl_pending=findViewById(R.id.frm_apprvl);
        frm_show_layout=findViewById(R.id.frm_show_second_layout);
        frm_group_hide=findViewById(R.id.frm_group_bar_hide);
        lnr_four_card_join=findViewById(R.id.lnr_fourth_card);
        tv_join_group=findViewById(R.id.tv_join_id);
        frm_join_nd_apprv=findViewById(R.id.join_id_frame);
        img_search=findViewById(R.id.search_id);
        rv_poll = findViewById(R.id.rv_business);
        list = new ArrayList<>();
        businessId = Integer.parseInt((PrefMananger.GetLoginData(context).getId() +""));
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", businessId);
        hm.put("groupuserlist", businessId);
        //by arsad hm.put("neighbrhood", nbr);
        groupListApi(hm);
        sm = new SharedPrefsManager(this);
        img_create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVerifiedUser)
                {
                    startActivity(new Intent(GroupsActivity.this, CreateGroups.class));

                }else {
                    GlobalMethods.getInstance(GroupsActivity.this).globalDialog(GroupsActivity.this, getString(R.string.unverified_msg));

                }
            }
        });


        //tv_sector_first=findViewById(R.id.sector_id);
       // tv_approval=findViewById(R.id.tv_approval);
         img_back=findViewById(R.id.img_back);
         frm_one=findViewById(R.id.id_one_image);
         frm_exit=findViewById(R.id.frm_exit);
         frm_owner_second=findViewById(R.id.lnr_group_owner_second);
         frm_exit_group_second=findViewById(R.id.frm_exit_second_show_group_visible);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        rv_poll.setLayoutManager(new LinearLayoutManager(this));
    }

    private void groupListApi(HashMap<String, Object> hm) {
        Boolean isInternetConnection = GlobalMethods.checkConnection(context);
        if(isInternetConnection) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<GroupResponseListPojo> call = service.groupList("grouplist", hm);
            call.enqueue(new Callback<GroupResponseListPojo>() {
                @Override
                public void onResponse(Call<GroupResponseListPojo> call, Response<GroupResponseListPojo> response) {
                    String status = response.body().getStatus();
                    if(response.body().getVerfied_msg().equals("User Verification is not completed!"))
                    {
                        isVerifiedUser = false;
                    }
                    if(status.equals("failed")) {
                        Toast.makeText(GroupsActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else {
                        listdata = response.body().getListdata();
                        groupListAdapter = new GroupListAdapter(listdata, GroupsActivity.this,isVerifiedUser);
                        rv_poll.setAdapter(groupListAdapter);
                        dialog.dismiss();
                    }
                }
                @Override
                public void onFailure(Call<GroupResponseListPojo> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("fasdfafsd",t.toString());
                    Toast.makeText(GroupsActivity.this, "Something seems to have gone wrong.Please try again", Toast.LENGTH_SHORT).show();


                }
            });
        }else {
            GlobalMethods.getInstance(this).globalDialog(context, "     No internet connection."     );
        }
    }
    @Override
    public void onClick(int pos) {
    }

    @Override
    public void onexit(int groupid) {

    }
    @Override
    public void onApproval(int groupid, String getUsername, String getGroupname) {
    }

    @Override
    public void onClickDetail(int pos, int id) {

    }
}