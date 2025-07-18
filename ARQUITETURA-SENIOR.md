# 🏗️ Arquitetura Senior - API Golden Raspberry Awards

## 📋 Visão Geral

Transformando o projeto atual em uma arquitetura de nível **Senior Engineer** aplicando padrões e práticas avançadas de engenharia de software.

## 🎯 Estrutura Proposta

```
src/main/java/com/texoit/airton/movieapi/
├── 🌐 presentation/           # Camada de Apresentação
│   ├── controller/
│   ├── dto/
│   ├── validator/
│   └── exception/
├── 🎯 application/           # Camada de Aplicação
│   ├── usecase/
│   ├── service/
│   ├── command/
│   ├── query/
│   └── mapper/
├── 💎 domain/                # Camada de Domínio
│   ├── model/
│   ├── repository/
│   ├── service/
│   ├── specification/
│   └── event/
├── 🔧 infrastructure/        # Camada de Infraestrutura
│   ├── persistence/
│   ├── config/
│   ├── external/
│   └── file/
└── 🔗 shared/                # Componentes Compartilhados
    ├── exception/
    ├── util/
    ├── constant/
    └── annotation/
```

## 🎭 Padrões de Design a Implementar

### 1. **Domain-Driven Design (DDD)**
```java
// Domain Model Rico
public class Producer {
    private ProducerId id;
    private ProducerName name;
    private List<MovieWin> wins;
    
    // Métodos de negócio
    public ProducerInterval calculateMinInterval();
    public ProducerInterval calculateMaxInterval();
    public boolean hasMultipleWins();
}

// Value Objects
public class ProducerInterval {
    private final int years;
    private final Year previousWin;
    private final Year followingWin;
    
    public ProducerInterval(int years, Year previousWin, Year followingWin) {
        if (years <= 0) throw new InvalidIntervalException();
        this.years = years;
        this.previousWin = previousWin;
        this.followingWin = followingWin;
    }
}
```

### 2. **CQRS (Command Query Responsibility Segregation)**
```java
// Commands
public class ImportMoviesCommand {
    private final InputStream csvStream;
    private final String filename;
}

// Queries
public class GetProducerIntervalsQuery {
    private final Optional<String> producerName;
    private final Optional<Integer> year;
}

// Handlers
@Component
public class GetProducerIntervalsQueryHandler {
    public ProducerIntervalsResult handle(GetProducerIntervalsQuery query) {
        // Lógica específica de query
    }
}
```

### 3. **Repository Pattern com Especificações**
```java
// Repository Interface
public interface ProducerRepository extends Repository<Producer, ProducerId> {
    List<Producer> findBySpecification(Specification<Producer> spec);
    Optional<Producer> findByName(ProducerName name);
}

// Specification Pattern
public class ProducerSpecifications {
    public static Specification<Producer> withMultipleWins() {
        return (root, query, cb) -> cb.greaterThan(
            cb.size(root.get("wins")), 1
        );
    }
    
    public static Specification<Producer> withWinInYear(int year) {
        return (root, query, cb) -> cb.equal(
            root.join("wins").get("year"), year
        );
    }
}
```

### 4. **Use Cases (Clean Architecture)**
```java
@UseCase
public class CalculateProducerIntervalsUseCase {
    private final ProducerRepository producerRepository;
    private final ProducerIntervalCalculator calculator;
    private final ProducerIntervalsMapper mapper;
    
    public ProducerIntervalsResponse execute(GetProducerIntervalsQuery query) {
        // 1. Buscar produtores
        List<Producer> producers = producerRepository.findBySpecification(
            ProducerSpecifications.withMultipleWins()
        );
        
        // 2. Calcular intervalos
        ProducerIntervalsResult result = calculator.calculate(producers);
        
        // 3. Mapear para resposta
        return mapper.toResponse(result);
    }
}
```

### 5. **Factory Pattern para Objetos Complexos**
```java
@Component
public class MovieFactory {
    public Movie createFromCsvRecord(CSVRecord record) {
        return Movie.builder()
            .year(Year.of(Integer.parseInt(record.get("year"))))
            .title(MovieTitle.of(record.get("title")))
            .studios(parseStudios(record.get("studios")))
            .producers(parseProducers(record.get("producers")))
            .isWinner(parseWinner(record.get("winner")))
            .build();
    }
}
```

## 🎯 Implementações Específicas

