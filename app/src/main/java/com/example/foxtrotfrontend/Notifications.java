package com.example.foxtrotfrontend;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Notifications extends AppCompatActivity {

    // Call when opening app
    // Changes interactionDate and updates streak if separate day
    // Returns streak
    public int updateInteraction() throws ParseException {

        // Get current system time as String
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");
        String strDate = dateFormat.format(currentDate);

        // Get shared preference
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        // Read shared preference
        int streak = 0;
        streak = sharedPref.getInt("streak", streak);
        String interactionDate = strDate;
        interactionDate = sharedPref.getString("interactionDate", interactionDate);

        // Update streak if new day. Ignore otherwise
        Date lastDate = dateFormat.parse(interactionDate);
        long diffInMillies = Math.abs(currentDate.getTime() - lastDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diff>=1 && diff<7) {
            streak = streak + 1;
            // Send notification
            openDialog();
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
