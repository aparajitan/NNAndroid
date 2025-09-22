package com.app_neighbrsnook.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.ChatUserListAdapter;
import com.app_neighbrsnook.model.notification.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class LikeBottomSheetFragment extends BottomSheetDialogFragment implements ChatUserListAdapter.UserCallBack {
    ChatUserListAdapter chatUserListAdapter;
    List<NotificationModel> list = new ArrayList<>();
    public static LikeBottomSheetFragment newInstance() {
        LikeBottomSheetFragment fragment = new LikeBottomSheetFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_like_bottom_sheet, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = view.findViewById(R.id.user_list_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatUserListAdapter = new ChatUserListAdapter(getData(), this);
        recyclerView.setAdapter(chatUserListAdapter);

    }


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_like_bottom_sheet, null);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    @Override public int getTheme() {
        return R.style.CustomBottomSheetDialog;
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
