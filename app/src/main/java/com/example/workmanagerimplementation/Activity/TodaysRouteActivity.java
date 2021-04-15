package com.example.workmanagerimplementation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.workmanagerimplementation.R;

public class TodaysRouteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_route);
        Toast.makeText(this, "Todays Route", Toast.LENGTH_SHORT).show();
    }
}