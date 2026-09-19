package mikkelmattern.utils;

import mikkelmattern.DTO.MovieSearchResponseDTO;
import mikkelmattern.DTO.MovieSearchResultDTO;
import mikkelmattern.DTO.PersonSearchResponseDTO;
import mikkelmattern.DTO.PersonSearchResultDTO;

import java.util.List;

public final class ConsolePrinter {

    private ConsolePrinter() {}

    public static void printMovieSearch(
            MovieSearchResponseDTO response
    ) {
        List<MovieSearchResultDTO> movies =
                response.getResults();

        if (movies == null || movies.isEmpty()) {
            System.out.println("Ingen film fundet.");
            return;
        }

        System.out.println();
        System.out.println("FILMRESULTATER");
        System.out.println(
                "────────────────────────────────────────────────────────────────────────────"
        );

        System.out.printf(
                "%-4s %-10s %-42s %-12s %-8s%n",
                "Nr.",
                "TMDb-ID",
                "Titel",
                "Dato",
                "Rating"
        );

        System.out.println(
                "────────────────────────────────────────────────────────────────────────────"
        );

        for (int i = 0; i < movies.size(); i++) {
            MovieSearchResultDTO movie = movies.get(i);

            System.out.printf(
                    "%-4d %-10d %-42s %-12s %-8s%n",
                    i + 1,
                    movie.getId(),
                    shorten(movie.getTitle(), 40),
                    valueOrDash(movie.getReleaseDate()),
                    formatRating(movie.getVoteAverage())
            );
        }

        System.out.println(
                "────────────────────────────────────────────────────────────────────────────"
        );

        System.out.printf(
                "Viser %d af %d resultater%n%n",
                movies.size(),
                response.getTotalResults()
        );
    }

    public static void printPersonSearch(
            PersonSearchResponseDTO response
    ) {
        List<PersonSearchResultDTO> people =
                response.getResults();

        if (people == null || people.isEmpty()) {
            System.out.println("Ingen personer fundet.");
            return;
        }

        System.out.println();
        System.out.println("PERSONRESULTATER");
        System.out.println(
                "────────────────────────────────────────────────────────────────────"
        );

        System.out.printf(
                "%-4s %-10s %-35s %-20s%n",
                "Nr.",
                "TMDb-ID",
                "Navn",
                "Afdeling"
        );

        System.out.println(
                "────────────────────────────────────────────────────────────────────"
        );

        for (int i = 0; i < people.size(); i++) {
            PersonSearchResultDTO person = people.get(i);

            System.out.printf(
                    "%-4d %-10d %-35s %-20s%n",
                    i + 1,
                    person.getId(),
                    shorten(person.getName(), 33),
                    valueOrDash(
                            person.getKnownForDepartment()
                    )
            );
        }

        System.out.println(
                "────────────────────────────────────────────────────────────────────"
        );

        System.out.printf(
                "Viser %d af %d resultater%n%n",
                people.size(),
                response.getTotalResults()
        );
    }

    private static String shorten(
            String text,
            int maximumLength
    ) {
        if (text == null) {
            return "-";
        }

        if (text.length() <= maximumLength) {
            return text;
        }

        return text.substring(
                0,
                maximumLength - 3
        ) + "...";
    }

    private static String valueOrDash(String value) {
        if (value == null || value.isBlank()) {
            return "-";
        }

        return value;
    }

    private static String formatRating(Double rating) {
        if (rating == null) {
            return "-";
        }

        return String.format("%.1f", rating);
    }
}