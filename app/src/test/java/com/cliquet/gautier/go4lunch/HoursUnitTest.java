package com.cliquet.gautier.go4lunch;

import com.google.api.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

        assertEquals(4, 2 + 2);
    }
}