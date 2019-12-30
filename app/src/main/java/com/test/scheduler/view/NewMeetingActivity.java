package com.test.scheduler.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.test.scheduler.R;
import com.test.scheduler.databinding.NewMeetingLayoutBinding;
import com.test.scheduler.model.Schedule;
import com.test.scheduler.viewmodel.ScheduleViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewMeetingActivity extends AppCompatActivity {

    NewMeetingLayoutBinding newMeetingLayoutBinding;
    private String date = "";
    private List<Schedule> schedules;
    ScheduleViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newMeetingLayoutBinding = DataBindingUtil.setContentView(this, R.layout.new_meeting_layout);
        date = getIntent().getStringExtra("date");
        newMeetingLayoutBinding.date.setText(date);
        schedules = new ArrayList<>();
        model = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        loadData(date);
        onClicks();

    }

    private void onClicks() {


        newMeetingLayoutBinding.dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(NewMeetingActivity.this,
                        R.style.CustomDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String formattedDate = getRequiredDateFormat(calendar.getTime());
                        newMeetingLayoutBinding.date.setText(formattedDate);
                        loadData(formattedDate);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        newMeetingLayoutBinding.startTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                final String[] amPm = {"AM"};

                TimePickerDialog timePickerDialog = new TimePickerDialog(NewMeetingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                if (hourOfDay >= 12) {
                                    amPm[0] = "PM";
                                } else {
                                    amPm[0] = "AM";
                                }
                                newMeetingLayoutBinding.startTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + " " + amPm[0]);

                            }
                        }, currentHour, currentMinute, false);

                timePickerDialog.show();

            }
        });

        newMeetingLayoutBinding.endTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                final String[] amPm = {"AM"};

                TimePickerDialog timePickerDialog = new TimePickerDialog(NewMeetingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                if (hourOfDay >= 12) {
                                    amPm[0] = "PM";
                                } else {
                                    amPm[0] = "AM";
                                }
                                newMeetingLayoutBinding.endTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + " " + amPm[0]);
                            }
                        }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        newMeetingLayoutBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        newMeetingLayoutBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newMeetingLayoutBinding.startTime.getText().toString().trim().length() > 0 &&
                        newMeetingLayoutBinding.endTime.getText().toString().trim().length() > 0) {
                    try {
                        if (checkSlot()) {
                            Toast.makeText(NewMeetingActivity.this, "Slot available", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NewMeetingActivity.this, "Slot not available", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(NewMeetingActivity.this, "Select start and end time", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public static String getRequiredDateFormat(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return simpleDateFormat.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean checkSlot() throws ParseException {
        int invalid = 0;
        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            long startTime = getSelectedTimestamp(getFormattedTime(schedule.getStart_time()));
            long endTime = getSelectedTimestamp(getFormattedTime(schedule.getEnd_time()));
            long selectedStartTime = getSelectedTimestamp(newMeetingLayoutBinding.startTime.getText().toString()) - 43200000;
            long selectedEndTime = getSelectedTimestamp(newMeetingLayoutBinding.endTime.getText().toString()) - 43200000;

            if ((selectedStartTime >= startTime && selectedStartTime <= endTime) ||
                    (selectedEndTime >= startTime && selectedEndTime <= endTime) ||
                    (selectedEndTime < selectedStartTime)) {
                invalid += 1;
            }
        }
        return invalid == 0;

    }

    public void loadData(String date) {
        model.getSchedules(date).observe(this, new Observer<List<Schedule>>() {
            @Override
            public void onChanged(@Nullable List<Schedule> scheduleList) {
                schedules = scheduleList;
            }
        });
    }

    private String getFormattedTime(String input) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        Date dt = sdf.parse(input);

        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        String formatedTime = sdfs.format(dt);
        return formatedTime;
    }

    private long getSelectedTimestamp(String timeToParse) throws ParseException {
        String toParse = date + " " + timeToParse; // Results in "2-5-2012 20:43"
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a"); // I assume d-M, you may refer to M-d for month-day instead.
        Date date = formatter.parse(toParse); // You will need try/catch around this
        return date.getTime();
    }
}
