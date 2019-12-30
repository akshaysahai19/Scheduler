package com.test.scheduler.view;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.test.scheduler.AppUtils;
import com.test.scheduler.R;
import com.test.scheduler.databinding.ScheduleRecyclerviewLayoutBinding;
import com.test.scheduler.model.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SchedulerAdapter extends RecyclerView.Adapter<SchedulerAdapter.MyViewHolder> {

    private List<Schedule> scheduleList;
    private Context context;

    public SchedulerAdapter(Context context,List<Schedule> scheduleList) {
        this.context=context;
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (AppUtils.isLandscape(context)){
            ScheduleRecyclerviewLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                    R.layout.schedule_recyclerview_layout, parent, false);
            return new MyViewHolder(binding);
        }else {
            ScheduleRecyclerviewLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                    R.layout.schedule_recyclerview_layout, parent, false);
            return new MyViewHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Schedule schedule = scheduleList.get(position);
        if(AppUtils.isLandscape(context)){

            holder.binding.description.setText(schedule.getDescription());
            try {
                holder.binding.startTime.setText(getFormattedTime(schedule.getStart_time()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                holder.binding.endTime.setText(getFormattedTime(schedule.getEnd_time()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.binding.participants.setText(schedule.getParticipants().toString()
                    .replace(", ", ",").replaceAll("[\\[.\\]]", ""));
        }else {
            try {
                holder.binding.time.setText(getFormattedTime(schedule.getStart_time()) + " - " + getFormattedTime(schedule.getEnd_time()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.binding.description.setText(schedule.getDescription());
        }

    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        ScheduleRecyclerviewLayoutBinding binding;

        public MyViewHolder(@NonNull ScheduleRecyclerviewLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    private String getFormattedTime(String input) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        Date dt = sdf.parse(input);

        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        String formatedTime = sdfs.format(dt);
        return formatedTime;
    }
}
