package com.media.library.repository;

import com.media.library.model.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
}
