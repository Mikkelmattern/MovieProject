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

public class MovieService {

    private final EntityManagerFactory emf;
    MovieDTO dto = new MovieDTO();
    private final MovieDAOImpl movieDAO = new MovieDAOImpl(emf);
    private final GenreDAOImpl genreDAO = new GenreDAOImpl(emf);
    private final ObjectMapper objectMapper = new ObjectMapper();
    public Movie fetchAndSaveMovie(String json) throws JsonProcessingException {
        MovieDTO dto = objectMapper.readValue(json, MovieDTO.class);
        Movie movie = toEntity();
        return movieDAO.save(movie);
    }

    private final TmdbClient tmdbClient;

    public MovieService(TmdbClient tmdbClient, MovieDAOImpl movieDAO, EntityManagerFactory emf) {
        this.tmdbClient = tmdbClient;
        this.emf = emf;
    }
    public Movie fetchAndSaveMovie(long movieId) {
        MovieDTO dto = tmdbClient.getMovie(movieId);

        Movie movie = toEntity(dto);
    }

    private Movie toEntity(){

        List<Genre> genreList = dto.getGenre()
                .stream()
                .map(genreDTO -> genreDAO.find(genreDTO.getId()))
                .filter(Objects::nonNull)
                .toList();

        return Movie.builder()
                .id(dto.getId())
                .adult(dto.isAdult())
                .title(dto.getTitle())
                .tagline(dto.getTagline())
                .budget(dto.getBudget())
                .genres(genreList)
                .revenue(dto.getRevenue())
                .runtime(dto.getRuntime())
                .voteAverage(dto.getVoteAverage())
                .build();
    }
}
