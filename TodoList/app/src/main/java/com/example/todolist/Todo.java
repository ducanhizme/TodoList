package com.example.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Todo {
    private int id;
    private int idUser;
    private String name;
    private String StartTime;
    private String EndTime;
    private String date;
    private boolean type;

    public Todo(int id, int idUser, String name, String startTime, String endTime, String date) {
        this.id = id;
        this.idUser = idUser;
        this.name = name;
        StartTime = startTime;
        EndTime = endTime;
        this.date = date;
    }

    public Todo(int idUser, String name, String startTime, String endTime,boolean type) {
        this.idUser = idUser;
        this.name = name;
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.type = type;
    }

    public Todo() {
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", new Locale("vi","VN"));
        Date date = new Date();
        return format.format(date);
    }

    public String getDateObject(){
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", name='" + name + '\'' +
                ", StartTime=" + StartTime +
                ", EndTime=" + EndTime +
                ", date=" + date +
                '}';
    }
}
