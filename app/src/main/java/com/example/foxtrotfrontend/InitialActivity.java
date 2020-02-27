package com.example.foxtrotfrontend;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class InitialActivity extends AppCompatActivity {

    private String initialPest = "initialPest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
    }


    public void newReport(View view) throws ParseException {
        // Spawn new report page from main menu
        SharedPreferences sharedPref = getSharedPreferences("MyPref", 0);
        int streak = 0;
        streak = sharedPref.getInt("streak", streak);
        int new_streak = updateInteraction();
        if (new_streak == streak+1) {
            openDialog();
            
        }
        Intent intent = new Intent(this, NewReportActivity.class);
        startActivity(intent);
    }

    public void blankReport(View view) throws ParseException {
        // Blank report
        // Get report id from server
        // Send blank report
        updateInteraction();
        int reportId = 0;
        double longitude = 0.121817;
        double latitude = 52.205338;
        Date date = Calendar.getInstance().getTime();
        ReportHTTP conn = new ReportHTTP("www.google.com");
        conn.newReport(reportId, date, latitude, longitude, initialPest, "", "", null, null, "0");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public int updateInteraction() throws ParseException {

        // Get current system time as String
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String strDate = dateFormat.format(currentDate);

        // Get shared preference
        SharedPreferences sharedPref = getSharedPreferences("MyPref", 0);

        // Read shared preference
        int streak = 0;
        streak = sharedPref.getInt("streak", streak);
        String interactionDate = strDate;
        interactionDate = sharedPref.getString("interactionDate", interactionDate);
        // Update streak if new day. Ignore otherwise
        Date lastDate = dateFormat.parse(interactionDate);
        long diffInMillies = Math.abs(currentDate.getTime() - lastDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        System.out.println(diff);
        if (diff>=0 && diff<7) {
            streak = streak + 1;
            // Send notification
//            openDialog();
        }

        // If new interactionDate, update reminder
        if (diff>=1) {
            cancelNotification();
            createNotification(lastDate);
        }

        // Write current time to shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("streak", streak);
        editor.putString("interactionDate", interactionDate);
        editor.commit();

        return streak;
    }

    // Create openDialog when streak is updated
    public void openDialog() {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog_demo);
        dialog.setTitle("Streak extended");
        dialog.show();
    }

    // Cancel previously created notification
    public void cancelNotification() {

        // Get ALARM_SERVICE from system
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Create DISPLAY_NOTIFICATION intent
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Cancel all such intents
        alarmManager.cancel(broadcast);
    }

    // Creates notification two weeks after last accessed
    public void createNotification(Date interactionDate) {

        // Get ALARM_SERVICE from system
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Create DISPLAY_NOTIFICATION intent
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create Calendar instant
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(interactionDate);
        calendar.add(Calendar.DAY_OF_YEAR, 14);

        // Add to ALARM_SERVICE
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);
    }
}
