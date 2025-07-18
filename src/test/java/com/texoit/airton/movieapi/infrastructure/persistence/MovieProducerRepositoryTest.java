package com.texoit.airton.movieapi.infrastructure.persistence;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.texoit.airton.movieapi.entity.Movie;
import com.texoit.airton.movieapi.entity.MovieProducer;
import com.texoit.airton.movieapi.entity.Producer;
import com.texoit.airton.movieapi.repository.MovieProducerRepository;

/**
 * Test Slice para MovieProducerRepository.
 * Demonstra práticas de senior engineer com @DataJpaTest e TestEntityManager.
 * Testa apenas a camada de persistência isoladamente.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class MovieProducerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MovieProducerRepository movieProducerRepository;

    @Test
    public void shouldFindWinningMoviesByProducerOrderedByProducerIdAndYear() {
        // Given
        Producer joelSilver = createAndPersistProducer("Joel Silver");
        Producer matthewVaughn = createAndPersistProducer("Matthew Vaughn");

        Movie movie1990 = createAndPersistMovie(1990, "Movie 1990", true);
        Movie movie1991 = createAndPersistMovie(1991, "Movie 1991", true);
        Movie movie2002 = createAndPersistMovie(2002, "Movie 2002", true);
        Movie movie2015 = createAndPersistMovie(2015, "Movie 2015", true);
        Movie movie2020 = createAndPersistMovie(2020, "Movie 2020", false); // Not a winner

        createAndPersistMovieProducer(movie1990, joelSilver);
        createAndPersistMovieProducer(movie1991, joelSilver);
        createAndPersistMovieProducer(movie2002, matthewVaughn);
        createAndPersistMovieProducer(movie2015, matthewVaughn);
        createAndPersistMovieProducer(movie2020, matthewVaughn); // Not a winner

        entityManager.flush();

        // When
        List<MovieProducer> result = movieProducerRepository.findByMovieWinnerOrderByProducerId(true);

        // Then
        assertNotNull(result);
        assertEquals(4, result.size());

        // Verify only winning movies are returned
        result.forEach(mp -> assertTrue(mp.getMovie().getWinner()));

        // Verify ordering (should be by producer id, then by year)
        MovieProducer first = result.get(0);
        MovieProducer second = result.get(1);
        MovieProducer third = result.get(2);
        MovieProducer fourth = result.get(3);

        assertTrue("Results should be ordered by producer ID first",
                first.getProducer().getId() <= second.getProducer().getId());

        // If same producer, should be ordered by year
        if (first.getProducer().getId().equals(second.getProducer().getId())) {
            assertTrue("Same producer movies should be ordered by year",
                    first.getMovie().getYear() <= second.getMovie().getYear());
        }
    }

    @Test
    public void shouldReturnEmptyListWhenNoWinningMovies() {
        // Given
        Producer producer = createAndPersistProducer("Test Producer");
        Movie movie = createAndPersistMovie(2020, "Non-winning Movie", false);
        createAndPersistMovieProducer(movie, producer);

        entityManager.flush();

        // When
        List<MovieProducer> result = movieProducerRepository.findByMovieWinnerOrderByProducerId(true);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldHandleMultipleMoviesForSameProducer() {
        // Given
        Producer producer = createAndPersistProducer("Multi-Movie Producer");

        Movie movie1 = createAndPersistMovie(2000, "Movie 1", true);
        Movie movie2 = createAndPersistMovie(2005, "Movie 2", true);
        Movie movie3 = createAndPersistMovie(2010, "Movie 3", true);

        createAndPersistMovieProducer(movie1, producer);
        createAndPersistMovieProducer(movie2, producer);
        createAndPersistMovieProducer(movie3, producer);

        entityManager.flush();

        // When
        List<MovieProducer> result = movieProducerRepository.findByMovieWinnerOrderByProducerId(true);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());

        // Verify all belong to the same producer
        result.forEach(mp -> assertEquals(producer.getName(), mp.getProducer().getName()));

        // Verify ordering by year
        assertTrue("Movies should be ordered by year",
                result.get(0).getMovie().getYear() <= result.get(1).getMovie().getYear());
        assertTrue("Movies should be ordered by year",
                result.get(1).getMovie().getYear() <= result.get(2).getMovie().getYear());
    }

    @Test
    public void shouldHandleProducersWithSameNameCorrectly() {
        // Given
        Producer producer1 = createAndPersistProducer("Same Name");
        Producer producer2 = createAndPersistProducer("Same Name");

        Movie movie1 = createAndPersistMovie(2000, "Movie 1", true);
        Movie movie2 = createAndPersistMovie(2005, "Movie 2", true);

        createAndPersistMovieProducer(movie1, producer1);
        createAndPersistMovieProducer(movie2, producer2);

        entityManager.flush();

        // When
        List<MovieProducer> result = movieProducerRepository.findByMovieWinnerOrderByProducerId(true);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify they are treated as different producers (different IDs)
        assertNotEquals("Producers should have different IDs",
                result.get(0).getProducer().getId(),
                result.get(1).getProducer().getId());
    }

    @Test
    public void shouldHandleQueryWithFalseParameterCorrectly() {
        // Given
        Producer producer = createAndPersistProducer("Test Producer");
        Movie winningMovie = createAndPersistMovie(2000, "Winning Movie", true);
        Movie nonWinningMovie = createAndPersistMovie(2005, "Non-winning Movie", false);

        createAndPersistMovieProducer(winningMovie, producer);
        createAndPersistMovieProducer(nonWinningMovie, producer);

        entityManager.flush();

        // When
        List<MovieProducer> result = movieProducerRepository.findByMovieWinnerOrderByProducerId(false);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse("Should return only non-winning movies", result.get(0).getMovie().getWinner());
    }

    // Helper methods
    private Producer createAndPersistProducer(String name) {
        Producer producer = new Producer(name);
        return entityManager.persistAndFlush(producer);
    }

    private Movie createAndPersistMovie(int year, String title, boolean winner) {
        Movie movie = new Movie(year, title, winner ? "yes" : "no");
        return entityManager.persistAndFlush(movie);
    }

    private MovieProducer createAndPersistMovieProducer(Movie movie, Producer producer) {
        MovieProducer movieProducer = new MovieProducer(movie, producer);
        return entityManager.persistAndFlush(movieProducer);
    }
}