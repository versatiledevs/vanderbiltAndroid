package com.versatiledevs.logicane;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import static android.R.attr.name;


public class newUserActivity extends AppCompatActivity {
    //private FirebaseAuth mAuth;
    private FirebaseAuth firebaseAuth;


    String uid = "no uid";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void createNewUser(View view) {
        RadioGroup radioGroup;
        RadioButton radioButton;

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //Getting the information from the edit text fields on the application.
        EditText edit_text_email = (EditText) findViewById(R.id.newUserEmail);
        EditText edit_text_password = (EditText) findViewById(R.id.newUserPassword);
        EditText edit_text_first_name = (EditText) findViewById(R.id.fName);
        EditText edit_text_last_name = (EditText) findViewById(R.id.lName);
        radioGroup = (RadioGroup) findViewById(R.id.radio);

        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);


        // Converting all of the EditText fields into strings.
        final String email = edit_text_email.getText().toString();
        final String password = edit_text_password.getText().toString();
        final String first_name = edit_text_first_name.getText().toString();
        final String last_name = edit_text_last_name.getText().toString();
        final String role = radioButton.getText().toString();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(newUserActivity.this, "A user with this email already exist.",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            firebaseAuth = FirebaseAuth.getInstance();
                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                            uid = user.getUid();
                            writeNewUser (uid, first_name, last_name, email, role);


                            Toast.makeText(newUserActivity.this, "New User Created Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(newUserActivity.this, Admin.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    public class User {

        String fName;
        String lName;
        String email;
        String role;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String fName, String lName, String email, String role) {
            this.fName = fName;
            this.lName = lName;
            this.email = email;
            this.role = role;
        }

    }
        //This method is for writing data to the database about the user.
        private void writeNewUser (String uid, String fName, String lName, String email, String role){

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users");

            //Loading the information into the Database.
            myRef.child(uid).child("email").setValue(email);
            myRef.child(uid).child("fName").setValue(fName);
            myRef.child(uid).child("lName").setValue(lName);
            myRef.child(uid).child("role").setValue(role);
        }


}
