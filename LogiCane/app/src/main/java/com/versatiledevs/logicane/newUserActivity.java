package com.versatiledevs.logicane;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthCredential;
import com.google.android.gms.tasks.OnCompleteListener;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class newUserActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

    }

    public void createNewUser(View view){

        mAuth = FirebaseAuth.getInstance();
        //Getting the information from the edit text fields on the application.
        EditText edit_text_email =(EditText) findViewById(R.id.newUserEmail);;
        EditText edit_text_password = (EditText) findViewById(R.id.newUserPassword);
        EditText edit_text_first_name = (EditText) findViewById(R.id.fName);
        EditText edit_text_last_name = (EditText) findViewById(R.id.lName);


        // Converting all of the EditText fields into strings.
        String email = edit_text_email.getText().toString();
        String password = edit_text_password.getText().toString();
        String first_name = edit_text_first_name.getText().toString();
        String last_name = edit_text_last_name.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(newUserActivity.this, "A user with this email already exist.",
                                    Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(newUserActivity.this, "New User Created Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(newUserActivity.this, Admin.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });





    }
}
