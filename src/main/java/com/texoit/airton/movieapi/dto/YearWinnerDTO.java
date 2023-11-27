package com.texoit.airton.movieapi.dto;

import java.util.ArrayList;
import java.util.List;

public class YearWinnerDTO {
	
	private List<YearWinnerMovieDTO> years;
	
	public YearWinnerDTO(List<YearWinnerMovieDTO> years) {
		this.years = new ArrayList<>();
		this.years.addAll(years);
	}
	
	public YearWinnerDTO() {
		this.years = new ArrayList<>();
	}

	public List<YearWinnerMovieDTO> getYears() {
		return years;
	}

	public void setYears(List<YearWinnerMovieDTO> years) {
		this.years = years;
	}

}
