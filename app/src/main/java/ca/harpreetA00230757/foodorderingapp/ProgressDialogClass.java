package ca.harpreetA00230757.foodorderingapp;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogClass {
    public static ProgressDialog CreateProgressDialog(Context context, String title, String message)
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progressDialog;
    }
}

