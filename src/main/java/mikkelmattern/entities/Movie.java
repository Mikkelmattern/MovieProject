package mikkelmattern.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    private String backdropPath;

    private String posterPath;

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
}