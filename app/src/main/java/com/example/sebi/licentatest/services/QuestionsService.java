package com.example.sebi.licentatest.services;


import com.example.sebi.licentatest.list.QuestionA;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface QuestionsService {

    @GET("question")
    @Headers("Content-type:application/json")
    Call<List<QuestionA>> getQuestions(@Query("type") String type);
}
