package com.versatiledevs.logicane;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }
    //If user clicks new user we will go to another activity to create users.
    public void newUser(View view){
        Intent intent = new Intent(Admin.this, newUserActivity.class);
        startActivity(intent);
        finish();

    }
    //Delete User view which is currently commented out in the .xml file.
    public void deleteUser(View view){
        Intent intent = new Intent(Admin.this, deleteUserActivity.class);
        startActivity(intent);
        finish();

    }
    //Delete Data view which is currently commented out in the .xml file.
    public void deleteData(View view){
        Intent intent = new Intent(Admin.this, deleteDataActivity.class);
        startActivity(intent);
        finish();

    }



}
