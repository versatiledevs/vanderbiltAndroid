package com.versatiledevs.logicane;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class patient_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_activity);

        Spinner dropdown = (Spinner)findViewById(R.id.patient_spinner);
        String[] items = new String[]{"Patient 1", "Patient 2", "Patient 3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_patient, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            /*case R.id.action_back:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            */
            case R.id.action_search:
                //startActivity(new Intent(this, SearchPatients.class));
                return true;
            /*case R.id.action_settings:
                startActivity(new Intent(this, Settings.class));
                return true;
                */
            case R.id.action_signout:
                startActivity(new Intent(patient_activity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
/*
    <item android:id="@+id/action_search"
        android:title="@string/action_search"
        android:icon="@drawable/action_search"
        app:showAsAction = "ifRoom"/>*/