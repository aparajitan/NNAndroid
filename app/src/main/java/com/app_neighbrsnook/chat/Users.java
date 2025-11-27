package com.app_neighbrsnook.chat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.app_neighbrsnook.R;

import java.util.ArrayList;
import java.util.List;

public class Users extends AppCompatActivity implements ChatUserListAdapter.UserCallBack {

    ListView usersList;
    RecyclerView chat_rv;
    TextView noUsersText;
    ChatUserListAdapter chatUserListAdapter;
    ArrayList<User> al;
    int totalUsers = 0;
    ProgressDialog pd;
    FirebaseFirestore db;
    ChatUserListAdapter.UserCallBack ucb= this;
    String name;
    String uid, my_uid;
    SharedPrefsManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        sm = new SharedPrefsManager(this);
//        usersList = (ListView)findViewById(R.id.usersList);
        chat_rv = findViewById(R.id.chat_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        chat_rv.setLayoutManager(linearLayoutManager);
        noUsersText = findViewById(R.id.noUsersText);
        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(Users.this);
        pd.setMessage("Loading...");
        pd.show();
        getAllUsers();
    }


    public void getAllUsers() {
        db.collection("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            al = new ArrayList<>();
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot document : list) {

                                User user = document.toObject(User.class);
                                 name = document.getData().get("name").toString();
                                 uid = document.getData().get("uid").toString();
                                al.add(user);

                            }
                            Log.d("TAG al", al.toString());
                            chatUserListAdapter = new ChatUserListAdapter(al, ucb);
                            chat_rv.setAdapter(chatUserListAdapter);
                            if(al.size() <=1){
                                noUsersText.setVisibility(View.VISIBLE);
                                chat_rv.setVisibility(View.GONE);
                            }
                            else{
                                noUsersText.setVisibility(View.GONE);
                                chat_rv.setVisibility(View.VISIBLE);
                                chatUserListAdapter = new ChatUserListAdapter(al, ucb);
                                chat_rv.setAdapter(chatUserListAdapter);

                            }
                            chatUserListAdapter = new ChatUserListAdapter(al, ucb);
                            chat_rv.setAdapter(chatUserListAdapter);
                              }

                });

        pd.dismiss();
    }


    @Override
    public void onUserClick(int pos) {

    }
}
