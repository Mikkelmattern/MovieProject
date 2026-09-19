package mikkelmattern.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonSearchResultDTO {

    private Long id;
    private String name;

    @JsonProperty("known_for_department")
    private String knownForDepartment;

    private Double popularity;

    @JsonProperty("profile_path")
    private String profilePath;
}