package com.heros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import MyAPI.MyAPI;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView tvData;
    private Button btnAddUser;

    private static final String BASE_URL="http://10.0.2.2:3000/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvData = findViewById(R.id.tvData);
        btnAddUser = findViewById(R.id.btnAddUser);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyAPI myAPI = retrofit.create(MyAPI.class);

        Call<List<User>> listCall = myAPI.getUser();

        listCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()){
                    tvData.setText("Code: " + response.code());
                    return;
                }

                List<User> userList = response.body();
                for (User user : userList){
                    String content = "";
                    content += "ID : " + user.getId() + "\n";
                    content += "Image : " + user.getImage() + "\n";
                    content += "Name : " + user.getName() + "\n";
                    content += "Description : " + user.getDesc() + "\n";

                    tvData.append(content);
                }
            }


            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
            tvData.setText("Error" + t.getLocalizedMessage());
            }
        });
    }
}
