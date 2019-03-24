package com.example.mytbooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    TextView bookingDate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View plusView = inflater.inflate(R.layout.fragment_home,null);
        plusView.findViewById(R.id.fab)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),BookingActivity.class);
                startActivity(intent);
            }
        });

//send selecteddate to minabooking side.
        bookingDate = plusView.findViewById(R.id.booking_date);
        Intent intent = getActivity().getIntent();
        String date = intent.getStringExtra("date");
        bookingDate.setText(date);

        return plusView;


    }
}
