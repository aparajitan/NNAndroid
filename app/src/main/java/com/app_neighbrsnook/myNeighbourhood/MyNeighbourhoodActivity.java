package com.app_neighbrsnook.myNeighbourhood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.PostActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.NeighbourhoodCategoryAdapter;
import com.app_neighbrsnook.businessModule.BusinessActivity;
import com.app_neighbrsnook.event.EventAllListCurrentData;
import com.app_neighbrsnook.group.GroupActivity;
import com.app_neighbrsnook.model.SubsModel;
import com.app_neighbrsnook.model.neighbrhood.NearbyModel;
import com.app_neighbrsnook.model.neighbrhood.NearestNeighbrhood;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pollModule.PollActivity;
import com.app_neighbrsnook.suggestion.SuggestionActivity;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PostActivityMyNeighbrhood;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyNeighbourhoodActivity extends AppCompatActivity implements NeighbourhoodCategoryAdapter.NeighbrnookInterface {
    RecyclerView category_rv, nearby_neighbourhood_rv;
    TextView titleTv, total_neighborhood_member, owner_nebourhood_name, owner_member_tv;
    ImageView back_btn;
    SharedPrefsManager sm;
    LinearLayout member_ll, group_ll, event_ll, poll_ll, business_ll, suggestion_ll, lnrPostList;
    TextView member_count_tv, group_count_tv, event_count_tv, poll_count_tv, business_count_tv, suggestion_count_tv, postCountNumber, my_neighbourhood;
    NeighbourhoodCategoryAdapter neighbourhoodCategoryAdapter;
    List<NearbyModel> list = new ArrayList<>();
    List<NearestNeighbrhood> list1 = new ArrayList<>();
    HashMap<String, Object> hm = new HashMap<>();
    LinearLayout neighbrhood_ll;
    ImageView search_imageview, add_imageview;
    String type = "own";
    String neighboursId = "own";
    boolean isUserVerified = true;
    RelativeLayout banner_rl;
    String ownerNeighbrhood, ownerNghdId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_neighbourhood);
        sm = new SharedPrefsManager(this);
        titleTv = findViewById(R.id.title);
        back_btn = findViewById(R.id.back_btn);
        titleTv.setText("My Neighbourhood");
        nearby_neighbourhood_rv = findViewById(R.id.nearby_neighbourhood_rv);
        member_count_tv = findViewById(R.id.member_count_tv);
        group_count_tv = findViewById(R.id.group_count_tv);
        event_count_tv = findViewById(R.id.event_count_tv);
        poll_count_tv = findViewById(R.id.poll_count_tv);
        business_count_tv = findViewById(R.id.business_count_tv);
        postCountNumber = findViewById(R.id.postCountNumber);
        // suggestion_count_tv = findViewById(R.id.suggestion_count_tv);
        my_neighbourhood = findViewById(R.id.my_neighbourhood);
        banner_rl = findViewById(R.id.banner_rl);
        member_ll = findViewById(R.id.member_ll);
        group_ll = findViewById(R.id.group_ll);
        event_ll = findViewById(R.id.event_ll);
        poll_ll = findViewById(R.id.poll_ll);
        business_ll = findViewById(R.id.business_ll);
        neighbrhood_ll = findViewById(R.id.neighbrhood_ll);
        total_neighborhood_member = findViewById(R.id.total_neighborhood_member);
        owner_nebourhood_name = findViewById(R.id.owner_nebourhood_name);
        lnrPostList = findViewById(R.id.postList);
        owner_member_tv = findViewById(R.id.owner_member_tv);
        add_imageview = findViewById(R.id.add_imageview);
        add_imageview.setVisibility(View.GONE);
        search_imageview = findViewById(R.id.search_imageview);
        search_imageview.setVisibility(View.GONE);
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("neighbrhood", (sm.getString(("neighbrhood"))));

        neighboursId = sm.getString("neighbrhood");
        Log.d("hmsss", hm.toString());
        myneighborhoodApi(hm);
        nearby_neighbourhood_rv.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        member_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
                    if (type.equals("nearby")) {
                        Intent memberIntent = new Intent(MyNeighbourhoodActivity.this, NeighbrhoodMemberActivity.class);
                        memberIntent.putExtra("neighbrhood", neighboursId);
                        startActivity(memberIntent);
                    } else {
                        Intent memberIntent = new Intent(MyNeighbourhoodActivity.this, NeighbrhoodMemberActivity.class);
                        memberIntent.putExtra("neighbrhood", sm.getString("neighbrhood"));
                        startActivity(memberIntent);
                    }
                } else {
                    GlobalMethods.getInstance(MyNeighbourhoodActivity.this).globalDialog(MyNeighbourhoodActivity.this, getString(R.string.unverified_msg));

                }

            }
        });
        neighbrhood_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(isUserVerified) {
                hm.put("neighbrhood", ownerNghdId);
                Log.d("sssss", ownerNeighbrhood);
                hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                myneighborhoodApi(hm);
                neighbrhood_ll.setVisibility(View.GONE);
                type = "owner";
//                }
            }
        });
        group_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
                    if (type.equals("nearby")) {
                        Intent memberIntent = new Intent(MyNeighbourhoodActivity.this, GroupActivity.class);
                        memberIntent.putExtra("neighbrhood", neighboursId);
                        startActivity(memberIntent);


                    } else {
                        Intent memberIntent = new Intent(MyNeighbourhoodActivity.this, GroupActivity.class);
                        memberIntent.putExtra("neighbrhood", sm.getString("neighbrhood"));
                        startActivity(memberIntent);
                    }
                } else {
                    GlobalMethods.getInstance(MyNeighbourhoodActivity.this).globalDialog(MyNeighbourhoodActivity.this, getString(R.string.unverified_msg));

                }


            }
        });
        event_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
                    if (type.equals("nearby")) {
                        Intent memberIntent = new Intent(MyNeighbourhoodActivity.this, EventAllListCurrentData.class);
                        memberIntent.putExtra("neighbrhood", neighboursId);
                        Log.d("dksjsjs", neighboursId);
                        startActivity(memberIntent);

                    } else {
                        Intent memberIntent = new Intent(MyNeighbourhoodActivity.this, EventAllListCurrentData.class);
                        memberIntent.putExtra("neighbrhood", sm.getString("neighbrhood"));
                        startActivity(memberIntent);
                    }
                } else {
                    GlobalMethods.getInstance(MyNeighbourhoodActivity.this).globalDialog(MyNeighbourhoodActivity.this, getString(R.string.unverified_msg));

                }
            }
        });
        poll_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
                    if (type.equals("nearby")) {
                        Intent memberIntent = new Intent(MyNeighbourhoodActivity.this, PollActivity.class);
                        memberIntent.putExtra("neighbrhood", neighboursId);
                        startActivity(memberIntent);

                    } else {
                        Intent memberIntent = new Intent(MyNeighbourhoodActivity.this, PollActivity.class);
//                    memberIntent.putExtra("neighbrhood", sm.getString("neighbrhood"));
                        memberIntent.putExtra("neighbrhood", sm.getString("neighbrhood"));
                        startActivity(memberIntent);
                    }
                } else {
                    GlobalMethods.getInstance(MyNeighbourhoodActivity.this).globalDialog(MyNeighbourhoodActivity.this, getString(R.string.unverified_msg));
                }
            }
        });
        business_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
                    if (type.equals("nearby")) {
                        Intent memberIntent = new Intent(MyNeighbourhoodActivity.this, BusinessActivity.class);
                        memberIntent.putExtra("title", neighboursId);
                        startActivity(memberIntent);
                    } else {
                        Intent memberIntent = new Intent(MyNeighbourhoodActivity.this, BusinessActivity.class);
                        memberIntent.putExtra("title", sm.getString("neighbrhood"));
                        startActivity(memberIntent);
                    }
                } else {
                    GlobalMethods.getInstance(MyNeighbourhoodActivity.this).globalDialog(MyNeighbourhoodActivity.this, getString(R.string.unverified_msg));
                }
            }
        });
        lnrPostList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
                    if (type.equals("nearby")) {
                        Intent memberIntent = new Intent(MyNeighbourhoodActivity.this, PostActivity.class);
                        memberIntent.putExtra("neighbrhood", neighboursId);
                        memberIntent.putExtra("source", "myneighbrhood");
                        // Log.d("dksjsjs", neighboursId);
                        startActivity(memberIntent);
                    } else {
                        Intent memberIntent = new Intent(MyNeighbourhoodActivity.this, PostActivity.class);
                        memberIntent.putExtra("neighbrhood", sm.getString("neighbrhood"));
                        memberIntent.putExtra("source", "myneighbrhood");
                        startActivity(memberIntent);
                    }
                } else {
                    GlobalMethods.getInstance(MyNeighbourhoodActivity.this).globalDialog(MyNeighbourhoodActivity.this, getString(R.string.unverified_msg));

                }
            }
        });

    }

    private void openDialog(int pos, String type) {

    }


    private void myneighborhoodApi(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<NearbyModel> call = service.myneighborhood("myneighborhood", hm);
        call.enqueue(new Callback<NearbyModel>() {
            @Override
            public void onResponse(Call<NearbyModel> call, Response<NearbyModel> response) {
                String status = response.body().getStatus();
                List<NearbyModel> list2 = new ArrayList<>();
                ownerNeighbrhood = response.body().getOwnerNeighbrhood().get(0).getName();
                ownerNghdId = response.body().getOwnerNeighbrhood().get(0).getId();
                if (status.equals("failed")) {
                } else {
                    list1 = response.body().getNearestNeighbrhood();
                    NearbyModel nearbyModel = new NearbyModel();
                    nearbyModel = response.body();
                    if (nearbyModel.getVerfied_msg().equals("User Verification is completed!")) {
                        isUserVerified = true;
                    } else {
                        isUserVerified = false;
                    }
                    member_count_tv.setText(nearbyModel.getMembers());
                    group_count_tv.setText(nearbyModel.getGroups());
                    event_count_tv.setText(nearbyModel.getEvents());
                    poll_count_tv.setText(nearbyModel.getPolls());
                    business_count_tv.setText(nearbyModel.getBusiness());
                    my_neighbourhood.setText(nearbyModel.getNeighbrhood());
                    postCountNumber.setText(nearbyModel.getPost());
                    // suggestion_count_tv.setText(nearbyModel.getSuggestions());
                    total_neighborhood_member.setText("Total members" + " " + nearbyModel.getTotmember());
                    nearby_neighbourhood_rv.setAdapter(new NeighbourhoodCategoryAdapter(list1, isUserVerified, MyNeighbourhoodActivity.this));
                    owner_nebourhood_name.setText(nearbyModel.getOwnerNeighbrhood().get(0).getName());
                    owner_member_tv.setText(nearbyModel.getOwnerNeighbrhood().get(0).getMember() + " " + "Members");
                    if (nearbyModel.getCoverimageandroid().isEmpty()) {

                    } else {


                    }
                }

            }

            @Override
            public void onFailure(Call<NearbyModel> call, Throwable t) {
                Toast.makeText(MyNeighbourhoodActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }


    private void subscribeApi(String neighbrhood, int subUnSub) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("neighbrhood", neighbrhood);
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("sub_stat", subUnSub);
        Log.d("hm", hm.toString());
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<SubsModel> call = service.neighborhoodSubscribeApi("subscribe", hm);
        call.enqueue(new Callback<SubsModel>() {
            @Override
            public void onResponse(Call<SubsModel> call, Response<SubsModel> response) {
                neighboursId = sm.getString("neighbrhood");
                hm.put("neighbrhood", sm.getString("neighbrhood"));
                hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                myneighborhoodApi(hm);

            }

            @Override
            public void onFailure(Call<SubsModel> call, Throwable t) {
                Toast.makeText(MyNeighbourhoodActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });


    }

    @Override
    public void onSubscribeClick(String neighbrhood) {
        subscribeApi(neighbrhood, 1);
        my_neighbourhood.setText(neighbrhood);
    }

    @Override
    public void onUnSubscribeClick(String neighbrhood) {
        subscribeApi(neighbrhood, 0);
    }

    @Override
    public void onNeighbrhoodDetail(String id, String status) {
        if (status.equals("1")) {
            neighbrhood_ll.setVisibility(View.VISIBLE);
        }
        neighboursId = id;
        hm.put("neighbrhood", id);
        //  Log.d("neighbrhood...", name);
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        myneighborhoodApi(hm);
        type = "nearby";
    }

    public interface ListPageListener {

        void businessList(String neighbrhood);

        void groupListpage(String neighbrhood);

        void eventList(String neighbrhood);
    }
}