

import mikkelmattern.dao.ActorDAOImpl;
import mikkelmattern.dao.DirectorDAOImpl;
import mikkelmattern.dao.MovieDAOImpl;
import mikkelmattern.entities.Actor;
import mikkelmattern.entities.Director;
import mikkelmattern.entities.Movie;
import mikkelmattern.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    private MovieDAOImpl movieDAO;
    private ActorDAOImpl actorDAO;
    private DirectorDAOImpl directorDAO;
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        movieDAO = mock(MovieDAOImpl.class);
        actorDAO = mock(ActorDAOImpl.class);
        directorDAO = mock(DirectorDAOImpl.class);

        searchService = new SearchService(
                movieDAO,
                actorDAO,
                directorDAO
        );
    }

    @Test
    void shouldSearchMoviesByTitle() {
        Movie movie = new Movie();
        movie.setId(11L);
        movie.setTitle("Star Wars");

        when(movieDAO.searchByTitle("Star Wars"))
                .thenReturn(List.of(movie));

        List<Movie> result =
                searchService.searchMovies("Star Wars");

        assertEquals(1, result.size());
        assertEquals("Star Wars", result.get(0).getTitle());

        verify(movieDAO).searchByTitle("Star Wars");
    }

    @Test
    void shouldSearchActorsByName() {
        Actor actor = new Actor();
        actor.setId(1L);
        actor.setName("Mark Hamill");

        when(actorDAO.searchByName("Mark"))
                .thenReturn(List.of(actor));

        List<Actor> result =
                searchService.searchActors("Mark");

        assertEquals(1, result.size());
        assertEquals("Mark Hamill", result.get(0).getName());

        verify(actorDAO).searchByName("Mark");
    }

    @Test
    void shouldSearchDirectorsByName() {
        Director director = new Director();
        director.setId(1L);
        director.setName("George Lucas");

        when(directorDAO.searchByName("Lucas"))
                .thenReturn(List.of(director));

        List<Director> result =
                searchService.searchDirectors("Lucas");

        assertEquals(1, result.size());
        assertEquals("George Lucas", result.get(0).getName());

        verify(directorDAO).searchByName("Lucas");
    }
}