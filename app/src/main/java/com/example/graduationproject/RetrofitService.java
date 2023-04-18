package com.example.graduationproject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that links paths and parameters for specific functions that communicate with the server
 * */
public interface RetrofitService {
    /**
     * Login path with user data parameter
     * */
    @POST("user/login")
    Call<LoginData> login(@Body UserData userData);

    /**
     * Signup path with user data and whether the user is individual
     * */
    @POST("user/signup")
    Call<UserData> signup(@Body UserData userData, @Query("is_individual")String isIndividual);

    /**
     * Get trend of specific field
     * */
    @GET("crawling/trend/{field}")
    Call<Object> getTrend(@Path("field")String field);

    /**
     * Get roadmap based on user info and field
     * */
    @GET("user/profile/roadmap")
    Call<Object> getRoadmap(@Query("user_pk")int id, @Query("field") String field);

    /**
     * Processing completion of specific items on the roadmap
     * */
    @PUT("user/profile/roadmap")
    Call<Object> putRoadmap(@Query("user_pk")int id, @Query("field") String field, @Body Object content);

    /**
     * Get the capability information about user preferences
     * */
    @POST("user/profile/capability")
    Call<Object> getCapability(@Query("user_pk")int id, @Body Object fields);

    /**
     * Receive user experience and preference/non-preference data
     * */
    @GET("user/profile/mypage")
    Call<Object> getMyPageInfo(@Query("user_pk")int id);

    /**
     * Send the user's personal survey data
     * */
    @POST("user/profile/roadmap")
    Call<Object> postPersonalSurvey(@Query("user_pk")int id, @Body Object data);

    /**
     * Determining the field of user (last step of signup)
     * */
    @PUT("user/profile/roadmap")
    Call<Object> postPersonalField(@Query("user_pk")int id, @Query("field")String field, @Body Object fieldBody);

    /**
     * Get links to references to specific field of technology
     * */
    @GET("user/profile/roadmap/url")
    Call<Object> getReferenceLink(@Query("name")String name, @Query("field") String field);

    /**
     * Send company requirements (Business signup)
     * */
    @POST("user/company/survey")
    Call<Object> postCompanySurvey(@Body Object content);

    /**
     * Get a list of talented people that will be recommended to business user
     * */
    @GET("user/company/recommendation")
    Call<Object> getRecommendedUser(@Query("company_id")int id);
}
