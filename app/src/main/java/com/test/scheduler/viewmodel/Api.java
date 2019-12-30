package com.test.scheduler.viewmodel;

import com.test.scheduler.model.Schedule;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "http://fathomless-shelf-5846.herokuapp.com/api/";

    @GET("schedule")
    Call<List<Schedule>> getSchedules(@Query("date") String date);

}
