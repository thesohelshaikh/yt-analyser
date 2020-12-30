package com.thesohelshaikh.ytanalyser;

public class VideoModel {
    String id;
    String duration;

    public VideoModel(String id, String duration) {
        this.id = id;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "VideoModel{" +
                "id='" + id + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
