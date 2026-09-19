package mikkelmattern.tmdb;

import mikkelmattern.DTO.CreditsDTO;
import mikkelmattern.DTO.MovieDTO;
import mikkelmattern.exceptions.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TmdbClientTest {

    private StubTmdbClient tmdbClient;

    @BeforeEach
    void setUp() {
        tmdbClient = new StubTmdbClient();
    }

    @Test
    void shouldConvertMovieResponse() {
        tmdbClient.responseJson = """
                {
                  "id": 11,
                  "title": "Star Wars",
                  "release_date": "1977-05-25",
                  "vote_average": 8.207,
                  "genres": [{"id": 12, "name": "Adventure"}]
                }
                """;

        MovieDTO movie = tmdbClient.getMovie(11L);

        assertAll(
                () -> assertEquals(11L, movie.getId()),
                () -> assertEquals("Star Wars", movie.getTitle()),
                () -> assertEquals("1977-05-25", movie.getReleaseDate()),
                () -> assertEquals(1, movie.getGenres().size()),
                () -> assertEquals("/movie/11?language=en-US", tmdbClient.requestedEndpoint)
        );
    }

    @Test
    void shouldConvertCreditsResponse() {
        tmdbClient.responseJson = """
                {
                  "id": 11,
                  "cast": [{"id": 2, "name": "Mark Hamill", "character": "Luke Skywalker"}],
                  "crew": [{"id": 1, "name": "George Lucas", "job": "Director"}]
                }
                """;

        CreditsDTO credits = tmdbClient.getCredits(11L);

        assertEquals("Mark Hamill", credits.getCast().get(0).getName());
        assertEquals("Director", credits.getCrew().get(0).getJob());
        assertEquals("/movie/11/credits?language=en-US", tmdbClient.requestedEndpoint);
    }

    @Test
    void shouldEncodeMovieSearchText() {
        tmdbClient.responseJson = emptySearchResponse();

        tmdbClient.searchMovies("  Star Wars  ");

        assertTrue(tmdbClient.requestedEndpoint.contains("query=Star+Wars"));
    }

    @Test
    void shouldEncodePersonSearchText() {
        tmdbClient.responseJson = emptySearchResponse();

        tmdbClient.searchPeople("  George Lucas  ");

        assertTrue(tmdbClient.requestedEndpoint.contains("query=George+Lucas"));
    }

    @Test
    void shouldRejectBlankMovieSearch() {
        assertThrows(
                IllegalArgumentException.class,
                () -> tmdbClient.searchMovies("   ")
        );
        assertNull(tmdbClient.requestedEndpoint);
    }

    @Test
    void shouldRejectNullPersonSearch() {
        assertThrows(
                IllegalArgumentException.class,
                () -> tmdbClient.searchPeople(null)
        );
        assertNull(tmdbClient.requestedEndpoint);
    }

    @Test
    void shouldThrowApiExceptionForInvalidMovieJson() {
        tmdbClient.responseJson = "not-json";

        ApiException exception = assertThrows(
                ApiException.class,
                () -> tmdbClient.getMovie(11L)
        );

        assertEquals(500, exception.getCode());
        assertTrue(exception.getMessage().contains("Could not convert movie JSON"));
    }

    private String emptySearchResponse() {
        return "{\"page\":1,\"results\":[],\"total_results\":0,\"total_pages\":0}";
    }

    private static class StubTmdbClient extends TmdbClient {
        private String responseJson;
        private String requestedEndpoint;

        @Override
        public String get(String endpoint) {
            requestedEndpoint = endpoint;
            return responseJson;
        }
    }
}
