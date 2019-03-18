package com.example.mytbooking;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

public class BookingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        RadioGroup radioGroup = findViewById(R.id.radio_group);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch (checkedId) {
                    case R.id.button_date:
                        Log.d("Sui", "mydatefragment");
                        DatePickerFragment datePickerFragment=new DatePickerFragment();
                        datePickerFragment.show(getSupportFragmentManager(),  "datepicker");

                        replaceFragment(new DatePickerFragment());

                        break;
                    case R.id.button_time:

                        Log.d("Sui", "mytimefragment");
                        replaceFragment(new TimeFragment());
                        break;
                }
            }

            private void replaceFragment(Fragment fragment) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        });


    }

}



