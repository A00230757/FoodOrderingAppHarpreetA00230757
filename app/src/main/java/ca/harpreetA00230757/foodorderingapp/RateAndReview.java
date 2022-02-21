package ca.harpreetA00230757.foodorderingapp;
//

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RateAndReview extends AppCompatActivity
{
    RatingBar rating_bar;
    Button bt_submitreview;
    EditText et_review;
    long orderno;
    String mobileno = "";
    String reviewpushkey = "";
    String orderpushkey = "";
    int rating = 0;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reviews_ref;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_and_review);
        rating_bar = findViewById(R.id.rating_bar);
        bt_submitreview = findViewById(R.id.bt_submitreview);
        et_review = findViewById(R.id.et_review);
        // String rating = String.valueOf(rating_bar.getRating());


        Intent incomingintent = getIntent();
        orderno =Long.parseLong( incomingintent.getStringExtra("orderno"));
        mobileno = incomingintent.getStringExtra("mobileno");
        orderpushkey = incomingintent.getStringExtra("orderpushkey");
        Toast.makeText(this,orderno+ mobileno+reviewpushkey+"",Toast.LENGTH_SHORT).show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        reviews_ref = firebaseDatabase.getReference("Reviews");

//        bt_submitreview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                //Getting the rating and displaying it on the toast
//                if(rating == 0 ||et_review.equals(""))
//                {
//                  Toast.makeText(RateAndReview.this, "All Fields Required", Toast.LENGTH_SHORT).show();
//                }
//                String rating=String.valueOf(rating_bar.getRating());
//                Toast.makeText(getApplicationContext(), rating+"", Toast.LENGTH_LONG).show();
//            }
//        });

    }
    public void  submitReview(View v)
    {
        rating = (int) rating_bar.getRating();
        Log.d("Hello",rating+"");
        String customerreview = et_review.getText().toString();
        if(rating == 0||customerreview.equals(""))
        {
            Toast.makeText(this, "All Fields Required", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Date date_obj = new Date();
            long reviewDate = date_obj.getTime();

//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//            String date_show = sdf.format(new Date(reviewDate));

            reviewpushkey = reviews_ref.push().getKey();
            RatingClass ratingobj = new RatingClass(orderno,reviewDate,customerreview,rating,mobileno,reviewpushkey);
            reviews_ref.child(reviewpushkey).setValue(ratingobj);
            Toast.makeText(RateAndReview.this,"Review Added Successfully",Toast.LENGTH_SHORT).show();


        }
    }
}