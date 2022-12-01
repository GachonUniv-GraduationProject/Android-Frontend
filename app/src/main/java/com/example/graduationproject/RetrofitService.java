package com.example.graduationproject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @POST("user/login")
    Call<LoginData> login(@Body UserData userData);

    @POST("user/signup")
    Call<UserData> signup(@Body UserData userData, @Query("is_individual")String isIndividual);

    @PUT("user/{id}/update")
    Call<ProfileData> profile(@Path("id") int id, @Body ProfileData profileData);

    @GET("crawling/trend/{field}")
    Call<Object> getTrend(@Path("field")String field);
}
