package com.app_neighbrsnook.marketPlace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.MarketPlaceChatListAdapterr;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.apiService.UrlClass;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.GroupDetailsbyNamePojo;
import com.app_neighbrsnook.pojo.GroupListPojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.ChatMarketPlacePojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.app_neighbrsnook.pojo.marketPlacePojo.UsersListChatsPojo;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListMarketPlaceActivity extends AppCompatActivity implements MarketPlaceChatListAdapterr.NewRequest{
    Context context;
    SharedPrefsManager sm;
    Activity activity;
    List<ChatMarketPlacePojo> list1 = new ArrayList<>();
    MarketPlaceChatListAdapterr marketPlaceChatListAdapterr;
    RecyclerView recUserList;
    String  stSenderId,stProductId;
    TextView title;
    ImageView search_imageview,add_imageview,back_btn;
   // List<ChatMarketPlacePojo> usersListChatsPojo = new ArrayList<>();

    int product_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        context=activity=this;
        sm = new SharedPrefsManager(this);
        recUserList=findViewById(R.id.item_list_rv);
        title=findViewById(R.id.title);
        search_imageview=findViewById(R.id.search_imageview);
        add_imageview=findViewById(R.id.add_imageview);
        back_btn=findViewById(R.id.back_btn);
        search_imageview.setVisibility(View.GONE);
        add_imageview.setVisibility(View.GONE);
        title.setText("Chat List");
        Intent intent = getIntent();
        product_id = intent.getIntExtra("id",0);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
       // linearLayoutManager.setReverseLayout(true);
        recUserList.setLayoutManager(linearLayoutManager);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        directMsgList();
    }
    private void directMsgList() {

        String url = "";//UrlClass.SELECT_NEIGHBRHOOD+"&lati="+latitude+"&longi="+longitude+"&areas="+address;
        Boolean isInternetConnection = GlobalMethods.checkConnection(context);
        if(isInternetConnection){
            url = UrlClass.MARKET_PLACE_CHAT_LIST+ PrefMananger.GetLoginData(context).getId() +"?p_id="+(product_id);

            ApiExecutor.getApiService().getChatListData(url).enqueue(new Callback<UsersListChatsPojo>() {
                @Override
                public void onResponse(Call<UsersListChatsPojo> call, Response<UsersListChatsPojo> response) {
                        list1=response.body().getChats();
                     //   list1.get(0).get
                        marketPlaceChatListAdapterr = new MarketPlaceChatListAdapterr(list1,ChatListMarketPlaceActivity.this);
                        recUserList.setAdapter(marketPlaceChatListAdapterr);
                }

                @Override
                public void onFailure(Call<UsersListChatsPojo> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("res", t.getMessage());
                }
            });
        }else {
            GlobalMethods.getInstance(context).globalDialog(this, "     No internet connection."     );
        }
    }

    private void readStatusUpdate(HashMap<String, Object> hm) {
//        dialog.show();
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if(isInternetConnection) {

            APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
            Call<CommonPojoSuccess> call = service.checkStatusUpdate(hm);
            call.enqueue(new Callback<CommonPojoSuccess>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<CommonPojoSuccess> call, Response<CommonPojoSuccess> response) {
                   // wishlistResponse=response.body();
                    String status = response.body().getStatus();
                    try {
                        if(status.equals(200)) {
                            //    FancyToast.makeText(getApplicationContext(), "Your item has been saved.", Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                            Toast.makeText(ChatListMarketPlaceActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                        else if (status.equals(404)){
                            Toast.makeText(ChatListMarketPlaceActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                        }
                    }catch (Exception e){

                    }
                    //dialog.dismiss();
                }

                @Override
                public void onFailure(Call<CommonPojoSuccess> call, Throwable t) {
                    //  dialog.dismiss();
                }
            });
        }else {
            GlobalMethods.getInstance(context).globalDialog(this, "     No internet connection."     );
        }
    }
    @Override
    public void onClickDetail(int position) {

        ChatMarketPlacePojo list=list1.get(position);
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("product_id",String.valueOf(list.getProductId()));
        hm.put("sender_id",String.valueOf(list.getSenderId()));
        hm.put("receiver_id",Integer.parseInt(sm.getString("user_id")));
        readStatusUpdate(hm);

    }
}