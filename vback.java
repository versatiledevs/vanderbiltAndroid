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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
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

    // temporary array
   // private ArrayList<Double> Dtest = new ArrayList<>();
    //private ArrayList<String> Stest = new ArrayList<>();



    /** mean **/
    private TextView meanHandleTrans,
                    meanHandleAbsolute,
                    meanBaseTrans,
                meanBaseAbsolute;

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

                //temporary vars
                String key;
                String stringValue;
                Double value;
                String[] timeToDoubleStr; //used for conversion of time
                double [] timeToDouble=new double[4];

                Signal userSignal= new Signal();
                Signal userSignalCopy= new Signal();

                double HA_total,
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
                grip_sum = (userSignal.m_F0 + userSignal.m_F1 + userSignal.m_F2 + userSignal.m_F3 + userSignal.m_F4 + userSignal.m_F5 + userSignal.m_F6 +userSignal.m_F7);

                userSignal.m_GripPressureSum= grip_sum;

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


    }// end of onStart


    public void data (View view){


        /***  FILLING THE TABLE: HANDLE ****/

        /***  Traverse Plane Acceleration Magnitude ( Traverse Acceleration "blue")  ***/
        /***  Acceleration Magnitude  ( Absolute Acceleration "Green" ) ***/
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

        maxHandleAbsolute.setText(decimalFormat.format(sortedBy_HA_Aceel.get(sortedBy_HA_TAceel.size()-1)));

        maxBaseTrans.setText(decimalFormat.format(sortedBy_BA_TAceel.get(sortedBy_BA_TAceel.size()-1)));

        maxBaseAbsolute.setText(decimalFormat.format(sortedBy_BA_Aceel.get(sortedBy_BA_Aceel.size()-1)));



        //meanHandleTrans.setText(decimalFormat.format( mean(array_HA_TransvPlaneAccelMagn) ));
        //meanHandleAbsolulte.setText(decimalFormat.format(mean(array_BA_TransvPlaneAccelMagn)));





    } //end of data

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

/*
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

*/
}// end of data_visualisation
