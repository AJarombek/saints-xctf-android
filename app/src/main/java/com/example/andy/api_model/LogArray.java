package com.example.andy.api_model;

import java.util.ArrayList;

/**
 * POJO representing an array of logs from the REST API
 * @author Andrew Jarombek
 * @since 11/29/2016
 */

public class LogArray {

    private ArrayList<Log> logs;

    public ArrayList<Log> getLogs() {
        return logs;
    }

    public void setLogs(ArrayList<Log> logs) {
        this.logs = logs;
    }
}
