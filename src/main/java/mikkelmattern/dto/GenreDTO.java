package mikkelmattern.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mikkelmattern.entities.Genre;

import java.util.List;

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public class GenreDTO {

        private List<Genre> genres;
    }
