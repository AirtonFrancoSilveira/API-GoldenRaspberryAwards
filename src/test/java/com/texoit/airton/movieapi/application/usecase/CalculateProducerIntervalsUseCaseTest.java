package com.texoit.airton.movieapi.application.usecase;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.texoit.airton.movieapi.dto.ProducerMinMaxPrizesDTO;
import com.texoit.airton.movieapi.dto.ProducerPrizesDTO;
import com.texoit.airton.movieapi.entity.Movie;
import com.texoit.airton.movieapi.entity.MovieProducer;
import com.texoit.airton.movieapi.entity.Producer;
import com.texoit.airton.movieapi.repository.MovieProducerRepository;

/**
 * Testes unitários para CalculateProducerIntervalsUseCase.
 * Demonstra práticas de senior engineer com mocks e testes isolados.
 */
@RunWith(MockitoJUnitRunner.class)
public class CalculateProducerIntervalsUseCaseTest {

    @Mock
    private MovieProducerRepository movieProducerRepository;

    private CalculateProducerIntervalsUseCase useCase;

    @Before
    public void setUp() {
        useCase = new CalculateProducerIntervalsUseCase(movieProducerRepository);
    }

    @Test
    public void shouldCalculateIntervalsSuccessfully() {
        // Given
        List<MovieProducer> winningMovies = createTestData();
        when(movieProducerRepository.findByMovieWinnerOrderByProducerId(true))
                .thenReturn(winningMovies);

        // When
        ProducerMinMaxPrizesDTO result = useCase.execute();

        // Then
        assertNotNull(result);
        assertNotNull(result.getMin());
        assertNotNull(result.getMax());

        // Verify repository was called
        verify(movieProducerRepository).findByMovieWinnerOrderByProducerId(true);
    }

    @Test
    public void shouldHandleEmptyDataGracefully() {
        // Given
        when(movieProducerRepository.findByMovieWinnerOrderByProducerId(true))
                .thenReturn(Collections.emptyList());

        // When
        ProducerMinMaxPrizesDTO result = useCase.execute();

        // Then
        assertNotNull(result);
        assertTrue(result.getMin().isEmpty());
        assertTrue(result.getMax().isEmpty());
    }

    @Test
    public void shouldCalculateMinIntervalCorrectly() {
        // Given
        List<MovieProducer> winningMovies = createConsecutiveWinData();
        when(movieProducerRepository.findByMovieWinnerOrderByProducerId(true))
                .thenReturn(winningMovies);

        // When
        ProducerMinMaxPrizesDTO result = useCase.execute();

        // Then
        assertFalse(result.getMin().isEmpty());

        ProducerPrizesDTO minInterval = result.getMin().get(0);
        assertEquals("Joel Silver", minInterval.getProducer());
        assertEquals(Integer.valueOf(1), minInterval.getInterval());
        assertEquals(Integer.valueOf(1990), minInterval.getPreviousWin());
        assertEquals(Integer.valueOf(1991), minInterval.getFollowingWin());
    }

    @Test
    public void shouldCalculateMaxIntervalCorrectly() {
        // Given
        List<MovieProducer> winningMovies = createLongIntervalData();
        when(movieProducerRepository.findByMovieWinnerOrderByProducerId(true))
                .thenReturn(winningMovies);

        // When
        ProducerMinMaxPrizesDTO result = useCase.execute();

        // Then
        assertFalse(result.getMax().isEmpty());

        ProducerPrizesDTO maxInterval = result.getMax().get(0);
        assertEquals("Matthew Vaughn", maxInterval.getProducer());
        assertEquals(Integer.valueOf(13), maxInterval.getInterval());
        assertEquals(Integer.valueOf(2002), maxInterval.getPreviousWin());
        assertEquals(Integer.valueOf(2015), maxInterval.getFollowingWin());
    }

    @Test
    public void shouldHandleMultipleIntervalsForSameProducer() {
        // Given
        List<MovieProducer> winningMovies = createMultipleIntervalsData();
        when(movieProducerRepository.findByMovieWinnerOrderByProducerId(true))
                .thenReturn(winningMovies);

        // When
        ProducerMinMaxPrizesDTO result = useCase.execute();

        // Then
        assertNotNull(result);
        assertFalse(result.getMin().isEmpty());
        assertFalse(result.getMax().isEmpty());

        // Verify that only consecutive intervals are considered
        // Matthew Vaughn should have intervals: 2002->2003 (1 year), 2003->2015 (12
        // years)
        List<ProducerPrizesDTO> minIntervals = result.getMin();
        boolean hasMatthewVaughnMin = minIntervals.stream()
                .anyMatch(interval -> "Matthew Vaughn".equals(interval.getProducer())
                        && interval.getInterval() == 1);

        assertTrue("Should include Matthew Vaughn's 1-year interval", hasMatthewVaughnMin);
    }

