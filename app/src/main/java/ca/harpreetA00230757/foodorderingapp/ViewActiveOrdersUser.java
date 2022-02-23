package ca.harpreetA00230757.foodorderingapp;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//user can view active orders using this activity
public class ViewActiveOrdersUser extends AppCompatActivity
{

    //declarations for active orders list , firebase database , storage .
    ListView lv_activeorders;
    ArrayList<OrderedItems> al_fooditems;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference showorder_ref;
    myadapter myad;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_active_orders_user);

        //memory to active orders list , firebase database , storage .
        lv_activeorders = findViewById(R.id.lv_activeorders);
        al_fooditems = new ArrayList<>();
        myad = new myadapter();


        //get intent to get order number from previous activity
        Intent incomingintent = getIntent();
        String orderno = incomingintent.getStringExtra("orderno");

        firebaseDatabase = FirebaseDatabase.getInstance();
        showorder_ref = firebaseDatabase.getReference("Orders").child(orderno).child("orderItems");

        Toast.makeText(this,orderno+"",Toast.LENGTH_SHORT).show();

        // to load orders to list
        showorder_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Hello_View",snapshot.toString());
                if(snapshot.exists()){
                    for (DataSnapshot sin :  snapshot.getChildren()){
                        OrderedItems obj = sin.getValue(OrderedItems.class);
                        al_fooditems.add(obj);
                    }
                    Log.d("Hello_View",al_fooditems.size()+"");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lv_activeorders.setAdapter(myad);
    }


    ////////Adapter code to set individual orders data to list/////
    class myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return al_fooditems.size();
        }

        @Override
        public Object getItem(int i) {
            return al_fooditems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i * 100;
        }

        @Override
        public View getView(int i, View customView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            customView = inflater.inflate(R.layout.single_row_design_of_activeorders, parent, false);

            OrderedItems obj = al_fooditems.get(i);

            String type = obj.getItem_type();
            TextView tv_productname = customView.findViewById(R.id.tv_productname);
            TextView tv_productqty = customView.findViewById(R.id.tv_productqty);
            TextView tv_productprice = customView.findViewById(R.id.tv_productprice);
            ImageView imv111 = customView.findViewById(R.id.imv111);
            ImageView imv_veg = customView.findViewById(R.id.imv_veg);

            tv_productname.setText(obj.getItem_name());
            tv_productqty.setText(obj.getItem_qty()+"");
            tv_productprice.setText(obj.getItem_price() + "");
            Picasso.get().load(obj.getItem_photo()).resize(300, 400).into(imv111);
            if (type.equals("Veg"))
            {
                imv_veg.setVisibility(View.VISIBLE);
                imv_veg.setImageResource(R.drawable.veg_icon);
            }
            else
            {
                imv_veg.setVisibility(View.VISIBLE);
                imv_veg.setImageResource(R.drawable.nonveg_icon);
            }


            return customView;

        }
    }

}