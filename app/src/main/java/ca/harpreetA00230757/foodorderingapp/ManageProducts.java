package ca.harpreetA00230757.foodorderingapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManageProducts extends AppCompatActivity
{
    FirebaseDatabase firebaseDatabase;
    DatabaseReference fooditem_ref;

    ArrayList<FoodItemDetails> al;
    ArrayList<CategoryDetails> al_category;
    myadapter myad;
    ListView lv_products;
    FloatingActionButton fab;
    String category_name ="";
    Spinner spinner_category;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);



        al = new ArrayList<>();
        myad = new myadapter();
        fab = findViewById(R.id.fab);
        lv_products = findViewById(R.id.lv_products);
        lv_products.setAdapter(myad);


        firebaseDatabase = FirebaseDatabase.getInstance();
        fooditem_ref = firebaseDatabase.getReference("category");
        al_category = new ArrayList<>();
        myadapter_category ad_category = new myadapter_category();

        spinner_category = findViewById(R.id.spiner_category);
        spinner_category.setAdapter(ad_category);

        fooditem_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                al.clear();
                if(snapshot.exists()){
                    for(DataSnapshot sin : snapshot.getChildren()){
                        CategoryDetails obj = sin.getValue(CategoryDetails.class);
                        al_category.add(obj);
                    }
                    ad_category.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ManageProducts.this, ""+al_category.get(position).getCategory_name(), Toast.LENGTH_SHORT).show();
                category_name = al_category.get(position).getCategory_name();
                viewProducts(al_category.get(position).getCategory_name());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent in = new Intent(ManageProducts.this,AddProduct.class);
                startActivity(in);
            }
        });

    }

    public void addProducts(View v)
    {
        Intent in = new Intent(this, AddProduct.class);
        startActivity(in);
    }

    public void viewProducts(String categoryname)
    {

        fooditem_ref.child(categoryname).child("food item").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    al.clear();

                    for (DataSnapshot single_data_snapshot : snapshot.getChildren())
                    {
                        FoodItemDetails item_obj = single_data_snapshot.getValue(FoodItemDetails.class);
                        al.add(item_obj);
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

    ////////Adapter code/////
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
        public View getView(int i, View customView, ViewGroup parent)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            customView = inflater.inflate(R.layout.single_row_design_of_listview_products, parent, false);

            FoodItemDetails obj = al.get(i);
            String type = obj.getItem_type();
            TextView tv111 = customView.findViewById(R.id.tv111);
            TextView tv222 = customView.findViewById(R.id.tv222);
            TextView tv333 = customView.findViewById(R.id.tv333);
            ImageView imv111 = customView.findViewById(R.id.imv111);
            ImageView imv_edit = customView.findViewById(R.id.imv_edit);
            ImageView imv_veg = customView.findViewById(R.id.imv_veg);

            // ImageView imv_nonveg = customView.findViewById(R.id.imv_nonveg);

            tv111.setText(obj.getItem_name());
            tv222.setText(obj.getItem_desc());
            tv333.setText(obj.getItem_price()+"");
            Picasso.get().load(obj.getPhoto()).resize(200, 200).into(imv111);
            if (type.equals("Veg"))
            {
                imv_veg.setVisibility(View.VISIBLE);
                imv_veg.setImageResource(R.drawable.vegicon);
            }
            else
            {
                imv_veg.setVisibility(View.VISIBLE);
                imv_veg.setImageResource(R.drawable.nonvegicon);
            }

            imv_edit.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View view) {

//                    Intent intent=new Intent(getApplicationContext(), EditFoodItem.class);
//                    intent.putExtra("catname",category_name);
//                    intent.putExtra("fooditemname",obj.getItem_name());
//                    intent.putExtra("fooditemdesc",obj.getItem_desc());
//                    intent.putExtra("fooditemprice",obj.getItem_price());
//                    intent.putExtra("fooditemtype",obj.getItem_type());
//                    intent.putExtra("fooditemphoto",obj.getPhoto());
//
//                    startActivity(intent);
                }

            });

            return customView;

        }
    }

    // Inner class
    class myadapter_category extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return al_category.size();
        }

        @Override
        public Object getItem(int i)
        {
            return al_category.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i*100;
        }

        @Override
        public View getView(int i, View customView, ViewGroup parent)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            customView = inflater.inflate(R.layout.single_row_of_spinner,parent,false);

            CategoryDetails obj = al_category.get(i);

            TextView tv111 = customView.findViewById(R.id.tv111);
            ImageView imv111 = customView.findViewById(R.id.imv111);


            tv111.setText(obj.getCategory_name());
            Picasso.get().load(obj.getPhoto()).resize(200,200).into(imv111);



            return  customView;

        }
    }
}