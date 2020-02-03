package com.cliquet.gautier.go4lunch.Models;

import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;

import java.util.HashMap;
import java.util.List;

public class OpeningHours {

    public void formattingOpeningHours(DetailsPojo detailsPojo) {
        HashMap<Integer, List<String>> openingHoursHM = new HashMap<>();
        List<DetailsPojo.Periods> periods = detailsPojo.getResults().getOpeningHours().getPeriods();
        int size = periods.size();

        for(int i = 0; i < size ; i++) {
//            openingHoursHM.put(periods.get(0).getOpen().getDay(), periods.get(0).getOpen().getTime());
//            openingHoursHM.put(periods.get(0).getClose().getDay(), periods.get(0).getClose().getTime());
        }
    }
}
