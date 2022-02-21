package ca.harpreetA00230757.foodorderingapp;
//
public class GetToken {
}


//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import static android.content.Context.MODE_PRIVATE;
//
//public class GetToken extends FirebaseInstanceIdService
//{
//    @Override
//    public void onTokenRefresh()
//    {
//        // Get updated InstanceID token.
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d("MYMESSAGE", "Refreshed token: " + refreshedToken);
//
//        // THIS TOKEN IN UNIQUE FOR EACH DEVICE AND IS ONLY AUTO GENERATED WHEN UR APP
//        // FIRST CONNECT TO INTERNET TO REGISTER IT ON CLOUD
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//
//        // Instance ID token to your app server.
//        savetoken(refreshedToken);
//    }
//
//    void savetoken(String refreshedToken)
//    {
//
//        //// Record a local copy of refreshed token in shared preference ////
//        SharedPreferences sharedPreferences = getSharedPreferences("mypref1",MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.putString("devicetoken",refreshedToken);
//
//        editor.commit();
//
//
//
//    }
//}
//
//
//
//
