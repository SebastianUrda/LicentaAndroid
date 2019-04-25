package com.example.sebi.licentatest.services;

import com.example.sebi.licentatest.data.Data;
import com.example.sebi.licentatest.data.DataList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface DataService {
    @GET("data")
    @Headers("Content-type:application/json")
    Call<DataList> messages();

    @POST("data")
    @Headers("Content-type:application/json")
    Call<Void> send(@Body Data model);

}
