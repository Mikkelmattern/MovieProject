package mikkelmattern.tmdb;

import mikkelmattern.exceptions.ApiException;
import mikkelmattern.utils.Utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TmdbClient {

    private static final String BASE_URL =
            "https://api.themoviedb.org/3";

    private final HttpClient httpClient;
    private final String accessToken;

    public TmdbClient() {this.httpClient = HttpClient.newHttpClient();this.accessToken = Utils.getPropertyValue("TMDB_ACCESS_TOKEN", "config.properties");
    }

    public String get(String endpoint) {HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + endpoint)).header("Authorization", "Bearer " + accessToken).header("Accept", "application/json").GET().build();

        try {HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new ApiException(response.statusCode(), "TMDb request failed: " + response.body());
            }

            return response.body();

        } catch (IOException exception) {
            throw new ApiException(500, "Could not connect to TMDb: " + exception.getMessage());

        } catch (InterruptedException exception) {

            Thread.currentThread().interrupt();
            throw new ApiException(500, "TMDb request was interrupted");
        }
    }
}