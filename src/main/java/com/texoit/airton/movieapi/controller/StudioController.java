package com.texoit.airton.movieapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.texoit.airton.movieapi.dto.StudioDTO;
import com.texoit.airton.movieapi.service.StudioService;

@RestController
@RequestMapping("/studio")
public class StudioController {
	
	Logger logger = LoggerFactory.getLogger(StudioController.class);
	
	@Autowired
	private StudioService studioService;
	
	@GetMapping("/winners")
	public ResponseEntity<StudioDTO> getGreatestWinners() {
		StudioDTO dto = studioService.getGreatestWinners();
		
		HttpStatus status = HttpStatus.OK;
		if ( dto.getStudios() == null || dto.getStudios().isEmpty() ) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return new ResponseEntity<StudioDTO>(dto, status) ;
	}

}
