package com.example.andy.saints_xctf_android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Pager to swipe through the tabs on the profile page
 * @author Andrew Jarombek
 * @since 1/27/2017 -
 */

public class ProfilePager extends FragmentStatePagerAdapter {

    private int tabCount;

    public ProfilePager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                return new LogsTab();
            case 1:
                return new StatisticsTab();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
