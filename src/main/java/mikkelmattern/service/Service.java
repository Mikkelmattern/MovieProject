package mikkelmattern.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import mikkelmattern.DTO.MovieDTO;
import mikkelmattern.dao.GenreDAOImpl;
import mikkelmattern.dao.MovieDAOImpl;
import mikkelmattern.entities.Genre;
import mikkelmattern.entities.Movie;
import mikkelmattern.tmdb.TmdbClient;

import java.util.List;
import java.util.Objects;

public abstract class Service {

    private final EntityManagerFactory emf;
    MovieDTO dto = new MovieDTO();
    private final MovieDAOImpl movieDAO;
    private final GenreDAOImpl genreDAO;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public Movie fetchAndSaveMovie(String json) throws JsonProcessingException {
        MovieDTO dto = objectMapper.readValue(json, MovieDTO.class);
        Movie movie = toEntity();
        return movieDAO.save(movie);
    }

    private final TmdbClient tmdbClient;

    public Service(TmdbClient tmdbClient, EntityManagerFactory emf) {
        this.tmdbClient = tmdbClient;
        this.emf = emf;
        this.movieDAO = new MovieDAOImpl(emf);
        this.genreDAO = new GenreDAOImpl(emf);
    }

    abstract Movie toEntity();
}
