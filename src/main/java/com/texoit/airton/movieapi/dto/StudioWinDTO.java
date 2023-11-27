package com.texoit.airton.movieapi.dto;

public class StudioWinDTO {
	
	private String name;
	
	private Long winCount;
	
	public StudioWinDTO () {}
	
	public StudioWinDTO (String name, Long winCount) {
		this.name = name;
		this.winCount = winCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getWinCount() {
		return winCount;
	}

	public void setWinCount(Long winCount) {
		this.winCount = winCount;
	}
	
}
