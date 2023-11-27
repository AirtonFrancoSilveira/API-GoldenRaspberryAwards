package com.texoit.airton.movieapi.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.texoit.airton.movieapi.dto.ProducerMinMaxPrizesDTO;
import com.texoit.airton.movieapi.dto.ProducerPrizesDTO;
import com.texoit.airton.movieapi.entity.Movie;
import com.texoit.airton.movieapi.entity.MovieProducer;
import com.texoit.airton.movieapi.entity.Producer;
import com.texoit.airton.movieapi.repository.MovieProducerRepository;
import com.texoit.airton.movieapi.repository.ProducerRepository;

@Service
public class ProducerService {
	
	Logger logger = LoggerFactory.getLogger(ProducerService.class);
	
	@Autowired
	private ProducerRepository producerRepository;
	
	@Autowired
	private MovieProducerRepository movieProducerRepository;
	
	public void saveProducers(Movie movie, String producers) {
		for (String strProducer : producers.split(",|\\ and ")) {
			Producer producer = new Producer(strProducer.trim());
			
			Example<Producer> example = Example.of(producer); 
			
			if (producerRepository.exists(example)) {
				producer = producerRepository.findByName(strProducer.trim());
			} else {
				producer = producerRepository.save(producer);
			}
			
			movieProducerRepository.save(new MovieProducer(movie, producer));
		}
	}
	
	public ProducerMinMaxPrizesDTO getMaxAndMinPrizes() {
		List<MovieProducer> mpList = movieProducerRepository.findByMovieWinnerOrderByProducerId(true);
		
		ProducerPrizesDTO min = findMinInterval(mpList);
		ProducerPrizesDTO max = findMaxInterval(mpList);
		
		ProducerMinMaxPrizesDTO dto = new ProducerMinMaxPrizesDTO();
		dto.addMin(min);
		dto.addMax(max);
		
		return dto;
	}

	private ProducerPrizesDTO findMinInterval(List<MovieProducer> mpList) {
		ProducerPrizesDTO min = new ProducerPrizesDTO(null, Integer.MAX_VALUE, null, null);
		
		for ( int i = 0; i < mpList.size() - 1; i++ ) {
			
			for (int j = i + 1; j < mpList.size(); j++) {
				
				MovieProducer mpi = mpList.get(i);
				MovieProducer mpj = mpList.get(j);
				
				if (mpi.getProducer().equals(mpj.getProducer())) {
					Integer interval = Math.abs(mpi.getMovie().getYear()-mpj.getMovie().getYear());
					
					if (interval < min.getInterval()) {
						min.setInterval(interval);
						min.setProducer(mpi.getProducer().getName());
						min.setPreviousWin(mpi.getMovie().getYear());
						min.setFollowingWin(mpj.getMovie().getYear());
						
						break;
					}
				}
			}
		}
		
		return min;
	}
	
	private ProducerPrizesDTO findMaxInterval(List<MovieProducer> mpList) {
		ProducerPrizesDTO max = new ProducerPrizesDTO(null, Integer.MIN_VALUE, null, null);
		
		for ( int i = 0; i < mpList.size() - 1; i++ ) {
			
			for (int j = i + 1; j < mpList.size(); j++) {
				
				MovieProducer mpi = mpList.get(i);
				MovieProducer mpj = mpList.get(j);
				
				if (mpi.getProducer().equals(mpj.getProducer())) {
					Integer interval = Math.abs(mpi.getMovie().getYear()-mpj.getMovie().getYear());
					
					if (interval > max.getInterval()) {
						max.setInterval(interval);
						max.setProducer(mpi.getProducer().getName());
						max.setPreviousWin(mpi.getMovie().getYear());
						max.setFollowingWin(mpj.getMovie().getYear());
						
						break;
					}
				}
			}
		}
		
		return max;
	}
	
}
