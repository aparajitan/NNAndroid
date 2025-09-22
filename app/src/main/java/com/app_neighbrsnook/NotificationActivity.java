package com.app_neighbrsnook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.app_neighbrsnook.adapter.NotificationAdapter;
import com.app_neighbrsnook.model.notification.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView notification_rv;
    NotificationAdapter notificationAdapter;
    List<NotificationModel> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        notification_rv = findViewById(R.id.notification_rv);
        notification_rv.setLayoutManager(new GridLayoutManager(this,3, RecyclerView.VERTICAL, false));
//        notificationAdapter = new NotificationAdapter((ArrayList<NotificationModel>) getData());
//        notification_rv.setAdapter(notificationAdapter);
    }
    private List<NotificationModel> getData()
    {
        list.add(new NotificationModel("poll", "2", "business",R.color.member, R.drawable.garden_suggestion));
        list.add(new NotificationModel("poll", "2", "group",R.color.group, R.drawable.garden_suggestion));
        list.add(new NotificationModel("poll", "2", "event",R.color.event, R.drawable.garden_suggestion));
        list.add(new NotificationModel("poll", "2", "suggestion",R.color.suggestion ,R.drawable.garden_suggestion));
        list.add(new NotificationModel("poll", "2", "Poll",R.color.member, R.drawable.garden_suggestion));
        return list;
    }
}