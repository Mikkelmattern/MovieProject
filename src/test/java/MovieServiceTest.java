import mikkelmattern.DTO.CreditsDTO;
import mikkelmattern.DTO.CrewDTO;
import mikkelmattern.DTO.GenreDTO;
import mikkelmattern.DTO.MovieDTO;
import mikkelmattern.dao.GenreDAOImpl;
import mikkelmattern.dao.MovieDAOImpl;
import mikkelmattern.entities.Genre;
import mikkelmattern.entities.Movie;
import mikkelmattern.service.MovieService;
import mikkelmattern.tmdb.TmdbClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    private TmdbClient tmdbClient;
    private MovieDAOImpl movieDAO;
    private GenreDAOImpl genreDAO;
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        tmdbClient = mock(TmdbClient.class);
        movieDAO = mock(MovieDAOImpl.class);
        genreDAO = mock(GenreDAOImpl.class);

        movieService = new MovieService(
            tmdbClient,
            movieDAO,
            genreDAO
        );
    }

    @Test
    void shouldOnlyReturnDirectors() {
        CrewDTO director = createCrewMember(
            1L,
            "George Lucas",
            "Director"
        );

        CrewDTO composer = createCrewMember(
            2L,
            "John Williams",
            "Original Music Composer"
        );

        CrewDTO producer = createCrewMember(
            3L,
            "Gary Kurtz",
            "Producer"
        );

        CreditsDTO credits = new CreditsDTO();
        credits.setCrew(
            List.of(director, composer, producer)
        );

        when(tmdbClient.getCredits(11L)).thenReturn(credits);

        List<CrewDTO> result = movieService.getDirectors(11L);

        assertEquals(1, result.size());

        assertEquals(
            "George Lucas",
            result.get(0).getName()
        );

        verify(tmdbClient).getCredits(11L);
    }

    private CrewDTO createCrewMember(
        Long id,
        String name,
        String job
    ) {
        CrewDTO crewMember = new CrewDTO();
        crewMember.setId(id);
        crewMember.setName(name);
        crewMember.setJob(job);

        return crewMember;
    }

    @Test
    void shouldSaveNewMovie() {
        MovieDTO dto = createMovieDTO();

        Genre existingGenre = new Genre(
                12L,
                "Adventure"
        );

        when(tmdbClient.getMovie(11L))
                .thenReturn(dto);

        when(genreDAO.find(12L))
                .thenReturn(existingGenre);

        when(movieDAO.find(11L))
                .thenReturn(null);

        when(movieDAO.save(any(Movie.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        Movie result =
                movieService.fetchAndSaveMovie(11L);

        assertNotNull(result);
        assertEquals(11L, result.getId());
        assertEquals("Star Wars", result.getTitle());
        assertEquals("1977-05-25", result.getReleaseDate());
        assertEquals(1, result.getGenres().size());
        assertSame(
                existingGenre,
                result.getGenres().get(0)
        );

        verify(movieDAO).save(any(Movie.class));
        verify(movieDAO, never())
                .update(any(Movie.class));

        verify(genreDAO).find(12L);
        verify(genreDAO, never())
                .save(any(Genre.class));
    }

    private MovieDTO createMovieDTO() {
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(12L);
        genreDTO.setName("Adventure");

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(11L);
        movieDTO.setTitle("Star Wars");
        movieDTO.setAdult(false);
        movieDTO.setTagline("A long time ago...");
        movieDTO.setBudget(11_000_000L);
        movieDTO.setRevenue(775_398_007L);
        movieDTO.setRuntime(121);
        movieDTO.setReleaseDate("1977-05-25");
        movieDTO.setPopularity(34.6946);
        movieDTO.setVoteAverage(8.207);
        movieDTO.setGenres(List.of(genreDTO));

        return movieDTO;
    }
}