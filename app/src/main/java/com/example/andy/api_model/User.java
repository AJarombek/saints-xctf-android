package com.example.andy.api_model;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * POJO representing a user from the REST API
 * @author Andrew Jarombek
 * @since 11/8/2016
 * @version 0.1 BETA RELEASE
 */

public class User {

    private String username;
    private String first;
    private String last;
    private String email;
    private String salt;
    private String password;
    private String profilepic;
    private String profilepic_name;
    private String description;
    private String activation_code;
    private Date member_since;
    private Integer class_year;
    private String location;
    private String favorite_event;
    private String[] forgotpassword;
    private String[] flair;
    private List<Notification> notifications;
    private List<GroupInfo> groups;
    private Map<String, Double> statistics;
    private String last_signin;
    private String week_start;

    @Override
    public String toString() {
        return "User: [ username: " + username + ", first: " + first + ", last: " + last +
                ", email: " + email + ", salt: " + salt + ", password: " + password +
                ", profilepic: " + profilepic + ", profilepic_name: " + profilepic_name +
                ", description: " + description + ", member_since: " + member_since.toString() +
                ", class_year: " + class_year + ", location: " + location + ", favorite_event: " +
                favorite_event + ", week_start: " + week_start + ", last_signin: " + last_signin +
                ", activation_code: " + activation_code + ", groups: [" + groups.toString() +
                "], forgotpassword: [" + Arrays.toString(forgotpassword) + "], flair: [" +
                Arrays.toString(flair) + "], notifications: [" + notifications.toString() +
                "], statistics: [" + statistics.toString() + "]]";
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
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(member_since);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        this.member_since = calendar.getTime();
    }

    public Integer getClass_year() {
        return class_year;
    }

    public void setClass_year(Integer class_year) {
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

    public List<GroupInfo> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupInfo> groups) {
        this.groups = groups;
    }

    public Map<String, Double> getStatistics() {
        return statistics;
    }

    public void setStatistics(Map<String, Double> statistics) {
        this.statistics = statistics;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setActivation_code(String activation_code) {
        this.activation_code = activation_code;
    }

    public void setForgotpassword(String[] forgotpassword) {
        this.forgotpassword = forgotpassword;
    }

    public String getEmail() {
        return email;
    }

    public String getActivation_code() {
        return activation_code;
    }

    public String[] getForgotpassword() {
        return forgotpassword;
    }

    public String getLast_signin() {
        return last_signin;
    }

    public void setLast_signin(String last_signin) {
        this.last_signin = last_signin;
    }

    public String getWeek_start() {
        return week_start;
    }

    public void setWeek_start(String week_start) {
        this.week_start = week_start;
    }

    public String[] getFlair() {
        return flair;
    }

    public void setFlair(String[] flair) {
        this.flair = flair;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
