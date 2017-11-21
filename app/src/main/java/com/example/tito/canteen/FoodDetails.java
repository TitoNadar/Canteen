package com.example.tito.canteen;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.tito.canteen.Common.Common;
import com.example.tito.canteen.DataBase.DataBase;
import com.example.tito.canteen.Model.Foods;
import com.example.tito.canteen.Model.Order;
import com.example.tito.canteen.Model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

public class FoodDetails extends AppCompatActivity implements RatingDialogListener{
    ElegantNumberButton elegantNumberButton;
    TextView food_name,food_price,food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton floatingActionButton,rating;
    RatingBar ratingBar;
    String foodid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference ratingTb1;
    Foods currentfood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        //Init FireBase
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Foods");

        ratingTb1=firebaseDatabase.getReference("Rating");

        elegantNumberButton=(ElegantNumberButton)findViewById(R.id.elegentnumberbutton);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.fab_food_description);
        floatingActionButton.bringToFront();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DataBase(getBaseContext()).addtoCart(new Order(foodid,
                        currentfood.getName(),
                        elegantNumberButton.getNumber(),
                        currentfood.getPrice(),
                        currentfood.getDiscount()));
                Toast.makeText(FoodDetails.this,"added",Toast.LENGTH_SHORT).show();
            }
        });

        food_name=(TextView)findViewById(R.id.detailed_food_name);
        food_price=(TextView)findViewById(R.id.food_price);
        food_description=(TextView)findViewById(R.id.description);
        food_image=(ImageView)findViewById(R.id.detailed_image);

        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsingtoolbarlayout);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);

        rating=(FloatingActionButton)findViewById(R.id.fab_food_rating);
        ratingBar=(RatingBar)findViewById(R.id.ratingbar);
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialod();
            }
        });

        if(getIntent()!=null)
        {
           foodid=getIntent().getStringExtra("FoodId");
           if(!foodid.isEmpty()&&foodid!=null)
           {
               if(Common.isConnectedToInternet(getBaseContext()))
               {
               getDetailFood(foodid);
                getFoodRating(foodid);
               }
           else{
                   Toast.makeText(FoodDetails.this,"not connected to Internet",Toast.LENGTH_SHORT).show();
                      return;}
           }
        }
    }

    private void getFoodRating(String foodid) {
        Query foodrating=ratingTb1.orderByChild("foodId").equalTo(foodid);
        foodrating.addValueEventListener(new ValueEventListener() {
           int sum=0,count=0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Rating rating=snapshot.getValue(Rating.class);
                            sum+=Integer.parseInt(rating.getRateValue());
                            count++;

                }
                if(count!=0)
                {
                float average=sum/count;
                ratingBar.setRating(average);}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialod() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad","Not good","Quite Ok","Good","Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate it")
                .setDescription("Give your feedback and some stars")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Write your comment here")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(R.color.white)
                .setCommentBackgroundColor(R.color.overlayActionBar)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetails.this)
                .show();

    }

    private void getDetailFood(final String foodid) {
        databaseReference.child(foodid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               currentfood=dataSnapshot.getValue(Foods.class);
                Picasso.with(getBaseContext()).load(currentfood.getImage()).into(food_image);
                food_name.setText(currentfood.getName());
                food_description.setText(currentfood.getDescription());
                food_price.setText(currentfood.getPrice());
                collapsingToolbarLayout.setTitle(currentfood.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPositiveButtonClicked(int value, String comments) {
        final Rating rating=new Rating(Common.currentuser.getPhone(),
                foodid,
                String.valueOf(value),comments);
        ratingTb1.child(Common.currentuser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Common.currentuser.getPhone()).exists())
                {
                    //Remove old rating
                    ratingTb1.child(Common.currentuser.getPhone()).removeValue();
                    //UPdate new Rating
                    ratingTb1.child(Common.currentuser.getPhone()).setValue(rating);
                }
                else
                {
                    ratingTb1.child(Common.currentuser.getPhone()).setValue(rating);
                }
                Toast.makeText(FoodDetails.this, "Thank you", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }
}
