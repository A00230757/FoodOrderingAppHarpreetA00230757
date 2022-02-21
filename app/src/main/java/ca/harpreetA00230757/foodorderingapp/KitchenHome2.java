package ca.harpreetA00230757.foodorderingapp;
//

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KitchenHome2 extends AppCompatActivity {
    RecyclerView rcv_runningorders;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference customerOrders;
    ArrayList<CustomerOrder> customerOrderArrayList;
    MyRecyclerAdapter myRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_home2);
        rcv_runningorders = findViewById(R.id.rcv_runningorders);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        rcv_runningorders.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        firebaseDatabase = FirebaseDatabase.getInstance();
        customerOrders = firebaseDatabase.getReference();
        customerOrderArrayList = new ArrayList<>();
        myRecyclerAdapter = new MyRecyclerAdapter();
        rcv_runningorders.setAdapter(myRecyclerAdapter);


        LoadData();
    }
    private void LoadData() {
        Toast.makeText(this, "load data called", Toast.LENGTH_SHORT).show();
        Log.d("ok", "load data");
        customerOrders.child("orders").orderByChild("deliverydate").equalTo(0)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        customerOrderArrayList.clear();
                        // Log.d("ok", "mainds : " + dataSnapshot + " ");
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Log.d("ok", dataSnapshot1 + "");
                            CustomerOrder obj = dataSnapshot1.getValue(CustomerOrder.class);
                            customerOrderArrayList.add(obj);
                        }
                        myRecyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        firebaseDatabase.getReference().child("orders").orderByChild("deliverydate").equalTo(0).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CustomerOrder neworder = dataSnapshot.getValue(CustomerOrder.class);
                printkot(neworder);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private String spaces(String str, int num) {
        String spaces = "";
        Log.d("Print", num - str.length() + "");
        if (num - str.length() < 0) {
            spaces = "  ";
        } else {
            // spaces = "\t\t";
            for (int i = 0; i < num - str.length(); i++) {
                spaces += " ";
            }
        }
        return spaces;
    }


    private void printkot(CustomerOrder obj)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:MM");
        String formattedDate =  sdf.format(new Date().getTime());
        Log.d("abc", "in print size" + obj.getOrderItems().size());
        String dataToPrint =
                "<BIG><BOLD><CENTER> Food Orders <BR>" +
                        "<CENTER>Queens Road, Amritsar<BR>" +
                        "<CENTER>+91-9876543210<BR>" +
                        "<CENTER>" + formattedDate + "<BR>" +
                        "<CENTER>Kitchen Order Ticket<BR>" +
                        "<CENTER> Order No. " + obj.getOrderno()+ "<BR>" +
                        "<BOLD><LEFT>Item\t\t\t\tQty<BR><LINE0><BR>";
        for (OrderedItems ordobj : obj.getOrderItems()) {
            String itemname = ordobj.getItem_name();
            if (34 - itemname.length() <= 0) {
                itemname = itemname.substring(0, 30);
            }
            String netamount = String.valueOf(ordobj.getItem_qty() * ordobj.getTotalprice());
            dataToPrint += "<BOLD>" + itemname + spaces(itemname, 34) + "<SMALL><NORMAL>" + ordobj.getItem_qty() + "<BR>";

            dataToPrint += "<LINE>";
        }
        dataToPrint += "<CUT>\n" + "<DRAWER>\n";// OPEN THE CASH DRAWER
        try {

            Intent intent = new Intent("pe.diegoveloper.printing");

            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, dataToPrint);
            startActivityForResult(intent, 1234);


        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=pe.diegoveloper.printerserverapp"));
            startActivity(intent);
        }
    }



    class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

        // Define ur own View Holder (Refers to Single Row)
        class MyViewHolder extends RecyclerView.ViewHolder {
            CardView singlecardview;

            // We have Changed View (which represent single row) to CardView in whole code
            public MyViewHolder(CardView itemView) {
                super(itemView);
                singlecardview = (itemView);
            }

        }

        // Inflate ur Single Row / CardView from XML here
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View viewthatcontainscardview = inflater.inflate(R.layout.cardviewkitchenhome_layout, parent, false);
            CardView cardView = (CardView) (viewthatcontainscardview.findViewById(R.id.cardview_kitchenhome));
            return new MyViewHolder(cardView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            CardView localcardview = holder.singlecardview;
            TextView tvorderno, tvorderdate, tvorderitems;
            final Button btnchangestatus;
            tvorderno = localcardview.findViewById(R.id.tv_kitchenhomeorderno);
            tvorderdate = localcardview.findViewById(R.id.tv_kitchenhomeorderdate);
            tvorderitems = localcardview.findViewById(R.id.tv_kitchenhomeorderitems);
            btnchangestatus = localcardview.findViewById(R.id.btn_kitchenhomeorderstatus);

            final CustomerOrder customerOrder = customerOrderArrayList.get(position);
            tvorderno.setText(customerOrder.getOrderno() + "");

            Timestamp ts = new Timestamp(customerOrder.getOrderdate());
            Date dt = new Date(ts.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:MM:SS");
            String orderdate = sdf.format(dt);


            tvorderdate.setText(orderdate);

            if (customerOrder.getStatus().toLowerCase().equals("pending")) {
                btnchangestatus.setBackgroundColor(Color.RED);
                btnchangestatus.setText("pending");
            }
            else if (customerOrder.getStatus().toLowerCase().equals("cooking")) {
                btnchangestatus.setBackgroundColor(Color.BLUE);
                btnchangestatus.setText("cooking");
            }

            ArrayList<OrderedItems> orderItems = customerOrder.getOrderItems();

            String items = "";
            for (OrderedItems obj : orderItems) {
                items += obj.getItem_qty() + " X " + obj.getItem_name() + "\r\n";
            }
            tvorderitems.setText(items);

            btnchangestatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(KitchenHome2.this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are You Sure?");
                    builder.setNegativeButton("No",null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            if(btnchangestatus.getText().toString().toLowerCase().equals("pending"))
                            {
                                btnchangestatus.setText("cooking");
                                btnchangestatus.setBackgroundColor(Color.BLUE);
                                firebaseDatabase.getReference().child("orders").
                                        child(customerOrder.getPushkey()).child("status").setValue("cooking");
                                String message = "Your order no. " + customerOrder.getOrderno() + " is in cooking process";
                                gettoken(customerOrder.getMobileno(),message);
                            }
                            else if(btnchangestatus.getText().toString().toLowerCase().equals("cooking"))
                            {
                                btnchangestatus.setText("delivered");
                                btnchangestatus.setBackgroundColor(Color.GREEN);
                                DatabaseReference reference = firebaseDatabase.getReference();
                                reference.child("orders").
                                        child(customerOrder.getPushkey()).child("status").setValue("delivered");
                                reference.child("orders").
                                        child(customerOrder.getPushkey()).child("deliverydate").setValue(new Date().getTime());
                                String message = "Your order no. " + customerOrder.getOrderno() + " is ready for delivery";
                                gettoken(customerOrder.getMobileno(),message);

                            }
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return customerOrderArrayList.size();
        }
    }
    private void gettoken(String mobileno, final String message)
    {
        String packagename = "com.example.digitalfoodorderingcustomer";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final String cloudserverip = "server1.vmm.education";

        String url = "http://" + cloudserverip + "/VMMCloudMessaging/GetTokenOfMobileno?packagename=" + packagename + "&mobileno=" + mobileno;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("MYMESSAGE", "RESPONSE " + response);

                        String devicetoken = response;

                        //send notification
                        SendNotification(message,devicetoken);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MYMESSAGE", error.toString());
                    }
                });


        requestQueue.add(stringRequest);
    }

    private void SendNotification(final String message, final String devicetoken)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final String cloudserverip = "server1.vmm.education";

        final String serverkey = "AAAAtmftrxQ:APA91bEWv8Q6J8nWAhd5xXZuTPjwCtwrr3YOpJWo6QXjf9Z_mFCbVPyb0gEtwuLdMarQs_RN0_nNGkCKfNad7p4GQlUmr03931QKxv2dBzs8tFWRxjrDZJdqZQyIsPbFm02yEKPg5v5R\n";
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
                MyData.put("title", "test title");
                MyData.put("message", message);
                return MyData;
            }
        };


        requestQueue.add(stringRequest);
    }
}