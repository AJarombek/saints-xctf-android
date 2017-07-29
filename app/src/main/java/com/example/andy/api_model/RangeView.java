package com.example.andy.api_model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * POJO representing a range view from the REST API
 * @author Andrew Jarombek
 * @since 6/10/2017
 */

public class RangeView {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;
    private double miles;
    private int feel;

    @Override
    public String toString() {
        return "RangeView: [ date: " + date  + ", miles: " + miles + ", feel" + feel + "]";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getMiles() {
        return miles;
    }

    public void setMiles(double miles) {
        this.miles = miles;
    }

    public int getFeel() {
        return feel;
    }

    public void setFeel(int feel) {
        this.feel = feel;
    }
}


