package com.example.tito.canteen;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class SignUp extends AppCompatActivity {
EditText name,phone,password;
Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name=(MaterialEditText)findViewById(R.id.name);
        phone=(MaterialEditText)findViewById(R.id.phone);
        password=(MaterialEditText)findViewById(R.id.password);
        signup=(Button)findViewById(R.id.signup);
        //Init FireBase
        final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference=firebaseDatabase.getReference("User");


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isConnectedToInternet(getBaseContext()))
                {
                final ProgressDialog progressDialog=new ProgressDialog(SignUp.this);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(phone.getText().toString()).exists())
                        {    progressDialog.dismiss();
                            Toast.makeText(SignUp.this,"User already exists",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            User user=new User(name.getText().toString(),password.getText().toString());
                            databaseReference.child(phone.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this,"SIGN UP SUCCESSFULL",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            else {
                    Toast.makeText(SignUp.this,"not connected to Internet",Toast.LENGTH_SHORT).show();
               return; }
            }

        });
    }
}
