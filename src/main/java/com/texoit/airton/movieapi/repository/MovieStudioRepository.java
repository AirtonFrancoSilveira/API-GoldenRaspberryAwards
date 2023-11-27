package com.texoit.airton.movieapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.texoit.airton.movieapi.entity.MovieStudio;
import com.texoit.airton.movieapi.entity.MovieStudioId;

public interface MovieStudioRepository extends JpaRepository<MovieStudio, MovieStudioId>{

}

