package com.example.mytbooking;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class BookingActivity extends AppCompatActivity {

    MaterialCalendarView calendarView;
    TextView cancelView;
    TextView saveView;
    CalendarDay selectedDate;

    RadioGroup radioGroup;
    RadioButton radioButton;

    int year;
    int month;
    int day;

    String date1;
    String name;
    String selectedTime;


    String stringDate;
    FirebaseFirestore db;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);


        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();


        calendarView = findViewById(R.id.calendar);

        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);

        calendarView.addDecorator(new CurrentDateDecorator(this));

        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(Calendar.getInstance().getTime()))
                .setMaximumDate(CalendarDay.from(2021, 1, 1))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, boolean selected) {

                selectedDate = date;
                Log.d("Sui", "dialog");

                timeDialog();
                getTime();


               // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                //stringDate = sdf.format(selectedDate.getDate());


                stringDate = Integer.toString(selectedDate.getYear()) + " - " +Integer.toString(selectedDate.getMonth() + 1) + " - " + Integer.toString(selectedDate.getDay());


                Query queryTime = db.collection("booking").whereEqualTo("date", stringDate);


                queryTime
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Sui",  "issucesful " + task.getResult().getDocuments());

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("SUI document.getId", document.getId() + " => " + document.getData().toString());




                                        if (document.getData().get("time").toString().replaceAll(" ", "").contains("07.00-11.00") == true) {


                                            Log.d("Sui",  stringDate + " 07.00 -11.00 is busy");

                                        }else if (document.getData().get("time").toString().replaceAll(" ", "").contains("11.00-15.00") == true) {


                                            Log.d("Sui",  stringDate + " 11.00 -15.00 is busy");

                                        }else if (document.getData().get("time").toString().replaceAll(" ", "").contains("15.00-19.00") == true) {


                                            Log.d("Sui",  stringDate + " 15.00 -19.00 is busy");

                                        } else if (document.getData().get("time").toString().replaceAll(" ", "").contains("19.00-23.00") == true)

                                            Log.d("Sui", stringDate+ " 19.00 -23.00 is busy");






                                    }
                                } else {
                                    Log.d("SUI", "Error getting documents: ", task.getException());
                                }
                            }

                        });


            }
        });


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


    }


    // bottomnavigation
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };


    // get date from calendar
    public void getTime() {
        if (selectedDate != null) {
            year = selectedDate.getYear();
            month = selectedDate.getMonth() + 1;
            day = selectedDate.getDay();
            date1 = Integer.toString(year) + " - " + Integer.toString(month) + " - " + Integer.toString(day);

        }


    }

    // timeDialog function
    private void timeDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_time, null);
        Dialog myDialog = new TimeDialog(this, 0, 0, view, R.style.DialogTheme);
        myDialog.setCancelable(true);
        myDialog.show();


        radioGroup = view.findViewById(R.id.radio_group);

        Log.d("Sui", "radioButton1");

        if (radioGroup != null)
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override

                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    Log.d("Sui", "radioButton2");

                    radioButton = radioGroup.findViewById(checkedId);

                    selectedTime = radioButton.getText().toString();

                }
            });


        cancelView = myDialog.findViewById(R.id.cancel);
        saveView = myDialog.findViewById(R.id.save);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cancel:
                        Intent intent = new Intent(BookingActivity.this, BookingActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.save:

                        if (year != 0 && selectedTime != null) {
                            Log.d("DAVID", "Save");
                            Intent intent1 = new Intent(BookingActivity.this, MainActivity.class);

                            intent1.putExtra("date", date1 + "   " + selectedTime);

                            //when save button is clicked then save date in firestore automatic

                            CollectionReference dbBooking = db.collection("booking");

                            Booking booking = new Booking("DAVID",
                                    date1, selectedTime
                            );
                            dbBooking.add(booking);

                           /* Log.d("DAVID", "add bocking: " + booking.name);
                            dbBooking.add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("DAVID", "Success");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("DAVID", "Error", e);
                                }
                            }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Log.d("DAVID", "Complete");
                                }
                            }).addOnCanceledListener(new OnCanceledListener() {
                                @Override
                                public void onCanceled() {
                                    Log.d("DAVID", "Canceled");

                                }
                            });*/


                            startActivity(intent1);
                            break;
                        }

                    default:
                        break;
                }

            }
        };
        cancelView.setOnClickListener(listener);
        saveView.setOnClickListener(listener);


    }


}



