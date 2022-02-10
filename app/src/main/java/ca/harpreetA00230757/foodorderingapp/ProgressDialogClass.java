package ca.harpreetA00230757.foodorderingapp;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.ArrayList;

public class ProgressDialogClass {
    public static ArrayList<OrderedItems> orderedItems = new ArrayList<>();
    public static ArrayList<OrderedItems> selected_orderedItems = new ArrayList<>();
    public static String mobileno = "+916280406195";
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

