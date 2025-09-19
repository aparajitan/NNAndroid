package com.app_neighbrsnook.suggestion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.SuggestionAdapter;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.model.SuggestionModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserCreateSuggestionShow extends AppCompatActivity implements SuggestionAdapter.NewRequest{
    List<SuggestionModel> suggestionModelList = new ArrayList<>();
    private DAO mydao;
    RecyclerView rv_all_suggestion;
    ImageView img_add;
    FrameLayout frm_publish_btn;
    Context context;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context= activity=this;
        setContentView(R.layout.activity_user_create_suggestion_show);
        mydao = Database.createDBInstance(this).getDao();
        Intent intent = getIntent();
        img_add=findViewById(R.id.img_back);
        rv_all_suggestion=findViewById(R.id.suggestion_show_profile_recycler);

        rv_all_suggestion.setLayoutManager(new LinearLayoutManager(this));
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserCreateSuggestionShow.this,AddSuggestion.class));
            }
        });
    }
    @Override
    public void onClick(int pos) {
    }
    @Override
    public void onDetail(int pos) {
        /*
        Intent groupintent  = new Intent(this, GroupMemberListSecond.class);
        groupintent.putExtra("positon", pos);
        startActivity(groupintent);*/
    }
    @Override
    public void threeDot(int pos) {
//        BottomSheetFragment bottomSheetDialog = BottomSheetFragment.newInstance("");
//        bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }
    @Override
    public void onDetailPage(int pos) {
    }
    @Override
    protected void onStart() {
        super.onStart();
        ReadData readData=new ReadData();
        readData.execute();
    }

    private class ReadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... recurrences) {
            suggestionModelList = new ArrayList<>();
            suggestionModelList = mydao.getallSuggestionList();
            Collections.sort(suggestionModelList, new Comparator<SuggestionModel>() {
                @Override
                public int compare(SuggestionModel lhs, SuggestionModel rhs) {
                    return Integer.compare( rhs.getId(),lhs.getId());
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            rv_all_suggestion.setAdapter(new SuggestionAdapter(suggestionModelList, UserCreateSuggestionShow.this)); }}
}