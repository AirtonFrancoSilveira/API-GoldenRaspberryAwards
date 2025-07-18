package com.texoit.airton.movieapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		List<ProducerPrizesDTO> allIntervals = calculateAllConsecutiveIntervals(mpList);

		List<ProducerPrizesDTO> minIntervals = findAllMinIntervals(allIntervals);
		List<ProducerPrizesDTO> maxIntervals = findAllMaxIntervals(allIntervals);

		ProducerMinMaxPrizesDTO dto = new ProducerMinMaxPrizesDTO();

		for (ProducerPrizesDTO minInterval : minIntervals) {
			dto.addMin(minInterval);
		}

		for (ProducerPrizesDTO maxInterval : maxIntervals) {
			dto.addMax(maxInterval);
		}

		return dto;
	}

	private List<ProducerPrizesDTO> calculateAllConsecutiveIntervals(List<MovieProducer> mpList) {
		List<ProducerPrizesDTO> intervals = new ArrayList<>();

		Map<String, List<Integer>> producerYears = new HashMap<>();

		for (MovieProducer mp : mpList) {
			String producerName = mp.getProducer().getName();
			Integer year = mp.getMovie().getYear();

			producerYears.computeIfAbsent(producerName, k -> new ArrayList<>()).add(year);
		}

		for (Map.Entry<String, List<Integer>> entry : producerYears.entrySet()) {
			String producerName = entry.getKey();
			List<Integer> years = entry.getValue();

			years.sort(Integer::compareTo);

			for (int i = 0; i < years.size() - 1; i++) {
				Integer previousWin = years.get(i);
				Integer followingWin = years.get(i + 1);
				Integer interval = followingWin - previousWin;

				intervals.add(new ProducerPrizesDTO(producerName, interval, previousWin, followingWin));
			}
		}

		return intervals;
	}

	private List<ProducerPrizesDTO> findAllMinIntervals(List<ProducerPrizesDTO> intervals) {
		List<ProducerPrizesDTO> minIntervals = new ArrayList<>();

		if (intervals.isEmpty()) {
			return minIntervals;
		}

		Integer minInterval = intervals.stream()
				.mapToInt(ProducerPrizesDTO::getInterval)
				.min()
				.orElse(Integer.MAX_VALUE);

		for (ProducerPrizesDTO interval : intervals) {
			if (interval.getInterval().equals(minInterval)) {
				minIntervals.add(interval);
			}
		}

		return minIntervals;
	}

	private List<ProducerPrizesDTO> findAllMaxIntervals(List<ProducerPrizesDTO> intervals) {
		List<ProducerPrizesDTO> maxIntervals = new ArrayList<>();

		if (intervals.isEmpty()) {
			return maxIntervals;
		}

		Integer maxInterval = intervals.stream()
				.mapToInt(ProducerPrizesDTO::getInterval)
				.max()
				.orElse(Integer.MIN_VALUE);

		for (ProducerPrizesDTO interval : intervals) {
			if (interval.getInterval().equals(maxInterval)) {
				maxIntervals.add(interval);
			}
		}

		return maxIntervals;
	}
}
