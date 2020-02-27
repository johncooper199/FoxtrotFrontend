package com.example.foxtrotfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    String[] reports = {"12/01/2018 04:01 AM - Bean rust",
            "24/08/2018 09:20 PM - Chocolate spot",
            "22/04/2018 04:21 AM - Downy mildew",
            "11/01/2018 02:57 AM - Pea and been weevil",
            "10/07/2018 04:00 AM - Chocolate spot",
            "11/01/2018 03:53 PM - Pea and been weevil",
            "20/05/2018 07:14 PM - Pea and been weevil",
            "22/08/2018 06:36 PM - Aphid",
            "13/09/2018 09:37 AM - Chocolate spot",
            "19/07/2018 03:53 PM - Aphid",
            "09/05/2018 09:25 AM - Chocolate spot",
            "16/07/2018 09:23 AM - Aphid",
            "20/09/2018 12:10 PM - Bean seed beetle",
            "25/05/2018 07:47 AM - Aphid",
            "01/08/2018 06:54 AM - Bean seed beetle",
            "22/12/2018 01:54 AM - Chocolate spot",
            "24/03/2018 04:46 PM - Bean rust",
            "06/05/2018 11:26 PM - Bean seed beetle",
            "14/12/2018 01:05 PM - Bean seed beetle",
            "15/12/2018 12:15 AM - Bean seed beetle"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Intent intent = getIntent();
        getSupportActionBar().setTitle("Update Report");

        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, reports);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.reportName);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView


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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendIntent() {
        NavUtils.navigateUpFromSameTask(this);
        Toast.makeText(this, "Report Sent", Toast.LENGTH_LONG).show();
    }
}
