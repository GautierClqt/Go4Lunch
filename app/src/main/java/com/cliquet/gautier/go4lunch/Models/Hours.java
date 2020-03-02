package com.cliquet.gautier.go4lunch.Models;

import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;

import java.util.Calendar;
import java.util.List;

public class Hours {

    private Calendar calendar = Calendar.getInstance();
    private int calendarDay = calendar.get(Calendar.DAY_OF_WEEK);
    private int calendarHours = calendar.get(Calendar.HOUR_OF_DAY);
    private String openTime;
    private String closeTime;
    private String hoursString;

    public String getOpeningHours(boolean openNow, List<DetailsPojo.Periods> periods) {
        String openHours = "closed";

        if(openNow) {
            for (int i = 0; i <= periods.size()-1; i++) {
                if (periods.get(i).getOpen().getDay() == calendarDay) {
                    openTime = periods.get(calendarDay - 1).getOpen().getTime();
                    openTime = "" + openTime.charAt(0) + openTime.charAt(1);
                    closeTime = periods.get(calendarDay - 1).getClose().getTime();
                    closeTime = "" + closeTime.charAt(0) + closeTime.charAt(1);

                    openHours = "" + openTime + ":00 - " + closeTime + ":00";
                    openHours = checkForClosingSoon(openHours);
                }
            }
        }
        else {
            for(int i = 0; i <= periods.size()-1; i++) {
                openTime = periods.get(calendarDay - 1).getOpen().getTime();
                openTime = "" + openTime.charAt(0) + openTime.charAt(1);
            }
            openHours = checkForOpeningSoon();
        }

        return openHours;
    }

    private String checkForOpeningSoon() {
        int comparedHours = Integer.parseInt(openTime)-calendarHours;

        if(comparedHours == 1) {
            hoursString = "opening soon";
        }
        else {
            hoursString = "closed";
        }

        return hoursString;
    }

    private String checkForClosingSoon(String openHours) {
        int comparedHours = Integer.parseInt(closeTime)-calendarHours;

        if(comparedHours == 1) {
            hoursString = "closing soon";
            return hoursString;
        }
        return openHours;
    }
}
