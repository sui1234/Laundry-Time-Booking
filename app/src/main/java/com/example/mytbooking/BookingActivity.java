package com.example.mytbooking;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;


public class BookingActivity extends AppCompatActivity {

    DatePicker date;
    int choice;

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


    private void timeDialog() {
        final String[] items = {"07.00 - 11.00", "11.00 - 15.00", "15.00 - 19.00","19.00 - 23.00"};

        AlertDialog.Builder timeDialog = new AlertDialog.Builder(BookingActivity.this);
        timeDialog.setTitle("                         Välj tid");

        timeDialog.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choice = which;
            }
        });
        timeDialog.setPositiveButton("Spara", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (choice != -1) {

                    Intent intent = new Intent(BookingActivity.this,MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(BookingActivity.this,
                            "Du har valt  " + items[choice],
                            Toast.LENGTH_SHORT).show();



                }
            }
        });

        timeDialog.setNegativeButton("back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (choice != -1) {
                    Toast.makeText(BookingActivity.this,
                            "Du har lämnat tid dialog  ",
                            Toast.LENGTH_SHORT).show();



                }
            }
        });
        timeDialog.show();
    }


}



