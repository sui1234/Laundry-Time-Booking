package com.example.mytbooking;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.Calendar;


public class BookingActivity extends AppCompatActivity {

    MaterialCalendarView calendarView;
    TextView cancelView;
    TextView saveView;
    CalendarDay selectedDate;

    RadioGroup radioGroup;
    RadioButton radioButton;
    //Booking activeBooking = null;

    int year;
    int month;
    int day;

    String date1;
    String selectedTime;

    View dialogView;


    String stringSelectedDate;
    FirebaseFirestore db;
    FirebaseAuth auth;

    boolean haveBooked = false;
    boolean isBusySave = false;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);


        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


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
                Log.d("Sui", "timeDialog visas");

                checkIfTimeBusy();

                timeDialog();

                checkIfTimeIsExpired();

                getDate();

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


    // timeDialog function
    private void timeDialog() {


        dialogView = getLayoutInflater().inflate(R.layout.dialog_time, null);
        Dialog myDialog = new TimeDialog(this, 0, 0, dialogView, R.style.DialogTheme);
        myDialog.setCancelable(true);

        myDialog.show();

        radioGroup = dialogView.findViewById(R.id.radio_group);

        Log.d("Sui", "radioButton1");

        if (radioGroup != null)
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    Log.d("Sui", "radioButton2");

                    radioButton = radioGroup.findViewById(checkedId);

                    selectedTime = radioButton.getText().toString();

//check if two users book at the same time.
                    checkIfTimeIsBusySave();
                }
            });


        cancelView = myDialog.findViewById(R.id.cancel);
        saveView = myDialog.findViewById(R.id.save);

        haveBooked();

        if (isBusySave == false) {

            saveView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (haveBooked() == false) {
                        if (year != 0 && selectedTime != null) {
                            Log.d("DAVID", "Save");

                            Intent intent1 = new Intent(BookingActivity.this, MyBookingsActivity.class);
                            intent1.putExtra("date", date1 + "   " + selectedTime);

                                /*if (activeBooking != null) {
                                    db.collection("booking").document(activeBooking.id).delete();
                                }*/

                            saveInFirestore();

                            startActivity(intent1);

                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.choose_period), Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.have_time), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingActivity.this, BookingActivity.class);
                startActivity(intent);
            }
        });

    }

    // when you open todays timeDialog, compare four time periods with current time, if current time is bigger than periods, the periods need to be setDisabled.
    public void checkIfTimeIsExpired() {


        Calendar calendar = Calendar.getInstance();
        String dayCurrent = Integer.toString(calendar.get(Calendar.DATE));
        String daySelected = Integer.toString(selectedDate.getDay());
        int hourCurrent = calendar.get(Calendar.HOUR_OF_DAY);

        Log.d("SUI dayCurrent ", dayCurrent);

        Log.d("SUI daySelected", daySelected);

        Log.d("SUI hourCurrent", Integer.toString(hourCurrent));


        if (dayCurrent.equals(daySelected)) {

            if (hourCurrent >= 11) {
                setButtonDisabled(0);

            }
            if (hourCurrent >= 15) {
                setButtonDisabled(0);
                setButtonDisabled(1);
            }
            if (hourCurrent >= 19) {
                setButtonDisabled(0);
                setButtonDisabled(1);
                setButtonDisabled(2);

            }
            if (hourCurrent >= 23) {
                setButtonDisabled(0);
                setButtonDisabled(1);
                setButtonDisabled(2);
                setButtonDisabled(3);
            }

        }


    }

    public void checkIfTimeBusy() {

        stringSelectedDate = Integer.toString(selectedDate.getYear()) + " - " + Integer.toString(selectedDate.getMonth() + 1)
                + " - " + Integer.toString(selectedDate.getDay());
        Query queryDate = db.collection("booking").whereEqualTo("date", stringSelectedDate);


        queryDate
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sui", "issuccessful " + task.getResult().getDocuments());

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if (document.getData().get("time").toString().replaceAll(" ", "").contains("07.00-11.00") == true) {

                                    setButtonDisabled(0);
                                    Log.d("Sui", stringSelectedDate + " 07.00 -11.00 is busy");

                                } else if (document.getData().get("time").toString().replaceAll(" ", "").contains("11.00-15.00") == true) {

                                    setButtonDisabled(1);
                                    Log.d("Sui", stringSelectedDate + " 11.00 -15.00 is busy");

                                } else if (document.getData().get("time").toString().replaceAll(" ", "").contains("15.00-19.00") == true) {

                                    setButtonDisabled(2);
                                    Log.d("Sui", stringSelectedDate + " 15.00 -19.00 is busy");

                                } else if (document.getData().get("time").toString().replaceAll(" ", "").contains("19.00-23.00") == true) {

                                    setButtonDisabled(3);
                                    Log.d("Sui", stringSelectedDate + " 19.00 -23.00 is busy");

                                }
                            }
                        } else {
                            Log.d("SUI", "Error getting documents: ", task.getException());
                        }
                    }

                });

    }


    // get date from calendar
    public void getDate() {
        if (selectedDate != null) {
            year = selectedDate.getYear();
            month = selectedDate.getMonth() + 1;
            day = selectedDate.getDay();
            date1 = Integer.toString(year) + " - " + Integer.toString(month) + " - " + Integer.toString(day);

        }


    }


    // set button disabled function
    public void setButtonDisabled(int i) {

        Log.d("SUI", "button1 is disabled");
        radioGroup.getChildAt(i).setEnabled(false);


    }

    //when saveButton is clicked then save date in firestore automatic
    public void saveInFirestore() {
        CollectionReference dbBooking = db.collection("booking");

        auth = FirebaseAuth.getInstance();


        Booking booking = new Booking(auth.getCurrentUser().getUid(),
                date1, selectedTime
        );

        dbBooking.add(booking)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("SUI", "its successful to add data in firestore");
                    }
                });

        //CollectionReference userBookingsRef = db.collection("user").document(auth.getCurrentUser().getUid()).collection("bookings");
        //userBookingsRef.add();
    }

    // check if user have booked times and the times are not expired.
    public boolean haveBooked() {

        Query queryTime = db.collection("booking")
                .whereEqualTo("name", auth.getCurrentUser().getUid());

        queryTime.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sui", "haveBooked query time is successful " + task.getResult().getDocuments());

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

                                // check if times are not expired,then haveBooked = true.
                                if (compareDateInt.compareTo(dateCurrentInt) > 0 ||
                                        (compareDateInt.compareTo(dateCurrentInt) == 0 && compareTimeInt.compareTo(timeCurrentInt) > 0)) {
                                    Log.d("Sui", "compare date and time successful");

                                    haveBooked = true;
                                }

                            }

                        }
                    }
                });
        return haveBooked;

    }

    public boolean checkIfTimeIsBusySave() {

        stringSelectedDate = Integer.toString(selectedDate.getYear()) + " - " + Integer.toString(selectedDate.getMonth() + 1) + " - " + Integer.toString(selectedDate.getDay());
        Query queryTime = db.collection("booking").whereEqualTo("date", stringSelectedDate);


        queryTime
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sui", "check if time is busy when save --issuccessful with save" + task.getResult().getDocuments());

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SUI document.getId", document.getId() + " => " + document.getData().toString());


                                if (document.getData().get("time").toString().equals(selectedTime)) {

                                    isBusySave = true;

                                    Log.d("Sui isbusysave", isBusySave + " .");
                                    Log.d("Sui", document.getData().get("time") + "time");
                                    Log.d("Sui", selectedTime + " is choosed by others");
                                } else {

                                    isBusySave = false;
                                    Log.d("Sui", selectedTime + " is not busy and save");
                                }
                            }
                        } else {
                            Log.d("SUI", "Error getting documents: ", task.getException());
                        }
                    }

                });

        return isBusySave;
    }


}



