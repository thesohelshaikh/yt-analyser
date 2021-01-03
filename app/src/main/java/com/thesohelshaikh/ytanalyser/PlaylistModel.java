package com.thesohelshaikh.ytanalyser;

import java.util.ArrayList;

public class PlaylistModel {
    private String id;
    private long totalDuration;
    private String title;
    private String thumbnailURL;
    private String createdBy;
    private int numberOfVideos;
    private ArrayList<String> videoIDs;

    public PlaylistModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }

    public void addDuration(long duration) {
        setTotalDuration(getTotalDuration() + duration);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getNumberOfVideos() {
        return numberOfVideos;
    }

    public void setNumberOfVideos(int numberOfVideos) {
        this.numberOfVideos = numberOfVideos;
    }

    public ArrayList<String> getVideoIDs() {
        return videoIDs;
    }

    public void setVideoIDs(ArrayList<String> videoIDs) {
        this.videoIDs = videoIDs;
    }

    @Override
    public String toString() {
        return "PlaylistModel{" +
                "id='" + id + '\'' +
                ", totalDuration='" + totalDuration + '\'' +
                ", title='" + title + '\'' +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", numberOfVideos=" + numberOfVideos +
                ", videoIDs=" + videoIDs +
                '}';
    }
}
