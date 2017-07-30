package com.example.andy.saints_xctf_android;

import android.util.Log;

import java.security.SecureRandom;

/**
 * Utility functions for the front end of the saintsxctf android app
 * @author Andrew Jarombek
 * @since 1/26/2017 -
 */

public class ControllerUtils {

    static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * Convert from other distance metrics to miles
     * @param distance -
     * @param metric -
     * @return the mileage
     */
    public static Double convertToMiles(Double distance, String metric) {
        switch (metric) {
            case "miles":
                return distance;
            case "meters":
                return (distance / 1609.344);
            case "kilometers":
                return (distance * 0.621317);
            default:
                return distance;
        }
    }

    /**
     * Calculate the average time it takes to complete each mile
     * @param distance in miles
     * @param minutes -
     * @param seconds -
     * @return the average mile pace
     */
    public static String milePace(Double distance, String minutes, String seconds) {
        if (minutes.equals("")) {
            if (seconds.equals(""))
                return "00:00:00";
            minutes = "0";
        }

        if (seconds.equals(""))
            seconds = "0";
        
        Log.i("CU", "Minutes: " + minutes);
        Log.i("CU", "Seconds: " + seconds);
        int s = (Integer.valueOf(minutes) * 60) + Integer.valueOf(seconds);
        long secondPace = Math.round(s / distance);
        return "00:" + String.valueOf(secondPace/60) + ":" + String.valueOf(secondPace%60);
    }

    /**
     * Generate a pseudo-random code of variable length
     * @param length how long the code should be
     * @return the code
     */
    public static String generateCode(int length) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(secureRandom.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    /**
     * Get the filter string to be used for the range view API request
     * @param run is the run filter selected
     * @param bike is the bike filter selected
     * @param swim is the swim filter selected
     * @param other is the other filter selected
     * @return the filter string
     */
    public static String getFilter(boolean run, boolean bike, boolean swim, boolean other) {
        String run_fil = run ? "r" : "";
        String bike_fil = bike ? "b" : "";
        String swim_fil = swim ? "s" : "";
        String other_fil = other ? "o" : "";

        return run_fil + bike_fil + swim_fil + other_fil;
    }
}
