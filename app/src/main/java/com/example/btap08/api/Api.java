package com.example.btap08.api;

import com.example.btap08.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("everything")
    Call<NewsResponse> getNews(
            @Query("q") String q,
            @Query("apiKey") String apiKey
    );

}