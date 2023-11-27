package com.texoit.airton.movieapi.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class MovieProducerId implements Serializable {
	
	private static final long serialVersionUID = -5332423598071950748L;

	private Long idMovie;
	
	private Long idProducer;
	
	public MovieProducerId() {}
	
	public MovieProducerId(Long idMovie, Long idProducer) {
		this.idMovie = idMovie;
		this.idProducer = idProducer;
	}

	public Long getIdMovie() {
		return idMovie;
	}

	public Long getIdProducer() {
		return idProducer;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		MovieProducerId other = (MovieProducerId) obj;
		return Objects.equals(idMovie, other.getIdMovie()) &&
				Objects.equals(idProducer, other.getIdProducer());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(idMovie, idProducer);
	}

}
