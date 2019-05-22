package com.heros;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import MyAPI.MyAPI;
import model.ImageResponse;
import model.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

public class Register extends AppCompatActivity {
    private static final String BASE_URL="http://10.0.2.2:3000/";
    private EditText etName,etDescription;
    private Button btnUpload,btnShowDetails;
    private ImageView imgProfile;
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
        imgProfile = findViewById(R.id.imageProfile);

        btnShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,MainActivity.class);
                startActivity(intent);
            }
        });


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }

            private  void RegisterUser(){
                String image = ("imageName");
                String id = (" ");
                String name = etName.getText().toString();
                String desc = etDescription.getText().toString();


                Map<String,String> map = new HashMap<>();
                map.put("name",name);
                map.put("desc",desc);
                map.put("image", imageName);


//                User user = new User(image,id,name,desc);
//
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(BASE_URL)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();

                 MyAPI myAPI = url.Url.getInstance().create(MyAPI.class);
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
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (data == null){
                Toast.makeText(this, "please select an image", Toast.LENGTH_SHORT).show();
            }
        }

        Uri uri = data.getData();
        imagePath = getRealpathFromUri(uri);
        previewImage(imagePath);
    }

    private void previewImage(String imagePath) {
        File imgFile = new File(imagePath);
        if (imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgProfile.setImageBitmap(myBitmap);
        }
    }

    private  String getRealpathFromUri( Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection, null, null,null);

        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }

        private void StrictMode()
    {
        android.os.StrictMode.ThreadPolicy policy =
                new android.os.StrictMode.ThreadPolicy.Builder().permitAll().build();
        android.os.StrictMode.setThreadPolicy(policy);
    }

    private  void  SaveImageOnly(){
        File file = new File(imagePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", file.getName(), requestBody);


        MyAPI myAPI = url.Url.getInstance().create(MyAPI.class);
        Call<ImageResponse> responseCall = myAPI.uploadImage(body);

        StrictMode();

        try {
            Response<ImageResponse> imageResponseResponse = responseCall.execute();

            imageName = imageResponseResponse.body().getFilename();

        }
        catch (IOException e){
            Toast.makeText(this, "Error",Toast.LENGTH_SHORT).show();
        }

    }


//    private void loadFromUrl() throws IOException {
//        StrictMode();
//
//            String imgURl ="https://www.google.com/search?q=image&tbm=isch&source=iu&ictx=1&fir=A6JJqffgz3xzlM%253A%252CpFs_4Fcq5AgpmM%252C%252Fm%252F0jg24&vet=1&usg=AI4_-kStnHiKDbMRDo0Xa5QgWDtYbRLCkQ&sa=X&ved=2ahUKEwjhzrrV9aviAhVm73MBHRK0Ac0Q_B0wE3oECAoQBg#imgrc=A6JJqffgz3xzlM:";
//            URL url = new URL(imgURl);
//            imgImage.setImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));
//
//    }
}
