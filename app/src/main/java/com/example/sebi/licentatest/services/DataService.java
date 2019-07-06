package com.example.sebi.licentatest.services;

import com.example.sebi.licentatest.entities.Data;
import com.example.sebi.licentatest.entities.DataList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DataService {
    @GET("data")
    @Headers("Content-type:application/json")
    Call<DataList> messages();

    @POST("data")
    @Headers("Content-type:application/json")
    Call<Void> send(@Body Data model);

    @GET("observation/around")
    @Headers("Content-type:application/json")
    Call<ResponseBody> getAroundMeasurements(@Query("latitude") double latitude, @Query("longitude") double longitude, @Query("timeDistance") double timeDistance, @Query("geoDistance") double geoDistance);



}
