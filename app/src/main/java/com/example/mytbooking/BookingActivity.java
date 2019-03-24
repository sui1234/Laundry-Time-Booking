package com.example.mytbooking;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class BookingActivity extends AppCompatActivity {

    CalendarView date;
    TextView cancelView;
    TextView saveView;
    String selectedDate;

    RadioGroup radioGroup;
    RadioButton radioButton;
    String choosedTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);


        date = findViewById(R.id.calendar);
        date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                selectedDate = year + " - " + (month + 1) + " -" + dayOfMonth;
                Log.d("Sui", "dialog");
                timeDialog();
            }
        });

    }

    private void timeDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_time, null);
        Dialog myDialog = new TimeDialog(this, 0, 0, view, R.style.DialogTheme);
        myDialog.setCancelable(true);
        myDialog.show();


        radioGroup = view.findViewById(R.id.radio_group);

        Log.d("Sui","radioButton1");

        if (radioGroup != null)
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override

                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    Log.d("Sui","radioButton2");

                    radioButton =(RadioButton) radioGroup.findViewById(checkedId);

                    choosedTime =radioButton.getText().toString();

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
                        Intent intent1 = new Intent(BookingActivity.this, MainActivity.class);

                        intent1.putExtra("date", selectedDate + "   " + choosedTime);
                        startActivity(intent1);
                        break;

                    default:
                        break;
                }

            }
        };
        cancelView.setOnClickListener(listener);
        saveView.setOnClickListener(listener);


    }

}



