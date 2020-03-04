package com.example.foxtrotfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Set;

public class MyReportsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    String[] myDataset = {"12/01/2018 04:01 AM - Bean rust",
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
        setContentView(R.layout.activity_nearby_reports);
        Intent intent = getIntent();
        getSupportActionBar().setTitle("My Reports");

        myDataset = getMyDataset();

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

    public String[] getMyDataset(){
        Set<String> idvals;
        SharedPreferences sharedPreferences = getSharedPreferences("REPORTS", MODE_PRIVATE);
        idvals = sharedPreferences.getStringSet("IDs", null);
        String[] result;
        if (idvals == null){
            result = myDataset;
        }
        else{
            result = new String[idvals.size()];
            int current = 0;
            for (String item : idvals){
                result[current] = item;
            }
        }
        return result;
    }
}

