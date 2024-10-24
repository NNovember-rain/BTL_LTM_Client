package com.game.solve.model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String username;
    private String  gender;
    private int points;

    public User() {

    }

    public User(int id, String username, String gender, int points) {
        this.id = id;
        this.username = username;
        this.gender = gender;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    //    public int getHighScore() {
//        String[] points = this.points.split(" ");
//        int result = 0;
//        for(String point : points) {
//            if(result < Integer.parseInt(point))
//            {
//                result = Integer.parseInt(point);
//            }
//        }
//        return result;
//    }
//
//    public int getTotalPoints() {
//        String[] points = this.points.split(" ");
//        int result = 0;
//        for(String point : points) {
//            result += Integer.parseInt(point);
//        }
//        return result;
//    }

}