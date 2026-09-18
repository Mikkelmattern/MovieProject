package mikkelmattern.dao;

public interface Dao<T> {

    T find(long id);

    T save(T entity);

    T update(T entity);

    boolean delete(T entity);
}