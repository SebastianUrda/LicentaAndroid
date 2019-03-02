package com.example.sebi.licentatest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface DataService {
    @GET("/data")
    @Headers("Content-type:application/json")
    Call<DataList> messages();

    @POST("/data")
    @Headers("Content-type:application/json")
    Call<Void> send(@Body Data model);
}
