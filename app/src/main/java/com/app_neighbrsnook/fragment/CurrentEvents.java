package com.app_neighbrsnook.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.model.CreateEventModule;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CurrentEvents extends Fragment {


    List<CreateEventModule> createEventModules = new ArrayList<>();
    private DAO mydao;
    ImageView img_back,img_create_event;
    CardView card_first;
    CardView card_diwali;
    ImageView img_third_card;
    RecyclerView rv_event;
    Activity activity;
    Context context;
    ImageView img_search;
    RecyclerView recy_my_events;
    SharedPrefsManager sm;
    Context id;
    HashMap<String, Object> hm = new HashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_current_events, container, false);
        Context intent = getContext();
       // id = intent.getApplicationContext("id",0);
       // sm = new SharedPrefsManager(getActivity());



        return view;
    }

    /*
    private void eventListApi() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String str = formatter.format(date);

        hm.put("userid",Integer.parseInt(sm.getString("user_id")));
        hm.put("eventuserlist", str);
        // hm.put("neighbrhood", sm.getString("neighbrhood"));

//        AppCommon.getInstance(BusinessActivity.this).setNonTouchableFlags(BusinessActivity.this);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<GroupResponseListPojo> call = service.eventlistall("showeventlistdetails", str,hm);
        call.enqueue(new Callback<GroupResponseListPojo>() {
            @Override
            public void onResponse(Call<GroupResponseListPojo> call, Response<GroupResponseListPojo> response) {
                Toast.makeText(getActivity(), response.body().toString(), Toast.LENGTH_SHORT).show();

                String status = response.body().getStatus();
                if(status.equals("failed")) {
                    Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                }
                else {
                    List<EventListPojoNbdata> nbdata = response.body().getNbdata();
                    Log.d("status----", "" + nbdata.toString());
                    recy_my_events.setAdapter(new EventListAdapter(nbdata, (EventListAdapter.NewRequest) CurrentEvents.this));
                }

            }

            @Override
            public void onFailure(Call<GroupResponseListPojo> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }


    */

}