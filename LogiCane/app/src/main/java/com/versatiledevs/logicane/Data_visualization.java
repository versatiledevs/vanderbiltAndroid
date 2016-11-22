
package com.versatiledevs.logicane;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import android.view.View.OnClickListener;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;



public class Data_visualization extends AppCompatActivity {

    //struct to hold data
    private final class Signal{
        //time
        public double time;
        //Handle properties
        public double m_HAx, m_HAy, m_HAz,
                m_HA_AccelMagnitude,
                m_HA_TransvPlaneAccelMag;


        //Ground vector properties
        public double m_BAx, m_BAy, m_BAz,
                m_BA_AccelMagnitude,
                m_BA_TransvPlaneAccelMag;

        //Velocity signal properties
        public double m_Gx, m_Gy, m_Gz,
                m_RotationalVelocityMag,
                m_TranvPlaneRotVelocMag;

        //Force properties
        public double m_F0,m_F1, m_F2, m_F3, m_F4, m_F5, m_F6, m_F7,
                m_V_US,
                m_V_LC,
                m_Pitch,
                m_Sstat,
                m_GripPressureSum;
    }


    //create an array of signal
    private ArrayList<Signal> patientInfo= new ArrayList<>();


    /** mean **/
    private TextView meanHandleTrans,
            meanHandleAbsolute,
            meanBaseTrans,
            meanBaseAbsolute;

    /*** SD ***/
    private TextView sdHandleTrans,
            sdHandleAbsolute,
            sdBaseTrans,
            sdBaseAbsolute;

    private TextView medianHandleTrans,
            medianHandleAbsolute,
            medianBaseTrans,
            medianBaseAbsolute;


    /***  min  ***/
    private TextView minHandleTrans,
            minHandleAbsolute,
            minBaseTrans,
            minBaseAbsolute;

    private TextView maxHandleTrans,
            maxHandleAbsolute,
            maxBaseTrans,
            maxBaseAbsolute;

    private RangeSeekBar  seekBarDouble;
    private TextView minTextDouble,
            maxTextDouble;


    private TextView startTime, endTime;

    final Double min = 0.0;

    // this is for debugging purpose
    private ArrayList<String> array_def = new ArrayList<>();

    // firebase database
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String urlDatabase = "https://logicane-cf98b.firebaseio.com/Data/M11/DGI/";

    DecimalFormat decimalFormat = new DecimalFormat("#.###");


