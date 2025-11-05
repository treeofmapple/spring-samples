package com.tom.first.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tom.first.datajpa.model.Video;

public interface VideoRepository extends JpaRepository <Video, Integer>{

}
