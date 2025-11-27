package com.app_neighbrsnook.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.utils.GlobalMethods;
import com.google.firebase.firestore.FirebaseFirestore;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.ChatUserListAdapter;
import com.app_neighbrsnook.adapter.DmMsgListAdapter;
import com.app_neighbrsnook.chat.Chat;
import com.app_neighbrsnook.model.notification.NotificationModel;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.directmessagelist.DirectMessagePojo;
import com.app_neighbrsnook.pojo.directmessagelist.Nbdatum;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DmFragment extends Fragment {

    ImageView searchImageView, addImageView, back_btn;
    TextView titleTv;
    ChatUserListFragment chatUserListFragment;
    RecyclerView userList_rv;
    ChatUserListAdapter chatUserListAdapter;
    List<NotificationModel> list = new ArrayList<>();
    FirebaseFirestore db;
    String name;
    String uid, my_uid;
    SharedPrefsManager sm;
    ArrayList<Chat> mChats;
    List<Nbdatum> list1 = new ArrayList<>();
    DmMsgListAdapter dmMsgListAdapter;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dm, container, false);
        titleTv = view.findViewById(R.id.title);
        back_btn= view.findViewById(R.id.back_btn);
        titleTv.setText("Direct message");
        db = FirebaseFirestore.getInstance();
        sm = new SharedPrefsManager(getActivity());

        searchImageView = view.findViewById(R.id.search_imageview);
        addImageView = view.findViewById(R.id.add_imageview);
        searchImageView.setVisibility(View.GONE);
        addImageView.setVisibility(View.VISIBLE);
        back_btn.setVisibility(View.GONE);
        directMsgList();
        userList_rv = view.findViewById(R.id.user_list_rv);
        userList_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatUserListFragment = new ChatUserListFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, chatUserListFragment).addToBackStack("chat_user_list").commit();


            }
        });
        return view;
    }

    private void directMsgList() {
        Boolean isInternetConnection = GlobalMethods.checkConnection(getActivity());
        if(isInternetConnection){
            HashMap<String, Object> hm =  new HashMap<>();
            hm.put("userid", Integer.parseInt(sm.getString("user_id")));
            Log.d("userid....." ,String.valueOf (Integer.parseInt(sm.getString("user_id"))));
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<DirectMessagePojo> call = service.directMsgListApi("directmessagelist", hm);
            call.enqueue(new Callback<DirectMessagePojo>() {
                @Override
                public void onResponse(Call<DirectMessagePojo> call, Response<DirectMessagePojo> response) {
                    if(response.body().getStatus().equals("success")) {
                        userList_rv.setVisibility(View.VISIBLE);
                        DirectMessagePojo directMessagePojo = new DirectMessagePojo();
                        directMessagePojo = response.body();
                        list1 = directMessagePojo.getNbdata();

                        dmMsgListAdapter = new DmMsgListAdapter(list1);
                        userList_rv.setAdapter(dmMsgListAdapter);
                    }
//                else
//                {
//                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
//
//                }
                }

                @Override
                public void onFailure(Call<DirectMessagePojo> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("res", t.getMessage());
                }
            });

        }else {

            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), "     No internet connection."     );

        }

    }



}