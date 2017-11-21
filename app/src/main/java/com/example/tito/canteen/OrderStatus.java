package com.example.tito.canteen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.tito.canteen.Common.Common;
import com.example.tito.canteen.Interfaces.ItemClickListener;
import com.example.tito.canteen.Model.Request;
import com.example.tito.canteen.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<Request,OrderViewHolder> firebaseRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);


        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("Requests");
        recyclerView=(RecyclerView)findViewById(R.id.orderstatusrecylerview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        loadOrderStatus(Common.currentuser.getPhone());



    }

    private void loadOrderStatus(String phone) {
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class,R.layout.order_status_layout,OrderViewHolder.class,databaseReference.orderByChild("phone").equalTo(phone)) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
          viewHolder.orderphone.setText(model.getPhone());
                viewHolder.orderstatus.setText(Common.convertCodetoStatus(model.getStatus()));
                viewHolder.orderaddress.setText(model.getAddress());
                viewHolder.orderid.setText("Order Id:"+firebaseRecyclerAdapter.getRef(position).getKey());
           viewHolder.setItemClickListener(new ItemClickListener() {
               @Override
               public void onClick(View view, int position, boolean isLongClick) {

               }
           });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }




    }



