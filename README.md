# API-GoldenRaspberryAwards

RESTful API for querying and maintaining the list of nominees and winners for the Worst Film category at the Golden Raspberry Awards. The API was created to meet Richardson maturity level 2.

## Requirements

To execute the project, you need to install JDK 8.

## Settings

- By default, the application is configured with servlet.contextPath=/api. To change it, open the application.properties file and change the property value.

    # Context
    server.servlet.contextPath=/api

- To change database settings with URL, user, password and console url, and enable/disable the console, open the application.properties file. The H2 database options and datasource properties are displayed as below:

    # H2
    spring.h2.console.enabled=true
    spring.h2.console.path=/h2
    
    # Datasource
    spring.datasource.url=jdbc:h2:mem:testdb
    spring.datasource.username=sa
    spring.datasource.password=
    spring.datasource.driver-class-name=org.h2.Driver
    spring.jpa.hibernate.ddl-auto=update

# To execute the project

- To execute the project, no external installation is required. When started, the application creates the database and populates it with data from the movielist.csv file, which is located in src/main/resources/.

1. Clone the repository or download it;
2. If you are using a tool external to the IDE, import the project as an existing Maven project;
3. Run the Maven command below:

    $ mvn install -Dmaven.test.skip=true

4. Para iniciar a aplicação clique no projeto com o botão direito do mouse, vá até a opção Run As e selecione Spring Boot App.

# EndPoints

To see the list of available REST calls, their parameters, HTTP response codes, and return type, start the application and go to: http://localhost:8080/api/swagger-ui.html#/   

# Tests

To run the tests, open the AppTest.java class, click Run -> Run As -> JUnit Test. This will cause all implemented integration tests to run.

