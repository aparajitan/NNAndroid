package com.app_neighbrsnook.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.ChatUserListAdapter;
import com.app_neighbrsnook.model.notification.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class AllChatFragment extends Fragment implements ChatUserListAdapter.UserCallBack{
    ImageView back_btn;
    TextView titleTv;
    RecyclerView userList_rv;
    ChatUserListAdapter chatUserListAdapter;
    List<NotificationModel> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_chat, container, false);
//        titleTv = view.findViewById(R.id.title);
//        back_btn = view.findViewById(R.id.back_btn);
//        titleTv.setText("Buyer/Seller list");
//        back_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().onBackPressed();
//            }
//        });

        list.clear();

        userList_rv = view.findViewById(R.id.user_list_rv);
        userList_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatUserListAdapter = new ChatUserListAdapter(getData(), this);
        userList_rv.setAdapter(chatUserListAdapter);
        return view;
    }

    private List<NotificationModel> getData()
    {
        list.add(new NotificationModel("Arsad Ali", "02 Fab, 2:18 am, Sector 104", "dm",R.color.member, R.drawable.round_corner_dialog_themcolor));
        list.add(new NotificationModel("Amit Singh", "05 Fab, 2:18 am, Sector 120", "poll",R.color.poll, R.drawable.poll_round_corner));
        list.add(new NotificationModel("Rajt", "06 Fab, 5:18 pm, Sector 144", "group",R.color.group, R.drawable.group_round_corner));

        return list;
    }

    @Override
    public void onUserClick(int pos) {

    }

}