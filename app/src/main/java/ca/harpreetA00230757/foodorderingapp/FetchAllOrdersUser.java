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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


///this activity is used to fetch all orders placed by user
public class FetchAllOrdersUser extends AppCompatActivity
{
    ArrayList<CustomerOrder> al_customerorder = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference showorder_ref;
    myadapter myad;
    ListView lv_viewAllOrders;

    ///declaration of customer orders list, firebase database and list view for orders.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_user_all_orders);

        //memory to all declarations
        myad = new myadapter();
        lv_viewAllOrders = findViewById(R.id.lv_viewAllOrders);
        lv_viewAllOrders.setAdapter(myad);

        firebaseDatabase = FirebaseDatabase.getInstance();
        showorder_ref = firebaseDatabase.getReference("Orders");

        showorder_ref.orderByChild("mobileno").equalTo(ProgressDialogClass.mobileno);

        //load orders of passed mobile number customer
        showorder_ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Log.d("Hello",snapshot.toString());
                if(snapshot.exists())
                {
                    for(DataSnapshot orderSnapshot: snapshot.getChildren())
                    {
                        CustomerOrder order_obj = orderSnapshot.getValue(CustomerOrder.class);
                        if(order_obj.getStatus().equals("delivered")){
                            al_customerorder.add(order_obj);
                        }

                    }
                    myad.notifyDataSetChanged();

                    Log.d("Hello-size",al_customerorder.size()+"");
                }
                else
                {
                    Toast.makeText(FetchAllOrdersUser.this, "No Order Found.", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    ////////Adapter code to show orders in list view/////
    class myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return al_customerorder.size();
        }

        @Override
        public Object getItem(int i) {
            return al_customerorder.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i * 100;
        }

        @Override
        public View getView(int i, View customView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            customView = inflater.inflate(R.layout.single_row_design_of_order_user, parent, false);

            CustomerOrder obj = al_customerorder.get(i);

            LinearLayout ll_order = customView.findViewById(R.id.ll_order);
            TextView tv_showorderid = customView.findViewById(R.id.tv_showorderid);
            TextView tv_showorderdate = customView.findViewById(R.id.tv_showorderdate);
            TextView tv_showamount = customView.findViewById(R.id.tv_showamount);
            Button bt_review = customView.findViewById(R.id.bt_review);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String date_show = sdf.format(new Date(obj.getOrderdate()));



            tv_showorderid.setText(obj.getOrderno()+"");
            tv_showorderdate.setText(date_show+"");
            tv_showamount.setText(obj.getTotalamount()+"");


            //click listener to view all active orders
            ll_order.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent in = new Intent(FetchAllOrdersUser.this,ViewActiveOrdersUser.class);
                    in.putExtra("orderno",obj.getPushkey()+"");
                    startActivity(in);
                }
            });

            //buttons to give ratings to order
            bt_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(FetchAllOrdersUser.this,RateAndReview.class);
                    in.putExtra("orderno",obj.getOrderno()+"");
                    in.putExtra("mobileno",obj.getMobileno()+"");
                    in.putExtra("orderpushkey",obj.getPushkey()+"");
                    startActivity(in);
                }
            });


            return customView;

        }
    }
}