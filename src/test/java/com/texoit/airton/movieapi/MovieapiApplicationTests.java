package com.texoit.airton.movieapi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.texoit.airton.movieapi.infrastructure.persistence.MovieProducerRepositoryTest;

/**
 * Suite completa de testes de integração.
 * Conforme especificação: "Devem ser implementados somente testes de
 * integração."
 * Garante que os dados obtidos estão de acordo com os dados fornecidos na
 * proposta.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        // 🔧 Infrastructure Layer Tests (Integration Tests)
        MovieProducerRepositoryTest.class,

        // 🌐 Presentation Layer Tests (Integration Tests)
        MovieControllerIntegrationTest.class,
        ProducerControllerIntegrationTest.class,
        StudioControllerIntegrationTest.class,

        // 🧪 Scenario Tests (Integration Tests)
        ProducerScenarioTest.class,
        ProducerIntervalsSpecificationTest.class
})
public class MovieapiApplicationTests {

}
