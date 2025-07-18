# API Golden Raspberry Awards

## Descrição do Projeto

Esta API RESTful foi desenvolvida para fornecer informações sobre os vencedores da categoria "Pior Filme" do Golden Raspberry Awards. O sistema lê dados de um arquivo CSV e disponibiliza endpoints para consultar informações sobre produtores e seus intervalos entre prêmios.

## Principais Funcionalidades

- **Cálculo de Intervalos**: Calcula o intervalo entre prêmios consecutivos para cada produtor
- **Consulta de Intervalos**: Retorna os produtores com maior e menor intervalo entre prêmios consecutivos
- **Múltiplos Resultados**: Quando há empate nos intervalos, retorna todos os produtores com o mesmo intervalo
- **Validação de Dados**: Processa apenas registros marcados como vencedores ("yes")

## Tecnologias Utilizadas

- **Java 8+**
- **Spring Boot 2.7.0**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **OpenCSV** (leitura de arquivos CSV)
- **Swagger/OpenAPI 3** (documentação)
- **JUnit 5** (testes de integração)
- **Maven** (gerenciamento de dependências)

## Pré-requisitos

- Java 8 ou superior
- Maven 3.6 ou superior
- Git (opcional, para clonar o repositório)

## Como Rodar o Projeto

### 1. Clonar o Repositório (se aplicável)
```bash
git clone [URL_DO_REPOSITORIO]
cd API-GoldenRaspberryAwards
```

### 2. Compilar o Projeto
```bash
mvn clean compile
```

### 3. Executar os Testes
```bash
mvn test
```

### 4. Executar a Aplicação
```bash
mvn spring-boot:run
```

**Ou executar o JAR:**
```bash
mvn clean package
java -jar target/movieapi-0.0.1-SNAPSHOT.jar
```

### 5. Acessar a Aplicação
- **URL Base**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Console H2**: http://localhost:8080/h2-console
  - **JDBC URL**: jdbc:h2:mem:testdb
  - **Username**: sa
  - **Password**: (deixar em branco)

## Endpoints Disponíveis

### Endpoint Principal (Especificação)
- **GET** `/api/producer/interval-prizes`
  - Retorna os produtores com maior e menor intervalo entre prêmios consecutivos
  - **Resposta exemplo**:
    ```json
    {
      "min": [
        {
          "producer": "Producer 1",
          "interval": 1,
          "previousWin": 2008,
          "followingWin": 2009
        }
      ],
      "max": [
        {
          "producer": "Producer 2",
          "interval": 99,
          "previousWin": 1900,
          "followingWin": 1999
        }
      ]
    }
    ```

### Outros Endpoints
- **GET** `/api/movie/{year}` - Buscar filmes por ano
- **GET** `/api/movie/years` - Listar anos com múltiplos vencedores
- **GET** `/api/studio/winners` - Listar estúdios vencedores
- **DELETE** `/api/movie/{id}` - Deletar filme por ID

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/texoit/airton/movieapi/
│   │   ├── presentation/          # Controladores REST
│   │   ├── application/           # Casos de uso
│   │   ├── domain/               # Entidades e value objects
│   │   ├── infrastructure/       # Repositórios e configurações
│   │   └── shared/               # Utilitários compartilhados
│   └── resources/
│       ├── movielist.csv         # Dados dos filmes
│       └── application.properties # Configurações
└── test/
    └── java/                     # Testes de integração
```

## Dados de Entrada

O sistema lê dados do arquivo `src/main/resources/movielist.csv` com as seguintes colunas:
- **year**: Ano do filme
- **title**: Título do filme
- **studios**: Estúdios produtores
- **producers**: Produtores (separados por vírgula, "and" ou "e")
- **winner**: Indicador se ganhou o prêmio ("yes" para vencedor)

## Regras de Negócio

1. **Intervalos Consecutivos**: Apenas prêmios consecutivos são considerados para o cálculo
2. **Múltiplos Produtores**: Quando há empate no intervalo, todos os produtores são retornados
3. **Parsing de Produtores**: Suporta separação por vírgula, "and" e "e"
4. **Validação**: Apenas registros com winner="yes" são processados

## Testes

### Executar Testes de Integração
```bash
mvn test
```

### Casos de Teste Incluídos
- **Teste de Intervalos**: Valida cálculo correto de intervalos mínimos e máximos
- **Teste de Múltiplos Resultados**: Verifica retorno de todos os produtores em caso de empate
- **Teste de Dados Inválidos**: Verifica tratamento de dados inconsistentes

## Configurações

### Banco de Dados H2
- **Tipo**: In-memory
- **Inicialização**: Automática com dados do CSV
- **Console**: Disponível em desenvolvimento

### Logging
- **Nível**: INFO
- **Padrão**: Logs estruturados para monitoramento

## Troubleshooting

### Problemas Comuns

1. **Erro de Memória**:
   ```bash
   java -Xmx512m -jar target/movieapi-0.0.1-SNAPSHOT.jar
   ```

2. **Porta em Uso**:
   ```bash
   java -jar target/movieapi-0.0.1-SNAPSHOT.jar --server.port=8081
   ```

3. **Arquivo CSV não encontrado**:
   - Verificar se o arquivo `movielist.csv` está em `src/main/resources/`

### Logs Importantes
- Inicialização do banco: `INFO com.texoit.airton.movieapi.infrastructure.config.DataInitializer`
- Erros de parsing: `ERROR com.texoit.airton.movieapi.infrastructure.service.CsvReaderService`

## Arquitetura

O projeto segue os princípios de **Clean Architecture** com separação clara de responsabilidades:

- **Presentation Layer**: Controllers REST
- **Application Layer**: Use Cases e serviços de aplicação
- **Domain Layer**: Entidades e regras de negócio
- **Infrastructure Layer**: Repositórios e integrações externas

## Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

## Autor

Desenvolvido por [Seu Nome]

## Contato

- Email: [seu-email@example.com]
- LinkedIn: [seu-linkedin]
- GitHub: [seu-github] 