    private GraphView graph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);


         graph = (GraphView) findViewById(R.id.displayView);

        // initialize
        meanHandleTrans     = (TextView) findViewById(R.id.meanHandleTransverse);
        meanHandleAbsolute  = (TextView) findViewById(R.id.meanHandleAbsolute);
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

        startTime           = (TextView) findViewById(R.id.startTextView);
        endTime             = (TextView) findViewById(R.id.endTextView);

        final Button weightBearingButton1 = (Button)  findViewById(R.id.weightBearingButton);
        final Button orientationButton1   = (Button)  findViewById(R.id.orientationButton);
        final Button speedButton1         = (Button)  findViewById(R.id.speedButton);
        final Button predictionButton1    = (Button)  findViewById(R.id.predictionButton);


        weightBearingButton1.setOnClickListener(weightBearingButtonListener);
        orientationButton1.setOnClickListener( orientationButtonListener );
        speedButton1.setOnClickListener(speedButtonListener);
        predictionButton1.setOnClickListener(predictionButtonListener);




        DatabaseReference myRefDouble = database.getReferenceFromUrl(urlDatabase + "item1");

        myRefDouble.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                // it returns the total count of the children.
                Long count = dataSnapshot.getChildrenCount();
                Iterator iter = dataSnapshot.getChildren().iterator(); //

                //temporary vars
                String key;
                String stringValue;
                Double value;
                String[] timeToDoubleStr; //used for conversion of time
                double [] timeToDouble=new double[4];

                Signal userSignal= new Signal();
                Signal userSignalCopy= new Signal();

                double  HA_total,
                        BA_total,
                        G_total,
                        grip_sum,
                        Gt_total,
                        HAtrans_total,
                        BAtrans_total;


                //dataTime.setText(count.toString());

                for (int x = 0; x < count; x++) {
                    key = ((DataSnapshot) iter.next()).getKey();   // it gets the children for 0

                    switch (key) {

                        case "TS":
                            stringValue  = dataSnapshot.child(key).getValue(String.class);
                            //convert the time from string to double
                            timeToDoubleStr = stringValue.split(":");
                            for (int j = 0; j < timeToDoubleStr.length; j++ )
                                timeToDouble[j]=Double.parseDouble(timeToDoubleStr[j]);

                            userSignal.time= ((timeToDouble[0]*60*60*60)+(timeToDouble[1] * 60*60) +(timeToDouble[2] *60 ) + timeToDouble[3]);
                            break;

                        case "HAx":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_HAx= value;
                            break;

                        case "HAy":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_HAy=value;
                            break;

                        case "HAz":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_HAz= value;
                            break;

                        case "BAx":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_BAx= value;
                            break;

                        case "BAy":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_BAy=value;
                            break;

                        case "BAz":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_BAz= value;
                            break;

                        case "Gx":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_Gx= value;
                            break;

                        case "Gy":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_Gy=value;
                            break;
                        case "Gz":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_Gz= value;
                            break;

                        case "f0":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_F0= value;
                            break;

                        case "f1":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_F1= value;
                            break;

                        case "f2":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_F2= value;
                            break;

                        case "f3":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_F3= value;
                            break;

                        case "f4":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_F4= value;
                            break;

                        case "f5":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_F5= value;
                            break;

                        case "f6":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_F6= value;
                            break;

                        case "f7":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_F7= value;
                            break;

                        case "V_US":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_F7= value;
                            break;

                        case "V_LC":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_V_LC= value;
                            break;

                        case "roll":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_V_US= value;
                            break;

                        case "pitch":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_Pitch= value;
                            break;

                        case "sstar":
                            value = dataSnapshot.child(key).getValue(Double.class);
                            userSignal.m_Sstat= value;
                            break;

                        default:
                            array_def.add(key);
                    }// and of switch statement
                }//end of for-loop

                //Computer the Base and Handle Acceleration
                HA_total = Math.sqrt((Math.pow(userSignal.m_HAx, 2)) + (Math.pow(userSignal.m_HAy, 2)) + (Math.pow(userSignal.m_HAz, 2)));
                BA_total = Math.sqrt((Math.pow(userSignal.m_BAx, 2)) + (Math.pow(userSignal.m_BAy, 2)) + (Math.pow(userSignal.m_BAz, 2)));

                userSignal.m_HA_AccelMagnitude = HA_total;
                userSignal.m_BA_AccelMagnitude = BA_total;

                //Compute the Rotational Velocity Magnitude
                G_total = Math.sqrt((Math.pow(userSignal.m_Gx, 2)) + (Math.pow(userSignal.m_Gy, 2)) + (Math.pow(userSignal.m_Gz, 2)));

                userSignal.m_RotationalVelocityMag = G_total;

                //Compute Transverse Plane Rotation  Velocity Magnitude
                Gt_total = Math.sqrt((Math.pow(userSignal.m_Gx, 2)) + (Math.pow(userSignal.m_Gy, 2)));
                userSignal.m_TranvPlaneRotVelocMag= Gt_total;

                //Compute Transverse Plane Acceleration  Velocity Magnitude
                HAtrans_total = Math.sqrt((Math.pow(userSignal.m_HAx, 2)) + (Math.pow(userSignal.m_HAy, 2)));
                BAtrans_total = Math.sqrt((Math.pow(userSignal.m_BAx, 2)) + (Math.pow(userSignal.m_BAy, 2)));

                userSignal.m_HA_TransvPlaneAccelMag= HAtrans_total;
                userSignal.m_BA_TransvPlaneAccelMag= BAtrans_total;

                //compute gripPressureSum
                grip_sum = (userSignal.m_F0 + userSignal.m_F1 + userSignal.m_F2 + userSignal.m_F3 + userSignal.m_F4 + userSignal.m_F5 + userSignal.m_F6 + userSignal.m_F7);

                userSignal.m_GripPressureSum = grip_sum;

                //Add the object to the list
                patientInfo.add(userSignal);
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

