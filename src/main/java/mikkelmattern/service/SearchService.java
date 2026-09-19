package mikkelmattern.service;

import mikkelmattern.dao.ActorDAOImpl;
import mikkelmattern.dao.DirectorDAOImpl;
import mikkelmattern.dao.MovieDAOImpl;
import mikkelmattern.entities.Actor;
import mikkelmattern.entities.Director;
import mikkelmattern.entities.Movie;

import java.util.List;

public class SearchService {

    private final MovieDAOImpl movieDAO;
    private final ActorDAOImpl actorDAO;
    private final DirectorDAOImpl directorDAO;

    public SearchService(
            MovieDAOImpl movieDAO,
            ActorDAOImpl actorDAO,
            DirectorDAOImpl directorDAO
    ) {
        this.movieDAO = movieDAO;
        this.actorDAO = actorDAO;
        this.directorDAO = directorDAO;
    }

    public List<Movie> searchMovies(String searchText) {
        return movieDAO.searchByTitle(searchText);
    }

    public List<Actor> searchActors(String searchText) {
        return actorDAO.searchByName(searchText);
    }

    public List<Director> searchDirectors(
            String searchText
    ) {
        return directorDAO.searchByName(searchText);
    }
}