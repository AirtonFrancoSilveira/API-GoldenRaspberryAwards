package com.texoit.airton.movieapi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.texoit.airton.movieapi.application.usecase.CalculateProducerIntervalsUseCaseTest;
import com.texoit.airton.movieapi.domain.model.ProducerIntervalTest;
import com.texoit.airton.movieapi.infrastructure.persistence.MovieProducerRepositoryTest;

/**
 * Suite completa de testes seguindo arquitetura senior.
 * Inclui testes de todas as camadas: Domain, Application, Infrastructure e
 * Presentation.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        // 💎 Domain Layer Tests
        ProducerIntervalTest.class,

        // 🎯 Application Layer Tests
        CalculateProducerIntervalsUseCaseTest.class,

        // 🔧 Infrastructure Layer Tests
        MovieProducerRepositoryTest.class,

        // 🌐 Presentation Layer Tests (Integration Tests)
        MovieControllerIntegrationTest.class,
        ProducerControllerIntegrationTest.class,
        StudioControllerIntegrationTest.class,

        // 🧪 Scenario Tests
        ProducerScenarioTest.class
})
public class MovieapiApplicationTests {

}
