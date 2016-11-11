package com.versatiledevs.logicane;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;



public class Data_visualization extends AppCompatActivity {



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
    private ArrayList<Double> array_Milliseconds = new ArrayList<>();


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



    /** mean **/
    private TextView meanHandleTrans;
    private TextView meanHandleAbsolute;
    private TextView meanBaseTrans;
    private TextView meanBaseAbsolute;

    /*** SD ***/
    private TextView sdHandleTrans;
    private TextView sdHandleAbsolute;
    private TextView sdBaseTrans;
    private TextView sdBaseAbsolute;

    private TextView medianHandleTrans;
    private TextView medianHandleAbsolute;
    private TextView medianBaseTrans;
    private TextView medianBaseAbsolute;


    /***  min  ***/
    private TextView minHandleTrans;
    private TextView minHandleAbsolute;
    private TextView minBaseTrans;
    private TextView minBaseAbsolute;

    private TextView maxHandleTrans;
    private TextView maxHandleAbsolute;
    private TextView maxBaseTrans;
    private TextView maxBaseAbsolute;


    private GraphView graph;




    // this is for debugging purpose
    private ArrayList<String> array_def = new ArrayList<>();

    // firebase database
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    DecimalFormat decimalFormat = new DecimalFormat("#.###");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        // initialize
        meanHandleTrans     = (TextView) findViewById(R.id.meanHandleTransverse);
        meanHandleAbsolute = (TextView) findViewById(R.id.meanHandleAbsolute);
        meanBaseTrans       = (TextView) findViewById(R.id.meanBaseTransverse);
        meanBaseAbsolute    = (TextView) findViewById(R.id.meanBaseAbsolute);

        sdHandleTrans       = (TextView) findViewById(R.id.sdHandleTransverse);
        sdHandleAbsolute    = (TextView) findViewById(R.id.sdHandleAbsolute);
        sdBaseTrans         = (TextView) findViewById(R.id.sdBaseTransverse);
        sdBaseAbsolute      = (TextView) findViewById(R.id.sdBaseAbsolute);

        medianHandleTrans      = (TextView) findViewById(R.id.medianHandleTransverse);
        medianHandleAbsolute   = (TextView) findViewById(R.id.medianHandleAbsolute);
        medianBaseTrans        = (TextView) findViewById(R.id.medianBaseTransverse);
        medianBaseAbsolute     = (TextView) findViewById(R.id.medianBaseAbsolute);

        minHandleTrans      = (TextView) findViewById(R.id.minHandleTransverse);
        minHandleAbsolute   = (TextView) findViewById(R.id.minHandleAbsolute);
        minBaseTrans        = (TextView) findViewById(R.id.minBaseTransverse);
        minBaseAbsolute     = (TextView) findViewById(R.id.minBaseAbsolute);

        maxHandleTrans      = (TextView) findViewById(R.id.maxHandleTransverse);
        maxHandleAbsolute   = (TextView) findViewById(R.id.maxHandleAbsolute);
        maxBaseTrans        = (TextView) findViewById(R.id.maxBaseTransverse);
        maxBaseAbsolute     = (TextView) findViewById(R.id.maxBaseAbsolute);

