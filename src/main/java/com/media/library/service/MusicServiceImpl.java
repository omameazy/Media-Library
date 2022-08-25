package com.media.library.service;


import com.media.library.model.Music;
import com.media.library.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MusicServiceImpl implements MusicService{

    @Autowired
    private MusicRepository repository;
    @Override
    public Music saveMusic(Music music) {
        return repository.save(music);
    }

    @Override
    public Music updateMusic(Music music) {
        return repository.save(music);
    }

    @Override
    public Music findMusicByID(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public Music findMusicByTitle(String title) {
        Music music= null;
        List<Music> musicList = repository.findAll();
        for (int i = 0; i < musicList.size(); i++) {
            if(musicList.get(i).getTitle().equals(title)){
                music= musicList.get(i);
                break;
            }
        }
        return music;
    }

    @Override
    public List<Music> findAllMusicByArtist(String artist) {
        List<Music> music= null;
        List<Music> musicList = repository.findAll();
        for (int i = 0; i < musicList.size(); i++) {
            if(musicList.get(i).getArtist().equals(artist)){
                music.add(musicList.get(i));
            }
        }
        return music;
    }

    @Override
    public List<Music> findAllMusic() {
        return repository.findAll();
    }

    @Override
    public void deleteMusicByID(Long id) {
        Optional<Music> music = repository.findById(id);
        if(music.isPresent()){
            repository.deleteById(id);
        }
    }
}
