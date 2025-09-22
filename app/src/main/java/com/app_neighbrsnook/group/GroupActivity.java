package com.app_neighbrsnook.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.google.gson.JsonElement;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.GroupListAdapter;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.myNeighbourhood.MyNeighbourhoodActivity;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.GroupListPojo;
import com.app_neighbrsnook.pojo.GroupResponseListPojo;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupActivity extends AppCompatActivity implements GroupListAdapter.NewRequest, MyNeighbourhoodActivity.ListPageListener {
    private DAO myDao;
    RecyclerView rv_poll;
    TextView tv_group_top, tv_join_group;
    ImageView img_search;
    ImageView img_back, cancel_iv;
    FrameLayout lnr_owner_second, lnr_owner_first, frm_exit_parent_first;
    FrameLayout frm_exit_group_first, frm_join_nd_apprv, frm_apprvl_pending, image_add, lnr_third_card, frm_exit_layout_group;
    FrameLayout frm_third_card, frm_exit_group_last, frm_join_last, frm_approval_join;
    SharedPrefsManager sm;
    Context context;
    Activity activity;
    GroupListPojo groupListPojo;
    int id;
    List<Integer> list;
    String nbr;
    int businessId;
    RelativeLayout search_rl;
    EditText search_et;
    List<GroupListPojo> listdata = new ArrayList<>();
    GroupListAdapter groupListAdapter;
    boolean isVerifiedUser = true;
    int fromuserid;
    HashMap<String, Object> hm = new HashMap<>();
    TextView tvAll, tvOwner, tvPrivate, tvPublic, tvJoined;
    private TextView lastSelectedButton;
    boolean isGoDetail = false;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        setContentView(R.layout.activity_group_show);

        sm = new SharedPrefsManager(this);
        Bundle intent = getIntent().getExtras();
        nbr = intent.getString("neighbrhood");
        myDao = Database.createDBInstance(this).getDao();

        findItem();

        // Set "All" as the default selected button
        lastSelectedButton = tvAll;
        updateButtonAppearance(tvAll, true);

        list = new ArrayList<>();
        businessId = Integer.parseInt((PrefMananger.GetLoginData(context).getId() + ""));
        Intent i = getIntent();
        fromuserid = i.getIntExtra("user_id", 0);
        businessId = Integer.parseInt((PrefMananger.GetLoginData(context).getId() + ""));
        Log.d("ddddddddd", String.valueOf(businessId));
        Log.d("list", list.toString());
        hm.put("userid", businessId);
        if (nbr != null) {
            if (nbr.equals(sm.getString("neighbrhood"))) {
                hm.put("neighbrhood", sm.getString("neighbrhood"));
            } else if (!nbr.equals("drawar")) {
                hm.put("neighbrhood", nbr);
            }
        } else {
            hm.put("userid", fromuserid);
            hm.put("groupuserlist", fromuserid);
        }

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Call your refresh method
                if (InternetConnection.checkConnection(context)) {
                    groupListApi(hm);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    GlobalMethods.getInstance(GroupActivity.this).globalDialog(context, "     No internet connection.     ");
                }
            }
        });

        if (InternetConnection.checkConnection(context)) {
            groupListApi(hm);
        } else {
            GlobalMethods.getInstance(GroupActivity.this).globalDialog(context, "     No internet connection.     ");
        }

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_rl.setVisibility(View.VISIBLE);
                search_et.requestFocus();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rv_poll.setLayoutManager(new LinearLayoutManager(this));
        image_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVerifiedUser) {
                    startActivity(new Intent(GroupActivity.this, CreateGroups.class));
                } else {
                    GlobalMethods.getInstance(GroupActivity.this).globalDialog(GroupActivity.this, getString(R.string.unverified_msg));
                }
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

        // Set click listeners
        tvAll.setOnClickListener(v -> {
            filterListByType("All");
            updateButtonAppearance(tvAll, true);
        });
        tvOwner.setOnClickListener(v -> {
            filterListByType("Owner");
            updateButtonAppearance(tvOwner, true);
        });
        tvPrivate.setOnClickListener(v -> {
            filterListByType("Private");
            updateButtonAppearance(tvPrivate, true);
        });
        tvPublic.setOnClickListener(v -> {
            filterListByType("Public");
            updateButtonAppearance(tvPublic, true);
        });
        tvJoined.setOnClickListener(v -> {
            filterListByType("Joined");
            updateButtonAppearance(tvJoined, true);
        });
    }

    private void findItem() {
        tvAll = findViewById(R.id.tvAll);
        tvOwner = findViewById(R.id.tvOwner);
        tvPrivate = findViewById(R.id.tvPrivate);
        tvPublic = findViewById(R.id.tvPublic);
        tvJoined = findViewById(R.id.tvJoined);
        image_add = findViewById(R.id.create_group_id);
        search_rl = findViewById(R.id.search_rl);
        cancel_iv = findViewById(R.id.cancel_iv);
        search_et = findViewById(R.id.search_et);
        img_search = findViewById(R.id.search_id);
        tv_group_top = findViewById(R.id.group_id);
        rv_poll = findViewById(R.id.rv_business);
        img_back = findViewById(R.id.img_back);
        lnr_owner_first = findViewById(R.id.lnr_group_join);
        lnr_owner_second = findViewById(R.id.lnr_group_owner_second);
        frm_exit_group_first = findViewById(R.id.frm_exit);
        frm_exit_parent_first = findViewById(R.id.frm_exit_second_show_group_visible);
        frm_join_nd_apprv = findViewById(R.id.join_id_frame);
        tv_join_group = findViewById(R.id.tv_join_id);
        frm_apprvl_pending = findViewById(R.id.frm_apprvl);
        lnr_third_card = findViewById(R.id.lnr_third_join);
        frm_third_card = findViewById(R.id.lnr_group_join_six);
        frm_exit_group_last = findViewById(R.id.frm_id_exit_last);
        frm_exit_layout_group = findViewById(R.id.frm_exit_layout);
        frm_join_last = findViewById(R.id.frm_join_id_last);
        frm_approval_join = findViewById(R.id.frm_approval_last);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
    }

    private void groupListApi(HashMap<String, Object> hm) {
        Boolean isInternetConnection = GlobalMethods.checkConnection(context);
        if (isInternetConnection) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<GroupResponseListPojo> call = service.groupList("grouplist", hm);
            call.enqueue(new Callback<GroupResponseListPojo>() {
                @Override
                public void onResponse(Call<GroupResponseListPojo> call, Response<GroupResponseListPojo> response) {
                    // Stop refresh animation when response is received
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    String status = response.body().getStatus();
                    if (response.body().getVerfied_msg().equals("User Verification is not completed!")) {
                        isVerifiedUser = false;
                    }
                    if (status.equals("failed")) {
                        Toast.makeText(GroupActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        listdata = response.body().getListdata();
                        groupListAdapter = new GroupListAdapter(listdata, GroupActivity.this, isVerifiedUser);
                        rv_poll.setAdapter(groupListAdapter);
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<GroupResponseListPojo> call, Throwable t) {
                    // Stop refresh animation on failure too
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    dialog.dismiss();
                    Log.e("fasdfafsd", t.toString());
                    Toast.makeText(GroupActivity.this, "Something seems to have gone wrong.Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Stop refresh animation if no internet
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            GlobalMethods.getInstance(this).globalDialog(context, "     No internet connection.");
        }
    }

    private void updateButtonAppearance(TextView button, boolean isSelected) {
        // Reset the last selected button
        if (lastSelectedButton != null) {
            lastSelectedButton.setBackgroundResource(R.drawable.filter_bg_unselected);
            lastSelectedButton.setTextColor(ContextCompat.getColor(this, R.color.black)); // Unselected text color
        }

        // Update the current button
        if (isSelected) {
            button.setBackgroundResource(R.drawable.filter_bg_selected);
            button.setTextColor(ContextCompat.getColor(this, R.color.white)); // Selected text color
            lastSelectedButton = button;
        }
    }

    private void filterListByType(String type) {
        if (groupListAdapter == null || listdata == null) {
            // Toast.makeText(this, "Data not loaded yet. Please wait...", Toast.LENGTH_SHORT).show();
            return;
        }
        List<GroupListPojo> filteredList = new ArrayList<>();
        for (GroupListPojo group : listdata) {
            switch (type) {
                case "All":
                    filteredList.add(group); // Show all groups
                    break;
                case "Private":
                    if (group.getGroupType().equals("Private")) {
                        filteredList.add(group);
                    }
                    break;
                case "Public":
                    if (group.getGroupType().equals("Public")) {
                        filteredList.add(group);
                    }
                    break;
                case "Owner":
                    if (PrefMananger.GetLoginData(context).getId().equals(group.getUserid())) {
                        filteredList.add(group); // Show groups owned by the user
                    }
                    break;
                case "Joined":
                    if (group.getGetjoin().equals("joined")) {
                        filteredList.add(group); // Show groups the user has joined
                    }
                    break;
            }
        }

        // Update the adapter with the filtered list
        groupListAdapter.filterList((ArrayList<GroupListPojo>) filteredList);
    }

    @Override
    public void onClick(int pos) {

    }

    @Override
    public void onexit(int groupid) {
        exitLayout(groupid);
    }

    @Override
    public void onApproval(int groupid, String getUsername, String getGroupname) {
        GroupListPojo listPojo = listdata.get(groupid);
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt((PrefMananger.GetLoginData(context).getId() + "")));
        hm.put("groupid", listPojo.getGroupid());
        hm.put("username", sm.getString("user_name"));
        hm.put("groupname", getGroupname);
        joinGroupApi(hm, listPojo);
    }

    @Override
    public void onClickDetail(int pos, int id) {
        Intent detailScreen = new Intent(this, GroupDetailActivity.class);
        detailScreen.putExtra("id", id);
        detailScreen.putExtra("type", "detail");
        detailScreen.putExtra("groupType", groupListPojo.getGroupType());
        startActivity(detailScreen);
    }

    private void exitApi(HashMap<String, Object> hm) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.userExit("exitgroup", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                dialog.dismiss();
                Toast.makeText(context, "Exit successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });

    }

    public void exitLayout(int groupid) {
        Dialog dialog = new Dialog(GroupActivity.this);
        dialog.setContentView(R.layout.group_exit_request);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(GroupActivity.this, android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        TextView tv_no = dialog.findViewById(R.id.no_id);
        TextView tv_yes = dialog.findViewById(R.id.yes_id);
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
                hm.put("userid", Integer.parseInt((PrefMananger.GetLoginData(context).getId() + "")));
                hm.put("groupid", groupid);
                exitApi(hm);

                dialog.dismiss();
                hm.put("userid", businessId);
                // hm.put("neighbrhood", " ");
                groupListApi(hm);
//                businessListApi(list);
            }

        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (isGoDetail) {
            isGoDetail = false;
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("userid", businessId);
            //by arsad hm.put("neighbrhood", " ");
            groupListApi(hm);
        }
    }

    private void joinGroupApi(HashMap<String, Object> hm, GroupListPojo businessModel) {
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
                if (response.body() != null && response.body().getJoinStatus() != null && response.body().getJoinStatus().equals("joined")) {
                    Intent detailScreen = new Intent(context, GroupDetailActivity.class);
                    detailScreen.putExtra("id", Integer.parseInt(businessModel.getGroupid()));
                    detailScreen.putExtra("data", businessModel.getGroupType());
                    detailScreen.putExtra("type", "detail");
                    startActivity(detailScreen);
                    isGoDetail = true;
                }

                if (isGoDetail) {
                    return;
                }
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userid", businessId);
                groupListApi(hm);
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                Log.d("res", t.getMessage());
            }
        });
    }

    @Override
    public void businessList(String neighbrhood) {
    }

    @Override
    public void groupListpage(String neighbrhood) {
        nbr = neighbrhood;
    }

    @Override
    public void eventList(String neighbrhood) {

    }

    private void filter(String text) {
        ArrayList<GroupListPojo> filteredlist = new ArrayList<GroupListPojo>();
        for (GroupListPojo item : listdata) {
            if ((item.getGroupname().toLowerCase().contains(text.toLowerCase()) ||
                    item.getUsername().toLowerCase().contains(text.toLowerCase()))) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            groupListAdapter.filterList(filteredlist);
        }
    }

    public static class InternetConnection {
        public static boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connMgr != null) {
                @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

                if (activeNetworkInfo != null) { // connected to the internet
                    // connected to the mobile provider's data plan
                    if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        return true;
                    } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
                }
            }
            return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        groupListApi(hm);
    }
}