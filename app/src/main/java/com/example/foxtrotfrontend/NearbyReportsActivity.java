package com.example.foxtrotfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Locale;

public class NearbyReportsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    String url = "http://10.248.103.59:4567";
    double latitude = 52.210925;
        double longitude = 0.092022;
    String[] myDataset = {};
    String[] dummy = {"Pea and been weevil - 4 miles",
            "Chocolate spot - 12 miles",
            "Bean seed beetle - 19 miles",
            "Chocolate spot - 26 miles",
            "Chocolate spot - 32 miles",
            "Chocolate spot - 40 miles",
            "Pea and been weevil - 42 miles",
            "Bean seed beetle - 46 miles",
            "Pea and been weevil - 47 miles",
            "Downy mildew - 52 miles",
            "Chocolate spot - 55 miles",
            "Chocolate spot - 64 miles",
            "Pea and been weevil - 68 miles",
            "Downy mildew - 74 miles",
            "Chocolate spot - 81 miles",
            "Chocolate spot - 90 miles",
            "Bean seed beetle - 99 miles",
            "Bean rust - 104 miles",
            "Aphid - 108 miles",
            "Downy mildew - 117 miles",
            "Chocolate spot - 126 miles",
            "Bean seed beetle - 131 miles",
            "Bean seed beetle - 133 miles",
            "Chocolate spot - 141 miles",
            "Bean rust - 148 miles",
            "Downy mildew - 152 miles",
            "Chocolate spot - 158 miles",
            "Pea and been weevil - 166 miles",
            "Bean seed beetle - 174 miles",
            "Aphid - 181 miles",
            "Pea and been weevil - 189 miles",
            "Aphid - 192 miles",
            "Chocolate spot - 196 miles",
            "Downy mildew - 200 miles",
            "Pea and been weevil - 202 miles",
            "Downy mildew - 203 miles",
            "Pea and been weevil - 208 miles",
            "Downy mildew - 212 miles",
            "Bean rust - 214 miles",
            "Aphid - 217 miles",
            "Bean seed beetle - 221 miles",
            "Pea and been weevil - 230 miles",
            "Pea and been weevil - 235 miles",
            "Chocolate spot - 239 miles",
            "Chocolate spot - 240 miles",
            "Aphid - 242 miles",
            "Chocolate spot - 250 miles",
            "Pea and been weevil - 254 miles",
            "Bean rust - 261 miles",
            "Chocolate spot - 263 miles",
            "Pea and been weevil - 265 miles",
            "Bean seed beetle - 270 miles",
            "Pea and been weevil - 278 miles",
            "Bean rust - 282 miles",
            "Bean seed beetle - 291 miles",
            "Pea and been weevil - 297 miles",
            "Downy mildew - 302 miles",
            "Chocolate spot - 309 miles",
            "Bean seed beetle - 316 miles",
            "Bean seed beetle - 321 miles",
            "Aphid - 327 miles",
            "Bean rust - 335 miles",
            "Downy mildew - 336 miles",
            "Aphid - 345 miles",
            "Chocolate spot - 347 miles",
            "Bean seed beetle - 351 miles",
            "Downy mildew - 355 miles",
            "Chocolate spot - 358 miles",
            "Chocolate spot - 362 miles",
            "Chocolate spot - 366 miles",
            "Downy mildew - 375 miles",
            "Bean seed beetle - 378 miles",
            "Chocolate spot - 380 miles",
            "Bean seed beetle - 381 miles",
            "Downy mildew - 389 miles",
            "Bean seed beetle - 390 miles",
            "Pea and been weevil - 392 miles",
            "Bean seed beetle - 394 miles",
            "Pea and been weevil - 401 miles",
            "Pea and been weevil - 402 miles",
            "Pea and been weevil - 411 miles",
            "Chocolate spot - 420 miles",
            "Pea and been weevil - 426 miles",
            "Pea and been weevil - 432 miles",
            "Bean seed beetle - 436 miles",
            "Chocolate spot - 439 miles",
            "Pea and been weevil - 444 miles",
            "Aphid - 452 miles",
            "Chocolate spot - 453 miles",
            "Chocolate spot - 458 miles",
            "Bean rust - 463 miles",
            "Aphid - 466 miles",
            "Bean rust - 472 miles",
            "Aphid - 478 miles",
            "Downy mildew - 486 miles",
            "Aphid - 487 miles",
            "Aphid - 493 miles",
            "Bean seed beetle - 497 miles",
            "Bean seed beetle - 505 miles",
            "Downy mildew - 513 miles"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            ReportHTTP reportHTTP = new ReportHTTP(url);
            ArrayList<NearbyReport> nearbyReports = reportHTTP.getInitialRadar(latitude, longitude);
            myDataset = new String[nearbyReports.size()];
            for (int i = 0; i < nearbyReports.size(); i++) {
                String temp = nearbyReports.get(i).getName() + " - " + getDistance(latitude, longitude, nearbyReports.get(i).getLatitude(), nearbyReports.get(i).getLongitude()) + " miles";
                myDataset[i] = temp;
            }
        } catch(Exception e){
            myDataset = dummy;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_reports);
        Intent intent = getIntent();
        getSupportActionBar().setTitle("Nearby Reports");

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new NearbyAdaptor(myDataset);
        recyclerView.setAdapter(mAdapter);
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

    public double getDistance(double lat1, double lon1, double lat2, double lon2) {

        double R = 6371000;
        double lat1R = Math.toRadians(lat1);
        double lat2R = Math.toRadians(lat2);
        double latDeltaR = Math.toRadians(lat2 - lat1);
        double lonDeltaR = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDeltaR / 2) * Math.sin(latDeltaR / 2) +
                Math.cos(lat1R) * Math.cos(lat2R) * Math.sin(lonDeltaR / 2) * Math.sin(lonDeltaR / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;

    }
}
