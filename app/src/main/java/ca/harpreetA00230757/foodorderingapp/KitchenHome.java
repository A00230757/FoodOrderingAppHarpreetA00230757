package ca.harpreetA00230757.foodorderingapp;
//

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KitchenHome extends AppCompatActivity
{
    ArrayList<CustomerOrder> al_customerorders = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference kitchen_ref,token_ref;
    GridView gridview_kitchen;
    myadapter myad;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_home);

        gridview_kitchen = findViewById(R.id.gridview_kitchen);
        firebaseDatabase = FirebaseDatabase.getInstance();
        kitchen_ref = firebaseDatabase.getReference("Orders");
        token_ref = firebaseDatabase.getReference("Token_Records");

        myad = new myadapter();
        gridview_kitchen.setAdapter(myad);

        kitchen_ref.orderByChild("deliverydate").equalTo(0).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                al_customerorders.clear();
                for(DataSnapshot sin : snapshot.getChildren())
                {
                    CustomerOrder customerorder_obj = sin.getValue(CustomerOrder.class);
                    al_customerorders.add(customerorder_obj);

                    //add to arraylist
                }
                myad.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        kitchen_ref.orderByChild("deliverydate").equalTo(0).addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                Log.d("Childadded",snapshot.toString());
                CustomerOrder neworder= snapshot.getValue(CustomerOrder.class);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

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
            return al_customerorders.size();
        }

        @Override
        public Object getItem(int i) {
            return al_customerorders.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i * 100;
        }

        @Override
        public View getView(int i, View customView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            customView = inflater.inflate(R.layout.single_row_design_of_kitchenhome, parent, false);

            CustomerOrder obj = al_customerorders.get(i);

            TextView tv_showorderno= customView.findViewById(R.id.tv_showorderno);
            TextView tv_showorderdate = customView.findViewById(R.id.tv_showorderdate);
            TextView tv_ordereditems = customView.findViewById(R.id.tv_ordereditems);
            Button btn_orderstatus = customView.findViewById(R.id.btn_orderstatus);


            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String date_show = sdf.format(new Date(obj.getOrderdate()));

            tv_showorderno.setText(obj.getOrderno()+"");
            tv_showorderdate.setText(date_show+"");


            ArrayList<OrderedItems> al23 = obj.getOrderItems();
            String         items = "";
            for (OrderedItems ds : al23)
            {
                items += ds.getItem_name()+"  X  "+ds.getItem_qty()+"\r\n";

            }



            Log.d("MYMSG",items);
            tv_ordereditems.setText(items);
            if (obj.getStatus().toLowerCase().equals("pending")) {
                btn_orderstatus.setBackgroundColor(Color.RED);
                btn_orderstatus.setText("pending");
            }
            else if (obj.getStatus().toLowerCase().equals("cooking")) {
                btn_orderstatus.setBackgroundColor(Color.BLUE);
                btn_orderstatus.setText("cooking");
            }

//            btn_orderstatus.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v) {
//                   //check stataus >> pending
//
//                    if(obj.getStatus().equals("pending"))
//                        kitchen_ref.child(obj.getPushkey()).child("status").setValue("Cooking");
//
//
//                }
//            });
            btn_orderstatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(KitchenHome.this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are You Sure?");
                    builder.setNegativeButton("No",null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            if(btn_orderstatus.getText().toString().toLowerCase().equals("pending"))
                            {
                                btn_orderstatus.setText("cooking");
                                btn_orderstatus.setBackgroundColor(Color.BLUE);
                                kitchen_ref.child(obj.getPushkey()).child("status").setValue("cooking");
                                String message = "Your order no. " + obj.getOrderno() + " is in cooking process";

                                //
                                token_ref.child(obj.getMobileno()).child("device_token").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String devicetoken = snapshot.getValue(String.class);
                                            SendNotification(message,devicetoken);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                                //

                            }
                            else if(btn_orderstatus.getText().toString().toLowerCase().equals("cooking"))
                            {
                                btn_orderstatus.setText("delivered");

                                kitchen_ref.child(obj.getPushkey()).child("status").setValue("delivered");
                                kitchen_ref.child(obj.getPushkey()).child("deliverydate").setValue(new Date().getTime());
                                String message = "Your order no. " + obj.getOrderno() + " is ready for delivery";
                                //
                                token_ref.child(obj.getMobileno()).child("device_token").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String devicetoken = snapshot.getValue(String.class);
                                            SendNotification(message,devicetoken);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                                //
                            }
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            });


            return customView;

        }
    }

    private void SendNotification(final String message, final String devicetoken) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final String cloudserverip = "server1.vmm.education";

//
        final String serverkey = "AAAAC9R_F7w:APA91bHl_gqYiEj-bP8Fha6fdYgjfUZUixxUE_XXvlHp7KQ1iI4p7HH2-vyZtF6SeSnK2pKyK9SYmhOXnyupd1puZ_hYclzQl17q1D1cvgiEXeARWnTh0tIVhodqS7sr-_I1PYoSXjuu";
        String url = "http://" + cloudserverip + "/VMMCloudMessaging/SendSimpleNotificationUsingTokens";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("MYMESSAGE", "RESPONSE " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MYMESSAGE", error.toString());
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("serverkey", serverkey);
                MyData.put("tokens", devicetoken);
                MyData.put("title", "Digital Food Ordering.");
                MyData.put("message", message);
                return MyData;
            }
        };


        requestQueue.add(stringRequest);
    }

}