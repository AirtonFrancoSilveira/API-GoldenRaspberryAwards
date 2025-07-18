package com.texoit.airton.movieapi.application.usecase;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.texoit.airton.movieapi.domain.model.ProducerInterval;
import com.texoit.airton.movieapi.dto.ProducerMinMaxPrizesDTO;
import com.texoit.airton.movieapi.dto.ProducerPrizesDTO;
import com.texoit.airton.movieapi.entity.MovieProducer;
import com.texoit.airton.movieapi.repository.MovieProducerRepository;
import com.texoit.airton.movieapi.shared.annotation.UseCase;

@UseCase
public class CalculateProducerIntervalsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CalculateProducerIntervalsUseCase.class);

    private final MovieProducerRepository movieProducerRepository;

    public CalculateProducerIntervalsUseCase(MovieProducerRepository movieProducerRepository) {
        this.movieProducerRepository = movieProducerRepository;
    }

    public ProducerMinMaxPrizesDTO execute() {
        logger.info("Starting producer intervals calculation");

        try {
            List<MovieProducer> winners = movieProducerRepository.findByMovieWinnerOrderByProducerId(true);
            logger.debug("Found {} winning movies", winners.size());

            List<ProducerInterval> intervals = calculateConsecutiveIntervals(winners);
            logger.debug("Calculated {} intervals", intervals.size());

            List<ProducerInterval> minIntervals = findMinimalIntervals(intervals);
            List<ProducerInterval> maxIntervals = findMaximalIntervals(intervals);

            ProducerMinMaxPrizesDTO result = buildResponse(minIntervals, maxIntervals);

            logger.info("Intervals calculation completed successfully. Min: {}, Max: {}",
                    minIntervals.size(), maxIntervals.size());

            return result;

        } catch (Exception e) {
            logger.error("Error calculating producer intervals", e);
            throw new RuntimeException("Failed to calculate producer intervals", e);
        }
    }

    /**
     * Calcula intervalos consecutivos entre vitórias dos produtores.
     */
    private List<ProducerInterval> calculateConsecutiveIntervals(List<MovieProducer> winners) {
        return winners.stream()
                .collect(Collectors.groupingBy(mp -> mp.getProducer().getName()))
                .entrySet().stream()
                .flatMap(entry -> {
                    String producerName = entry.getKey();
                    List<Integer> years = entry.getValue().stream()
                            .map(mp -> mp.getMovie().getYear())
                            .sorted()
                            .collect(Collectors.toList());

                    return calculateIntervalsForProducer(producerName, years).stream();
                })
                .collect(Collectors.toList());
    }

    /**
     * Calcula intervalos para um produtor específico.
     */
    private List<ProducerInterval> calculateIntervalsForProducer(String producerName, List<Integer> years) {
        return years.stream()
                .collect(Collectors.toList())
                .stream()
                .collect(Collectors.groupingBy(year -> years.indexOf(year)))
                .entrySet().stream()
                .filter(entry -> entry.getKey() < years.size() - 1)
                .map(entry -> {
                    int currentIndex = entry.getKey();
                    int currentYear = years.get(currentIndex);
                    int nextYear = years.get(currentIndex + 1);
                    int interval = nextYear - currentYear;

                    return new ProducerInterval(
                            producerName,
                            interval,
                            Year.of(currentYear),
                            Year.of(nextYear));
                })
                .collect(Collectors.toList());
    }

    /**
     * Encontra todos os intervalos mínimos.
     */
    private List<ProducerInterval> findMinimalIntervals(List<ProducerInterval> intervals) {
        if (intervals.isEmpty()) {
            return List.of();
        }

        int minYears = intervals.stream()
                .mapToInt(ProducerInterval::getYears)
                .min()
                .orElse(0);

        return intervals.stream()
                .filter(interval -> interval.getYears() == minYears)
                .collect(Collectors.toList());
    }

    /**
     * Encontra todos os intervalos máximos.
     */
    private List<ProducerInterval> findMaximalIntervals(List<ProducerInterval> intervals) {
        if (intervals.isEmpty()) {
            return List.of();
        }

        int maxYears = intervals.stream()
                .mapToInt(ProducerInterval::getYears)
                .max()
                .orElse(0);

        return intervals.stream()
                .filter(interval -> interval.getYears() == maxYears)
                .collect(Collectors.toList());
    }

    /**
     * Constrói a resposta DTO a partir dos intervalos calculados.
     */
    private ProducerMinMaxPrizesDTO buildResponse(List<ProducerInterval> minIntervals,
            List<ProducerInterval> maxIntervals) {
        ProducerMinMaxPrizesDTO result = new ProducerMinMaxPrizesDTO();

        minIntervals.forEach(interval -> {
            ProducerPrizesDTO dto = new ProducerPrizesDTO(
                    interval.getProducerName(),
                    interval.getYears(),
                    interval.getPreviousWin().getValue(),
                    interval.getFollowingWin().getValue());
            result.addMin(dto);
        });

        maxIntervals.forEach(interval -> {
            ProducerPrizesDTO dto = new ProducerPrizesDTO(
                    interval.getProducerName(),
                    interval.getYears(),
                    interval.getPreviousWin().getValue(),
                    interval.getFollowingWin().getValue());
            result.addMax(dto);
        });

        return result;
    }
}