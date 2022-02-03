package ca.harpreetA00230757.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class AddProduct extends AppCompatActivity
{
    EditText et_productname,et_productdesc,et_productprice;
    ImageView imv_product;
    Spinner spinner_category;
    RadioButton radiobt_veg,radiobt_nonveg;
    ProgressDialog progressDialog;

    FirebaseStorage firebaseStorage;
    StorageReference mainref_storage;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference fooditem_ref;

    Uri GalleryUri = null;
    Bitmap CameraBitmap = null;
    String type = "";
    String filenametobeuploaded;
    String tempfilepath;
    String product_type = "";
    String category_name ="";

    ArrayList<CategoryDetails> al;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        et_productname = findViewById(R.id.et_productname);
        et_productdesc = findViewById(R.id.et_productdesc);
        et_productprice = findViewById(R.id.et_productprice);
        radiobt_veg = findViewById(R.id.radiobt_veg);
        radiobt_nonveg = findViewById(R.id.radiobt_nonveg);
        imv_product = findViewById(R.id.imv_product);



        al = new ArrayList<>();
//        al.add(new CategoryDetails("Pizza", "", ""));
//        al.add(new CategoryDetails("Burger", "", ""));
//        al.add(new CategoryDetails("Pasta", "", ""));
//        al.add(new CategoryDetails("Wraps", "", ""));
//        al.add(new CategoryDetails("Lasagne", "",""));

        myadapter ad = new myadapter();

        spinner_category = findViewById(R.id.spinner_category);
        spinner_category.setAdapter(ad);

        firebaseStorage = FirebaseStorage.getInstance();
        mainref_storage = firebaseStorage.getReference("fooditems_images");

        firebaseDatabase = FirebaseDatabase.getInstance();
        fooditem_ref = firebaseDatabase.getReference("category");

        radiobt_veg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    product_type = "Veg";
                }
            }
        });
        radiobt_nonveg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    product_type = "Non Veg";
                }
            }
        });

        fooditem_ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                al.clear();
                if(snapshot.exists()){
                    for(DataSnapshot sin : snapshot.getChildren()){
                        CategoryDetails obj = sin.getValue(CategoryDetails.class);
                        al.add(obj);
                    }
                    ad.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(AddProduct.this, ""+al.get(position).getCategory_name()+" Selected", Toast.LENGTH_SHORT).show();
                category_name = al.get(position).getCategory_name();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void fromCamera(View v)
    {
        imv_product.setVisibility(View.VISIBLE);
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(in,100);
    }
    public void fromGallery(View v)
    {
        imv_product.setVisibility(View.VISIBLE);
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

                imv_product.setImageBitmap(map);

            }
        }
        else if(requestCode == 101)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Uri uri = backdata.getData();
                GalleryUri = backdata.getData();
                type = "gallery";
                imv_product.setImageURI(uri);
            }
        }
    }

    public void submit(View v)
    {
        progressDialog = ProgressDialogClass.CreateProgressDialog(this,"Adding Food Item","Please Wait!!");
        progressDialog.show();
        String product_name = et_productname.getText().toString();
        String product_desc = et_productdesc.getText().toString();
        int product_price = Integer.parseInt(et_productprice.getText()+"");


        if(product_name.equals("") || product_desc.equals(""))
        {
            progressDialog.hide();
            Toast.makeText(this,"All Fields Required",Toast.LENGTH_LONG).show();

        }

        if(CameraBitmap == null && GalleryUri == null)
        {
            Toast.makeText(this, "Select an Image from either gallery or click from camera", Toast.LENGTH_SHORT).show();
            progressDialog.hide();
        }
        else
        {
            fooditem_ref.child(category_name).child("food item").child(product_name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        Toast.makeText(AddProduct.this, product_name+"Already Exists!!", Toast.LENGTH_SHORT).show();
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
                                            FoodItemDetails item_obj = new FoodItemDetails(product_name,product_desc,product_type,product_price,downloadpath);
                                            fooditem_ref.child(category_name).child("food item").child(product_name).setValue(item_obj);
                                            progressDialog.dismiss();
                                            Toast.makeText(AddProduct.this, "Food Item Added", Toast.LENGTH_SHORT).show();



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

                                            FoodItemDetails item_obj = new FoodItemDetails(product_name,product_desc,product_type,product_price,downloadpath);

                                            fooditem_ref.child(category_name).child("food item").child(product_name).setValue(item_obj);
                                            progressDialog.dismiss();

                                            Toast.makeText(AddProduct.this, "Food Item Added", Toast.LENGTH_SHORT).show();

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



    // Inner class
    class myadapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return al.size();
        }

        @Override
        public Object getItem(int i)
        {
            return al.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i*100;
        }

        @Override
        public View getView(int i, View customView, ViewGroup parent)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            customView = inflater.inflate(R.layout.single_row_of_spinner,parent,false);

            CategoryDetails obj = al.get(i);

            TextView tv111 = customView.findViewById(R.id.tv111);
            ImageView imv111 = customView.findViewById(R.id.imv111);

            tv111.setText(obj.getCategory_name());
            Picasso.get().load(obj.getPhoto()).resize(200,200).into(imv111);


            return  customView;

        }
    }
}