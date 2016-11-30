package com.example.andy.api_model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * POJO representing a user from the REST API
 * @author Andrew Jarombek
 * @since 11/8/2016
 */

public class User {

    private String username;
    private String first;
    private String last;
    private String salt;
    private String password;
    private String profilepic;
    private String profilepic_name;
    private String description;
    private Date member_since;
    private int class_year;
    private String location;
    private String favorite_event;
    private Map<String, String> groups;
    private Map<String, Double> statistics;

    @Override
    public String toString() {
        return "User: [ username: " + username + ", first: " + first + ", last: " + last +
                ", salt: " + salt + ", password: " + password + ", profilepic: " + profilepic +
                ", profilepic_name: " + profilepic_name + ", description: " + description +
                ", member_since: " + member_since.toString() + ", class_year: " + class_year +
                ", location: " + location + ", favorite_event: " + favorite_event + ", groups: [" +
                groups.toString() + "], statistics: [" + statistics.toString() + "]]";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getProfilepic_name() {
        return profilepic_name;
    }

    public void setProfilepic_name(String profilepic_name) {
        this.profilepic_name = profilepic_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getMember_since() {
        return member_since;
    }

    public void setMember_since(Date member_since) {
        this.member_since = member_since;
    }

    public int getClass_year() {
        return class_year;
    }

    public void setClass_year(int class_year) {
        this.class_year = class_year;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFavorite_event() {
        return favorite_event;
    }

    public void setFavorite_event(String favorite_event) {
        this.favorite_event = favorite_event;
    }

    public Map<String, String> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, String> groups) {
        this.groups = groups;
    }

    public Map<String, Double> getStatistics() {
        return statistics;
    }

    public void setStatistics(Map<String, Double> statistics) {
        this.statistics = statistics;
    }
}
