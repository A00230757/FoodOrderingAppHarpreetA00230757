package ca.harpreetA00230757.foodorderingapp;
//
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


//this is splash screen where thread hold it for 3 seconds and after tgat MainActivity appears
public class LauncherScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_screen);
        Intent intent = new Intent(this,MainActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        },3000);
    }
}