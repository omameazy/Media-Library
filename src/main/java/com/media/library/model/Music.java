package com.media.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Entity
@Builder
@Table(name = "music_library")
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "title")
    private String title;
    @NotNull
    @Column(name = "artist")
    private String artist;

    @Column(name = "album")
    private String album;
    @Column(name = "description")
    private String description;
    @NotNull
    @Column(name = "format")
    private String format;
    @Column(name = "duration")
    private String duration;
    @Column(name = "category")
    private String category;

    @Column(name = "size")
    private String size;

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

    public Music() {

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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public String getViews() {
        return views;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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
}
