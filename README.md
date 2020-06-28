# spring-boot-skeleton-sample
This is a simple Spring boot skeleton for creating a new REST Application. This project has the all minimum requirements for creating a REST application using sparing boot. 
  ### Technologies/concepts Used
      * Java 8
      * Maven 3
      * Spring Boot 2.2.6
      * Embeded Jetty server
      * Postgress
      * JPA 2.2.6
      * JWT Authentication with spring security 
      * Jersey Rest Client for consuming other rest applications
      * Swagger 2.4.0 for  API Documentation
      * lombok 1.18.8
      * Logging Enabled with Slf4j
      * Simple Login, Sign Up API's
 ## Steps to Setup

**1. Clone the application**

```bash
https://github.com/jayachandra-jai/spring-boot-skeleton-sample.git
```

**2. Create Postgres database**
```bash
create database user_database
```

**3. Change Postgres username and password as per your installation**

+ open `src/main/resources/application.properties`

+ change `spring.datasource.username` and `spring.datasource.password` as per your Postgres installation

**4. Build and run the app using maven**

```bash
mvn clean insatll
mvn package
java -jar target/spring-boot-skeleton-sample-0.0.1-SNAPSHOT.jar

```

Alternatively, you can run the app without packaging it using -

```bash
mvn spring-boot:run
```
**5. Test the application**
+ Add Dummy Admin `curl http://localhost:<PORT>/app/api/user/add-first-admin`
+ Check Swager API Doument `http://localhost:<PORT>/app/swagger-ui.html` 

    
      

