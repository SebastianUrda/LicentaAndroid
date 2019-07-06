package com.example.sebi.licentatest.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sebi.licentatest.R;
import com.example.sebi.licentatest.entities.User;
import com.example.sebi.licentatest.services.UserService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText confirmPassword;
    EditText email;
    Button register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        username = findViewById(R.id.username);
        password = findViewById(R.id.passwordIn);
        confirmPassword = findViewById(R.id.confirmPasswordIn);
        email = findViewById(R.id.emailIn);
        register = findViewById(R.id.registerButton);

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("") || password.getText().toString().equals("")
                        || confirmPassword.getText().toString().equals("") || email.getText().toString().equals("")) {
                    Toast.makeText(Register.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                        Toast.makeText(Register.this, "Passwords did not match", Toast.LENGTH_SHORT).show();
                    } else {
                        User toRegister = new User(username.getText().toString(), password.getText().toString(), email.getText().toString());
                        Log.d("Register", toRegister.toString());
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(getString(R.string.server_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        UserService userService = retrofit.create(UserService.class);
                        userService.register(toRegister).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    User user = response.body();
                                    if (user != null) {
                                        Log.d("AUTH", user.toString());
                                        SharedPreferences.Editor e = getSharedPreferences("Auth", MODE_PRIVATE).edit();
                                        e.putString("userId", String.valueOf(user.getId())).apply();
                                        e.putString("deviceId", String.valueOf(user.getDeviceID())).apply();
                                        e.putString("userName", user.getUsername()).apply();
                                        e.putString("userRole", user.getRole()).apply();
                                        e.putString("loggedIn", "true").apply();
                                        if (user.getId() != 0) {
                                            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(myIntent);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Registration Failed. Please try again!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                } else {
                                    try {
                                        Log.d("AUTH", "FAIL" + response.message() + " " + response.body() + " " + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Registration Failed. Please try again!", Toast.LENGTH_SHORT).show();
                                Log.d("AUTH", t.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }
}
