package com.cliquet.gautier.go4lunch.Models;

import android.content.Context;

import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;

import java.util.Calendar;
import java.util.List;

public class Hours {

    int calendarDay = Calendar.DAY_OF_WEEK;
    int calendarHours = Calendar.HOUR_OF_DAY;
    String openTime;
    String closeTime;
    String hoursString;

    public String compareHours(List<DetailsPojo.Periods> periods) {
        String openHours = "closed";


        for(int i = 0; i <= periods.size(); i++) {
            if(periods.get(i).getOpen().getDay() == calendarDay) {
                openTime = periods.get(calendarDay-1).getOpen().getTime();
                openTime = ""+openTime.charAt(0)+openTime.charAt(1);
                closeTime = periods.get(calendarDay-1).getClose().getTime();
                closeTime = ""+closeTime.charAt(0)+closeTime.charAt(1);

                if(calendarHours >= Integer.parseInt(openTime) && calendarHours <= Integer.parseInt(closeTime)) {
                    openHours = ""+openTime+":00 - "+closeTime+":00";
                }
            }
        }

        return openHours;
    }

    private void checkForOpenSoon(Context context) {

        int comparedHours = Integer.parseInt(openTime)-calendarDay;

        if(comparedHours == 1) {
            hoursString = "opening soon";
        }
        else if(comparedHours < 0) {
            //call compareHours()
        }
        else if
    }
}
