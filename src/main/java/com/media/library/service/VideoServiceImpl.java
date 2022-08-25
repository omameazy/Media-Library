package com.media.library.service;

import com.media.library.model.Music;
import com.media.library.model.Video;
import com.media.library.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoServiceImpl implements VideoService{

    @Autowired
    private VideoRepository repository;
    @Override
    public Video saveVideo(Video video) {
        return repository.save(video);
    }

    @Override
    public Video updateVideo(Video video) {
        return repository.save(video);
    }

    @Override
    public Video findVideoByID(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public Video findVideoByTitle(String title) {
        Video video= null;
        List<Video> videoList = repository.findAll();
        for (int i = 0; i < videoList.size(); i++) {
            if(videoList.get(i).getTitle().equals(title)){
                video= videoList.get(i);
                break;
            }
        }
        return video;
    }

    @Override
    public List<Video> findAllVideoByProducer(String producer) {
        List<Video> videos= null;
        List<Video> videoList = repository.findAll();
        for (int i = 0; i < videoList.size(); i++) {
            if(videoList.get(i).getProducer().equals(producer)){
                videos.add(videoList.get(i));
            }
        }
        return videos;
    }

    @Override
    public List<Video> findAllVideo() {
        return repository.findAll();
    }

    @Override
    public void deleteVideoByID(Long id) {

        Optional<Video> video= repository.findById(id);
        if(video.isPresent()){
            repository.deleteById(id);
        }
    }
}
