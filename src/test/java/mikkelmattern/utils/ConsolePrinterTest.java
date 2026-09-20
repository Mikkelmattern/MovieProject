package mikkelmattern.utils;

import mikkelmattern.DTO.MovieSearchResponseDTO;
import mikkelmattern.DTO.MovieSearchResultDTO;
import mikkelmattern.DTO.PersonSearchResponseDTO;
import mikkelmattern.DTO.PersonSearchResultDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsolePrinterTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream output;

    @BeforeEach
    void captureOutput() {
        originalOut = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
    }

    @Test
    void shouldPrintMovieSearchResults() {
        MovieSearchResultDTO movie = new MovieSearchResultDTO();
        movie.setId(11L);
        movie.setTitle("Star Wars");
        movie.setReleaseDate("1977-05-25");
        movie.setVoteAverage(8.207);
        MovieSearchResponseDTO response = new MovieSearchResponseDTO();
        response.setResults(List.of(movie));
        response.setTotalResults(1);

        ConsolePrinter.printMovieSearch(response);

        String printed = output.toString(StandardCharsets.UTF_8);
        assertTrue(printed.contains("FILMRESULTATER"));
        assertTrue(printed.contains("Star Wars"));
        assertTrue(printed.contains("8,2") || printed.contains("8.2"));
    }

    @Test
    void shouldPrintMessageWhenNoMoviesAreFound() {
        MovieSearchResponseDTO response = new MovieSearchResponseDTO();
        response.setResults(List.of());

        ConsolePrinter.printMovieSearch(response);

        assertTrue(output.toString(StandardCharsets.UTF_8).contains("Ingen film fundet."));
    }

    @Test
    void shouldPrintPersonSearchResults() {
        PersonSearchResultDTO person = new PersonSearchResultDTO();
        person.setId(1L);
        person.setName("George Lucas");
        person.setKnownForDepartment("Directing");
        PersonSearchResponseDTO response = new PersonSearchResponseDTO();
        response.setResults(List.of(person));
        response.setTotalResults(1);

        ConsolePrinter.printPersonSearch(response);

        String printed = output.toString(StandardCharsets.UTF_8);
        assertTrue(printed.contains("PERSONRESULTATER"));
        assertTrue(printed.contains("George Lucas"));
        assertTrue(printed.contains("Directing"));
    }

    @Test
    void shouldPrintMessageWhenNoPeopleAreFound() {
        PersonSearchResponseDTO response = new PersonSearchResponseDTO();
        response.setResults(null);

        ConsolePrinter.printPersonSearch(response);

        assertTrue(output.toString(StandardCharsets.UTF_8).contains("Ingen personer fundet."));
    }
}
