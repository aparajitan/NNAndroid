package com.app_neighbrsnook;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.StatePojo;
import com.app_neighbrsnook.post.CreatePostActivity;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUsActivity extends AppCompatActivity {
    ImageView img_back,img_plus;
    FrameLayout frm_show_password;
    SharedPrefsManager sm;
    TextView about_us_detail_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        sm = new SharedPrefsManager(this);
        img_back=findViewById(R.id.img_back);
        img_plus=findViewById(R.id.plus_create_id);
        about_us_detail_tv = findViewById(R.id.about_us_detail_tv);
        contactApi();
        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutUsActivity.this,CreatePostActivity.class));
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void contactApi() {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<StatePojo> call = service.aboutusapi("aboutus",Integer.parseInt(sm.getString("user_id")),"abc1239");
        call.enqueue(new Callback<StatePojo>() {
            @Override
            public void onResponse(Call<StatePojo> call, Response<StatePojo> response) {

                StatePojo statePojo = response.body();
                about_us_detail_tv.setText(response.body().getNbdata().get(0).getAboutus());
//                for(int i=1; i<= statePojo.getNbdata().size(); i++) {
//                    about_us_detail_tv.setText(statePojo.getNbdata().get(i).getAboutus());
//                }
            }

            @Override
            public void onFailure(Call<StatePojo> call, Throwable t) {
                Toast.makeText(AboutUsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }

    }
