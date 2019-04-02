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

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

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
    String date;
    String name;
    String selectedTime;

    FirebaseFirestore db;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);


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
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                selectedDate = date;
                Log.d("Sui", "dialog");
                timeDialog();
                getTime();

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
            date = Integer.toString(year) + " - " + Integer.toString(month) + " - " + Integer.toString(day);

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
                            Intent intent1 = new Intent(BookingActivity.this, MainActivity.class);

                            intent1.putExtra("date", date + "   " + selectedTime);

                            //when save button is clicked then save date in firestore automatic

                            CollectionReference dbBooking = db.collection("booking");

                            Booking booking = new Booking(name,
                                    date, selectedTime
                            );

                            dbBooking.add(booking);


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



