package edu.mtsu.csci3033.groceryimproved;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {


    private static final String LIST_ITEMS = "items";


    public Spinner spinner;
    private EditText itemEditText;
    private SharedPreferences christmas_list;
    private String Itemnew = "none";
    private String newFam = "none";
    private EditText newItemString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        christmas_list = getSharedPreferences("items", Context.MODE_PRIVATE);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.members,
                R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(Item_listener);

        itemEditText = (EditText) findViewById(R.id.item_view);

        ImageButton saveButton = (ImageButton)
                findViewById(R.id.SaveItem);

        saveButton.setOnClickListener(saveClickListener);


    }


     android.widget.AdapterView.OnItemSelectedListener Item_listener = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, android.view.View view, int pos, long id){
                newFam = (String) parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView<?> parent){

        }
    };

    View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(itemEditText.getText().length() > 0){
                Itemnew = itemEditText.getText().toString();
            }

            if(Itemnew.equals("none") || newFam.equals("none"))
            {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Main2Activity.this);

                builder.setMessage(R.string.missingMessage);

                builder.setPositiveButton("OK", null);

                AlertDialog errorDialog = builder.create();
                errorDialog.show();

            }
            else {

                addChristmasItem(Main2Activity.this);
                sendMessage(v);
            }


        }
    };

    public void addChristmasItem(Activity activity) {
        String ChristmasList = christmas_list.getString(newFam, null);

        if(ChristmasList != null){
            ChristmasList = ChristmasList+","+Itemnew;
        }
        else{
            ChristmasList = Itemnew;
        }

        putStringInPreferences(activity, ChristmasList);
    }

    public void putStringInPreferences(Activity activity, String ChristmasList){
        SharedPreferences.Editor editor = christmas_list.edit();
        editor.putString(newFam, ChristmasList);
        editor.commit();
    }

    /*
    public String getStringFromPreferences(Activity activity, String defaultValue, String Fam){
        SharedPreferences sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
        String temp = sharedPreferences.getString(Fam, null);
        return temp;
    }
    */

    public void  sendMessage(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
