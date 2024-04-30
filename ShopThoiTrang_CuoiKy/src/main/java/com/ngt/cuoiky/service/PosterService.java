package com.ngt.cuoiky.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.model.Poster;
import com.ngt.cuoiky.repository.PosterRepository;

@Service
public class PosterService {
    private final Logger log = LoggerFactory.getLogger(PosterService.class);
    @Autowired
    private PosterRepository posterRepository;

    public List<Poster> listPosterRight() {
        return posterRepository.listPosterRight();
    }

    public List<Poster> listPosterLeft() {
        return posterRepository.listPosterLeft();
    }

    public List<Poster> listPosterRightUser() {
        return posterRepository.listPosterRightUser();
    }

    public List<Poster> listPosterLeftUser() {
        return posterRepository.listPosterLeftUser();
    }

}
