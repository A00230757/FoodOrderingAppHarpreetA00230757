package ca.harpreetA00230757.foodorderingapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


// this activity generate sale report based on selected period
public class ViewDailySale extends AppCompatActivity
{

    // declarations of calender , text view , list for sale items , firebase database and storage and total variables.
    int dd, yy, mm;
    TextView tv_showdate;
    ListView lv_dailysale;
    TextView tv_showtotalamount;
    myadapter myad;

    final Calendar myCalendar = Calendar.getInstance();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dailysale_ref;
    ArrayList<DailySale> dailysalesArrayList  = new ArrayList<>();

    int dailysaleamount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_sale);
// memory to calender , text view , list for sale items , firebase database and storage and total variables.


        tv_showdate = findViewById(R.id.tv_showdate);
        tv_showtotalamount = findViewById(R.id.tv_showtotalamount);
        lv_dailysale = findViewById(R.id.lv_dailysale);


        firebaseDatabase = FirebaseDatabase.getInstance();
        dailysale_ref = firebaseDatabase.getReference("Orders");

        myad = new myadapter();

        Calendar calendar = Calendar.getInstance();
        yy = calendar.get(Calendar.YEAR);
        mm = calendar.get(Calendar.MONTH);

        dd = calendar.get(Calendar.DAY_OF_MONTH);



        // after selecting a particular period fetch_data function called to load data from cloud to list
        tv_showdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatePickerDialog dpd = new DatePickerDialog(ViewDailySale.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                        Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();
                        try {

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date d1 = sdf.parse(date);
                            String exactdate = sdf.format(d1);
                            Toast.makeText(ViewDailySale.this, ""+exactdate, Toast.LENGTH_SHORT).show();
                            tv_showdate.setText(exactdate);
                            fetch_data(tv_showdate.getText().toString());//load data of sales from cloud to list
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, yy, mm, dd);
                dpd.show();
            }
        });




    }


    //load data of sales from cloud to list
    public void fetch_data(String selecteddate){
        Toast.makeText(this, ""+selecteddate, Toast.LENGTH_SHORT).show();
        String startdate = selecteddate + " 00:00:00";
        String enddate = selecteddate + " 23:59:59";

        dailysaleamount = 0;
        Timestamp mintimestamp,maxtimestamp;
        final long mintime,maxtime;

        try{

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(startdate);
            mintimestamp = new Timestamp(parsedDate.getTime());
            Date parsedDate2 = dateFormat.parse(enddate);
            maxtimestamp = new Timestamp(parsedDate2.getTime());


            mintime = mintimestamp.getTime();
            maxtime= maxtimestamp.getTime();

            Log.d("bingo", mintime +  "  " + maxtime);
            Toast.makeText(this, ""+mintime+"...."+maxtime, Toast.LENGTH_SHORT).show();


            dailysaleamount = 0;
            ArrayList<DailySale> dumpsaleList = new ArrayList<>();
            dailysale_ref.orderByChild("date_vise_sale").equalTo(selecteddate).addListenerForSingleValueEvent(new ValueEventListener()
            {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    dumpsaleList.clear();
                    if(snapshot.exists()){

                        for(DataSnapshot sin : snapshot.getChildren())
                        {
                            CustomerOrder customerOrder = sin.getValue(CustomerOrder.class);
                            if(customerOrder.getStatus().equals("delivered")){
                                for(OrderedItems orderedItems_obj: customerOrder.getOrderItems())
                                {
                                    dumpsaleList.add(new DailySale(orderedItems_obj.getItem_name(), orderedItems_obj.getTotalprice()));
                                }
                            }
                        }
                    }
                    else {
                        dumpsaleList.clear();
                        dailysalesArrayList.clear();
                        myad.notifyDataSetChanged();
                    }
                    Log.d("dailysale",dumpsaleList.size()+"");

                    for(DailySale ds : dumpsaleList)
                    {
                        boolean flag = false;
                        int index = 0;
                        int amount  =0;
                        for(DailySale obj : dailysalesArrayList)
                        {
                            if (ds.getItemname().equals(obj.getItemname()))
                            {
                                flag= true;
                                amount= obj.getItemprice();
                                break;
                            }
                            index ++;
                        }
                        if(flag==false)
                        {
                            dailysalesArrayList.add(ds);
                        }
                        else
                        {
                            int oldamount =  dailysalesArrayList.get(index).getItemprice();
                            dailysalesArrayList.get(index).setItemprice(oldamount  + amount);
                        }
                    }

                    for(DailySale ds : dailysalesArrayList)
                    {
                        Log.d("dailysale",ds.getItemname() + " " + ds.getItemprice());
                        dailysaleamount += ds.getItemprice();

                    }
                    tv_showtotalamount.setText("\u20B9 " +dailysaleamount+"");
                    myad.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
            lv_dailysale.setAdapter(myad);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    ////////Adapter code link cloud data to sales list/////
    class myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dailysalesArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return dailysalesArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i * 100;
        }

        @Override
        public View getView(int i, View customView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            customView = inflater.inflate(R.layout.single_row_design_of_listview_dailysale, parent, false);

            DailySale obj = dailysalesArrayList.get(i);


            TextView tv_dailysaleitemname = customView.findViewById(R.id.tv_dailysaleitemname);
            TextView tv_dailysaleamount = customView.findViewById(R.id.tv_dailysaleamount);

            tv_dailysaleitemname.setText(obj.getItemname());
            tv_dailysaleamount.setText("\u20B9 " +obj.getItemprice()+"");



            return customView;

        }
    }

}