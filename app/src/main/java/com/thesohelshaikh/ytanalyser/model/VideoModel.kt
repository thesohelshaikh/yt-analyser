package com.thesohelshaikh.ytanalyser.model;

public class VideoModel {
    String id;
    String duration;
    String title;
    String thumbnailURL;
    String channelTitle;

    public VideoModel() {
    }

    public VideoModel(String id, String duration, String title, String thumbnailURL, String channelTitle) {
        this.id = id;
        this.duration = duration;
        this.title = title;
        this.thumbnailURL = thumbnailURL;
        this.channelTitle = channelTitle;
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

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
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
