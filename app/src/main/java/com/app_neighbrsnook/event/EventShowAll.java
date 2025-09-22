package com.app_neighbrsnook.event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.utils.GlobalMethods;
import com.google.android.material.tabs.TabLayout;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.EventListAdapter;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.model.CreateEventModule;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.EventListPojoNbdata;
import com.app_neighbrsnook.pojo.EventListPojos;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventShowAll extends AppCompatActivity implements EventListAdapter.NewRequest{
    List<CreateEventModule> createEventModules = new ArrayList<>();
    private DAO mydao;
    ImageView img_back;
    CardView card_first;
    CardView card_diwali;
    ImageView img_third_card;
    RecyclerView rv_event;
    Activity activity;
    Context context;
    boolean isVerifiedUser = true;

    ImageView img_search;
    RecyclerView recy_my_events,recy_past,recy_upcoming;
    SharedPrefsManager sm;
    int id;
    HashMap<String, Object> hm = new HashMap<>();
    FrameLayout frm_replace,img_create_event;
    ViewPager viewPager;
    public TabLayout tabLayout;
    FrameLayout card_current,card_upcoming,card_past;
    Boolean is_past = false;
    Boolean is_current = false;
    Boolean is_upcoming =false;
    TextView tv_past,tv_current,tv_upcoming;
    FrameLayout frm_current,frm_upcoming,frm_past;
    String  st_current,st_upcoming,st_past,st_neighbrhood, nbr;
    TextView neighbrhoo_tv_id;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_show_all);
        context=activity=this;
        setContentView(R.layout.activity_my_events);
        frm_current=findViewById(R.id.current_id);
        frm_upcoming=findViewById(R.id.upcoming_id);
        frm_past=findViewById(R.id.past_id);
        recy_past=findViewById(R.id.rec_past);
        recy_past=findViewById(R.id.recy_upcoming);
        tv_past=findViewById(R.id.id_past);
        tv_current=findViewById(R.id.id_current);
        tv_upcoming=findViewById(R.id.id_upcoming);
        card_past=findViewById(R.id.past_card_id);
        card_current=findViewById(R.id.current_card_id);
        card_upcoming=findViewById(R.id.upcoming_card_id);
        neighbrhoo_tv_id=findViewById(R.id.neighbrhoo_tv_id);
        frm_replace=findViewById(R.id.frame_filter_id);
        viewPager=findViewById(R.id.filters_view_pager);
        tabLayout=findViewById(R.id.post_property_tab_layout);
        // tab_filters.addTab(tab_filters.newTab().setText("Basic Details"));
        img_back=findViewById(R.id.img_back);
        recy_my_events=findViewById(R.id.recy_my_events);
        mydao = Database.createDBInstance(this).getDao();
      //  WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        Intent intent = getIntent();
        if(getIntent().getExtras() != null) {
            nbr = intent.getStringExtra("neighbrhood");
        }
        else{
            nbr = "";
        }
        sm = new SharedPrefsManager(this);
        // photo_recycler_view.setLayoutManager(new GridLayoutManager(context,3,RecyclerView.VERTICAL,false));
        img_search=findViewById(R.id.img_event_search);
        img_create_event=findViewById(R.id.create_event_id);
        card_first=findViewById(R.id.card_view_first);
        card_diwali=findViewById(R.id.card_diwali);
        img_third_card=findViewById(R.id.img_third_id);
        rv_event=findViewById(R.id.events_recycler_view);
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventShowAll.this, UserViewEvent.class));
            }
        });
        recy_my_events.setLayoutManager(new GridLayoutManager(context,3,RecyclerView.VERTICAL,false));
        hm.put("userid",Integer.parseInt(sm.getString("user_id")));
        hm.put("eventuserlist",Integer.parseInt(sm.getString("user_id")));
        // hm.put("neighbrhood", nbr);
        eventListApi(hm);
        img_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVerifiedUser) {
                    startActivity(new Intent(EventShowAll.this, CreateEvent.class));
                } else {
                    GlobalMethods.getInstance(EventShowAll.this).globalDialog(EventShowAll.this, getString(R.string.unverified_msg));

                }            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        card_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frm_current.setVisibility(View.GONE);
                frm_past.setVisibility(View.GONE);
                frm_upcoming.setVisibility(View.GONE);
                is_current = true;
                is_upcoming = false;
                is_past = false;
                card_current.setBackgroundResource(R.drawable.select_background_event_list);
                card_past.setBackgroundResource(R.drawable.card_background_current);
                card_upcoming.setBackgroundResource(R.drawable.card_background_current);
                frm_current.setVisibility(View.VISIBLE);
                // recy_my_events
                adapter.updateList(listPojoNbdata1);

            }
        });
        card_upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frm_current.setVisibility(View.GONE);
                frm_past.setVisibility(View.GONE);
                frm_upcoming.setVisibility(View.GONE);
                is_upcoming = true;
                // propert_type="";
                is_current = false;
                is_past = false;
                card_upcoming.setBackgroundResource(R.drawable.select_background_event_list);
                card_past.setBackgroundResource(R.drawable.card_background_current);
                card_current.setBackgroundResource(R.drawable.card_background_current);
                frm_current.setVisibility(View.VISIBLE);
                adapter.updateList(listPojoNbdata3);
            }
        });
        card_past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frm_current.setVisibility(View.GONE);
                frm_past.setVisibility(View.GONE);
                frm_upcoming.setVisibility(View.GONE);
                is_past = true;
                is_current = false;
                is_upcoming = false;
                card_past.setBackgroundResource(R.drawable.select_background_event_list);
                card_upcoming.setBackgroundResource(R.drawable.card_background_current);
                card_current.setBackgroundResource(R.drawable.card_background_current);
                frm_current.setVisibility(View.VISIBLE);
                adapter.updateList(listPojoNbdata2);

                //frm_other.setBackgroundResource(R.drawable.employe_circle_rounded);
                // }
            }
        });
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        }*/
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);


    }
    List<EventListPojoNbdata> listPojoNbdata = new ArrayList<>();
    private ArrayList<EventListPojoNbdata> filterList(String running, String willEvent){
        ArrayList<EventListPojoNbdata> arrayList = new ArrayList<>();
        for (EventListPojoNbdata data : listPojoNbdata){
            if (data.getIseventrunning().equals(running) && data.getWilleventstart().equals(willEvent)){
                arrayList.add(data);
            }
        }
        return arrayList;
    }

    List<EventListPojoNbdata> listPojoNbdata1 = new ArrayList<>();
    List<EventListPojoNbdata> listPojoNbdata2 = new ArrayList<>();
    List<EventListPojoNbdata> listPojoNbdata3 = new ArrayList<>();

    EventListAdapter adapter;
    private void eventListApi(HashMap<String, Object> hm) {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();


        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String str = formatter.format(date);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<EventListPojos> call = service.eventlistall("eventlist",hm);

        call.enqueue(new Callback<EventListPojos>() {
            @Override
            public void onResponse(Call<EventListPojos> call, Response<EventListPojos> response) {
                EventListPojos rootObject= response.body();
                // Toast.makeText(EventAllListCurrentData.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                String status = response.body().getStatus();
                if (response.body().getVerfied_msg().equals("User Verification is not completed!")) {
                    isVerifiedUser = false;
                }
                if(status.equals("success")) {
                    dialog.dismiss();
                    st_past= String.valueOf(rootObject.getEvencountPast());
                    st_current= String.valueOf(rootObject.getEventcountCurrent());
                    st_upcoming= String.valueOf(rootObject.getEventcountFuture());
                    st_neighbrhood= String.valueOf(rootObject.getNeighbrhood());
                    tv_past.setText(st_past);
                    tv_current.setText(st_current);
                    tv_upcoming.setText(st_upcoming);
                    neighbrhoo_tv_id.setText(st_neighbrhood);
                    listPojoNbdata1 = response.body().getEventCurrent();
                    listPojoNbdata2 = response.body().getEventPast();
                    listPojoNbdata3 = response.body().getEventFuture();
                    //      Log.d("status----", "" + listPojoNbdata.toString());
                    adapter = new EventListAdapter(listPojoNbdata1, EventShowAll.this,isVerifiedUser);
                    recy_my_events.setAdapter(adapter);

                }
                else {
                    Toast.makeText(EventShowAll.this, response.body().message, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            }
            @Override
            public void onFailure(Call<EventListPojos> call, Throwable t) {
                Toast.makeText(EventShowAll.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                Log.d("res", t.getMessage());
            }
        });
    }

}