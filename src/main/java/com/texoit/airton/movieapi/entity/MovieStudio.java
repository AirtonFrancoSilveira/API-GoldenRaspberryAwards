package com.texoit.airton.movieapi.entity;

import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="MOVIE_STUDIO")
public class MovieStudio {

	@EmbeddedId
	private MovieStudioId id;
	
	@ManyToOne
	@MapsId("idMovie")
	private Movie movie;
	
	@ManyToOne
	@MapsId("idStudio")
	private Studio studio;
	
	public MovieStudio() {}
	
	public MovieStudio(Movie movie, Studio studio) {
		this.movie = movie;
		this.studio = studio;
		this.id = new MovieStudioId(movie.getId(), studio.getId());
	}

	public MovieStudioId getId() {
		return id;
	}

	public void setId(MovieStudioId id) {
		this.id = id;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Studio getStudio() {
		return studio;
	}

	public void setStudio(Studio studio) {
		this.studio = studio;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		MovieStudio other = (MovieStudio) obj;
		return Objects.equals(movie, other.getMovie()) &&
				Objects.equals(studio, other.getStudio());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(movie, studio);
	}
	
}
