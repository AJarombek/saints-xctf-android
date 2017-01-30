package com.example.andy.saints_xctf_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.andy.api_model.User;

import java.util.Map;

/**
 * Pager to swipe through the tabs on the profile page
 * @author Andrew Jarombek
 * @since 1/27/2017 -
 */

public class ProfilePager extends FragmentStatePagerAdapter {

    private int tabCount;
    private User user;

    public ProfilePager(FragmentManager fm, int tabCount, User user) {
        super(fm);
        this.tabCount = tabCount;
        this.user = user;
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        Bundle data = new Bundle();
        switch (position) {
            case 0:
                data.putString("username", String.valueOf(user.getUsername()));
                LogsTab logsTab = new LogsTab();
                logsTab.setArguments(data);
                return logsTab;
            case 1:
                Map<String,Double> statistics = user.getStatistics();
                data.putString("workout_career", String.valueOf(statistics.get("miles")));
                data.putString("workout_year", String.valueOf(statistics.get("milespastyear")));
                data.putString("workout_month", String.valueOf(statistics.get("milespastmonth")));
                data.putString("workout_week", String.valueOf(statistics.get("milespastweek")));

                data.putString("running_career", String.valueOf(statistics.get("runmiles")));
                data.putString("running_year", String.valueOf(statistics.get("runmilespastyear")));
                data.putString("running_month", String.valueOf(statistics.get("runmilespastmonth")));
                data.putString("running_week", String.valueOf(statistics.get("runmilespastweek")));

                data.putString("feel_career", String.valueOf(statistics.get("alltimefeel")));
                data.putString("feel_year", String.valueOf(statistics.get("yearfeel")));
                data.putString("feel_month", String.valueOf(statistics.get("monthfeel")));
                data.putString("feel_week", String.valueOf(statistics.get("weekfeel")));

                StatisticsTab statisticsTab = new StatisticsTab();
                statisticsTab.setArguments(data);
                return statisticsTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
