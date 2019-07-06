package com.example.sebi.licentatest.services;

import com.example.sebi.licentatest.entities.AlertA;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;


public interface AlertService {
    @GET("alerts")
    @Headers("Content-type:application/json")
    Call<List<AlertA>> getAlerts();
}
