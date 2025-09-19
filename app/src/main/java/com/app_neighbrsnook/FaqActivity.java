package com.app_neighbrsnook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.adapter.FaqAdapter;
import com.app_neighbrsnook.model.faq.Faq;
import com.app_neighbrsnook.model.faq.Faqdatum;
import com.app_neighbrsnook.model.notification.NotificationModel;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaqActivity extends AppCompatActivity {
    RecyclerView notification_rv;
    ImageView back_btn, search_imageview, add_imageview;
    TextView titleTv;
    FaqAdapter faqAdapter;
    List<NotificationModel> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        notification_rv = findViewById(R.id.faq_rv);
        notification_rv.setLayoutManager(new LinearLayoutManager(this));

//        notification_rv.setAdapter(faqAdapter);
        titleTv = findViewById(R.id.title);
        back_btn = findViewById(R.id.back_btn);
        titleTv.setText("FAQ");
        search_imageview = findViewById(R.id.search_imageview);
        search_imageview.setVisibility(View.INVISIBLE);
        add_imageview = findViewById(R.id.add_imageview);
        add_imageview.setVisibility(View.INVISIBLE);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        faqApi();
    }
    private List<NotificationModel> getData()
    {
        list.add(new NotificationModel("Does your app use voice enabled features?", "This is currently not supported in this version of app", "business",R.color.member, R.drawable.garden_suggestion));
        list.add(new NotificationModel("Does your app use voice enabled features?", "This is currently not supported in this version of app", "group",R.color.group, R.drawable.garden_suggestion));
        list.add(new NotificationModel("Does your app use voice enabled features?", "This is currently not supported in this version of app", "event",R.color.event, R.drawable.garden_suggestion));
         return list;
    }

    private void faqApi() {

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<Faq> call = service.faqApi("faq");
        call.enqueue(new Callback<Faq>() {
            @Override
            public void onResponse(Call<Faq> call, Response<Faq> response) {
                List<Faqdatum> list = response.body().getFaqdata();
                faqAdapter = new FaqAdapter(list);
                notification_rv.setAdapter(faqAdapter);
            }
            @Override
            public void onFailure(Call<Faq> call, Throwable t) {

//                AppCommon.getInstance(PlaceOrderActivity.this).clearNonTouchableFlags(PlaceOrderActivity.this);
                Toast.makeText(FaqActivity.this, "Data found", Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

}