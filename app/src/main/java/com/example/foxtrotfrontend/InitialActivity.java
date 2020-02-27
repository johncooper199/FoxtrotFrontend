package com.example.foxtrotfrontend;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class InitialActivity extends AppCompatActivity {

    private String initialPest = "initialPest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
    }


    public void newReport(View view) throws ParseException {
        // Spawn new report page from main menu
        Notifications nf = new Notifications();
        nf.updateInteraction();
        Intent intent = new Intent(this, NewReportActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void blankReport(View view) throws ParseException {
        // Blank report
        // Get report id from server
        // Send blank report
        Notifications nf = new Notifications();
        nf.updateInteraction();
        int reportId = 0;
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Date date = Calendar.getInstance().getTime();
        ReportHTTP conn = new ReportHTTP("www.google.com");
        conn.newReport(reportId, date, latitude, longitude, initialPest, "", "", null, null, "0");
    }
}
