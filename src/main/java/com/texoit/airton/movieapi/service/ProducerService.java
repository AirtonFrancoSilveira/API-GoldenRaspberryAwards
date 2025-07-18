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

		// Calcular todos os intervalos consecutivos
		List<ProducerPrizesDTO> allIntervals = calculateAllConsecutiveIntervals(mpList);

		// Encontrar todos os produtores com intervalo mínimo e máximo
		List<ProducerPrizesDTO> minIntervals = findAllMinIntervals(allIntervals);
		List<ProducerPrizesDTO> maxIntervals = findAllMaxIntervals(allIntervals);

		ProducerMinMaxPrizesDTO dto = new ProducerMinMaxPrizesDTO();

		// Adicionar todos os intervalos mínimos
		for (ProducerPrizesDTO minInterval : minIntervals) {
			dto.addMin(minInterval);
		}

		// Adicionar todos os intervalos máximos
		for (ProducerPrizesDTO maxInterval : maxIntervals) {
			dto.addMax(maxInterval);
		}

		return dto;
	}

	private List<ProducerPrizesDTO> calculateAllConsecutiveIntervals(List<MovieProducer> mpList) {
		List<ProducerPrizesDTO> intervals = new ArrayList<>();

		// Agrupar filmes por produtor
		Map<String, List<Integer>> producerYears = new HashMap<>();

		for (MovieProducer mp : mpList) {
			String producerName = mp.getProducer().getName();
			Integer year = mp.getMovie().getYear();

			producerYears.computeIfAbsent(producerName, k -> new ArrayList<>()).add(year);
		}

		// Calcular intervalos consecutivos para cada produtor
		for (Map.Entry<String, List<Integer>> entry : producerYears.entrySet()) {
			String producerName = entry.getKey();
			List<Integer> years = entry.getValue();

			// Ordenar anos
			years.sort(Integer::compareTo);

			// Calcular intervalos consecutivos
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

		// Encontrar o menor intervalo
		Integer minInterval = intervals.stream()
				.mapToInt(ProducerPrizesDTO::getInterval)
				.min()
				.orElse(Integer.MAX_VALUE);

		// Adicionar todos os produtores com o menor intervalo
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

		// Encontrar o maior intervalo
		Integer maxInterval = intervals.stream()
				.mapToInt(ProducerPrizesDTO::getInterval)
				.max()
				.orElse(Integer.MIN_VALUE);

		// Adicionar todos os produtores com o maior intervalo
		for (ProducerPrizesDTO interval : intervals) {
			if (interval.getInterval().equals(maxInterval)) {
				maxIntervals.add(interval);
			}
		}

		return maxIntervals;
	}
}
