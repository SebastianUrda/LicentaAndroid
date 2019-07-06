package com.example.sebi.licentatest.services;

import com.example.sebi.licentatest.entities.Device;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface DeviceService {

    @GET("device")
    @Headers("Content-type:application/json")
    Call<Device> registerDevice(@Query("name") String name, @Query("userId") int userId);
}
