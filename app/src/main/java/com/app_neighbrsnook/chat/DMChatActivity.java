package com.app_neighbrsnook.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.app_neighbrsnook.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DMChatActivity extends AppCompatActivity {
    private EditText metText;
    private ImageView mbtSent;
    private ListenerRegistration mChatMessageEventListener, mUserListEventListener;
    private List<Chat> mChats;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private String mId;
    FirebaseFirestore db;
    ImageView searchImageView, addImageView, back_btn, cancel_iv;
    String name, uid, my_uid;

    SharedPrefsManager sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmchat);

        Intent i = getIntent();
        name = i.getStringExtra("user_name");
        uid = i.getStringExtra("uid");
        my_uid = i.getStringExtra("my_uid");
        db = FirebaseFirestore.getInstance();
        mId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        sm = new SharedPrefsManager(this);
        metText = findViewById(R.id.etText);
        mbtSent = findViewById(R.id.btSent);
        searchImageView.setVisibility(View.VISIBLE);
        addImageView.setVisibility(View.VISIBLE);
        mRecyclerView = findViewById(R.id.rvChat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        readMessages();
        mbtSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = metText.getText().toString();

                if (!message.isEmpty()) {

                    sendMsg(message);
                }

                metText.setText("");
            }
        });
    }

    private void readMessages() {
        mChats = new ArrayList<>();
        db.collection("message")
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list =queryDocumentSnapshots.getDocuments();
                        mChats.clear();
                        for(DocumentSnapshot document : list)
                        {
                            if ((document.getData().get("sender").toString().equals(sm.getString("my_uid")) && document.getData().get("reciever").toString().equals(uid)) ||
                                    (document.getData().get("reciever").toString().equals(sm.getString("my_uid")) && document.getData().get("sender").toString().equals(uid))){
                                mChats.add(new Chat(document.getData().get("message").toString(),document.getData().get("reciever").toString(),document.getData().get("sender").toString(), document.getData().get("time").toString()));
                                Log.d("Message", mChats.toString());
                            }
                        }

                        Collections.reverse(mChats);
                        mAdapter = new ChatAdapter(mChats, sm.getString("my_uid"));
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();


                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getChatMessages();
    }


    private void getChatMessages(){
        db.collection("message")
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("TAG", "onEvent: Listen failed.", e);
                            return;
                        }
                        mChats.clear();

                        if(queryDocumentSnapshots != null){
                            for(QueryDocumentSnapshot document : queryDocumentSnapshots)
                            {
                                if ((document.getData().get("sender").toString().equals(sm.getString("my_uid")) && document.getData().get("reciever").toString().equals(uid)) ||
                                        (document.getData().get("reciever").toString().equals(sm.getString("my_uid")) && document.getData().get("sender").toString().equals(uid))){
                                    mChats.add(new Chat(document.getData().get("message").toString(),document.getData().get("reciever").toString(),document.getData().get("sender").toString(), document.getData().get("time").toString()));
                                }
                            }
                            Collections.reverse(mChats);
                            mAdapter = new ChatAdapter(mChats, sm.getString("my_uid"));
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }

    private void sendMsg(String message) {
        Map<String, Object> messageMap = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        messageMap.put("message", message);
        messageMap.put("name", name);
        messageMap.put("time",formatter.format(date));
        messageMap.put("reciever",uid);
        messageMap.put("sender",sm.getString("my_uid"));
        db.collection("message").add(messageMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });

    }
}