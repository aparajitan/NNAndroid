package com.app_neighbrsnook.pollModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.PollAdapter;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.fragment.BottomSheetPollFragment;
import com.app_neighbrsnook.intreface.OnUpdateFavListenerPoll;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.Poll;
import com.app_neighbrsnook.pojo.PollListPojo;
import com.app_neighbrsnook.profile.MyProfileOtherUser;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollActivity extends AppCompatActivity implements PollAdapter.NewRequest, OnUpdateFavListenerPoll {
    ImageView searchImageView, addImageView, back_btn, cancel_iv;
    TextView titleTv;
    RecyclerView rv_poll;
    PollAdapter pollAdapter;
    private DAO myDao;
    SharedPrefsManager sm;
    List<PollListPojo> listdata = new ArrayList<>();
    HashMap<String, Object> hm = new HashMap<>();
    boolean isVerifiedUser = true;
    RelativeLayout search_rl;
    EditText search_et;
    String nbr;
    int fromuserid;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        sm = new SharedPrefsManager(this);
        Bundle intent = getIntent().getExtras();
        assert intent != null;
        if (intent != null) {
            nbr = intent.getString("neighbrhood");
        }
        Intent i = getIntent();
        fromuserid = i.getIntExtra("user_id", 0);
        myDao = Database.createDBInstance(this).getDao();

        titleTv = findViewById(R.id.title);
        titleTv.setText("Polls");
        rv_poll = findViewById(R.id.rv_business);
        cancel_iv = findViewById(R.id.cancel_iv);
        rv_poll.setLayoutManager(new LinearLayoutManager(this));
        addImageView = findViewById(R.id.add_imageview);
        searchImageView = findViewById(R.id.search_imageview);
        searchImageView.setVisibility(View.VISIBLE);
        addImageView.setVisibility(View.VISIBLE);
        back_btn = findViewById(R.id.back_btn);
        search_rl = findViewById(R.id.search_rl);
        search_et = findViewById(R.id.search_et);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);

        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        if (nbr != null) {
            if (nbr.equals(sm.getString("neighbrhood"))) {
                hm.put("neighbrhood", sm.getString("neighbrhood"));
            } else if (!nbr.equals("drawar")) {
                hm.put("neighbrhood", nbr);
            }
        }

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Call your refresh method
                pollListApi();
            }
        });

        pollListApi();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                InputMethodManager mgr = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                }
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
                Log.d("STring....", editable.toString());
                filter(editable.toString());

            }
        });

        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVerifiedUser) {
                    Intent createBusinessIntent = new Intent(PollActivity.this, CreatePollActivity.class);
                    createBusinessIntent.putExtra("from", "PollCreate");
                    startActivity(createBusinessIntent);
                } else {
                    GlobalMethods.getInstance(PollActivity.this).globalDialog(PollActivity.this, getString(R.string.unverified_msg));
                }
            }
        });
    }

    private void pollListApi() {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<Poll> call = service.pollList("polllist", hm);
        call.enqueue(new Callback<Poll>() {
            @Override
            public void onResponse(Call<Poll> call, Response<Poll> response) {
                // Stop refresh animation when response is received
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                String status = response.body().getStatus();
                if (response.body().getVerfied_msg().equals("User Verification is completed!")) {
                    isVerifiedUser = true;
                } else {
                    isVerifiedUser = false;
                }
                if (status.equals("success")) {
                    listdata = response.body().getNbdata();
                    if (listdata.size() > 0) {
                        pollAdapter = new PollAdapter(listdata, PollActivity.this);
                        rv_poll.setAdapter(pollAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<Poll> call, Throwable t) {
                // Stop refresh animation on failure too
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                Toast.makeText(PollActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(int pos) {

    }

    @Override
    public void onDetail(int userId) {
        if (isVerifiedUser) {
            Intent i = new Intent(PollActivity.this, MyProfileOtherUser.class);
            i.putExtra("user_id", userId);
            startActivity(i);
        } else {
            GlobalMethods.getInstance(PollActivity.this).globalDialog(PollActivity.this, getString(R.string.unverified_msg));
        }
    }

    @Override
    public void threeDot(int pos) {
        if (isVerifiedUser) {
            BottomSheetPollFragment bottomSheetPoll = new BottomSheetPollFragment(PollActivity.this,
                    listdata.get(pos).getFavouritstatus(),
                    listdata.get(pos).getCreatedBy(),
                    listdata.get(pos).getNeighborhood(),
                    listdata.get(pos).getPollQues(),
                    listdata.get(pos).getpId(),
                    listdata.get(pos).getUserid(),
                    "poll");
            bottomSheetPoll.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment1");
        } else {
            GlobalMethods.getInstance(PollActivity.this).globalDialog(PollActivity.this, getString(R.string.unverified_msg));
        }
    }

    @Override
    public void onDetailPage(int position, int pos, String isVoted, String ispollrunning) {
        if (isVerifiedUser) {
            if (ispollrunning.equals("1")) {
                if (isVoted.equals("1")) {
                    Intent pollDetailIntent = new Intent(this, PollDetailActivity.class);
                    pollDetailIntent.putExtra("id", pos);
                    startActivity(pollDetailIntent);
                } else {
                    Intent pollDetailIntent = new Intent(this, PollDetailActivity.class);
                    pollDetailIntent.putExtra("id", pos);
                    startActivity(pollDetailIntent);
                }
            } else if (ispollrunning.equals("2")) {
                if (isVoted.equals("1")) {
                    Intent pollDetailIntent = new Intent(this, PollDetailActivity.class);
                    pollDetailIntent.putExtra("id", pos);
                    startActivity(pollDetailIntent);
                } else if ((listdata.get(position).getCreatedBy().equals(sm.getString("user_name")))) {
                    Intent pollDetailIntent = new Intent(this, PollDetailActivity.class);
                    pollDetailIntent.putExtra("id", pos);
                    startActivity(pollDetailIntent);
                } else {
                    GlobalMethods.getInstance(this).globalDialogCenter(this, "Polling is closed.");
                }
            } else if (ispollrunning.equals("0")) {
                Intent pollDetailIntent = new Intent(this, PollDetailActivity.class);
                pollDetailIntent.putExtra("id", pos);
                startActivity(pollDetailIntent);
            }
        } else {
            GlobalMethods.getInstance(PollActivity.this).globalDialog(PollActivity.this, getString(R.string.unverified_msg));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        pollListApi();
    }


    private void filter(String text) {
        Log.d("polllist..", text);
        ArrayList<PollListPojo> filteredlist = new ArrayList<PollListPojo>();
        for (PollListPojo item : listdata) {
            Log.d("polllist..", item.toString());
            if (item.getCreatedBy().toLowerCase().contains(text.toLowerCase()) || item.getPollQues().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("polllist...filter", filteredlist.toString());
            pollAdapter.filterList(filteredlist);
        }
    }

    @Override
    public void onListItemClicked(String itemName) {
        pollListApi();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PollActivity.this, MainActivity.class));
        finishAffinity();
    }
}