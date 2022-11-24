package com.example.graduationproject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitService {
    @POST("user/login")
    Call<LoginData> login(@Body UserData userData);

    @POST("user/signup")
    Call<LoginData> signup(@Body UserData userData);
}
