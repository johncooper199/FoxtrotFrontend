package com.example.foxtrotfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class NewReportActivity extends AppCompatActivity {

    String[] pests = {"Pea and been weevil", "Downy mildew", "Aphid", "Chocolate spot", "Bean seed beetle", "Bean rust"};
    Double lat = null;
    Double lon = null;
    Bitmap imageBitmap = null;
    Integer severity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);
        Intent intent = getIntent();
        getSupportActionBar().setTitle("New Report");

        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, pests);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.pestName);
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        String flag = intent.getStringExtra("I_CAME_FROM");
        if((flag != null) && flag.equals("initial")){
            //you came from a1 activity
            actv.setText("Bean Rust");
        }

        actv.setThreshold(1);//will start working from first character

        // Setting Button to Go To Map Activity
        Button buttonLoc = (Button) findViewById(R.id.location);
        buttonLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPointOnMap();
            }
        });

        // Setting Button to Go To Camera Activity
        Button buttonCam = (Button) findViewById(R.id.cameraButton);
        buttonCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        // Setting Button to Go Back To Main Once Sent
        Button buttonSend = (Button) findViewById(R.id.Send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent();
            }
        });

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

    static final int PICK_MAP_POINT_REQUEST = 999;  // The request code
    private void pickPointOnMap() {
        Intent pickPointIntent = new Intent(this, MapsActivity.class);
        startActivityForResult(pickPointIntent, PICK_MAP_POINT_REQUEST);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void sendIntent() {
        EditText pestName = (EditText) findViewById(R.id.pestName);
        EditText cropName = (EditText) findViewById(R.id.cropName);
        EditText description = (EditText) findViewById(R.id.description);

        if (pestName.getText().toString().equals("")){
            Toast.makeText(this, "Please insert a pest", Toast.LENGTH_LONG).show();
            return;
        }
        if (cropName.getText().toString().equals("")){
            Toast.makeText(this, "Please insert a crop", Toast.LENGTH_LONG).show();
            return;
        }
        if (severity == null){
            Toast.makeText(this, "Please give a severity", Toast.LENGTH_LONG).show();
            return;
        }
        if (lat == null){
            Toast.makeText(this, "Please give a location", Toast.LENGTH_LONG).show();
            return;
        }
        if (imageBitmap == null){
            Toast.makeText(this, "Please give an image", Toast.LENGTH_LONG).show();
            return;
        }

        Random random = new Random();
        Date date = new Date();
        Integer idval = random.nextInt();

        try {
            ReportHTTP reportHTTP = new ReportHTTP("http://10.248.103.59:4567");
            Boolean success = reportHTTP.newReport(idval, date, lat, lon, pestName.getText().toString(), description.getText().toString(), "", imageBitmap, severity.toString());
            if (success){
                Set<String> idvals;
                SharedPreferences sharedPreferences = getSharedPreferences("REPORTS", MODE_PRIVATE);
                idvals = sharedPreferences.getStringSet("MYREPORTS", null);
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                String currentTime = (sdf.format(cal.getTime()));
                String newid = currentTime + " - " + pestName.getText().toString();
                if (idvals == null){
                    idvals = new HashSet<>();
                    idvals.add(newid);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("MYREPORTS", idvals);
                    editor.commit();
                }
                else{
                    Set<String> newvals = new HashSet<>();
                    newvals.addAll(idvals);
                    newvals.add(newid);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("MRREPORS", newvals);
                    editor.commit();

                }
                NavUtils.navigateUpFromSameTask(this);
                Toast.makeText(this, "Report Sent", Toast.LENGTH_LONG).show();
            }
            else{
                Set<String> idvals;
                SharedPreferences sharedPreferences = getSharedPreferences("REPORTS", MODE_PRIVATE);
                idvals = sharedPreferences.getStringSet("MYREPORTS", null);
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                String currentTime = (sdf.format(cal.getTime()));
                String newid = currentTime + " - " + pestName.getText().toString();
                if (idvals == null){
                    idvals = new HashSet<>();
                    idvals.add(newid);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("MYREPORTS", idvals);
                    editor.commit();
                }
                else{
                    Set<String> newvals = new HashSet<>();
                    newvals.addAll(idvals);
                    newvals.add(newid);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("MRREPORS", newvals);
                    editor.commit();

                }
                NavUtils.navigateUpFromSameTask(this);
                Toast.makeText(this, "Report Sent", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            e.printStackTrace();
            Set<String> idvals;
            SharedPreferences sharedPreferences = getSharedPreferences("REPORTS", MODE_PRIVATE);
            idvals = sharedPreferences.getStringSet("MYREPORTS", null);
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            String currentTime = (sdf.format(cal.getTime()));
            String newid = currentTime + " - " + pestName.getText().toString();
            if (idvals == null){
                idvals = new HashSet<>();
                idvals.add(newid);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet("MYREPORTS", idvals);
                editor.commit();
            }
            else{
                Set<String> newvals = new HashSet<>();
                newvals.addAll(idvals);
                newvals.add(newid);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet("MYREPORTS", newvals);
                editor.commit();

            }
            NavUtils.navigateUpFromSameTask(this);
            Toast.makeText(this, "Report Sent", Toast.LENGTH_LONG).show();
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.severity1:
                if (checked)
                    severity = 1;
                    break;
            case R.id.severity2:
                if (checked)
                    severity = 2;
                break;
            case R.id.severity3:
                if (checked)
                    severity = 3;
                break;
            case R.id.severity4:
                if (checked)
                    severity = 4;
                break;
            case R.id.severity5:
                if (checked)
                    severity = 5;
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_MAP_POINT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                LatLng latLng = (LatLng) data.getParcelableExtra("picked_point");
                Toast.makeText(this, "Point Chosen" + latLng.latitude + " " + latLng.longitude, Toast.LENGTH_LONG).show();
                lat = latLng.latitude;
                lon = latLng.longitude;
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
        }
    }
}
