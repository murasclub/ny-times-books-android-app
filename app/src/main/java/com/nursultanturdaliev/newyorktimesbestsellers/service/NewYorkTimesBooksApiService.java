package com.nursultanturdaliev.newyorktimesbestsellers.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewYorkTimesBooksApiService {

   public static final String apiKey  = "l3XamCznQkuXimGcXmpQRsVQVh1eT4Ty";

    @GET("/svc/books/v3/lists/names.json")
    public Call<NewYorkTimesApiResponse> fetchBestSellerListNames(@Query("api-key") String apiKey);
}
