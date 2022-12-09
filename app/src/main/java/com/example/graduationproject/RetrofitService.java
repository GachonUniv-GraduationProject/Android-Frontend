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

    @GET("user/profile/roadmap")
    Call<Object> getRoadmap(@Query("user_pk")int id, @Query("field") String field);

    @PUT("user/profile/roadmap")
    Call<Object> putRoadmap(@Query("user_pk")int id, @Query("field") String field, @Body Object content);

    @POST("user/profile/capability")
    Call<Object> getCapability(@Query("user_pk")int id, @Body Object fields);

    // 경험과 선호/비선호 데이터 받기
    @GET("user/profile/mypage")
    Call<Object> getMyPageInfo(@Query("user_pk")int id);

    // TODO: NLP_POS_NEG, NLP_FIELD 에 따라서 각각 설문 문항 POST (회원가입 설문)
    @POST("user/profile/roadmap")
    Call<Object> postPersonalSurvey(@Query("user_pk")int id, @Body Object data);

    // 분야 확정 짓기 (회원가입 마지막)
    @PUT("user/profile/roadmap")
    Call<Object> postPersonalField(@Query("user_pk")int id, @Query("field")String field, @Body Object fieldBody);

    @GET("user/profile/roadmap/url")
    Call<Object> getReferenceLink(@Query("name")String name, @Query("field") String field);

    // 회사 요구사항 입력 (기업 회원가입)
    @POST("user/company/survey")
    Call<Object> postCompanySurvey(@Body Object content);

    // 회사가 추천받을 인재 리스트 받기
    @GET("user/company/recommendation")
    Call<Object> getRecommendedUser(@Query("company_id")int id);
}
