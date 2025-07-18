package com.texoit.airton.movieapi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.texoit.airton.movieapi.infrastructure.persistence.MovieProducerRepositoryTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MovieProducerRepositoryTest.class,
        MovieControllerIntegrationTest.class,
        ProducerControllerIntegrationTest.class,
        StudioControllerIntegrationTest.class,
        ProducerScenarioTest.class,
        ProducerIntervalsSpecificationTest.class
})
public class MovieapiApplicationTests {

}
