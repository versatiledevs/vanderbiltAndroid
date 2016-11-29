/*
    Android app is developed by VersatileDevs
        Luis chunga
        Sina Serati
        Jordan Dowling
        PJ
        Geo

        Latest update on Oct/6/2016
        Description: This is the profile activity that allows to sign out the user

 */

package com.versatiledevs.logicane;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button buttonSignOut;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // assign button
        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);

        //get firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                // user auth state is changed - user is null
                // it launches the login activity
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();

                }
            }

        };
    }

    public void signOutPublic(View view)
    {
        signOut();
    }

    //sign out method
    private void signOut() {
        firebaseAuth.signOut();
        Toast.makeText(MainActivity.this, "Goodbye.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

}