package ca.harpreetA00230757.foodorderingapp;
//

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class View_CustomerReviews extends AppCompatActivity
{
    ListView lv_reviews;
    ArrayList<RatingClass> al_rating;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference showreviews_ref;
    myadapter myad;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__customer_reviews);

        lv_reviews = findViewById(R.id.lv_reviews);
        myad = new myadapter();
        al_rating = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        showreviews_ref = firebaseDatabase.getReference("Reviews");

        //Toast.makeText(this, orderno + "", Toast.LENGTH_SHORT).show();
        lv_reviews.setAdapter(myad);

        showreviews_ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Hello_View", snapshot.toString());
                if (snapshot.exists())
                {
                    for (DataSnapshot sin : snapshot.getChildren())
                    {
                        RatingClass obj = sin.getValue(RatingClass.class);
                        al_rating.add(obj);
                    }
                    myad.notifyDataSetChanged();;
                    Log.d("Hello_View", al_rating.size() + "");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        // myad.notifyDataSetChanged();
    }

    ////////Adapter code/////
    class myadapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return al_rating.size();
        }

        @Override
        public Object getItem(int i) {
            return al_rating.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i * 100;
        }

        @Override
        public View getView(int i, View customView, ViewGroup parent)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            customView = inflater.inflate(R.layout.single_row_design_of_reviews, parent, false);

            RatingClass obj = al_rating.get(i);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String date_show = sdf.format(new Date(obj.getRatingdate()));

            TextView tv_mobileno = customView.findViewById(R.id.tv_mobileno);
            TextView tv_reviewdate = customView.findViewById(R.id.tv_reviewdate);
            TextView tv_reviewtext = customView.findViewById(R.id.tv_reviewtext);

            RatingBar rating_bar = customView.findViewById(R.id.rating_bar);

            tv_mobileno.setText(obj.getMobileno()+"");
            tv_reviewdate.setText(date_show+"");
            tv_reviewtext.setText(obj.getReviewText()+"");

            rating_bar.setRating(obj.getRatingvalue());


            return customView;


        }
    }
}