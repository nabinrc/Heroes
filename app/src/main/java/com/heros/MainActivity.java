package com.heros;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import MyAPI.MyAPI;
import adapter.HeroesAdapter;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import url.Url;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnAddUser = findViewById(R.id.btnAddUser);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });

        recyclerView= findViewById(R.id.recyclerView);
        getAllHeroes();

    }
    private void getAllHeroes(){
        MyAPI myAPI = Url.getInstance().create(MyAPI.class);
        final Call<List<User>> listCall = myAPI.getAllHeroes();

        listCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "reponse ", Toast.LENGTH_SHORT).show();

                }
                List<User> listHero = response.body();
                HeroesAdapter heroAdapter = new HeroesAdapter(listHero,MainActivity.this);
                recyclerView.setAdapter(heroAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "DEtail error ", Toast.LENGTH_SHORT).show();

            }
        });



    }
}