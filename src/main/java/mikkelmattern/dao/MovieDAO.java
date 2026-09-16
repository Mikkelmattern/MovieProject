package mikkelmattern.dao;

import mikkelmattern.entities.Movie;

public interface MovieDAO {
    Movie find(long id);

    Movie save(Movie movie);

    void update(Movie movie);

    boolean delete(Movie movie);
}