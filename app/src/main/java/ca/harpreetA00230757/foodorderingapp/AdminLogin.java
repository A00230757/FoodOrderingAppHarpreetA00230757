package ca.harpreetA00230757.foodorderingapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class  AdminLogin extends AppCompatActivity
{
    EditText et_username,et_password;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mainref;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        FirebaseApp.initializeApp(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mainref = firebaseDatabase.getReference("admin");

    }
    public void login(View v)
    {
        Intent in = new Intent(AdminLogin.this,AdminHome.class);
        startActivity(in);
        finish();
//        String username = et_username.getText().toString();
//        String password = et_password.getText().toString();
//        if(username.equals("") || password.equals(""))
//        {
//            Toast.makeText(this,"All Fields Required",Toast.LENGTH_LONG).show();
//        }
//        else
//        {
//            mainref.child(username).addListenerForSingleValueEvent(new ValueEventListener()
//            {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot)
//                {
//                    if (snapshot.exists())
//                    {
//                        AdminDetails admin_obj = snapshot.getValue(AdminDetails.class);
////                    assert admin_obj != null;
//                        // tv1.setText(admin_obj.toString());
//                        Log.d("Hello", admin_obj.getPassword());
//                        if (admin_obj.getPassword().equals(password))
//                        {
//                            Toast.makeText(AdminLogin.this, "Login Succesfull", Toast.LENGTH_LONG).show();
//                            Intent in = new Intent(AdminLogin.this,AdminHome.class);
//                            startActivity(in);
//                            finish();
//                        }
//                        else
//                        {
//                            Toast.makeText(AdminLogin.this, "Wrong Password", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                    else
//                    {
//                        Toast.makeText(AdminLogin.this, "Username Invalid", Toast.LENGTH_LONG).show();
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
    }
}