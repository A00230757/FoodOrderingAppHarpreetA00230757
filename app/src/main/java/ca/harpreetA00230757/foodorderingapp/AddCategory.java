package ca.harpreetA00230757.foodorderingapp;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class AddCategory extends AppCompatActivity
{
    EditText et_categoryname,et_categorydesc;
    ImageView imv_category;
    FirebaseStorage firebaseStorage;
    StorageReference  mainref_storage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mainref;
    Uri GalleryUri = null;
    Bitmap CameraBitmap = null;
    String type = "";
    String filenametobeuploaded;
    String tempfilepath;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        firebaseStorage = FirebaseStorage.getInstance();
        mainref_storage = firebaseStorage.getReference("category_mages");

        firebaseDatabase = FirebaseDatabase.getInstance();
        mainref = firebaseDatabase.getReference("category");

        et_categoryname = findViewById(R.id.et_categoryname);
        et_categorydesc = findViewById(R.id.et_categorydesc);
        imv_category = findViewById(R.id.imv_category);

        startPermission();
    }

    public void fromCamera(View v)
    {
        imv_category.setVisibility(View.VISIBLE);
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(in,100);
    }
    public void fromGallery(View v)
    {
        imv_category.setVisibility(View.VISIBLE);
        Intent in = new Intent(Intent.ACTION_PICK);
        in.setType("image/*");
        startActivityForResult(in,101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent backdata)
    {
        super.onActivityResult(requestCode, resultCode, backdata);
        if(requestCode == 100)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Bundle bundle = backdata.getExtras();
                Bitmap map = (Bitmap) bundle.get("data");
                CameraBitmap = map;
                type = "camera";
                try
                {
                    tempfilepath = Environment.getExternalStorageDirectory() + File.separator + "temp.jpg";
                    FileOutputStream fos = new FileOutputStream(tempfilepath);
                    CameraBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);

                    filenametobeuploaded = "temp" + (int) (Math.random() * 1000000000) + ".jpg";
                    Toast.makeText(this, "" + filenametobeuploaded, Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                imv_category.setImageBitmap(map);

            }
        }
        else if(requestCode == 101)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Uri uri = backdata.getData();
                GalleryUri = backdata.getData();
                type = "gallery";
                imv_category.setImageURI(uri);
            }
        }
    }

    public void submit(View v)
    {
        progressDialog = GlobalClass.CreateProgressDialog(this,"Adding Category","Please Wait......");
        progressDialog.show();
        String  category_name = et_categoryname.getText().toString();
        String  category_desc = et_categorydesc.getText().toString();

        if(category_name.equals("") || category_desc.equals(""))
        {
            Toast.makeText(this,"All Fields Required",Toast.LENGTH_LONG).show();
        }

        if(CameraBitmap == null && GalleryUri == null)
        {
            Toast.makeText(this, "Select an Image from either gallery or click from camera", Toast.LENGTH_SHORT).show();
        }
        else
        {

            mainref.child(category_name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Toast.makeText(AddCategory.this, category_name+" Already Exists", Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                    else
                    {
                        //images
                        if (type.equals("gallery"))
                        {
                            // gallery upload
                            File localfile = new File(getRealPathFromURI(GalleryUri));

                            String local2 = "temp" + (int) (Math.random() * 1000000000) + localfile.getName();
                            Uri uri2 = Uri.fromFile(localfile);
                            final StorageReference newfile = mainref_storage.child(local2);
                            final UploadTask uploadTask = newfile.putFile(uri2);
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                            {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    Task<Uri> uriTask = newfile.getDownloadUrl();
                                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>()
                                    {
                                        @Override
                                        public void onSuccess(Uri uri)
                                        {
                                            String downloadpath = uri.toString();
                                            //insert
                                            CategoryDetails category_obj = new CategoryDetails(category_name,category_desc,downloadpath);
                                            mainref.child(category_name).setValue(category_obj);
                                            progressDialog.hide();
                                            Toast.makeText(AddCategory.this, "Category Added", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    uploadTask.addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {

                                        }
                                    });

                                }
                            });

                        }

                        else
                        {
                            File localfile = new File(getRealPathFromURI(getImageUri(getApplicationContext(), CameraBitmap)));

                            String local2 = "temp" + (int) (Math.random() * 1000000000) + localfile.getName();
                            Uri uri2 = Uri.fromFile(localfile);
                            final StorageReference newfile = mainref_storage.child(local2);
                            final UploadTask uploadTask = newfile.putFile(uri2);
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                            {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    Task<Uri> uriTask = newfile.getDownloadUrl();
                                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>()
                                    {
                                        @Override
                                        public void onSuccess(Uri uri)
                                        {
                                            String downloadpath = uri.toString();
                                            //insert

                                            CategoryDetails category_obj = new CategoryDetails(category_name,category_desc,downloadpath);
                                            mainref.child(category_name).setValue(category_obj);
                                            progressDialog.hide();
                                            Toast.makeText(AddCategory.this, "Category Added", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    uploadTask.addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {

                                        }
                                    });
                                }
                            });

                        }
                        //
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }

    }
    public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public Uri getImageUri(Context inContext, Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    ///////////Run time permissions /////////
    public void startPermission()
    {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)//if android version is greater than M(marshmallow)
        {
            // check if permissions are already granted,otherwise show ask Permission dialog
            if(checkPermission())
            {
                // Toast.makeText(this,"All Permissions already granted",Toast.LENGTH_LONG).show();
            }
            else
            {
                // Toast.makeText(this,"Permissions Not Granted",Toast.LENGTH_LONG).show();
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

}