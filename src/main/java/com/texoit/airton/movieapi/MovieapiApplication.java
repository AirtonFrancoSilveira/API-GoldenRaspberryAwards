package com.texoit.airton.movieapi;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.texoit.airton.movieapi.entity.Movie;
import com.texoit.airton.movieapi.repository.MovieRepository;
import com.texoit.airton.movieapi.service.ProducerService;
import com.texoit.airton.movieapi.service.StudioService;

@SpringBootApplication(scanBasePackages = { "com.texoit.airton.movieapi" })
public class MovieapiApplication {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private StudioService studioService;

	@Autowired
	private ProducerService producerService;

	public static void main(String[] args) {
		SpringApplication.run(MovieapiApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext appContext) {
		return args -> {

			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream in = cl.getResourceAsStream("movielist.csv");
			Reader reader = new InputStreamReader(in);
			Iterable<CSVRecord> records = CSVFormat.RFC4180
					.withDelimiter(';')
					.withHeader("year", "title", "studios", "producers", "winner")
					.parse(reader);

			for (CSVRecord record : records) {
				if (record.getRecordNumber() == 1) {
					continue;
				}

				String winner = record.get("winner");
				Movie movie = movieRepository
						.save(new Movie(Integer.valueOf(record.get("year")), record.get("title"), winner));

				String studios = record.get("studios");
				studioService.saveStudios(movie, studios);

				String producers = record.get("producers");
				producerService.saveProducers(movie, producers);
			}

		};
	}

}
