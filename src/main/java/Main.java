import jakarta.persistence.EntityManagerFactory;
import mikkelmattern.DTO.MovieSearchResponseDTO;
import mikkelmattern.DTO.PersonSearchResponseDTO;
import mikkelmattern.config.HibernateConfig;
import mikkelmattern.service.MovieService;
import mikkelmattern.tmdb.TmdbClient;
import mikkelmattern.utils.ConsolePrinter;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        TmdbClient client = new TmdbClient();

        MovieService movieService = new MovieService(client, emf);
        for (long l = 1L; l < 100; l++) {
        }
        long movieId = 640146;
        movieService.fetchAndSaveMovie(movieId);

        MovieSearchResponseDTO response = client.searchMovies("Star Wars");

        ConsolePrinter.printMovieSearch(response);
    }
}