# MovieProject

MovieProject is a Java application that collects movie data from the TMDb API and stores it in a PostgreSQL database.

The main purpose of the project is to build a local movie database containing information about movies, genres, actors, and directors. Data is retrieved from TMDb, converted from JSON into Java objects, and persisted using Hibernate and JPA.

Once the data has been imported, the application can search the local database for movies by title and for actors or directors by name. This makes it possible to perform searches without requesting the same information from TMDb every time.

The project demonstrates external API integration, JSON mapping, relational database design, object-relational mapping, DAO architecture, service layers, and automated testing.

## Technologies

The project is built with Java and Maven and uses:

- TMDb API for movie data
- PostgreSQL for persistent storage
- Hibernate and JPA for database communication
- Jackson for JSON conversion
- JUnit and Mockito for testing
- Lombok for reducing boilerplate code
