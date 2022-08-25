package com.media.library.service;


import com.media.library.model.Video;

import java.io.Serializable;
import java.util.List;

public interface VideoService extends Serializable {
    Video saveVideo(Video video);
    Video updateVideo(Video video);
    Video findVideoByID(Long id);
    Video findVideoByTitle(String title);
    List<Video> findAllVideoByProducer(String producer);
    List<Video> findAllVideo();
    void deleteVideoByID(Long id);
}
