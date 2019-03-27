package com.example.mytbooking;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    String choosedTime;

    int year;
    int month;
    int day;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);


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


        /*calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                selectedDate = year + " - " + (month + 1) + " -" + dayOfMonth;
                Log.d("Sui", "dialog");
                timeDialog();
            }
        });*/


    }

    public void getTime() {
        if (selectedDate != null) {
            year = selectedDate.getYear();
            month = selectedDate.getMonth() + 1; //月份跟系统一样是从0开始的，实际获取时要加1
            day = selectedDate.getDay();

        }


    }

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

                    choosedTime = radioButton.getText().toString();

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

                        if (year != 0 && choosedTime != null) {
                            Intent intent1 = new Intent(BookingActivity.this, MainActivity.class);

                            intent1.putExtra("date", year + "-" + month + "-" + day + "   " + choosedTime);
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



