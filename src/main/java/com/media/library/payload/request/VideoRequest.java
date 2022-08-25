package com.media.library.payload.request;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class VideoRequest implements Serializable {

    @NotBlank
    private String title;
    @NotEmpty
    private String producer;
    private String season;
    private String episode;
    @NotNull
    private String year;
    private String resolution;
    @NotNull
    private String description;
    @NotNull
    private String duration;

    public VideoRequest(String title, String producer, String season, String episode, String year, String resolution, String description, String duration) {
        this.title = title;
        this.producer = producer;
        this.season = season;
        this.episode = episode;
        this.year = year;
        this.resolution = resolution;
        this.description = description;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }


    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
