package com.example.andy.api_model;

import java.util.List;
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
    private List<String> groupmembers;
    private Map<String, Double> statistics;

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

    public List<String> getGroupmembers() {
        return groupmembers;
    }

    public void setGroupmembers(List<String> groupmembers) {
        this.groupmembers = groupmembers;
    }

    public Map<String, Double> getStatistics() {
        return statistics;
    }

    public void setStatistics(Map<String, Double> statistics) {
        this.statistics = statistics;
    }
}
