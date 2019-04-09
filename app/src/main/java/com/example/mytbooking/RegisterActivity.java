package com.example.mytbooking;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {


    private Button buttonRegister;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editHomeNumber;
    private EditText editName;


    private String email;
    private String password;
    private String homeNumber;
    private String name;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        buttonRegister = findViewById(R.id.register);
        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        editHomeNumber = findViewById(R.id.home_number);
        editName = findViewById(R.id.name);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser(){
        email = editEmail.getText().toString().trim();
        password = editPassword.getText().toString().trim();
        homeNumber = editHomeNumber.getText().toString().trim();
        name = editName.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Mata in ditt email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Mata in ditt lösenord", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(homeNumber)){
            Toast.makeText(this, "Mata in ditt hemma nummer", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Mata in ditt namn", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("!!!","user");


        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful()) {
                    Log.d("!!!", "user created");
                    Toast.makeText(RegisterActivity.this, "register succecesful", Toast.LENGTH_SHORT).show();

                    CollectionReference dbUser = db.collection("user");
                    User user = new User(
                            email,
                            name,
                            homeNumber
                    );
                    dbUser.add(user);

                } else {
                    Log.d("!!!", "create user failed" , task.getException());

                }

            }
        });
    }
}
