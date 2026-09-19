package mikkelmattern.tmdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mikkelmattern.DTO.CreditsDTO;
import mikkelmattern.DTO.MovieDTO;
import mikkelmattern.DTO.MovieSearchResponseDTO;
import mikkelmattern.DTO.PersonSearchResponseDTO;
import mikkelmattern.exceptions.ApiException;
import mikkelmattern.utils.Utils;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class TmdbClient {

    // https://api.themoviedb.org/3/movie/1?language=en-US
    private static final String BASE_URL = "https://api.themoviedb.org/3";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String accessToken;

    public TmdbClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.accessToken = Utils.getPropertyValue("TMDB_ACCESS_TOKEN", "config.properties");
    }

    public MovieDTO getMovie(long movieId) {
        String json = get("/movie/" + movieId + "?language=en-US");

        try {
            return objectMapper.readValue(json, MovieDTO.class);
        } catch (JsonProcessingException exception) {
            throw new ApiException(500, "Could not convert movie JSON: " + exception.getMessage());
        }
    }

    public CreditsDTO getCredits(long movieId) {
        String json = get("/movie/" + movieId + "/credits?language=en-US");

        try {
            return objectMapper.readValue(json, CreditsDTO.class);
        } catch (JsonProcessingException exception) {
            throw new ApiException(500, "Could not convert credits JSON: " + exception.getMessage());
        }
    }

    public String get(String endpoint) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json").GET().build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new ApiException(response.statusCode(), "TMDb request failed: " + response.body());
            }
            return response.body();

        } catch (IOException exception) {throw new ApiException(500, "Could not connect to TMDb: " + exception.getMessage());

        } catch (InterruptedException exception) {Thread.currentThread().interrupt();
            throw new ApiException(500, "TMDb request was interrupted");
        }
    }

    public MovieSearchResponseDTO searchMovies(
            String searchText
    ) {
        if (searchText == null || searchText.isBlank()) {
            throw new IllegalArgumentException(
                    "Search text cannot be empty"
            );
        }

        String encodedSearchText = URLEncoder.encode(
                searchText.trim(),
                StandardCharsets.UTF_8
        );

        String json = get(
                "/search/movie"
                        + "?query=" + encodedSearchText
                        + "&language=en-US"
                        + "&include_adult=false"
        );

        try {
            return objectMapper.readValue(
                    json,
                    MovieSearchResponseDTO.class
            );
        } catch (JsonProcessingException exception) {
            throw new ApiException(
                    500,
                    "Could not convert search JSON: "
                            + exception.getMessage()
            );
        }
    }

    public PersonSearchResponseDTO searchPeople(
            String searchText
    ) {
        if (searchText == null || searchText.isBlank()) {
            throw new IllegalArgumentException(
                    "Search text cannot be empty"
            );
        }

        String encodedSearchText = URLEncoder.encode(
                searchText.trim(),
                StandardCharsets.UTF_8
        );

        String json = get(
                "/search/person"
                        + "?query=" + encodedSearchText
                        + "&language=en-US"
                        + "&include_adult=false"
        );

        try {
            return objectMapper.readValue(
                    json,
                    PersonSearchResponseDTO.class
            );
        } catch (JsonProcessingException exception) {
            throw new ApiException(
                    500,
                    "Could not convert person search JSON: "
                            + exception.getMessage()
            );
        }
    }
}