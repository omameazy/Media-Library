package com.media.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@AllArgsConstructor
@Builder
@Table(name = "video_library")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "title")
    private String title;
    @NotNull
    @Column(name = "producer")
    private String producer;
    @NotNull
    @Column(name = "category")
    private String category;
    @Column(name = "season")
    private String season;
    @Column(name = "episode")
    private String episode;
    @NotNull
    @Column(name = "year")
    private String year;
    @Column(name = "resolution")
    private String resolution;
    @NotNull
    @Column(name = "size")
    private String size;
    @Column(name = "description")
    private String description;
    @NotNull
    @Column(name = "format")
    private String format;
    @Column(name = "duration")
    private String duration;

    @NotNull
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "upload_dir")
    private String uploadDir;
    @NotNull
    @Column(name = "file_url")
    private String fileURL;
    @Column(name = "views")
    private String views;
    @Column(name = "downloads")
    private String downloads;
    @Column(name = "status")
    private String status;

    public Video() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getDownloads() {
        return downloads;
    }

    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
