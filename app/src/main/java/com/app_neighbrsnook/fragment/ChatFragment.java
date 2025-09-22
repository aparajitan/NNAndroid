package com.app_neighbrsnook.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.ConfirmActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.ChatAdapter;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.intreface.BuyCallBack;
import com.app_neighbrsnook.model.ChatModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatFragment extends Fragment implements BuyCallBack {
    ImageView ic_back, send_msg_iv;
    RecyclerView recyclerView;
    EditText msg_et;
    List<ChatModel> list = new ArrayList<>();
    DAO dao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        dao = Database.createDBInstance(getActivity()).getDao();
        ic_back = view.findViewById(R.id.ic_back);
        send_msg_iv = view.findViewById(R.id.send_msg_iv);
        msg_et = view.findViewById(R.id.msg_et);
        ReadData readPollData = new ReadData();
        readPollData.execute();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        send_msg_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                ChatModel commentModel = new ChatModel();
                commentModel.setComment_msg(msg_et.getText().toString());
//                commentModel.setSender_or_reciever("sender");
                commentModel.setSender_or_reciever(ChatModel.RECIVER);
                InsertCommentData readData = new InsertCommentData();
                readData.execute(commentModel);
                msg_et.setText(" ");
            }
        });

        return view;
    }

    @Override
    public void onBuyClick(int pos) {

    }

    @Override
    public void onsellClick(int pos) {
        Intent i = new Intent (getActivity(), ConfirmActivity.class);
        i.putExtra("price", "price");
        i.putExtra("name","name");
        i.putExtra("question", "buy");
        i.putExtra("type", "buyer");
        startActivity(i);
    }

    @Override
    public void onTemplateClick(String t) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        ChatModel commentModel = new ChatModel();
        commentModel.setComment_msg(t);
        commentModel.setSender_or_reciever(ChatModel.SENDER);
        InsertCommentData readData = new InsertCommentData();
        readData.execute(commentModel);


    }



    private class InsertCommentData extends AsyncTask<ChatModel, Void, Void> {

        @Override
        protected Void doInBackground(ChatModel... investers) {
            dao.insertChatData(investers[0]);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ReadData readPollData = new ReadData();
            readPollData.execute();
        }
    }

    private class ReadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... recurrences) {
            list = new ArrayList<>();
            list = dao.getAllChatList();

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.setAdapter(new ChatAdapter(list, "buyer", ChatFragment.this));

        }

    }
}