package mikkelmattern.service;

import mikkelmattern.DTO.CreditsDTO;
import mikkelmattern.DTO.CrewDTO;
import mikkelmattern.DTO.GenreDTO;
import mikkelmattern.DTO.MovieDTO;
import mikkelmattern.dao.GenreDAOImpl;
import mikkelmattern.dao.MovieDAOImpl;
import mikkelmattern.entities.Genre;
import mikkelmattern.entities.Movie;
import mikkelmattern.tmdb.TmdbClient;

import java.util.List;

public class MovieService {

    private final TmdbClient tmdbClient;
    private final MovieDAOImpl movieDAO;
    private final GenreDAOImpl genreDAO;

    public MovieService(
        TmdbClient tmdbClient,
        MovieDAOImpl movieDAO,
        GenreDAOImpl genreDAO
    ) {
        this.tmdbClient = tmdbClient;
        this.movieDAO = movieDAO;
        this.genreDAO = genreDAO;
    }

    public Movie fetchAndSaveMovie(long movieId) {
        MovieDTO dto = tmdbClient.getMovie(movieId);

        Movie movie = toEntity(dto);

        Movie existing = movieDAO.find(movieId);

        if (existing == null) {
            return movieDAO.save(movie);
        }

        return movieDAO.update(movie);
    }

    public List<CrewDTO> getDirectors(long movieId) {
        CreditsDTO credits = tmdbClient.getCredits(movieId);

        if (credits.getCrew() == null) {
            return List.of();
        }

        return credits.getCrew().stream()
                .filter(member ->
                    "Director".equals(member.getJob()))
                    .toList();
    }

    private Movie toEntity(MovieDTO dto) {
        List<Genre> genres = dto.getGenres() == null
            ? List.of()
            : dto.getGenres().stream()
            .map(this::findOrSaveGenre)
            .toList();

        return Movie.builder()
            .id(dto.getId())
            .title(dto.getTitle())
            .adult(dto.isAdult())
            .tagline(dto.getTagline())
            .budget(dto.getBudget())
            .revenue(dto.getRevenue())
            .runtime(dto.getRuntime())
            .releaseDate(dto.getReleaseDate())
            .popularity(dto.getPopularity())
            .voteAverage(dto.getVoteAverage())
            .genres(genres)
            .build();
    }

    private Genre findOrSaveGenre(GenreDTO dto) {
        Genre existing = genreDAO.find(dto.getId());

        if (existing != null) {
            return existing;
        }

        Genre genre = new Genre(
            dto.getId(),
            dto.getName()
        );

        return genreDAO.save(genre);
    }
}