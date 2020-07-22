package com.example.retrofit2;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public
interface APInterface {

    @GET("/v2/top-headlines")
    Call<Result> getNews(@Query("sources")String sourceValue, @Query("apiKey") String apiKey);

   @GET("/v2/everything")
   Call<Result> getNews(@QueryMap Map<String, Object> options);

   // @GET("/v2/everything")
    //Call<Result> getNews(@Query("sources")String sourceValue, @Query("apiKey") String apiKey);

}
