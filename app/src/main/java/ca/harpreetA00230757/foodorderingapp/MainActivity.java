package ca.harpreetA00230757.foodorderingapp;
//
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


//here admin or user can open there login screens
public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
//    public void login(View v)
//    {
//        Intent in = new Intent(this,ManageCategory.class);
//        startActivity(in);
//    }
//    public void loginKitchen(View v)
//    {
//        Intent in = new Intent(this,ManageProducts.class);
//        startActivity(in);
//    }

        public void login(View v)
    {
        Intent in = new Intent(this,AdminLogin.class);
        startActivity(in);
    }
    public void loginKitchen(View v)
    {
        Intent in = new Intent(this,KitchenLogin.class);
        startActivity(in);
    }
    public void shop(View v)
    {
        Intent in = new Intent(this,UserLogin.class);
        startActivity(in);
    }
//    public void viewadminorders(View v)
//    {
//        Intent in = new Intent(this,FetchUserAllOrders.class);
//        startActivity(in);
//    }

    public void viewuserorders(View v)
    {
        Intent in = new Intent(this,FetchAllOrdersUser.class);
        startActivity(in);
    }
//    public void viewcustomerreviews(View v)
//    {
//        Intent in = new Intent(this,View_CustomerReviews.class);
//        startActivity(in);
//    }
//    public void viewdailysale(View v)
//    {
//        Intent in = new Intent(this,ViewDailySale.class);
//        startActivity(in);
//    }
}