package com.app_neighbrsnook.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.app_neighbrsnook.R;

public class Login extends AppCompatActivity {
    TextView register;
    EditText username, password;
    Button loginButton;
    String user, pass;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String uid;
    SharedPrefsManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        register = (TextView) findViewById(R.id.register);
//        username = (EditText) findViewById(R.id.username);
//        password = (EditText) findViewById(R.id.password);
//        loginButton = (Button) findViewById(R.id.loginButton);
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//        sm = new SharedPrefsManager(this);
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Login.this, Register.class));
//            }
//        });
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                user = username.getText().toString();
//                pass = password.getText().toString();
//
//                if (user.equals("")) {
//                    username.setError("can't be blank");
//                } else if (pass.equals("")) {
//                    password.setError("can't be blank");
//                } else {
////                    LoginApp(user, pass);
//                    getMultipleDocs(user, pass);
//                }
//
//            }
//        });
//    }
//
//    private void LoginApp(String userid, String password) {
//        mAuth.createUserWithEmailAndPassword(userid, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("TAG", "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
////                            user.getIdToken(true);
//
////                            Log.d("userName", mAuth.getCurrentUser().getDisplayName().toString());
////                            getMultipleDocs(userid);
//                            Toast.makeText(Login.this, "Authentication success.", Toast.LENGTH_SHORT).show();
////                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(Login.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
////                            updateUI(null);
//                        }
//                    }
//
//
//
//                });
//    }
//
//
//
//    public void getMultipleDocs(String email, String pass) {
//        db.collection("users")
//                .whereEqualTo("email", email)
//                .whereEqualTo("password", pass)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
////                            Toast.makeText(Login.this, "Authentication success." , Toast.LENGTH_SHORT).show();
//
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                uid = document.getData().get("uid").toString();
//                                sm.setString("my_uid", uid);
//                                Intent i = new Intent (Login.this, Users.class);
//                                startActivity(i);
//                            }
//                        } else {
//                            Log.d("TAG", "Error getting documents: ", task.getException());
//                            Toast.makeText(Login.this, "Wrong userid or pasword", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//    }
//
//
//    //get all user from database
//
//    public void getAllUsers() {
//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("TAG", document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w("TAG", "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//
    }

}