    @Test
    public void shouldHandleMultipleProducersWithSameInterval() {
        // Given
        List<MovieProducer> winningMovies = createTiedIntervalsData();
        when(movieProducerRepository.findByMovieWinnerOrderByProducerId(true))
                .thenReturn(winningMovies);

        // When
        ProducerMinMaxPrizesDTO result = useCase.execute();

        // Then
        assertNotNull(result);

        // Both Joel Silver and Matthew Vaughn should have 1-year intervals
        List<ProducerPrizesDTO> minIntervals = result.getMin();
        assertEquals(2, minIntervals.size());

        boolean hasJoelSilver = minIntervals.stream()
                .anyMatch(interval -> "Joel Silver".equals(interval.getProducer()));
        boolean hasMatthewVaughn = minIntervals.stream()
                .anyMatch(interval -> "Matthew Vaughn".equals(interval.getProducer()));

        assertTrue("Should include Joel Silver", hasJoelSilver);
        assertTrue("Should include Matthew Vaughn", hasMatthewVaughn);
    }

    @Test(expected = RuntimeException.class)
    public void shouldHandleRepositoryException() {
        // Given
        when(movieProducerRepository.findByMovieWinnerOrderByProducerId(true))
                .thenThrow(new RuntimeException("Database error"));

        // When
        useCase.execute();

        // Then - Exception should be thrown
    }

    // Helper methods to create test data
    private List<MovieProducer> createTestData() {
        Producer joelSilver = new Producer("Joel Silver");
        Producer matthewVaughn = new Producer("Matthew Vaughn");

        Movie movie1990 = new Movie(1990, "Movie 1990", "yes");
        Movie movie1991 = new Movie(1991, "Movie 1991", "yes");
        Movie movie2002 = new Movie(2002, "Movie 2002", "yes");
        Movie movie2015 = new Movie(2015, "Movie 2015", "yes");

        return Arrays.asList(
                new MovieProducer(movie1990, joelSilver),
                new MovieProducer(movie1991, joelSilver),
                new MovieProducer(movie2002, matthewVaughn),
                new MovieProducer(movie2015, matthewVaughn));
    }

    private List<MovieProducer> createConsecutiveWinData() {
        Producer joelSilver = new Producer("Joel Silver");

        Movie movie1990 = new Movie(1990, "Movie 1990", "yes");
        Movie movie1991 = new Movie(1991, "Movie 1991", "yes");

        return Arrays.asList(
                new MovieProducer(movie1990, joelSilver),
                new MovieProducer(movie1991, joelSilver));
    }

    private List<MovieProducer> createLongIntervalData() {
        Producer matthewVaughn = new Producer("Matthew Vaughn");

        Movie movie2002 = new Movie(2002, "Movie 2002", "yes");
        Movie movie2015 = new Movie(2015, "Movie 2015", "yes");

        return Arrays.asList(
                new MovieProducer(movie2002, matthewVaughn),
                new MovieProducer(movie2015, matthewVaughn));
    }

    private List<MovieProducer> createMultipleIntervalsData() {
        Producer matthewVaughn = new Producer("Matthew Vaughn");

        Movie movie2002 = new Movie(2002, "Movie 2002", "yes");
        Movie movie2003 = new Movie(2003, "Movie 2003", "yes");
        Movie movie2015 = new Movie(2015, "Movie 2015", "yes");

        return Arrays.asList(
                new MovieProducer(movie2002, matthewVaughn),
                new MovieProducer(movie2003, matthewVaughn),
                new MovieProducer(movie2015, matthewVaughn));
    }

    private List<MovieProducer> createTiedIntervalsData() {
        Producer joelSilver = new Producer("Joel Silver");
        Producer matthewVaughn = new Producer("Matthew Vaughn");

        Movie movie1990 = new Movie(1990, "Movie 1990", "yes");
        Movie movie1991 = new Movie(1991, "Movie 1991", "yes");
        Movie movie2002 = new Movie(2002, "Movie 2002", "yes");
        Movie movie2003 = new Movie(2003, "Movie 2003", "yes");

        return Arrays.asList(
                new MovieProducer(movie1990, joelSilver),
                new MovieProducer(movie1991, joelSilver),
                new MovieProducer(movie2002, matthewVaughn),
                new MovieProducer(movie2003, matthewVaughn));
    }
}