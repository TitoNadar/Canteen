package com.example.tito.canteen;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tito.canteen.Common.Common;
import com.example.tito.canteen.DataBase.DataBase;
import com.example.tito.canteen.Model.MyResponse;
import com.example.tito.canteen.Model.Notification;
import com.example.tito.canteen.Model.Order;
import com.example.tito.canteen.Model.Request;
import com.example.tito.canteen.Model.Sender;
import com.example.tito.canteen.Model.Token;
import com.example.tito.canteen.Remote.APIService;
import com.example.tito.canteen.ViewHolder.CartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity {
RecyclerView recyclerView;
RecyclerView.LayoutManager layoutManager;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
TextView textView;
Button button;
List<Order> cart=new ArrayList<>();
CartAdapter adapter;
APIService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //init service
        mApiService=Common.getFCMService();

        //Init firebase
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Requests");
        recyclerView=(RecyclerView)findViewById(R.id.listcard);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
         textView=(TextView)findViewById(R.id.total);
         button=(Button)findViewById(R.id.btnplaceorder);
         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                if(cart.size()>0)
                 showAlertDialog();
                else Toast.makeText(Cart.this, "Your Cart is Empty", Toast.LENGTH_SHORT).show();
             }
         });
          loadlistfood();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        if(item.getTitle().equals(Common.DELETE))
        {
            deleteCart(item.getOrder()); //getOrder is positon he
        }
        return super.onContextItemSelected(item);
    }

    private void deleteCart(int position) {
        cart.remove(position);
        new DataBase(this).cleanCart();
        for(Order item:cart)
        {
            new DataBase(this).addtoCart(item);
        }
        //Refresh
        loadlistfood();

    }

    private void showAlertDialog() {
        final AlertDialog.Builder alertdialog=new AlertDialog.Builder(Cart.this);
    alertdialog.setTitle("ONE MORE STEP");
    alertdialog.setMessage("EnterAddress");

        LayoutInflater inflater=this.getLayoutInflater();
        View address_comment=inflater.inflate(R.layout.order_address_comment,null);
        final MaterialEditText address=(MaterialEditText)address_comment.findViewById(R.id.address);
        final MaterialEditText comment=(MaterialEditText)address_comment.findViewById(R.id.comment);
        alertdialog.setView(address_comment);
        alertdialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            Request request=new Request(Common.currentuser.getPhone(),Common.currentuser.getName(),address.getText().toString(),textView.getText().toString(),"0",cart,comment.getText().toString());

                //Submit to Firebase

                //Using SCurrentTimeInMiillis as Key

                String ordernumber=String.valueOf(System.currentTimeMillis());

                databaseReference.child(ordernumber).setValue(request);

                //Dele Cart
                new DataBase(getBaseContext()).cleanCart();

                sendNotificationOrder(ordernumber);

            }
        });
        alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
           dialog.dismiss();
            }

        });
        alertdialog.show();

    }

    private void sendNotificationOrder(final String ordernumber) {

        DatabaseReference databaseReference=firebaseDatabase.getReference("Tokens");
        databaseReference.orderByChild("serverToken").equalTo(true)

              .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            Token token =dataSnapshot1.getValue(Token.class);

                            //Make raw payload
                            Notification notification=new Notification("tito","You have new order "+ordernumber);
                            Sender sender=new Sender(token.getToken(),notification);
                            mApiService.sendNotification(sender)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            if(response.body().success==1)
                                            {
                                                Toast.makeText(Cart.this, "Order succesfully placed", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(Cart.this, "Order was not placed", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Log.e("ERROR",t.getMessage());

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void loadlistfood() {
        cart=new DataBase(this).getCarts();
        adapter=new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //calculate total price
        int total=0;
        for(Order order:cart)
        {
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
            Locale locale=new Locale("en","US");
            NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);
            textView.setText(numberFormat.format(total));
        }
    }
}
