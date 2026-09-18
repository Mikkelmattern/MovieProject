package mikkelmattern.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class CreditsDTO {

    private Long id;
    private List<CastDTO> cast;
    private List<CrewDTO> crew;
}
