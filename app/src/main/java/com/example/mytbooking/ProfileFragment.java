package com.example.mytbooking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {


    View view;
    TextView emailView;
    TextView addressView;
    TextView titleNameView;
    String email;
    String name;
    String address;
    Button logout;

    FirebaseFirestore db;
    FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_profile, null);

        emailView = view.findViewById(R.id.profile_edit_email);
        addressView = view.findViewById(R.id.profile_edit_address);
        titleNameView = view.findViewById(R.id.textView);

        logout = view.findViewById(R.id.logout);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Log.d("SUI", auth.getCurrentUser().getEmail());


        email = auth.getCurrentUser().getEmail();

        emailView.setText("Email:  " + email);
        getNameAndAddress();


        setupFirebaseListener();
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();

                Log.d("SUI", "sign out");
            }
        });
        return view;
    }


    public void setupFirebaseListener() {


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    Intent intent = new Intent(getActivity(), SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }


    public void getNameAndAddress() {


        Query queryUser = db.collection("user")
                .whereEqualTo("email", auth.getCurrentUser().getEmail().toString());
        queryUser.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            Log.d("Sui", "query user is successful " + task.getResult().getDocuments());


                            for (QueryDocumentSnapshot document : task.getResult()) {

                                name = document.getData().get("name").toString();

                                Log.d("SUI name", name);
                                //nameView.setText("Name:  " + name);
                                titleNameView.setText(name);


                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                address = document.getData().get("address").toString();

                                Log.d("SUI address", address);
                                addressView.setText("Address:  " + address);


                            }


                        }
                    }
                });
    }


}