### 1. **Exception Handling Centralizado**
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .code("VALIDATION_ERROR")
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build());
    }
    
    @ExceptionHandler(ProducerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ProducerNotFoundException ex) {
        return ResponseEntity.notFound()
            .body(ErrorResponse.builder()
                .code("PRODUCER_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build());
    }
}
```

### 2. **Validation com Bean Validation**
```java
@RestController
@RequestMapping("/api/v1/producers")
@Validated
public class ProducerController {
    
    @GetMapping("/intervals")
    public ResponseEntity<ProducerIntervalsResponse> getIntervals(
        @Valid GetProducerIntervalsRequest request
    ) {
        // Implementação
    }
}

// Request DTO
public class GetProducerIntervalsRequest {
    @Size(min = 2, max = 50)
    private String producerName;
    
    @Min(1980)
    @Max(2100)
    private Integer year;
}
```

### 3. **Logging Estruturado**
```java
@Component
public class ProducerIntervalCalculator {
    private static final Logger logger = LoggerFactory.getLogger(ProducerIntervalCalculator.class);
    
    public ProducerIntervalsResult calculate(List<Producer> producers) {
        logger.info("Calculating intervals for {} producers", producers.size());
        
        Stopwatch stopwatch = Stopwatch.createStarted();
        
        try {
            ProducerIntervalsResult result = doCalculate(producers);
            
            logger.info("Intervals calculated successfully in {}ms. Min: {}, Max: {}", 
                stopwatch.elapsed(TimeUnit.MILLISECONDS),
                result.getMinIntervals().size(),
                result.getMaxIntervals().size());
            
            return result;
        } catch (Exception e) {
            logger.error("Error calculating intervals", e);
            throw new IntervalCalculationException("Failed to calculate intervals", e);
        }
    }
}
```

### 4. **Configuration Management**
```java
@ConfigurationProperties(prefix = "movieapi")
@Data
@Component
public class MovieApiProperties {
    private Csv csv = new Csv();
    private Calculation calculation = new Calculation();
    
    @Data
    public static class Csv {
        private String delimiter = ";";
        private String encoding = "UTF-8";
        private boolean skipFirstLine = true;
    }
    
    @Data
    public static class Calculation {
        private boolean includeNonConsecutive = false;
        private int maxResults = 100;
    }
}
```

### 5. **Metrics e Observabilidade**
```java
@Component
public class MetricsService {
    private final MeterRegistry meterRegistry;
    private final Timer calculationTimer;
    
    public MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.calculationTimer = Timer.builder("producer.interval.calculation")
            .description("Time taken to calculate producer intervals")
            .register(meterRegistry);
    }
    
    public void recordCalculation(Duration duration, int producerCount) {
        calculationTimer.record(duration);
        meterRegistry.counter("producer.interval.calculated", 
            "producer_count", String.valueOf(producerCount)).increment();
    }
}
```

## 🧪 Testes Avançados

### 1. **Test Slices**
```java
@DataJpaTest
public class ProducerRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private ProducerRepository producerRepository;
    
    @Test
    public void shouldFindProducersWithMultipleWins() {
        // Given
        Producer producer = createProducerWithMultipleWins();
        entityManager.persistAndFlush(producer);
        
        // When
        List<Producer> result = producerRepository.findBySpecification(
            ProducerSpecifications.withMultipleWins()
        );
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo(producer.getName());
    }
}
```

### 2. **Contract Tests**
```java
@SpringBootTest
@AutoConfigureTestRestTemplate
public class ProducerControllerContractTest {
    
    @Test
    public void shouldReturnProducerIntervalsInCorrectFormat() {
        // Given
        setupTestData();
        
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
            "/api/v1/producers/intervals", String.class
        );
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        DocumentContext json = JsonPath.parse(response.getBody());
        assertThat(json.read("$.min", List.class)).isNotEmpty();
        assertThat(json.read("$.max", List.class)).isNotEmpty();
        assertThat(json.read("$.min[0].producer", String.class)).isNotBlank();
        assertThat(json.read("$.min[0].interval", Integer.class)).isPositive();
    }
}
```

## 🔧 Dependências Adicionais

```xml
<dependencies>
    <!-- Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- Metrics -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-registry-prometheus</artifactId>
    </dependency>
    
    <!-- Mappers -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>1.5.3.Final</version>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- Utilities -->
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>31.1-jre</version>
    </dependency>
</dependencies>
```

## 🎯 Benefícios da Arquitetura Senior

### **1. Manutenibilidade**
- Código organizado em camadas bem definidas
- Responsabilidades claramente separadas
- Fácil de entender e modificar

### **2. Testabilidade**
- Interfaces bem definidas
- Injeção de dependência
- Testes unitários e de integração robustos

### **3. Extensibilidade**
- Novos recursos facilmente adicionados
- Padrões consistentes
- Configuração externalizada

### **4. Observabilidade**
- Logs estruturados
- Métricas de performance
- Health checks

### **5. Qualidade**
- Validação centralizada
- Tratamento de exceções
- Documentação automática

## 🚀 Próximos Passos

1. **Implementar Domain Models** ricos
2. **Criar Use Cases** específicos
3. **Adicionar Specifications** para queries
4. **Implementar Mappers** automáticos
5. **Configurar Observabilidade**
6. **Adicionar Testes Avançados**
7. **Documentar Arquitetura**

Esta arquitetura demonstra claramente experiência senior em:
- **Design Patterns**
- **Clean Architecture**
- **Domain-Driven Design**
- **SOLID Principles**
- **Observabilidade**
- **Testabilidade** 