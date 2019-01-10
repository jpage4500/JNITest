package com.github.jpage4500.jnitest.models;

public class Movie {
    private String name;
    private int lastUpdated;

    private MovieDetail detail;

    public Movie() { }

    public Movie(String name, int lastUpdated) {
        this.name = name;
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(int lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public MovieDetail getDetail() {
        return detail;
    }

    public void setDetail(MovieDetail detail) {
        this.detail = detail;
    }
}
