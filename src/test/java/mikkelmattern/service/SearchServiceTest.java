package mikkelmattern.service;

import mikkelmattern.dao.ActorDAOImpl;
import mikkelmattern.dao.DirectorDAOImpl;
import mikkelmattern.dao.MovieDAOImpl;
import mikkelmattern.entities.Actor;
import mikkelmattern.entities.Director;
import mikkelmattern.entities.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
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
        searchService = new SearchService(movieDAO, actorDAO, directorDAO);
    }

    @Test
    void shouldSearchMoviesByTitle() {
        List<Movie> expected = List.of(
                Movie.builder().id(11L).title("Star Wars").build()
        );
        when(movieDAO.searchByTitle("Star Wars")).thenReturn(expected);

        List<Movie> result = searchService.searchMovies("Star Wars");

        assertSame(expected, result);
        verify(movieDAO).searchByTitle("Star Wars");
        verifyNoInteractions(actorDAO, directorDAO);
    }

    @Test
    void shouldSearchActorsByName() {
        Actor actor = new Actor();
        actor.setId(1L);
        actor.setName("Mark Hamill");
        List<Actor> expected = List.of(actor);
        when(actorDAO.searchByName("Mark")).thenReturn(expected);

        List<Actor> result = searchService.searchActors("Mark");

        assertSame(expected, result);
        verify(actorDAO).searchByName("Mark");
        verifyNoInteractions(movieDAO, directorDAO);
    }

    @Test
    void shouldSearchDirectorsByName() {
        Director director = new Director(1L, "George Lucas");
        List<Director> expected = List.of(director);
        when(directorDAO.searchByName("Lucas")).thenReturn(expected);

        List<Director> result = searchService.searchDirectors("Lucas");

        assertSame(expected, result);
        verify(directorDAO).searchByName("Lucas");
        verifyNoInteractions(movieDAO, actorDAO);
    }
}
