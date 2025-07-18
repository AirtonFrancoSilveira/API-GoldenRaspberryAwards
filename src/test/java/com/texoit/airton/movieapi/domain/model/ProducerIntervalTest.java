package com.texoit.airton.movieapi.domain.model;

import static org.junit.Assert.*;

import java.time.Year;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.texoit.airton.movieapi.shared.exception.InvalidIntervalException;

/**
 * Testes unitários para ProducerInterval (Value Object).
 * Demonstra práticas de senior engineer com testes abrangentes.
 */
@RunWith(Enclosed.class)
public class ProducerIntervalTest {

    public static class CreationAndValidation {

        @Test
        public void shouldCreateValidInterval() {
            // Given
            String producer = "Matthew Vaughn";
            int years = 13;
            Year previousWin = Year.of(2002);
            Year followingWin = Year.of(2015);

            // When
            ProducerInterval interval = new ProducerInterval(producer, years, previousWin, followingWin);

            // Then
            assertEquals(producer, interval.getProducerName());
            assertEquals(years, interval.getYears());
            assertEquals(previousWin, interval.getPreviousWin());
            assertEquals(followingWin, interval.getFollowingWin());
        }

        @Test(expected = NullPointerException.class)
        public void shouldFailWhenProducerIsNull() {
            // Given
            String producer = null;
            int years = 5;
            Year previousWin = Year.of(2000);
            Year followingWin = Year.of(2005);

            // When & Then
            new ProducerInterval(producer, years, previousWin, followingWin);
        }

        @Test(expected = InvalidIntervalException.class)
        public void shouldFailWhenIntervalIsZero() {
            // Given
            String producer = "Test Producer";
            int years = 0;
            Year previousWin = Year.of(2000);
            Year followingWin = Year.of(2005);

            // When & Then
            new ProducerInterval(producer, years, previousWin, followingWin);
        }

        @Test(expected = InvalidIntervalException.class)
        public void shouldFailWhenIntervalIsNegative() {
            // Given
            String producer = "Test Producer";
            int years = -1;
            Year previousWin = Year.of(2000);
            Year followingWin = Year.of(2005);

            // When & Then
            new ProducerInterval(producer, years, previousWin, followingWin);
        }

        @Test(expected = InvalidIntervalException.class)
        public void shouldFailWhenPreviousWinIsNull() {
            // Given
            String producer = "Test Producer";
            int years = 5;
            Year previousWin = null;
            Year followingWin = Year.of(2005);

            // When & Then
            new ProducerInterval(producer, years, previousWin, followingWin);
        }

        @Test(expected = InvalidIntervalException.class)
        public void shouldFailWhenFollowingWinIsNull() {
            // Given
            String producer = "Test Producer";
            int years = 5;
            Year previousWin = Year.of(2000);
            Year followingWin = null;

            // When & Then
            new ProducerInterval(producer, years, previousWin, followingWin);
        }

        @Test(expected = InvalidIntervalException.class)
        public void shouldFailWhenPreviousWinIsAfterFollowing() {
            // Given
            String producer = "Test Producer";
            int years = 5;
            Year previousWin = Year.of(2010);
            Year followingWin = Year.of(2005);

            // When & Then
            new ProducerInterval(producer, years, previousWin, followingWin);
        }

        @Test(expected = InvalidIntervalException.class)
        public void shouldFailWhenIntervalDoesNotMatchYearDifference() {
            // Given
            String producer = "Test Producer";
            int years = 10; // Diferença real é 5
            Year previousWin = Year.of(2000);
            Year followingWin = Year.of(2005);

            // When & Then
            new ProducerInterval(producer, years, previousWin, followingWin);
        }
    }

    public static class ComparisonMethods {

        @Test
        public void shouldIdentifyShorterInterval() {
            // Given
            ProducerInterval interval1 = createInterval("Producer A", 1, 2000, 2001);
            ProducerInterval interval2 = createInterval("Producer B", 5, 2000, 2005);

            // When & Then
            assertTrue(interval1.isShorterThan(interval2));
            assertFalse(interval2.isShorterThan(interval1));
        }

        @Test
        public void shouldIdentifyLongerInterval() {
            // Given
            ProducerInterval interval1 = createInterval("Producer A", 1, 2000, 2001);
            ProducerInterval interval2 = createInterval("Producer B", 5, 2000, 2005);

            // When & Then
            assertTrue(interval2.isLongerThan(interval1));
            assertFalse(interval1.isLongerThan(interval2));
        }

        @Test
        public void shouldIdentifySameDurationIntervals() {
            // Given
            ProducerInterval interval1 = createInterval("Producer A", 5, 2000, 2005);
            ProducerInterval interval2 = createInterval("Producer B", 5, 2010, 2015);

            // When & Then
            assertTrue(interval1.hasSameDurationAs(interval2));
            assertTrue(interval2.hasSameDurationAs(interval1));
        }
    }

    public static class ClassificationMethods {

        @Test
        public void shouldIdentifyConsecutiveInterval() {
            // Given
            ProducerInterval interval = createInterval("Producer A", 1, 2000, 2001);

            // When & Then
            assertTrue(interval.isConsecutive());
        }

        @Test
        public void shouldIdentifyNonConsecutiveInterval() {
            // Given
            ProducerInterval interval = createInterval("Producer A", 5, 2000, 2005);

            // When & Then
            assertFalse(interval.isConsecutive());
        }

        @Test
        public void shouldIdentifyLongTermInterval() {
            // Given
            ProducerInterval interval = createInterval("Producer A", 11, 2000, 2011);

            // When & Then
            assertTrue(interval.isLongTerm());
        }

        @Test
        public void shouldIdentifyNonLongTermInterval() {
            // Given
            ProducerInterval interval = createInterval("Producer A", 5, 2000, 2005);

            // When & Then
            assertFalse(interval.isLongTerm());
        }
    }

    public static class EqualsAndHashCode {

        @Test
        public void shouldBeEqualWhenAllFieldsAreEqual() {
            // Given
            ProducerInterval interval1 = createInterval("Producer A", 5, 2000, 2005);
            ProducerInterval interval2 = createInterval("Producer A", 5, 2000, 2005);

            // When & Then
            assertEquals(interval1, interval2);
            assertEquals(interval1.hashCode(), interval2.hashCode());
        }

        @Test
        public void shouldBeDifferentWhenProducerIsDifferent() {
            // Given
            ProducerInterval interval1 = createInterval("Producer A", 5, 2000, 2005);
            ProducerInterval interval2 = createInterval("Producer B", 5, 2000, 2005);

            // When & Then
            assertNotEquals(interval1, interval2);
        }

        @Test
        public void shouldBeDifferentWhenYearsAreDifferent() {
            // Given
            ProducerInterval interval1 = createInterval("Producer A", 5, 2000, 2005);
            ProducerInterval interval2 = createInterval("Producer A", 3, 2000, 2003);

            // When & Then
            assertNotEquals(interval1, interval2);
        }
    }

    public static class ToStringMethod {

        @Test
        public void shouldHaveInformativeToString() {
            // Given
            ProducerInterval interval = createInterval("Matthew Vaughn", 13, 2002, 2015);

            // When
            String toString = interval.toString();

            // Then
            assertTrue(toString.contains("Matthew Vaughn"));
            assertTrue(toString.contains("13"));
            assertTrue(toString.contains("2002"));
            assertTrue(toString.contains("2015"));
        }
    }

    // Helper method
    private static ProducerInterval createInterval(String producer, int years, int previousWin, int followingWin) {
        return new ProducerInterval(producer, years, Year.of(previousWin), Year.of(followingWin));
    }
}