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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {


    private Button buttonRegister;
    private EditText editEmail;
    private EditText editPassword;
    //private EditText editHomeNumber;
    //private EditText editVerificationCode;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        buttonRegister = findViewById(R.id.register_button);
        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        //editHomeNumber = findViewById(R.id.number);
        //editVerificationCode = findViewById(R.id.verification_code);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser(){
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        //String homeNumber = editHomeNumber.getText().toString().trim();
        //String verificationCode = editVerificationCode.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Mata in ditt email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Mata in ditt lösenord", Toast.LENGTH_SHORT).show();
            return;
        }
        /*if(TextUtils.isEmpty(homeNumber)){
            Toast.makeText(this, "Mata in ditt hem nummer", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(verificationCode)){
            Toast.makeText(this, "Mata in ditt verifieringskod", Toast.LENGTH_SHORT).show();
            return;
        }*/

        Log.d("!!!","user");

        createAccount(email,password);




    }

    private void createAccount (String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful()) {
                    Log.d("!!!", "user created");
                    Toast.makeText(RegisterActivity.this, "register succecesful", Toast.LENGTH_SHORT).show();

                } else {
                    Log.d("!!!", "create user failed" , task.getException());

                }

            }
        });


    }


}
