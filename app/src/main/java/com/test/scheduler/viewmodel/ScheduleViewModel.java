package com.test.scheduler.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.test.scheduler.model.Schedule;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScheduleViewModel extends ViewModel {

    //this is the data that we will fetch asynchronously
    private MutableLiveData<List<Schedule>> scheduleList;

    //we will call this method to get the data
    public LiveData<List<Schedule>> getSchedules(String date) {
        //if the list is null
        scheduleList = new MutableLiveData<List<Schedule>>();
        //we will load it asynchronously from server in this method
        loadSchedules(date);

        //finally we will return the list
        return scheduleList;
    }


    //This method is using Retrofit to get the JSON data from URL
    private void loadSchedules(String date) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<List<Schedule>> call = api.getSchedules(date);


        call.enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {

                System.out.println("Call: " + call);
                System.out.println("Response: " + response);
                //finally we are setting the list to our MutableLiveData
                scheduleList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                System.out.println("Throw: " + t.getLocalizedMessage());
            }
        });
    }

}
