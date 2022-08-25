package com.media.library.service;

import com.media.library.model.Music;

import java.io.Serializable;
import java.util.List;

public interface MusicService extends Serializable {
    Music saveMusic(Music music);
    Music updateMusic(Music music);
    Music findMusicByID(Long id);
    Music findMusicByTitle(String title);
    List<Music> findAllMusicByArtist(String artist);
    List<Music> findAllMusic();
    void deleteMusicByID(Long id);

}
