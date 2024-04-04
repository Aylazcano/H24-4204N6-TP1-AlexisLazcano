package com.example.tp1clientandroid.http;

import org.kickmyb.transfer.AddTaskRequest;
import org.kickmyb.transfer.HomeItemResponse;
import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;
import org.kickmyb.transfer.TaskDetailResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Service {

    @POST("api/id/signin")
    Call<SigninResponse> signin(@Body SigninRequest signinRequest);

    @POST("api/id/signup")
    Call<SigninResponse> signup(@Body SignupRequest signupRequest);

    @POST("api/id/signout")
    Call<String> signout();

    @POST("api/add")
    Call<String> addOne(@Body AddTaskRequest addTaskRequest);

    @GET("api/progress/{taskID}/{value}")
    Call<String> updateProgress(@Path("taskID") long taskID, @Path("value") int value);

    @GET("api/home")
    Call<List<HomeItemResponse>> home();

    @GET("api//detail/{id}")
    Call<TaskDetailResponse> detail(@Path("id") long id );
}
