package com.example.andy.saints_xctf_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.andy.api_model.Group;
import com.example.andy.api_model.User;

import java.util.Map;

/**
 * Pager to swipe through the tabs on the group page
 * @author Andrew Jarombek
 * @since 7/18/2017 -
 */

public class GroupPager extends FragmentStatePagerAdapter {

    private int tabCount;
    private Group group;

    public GroupPager(FragmentManager fm, int tabCount, Group group) {
        super(fm);
        this.tabCount = tabCount;
        this.group = group;
    }

    @Override
    public Fragment getItem(int position) {
        // Returning the current tabs
        Bundle data = new Bundle();
        switch (position) {
            case 0:
                data.putString("groupname", String.valueOf(group.getGroup_name()));
                LogsTab logsTab = new LogsTab();
                logsTab.setArguments(data);
                return logsTab;
            case 1:
                LeaderboardTab leaderboardTab = new LeaderboardTab();
                leaderboardTab.setArguments(data);
                return leaderboardTab;
            case 2:
                MessagesTab messagesTab = new MessagesTab();
                messagesTab.setArguments(data);
                return messagesTab;
            case 3:
                MembersTab membersTab = new MembersTab();
                membersTab.setArguments(data);
                return membersTab;
            case 4:
                AdminTab adminTab = new AdminTab();
                adminTab.setArguments(data);
                return adminTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

