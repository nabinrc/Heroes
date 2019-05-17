package com.heros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import MyAPI.MyAPI;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {
    private static final String BASE_URL="http://10.0.2.2:3000/";
    private EditText etName,etDescription;
    private Button btnUpload,btnShowDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        btnUpload = findViewById(R.id.btnUpload);
        btnShowDetails = findViewById(R.id.btnShowDetails);

        btnShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }

            private  void RegisterUser(){
                String image = (" ");
                String id = (" ");
                String name = etName.getText().toString();
                String desc = etDescription.getText().toString();

                User user = new User(image,id,name,desc);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                 MyAPI myAPI = retrofit.create(MyAPI.class);

                Call<Void> voidCall = myAPI.registerUser(user);

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
}
