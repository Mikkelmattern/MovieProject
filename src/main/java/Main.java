import jakarta.persistence.EntityManagerFactory;
import mikkelmattern.DTO.MovieSearchResponseDTO;
import mikkelmattern.DTO.PersonSearchResponseDTO;
import mikkelmattern.config.HibernateConfig;
import mikkelmattern.tmdb.TmdbClient;
import mikkelmattern.utils.ConsolePrinter;

public class Main {

    public static void main(String[] args) {
        TmdbClient client = new TmdbClient();

        MovieSearchResponseDTO response = client.searchMovies("Star Wars");

        ConsolePrinter.printMovieSearch(response);
    }
}