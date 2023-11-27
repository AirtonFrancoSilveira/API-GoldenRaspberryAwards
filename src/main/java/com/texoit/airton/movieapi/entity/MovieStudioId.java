package com.texoit.airton.movieapi.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class MovieStudioId implements Serializable {
	
	private static final long serialVersionUID = -5522529450406784648L;

	private Long idMovie;
	
	private Long idStudio;
	
	public MovieStudioId() {}
	
	public MovieStudioId(Long idMovie, Long idStudio) {
		this.idMovie = idMovie;
		this.idStudio = idStudio;
	}

	public Long getIdMovie() {
		return idMovie;
	}

	public Long getIdStudio() {
		return idStudio;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		MovieStudioId other = (MovieStudioId) obj;
		return Objects.equals(idMovie, other.getIdMovie()) &&
				Objects.equals(idStudio, other.getIdStudio());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(idMovie, idStudio);
	}

}
