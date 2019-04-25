package com.example.mytbooking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {
    TextView bookingDate;
    FirebaseFirestore db;
    FirebaseAuth auth;
    View plusView;
    String timeResult;

    String id;

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


        getTimeFromFire();
        cancel();


        return plusView;


    }

    // get time from firestore and check if the booked time is expired, if not ,then display it in bookingDate.

    public void getTimeFromFire() {

        bookingDate = plusView.findViewById(R.id.booking_date);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        Query queryTime = db.collection("booking")
                .whereEqualTo("id", auth.getCurrentUser().getUid());

        queryTime.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sui", "query time is successful " + task.getResult().getDocuments());

                            for (QueryDocumentSnapshot document : task.getResult()) {


                                Calendar calendar = Calendar.getInstance();

                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH) + 1;
                                int day = calendar.get(Calendar.DATE);
                                int time = calendar.get(Calendar.HOUR_OF_DAY);

                                String dateCurrent = Integer.toString(year) + Integer.toString(month) + Integer.toString(day);

                                String compareDate = document.getData().get("date").toString().replaceAll(" ", "").replaceAll("-", "");


                                String timeCurrent = Integer.toString(time);
                                String getTimeFire = document.getData().get("time").toString().replace(".", "").replace(" - ", "");

                                String compareTime = getTimeFire.substring(4, 6);


                                Integer compareDateInt = Integer.parseInt(compareDate);
                                Integer dateCurrentInt = Integer.parseInt(dateCurrent);

                                Integer compareTimeInt = Integer.parseInt(compareTime);
                                Integer timeCurrentInt = Integer.parseInt(timeCurrent);


                                Log.d("SUI timeCurrent", timeCurrent);

                                Log.d("SUI3", getTimeFire);
                                Log.d("SUI compareTime", compareTime);

                                Log.d("SUI currentdate", dateCurrent);
                                Log.d("SUI2 comparedate", compareDate);

                                Log.d("SUI compare date result", Integer.toString(compareDateInt.compareTo(dateCurrentInt)));

                                Log.d("SUi compare time result", Integer.toString(compareTimeInt.compareTo(timeCurrentInt)));

                                if (compareDateInt.compareTo(dateCurrentInt) > 0 ||
                                        (compareDateInt.compareTo(dateCurrentInt) == 0 && compareTimeInt.compareTo(timeCurrentInt) > 0)) {

                                    Log.d("Sui", "compare date and time successful");

                                    id = document.getId();
                                    Log.d("SUI ID", id);

                                    timeResult = document.getData().get("date").toString() + "  " + document.getData().get("time").toString();

                                    bookingDate.setText(timeResult);

                                }

                            }

                        }
                    }
                });

    }


    public void cancel() {

        bookingDate = plusView.findViewById(R.id.booking_date);


        bookingDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                if (timeResult != null) {
                    alert.setMessage(getResources().getString(R.string.cancel_sentence) + " "+ timeResult + " ?")

                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Log.d("Sui avboka", "succesful");

                                    //delete date from firestore

                                    db.collection("booking").document(id).delete();
                                    Log.d("Sui Document id " ,db.collection("booking").document(id)+"." );
                                    bookingDate.setText("");

                                    bookingDate.setEnabled(false);

                                    if (bookingDate.getText().equals(""))
                                        Log.d("SUI bookingDate", "empty");
                                    else
                                        Log.d("SUI bookingDate", ":" + bookingDate.getText() + ":");
                                }

                            })

                            .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();

                                    //Intent intent = new Intent(getContext(), BookingActivity.class);
                                    //startActivity(intent);
                                }

                            });

                    alert.setTitle(getResources().getString(R.string.to_cancel));

                    alert.show();

                }

            }

        });


    }


}
