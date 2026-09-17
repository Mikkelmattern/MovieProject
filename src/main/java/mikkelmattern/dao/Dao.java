package mikkelmattern.dao;

import mikkelmattern.entities.Actor;

public interface Dao<T> {
    T find(long id);

    T save(T T);

    T update(T entity);

    T delete(T entity);
}