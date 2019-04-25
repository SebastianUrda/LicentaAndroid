package com.example.sebi.licentatest.services;

import com.example.sebi.licentatest.list.Answer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AnswerService {
    @POST("answers")
    @Headers("Content-type:application/json")
    Call<Void> sendAnswers(@Body List<Answer> toSend);
}
