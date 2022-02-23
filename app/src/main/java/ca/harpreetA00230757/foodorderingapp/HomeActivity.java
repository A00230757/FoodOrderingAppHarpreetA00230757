package ca.harpreetA00230757.foodorderingapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void launchMainActivity(View v)
    {
        Intent in = new Intent(this,ViewAllCategories.class);
        startActivity(in);
    }

    public void viewActiveOrders(View v)
    {
        Intent in = new Intent(this,FetchCurrentOrdersUser.class);
        startActivity(in);
    }

    public void viewOrdersHistory(View v)
    {
        //orderhistory all orders
        Intent in = new Intent(this, FetchAllOrdersUser.class);
        startActivity(in);
    }

    public void changePassword(View view)
    {
    }

    public void logout(View view)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("foodorderpref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("usermobileno");

        editor.apply();

        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
    }
}