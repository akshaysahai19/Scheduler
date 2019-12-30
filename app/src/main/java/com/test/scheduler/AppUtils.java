package com.test.scheduler;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AppUtils {

    public static boolean isLandscape(Context context){
        if(context.getResources().getDisplayMetrics().widthPixels>context.getResources().getDisplayMetrics().
                heightPixels){
            return true;
        }else {
            return false;
        }
    }

    public static String getWeekday(Calendar calendar){
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        return dayFormat.format(calendar.getTime());
    }


}
