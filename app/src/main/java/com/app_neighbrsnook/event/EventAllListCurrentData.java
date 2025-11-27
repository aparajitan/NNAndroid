package com.app_neighbrsnook.event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.adapter.GroupListAdapter;
import com.app_neighbrsnook.businessModule.CreateBusinessPageActivity;
import com.app_neighbrsnook.group.GroupActivity;
import com.app_neighbrsnook.pojo.EventlistPojo;
import com.app_neighbrsnook.pojo.GroupListPojo;
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

public class EventAllListCurrentData extends AppCompatActivity implements EventListAdapter.NewRequest {
    List<CreateEventModule> createEventModules = new ArrayList<>();
    private DAO mydao;
    ImageView img_back;
    CardView card_first;
    CardView card_diwali;
    ImageView img_third_card,cancel_iv;
    RecyclerView rv_event;
    Activity activity;
    Context context;
    ImageView img_search;
    RecyclerView recy_my_events,recy_past,recy_upcoming;
    SharedPrefsManager sm;
    int id;
    boolean isVerifiedUser = true;

    HashMap<String, Object> hm = new HashMap<>();
    FrameLayout frm_replace;
    ViewPager viewPager;
    public TabLayout tabLayout;
    FrameLayout card_current,card_upcoming,card_past;
    Boolean is_past = false;
    Boolean is_current = false;
    Boolean is_upcoming =false;
    TextView tv_past,tv_current,tv_upcoming;
    FrameLayout frm_current,frm_upcoming,frm_past,img_create_event;
    String  st_current,st_upcoming,st_past,st_neighbrhood, nbr, value;
    TextView neighbrhoo_tv_id;
    EditText search_et;
    RelativeLayout search_rl;
    int fromuserid;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        search_rl = findViewById(R.id.search_rl);
        search_et = findViewById(R.id.search_et);

        cancel_iv = findViewById(R.id.cancel_iv);

        card_past = findViewById(R.id.past_card_id);
        card_current = findViewById(R.id.current_card_id);
        card_upcoming = findViewById(R.id.upcoming_card_id);
        neighbrhoo_tv_id = findViewById(R.id.neighbrhoo_tv_id);

        frm_replace=findViewById(R.id.frame_filter_id);
        viewPager=findViewById(R.id.filters_view_pager);
        tabLayout=findViewById(R.id.post_property_tab_layout);
        // tab_filters.addTab(tab_filters.newTab().setText("Basic Details"));
        img_back=findViewById(R.id.img_back);
        recy_my_events=findViewById(R.id.recy_my_events);
        mydao = Database.createDBInstance(this).getDao();
//        Intent intent = getIntent();
//        if (getIntent().getExtras() != null) {
//
//        }
        Intent i = getIntent();

        fromuserid = i.getIntExtra("user_id",0);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("neighbrhood");

            //The key argument here must match that used in the other activity
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
            public void onClick(View v) {
                search_rl.setVisibility(View.VISIBLE);
                search_et.requestFocus();
            }
        });
        recy_my_events.setLayoutManager(new GridLayoutManager(context, 3, RecyclerView.VERTICAL, false));
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        if (value!=null) {
            if (value.equals(sm.getString("neighbrhood"))) {
                hm.put("neighbrhood", sm.getString("neighbrhood"));
            } else if (!value.equals("drawar")){
                hm.put("neighbrhood", value);
            }
        }
        else {
            hm.put("userid", fromuserid);
            hm.put("eventuserlist", fromuserid);
        }
//        Log.d("sfdfsfsfs",value);
        eventListApi(hm);
        img_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVerifiedUser)
                {
                    startActivity(new Intent(EventAllListCurrentData.this,CreateEvent.class));

                }else {
                    GlobalMethods.getInstance(EventAllListCurrentData.this).globalDialog(EventAllListCurrentData.this, getString(R.string.unverified_msg));


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
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(context,MainActivity.class));
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
            public void onClick (View v) {
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

//        GlobalMethods.progressDialog(true, EventAllListCurrentData.this);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String str = formatter.format(date);
        //hm.put("eventuserlist", str);
        //by arsad hm.put("neighbrhood", sm.getString("neighbrhood"));

//        AppCommon.getInstance(BusinessActivity.this).setNonTouchableFlags(BusinessActivity.this);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<EventListPojos> call = service.eventlistall("eventlist",hm);
        call.enqueue(new Callback<EventListPojos>() {
            @Override
            public void onResponse(Call<EventListPojos> call, Response<EventListPojos> response) {
                EventListPojos rootObject= response.body();
               // Toast.makeText(EventAllListCurrentData.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                String status = response.body().getStatus();

                if(response.body().getVerfied_msg().equals("User Verification is not completed!"))
                {
                    isVerifiedUser = false;
                }
                if(status.equals("failed")) {
                    Toast.makeText(EventAllListCurrentData.this, response.body().message, Toast.LENGTH_SHORT).show();

                }
                else {
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
                    listPojoNbdata3 = response.body().getEventFuture();
                    //      Log.d("status----", "" + listPojoNbdata.toString());
                    adapter = new EventListAdapter(listPojoNbdata1, EventAllListCurrentData.this,isVerifiedUser);
                    recy_my_events.setAdapter(adapter);

                    dialog.dismiss();

                }

            }
            @Override
            public void onFailure(Call<EventListPojos> call, Throwable t) {
                if (t.toString().contains("timeout")){
                    Toast.makeText(EventAllListCurrentData.this, "Something went wrong", Toast.LENGTH_SHORT).show();


                }else if (t.toString().contains("Unable to resolve host")){
                    Toast.makeText(EventAllListCurrentData.this, "No internet", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(EventAllListCurrentData.this, "Something seems to have gone wrong.Please try again", Toast.LENGTH_SHORT).show();


                }

                //   Toast.makeText(EventAllListCurrentData.this, t.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                Log.d("res", t.getMessage());
            }
        });
    }
    private void filter(String text) {
        ArrayList<EventListPojoNbdata> filteredlist = new ArrayList<EventListPojoNbdata>();
        for (EventListPojoNbdata item : listPojoNbdata1) {
            if ((item.getTitle().toLowerCase().contains(text.toLowerCase()))) {
                filteredlist.add(item);
            }
            // Log.d(String.valueOf(listdata),"listdata");
        }
//        item.getUsername().toLowerCase().contains(text.toLowerCase())||
//                item.getDescription().toLowerCase().contains(text.toLowerCase())

        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(filteredlist);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        eventListApi(hm);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EventAllListCurrentData.this, MainActivity.class));
        finishAffinity();
    }
}