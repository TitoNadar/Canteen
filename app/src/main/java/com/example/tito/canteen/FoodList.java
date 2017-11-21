package com.example.tito.canteen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tito.canteen.Common.Common;
import com.example.tito.canteen.DataBase.DataBase;
import com.example.tito.canteen.Interfaces.ItemClickListener;
import com.example.tito.canteen.Model.Foods;
import com.example.tito.canteen.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String categoryId;
    DataBase dataBase;
    FirebaseRecyclerAdapter<Foods,FoodViewHolder> foodViewHolderFirebaseRecyclerAdapter;
  //Search Functionality
  FirebaseRecyclerAdapter<Foods,FoodViewHolder> firebaseRecyclerSearchAdapter;
  MaterialSearchBar materialSearchBar;
  List<String> suggestionList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Init Firebase
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Foods");

        //Init recylerview
        recyclerView=(RecyclerView)findViewById(R.id.recyler_food_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //check the category ID
        if(getIntent()!=null)
        {
            categoryId=getIntent().getStringExtra("Category");
        }
        if(!categoryId.isEmpty()&&categoryId!=null)
        {

            if(Common.isConnectedToInternet(getBaseContext()))
            {
          loadListFood(categoryId);}
          else{
                Toast.makeText(FoodList.this,"not connected to Internet",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Init database
        dataBase=new DataBase(this);
        materialSearchBar=(MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter your Food");

        loadSuggestions();
        materialSearchBar.setLastSuggestions(suggestionList);
        materialSearchBar.setCardViewElevation(0);

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //WE WILL CHANGE SUGGESTION LIST WHEN USER TYPES
              List<String> suggest =new ArrayList<String>();
              for(String search:suggestionList) //loop in suggestionlist
              {
                  if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                  {suggest.add(search);}
              }
              materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when search bar is closed restore original adapter
                if(!enabled)
                {recyclerView.setAdapter(foodViewHolderFirebaseRecyclerAdapter);}
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        firebaseRecyclerSearchAdapter=new FirebaseRecyclerAdapter<Foods, FoodViewHolder>(
                Foods.class,
                R.layout.food_list_view,
                FoodViewHolder.class,
                databaseReference.orderByChild("name").equalTo(text.toString())) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Foods model, int position) {
                viewHolder.textView.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);
                final Foods local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(FoodList.this, FoodDetails.class);
                        intent.putExtra("FoodId",firebaseRecyclerSearchAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerSearchAdapter);
    }

            private void loadSuggestions() {
                databaseReference.orderByChild("menuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                            Foods foods = dataSnapshot1.getValue(Foods.class);
                            suggestionList.add(foods.getName());
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            private void loadListFood(String categoryId) {

        foodViewHolderFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Foods, FoodViewHolder>(Foods.class,
                        R.layout.food_list_view,
                        FoodViewHolder.class,
                        databaseReference.orderByChild("menuId").equalTo(categoryId))//Like Get * From Foods where MenuId=

                {
                    @Override
                    protected void populateViewHolder(final FoodViewHolder viewHolder, final Foods model, final int position) {

                        viewHolder.textView.setText(model.getName());

                        Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);
                        if(dataBase.isFavorites(foodViewHolderFirebaseRecyclerAdapter.getRef(position).getKey()))
                        {
                            viewHolder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                        }
                        else {
                            viewHolder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        }
                        //Click to change status
                        viewHolder.fav.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!dataBase.isFavorites(foodViewHolderFirebaseRecyclerAdapter.getRef(position).getKey()))
                                {
                                   dataBase.addtoFavorites(foodViewHolderFirebaseRecyclerAdapter.getRef(position).getKey());
                                    viewHolder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                                    Toast.makeText(FoodList.this, viewHolder.textView.getText().toString()+" added to  your Favorite's List", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    dataBase.removefromFavorites(foodViewHolderFirebaseRecyclerAdapter.getRef(position).getKey());
                                    viewHolder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                    Toast.makeText(FoodList.this, model.getName()+" removed from your Favorite's List", Toast.LENGTH_SHORT).show();
                                }
                                }


                        });

                        final Foods local = model;
                        viewHolder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                Intent intent = new Intent(FoodList.this, FoodDetails.class);
                                intent.putExtra("FoodId", foodViewHolderFirebaseRecyclerAdapter.getRef(position).getKey());
                                startActivity(intent);
                            }
                        });
                    }
                };
                recyclerView.setAdapter(foodViewHolderFirebaseRecyclerAdapter);
            }
        }
