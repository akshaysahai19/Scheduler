package com.test.scheduler.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.test.scheduler.AppUtils;
import com.test.scheduler.R;
import com.test.scheduler.databinding.ActivityMainBinding;
import com.test.scheduler.model.Schedule;
import com.test.scheduler.viewmodel.ScheduleViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    SchedulerAdapter schedulerAdapter;
    ScheduleViewModel model;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.schedulesRecyclerview.setHasFixedSize(true);
        binding.schedulesRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        initializeCal();

        model = ViewModelProviders.of(this).get(ScheduleViewModel.class);

        getCurrentDate();

        onClicks();

    }

    private void onClicks() {
        binding.prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrevDate();
            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNextDate();
            }
        });

        binding.newMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewMeetingActivity.class);
                intent.putExtra("date", binding.dateTitle.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void initializeCal() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public void getCurrentDate() {
        Date c = calendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = simpleDateFormat.format(c);
        updateTitle(formattedDate);
        loadData(formattedDate);
    }

    public void getPrevDate() {
        calendar.add(Calendar.DATE, -1);
        Date nextDate = calendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = simpleDateFormat.format(nextDate);
        updateTitle(formattedDate);
        loadData(formattedDate);
    }

    public void getNextDate() {
        calendar.add(Calendar.DATE, 1);
        Date nextDate = calendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = simpleDateFormat.format(nextDate);
        updateTitle(formattedDate);
        loadData(formattedDate);
    }


    private void loadData(String date) {
        model.getSchedules(date).observe(this, new Observer<List<Schedule>>() {
            @Override
            public void onChanged(@Nullable List<Schedule> scheduleList) {
                schedulerAdapter = new SchedulerAdapter(MainActivity.this, scheduleList);
                binding.schedulesRecyclerview.setAdapter(schedulerAdapter);
            }
        });
    }

    private void updateTitle(String date) {
        if (AppUtils.isLandscape(this)) {
            binding.dateTitle.setText(AppUtils.getWeekday(calendar) + ", " + date);
        } else {
            binding.dateTitle.setText(date);

        }
    }


}
