package com.texoit.airton.movieapi.dto;

import java.util.ArrayList;
import java.util.List;

public class StudioDTO {
	
	private List<StudioWinDTO> studios;
	
	public StudioDTO(List<StudioWinDTO> winners) {
		this.studios = new ArrayList<>();
		this.studios.addAll(winners);
	}

	public List<StudioWinDTO> getStudios() {
		return studios;
	}

	public void setStudios(List<StudioWinDTO> studios) {
		this.studios = studios;
	}
	
}
