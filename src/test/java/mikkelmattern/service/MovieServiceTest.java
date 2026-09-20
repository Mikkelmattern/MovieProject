package mikkelmattern.service;

import mikkelmattern.DTO.CreditsDTO;
import mikkelmattern.DTO.CrewDTO;
import mikkelmattern.DTO.GenreDTO;
import mikkelmattern.DTO.MovieDTO;
import mikkelmattern.dao.GenreDAOImpl;
import mikkelmattern.dao.MovieDAOImpl;
import mikkelmattern.entities.Genre;
import mikkelmattern.entities.Movie;
import mikkelmattern.tmdb.TmdbClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        movieService = new MovieService(tmdbClient, movieDAO, genreDAO);
    }

    @Test
    void shouldOnlyReturnDirectors() {
        CreditsDTO credits = new CreditsDTO();
        credits.setCrew(List.of(
                crewMember(1L, "George Lucas", "Director"),
                crewMember(2L, "John Williams", "Original Music Composer"),
                crewMember(3L, "Gary Kurtz", "Producer")
        ));
        when(tmdbClient.getCredits(11L)).thenReturn(credits);

        List<CrewDTO> result = movieService.getDirectors(11L);

        assertEquals(1, result.size());
        assertEquals("George Lucas", result.get(0).getName());
        verify(tmdbClient).getCredits(11L);
    }

    @Test
    void shouldReturnEmptyDirectorListWhenCrewIsNull() {
        CreditsDTO credits = new CreditsDTO();
        when(tmdbClient.getCredits(11L)).thenReturn(credits);

        List<CrewDTO> result = movieService.getDirectors(11L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSaveNewMovieAndReuseExistingGenre() {
        MovieDTO dto = movieDTOWithGenres();
        Genre existingGenre = new Genre(12L, "Adventure");
        when(tmdbClient.getMovie(11L)).thenReturn(dto);
        when(genreDAO.find(12L)).thenReturn(existingGenre);
        when(movieDAO.find(11L)).thenReturn(null);
        when(movieDAO.save(any(Movie.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Movie result = movieService.fetchAndSaveMovie(11L);

        assertAll(
                () -> assertEquals(11L, result.getId()),
                () -> assertEquals("Star Wars", result.getTitle()),
                () -> assertEquals("1977-05-25", result.getReleaseDate()),
                () -> assertEquals(8.207, result.getVoteAverage()),
                () -> assertEquals(List.of(existingGenre), result.getGenres())
        );
        verify(movieDAO).save(any(Movie.class));
        verify(movieDAO, never()).update(any(Movie.class));
        verify(genreDAO, never()).save(any(Genre.class));
    }

    @Test
    void shouldSaveMissingGenreBeforeMovie() {
        MovieDTO dto = movieDTOWithGenres();
        when(tmdbClient.getMovie(11L)).thenReturn(dto);
        when(genreDAO.find(12L)).thenReturn(null);
        when(genreDAO.save(any(Genre.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(movieDAO.find(11L)).thenReturn(null);
        when(movieDAO.save(any(Movie.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Movie result = movieService.fetchAndSaveMovie(11L);

        assertEquals(1, result.getGenres().size());
        assertEquals(12L, result.getGenres().get(0).getId());
        assertEquals("Adventure", result.getGenres().get(0).getName());
        verify(genreDAO).save(argThat(genre ->
                genre.getId().equals(12L) && genre.getName().equals("Adventure")
        ));
    }

    @Test
    void shouldUpdateMovieWhenItAlreadyExists() {
        MovieDTO dto = movieDTOWithGenres();
        Movie existing = Movie.builder().id(11L).title("Old title").build();
        when(tmdbClient.getMovie(11L)).thenReturn(dto);
        when(genreDAO.find(12L)).thenReturn(new Genre(12L, "Adventure"));
        when(movieDAO.find(11L)).thenReturn(existing);
        when(movieDAO.update(any(Movie.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Movie result = movieService.fetchAndSaveMovie(11L);

        assertEquals("Star Wars", result.getTitle());
        verify(movieDAO).update(any(Movie.class));
        verify(movieDAO, never()).save(any(Movie.class));
    }

    @Test
    void shouldHandleMovieWithoutGenres() {
        MovieDTO dto = movieDTOWithGenres();
        dto.setGenres(null);
        when(tmdbClient.getMovie(11L)).thenReturn(dto);
        when(movieDAO.find(11L)).thenReturn(null);
        when(movieDAO.save(any(Movie.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Movie result = movieService.fetchAndSaveMovie(11L);

        assertNotNull(result.getGenres());
        assertTrue(result.getGenres().isEmpty());
        verifyNoInteractions(genreDAO);
    }

    @Test
    void shouldDelegateTitleSearchToMovieDAO() {
        List<Movie> expected = List.of(
                Movie.builder().id(11L).title("Star Wars").build()
        );
        when(movieDAO.searchByTitle("Star Wars")).thenReturn(expected);

        List<Movie> result = movieService.searchByTitle("Star Wars");

        assertSame(expected, result);
        verify(movieDAO).searchByTitle("Star Wars");
    }

    private CrewDTO crewMember(Long id, String name, String job) {
        CrewDTO member = new CrewDTO();
        member.setId(id);
        member.setName(name);
        member.setJob(job);
        return member;
    }

    private MovieDTO movieDTOWithGenres() {
        GenreDTO genre = new GenreDTO();
        genre.setId(12L);
        genre.setName("Adventure");

        MovieDTO movie = new MovieDTO();
        movie.setId(11L);
        movie.setTitle("Star Wars");
        movie.setAdult(false);
        movie.setTagline("A long time ago...");
        movie.setBudget(11_000_000L);
        movie.setRevenue(775_398_007L);
        movie.setRuntime(121);
        movie.setReleaseDate("1977-05-25");
        movie.setPopularity(34.6946);
        movie.setVoteAverage(8.207);
        movie.setGenres(List.of(genre));
        return movie;
    }
}
