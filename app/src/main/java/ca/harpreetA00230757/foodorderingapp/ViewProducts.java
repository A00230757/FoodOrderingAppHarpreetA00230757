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
import android.widget.Button;
import android.widget.ImageView;
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

public class ViewProducts extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference fooditem_ref;

    ArrayList<FoodItemDetails> al;
    myadapter myad;
    ListView lv_products;
    Button bt_makepayment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        al = new ArrayList<>();
        myad = new myadapter();
        lv_products = findViewById(R.id.lv_products);
        bt_makepayment = findViewById(R.id.bt_makepayment);
        lv_products.setAdapter(myad);

        Intent incomingintent = getIntent();
        String category = incomingintent.getStringExtra("category");

        firebaseDatabase = FirebaseDatabase.getInstance();
        fooditem_ref = firebaseDatabase.getReference("category");
        Toast.makeText(this,category+"",Toast.LENGTH_SHORT).show();

        fooditem_ref.child(category).child("food item").addValueEventListener(new ValueEventListener()
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

        findViewById(R.id.view_cart).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent in = new Intent(ViewProducts.this,CartDetails.class);
                startActivity(in);
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
            customView = inflater.inflate(R.layout.single_row_design_of_product, parent, false);

            FoodItemDetails obj = al.get(i);
            String type = obj.getItem_type();
            TextView tv_productname = customView.findViewById(R.id.tv_productname);
            TextView tv_productdesc = customView.findViewById(R.id.tv_productdesc);
            TextView tv_productprice = customView.findViewById(R.id.tv_productprice);
            ImageView imv111 = customView.findViewById(R.id.imv111);
            ImageView imv_veg = customView.findViewById(R.id.imv_veg);
            ImageView imv_shoppingcart = customView.findViewById(R.id.imv_shoppingcart);

            // ImageView imv_nonveg = customView.findViewById(R.id.imv_nonveg);

            tv_productname.setText(obj.getItem_name());
            tv_productdesc.setText(obj.getItem_desc());
            tv_productprice.setText(obj.getItem_price() + "");
            Picasso.get().load(obj.getPhoto()).resize(300, 400).into(imv111);
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
               /* imv_shoppingcart.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        manage_cart(obj);
                    }
                });*/
            imv_shoppingcart.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    manage_cart(obj);
                }
            });

            return customView;

        }
    }
    public void manage_cart(FoodItemDetails obj)
    {
        //OrderedItems obj1 = new OrderedItems(obj.getItem_name(),obj.getItem_desc(),obj.getItem_type(),obj.getPhoto(),1,obj.getItem_price(),(obj.getItem_price()));
        if(ProgressDialogClass.orderedItems == null)
        {
            OrderedItems obj1 = new OrderedItems(obj.getItem_name(),obj.getItem_desc(),obj.getItem_type(),obj.getPhoto(),1,obj.getItem_price(),(obj.getItem_price()));

            ProgressDialogClass.orderedItems.add(obj1);
            Toast.makeText(this,""+ProgressDialogClass.orderedItems.size(), Toast.LENGTH_SHORT).show();
        }
        else
        {
            int qty = 1;
            boolean  isadded = false;
            int index = 0;
            for(OrderedItems item : ProgressDialogClass.orderedItems)
            {
//    Log.d("HelloCart_1",item.getItem_name()+"....."+item.getItem_qty()+"....."+item.getTotalprice());

                if(item.getItem_name().equals(obj.getItem_name()))
                {
                    isadded = true;
                    break;
                }
                index++;

            }

            if(isadded)
            {
                int qty1 = ProgressDialogClass.orderedItems.get(index).getItem_qty()+qty;
                ProgressDialogClass.orderedItems.get(index).setItem_qty(qty1);
                ProgressDialogClass.orderedItems.get(index).setTotalprice(ProgressDialogClass.orderedItems.get(index).getItem_price() * ProgressDialogClass.orderedItems.get(index).getItem_qty() );
            }
            else
            {
                OrderedItems obj1 = new OrderedItems(obj.getItem_name(),obj.getItem_desc(),obj.getItem_type(),obj.getPhoto(),1,obj.getItem_price(),(obj.getItem_price()));

                ProgressDialogClass.orderedItems.add(obj1);
                Toast.makeText(this,""+ProgressDialogClass.orderedItems.size(), Toast.LENGTH_SHORT).show();
            }

            for(OrderedItems item : ProgressDialogClass.orderedItems)
            {
                Log.d("HelloCart",item.getItem_name()+"....."+item.getItem_qty()+"....."+item.getTotalprice());
            }

        }
        // GlobalClass.orderedItems.add(obj1);
        // Toast.makeText(ViewProducts.this, ""+GlobalClass.orderedItems.size(), Toast.LENGTH_SHORT).show();


    }

}

