package ca.harpreetA00230757.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

public class ManageCategory extends AppCompatActivity
{
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mainref;

    ArrayList<CategoryDetails> al;
    myadapter myad;
    ListView lv_category;
    FloatingActionButton fab;

    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        al = new ArrayList<>();
        myad = new myadapter();
        fab = findViewById(R.id.fab);
        lv_category = findViewById(R.id.lv_category);
        lv_category.setAdapter(myad);


        firebaseDatabase = FirebaseDatabase.getInstance();
        mainref = firebaseDatabase.getReference("category");

        mainref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    al.clear();

                    for(DataSnapshot single_data_snapshot : snapshot.getChildren())
                    {
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
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Dialog msg") .setTitle("R.string.dialog_title");
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent in = new Intent(ManageCategory.this,AddCategory.class);
                startActivity(in);
            }
        });

    }
//    public void addCategory(View v)
//    {
//        Intent in = new Intent(this,AddCategory.class);
//        startActivity(in);
//    }



    ////////Adapter code/////
    class myadapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return al.size();
        }

        @Override
        public Object getItem(int i)
        {
            return al.get(i);
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
            customView = inflater.inflate(R.layout.singlerow_design_of_listview_category,parent,false);

            CategoryDetails obj = al.get(i);

            TextView tv111 = customView.findViewById(R.id.tv111);
            TextView tv222 = customView. findViewById(R.id.tv222);
            ImageView imv111 = customView. findViewById(R.id.imv111);
            ImageView imv_delete = customView. findViewById(R.id.imv_delete);

            tv111.setText(obj.getCategory_name());
            tv222.setText(obj.getCategory_desc());
            Picasso.get().load(obj.getPhoto()).resize(200,200).into(imv111);



            imv_delete.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
//                    mainref.child(obj.getCategory_name()).removeValue();
//                    Toast.makeText(ManageCategory.this, "Category Deleted", Toast.LENGTH_SHORT).show();


                    builder.setMessage("Do you want to delete this category "+obj.getCategory_name()+"?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mainref.child(obj.getCategory_name()).removeValue();
                                    Toast.makeText(ManageCategory.this, "Category Deleted", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Delete");
                    alert.show();
                    Log.d("MYMSG","");


                }
            });


            return  customView;

        }
    }

}
