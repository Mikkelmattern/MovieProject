package mikkelmattern.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = "genres")
public class Movie {

    @Id
    private Long id;

    private String title;

    private boolean adult;

    private String tagline;

    private Long budget;

    private Long revenue;

    private Integer runtime;

    private String releaseDate;

    private Double popularity;

    private Double voteAverage;

    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns =
            @JoinColumn(name = "movie_id"),
            inverseJoinColumns =
            @JoinColumn(name = "genre_id")
    )
    @Builder.Default
    private List<Genre> genres =
            new ArrayList<>();
}