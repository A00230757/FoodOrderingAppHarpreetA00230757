package ca.harpreetA00230757.foodorderingapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


//when user start shopping then this activity appears first to show all cuisines/categories

// when user choose any category then food items corresponding to that particular category get visible in second activity

public class ViewAllCategories extends AppCompatActivity {

    //declarations to firebase database , storage and grid list of category
    GridView gridView_category;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference mainref;

    ArrayList<CategoryDetails> al;
    myadapter myad;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_categories);

        //memory to firebase database , storage and grid list of category
        gridView_category = findViewById(R.id.gridView_category);

        al = new ArrayList<>();
        myad = new myadapter();
        gridView_category.setAdapter(myad);


        firebaseDatabase = FirebaseDatabase.getInstance();
        mainref = firebaseDatabase.getReference("category");

        //load categories from  firebase and add to list
        mainref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    al.clear();

                    for (DataSnapshot single_data_snapshot : snapshot.getChildren()) {
                        CategoryDetails category_obj = single_data_snapshot.getValue(CategoryDetails.class);
                        al.add(category_obj);
                    }
                    // Toast.makeText(ManageCategory.this,"size of list"+al.size(),Toast.LENGTH_LONG).show();

                    //send refresh to list
                    myad.notifyDataSetChanged();
                }
                else
                {
                    al.clear();
                    myad.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    ////////Adapter code to set firebase category data to grid list/////
    class myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return al.size();
        }

        @Override
        public Object getItem(int i) {
            return al.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i * 100;
        }

        @Override
        public View getView(int i, View customView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            customView = inflater.inflate(R.layout.singlerow_design_of_gridview_category, parent, false);

            CategoryDetails obj = al.get(i);

            TextView tv111 = customView.findViewById(R.id.tv111);

            ImageView imv111 = customView.findViewById(R.id.imv111);

            LinearLayout lv1 = customView.findViewById(R.id.lv1);

            tv111.setText(obj.getCategory_name());

            Picasso.get().load(obj.getPhoto()).resize(500,600).into(imv111);
            lv1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent in = new Intent(ViewAllCategories.this,ViewProducts.class);
                    in.putExtra("category",obj.getCategory_name()+"");
                    startActivity(in);
                }
            });
            return customView;

        }
    }
}