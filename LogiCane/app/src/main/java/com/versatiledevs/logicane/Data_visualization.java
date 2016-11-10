package com.versatiledevs.logicane;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
/**
 * Created by Luis on 11/9/2016.
 */



public class Data_visualization extends AppCompatActivity {



    private TextView dataTime;
    private TextView dataMax;

    // Array for the handle vector
    private ArrayList<Double> array_HAx = new ArrayList<>();
    private ArrayList<Double> array_HAy = new ArrayList<>();
    private ArrayList<Double> array_HAz = new ArrayList<>();
    private ArrayList<Double> array_HA_AccelMagnitude = new ArrayList<>();
    private ArrayList<Double> array_HA_TransvPlaneAccelMagn = new ArrayList<>();

    // Array for the bottom vector
    private ArrayList<Double> array_BAx = new ArrayList<>();
    private ArrayList<Double> array_BAy = new ArrayList<>();
    private ArrayList<Double> array_BAz = new ArrayList<>();
    private ArrayList<Double> array_BA_AccelMagnitude = new ArrayList<>();
    private ArrayList<Double> array_BA_TransvPlaneAccelMagn = new ArrayList<>();

    // Array for the velocity signal
    private ArrayList<Double> array_Gx = new ArrayList<>();
    private ArrayList<Double> array_Gy = new ArrayList<>();
    private ArrayList<Double> array_Gz = new ArrayList<>();
    private ArrayList<Double> array_RotationalVelocityMag = new ArrayList<>();
    private ArrayList<Double> array_TranvPlaneRotVelocMag = new ArrayList<>();

    // Array for the time
    private ArrayList<String> array_StringTS = new ArrayList<>();
    private ArrayList<Double> array_DoubleTS = new ArrayList<>();

    // Array for the force signal
    private ArrayList<Double> array_f0 = new ArrayList<>();
    private ArrayList<Double> array_f1 = new ArrayList<>();
    private ArrayList<Double> array_f2 = new ArrayList<>();
    private ArrayList<Double> array_f3 = new ArrayList<>();
    private ArrayList<Double> array_f4 = new ArrayList<>();
    private ArrayList<Double> array_f5 = new ArrayList<>();
    private ArrayList<Double> array_f6 = new ArrayList<>();
    private ArrayList<Double> array_f7 = new ArrayList<>();
    private ArrayList<Double> array_gripPressureSum = new ArrayList<>();


    private ArrayList<Double> array_V_US = new ArrayList<>();
    private ArrayList<Double> array_V_LC = new ArrayList<>();
    private ArrayList<Double> array_roll = new ArrayList<>();
    private ArrayList<Double> array_pitch = new ArrayList<>();
    private ArrayList<Double> array_sstat = new ArrayList<>();

    // temporary array
    private ArrayList<Double> Dtest = new ArrayList<>();
    private ArrayList<String> Stest = new ArrayList<>();


    // this is for debugging purpose
    private ArrayList<String> array_def = new ArrayList<>();

