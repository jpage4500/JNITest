package com.github.jpage4500.jnitest.models;

public class MovieDetail {
    private String name;
    private float score;
    private Actor[] actors;
    private String description;

    public MovieDetail() {
    }

    public MovieDetail(String name, float score, Actor[] actors, String description) {
        this.name = name;
        this.score = score;
        this.actors = actors;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Actor[] getActors() {
        return actors;
    }

    public void setActors(Actor[] actors) {
        this.actors = actors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
