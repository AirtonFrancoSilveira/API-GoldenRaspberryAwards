package com.texoit.airton.movieapi.domain.model;

import java.time.Year;
import java.util.Objects;

import com.texoit.airton.movieapi.shared.exception.InvalidIntervalException;

/**
 * Value Object representando um intervalo entre prêmios de um produtor.
 * Segue os princípios de DDD com encapsulamento de regras de negócio.
 */
public class ProducerInterval {

    private final String producerName;
    private final int years;
    private final Year previousWin;
    private final Year followingWin;

    public ProducerInterval(String producerName, int years, Year previousWin, Year followingWin) {
        validateInterval(years, previousWin, followingWin);
        this.producerName = Objects.requireNonNull(producerName, "Producer name cannot be null");
        this.years = years;
        this.previousWin = previousWin;
        this.followingWin = followingWin;
    }

    private void validateInterval(int years, Year previousWin, Year followingWin) {
        if (years <= 0) {
            throw new InvalidIntervalException("Interval must be positive");
        }

        if (previousWin == null || followingWin == null) {
            throw new InvalidIntervalException("Win years cannot be null");
        }

        if (previousWin.isAfter(followingWin)) {
            throw new InvalidIntervalException("Previous win cannot be after following win");
        }

        if (followingWin.getValue() - previousWin.getValue() != years) {
            throw new InvalidIntervalException("Interval years must match the difference between win years");
        }
    }

    /**
     * Verifica se este intervalo é menor que outro
     */
    public boolean isShorterThan(ProducerInterval other) {
        return this.years < other.years;
    }

    /**
     * Verifica se este intervalo é maior que outro
     */
    public boolean isLongerThan(ProducerInterval other) {
        return this.years > other.years;
    }

    /**
     * Verifica se este intervalo tem a mesma duração que outro
     */
    public boolean hasSameDurationAs(ProducerInterval other) {
        return this.years == other.years;
    }

    /**
     * Verifica se este é um intervalo consecutivo (apenas 1 ano)
     */
    public boolean isConsecutive() {
        return this.years == 1;
    }

    /**
     * Verifica se este é um intervalo longo (mais de 10 anos)
     */
    public boolean isLongTerm() {
        return this.years > 10;
    }

    // Getters
    public String getProducerName() {
        return producerName;
    }

    public int getYears() {
        return years;
    }

    public Year getPreviousWin() {
        return previousWin;
    }

    public Year getFollowingWin() {
        return followingWin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ProducerInterval that = (ProducerInterval) o;
        return years == that.years &&
                Objects.equals(producerName, that.producerName) &&
                Objects.equals(previousWin, that.previousWin) &&
                Objects.equals(followingWin, that.followingWin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producerName, years, previousWin, followingWin);
    }

    @Override
    public String toString() {
        return String.format("ProducerInterval{producer='%s', years=%d, %d→%d}",
                producerName, years, previousWin.getValue(), followingWin.getValue());
    }
}