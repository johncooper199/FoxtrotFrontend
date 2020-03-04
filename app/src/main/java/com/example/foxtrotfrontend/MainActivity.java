package com.example.foxtrotfrontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("NIAB Data Collection");

        // Checking if it should show streak screen
        SharedPreferences sharedPreferences = getSharedPreferences("STREAKS", MODE_PRIVATE);
        int lastOpened = sharedPreferences.getInt("LAST_OPENED", 0);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //TODO: Remove
        int currentTime = Integer.parseInt(sdf.format(cal.getTime()));
        if ((lastOpened==0) || (lastOpened<currentTime)){
            Intent intent = new Intent(this, InitialActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void newReport(View view){
        // Spawn new report page from main menu
        Intent intent = new Intent(this, NewReportActivity.class);
        startActivity(intent);
    }

    public void nearbyReports(View view){
        // Spawn new report page from main menu
        Intent intent = new Intent(this, NearbyReportsActivity.class);
        startActivity(intent);
    }

    public void myReports(View view) {
        // Spawn new report page from main menu
        Intent intent = new Intent(this, MyReportsActivity.class);
        startActivity(intent);
    }

    public void updateReports(View view) {
        // Spawn new report page from main menu
        Intent intent = new Intent(this, UpdateActivity.class);
        startActivity(intent);
    }
}
