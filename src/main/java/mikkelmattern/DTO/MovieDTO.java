package mikkelmattern.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class MovieDTO {

    private Long id;
    private String title;
    private boolean adult;
    private String tagline;
    private Long budget;
    private Long revenue;
    private Integer runtime;

    @JsonProperty("release_year")
    private String releaseDate;

    @JsonProperty("vote_average")
    private double voteAverage;

    private List<GenreDTO> genre;

}