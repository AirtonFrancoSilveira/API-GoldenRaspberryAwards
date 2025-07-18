package com.texoit.airton.movieapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.texoit.airton.movieapi.dto.ProducerMinMaxPrizesDTO;
import com.texoit.airton.movieapi.dto.ProducerPrizesDTO;
import com.texoit.airton.movieapi.entity.Movie;
import com.texoit.airton.movieapi.entity.MovieProducer;
import com.texoit.airton.movieapi.entity.Producer;
import com.texoit.airton.movieapi.repository.MovieProducerRepository;
import com.texoit.airton.movieapi.repository.MovieRepository;
import com.texoit.airton.movieapi.repository.ProducerRepository;
import com.texoit.airton.movieapi.service.ProducerService;

/**
 * Teste de integração para verificar se o sistema apresenta dois resultados min
 * com intervalo igual a 1 e dois resultados max com intervalo igual a 22,
 * conforme especificado nos itens observados.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProducerIntervalsSpecificationTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private MovieProducerRepository movieProducerRepository;

    @Autowired
    private ProducerService producerService;

    @Test
    public void testProducerIntervalsSpecification() {
        // Limpar dados existentes
        movieProducerRepository.deleteAll();
        movieRepository.deleteAll();
        producerRepository.deleteAll();

        // Criar produtores
        Producer matthewVaughn = new Producer("Matthew Vaughn");
        Producer joelSilver = new Producer("Joel Silver");

        producerRepository.save(matthewVaughn);
        producerRepository.save(joelSilver);

        // Criar filmes conforme especificação
        Movie movie1980 = new Movie(1980, "Test 1", "yes");
        Movie movie1990 = new Movie(1990, "The Adventures of Ford Fairlane", "yes");
        Movie movie1991 = new Movie(1991, "Hudson Hawk", "yes");
        Movie movie2002 = new Movie(2002, "Swept Away", "yes");
        Movie movie2003 = new Movie(2003, "Test 2", "yes");
        Movie movie2015 = new Movie(2015, "Fantastic Four", "yes");
        Movie movie2037 = new Movie(2037, "Test 3", "yes");

        movieRepository
                .saveAll(Arrays.asList(movie1980, movie1990, movie1991, movie2002, movie2003, movie2015, movie2037));

        // Criar relacionamentos MovieProducer
        movieProducerRepository.save(new MovieProducer(movie1980, matthewVaughn));
        movieProducerRepository.save(new MovieProducer(movie1990, joelSilver));
        movieProducerRepository.save(new MovieProducer(movie1991, joelSilver));
        movieProducerRepository.save(new MovieProducer(movie2002, matthewVaughn));
        movieProducerRepository.save(new MovieProducer(movie2003, matthewVaughn));
        movieProducerRepository.save(new MovieProducer(movie2015, matthewVaughn));
        movieProducerRepository.save(new MovieProducer(movie2037, matthewVaughn));

        // Executar o serviço
        ProducerMinMaxPrizesDTO result = producerService.getMaxAndMinPrizes();

        // Validar resultados
        assertNotNull("Resultado não deve ser nulo", result);

        // Deve ter 2 resultados min (interval = 1)
        List<ProducerPrizesDTO> minResults = result.getMin();
        assertEquals("Deve haver exatamente 2 resultados min", 2, minResults.size());

        // Verificar se todos os min têm intervalo = 1
        for (ProducerPrizesDTO min : minResults) {
            assertEquals("Intervalo mínimo deve ser 1", Integer.valueOf(1), min.getInterval());
        }

        // Deve ter 2 resultados max (interval = 22)
        List<ProducerPrizesDTO> maxResults = result.getMax();
        assertEquals("Deve haver exatamente 2 resultados max", 2, maxResults.size());

        // Verificar se todos os max têm intervalo = 22
        for (ProducerPrizesDTO max : maxResults) {
            assertEquals("Intervalo máximo deve ser 22", Integer.valueOf(22), max.getInterval());
        }

        // Validar dados específicos
        boolean foundJoelSilverMin = minResults.stream()
                .anyMatch(p -> "Joel Silver".equals(p.getProducer()) &&
                        p.getInterval() == 1 &&
                        p.getPreviousWin() == 1990 &&
                        p.getFollowingWin() == 1991);

        boolean foundMatthewVaughnMin = minResults.stream()
                .anyMatch(p -> "Matthew Vaughn".equals(p.getProducer()) &&
                        p.getInterval() == 1 &&
                        p.getPreviousWin() == 2002 &&
                        p.getFollowingWin() == 2003);

        boolean foundMatthewVaughnMax1 = maxResults.stream()
                .anyMatch(p -> "Matthew Vaughn".equals(p.getProducer()) &&
                        p.getInterval() == 22 &&
                        p.getPreviousWin() == 1980 &&
                        p.getFollowingWin() == 2002);

        boolean foundMatthewVaughnMax2 = maxResults.stream()
                .anyMatch(p -> "Matthew Vaughn".equals(p.getProducer()) &&
                        p.getInterval() == 22 &&
                        p.getPreviousWin() == 2015 &&
                        p.getFollowingWin() == 2037);

        assertTrue("Deve encontrar Joel Silver no min (1990→1991)", foundJoelSilverMin);
        assertTrue("Deve encontrar Matthew Vaughn no min (2002→2003)", foundMatthewVaughnMin);
        assertTrue("Deve encontrar Matthew Vaughn no max (1980→2002)", foundMatthewVaughnMax1);
        assertTrue("Deve encontrar Matthew Vaughn no max (2015→2037)", foundMatthewVaughnMax2);

        // Logs para verificação
        System.out.println("✅ Teste da especificação passou!");
        System.out.println("📊 Intervalos mínimos encontrados:");
        for (ProducerPrizesDTO min : minResults) {
            System.out.println("   - " + min.getProducer() + ": " + min.getInterval() + " anos (" + min.getPreviousWin()
                    + "→" + min.getFollowingWin() + ")");
        }
        System.out.println("📊 Intervalos máximos encontrados:");
        for (ProducerPrizesDTO max : maxResults) {
            System.out.println("   - " + max.getProducer() + ": " + max.getInterval() + " anos (" + max.getPreviousWin()
                    + "→" + max.getFollowingWin() + ")");
        }
    }
}