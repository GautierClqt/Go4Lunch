package com.cliquet.gautier.go4lunch;

import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;
import com.cliquet.gautier.go4lunch.Models.Hours;
import com.google.api.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 *
 */

public class HoursUnitTest {
    @Test
    public void testCorrectHoursAreDisplayedInListFragment() {

        Hours hours = new Hours();
        List<DetailsPojo.Periods> periodsList = new ArrayList<>();

        for(int i = 0; i <= 6; i++) {
            DetailsPojo.Periods periods = new DetailsPojo.Periods();
            DetailsPojo.Open open = new DetailsPojo.Open();
            DetailsPojo.Close close = new DetailsPojo.Close();

            open.setDay(i+1);
            close.setDay(i+1);
            //open.setTime(String.valueOf(900+i*100));
            open.setTime("1800");
            //close.setTime(String.valueOf(1200+i*100));
            close.setTime("1900");
            periods.setOpen(open);
            periods.setClose(close);
            periodsList.add(periods);
        }

        String openHoursTest = hours.getOpeningHours(false, periodsList);
        assertEquals("01:00 - 24:00", openHoursTest);
    }
}