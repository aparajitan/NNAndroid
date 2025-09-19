package com.app_neighbrsnook.pollModule;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.PollAdapter;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.fragment.BottomSheetPollFragment;
import com.app_neighbrsnook.intreface.OnUpdateFavListenerPoll;
import com.app_neighbrsnook.model.BusinessModel;
import com.app_neighbrsnook.model.PollModel;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.Poll;
import com.app_neighbrsnook.pojo.PollListPojo;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollActivityDemo1 extends AppCompatActivity implements PollAdapter.NewRequest, OnUpdateFavListenerPoll {
    ImageView searchImageView, addImageView, back_btn, no_data_iv;
    TextView titleTv;
    RecyclerView rv_poll;
    PollAdapter pollAdapter;
    List<PollModel> pollList = new ArrayList<>();
    BusinessModel bm;
    private DAO myDao;
    SharedPrefsManager sm;
    List<PollListPojo> listdata = new ArrayList<>();
    HashMap<String, Object> hm = new HashMap<>();
    boolean isVerifiedUser = true;
    RelativeLayout search_rl;
    EditText search_et;
    String nbr;

    int fromuserid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);
        sm = new SharedPrefsManager(this);
        Bundle intent = getIntent().getExtras();
        assert intent != null;
       // from = intent.getString("from");
        if (intent != null) {
            nbr = intent.getString("neighbrhood");

            //The key argument here must match that used in the other activity
        }
        Intent i = getIntent();
        fromuserid = i.getIntExtra("user_id",0);
        myDao = Database.createDBInstance(this).getDao();
        titleTv = findViewById(R.id.title);
        titleTv.setText("Polls");
        rv_poll = findViewById(R.id.rv_business);
        rv_poll.setLayoutManager(new LinearLayoutManager(this));
        addImageView = findViewById(R.id.add_imageview);
        searchImageView = findViewById(R.id.search_imageview);
        searchImageView.setVisibility(View.VISIBLE);
        addImageView.setVisibility(View.VISIBLE);
        back_btn = findViewById(R.id.back_btn);
        search_rl = findViewById(R.id.search_rl);
        search_et = findViewById(R.id.search_et);
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("polluserlist", Integer.parseInt(sm.getString("user_id")));
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
                Log.d("STring...." , editable.toString());
                filter(editable.toString());

            }
        });

        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isVerifiedUser) {
                    Intent createBusinessIntent = new Intent(PollActivityDemo1.this, CreatePollActivity.class);
                    startActivity(createBusinessIntent);
                }
                else {
                    GlobalMethods.getInstance(PollActivityDemo1.this).globalDialog(PollActivityDemo1.this, getString(R.string.unverified_msg));
                }
            }
        });
    }

    private void pollListApi() {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
  /*      if (from.equals("my_neighbrhood")) {
            hm.put("neighbrhood", sm.getString("neighbrhood"));
        }*/
        /*if (from.equals("profile")) {
            hm.put("polluserlist", Integer.parseInt(sm.getString("user_id")));
        }*/

        Call<Poll> call = service.pollList("polllist", hm);
        call.enqueue(new Callback<Poll>() {
            @Override
            public void onResponse(Call<Poll> call, Response<Poll> response) {
                String status = response.body().getStatus();
                if(response.body().getVerfied_msg().equals("User Verification is completed!"))
                {
                    isVerifiedUser = true;
                }
                else {
                    isVerifiedUser = false;
                }
                if (status.equals("success")) {
                    listdata = response.body().getNbdata();
                    if (listdata.size() > 0) {
                        rv_poll.setAdapter(new PollAdapter(listdata, PollActivityDemo1.this));
                    }

                }

            }

            @Override
            public void onFailure(Call<Poll> call, Throwable t) {
                Toast.makeText(PollActivityDemo1.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(int pos) {

    }

    @Override
    public void onDetail(int pos) {
    }

    @Override
    public void threeDot(int pos) {
//        BottomSheetPollFragment bottomSheetDialog = new BottomSheetPollFragment(listdata, pos, "poll");
        BottomSheetPollFragment bottomSheetPoll = new BottomSheetPollFragment(PollActivityDemo1.this,
                listdata.get(pos).getFavouritstatus(),
                listdata.get(pos).getCreatedBy(),
                listdata.get(pos).getNeighborhood(),
                listdata.get(pos).getPollQues(),
                listdata.get(pos).getpId(),
                listdata.get(pos).getCreatedBy(),
                "poll");
        bottomSheetPoll.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment1");

//        BottomSheetFragment bottomSheetBusiness = new BottomSheetFragment(businessList, pos, "business");
//        bottomSheetBusiness.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

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
//                    pollDetailIntent.putExtra("pollclosed", "pollclosed");
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
                    GlobalMethods.getInstance(this).globalDialog(this, "          Polling is closed.          ");
                }
            } else if (ispollrunning.equals("0")) {
                Intent pollDetailIntent = new Intent(this, PollDetailActivity.class);
                pollDetailIntent.putExtra("id", pos);
                startActivity(pollDetailIntent);
//                GlobalMethods.getInstance(this).globalDialog(this,"Voting opens when the poll starts " + listdata.get(position).getStartDate());
            }
        } else {
            //if user not verified code here
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        pollListApi();
    }


    private void filter(String text) {
        Log.d("polllist..", text);
        ArrayList<PollListPojo> filteredlist = new ArrayList<>();
        for (PollListPojo item : listdata) {
            Log.d("polllist..", item.toString());

                if (item.getCreatedBy().toLowerCase().contains(text.toLowerCase()) ||
                        item.getPollQues().toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(item);
                }

        }


        if (filteredlist.isEmpty()) {
//            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("polllist...filter", filteredlist.toString());
            pollAdapter.filterList(filteredlist);
        }
    }


    @Override
    public void onListItemClicked(String itemName) {
        pollListApi();
    }
}