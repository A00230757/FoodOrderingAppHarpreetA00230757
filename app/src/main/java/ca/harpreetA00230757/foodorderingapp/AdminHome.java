package ca.harpreetA00230757.foodorderingapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AdminHome extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        startPermission();
    }

    public void startPermission()
    {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)//if android version is greater than M(marshmallow)
        {
            // check if permissions are already granted,otherwise show ask Permission dialog
            if(checkPermission())
            {
                Toast.makeText(this,"All Permissions already granted",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this,"Permissions Not Granted",Toast.LENGTH_LONG).show();
                requestPermission();
            }
        }
    }
    //this method returns true if all permissions are already granted
    boolean checkPermission()
    {
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        // boolean result3 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;

        return (result1 && result2 );
    }

    public  void requestPermission()
    {
        // Show ask for permission Dialog(passing array of permissions that u want to ask)
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);


    }

    // After User Selects desired permissions,this method is automatically called
    //it has request code ,permissions array and corresponding grantresults array

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1)
        {
            if(grantResults.length > 0)
            {
                if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED )
                {
                    Toast.makeText(this, "All Permisssions Granted", Toast.LENGTH_SHORT).show();
                }

                if( grantResults[0] == PackageManager.PERMISSION_GRANTED )
                {
                    Toast.makeText(this, "Camera Permisssions Granted", Toast.LENGTH_SHORT).show();
                }
                if( grantResults[1] == PackageManager.PERMISSION_GRANTED )
                {
                    Toast.makeText(this, "Storage Permisssions Granted", Toast.LENGTH_SHORT).show();
                }

                if( grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED)
                {
                    Toast.makeText(this, "All Permissions Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void changePassword(View v)
    {
//        Intent in =  new Intent(this,ChangePasswordAdmin.class);
//        startActivity(in);
    }
    public void manageCategory(View v)
    {
        Intent in =  new Intent(this,ManageCategory.class);
        startActivity(in);
    }
    public void manageProducts(View v)
    {
        Intent in = new Intent(this,ManageProducts.class);
        startActivity(in);
    }
    public void viewDailySale(View v)
    {
        Intent in = new Intent(this,ViewDailySale.class);
        startActivity(in);
    }
    public void viewReviews(View v)
    {
        Intent in = new Intent(this,View_CustomerReviews.class);
        startActivity(in);
    }
    public void viewAllOrders(View v)
    {
        Intent in = new Intent(this,FetchUserAllOrders.class);
        startActivity(in);
    }
}