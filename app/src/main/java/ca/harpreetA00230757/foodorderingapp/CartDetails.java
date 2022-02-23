package ca.harpreetA00230757.foodorderingapp;

//


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


//this activity shows already added food itemd in cart
public class CartDetails extends AppCompatActivity implements PaymentResultListener
{
    //    ArrayList<FoodItemDetails> al;
//    ArrayList<CartDetails> al_cart;

    //declarations for cart itemslist view , firabse databse , storage
    ListView lv_cartItems;
    TextView tv_foodcarttotalamount;
    TextView tv_foodcartgst;
    TextView tv_foodcartamountpayable;
    myadapter myad;
    AlertDialog.Builder builder;

    //declarations for total amount , gst , sub amount variables
    int sub_amount = 0;
    double gst = 0.0;
    double total_amount = 0.0;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference orderref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_details);

        lv_cartItems = findViewById(R.id.lv_cart_items);
//        al_cart = new ArrayList<>();


        //giving memory to different variables and list
        tv_foodcarttotalamount = findViewById(R.id.tv_foodcarttotalamount);
        tv_foodcartgst = findViewById(R.id.tv_foodcartgst);
        tv_foodcartamountpayable = findViewById(R.id.tv_foodcartamountpayable);


        myad = new myadapter();
        lv_cartItems.setAdapter(myad);
        if(ProgressDialogClass.orderedItems == null)
        {
            Toast.makeText(this, "No item in cart", Toast.LENGTH_SHORT).show();
        }
        else {
            myad.notifyDataSetChanged();
            calculateAmount();
        }
 /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        Checkout.preload(getApplicationContext());


        findViewById(R.id.bt_makepayment).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startPayment();
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        orderref = firebaseDatabase.getReference("Orders");

    }

    long orderno;

    //this function store order in database after successfull payment
    @Override
    public void onPaymentSuccess(String s)
    {
        Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();
        String pushKey = orderref.push().getKey();
        Date date_obj = new Date();
        SimpleDateFormat sdf1  = new SimpleDateFormat("yyyy-MM-dd");
        String date_vise_sale = sdf1.format(date_obj);
        //
        SimpleDateFormat sdf2  = new SimpleDateFormat("yyyy-MM");
        String month_sale = sdf2.format(date_obj);
        //

        long orderDate = date_obj.getTime();
        try
        {
            //insert
            firebaseDatabase.getReference("ORDER_NO").child("Orders").child("orderno").runTransaction(new Transaction.Handler()
            {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    if(mutableData.getValue()==null)
                    {
                        mutableData.setValue(100);
                        orderno = 100;
                    }
                    else
                    {
                        orderno = (Long)   mutableData.getValue() + 1;
                        mutableData.setValue(orderno);
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                    if(databaseError != null)
                    {
                        Toast.makeText(CartDetails.this, "Error generating order no.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        CustomerOrder customerOrder_obj = new CustomerOrder(orderno,ProgressDialogClass.mobileno,"pending",0,sub_amount,gst,total_amount,orderDate,0L,ProgressDialogClass.orderedItems,pushKey,"","" ,date_vise_sale,month_sale);

                        orderref.child(pushKey).setValue(customerOrder_obj);
                        Toast.makeText(getApplicationContext(),"Order Placed Successfully",Toast.LENGTH_SHORT).show();
                        ProgressDialogClass.orderedItems.clear();
                        finish();

                    }
                }
            });


            //



        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Error"+s, Toast.LENGTH_SHORT).show();

    }


    ////////Adapter code/////
    class myadapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return ProgressDialogClass.orderedItems.size();
        }

        @Override
        public Object getItem(int i) {
            return ProgressDialogClass.orderedItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i * 100;
        }

        @Override
        public View getView(int i, View customView, ViewGroup parent)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            customView = inflater.inflate(R.layout.single_row_design_of_listview_cart_items, parent, false);

            OrderedItems obj_cartitem = ProgressDialogClass.orderedItems.get(i);
            TextView tv_itemname = customView.findViewById(R.id.tv_itemname);
            TextView tv_itemqty = customView.findViewById(R.id.tv_itemqty);

            TextView tv_foodcarttotalamount = customView.findViewById(R.id.tv_foodcarttotalamount);
            TextView tv_foodcartgst = customView.findViewById(R.id.tv_foodcartgst);
            TextView tv_foodcartamountpayable = customView.findViewById(R.id.tv_foodcartamountpayable);



            ImageView imv111 = customView.findViewById(R.id.imv_item);
            ImageView imv_addtocart = customView.findViewById(R.id.imv_addto_cart);
            ImageView imv_deletefromcart = customView.findViewById(R.id.imv_deletefrom_cart);
            ImageView imv_cancelfrom_cart = customView.findViewById(R.id.imv_cancelfrom_cart);

            tv_itemname.setText(obj_cartitem.getItem_name());
            tv_itemqty.setText("Qty :"+obj_cartitem.getItem_qty()+"");



            Picasso.get().load(obj_cartitem.getItem_photo()).resize(250, 250).into(imv111);
            imv_addtocart.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //add
                    addToManagecart(i);
                }
            });

            imv_deletefromcart.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    deleteFromManageCart(i);
                }
            });
          /*  imv_cancelfromcart.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    lv_cartItems.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int index, long id)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CartDetails.this);
                            builder.setTitle("Deletiing element");
                            builder.setMessage("Are you sure you want to delete this item?");
                            builder.setIcon(R.drawable.cancel_icon);

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int i)
                                {
                                    GlobalClass.orderedItems.remove(index);
                                    myad.notifyDataSetChanged();

                                    Toast.makeText(CartDetails.this,"Item Removed",Toast.LENGTH_LONG).show();
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int i)
                                {

                                    Toast.makeText(CartDetails.this,"Cancelled Deletion",Toast.LENGTH_LONG).show();

                                }
                            });

                            AlertDialog ad = builder.create();
                            ad.show();
                        }
                    });
                }

            });*/


            // to delete any item from cart
            imv_cancelfrom_cart.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    builder = new AlertDialog.Builder(CartDetails.this);
                    builder.setTitle("Deleting element");
                    builder.setMessage("Are you sure you want to delete this item?");
                    builder.setIcon(R.drawable.cancel_icon);

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int postion)
                        {
                            // Toast.makeText(CartDetails.this, GlobalClass.orderedItems.size()+"......."+i, Toast.LENGTH_SHORT).show();
                            ProgressDialogClass.orderedItems.remove(i);
                            myad.notifyDataSetChanged();
                            calculateAmount();

                            Toast.makeText(CartDetails.this,"Item Removed",Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int i)
                        {

                            Toast.makeText(CartDetails.this,"Cancelled Deletion",Toast.LENGTH_LONG).show();

                        }
                    });

                    AlertDialog ad = builder.create();
                    ad.show();

                }
            });


            return customView;

        }
    }


    // to start payment
    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Digital Food Ordering");
            options.put("description", "Billing Amount");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", total_amount * 100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "");
            preFill.put("contact","+19052264147");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    // to add more item in manage cart
    public void addToManagecart(int index)
    {
        int qty1 = ProgressDialogClass.orderedItems.get(index).getItem_qty()+1;
        ProgressDialogClass.orderedItems.get(index).setItem_qty(qty1);
        ProgressDialogClass.orderedItems.get(index).setTotalprice(ProgressDialogClass.orderedItems.get(index).getItem_price() * ProgressDialogClass.orderedItems.get(index).getItem_qty() );
        myad.notifyDataSetChanged();


        calculateAmount();


    }

    // to delete item from manage cart
    public void deleteFromManageCart(int index)
    {
        if(ProgressDialogClass.orderedItems.get(index).getItem_qty()<1){
            Toast.makeText(getApplicationContext(), "Qty is less than 1", Toast.LENGTH_SHORT).show();
        }
        else {
            int qty1 = ProgressDialogClass.orderedItems.get(index).getItem_qty() - 1;
            ProgressDialogClass.orderedItems.get(index).setItem_qty(qty1);
            ProgressDialogClass.orderedItems.get(index).setTotalprice(ProgressDialogClass.orderedItems.get(index).getItem_price() * ProgressDialogClass.orderedItems.get(index).getItem_qty());
        }
        myad.notifyDataSetChanged();


        calculateAmount();

    }


    // to calculate amount
    public  void calculateAmount()
    {
        int sub_amount = 0;
        for(OrderedItems item : ProgressDialogClass.orderedItems)
        {
            sub_amount = sub_amount+item.getTotalprice();

        }
        // Toast.makeText(this,"amount"+sub_amount,Toast.LENGTH_SHORT).show();
        tv_foodcarttotalamount.setText(sub_amount+"");
        calculateGst(sub_amount);
    }

    // to calculate tax
    public void calculateGst(int subamount)
    {
        double part = 5.0;
        gst = (part*subamount)/100;
        // Toast.makeText(this,"gst"+gst,Toast.LENGTH_SHORT).show();
        tv_foodcartgst.setText(gst+"");
        calculateAmountPayable(subamount,gst);
    }

    // to calculate payaable amount
    public void calculateAmountPayable(int subamount,double gst)
    {
        total_amount = subamount + gst;
        tv_foodcartamountpayable.setText(total_amount+"");
        // Toast.makeText(this,"total :"+total_amount,Toast.LENGTH_SHORT).show();
    }
}