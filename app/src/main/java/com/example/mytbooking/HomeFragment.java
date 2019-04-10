package com.example.mytbooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeFragment extends Fragment {
    TextView bookingDate;
    FirebaseFirestore db;
    FirebaseAuth auth;
    View plusView;
    String timeResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        plusView = inflater.inflate(R.layout.fragment_home, null);
        plusView.findViewById(R.id.fab)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), BookingActivity.class);
                        startActivity(intent);
                    }
                });

//send selected date to mina booking side.
        getTimeFromFire();


       /* bookingDate = plusView.findViewById(R.id.booking_date);
        Intent intent = getActivity().getIntent();
        String date = intent.getStringExtra("date");
        bookingDate.setText(date);*/

        return plusView;


    }

    public void getTimeFromFire() {
        bookingDate = plusView.findViewById(R.id.booking_date);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        Query queryTime = db.collection("booking")
                .whereEqualTo("name", auth.getCurrentUser().getUid());

        queryTime.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sui", "query time is successful " + task.getResult().getDocuments());

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("Sui", "get date successful");
                                timeResult = document.getData().get("date").toString() + "  " + document.getData().get("time").toString();

                                bookingDate.setText(timeResult);
                            }

                        }
                    }
                });





    }
}
