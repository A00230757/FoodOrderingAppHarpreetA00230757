package ca.harpreetA00230757.foodorderingapp;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
public class FetchUserAllOrders extends AppCompatActivity
{

    ///declaration of customer orders list, firebase database and list view for orders.
    ArrayList<CustomerOrder> al_customerorder = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference showorder_ref;
    ProgressDialog progressDialog;
    myadapter myad;
    ListView lv_viewAllOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_user_all_orders);

        //memory to all declarations
        myad = new myadapter();
        lv_viewAllOrders = findViewById(R.id.lv_viewAllOrders);
        lv_viewAllOrders.setAdapter(myad);
        progressDialog = ProgressDialogClass.CreateProgressDialog(this,"Loading All Orders","Please Wait");
        progressDialog.show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        showorder_ref = firebaseDatabase.getReference("Orders");

        //load orders from firebase database to list
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

                        al_customerorder.add(order_obj);
                        myad.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                    Log.d("Hello-size",al_customerorder.size()+"");
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(FetchUserAllOrders.this, "No Order Found.", Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    ////////Adapter code to individual orders on list view/////
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
            customView = inflater.inflate(R.layout.single_row_design_of_order, parent, false);

            CustomerOrder obj = al_customerorder.get(i);

            LinearLayout ll_order = customView.findViewById(R.id.ll_order);
            TextView tv_showorderid = customView.findViewById(R.id.tv_showorderid);
            TextView tv_showorderdate = customView.findViewById(R.id.tv_showorderdate);
            TextView tv_showamount = customView.findViewById(R.id.tv_showamount);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String date_show = sdf.format(new Date(obj.getOrderdate()));

//ArrayList<OrderedItems> al23 = al_customerorder.get(i).orderItems;
//String         items = "";
//for (OrderedItems ds : al23){
//    items += ds.getItem_name()+"  "+ds.getItem_qty()+"\n";
//
//}
//            tv_showamount.setText(items);

            tv_showorderid.setText(obj.getOrderno()+"");
            tv_showorderdate.setText(date_show+"");
            tv_showamount.setText(obj.getTotalamount()+"");

            ll_order.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent in = new Intent(FetchUserAllOrders.this,ViewOrders.class);
                    in.putExtra("orderno",obj.getPushkey()+"");
                    startActivity(in);
                }
            });



            return customView;

        }
    }
}