    // firebase database
    private FirebaseDatabase database = FirebaseDatabase.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        // initialize
        //dataTime = (TextView) findViewById(R.id.data_Time);
        //dataMax  = (TextView) findViewById(R.id.data_max);

    }


    @Override
    protected void onStart(){
        super.onStart();


        DatabaseReference myRefDouble = database.getReferenceFromUrl("https://logicane-cf98b.firebaseio.com/Data/M11/DGI/item1");


        myRefDouble.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                // it returns the total count of the children.
                Long count = dataSnapshot.getChildrenCount();
                Iterator iter = dataSnapshot.getChildren().iterator(); //


                String key;
                String stringValue;
                Double value;


                //dataTime.setText(count.toString());

                for (int x = 0; x < count; x++) {


                    key = ((DataSnapshot) iter.next()).getKey();   // it gets the children for 0



                    switch (key) {

                        case "TS":
                            stringValue  = dataSnapshot.child(key).getValue(String.class);
                            array_StringTS.add(stringValue);
                            break;

                        case "HAx":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_HAx.add(value);
                            break;

                        case "HAy":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_HAy.add(value);
                            break;

                        case "HAz":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_HAz.add(value);
                            break;

                        case "BAx":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_BAx.add(value);
                            break;

                        case "BAy":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_BAy.add(value);
                            break;

                        case "BAz":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_BAz.add(value);
                            break;

                        case "Gx":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_Gx.add(value);
                            break;

                        case "Gy":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_Gy.add(value);

                            break;
                        case "Gz":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_Gz.add(value);
                            break;

                        case "f0":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_f0.add(value);
                            break;

                        case "f1":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_f1.add(value);
                            break;

                        case "f2":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_f2.add(value);
                            break;

                        case "f3":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_f3.add(value);
                            break;

                        case "f4":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_f4.add(value);
                            break;

                        case "f5":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_f5.add(value);
                            break;

                        case "f6":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_f6.add(value);
                            break;

                        case "f7":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_f7.add(value);
                            break;

                        case "V_US":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_V_US.add(value);
                            break;

                        case "V_LC":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_V_LC.add(value);
                            break;

                        case "roll":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_roll.add(value);
                            break;

                        case "pitch":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_pitch.add(value);
                            break;

                        case "sstar":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            array_sstat.add(value);
                            break;

                        default:
                            array_def.add(key);


                    }

                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {  }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {    }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {   }

            @Override
            public void onCancelled(DatabaseError databaseError) {   }
        });

    }


    public void gripPressureSum (View view){



        // read the time and convert it to double

       // dataMax.append("total number of children "+array_StringTS.size()+"\n");
        String[]time;

        for (int i = 0; i < array_StringTS.size(); i++)
        {
            //split the string every time it find the token ":"
            time = array_StringTS.get(i).split(":");

            // convert the string to double and store it in to a list
            for (int j = 0; j < time.length; j++ )
                array_DoubleTS.add(Double.parseDouble(time[j]));
        }

        // dataMax.append(array_StringTS.get(0) + " \n");


        // convert the time in milliseconds
        for (int i = 0; i < array_DoubleTS.size(); i+=4)
            Dtest.add((array_DoubleTS.get(i)*60*60*60 )+(array_DoubleTS.get(i+1) * 60*60) +(array_DoubleTS.get(i+2) *60 ) + array_DoubleTS.get(i+3));
        // dataMax.append(array_DoubleTS.get(i) +" "+array_DoubleTS.get(i+1) +" "+array_DoubleTS.get(i+2) +" "+array_DoubleTS.get(i+3) +" \n");

        //for (int i = 0; i < Dtest.size(); i++)
        //    dataMax.append(Dtest.get(i)+ "\n");

/*        double HAt_total;
        double BAt_total;

        dataMax.append(array_HAy.size()+"\n");

        for (int i =0; i < array_HAx.size(); i++){

            HAt_total = Math.sqrt( (Math.pow(array_HAy.get(i), 2)) + (Math.pow(array_HAy.get(i), 2))  );
            BAt_total = Math.sqrt( (Math.pow(array_BAy.get(i), 2)) + (Math.pow(array_BAy.get(i), 2))  );

            // add to the array
            array_HA_TransvPlaneAccelMagn.add(HAt_total);
            array_BA_TransvPlaneAccelMagn.add(BAt_total);
        }



        for (int i =0; i < test.size(); i++) {

             dataMax.append(test.get(i)+ "\n");
        }

        //  the Collection min and max returns a maximum or minimum number of the array list.
        dataMax.append("max  "+ Collections.max(array_BA_TransvPlaneAccelMagn) +"  mim "+ Collections.min(array_BA_TransvPlaneAccelMagn) +"\n");
        dataMax.append("SD  "+ standardDeviation(array_BA_TransvPlaneAccelMagn) +"\n");



        for (int i =0; i < array_HAx.size(); i++) {

            dataMax.append(array_HA_TransvPlaneAccelMagn.get(i)+"    "+ array_BA_TransvPlaneAccelMagn.get(i) +"\n");
        }
 */

/*
        double Gt_total;

        dataMax.append(array_Gx.size()+"\n");

        for (int i =0; i < array_Gx.size(); i++) {
            Gt_total = Math.sqrt((Math.pow(array_Gx.get(i), 2)) + (Math.pow(array_Gy.get(i), 2)) );

            array_TranvPlaneRotVelocMag.add(Gt_total);

        }

        for (int i = 0; i < array_Gx.size(); i++){
            dataMax.append(array_TranvPlaneRotVelocMag.get(i)+"   \n");
        }

*/

        //Rotational Velocity Magnitude
   /*     double G_total;

        dataMax.append(array_Gx.size()+"\n");
        for (int i =0; i < array_Gx.size(); i++) {
            G_total = Math.sqrt((Math.pow(array_Gx.get(i), 2)) + (Math.pow(array_Gy.get(i), 2)) + (Math.pow(array_Gz.get(i), 2)));

            // it converts the negative number to a positive
            G_total= Math.abs(G_total);

            array_RotationalVelocityMag.add(G_total);

        }

        double min = array_RotationalVelocityMag.get(0);
        double max = array_RotationalVelocityMag.get(0);

        for(Integer i: array_RotationalVelocityMag ) {
            if(i < min) min = i;
            if(i > max) max = i;
        }


        for (int i = 0; i < array_Gx.size(); i++){
            dataMax.append(array_RotationalVelocityMag.get(i)+"   \n");
        }
*/

/*
        double HA_total;
        double BA_total;


        dataMax.append(array_f0.size()+"\n");

        for (int i =0; i < array_HAz.size(); i++){

            HA_total = Math.sqrt( (Math.pow(array_HAy.get(i), 2)) + (Math.pow(array_HAy.get(i), 2)) + (Math.pow(array_HAz.get(i), 2)) );

            BA_total = Math.sqrt( (Math.pow(array_BAy.get(i), 2)) + (Math.pow(array_BAy.get(i), 2)) + (Math.pow(array_BAz.get(i), 2)) );


            // add to the array
            array_HA_AccelMagnitude.add(HA_total);
            array_BA_AccelMagnitude.add(BA_total);
        }


        for (int i =0; i < array_f0.size(); i++) {

            dataMax.append(array_HA_AccelMagnitude.get(i)+"    "+ array_BA_AccelMagnitude.get(i) +"\n");
        }

*/
    }






    //  gripPressureSum
 /*   public void gripPressureSum (View view){

        double sum;

        dataMax.append(array_f0.size()+"\n");
        for (int i =0; i < array_f0.size(); i++){

            sum = array_f0.get(i) + array_f1.get(i) + array_f2.get(i) + array_f3.get(i) + array_f4.get(i) + array_f5.get(i) + array_f6.get(i) + array_f7.get(i);

            array_gripPressureSum.add(sum);
        }

        /*for (int i =0; i < array_f0.size(); i++) {

            dataMax.append(array_gripPressureSum.get(i) +"\n");
        }*/

    //}





    public double mean (ArrayList arrayMean){

        double array=0;

        for (int i = 0; i < arrayMean.size(); i++) {

            array += (Double) arrayMean.get(i);

        }
        return (array/arrayMean.size());
    }

    public double standardDeviation (ArrayList arraySD){


        double mean1 = mean(arraySD);
        double n = (arraySD.size()-1);
        double num=0;

        for (int i = 0; i < arraySD.size(); i++){
            num += Math.pow(((Double)arraySD.get(i) - mean1),2);
        }

        return (Math.sqrt(num/n));
    }





}
