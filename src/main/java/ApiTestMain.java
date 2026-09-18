import mikkelmattern.DTO.CreditsDTO;
import mikkelmattern.DTO.CrewDTO;
import mikkelmattern.DTO.MovieDTO;
import mikkelmattern.tmdb.TmdbClient;

import java.util.List;

public class ApiTestMain {

    public static void main(String[] args) {
        TmdbClient tmdbClient = new TmdbClient();

        long movieId = 11;

        MovieDTO movie =
                tmdbClient.getMovie(movieId);

        CreditsDTO credits =
                tmdbClient.getCredits(movieId);

        List<CrewDTO> directors =
                credits.getCrew() == null
                        ? List.of()
                        : credits.getCrew().stream()
                        .filter(member ->
                                "Director".equals(
                                        member.getJob()
                                )
                        )
                        .toList();

        System.out.println("Film:");
        System.out.println("ID: " + movie.getId());
        System.out.println("Titel: " + movie.getTitle());
        System.out.println(
                "Udgivelsesdato: "
                        + movie.getReleaseDate()
        );
        System.out.println(
                "Rating: "
                        + movie.getVoteAverage()
        );
        System.out.println(
                "Popularitet: "
                        + movie.getPopularity()
        );
        System.out.println(
                "Genrer: "
                        + movie.getGenres()
        );

        System.out.println("\nInstruktører:");

        directors.forEach(director ->
                System.out.println(director.getName())
        );

        int castCount = credits.getCast() == null
                ? 0
                : credits.getCast().size();

        System.out.println(
                "\nAntal cast-medlemmer: " + castCount
        );
    }
}