package mikkelmattern.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Actor {

    @Id
    private Long id;

    private String name;

    private boolean adult;

    @ElementCollection
    @CollectionTable(name = "actor_also_known_as", joinColumns = @JoinColumn(name = "actor_id"))
    private List<String> alsoKnownAs;

    private String biography;

    private String birthday;

    private String deathday;

    private int gender;

    private String homepage;

    private String imdbId;

    private String knownForDepartment;

    private String placeOfBirth;

    private double popularity;

    private String profilePath;
}