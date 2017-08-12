package com.jarombek.andy.api_model.pojos;

import java.util.ArrayList;
import java.util.Map;

/**
 * POJO representing a group from the REST API
 * @author Andrew Jarombek
 * @since 11/8/2016
 */

public class Group {

    private String group_name;
    private String group_title;
    private String grouppic;
    private String grouppic_name;
    private String description;
    private String week_start;
    private ArrayList<GroupMember> members;
    private Map<String, Double> statistics;
    private Map<String, ArrayList<LeaderboardItem>> leaderboards;

    @Override
    public String toString() {
        return "Group: [ group_name: " + group_name + ", group_title: " + group_title +
                ", grouppic: " + grouppic + ", grouppic_name: " + grouppic_name +
                ", description: " + description + ", week_start: " + week_start + ", members: [" +
                members.toString() + "], statistics: " + statistics + "], leaderboards: " +
                leaderboards + "]]";
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_title() {
        return group_title;
    }

    public void setGroup_title(String group_title) {
        this.group_title = group_title;
    }

    public String getGrouppic() {
        return grouppic;
    }

    public void setGrouppic(String grouppic) {
        this.grouppic = grouppic;
    }

    public String getGrouppic_name() {
        return grouppic_name;
    }

    public void setGrouppic_name(String grouppic_name) {
        this.grouppic_name = grouppic_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<GroupMember> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<GroupMember> members) {
        this.members = members;
    }

    public Map<String, Double> getStatistics() {
        return statistics;
    }

    public void setStatistics(Map<String, Double> statistics) {
        this.statistics = statistics;
    }

    public Map<String, ArrayList<LeaderboardItem>> getLeaderboards() {
        return leaderboards;
    }

    public void setLeaderboards(Map<String, ArrayList<LeaderboardItem>> leaderboards) {
        this.leaderboards = leaderboards;
    }

    public String getWeek_start() {
        return week_start;
    }

    public void setWeek_start(String week_start) {
        this.week_start = week_start;
    }
}
