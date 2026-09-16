package mikkelmattern.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Movie {

    @Id
    private Long id;

    private String title;

    private String originalTitle;

    private String originalLanguage;

    private boolean adult;

    private boolean video;

    private String overview;

    private String tagline;

    private String status;

    private String homepage;

    private String imdbId;

    private String releaseDate;

    private Long budget;

    private Long revenue;

    private Integer runtime;

    private Double popularity;

    private Double voteAverage;

    private Integer voteCount;

    private String belongsToCollection;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @ElementCollection
    @CollectionTable(name = "movie_production_companies", joinColumns = @JoinColumn(name = "movie_id"))
    private List<String> productionCompanies;

    @ElementCollection
    @CollectionTable(name = "movie_production_countries", joinColumns = @JoinColumn(name = "movie_id"))
    private List<String> productionCountries;

    @ElementCollection
    @CollectionTable(name = "movie_spoken_languages", joinColumns = @JoinColumn(name = "movie_id"))
    private List<String> spokenLanguages;

    @Builder
    public Movie(String title, String originalTitle, String originalLanguage, boolean adult, boolean video,
                 String overview, String tagline, String status, String homepage, String imdbId,
                 String releaseDate, Long budget, Long revenue, Integer runtime, Double popularity,
                 Double voteAverage, Integer voteCount, String belongsToCollection, List<Genre> genres,
                 List<String> productionCompanies, List<String> productionCountries, List<String> spokenLanguages) {
        this.title = title;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.adult = adult;
        this.video = video;
        this.overview = overview;
        this.tagline = tagline;
        this.status = status;
        this.homepage = homepage;
        this.imdbId = imdbId;
        this.releaseDate = releaseDate;
        this.budget = budget;
        this.revenue = revenue;
        this.runtime = runtime;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.belongsToCollection = belongsToCollection;
        this.genres = genres;
        this.productionCompanies = productionCompanies;
        this.productionCountries = productionCountries;
        this.spokenLanguages = spokenLanguages;
    }
}