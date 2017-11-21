package com.example.tito.canteen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tito.canteen.Common.Common;
import com.example.tito.canteen.Interfaces.ItemClickListener;
import com.example.tito.canteen.Model.Category;
import com.example.tito.canteen.Model.Token;
import com.example.tito.canteen.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
  FirebaseDatabase firebaseDatabase;
  DatabaseReference databaseReference;
  TextView textView;
  RecyclerView recyclerView;
  RecyclerView.LayoutManager layoutManager;

  FirebaseRecyclerAdapter<Category,MenuViewHolder> firebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //Init Firebase
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Category");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(Home.this,Cart.class);
               startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set Nmae of User
        View headerView=navigationView.getHeaderView(0);
        textView=(TextView)headerView.findViewById(R.id.name);
        textView.setText(Common.currentuser.getName());

        //REcyler Menu
        recyclerView=(RecyclerView)findViewById(R.id.recyler_menu);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        //Initialise Paper
        Paper.init(this);

        if(Common.isConnectedToInternet(this))
        {
        loadmenu();}
        else{
            Toast.makeText(Home.this,"not connected to Internet",Toast.LENGTH_SHORT).show();
            return;
        }
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void updateToken(String tokenrefreshed) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=database.getReference("Tokens");
        Token token=new Token(tokenrefreshed,false); //false because token send from client side
        databaseReference.child(Common.currentuser.getPhone()).setValue(token);
    }

    private void loadmenu() {
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,R.layout.menu_item,MenuViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.textView.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);
                final Category clickItem=model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent=new Intent(Home.this,FoodList.class);
                        intent.putExtra("Category",firebaseRecyclerAdapter.getRef(position).getKey());

                          startActivity(intent);
                       // Toast.makeText(Home.this,"hi",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.getItemId()==R.id.refresh)
            loadmenu();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.cart) {

            Intent intent=new Intent(Home.this,Cart.class);
            startActivity(intent);

        }
        if (id == R.id.orders) {
            Intent intent=new Intent(Home.this,OrderStatus.class);
            startActivity(intent);

        }
        if (id == R.id.nav_menu) {


        }
        if (id == R.id.favorites) {
            Intent intent=new Intent(Home.this,MyFavorites.class);
            startActivity(intent);


        }
        if (id == R.id.signout) {
            //Clear Remember ME
            Paper.book().destroy();

            Intent intent=new Intent(Home.this,SignIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
