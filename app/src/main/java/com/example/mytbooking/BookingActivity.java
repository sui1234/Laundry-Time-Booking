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
import android.widget.DatePicker;
import android.widget.TextView;


public class BookingActivity extends AppCompatActivity {

    DatePicker date;
    int choice;
    TextView cancelView;
    TextView saveView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        date = findViewById(R.id.date_picker);
        date.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.d("Sui", "dialog");
                timeDialog();

            }
        });

    }


    private void timeDialog(){
        View view = getLayoutInflater().inflate(R.layout.dialog_date, null);
        Dialog myDialog = new TimeDialog(this, 0, 0, view, R.style.DialogTheme);
        myDialog.setCancelable(true);
        myDialog.show();


        cancelView = myDialog.findViewById(R.id.cancel);
        saveView = myDialog.findViewById(R.id.save);

       View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.cancel:
                        Intent intent = new Intent(BookingActivity.this, BookingActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.save:
                        Intent intent1 = new Intent(BookingActivity.this, MainActivity.class);
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