/*
       Double min = 0.0;
      final double max = patientInfo.size();
        seekBarDouble = (RangeSeekBar) findViewById(R.id.rangerSelector);
        minTextDouble = (TextView) findViewById(R.id.startTextView);
        maxTextDouble = (TextView) findViewById(R.id.endTextView);
        seekBarDouble.setRangeValues(min, max); // set range type of seekbar is Double

        seekBarDouble.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {


             //   if (min != minValue) {
               //     Toast.makeText(Data_visualization.this, "Min border changed" + minValue, Toast.LENGTH_SHORT).show();
                    //startTime.setText(patientInfo.get(.time));

              //  }

               /* if (max != maxValue ) {
                    Toast.makeText(Data_visualization.this, "Max border changed", Toast.LENGTH_SHORT).show();
                }

             //  min =  minValue;
              //  max = (Double) maxValue;
            }
        })*/
    } // end of onCreate






    public OnClickListener weightBearingButtonListener = new OnClickListener() {
        @Override
        public void onClick (View v){

            GraphView graph = (GraphView) findViewById(R.id.displayView);

            LineGraphSeries <DataPoint> series1 = new LineGraphSeries<>();
            LineGraphSeries <DataPoint> series2 = new LineGraphSeries<>();

            ArrayList<Double> sortedBy_gripPressuresum= new ArrayList<>();
            ArrayList<Double> sortedBy_L_VC= new ArrayList<>();

            //make a copy ...
            for(int i=0; i< patientInfo.size(); i++){
                sortedBy_gripPressuresum.add(patientInfo.get(i).m_GripPressureSum);
                sortedBy_L_VC.add(patientInfo.get(i).m_V_LC);

            }
            Collections.sort(sortedBy_gripPressuresum);
            Collections.sort(sortedBy_L_VC);

            clearTable ();

            // mean
            meanHandleTrans.setText(decimalFormat.format(mean(sortedBy_gripPressuresum)));
            meanHandleAbsolute.setText(decimalFormat.format(mean(sortedBy_L_VC)));
            //SD
            sdHandleTrans.setText(decimalFormat.format(standardDeviation(sortedBy_gripPressuresum)));
            sdHandleAbsolute.setText(decimalFormat.format(standardDeviation(sortedBy_L_VC)));
            // median
            medianHandleTrans.setText(decimalFormat.format(median(sortedBy_gripPressuresum)));
            medianHandleAbsolute.setText(decimalFormat.format(median(sortedBy_L_VC)));
            // min
            minHandleTrans.setText(decimalFormat.format(sortedBy_gripPressuresum.get(0)));
            minHandleAbsolute.setText(decimalFormat.format(sortedBy_L_VC.get(0)));
            // max
            maxHandleTrans.setText(decimalFormat.format(sortedBy_gripPressuresum.get(sortedBy_gripPressuresum.size()-1)));
            maxHandleAbsolute.setText(decimalFormat.format(sortedBy_L_VC.get(sortedBy_L_VC.size()-1)));

            graph.removeAllSeries();

            for(int i =0; i< sortedBy_gripPressuresum.size(); i++) {
                series1.appendData(new DataPoint(i, patientInfo.get(i).m_GripPressureSum), true, sortedBy_gripPressuresum.size());
                series2.appendData(new DataPoint(i, patientInfo.get(i).m_V_LC), true, sortedBy_L_VC.size());
            }

            // set manual X bounds
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(sortedBy_gripPressuresum.get(0));
            graph.getViewport().setMaxY(sortedBy_gripPressuresum.get(sortedBy_gripPressuresum.size()-1));

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(sortedBy_L_VC.size());


            // This option does not work in my laptop
            graph.getViewport().setScrollable(true);    // enables horizontal scrolling
            graph.getViewport().setScrollableY(true);   // enables vertical scrolling
            graph.getViewport().setScalable(true);      // enables horizontal zooming and scrolling
            graph.getViewport().setScalableY(true);     // enables vertical zooming and scrolling

            series1.setColor(Color.BLACK);
            series2.setColor(Color.RED);
            series1.setThickness(3);
            series2.setThickness(3);

            series1.setTitle(" Curve 1");

            graph.addSeries(series1);

            graph.getSecondScale().addSeries(series2);
            // the y bounds are always manual for second scale
            graph.getSecondScale().setMinY(sortedBy_L_VC.get(0));
            graph.getSecondScale().setMaxY(sortedBy_L_VC.get(sortedBy_L_VC.size()-1));
            series1.setTitle(" Curve 2");
            graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);
            graph.addSeries(series2);


        }
    };  // end of weightBearing listener


    public OnClickListener orientationButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            GraphView graph = (GraphView) findViewById(R.id.displayView);

            LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>();
            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>();
            LineGraphSeries<DataPoint> series5 = new LineGraphSeries<>();
            LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>();

            ArrayList<Double> sortedBy_BA_Aceel = new ArrayList<>();
            ArrayList<Double> sortedBy_BA_TAceel = new ArrayList<>();
            ArrayList<Double> sortedBy_HA_Aceel = new ArrayList<>();
            ArrayList<Double> sortedBy_HA_TAceel = new ArrayList<>();

            //make a copy ...
            for (int i = 0; i < patientInfo.size(); i++) {
                sortedBy_HA_TAceel.add(patientInfo.get(i).m_HA_TransvPlaneAccelMag);
                sortedBy_HA_Aceel.add(patientInfo.get(i).m_HA_AccelMagnitude);
                sortedBy_BA_TAceel.add(patientInfo.get(i).m_BA_TransvPlaneAccelMag);
                sortedBy_BA_Aceel.add(patientInfo.get(i).m_BA_AccelMagnitude);
            }
            Collections.sort(sortedBy_HA_TAceel);
            Collections.sort(sortedBy_HA_Aceel);
            Collections.sort(sortedBy_BA_TAceel);
            Collections.sort(sortedBy_BA_Aceel);


            // mean
            meanHandleTrans.setText(decimalFormat.format(mean(sortedBy_HA_TAceel)));
            meanHandleAbsolute.setText(decimalFormat.format(mean(sortedBy_HA_Aceel)));
            meanBaseTrans.setText(decimalFormat.format(mean(sortedBy_BA_TAceel)));
            meanBaseAbsolute.setText(decimalFormat.format(mean(sortedBy_BA_Aceel)));
            //SD
            sdHandleTrans.setText(decimalFormat.format(standardDeviation(sortedBy_HA_TAceel)));
            sdHandleAbsolute.setText(decimalFormat.format(standardDeviation(sortedBy_HA_Aceel)));
            sdBaseTrans.setText(decimalFormat.format(standardDeviation(sortedBy_BA_TAceel)));
            sdBaseAbsolute.setText(decimalFormat.format(standardDeviation(sortedBy_BA_Aceel)));
            // median
            medianHandleTrans.setText(decimalFormat.format(median(sortedBy_HA_TAceel)));
            medianHandleAbsolute.setText(decimalFormat.format(median(sortedBy_HA_Aceel)));
            medianBaseTrans.setText(decimalFormat.format(median(sortedBy_BA_TAceel)));
            medianBaseAbsolute.setText(decimalFormat.format(median(sortedBy_BA_Aceel)));
            //skew
       /*     medianHandleTrans.setText(decimalFormat.format(skew(sortedBy_HA_TAceel)));
            medianHandleAbsolute.setText(decimalFormat.format(skew(sortedBy_HA_Aceel)));
            medianBaseTrans.setText(decimalFormat.format(skew(sortedBy_BA_TAceel)));
            medianBaseAbsolute.setText(decimalFormat.format(skew(sortedBy_BA_Aceel)));
*/
            // min
            minHandleTrans.setText(decimalFormat.format(sortedBy_HA_TAceel.get(0)));
            minHandleAbsolute.setText(decimalFormat.format(sortedBy_HA_Aceel.get(0)));
            minBaseTrans.setText(decimalFormat.format(sortedBy_BA_TAceel.get(0)));
            minBaseAbsolute.setText(decimalFormat.format(sortedBy_BA_Aceel.get(0)));
            // max
            maxHandleTrans.setText(decimalFormat.format(sortedBy_HA_TAceel.get(sortedBy_HA_TAceel.size() - 1)));
            maxHandleAbsolute.setText(decimalFormat.format(sortedBy_HA_Aceel.get(sortedBy_HA_Aceel.size() - 1)));
            maxBaseTrans.setText(decimalFormat.format(sortedBy_BA_TAceel.get(sortedBy_BA_TAceel.size() - 1)));
            maxBaseAbsolute.setText(decimalFormat.format(sortedBy_BA_Aceel.get(sortedBy_BA_Aceel.size() - 1)));

            graph.clearSecondScale();
            graph.removeAllSeries();


            for (int i = 0; i < sortedBy_HA_TAceel.size(); i++){
                series3.appendData(new DataPoint(i, patientInfo.get(i).m_BA_AccelMagnitude), true, sortedBy_HA_TAceel.size());
                series4.appendData(new DataPoint(i, patientInfo.get(i).m_BA_TransvPlaneAccelMag), true, sortedBy_HA_Aceel.size());
                series5.appendData(new DataPoint(i, patientInfo.get(i).m_HA_AccelMagnitude), true, sortedBy_BA_TAceel.size());
                series2.appendData(new DataPoint(i, patientInfo.get(i).m_HA_TransvPlaneAccelMag), true, sortedBy_BA_Aceel.size());

            }

            // set manual X bounds
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(3);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(sortedBy_BA_Aceel.size());


            // This option does not work in my laptop
            graph.getViewport().setScrollable(true);    // enables horizontal scrolling
            graph.getViewport().setScrollableY(true);   // enables vertical scrolling
            graph.getViewport().setScalable(true);      // enables horizontal zooming and scrolling
            graph.getViewport().setScalableY(true);     // enables vertical zooming and scrolling

            series3.setTitle(" Curve 1");

            series3.setColor(Color.BLUE);
            series4.setColor(Color.GREEN);
            series5.setColor(Color.YELLOW);
            series2.setColor(Color.RED);

            series3.setThickness(3);
            series4.setThickness(3);
            series5.setThickness(3);
            series2.setThickness(3);

            graph.addSeries(series3);
            graph.addSeries(series4);
            graph.addSeries(series5);
            graph.addSeries(series2);


        }
    }; // end of orientation listener





    public OnClickListener speedButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            GraphView graph = (GraphView) findViewById(R.id.displayView);

            LineGraphSeries <DataPoint> series1 = new LineGraphSeries<>();
            LineGraphSeries <DataPoint> series2 = new LineGraphSeries<>();
            LineGraphSeries <DataPoint> series3 = new LineGraphSeries<>();
            LineGraphSeries <DataPoint> series4 = new LineGraphSeries<>();

            ArrayList<Double> sortedBy_BA_Aceel= new ArrayList<>();
            ArrayList<Double> sortedBy_BA_TAceel= new ArrayList<>();
            ArrayList<Double> sortedBy_HA_Aceel= new ArrayList<>();
            ArrayList<Double> sortedBy_HA_TAceel= new ArrayList<>();

            //make a copy ...
            for(int i=0; i< patientInfo.size(); i++){
                sortedBy_BA_Aceel.add(patientInfo.get(i).m_BA_AccelMagnitude);
                sortedBy_BA_TAceel.add(patientInfo.get(i).m_BA_TransvPlaneAccelMag);
                sortedBy_HA_Aceel.add(patientInfo.get(i).m_HA_AccelMagnitude);
                sortedBy_HA_TAceel.add(patientInfo.get(i).m_HA_TransvPlaneAccelMag);
            }
            Collections.sort(sortedBy_BA_Aceel);
            Collections.sort(sortedBy_BA_TAceel);
            Collections.sort(sortedBy_HA_Aceel);
            Collections.sort(sortedBy_HA_TAceel);

            // mean
            meanHandleTrans.setText(decimalFormat.format(mean(sortedBy_HA_TAceel)));
            meanHandleAbsolute.setText(decimalFormat.format(mean(sortedBy_HA_Aceel)));
            meanBaseTrans.setText(decimalFormat.format(mean(sortedBy_BA_TAceel)));
            meanBaseAbsolute.setText(decimalFormat.format(mean(sortedBy_BA_Aceel)));
            //SD
            sdHandleTrans.setText(decimalFormat.format(standardDeviation(sortedBy_HA_TAceel)));
            sdHandleAbsolute.setText(decimalFormat.format(standardDeviation(sortedBy_HA_Aceel)));
            sdBaseTrans.setText(decimalFormat.format(standardDeviation(sortedBy_BA_TAceel)));
            sdBaseAbsolute.setText(decimalFormat.format(standardDeviation(sortedBy_BA_Aceel)));
            // median
            medianHandleTrans.setText(decimalFormat.format(median(sortedBy_HA_TAceel)));
            medianHandleAbsolute.setText(decimalFormat.format(median(sortedBy_HA_Aceel)));
            medianBaseTrans.setText(decimalFormat.format(median(sortedBy_BA_TAceel)));
            medianBaseAbsolute.setText(decimalFormat.format(median(sortedBy_BA_Aceel)));
            // min
            minHandleTrans.setText(decimalFormat.format(sortedBy_HA_TAceel.get(0)));
            minHandleAbsolute.setText(decimalFormat.format(sortedBy_HA_Aceel.get(0)));
            minBaseTrans.setText(decimalFormat.format(sortedBy_BA_TAceel.get(0)));
            minBaseAbsolute.setText(decimalFormat.format(sortedBy_BA_Aceel.get(0)));
            // max
            maxHandleTrans.setText(decimalFormat.format(sortedBy_HA_TAceel.get(sortedBy_HA_TAceel.size()-1)));
            maxHandleAbsolute.setText(decimalFormat.format(sortedBy_HA_Aceel.get(sortedBy_HA_Aceel.size()-1)));
            maxBaseTrans.setText(decimalFormat.format(sortedBy_BA_TAceel.get(sortedBy_BA_TAceel.size()-1)));
            maxBaseAbsolute.setText(decimalFormat.format(sortedBy_BA_Aceel.get(sortedBy_BA_Aceel.size()-1)));

            graph.clearSecondScale();
            graph.removeAllSeries();

            for(int i =0; i< sortedBy_HA_TAceel.size(); i++) {
                series1.appendData(new DataPoint(i, patientInfo.get(i).m_BA_AccelMagnitude), true, sortedBy_HA_TAceel.size());
                series2.appendData(new DataPoint(i, patientInfo.get(i).m_BA_TransvPlaneAccelMag), true, sortedBy_HA_TAceel.size());
                series3.appendData(new DataPoint(i, patientInfo.get(i).m_HA_AccelMagnitude), true, sortedBy_HA_TAceel.size());
                series4.appendData(new DataPoint(i, patientInfo.get(i).m_HA_TransvPlaneAccelMag), true, sortedBy_HA_TAceel.size());
            }

            // set manual X bounds
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(3);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(sortedBy_BA_Aceel.size());

            // This option does not work in my laptop
            graph.getViewport().setScrollable(true);    // enables horizontal scrolling
            graph.getViewport().setScrollableY(true);   // enables vertical scrolling
            graph.getViewport().setScalable(true);      // enables horizontal zooming and scrolling
            graph.getViewport().setScalableY(true);     // enables vertical zooming and scrolling

            series1.setTitle(" Curve 1");

            series2.setColor(Color.BLACK);
            series1.setColor(Color.RED);
            series3.setColor(Color.BLUE);
            series4.setColor(Color.GREEN);

            series1.setThickness(3);
            series2.setThickness(3);
            series3.setThickness(3);
            series4.setThickness(3);

            graph.addSeries(series1);
            graph.addSeries(series2);
            graph.addSeries(series3);
            graph.addSeries(series4);


        }
    }; // end of speed listener




    public OnClickListener predictionButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    }; // end of prediction listener



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
        int size = arrayMedian.size()/2;

        if (arrayMedian.size() % 2 == 0)

            return ((Double) arrayMedian.get(size) + (Double)arrayMedian.get(size - 1));

        else
            return ((Double)arrayMedian.get(size));

    }



  /*  public static double skew (ArrayList data) {


        //skewness = [n / (n -1) (n - 2)] sum[(x_i - mean)^3] / std^3
        int i, n = data.size();

        if (n == 0)
            return n;

        double d, avg = MISSG;
        int count = 0;

        for (i=0; i<n; i++) {
            d = (Double)data.get(i);
            if (d != MISSG) {
                avg = Calculator.add (avg, d);
                count ++;
            }
        }

        if (count < 3)
            return MISSG;

        avg = avg / count;

        double stdev = 0.;
        for (i=0; i<n; i++) {
            d = (Double)data.get(i);
            if (d != MISSG) {
                d = d - avg;
                stdev = stdev + d * d;
            }
        }

        stdev = Math.sqrt (stdev / (count - 1));

        if (stdev == 0)
            return 0;

        double skew = 0;
        for (i=0; i < data.size() ; i++) {
            d = (Double)data.get(i);
            //if (d != MISSG) {
                d = d - avg;
                d = d / stdev;
                skew = skew + d * d * d;
            //}
        }

        return skew * count / ((count - 1) * (count - 2));
    }
*/

    public void clearTable (){


        // mean
        meanHandleTrans.setText("");
        meanHandleAbsolute.setText("");
        meanBaseTrans.setText("");
        meanBaseAbsolute.setText("");

        //SD
        sdHandleTrans.setText("");
        sdHandleAbsolute.setText("");
        sdBaseTrans.setText("");
        sdBaseAbsolute.setText("");


        // median
        medianHandleTrans.setText("");
        medianHandleAbsolute.setText("");
        medianBaseTrans.setText("");
        medianBaseAbsolute.setText("");


        // min
        minHandleTrans.setText("");
        minHandleAbsolute.setText("");
        minBaseTrans.setText("");
        minBaseAbsolute.setText("");

        // max
        maxHandleTrans.setText("");
        maxHandleAbsolute.setText("");
        maxBaseTrans.setText("");
        maxBaseAbsolute.setText("");

    }// end of clear table


}// end of data_visualisation


/***  FILLING THE TABLE: HANDLE ****/

/***  Traverse Plane Acceleration Magnitude ( Traverse Acceleration "blue")  ***/
/***  Acceleration Magnitude  ( Absolute Acceleration "Green" ) ***/