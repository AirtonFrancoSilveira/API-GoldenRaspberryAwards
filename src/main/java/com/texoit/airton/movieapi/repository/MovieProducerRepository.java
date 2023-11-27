package com.texoit.airton.movieapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.texoit.airton.movieapi.entity.MovieProducer;
import com.texoit.airton.movieapi.entity.MovieProducerId;

public interface MovieProducerRepository extends JpaRepository<MovieProducer, MovieProducerId> {
	
	@Query(value="select mp from MovieProducer as mp join mp.movie as movie join mp.producer as producer "
			+ "where movie.winner = true order by producer.id, movie.year")
	List<MovieProducer> findByMovieWinnerOrderByProducerId(Boolean isWinner);
	
}

