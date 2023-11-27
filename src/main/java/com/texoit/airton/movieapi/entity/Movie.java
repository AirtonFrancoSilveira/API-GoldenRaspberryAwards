package com.texoit.airton.movieapi.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="MOVIE")
public class Movie {
	
	@Id
	@Column(name="ID_MOVIE")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="YEAR", nullable=false)
	private Integer year;
	
	@Column(name="TITLE", nullable=false)
	private String title;
	
	@Column(name="IS_WINNER", nullable=false)
	private Boolean winner;

	@OneToMany(mappedBy="movie", cascade=CascadeType.ALL, orphanRemoval = true, fetch=FetchType.EAGER)
	private Set<MovieStudio> studios = new HashSet<>();
	
	@OneToMany(mappedBy="movie", cascade=CascadeType.ALL, orphanRemoval = true, fetch=FetchType.EAGER)
	private Set<MovieProducer> producers = new HashSet<>();
	
	public Movie() {}
	
	public Movie(Integer year, String title, String winner) {
		this.year = year;
		this.title = title;
		this.winner = (winner != null && "yes".equalsIgnoreCase(winner)) ;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getWinner() {
		return winner;
	}

	public void setWinner(Boolean winner) {
		this.winner = winner;
	}

	public Set<MovieStudio> getStudios() {
		return studios;
	}

	public void setStudios(Set<MovieStudio> studios) {
		this.studios = studios;
	}

	public Set<MovieProducer> getProducers() {
		return producers;
	}

	public void setProducers(Set<MovieProducer> producers) {
		this.producers = producers;
	}
	
	@Override
	public String toString() {
		return "Year: "+ getYear() + " - Title: "+ getTitle() + " - Winner: "+ getWinner();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		Movie other = (Movie) obj;
		return Objects.equals(id, other.getId());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, year);
	}
}

