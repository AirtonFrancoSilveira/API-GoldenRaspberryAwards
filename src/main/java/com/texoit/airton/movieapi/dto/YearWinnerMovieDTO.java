package com.texoit.airton.movieapi.dto;

public class YearWinnerMovieDTO {
	
	private Integer year;
	
	private Long winnerCount;
	
	public YearWinnerMovieDTO(Integer year, Long winnerCount) {
		this.year = year;
		this.winnerCount = winnerCount;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Long getWinnerCount() {
		return winnerCount;
	}

	public void setWinnerCount(Long winnerCount) {
		this.winnerCount = winnerCount;
	}
	
}
