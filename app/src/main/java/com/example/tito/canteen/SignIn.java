package com.example.tito.canteen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tito.canteen.Common.Common;
import com.example.tito.canteen.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {
EditText phone,password;
Button signin;
CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        phone=(MaterialEditText)findViewById(R.id.phone);
        password=(MaterialEditText)findViewById(R.id.password);
        signin=(Button)findViewById(R.id.signin);
        checkBox=(CheckBox)findViewById(R.id.remember);

        //Init Paper
        Paper.init(this);

        //Init FireBase
       final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
       final DatabaseReference databaseReference=firebaseDatabase.getReference("User");

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    if(checkBox.isChecked())
                    {
                        Paper.book().write(Common.USER_KEY,phone.getText().toString());
                        Paper.book().write(Common.USER_PASSWORD_KEY,password.getText().toString());
                    }
                    final ProgressDialog progressDialog = new ProgressDialog(SignIn.this);
                    progressDialog.setMessage("Please Wait");
                    progressDialog.show();
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(phone.getText().toString()).exists()) {
                                //Dismiss Progress Dialog
                                progressDialog.dismiss();
                                //Check if database contains such a user
                                User user = dataSnapshot.child(phone.getText().toString()).getValue(User.class);
                                user.setPhone(phone.getText().toString()); //Set Phone
                                if (user.getPassword().equals(password.getText().toString())) {
                                    Intent intent = new Intent(SignIn.this, Home.class);
                                    Common.currentuser = user;
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignIn.this, "Sign in is  Failed", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //Dismiss Progress Dialog
                                progressDialog.dismiss();
                                Toast.makeText(SignIn.this, "USER DOESNT EXISTS.PLEASE SIGN UP FIRST!!!", Toast.LENGTH_SHORT).show();
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(SignIn.this,"not connected to Internet",Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });

    }
}
