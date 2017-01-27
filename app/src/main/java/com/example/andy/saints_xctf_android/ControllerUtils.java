package com.example.andy.saints_xctf_android;

/**
 * Utility functions for the front end of the saintsxctf android app
 * @author Andrew Jarombek
 * @since 1/26/2017 -
 */

public class ControllerUtils {

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

    public static String milePace(Double distance, String minutes, String seconds) {
        if (minutes.equals(""))
            if (seconds.equals(""))
                return "0:00";
            minutes = "0";

        if (seconds.equals(""))
            seconds = "0";

        int s = (Integer.valueOf(minutes) * 60) + Integer.valueOf(seconds);
        long secondPace = Math.round(s / distance);
        return String.valueOf(secondPace/60) + ":" + String.valueOf(secondPace%60);
    }
}