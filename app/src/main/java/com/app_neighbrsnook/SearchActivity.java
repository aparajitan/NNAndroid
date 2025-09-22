package com.app_neighbrsnook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.app_neighbrsnook.adapter.BusinessAdapter;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.BusinessListPojo;
import com.app_neighbrsnook.pojo.Listdatum;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    RecyclerView rv;
    EditText search_et;
    List<Listdatum> listdata = new ArrayList<>();
    BusinessAdapter businessAdapter;
    SharedPrefsManager sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        EditText finalSearch_et = search_et;
        List<Integer> list = new ArrayList<>();
        sm = new SharedPrefsManager(this);
        int userid = Integer.parseInt(sm.getString("user_id"));
        list.add(userid);
//        searchApi(list);
//
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
//                filter(editable.toString());
//
            }
        });


        rv = findViewById(R.id.rv);
        search_et = findViewById(R.id.search_et);
        rv.setLayoutManager(new LinearLayoutManager(this));


    }

    private void searchApi(HashMap<String, Object> hm) {


//        AppCommon.getInstance(BusinessActivity.this).setNonTouchableFlags(BusinessActivity.this);
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<BusinessListPojo> call = service.searchApi("search","abc1239", hm);
            call.enqueue(new Callback<BusinessListPojo>() {
                @Override
                public void onResponse(Call<BusinessListPojo> call, Response<BusinessListPojo> response) {

                    String status = response.body().getStatus();
                    if(status.equals("failed")) {
//                    Toast.makeText(BusinessActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        listdata = response.body().getListdata();
//                        businessAdapter = new BusinessAdapter(listdata, SearchActivity.this);
                        rv.setAdapter(businessAdapter);
                    }

                }

                @Override
                public void onFailure(Call<BusinessListPojo> call, Throwable t) {
                    Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("res", t.getMessage());
                }
            });
        }


//    private void filter(String text) {
//        ArrayList<BusinessModel> filteredlist = new ArrayList<BusinessModel>();
//
//
//
//            for (Listdatum item : businessList) {
//                if (item.get().toLowerCase().contains(text.toLowerCase())) {
//                    filteredlist.add(item);
//                }
//
//        }




//        if (filteredlist.isEmpty()) {
//            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
//        } else {
//            businessAdapter.filterList(filteredlist);
//        }
    }
