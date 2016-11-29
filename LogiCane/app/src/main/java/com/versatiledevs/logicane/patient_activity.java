package com.versatiledevs.logicane;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.button;

public class patient_activity extends AppCompatActivity {
    //variables for spinner strings and lists
    String patient;
    String spinner_value;
    String test;
    String test_value;
    final List<String> patient_list = new ArrayList<String>();
    //final List<String> test_list = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_activity);

        ////create a path to connect to database, create an object based on database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReferenceFromUrl("https://logicane-cf98b.firebaseio.com/Data");

        //loop through the list of patients
        //add them to patient list
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Long count = snapshot.getChildrenCount();
                Iterator iter = snapshot.getChildren().iterator();
                for (int x = 0; x < count; x++) {
                    patient = ((DataSnapshot) iter.next()).getKey();
                    //Getting the data from snapshot
                    //patient = postSnapshot.child("Data").getValue(String.class);
                    patient_list.add(patient);
                }
                //spinner for the patient list
                final Spinner dropdown = (Spinner) findViewById(R.id.patient_spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(patient_activity.this, android.R.layout.simple_spinner_dropdown_item, patient_list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropdown.setAdapter(adapter);

                //get value chosen in spinner
                spinner_value = dropdown.getSelectedItem().toString();

                //test spinner ------------not complete!!!!!
                Spinner spinner_test = (Spinner) findViewById(R.id.test_spinner);
                String[] test_list = {"DGI", "SM"};
                ArrayAdapter<String> test_adapter = new ArrayAdapter<String>(patient_activity.this, android.R.layout.simple_spinner_dropdown_item, test_list);
                test_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_test.setAdapter(adapter);



            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //error handling
            }
        });
        //on button click
        //pass spinner value to next intent
        Button button;
        button = (Button)findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patient_activity.this, Data_visualization.class);
                intent.putExtra("patient", spinner_value);
                startActivity(intent);
            }
        });  //end setOnCLickListener


    }             //end onCreate
        //menu inflater used for the back button and settings/signout
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_patient, menu);
            return true;

        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle item selection
            switch (item.getItemId()) {
                case R.id.action_search:
                    //startActivity(new Intent(this, SearchPatients.class));
                    return true;
                case R.id.action_signout:
                    startActivity(new Intent(patient_activity.this, LoginActivity.class));
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    //this button will send you directly to a data view for testing
    public void DataVisualizationPublic(View view) {

        String key = "item1";   //this is the item that is send to data visualisation
        Intent intent = new Intent(patient_activity.this, Data_visualization.class);
        intent.putExtra("item", key);
        Toast.makeText(this, "Data Visualization " + key, Toast.LENGTH_SHORT).show();
        startActivity(intent);

        // Toast.makeText(this, "Data Visualization ", Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(this, Data_visualization.class));
        //finish();
    }
}
