package com.example.tito.canteen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tito.canteen.Common.Common;
import com.example.tito.canteen.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    Button signin, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signin = (Button) findViewById(R.id.signin);
        signup = (Button) findViewById(R.id.signup);

        //INitialise Paper
        Paper.init(this);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

        //Check Remember Me
        String phone = Paper.book().read(Common.USER_KEY);
        String password = Paper.book().read(Common.USER_PASSWORD_KEY);

        if (phone != null && password != null) {
            if (!phone.isEmpty() && !password.isEmpty()) {
                login(phone, password);
            }
        }

    }

    private void login(final String phone, final String password) {
        if (Common.isConnectedToInternet(getBaseContext())) {

            //Init FireBase
            final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference("User");

            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(phone).exists()) {
                        //Dismiss Progress Dialog
                        progressDialog.dismiss();
                        //Check if database contains such a user
                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone); //Set Phone
                        if (user.getPassword().equals(password)) {
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            Common.currentuser = user;
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Sign in is  Failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //Dismiss Progress Dialog
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "USER DOESNT EXISTS.PLEASE SIGN UP FIRST!!!", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(MainActivity.this, "not connected to Internet", Toast.LENGTH_SHORT).show();
            return;
        }


    }
}




