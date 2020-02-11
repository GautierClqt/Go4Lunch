package com.cliquet.gautier.go4lunch.Models;

import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;

import java.util.Calendar;
import java.util.List;

public class Hours {

    public String compareHours(List<DetailsPojo.Periods> periods) {
        String openHours = "closed";
        int calendarDay = Calendar.DAY_OF_WEEK;
        int calendarHours = Calendar.HOUR_OF_DAY;

        for(int i = 0; i <= periods.size(); i++) {
            if(periods.get(i).getOpen().getDay() == calendarDay) {
                String openTime = periods.get(calendarDay-1).getOpen().getTime();
                String closeTime = periods.get(calendarDay-1).getClose().getTime();
                openTime = ""+openTime.charAt(0)+openTime.charAt(1);
                closeTime = ""+closeTime.charAt(0)+closeTime.charAt(1);

                if(calendarHours >= Integer.parseInt(openTime) && calendarHours <= Integer.parseInt(closeTime)) {
                    openHours = ""+openTime+":00 - "+closeTime+":00";
                }
            }
        }

        return openHours;
    }
}
