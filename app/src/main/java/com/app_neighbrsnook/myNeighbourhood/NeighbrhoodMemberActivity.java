package com.app_neighbrsnook.myNeighbourhood;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.app_neighbrsnook.profile.MyProfileOtherUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.NeighbourhoodMemberAdapter;
import com.app_neighbrsnook.chat.User;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.neighborhoodMember.Listdatum;
import com.app_neighbrsnook.pojo.neighborhoodMember.MemberPojo;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NeighbrhoodMemberActivity extends AppCompatActivity implements NeighbourhoodMemberAdapter.UserCallBack {
    ImageView back_btn, cancel_iv;
    TextView titleTv, tvAll, tvBlock;
    RecyclerView userList_rv;
    NeighbourhoodMemberAdapter neighbourhoodMemberAdapter;
    SharedPrefsManager sm;
    List<Listdatum> list1 = new ArrayList<>();
    NeighbourhoodMemberAdapter.UserCallBack ucb = this;
    ImageView searchImageView, addImageView;
    String nbr;
    LinearLayout llAll, llBlock;
    private TextView lastSelectedButton;
    RelativeLayout search_rl;
    EditText search_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbrhood_member);

        sm = new SharedPrefsManager(this);

        userList_rv = findViewById(R.id.user_list_rv);
        titleTv = findViewById(R.id.title);
        back_btn = findViewById(R.id.back_btn);
        searchImageView = findViewById(R.id.search_imageview);
        cancel_iv = findViewById(R.id.cancel_iv);
        search_rl = findViewById(R.id.search_rl);
        search_et = findViewById(R.id.search_et);
        addImageView = findViewById(R.id.add_imageview);
        llAll = findViewById(R.id.llAll);
        tvAll = findViewById(R.id.tvAll);
        llBlock = findViewById(R.id.llBlock);
        tvBlock = findViewById(R.id.tvBlock);

        addImageView.setVisibility(View.GONE);

        Bundle intent = getIntent().getExtras();
        nbr = intent.getString("neighbrhood");
        userList_rv.setLayoutManager(new LinearLayoutManager(this));
        getAllUsers();
        lastSelectedButton = tvAll;
        updateButtonAppearance(tvAll, true);

        titleTv.setText("Members");

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

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llAll.setOnClickListener(v -> {
            filterListByType("All");
            updateButtonAppearance(tvAll, true);
        });
        llBlock.setOnClickListener(v -> {
            filterListByType("Blocked");
            updateButtonAppearance(tvBlock, true);
        });
    }

    public void getAllUsers() {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        if (nbr != null) {
            if (nbr.equals(sm.getString("neighbrhood"))) {
                hm.put("neighbrhood", sm.getString("neighbrhood"));
            } else if (!nbr.equals("drawar")) {
                hm.put("neighbrhood", nbr);
            }
        }
        hm.put("nbd_id", nbr);
        hm.put("type", "members");
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<MemberPojo> call = service.memberListApi("neighbrhoodmemberlist", hm);
        call.enqueue(new Callback<MemberPojo>() {
            @Override
            public void onResponse(Call<MemberPojo> call, Response<MemberPojo> response) {

                if (response.body().getMessage().equals("Data Found")) {
                    list1 = response.body().getListdata();
//                    }
                    neighbourhoodMemberAdapter = new NeighbourhoodMemberAdapter(list1, ucb);
                    userList_rv.setAdapter(neighbourhoodMemberAdapter);
                } else {
                    Toast.makeText(NeighbrhoodMemberActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MemberPojo> call, Throwable t) {
                Toast.makeText(NeighbrhoodMemberActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });


    }
    @Override
    public void onUserClick(int id, String name, String userPic, int isBlocked) {
        Intent i = new Intent(NeighbrhoodMemberActivity.this, MyProfileOtherUser.class);
        i.putExtra("user_id", id);
        startActivity(i);

    }

    @Override
    public void onUserBlock(String memberUserId, String flag) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("blocker_userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("blocked_userid", memberUserId);
        hm.put("action", flag);
        APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
        Call<CommonPojoSuccess> call = service.userBlockApi(hm);

        call.enqueue(new Callback<CommonPojoSuccess>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<CommonPojoSuccess> call, Response<CommonPojoSuccess> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CommonPojoSuccess commonPojoSuccess = response.body();
                    if ("success".equals(commonPojoSuccess.getStatus())) {

                    } else {
                    }
                } else {
                    Log.e(TAG, "Token Update Failed: " + response.errorBody());
                    FancyToast.makeText(getApplicationContext(), "API Error: Failed to update token.", Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }

            @Override
            public void onFailure(Call<CommonPojoSuccess> call, Throwable t) {
                Log.e(TAG, "API Failure: " + t.getMessage());
                FancyToast.makeText(getApplicationContext(), "Network Error: Failed to update token.", Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }

    private void filter(String text) {
        List<Listdatum> filteredList = new ArrayList<>();
        for (Listdatum item : list1) {
            if (item.getFullname().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        neighbourhoodMemberAdapter.filterList(filteredList);
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
        List<Listdatum> filteredList = new ArrayList<>();

        for (Listdatum listdatum : list1) {
            switch (type) {
                case "All":
                    filteredList.add(listdatum); // Show all groups
                    break;
                case "Blocked":
                    if (listdatum.getIs_blocked() == 1) {
                        filteredList.add(listdatum);
                    }
                    break;
            }
        }

        // Update the adapter with the filtered list
        neighbourhoodMemberAdapter.filterList((ArrayList<Listdatum>) filteredList);
    }

}