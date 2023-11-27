package com.texoit.airton.movieapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.texoit.airton.movieapi.entity.Producer;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
	
	Producer findByName(String name);
	
}

