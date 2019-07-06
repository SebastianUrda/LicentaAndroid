package com.example.sebi.licentatest.services;
import com.example.sebi.licentatest.entities.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserService {
    @POST("user")
    @Headers("Content-type:application/json")
    Call<User> authenticate(@Body User auth);

    @POST("register")
    @Headers("Content-type:application/json")
    Call<User> register(@Body User reg);

}
