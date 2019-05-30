package com.heros;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    private static final String BASE_URL="http://10.0.2.2:3000/";
    private EditText etName,etDescription;
    private Button btnUpload,btnShowDetails;
    private ImageView imgImage;
    String imagePath;
    String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        btnUpload = findViewById(R.id.btnUpload);
        btnShowDetails = findViewById(R.id.btnShowDetails);
        imgImage = findViewById(R.id.imgImage);
        checkPermission();
//        loadFromURL();


        btnShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,MainActivity.class);
                startActivity(intent);
            }
        });
        imgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BrowseImage();

            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveImageOnly();
                RegisterUser();
            }

            private  void RegisterUser(){
                String image = imageName;
                String id = (" ");
                String name = etName.getText().toString();
                String desc = etDescription.getText().toString();
                Map<String,String> map = new HashMap<>();
                map.put("name",name);
                map.put("desc",desc);
                map.put("image",image);



                MyAPI myAPI = Url.getInstance().create(MyAPI.class);


                Call<Void> voidCall = myAPI.addHero2(map);

                voidCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(Register.this,"Registration Successfully",Toast.LENGTH_LONG).show();
                        etName.setText(" ");
                        etDescription.setText(" ");

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(Register.this,"Error : " + t.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                    }
                });

            }
        });


    }
    private void BrowseImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (data == null){
                Toast.makeText(this,"Please Select an image",Toast.LENGTH_LONG).show();
            }
        }
        Uri uri = data.getData();//error
        imagePath = getRealPathFromUri(uri);
        previewImage(imagePath);
    }
    private String getRealPathFromUri(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(),uri,projection,null,null,null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }
    private void previewImage(String imagePath){
        File imgFile = new File(imagePath);
        if (imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgImage.setImageBitmap(myBitmap);
        }
    }

    private void checkPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }
    }



    private void StrictMode()
    {
        android.os.StrictMode.ThreadPolicy policy =
                new android.os.StrictMode.ThreadPolicy.Builder().permitAll().build();
        android.os.StrictMode.setThreadPolicy(policy);
    }
    private void SaveImageOnly(){
        File file=new File(imagePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile",file.getName(),requestBody);

        MyAPI myAPI = Url.getInstance().create(MyAPI.class);
        Call<ImageResponse> responseCall = myAPI.uploadImage(body);
        StrictMode();
        try {
            Response<ImageResponse> imageResponseResponse = responseCall.execute();
            imageName = imageResponseResponse.body().getFilename();
        } catch (IOException e) {
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

//    private void loadFromURL(){
//        StrictMode();
//        try {
//            String imgURL = "http://10.0.2.2:3000/uploads/download.png";
//            URL url = new URL(imgURL);
//            imgImage.setImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));
//        }  catch (IOException e) {
//            Toast.makeText(this,"Error" + e.toString(),Toast.LENGTH_LONG).show();
//        }
//    }
}