        //GraphView graph = (GraphView) findViewById(R.id.displayView);

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


    }// end of onStart


    public void data (View view){


        /***  FILLING THE TABLE: HANDLE ****/

        //function calls
        convertTimeMilliseconds();
        accelerationMagnitude();
        rotationalVelocityMagnitude();
        transversePlaneAccelerationMagnitude();
        gripPressureSum();
        TransPlaneRotatVelocMag();


        // mean
        meanHandleTrans.setText(decimalFormat.format(mean(array_HA_TransvPlaneAccelMagn)));
        meanHandleAbsolute.setText(decimalFormat.format(mean(array_HA_AccelMagnitude)));
        meanBaseTrans.setText(decimalFormat.format(mean(array_BA_TransvPlaneAccelMagn)));
        meanBaseAbsolute.setText(decimalFormat.format(mean(array_BA_AccelMagnitude)));

        //SD
        sdHandleTrans.setText(decimalFormat.format(standardDeviation(array_HA_TransvPlaneAccelMagn)));
        sdHandleAbsolute.setText(decimalFormat.format(standardDeviation(array_HA_AccelMagnitude)));
        sdBaseTrans.setText(decimalFormat.format(standardDeviation(array_BA_TransvPlaneAccelMagn)));
        sdBaseAbsolute.setText(decimalFormat.format(standardDeviation(array_BA_AccelMagnitude)));


        // median
        medianHandleTrans.setText(decimalFormat.format(median(array_HA_TransvPlaneAccelMagn)));
        medianHandleAbsolute.setText(decimalFormat.format(median(array_HA_AccelMagnitude)));
        medianBaseTrans.setText(decimalFormat.format(median(array_BA_TransvPlaneAccelMagn)));
        medianBaseAbsolute.setText(decimalFormat.format(median(array_BA_AccelMagnitude)));


        // min
        minHandleTrans.setText(decimalFormat.format(min(array_HA_TransvPlaneAccelMagn)));
        minHandleAbsolute.setText(decimalFormat.format(min(array_HA_AccelMagnitude)));
        minBaseTrans.setText(decimalFormat.format(min(array_BA_TransvPlaneAccelMagn)));
        minBaseAbsolute.setText(decimalFormat.format(min(array_BA_AccelMagnitude)));

        // max
        maxHandleTrans.setText(decimalFormat.format(max(array_HA_TransvPlaneAccelMagn)));
        maxHandleAbsolute.setText(decimalFormat.format(max(array_HA_AccelMagnitude)));
        maxBaseTrans.setText(decimalFormat.format(max(array_BA_TransvPlaneAccelMagn)));
        maxBaseAbsolute.setText(decimalFormat.format(max(array_BA_AccelMagnitude)));


        GraphView graph = (GraphView) findViewById(R.id.displayView);


        LineGraphSeries <DataPoint> series1 = new LineGraphSeries<>();
        for(int i =0; i<array_Milliseconds.size(); i++) {

            series1.appendData(new DataPoint(i, array_HA_TransvPlaneAccelMagn.get(i)), true, 100);
        }


        LineGraphSeries <DataPoint> series2 = new LineGraphSeries<>();
        for(int i =0; i<array_Milliseconds.size(); i++) {

            series2.appendData(new DataPoint(i, array_HA_AccelMagnitude.get(i)), true, 100);
        }


        LineGraphSeries <DataPoint> series3 = new LineGraphSeries<>();
        for(int i =0; i<array_Milliseconds.size(); i++) {

            series3.appendData(new DataPoint(i, array_BA_TransvPlaneAccelMagn.get(i)), true, 100);
        }


        LineGraphSeries <DataPoint> series4 = new LineGraphSeries<>();
        for(int i =0; i<array_Milliseconds.size(); i++) {

            series4.appendData(new DataPoint(i, array_BA_AccelMagnitude.get(i)), true, 100);
        }


        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(3);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(array_Milliseconds.size()+20);


        // This option does not work on my laptop
        graph.getViewport().setScrollable(true);    // enables horizontal scrolling
        graph.getViewport().setScrollableY(true);   // enables vertical scrolling
        graph.getViewport().setScalable(true);      // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true);     // enables vertical zooming and scrolling

        series2.setColor(Color.RED);
        series3.setColor(Color.BLUE);
        series4.setColor(Color.GREEN);

        graph.addSeries(series1);
        graph.addSeries(series2);
        graph.addSeries(series3);
        graph.addSeries(series4);

    } //end of data

    public void convertTimeMilliseconds(){

        String[]time; //it store the split string


        // This for loop converts the hours that is in string to double
        // so, it can be convert it to milliseconds
        for (int i = 0; i < array_StringTS.size(); i++)
        {
            //split the string every time it find the token ":"
            time = array_StringTS.get(i).split(":");

            // convert the string to double and store it in to a list
            for (int j = 0; j < time.length; j++ )
                array_DoubleTS.add(Double.parseDouble(time[j]));
        }


        // convert the time in milliseconds
        for (int i = 0; i < array_DoubleTS.size(); i+=4)
            array_Milliseconds.add((array_DoubleTS.get(i)*60*60*60 )+(array_DoubleTS.get(i+1) * 60*60) +(array_DoubleTS.get(i+2) *60 ) + array_DoubleTS.get(i+3));

    }

    public void accelerationMagnitude() {

        double HA_total;
        double BA_total;

        for (int i = 0; i < array_HAz.size(); i++) {

            HA_total = Math.sqrt((Math.pow(array_HAx.get(i), 2)) + (Math.pow(array_HAy.get(i), 2)) + (Math.pow(array_HAz.get(i), 2)));

            BA_total = Math.sqrt((Math.pow(array_BAx.get(i), 2)) + (Math.pow(array_BAy.get(i), 2)) + (Math.pow(array_BAz.get(i), 2)));

            // add to the array
            array_HA_AccelMagnitude.add(HA_total);
            array_BA_AccelMagnitude.add(BA_total);

        }
    }



    //Rotational Velocity Magnitude
    public void rotationalVelocityMagnitude(){

        double G_total;

        for (int i =0; i < array_Gx.size(); i++){

            G_total = Math.sqrt((Math.pow(array_Gx.get(i), 2)) + (Math.pow(array_Gy.get(i), 2)) + (Math.pow(array_Gz.get(i), 2)));
            array_RotationalVelocityMag.add(G_total);
        }
    }



    //  gripPressureSum
    public void gripPressureSum (){

        double sum;

        for (int i =0; i < array_f0.size(); i++){

            sum = array_f0.get(i) + array_f1.get(i) + array_f2.get(i) + array_f3.get(i) + array_f4.get(i) + array_f5.get(i) + array_f6.get(i) + array_f7.get(i);

            array_gripPressureSum.add(sum);
        }

    }



    public void TransPlaneRotatVelocMag (){

        double Gt_total;

        for (int i =0; i < array_Gx.size(); i++) {

            Gt_total = Math.sqrt((Math.pow(array_Gx.get(i), 2)) + (Math.pow(array_Gy.get(i), 2)) );
            array_TranvPlaneRotVelocMag.add(Gt_total);
        }
    }



    public void transversePlaneAccelerationMagnitude(){

        double HAt_total;
        double BAt_total;

        for (int i =0; i < array_HAx.size(); i++){

            HAt_total = Math.sqrt( (Math.pow(array_HAx.get(i), 2)) + (Math.pow(array_HAy.get(i), 2))  );
            BAt_total = Math.sqrt( (Math.pow(array_BAx.get(i), 2)) + (Math.pow(array_BAy.get(i), 2))  );

            // add to the array
            array_HA_TransvPlaneAccelMagn.add(HAt_total);
            array_BA_TransvPlaneAccelMagn.add(BAt_total);
        }

    }



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


    public double median(ArrayList arrayMedian){

        Collections.sort(arrayMedian);

        int size = arrayMedian.size()/2;

        if (arrayMedian.size() % 2 == 0)

           return ((Double) arrayMedian.get(size) + (Double)arrayMedian.get(size - 1));

        else
            return ((Double)arrayMedian.get(size));

    }



    //  the Collection min and max returns a maximum or minimum number of the array list.
    public double min (ArrayList arrayMin){

        double min;
        min = (Double) Collections.min(arrayMin);
        return min;
    }


    public double max (ArrayList arrayMax){

        double max;
        max = (Double) Collections.max(arrayMax);
        return max;
    }




}// end of data_visualisation
