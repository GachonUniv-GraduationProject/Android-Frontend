package com.example.graduationproject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {
    @GET("user")
    Call<String> getData();
}
