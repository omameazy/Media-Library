package com.media.library.payload.request;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class MusicRequest implements Serializable {

    @NotBlank
    private String title;
    @NotEmpty
    private String artist;
    @NotNull
    private String album;
    @NotNull
    private String description;

    @NotNull
    private String duration;


    public MusicRequest(String title, String artist, String album, String description, String duration) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.description = description;
        this.duration = duration;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
