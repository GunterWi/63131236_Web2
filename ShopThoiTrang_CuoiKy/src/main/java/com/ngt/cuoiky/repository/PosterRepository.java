package com.ngt.cuoiky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ngt.cuoiky.model.Poster;

public interface PosterRepository extends JpaRepository<Poster, Integer> {

    @Query("SELECT p FROM Poster p WHERE p.type = 'right'")
    public List<Poster> listPosterRight();

    @Query("SELECT p FROM Poster p WHERE p.type = 'right' AND p.isActive <> FALSE")
    public List<Poster> listPosterRightUser();

    @Query("SELECT p FROM Poster p WHERE p.type = 'left'")
    public List<Poster> listPosterLeft();

    @Query("SELECT p FROM Poster p WHERE p.type = 'left' AND p.isActive <> FALSE")
    public List<Poster> listPosterLeftUser();
}
