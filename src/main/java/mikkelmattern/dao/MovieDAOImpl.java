package mikkelmattern.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import mikkelmattern.entities.Movie;

import java.util.function.Function;

public class MovieDAOImpl implements Dao<Movie> {

    private final EntityManagerFactory emf;

    public MovieDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Movie find(long id) {
        return executeQuery(
                em -> em.find(Movie.class, id)
        );
    }

    @Override
    public Movie save(Movie movie) {
        return executeTransaction(em -> {
            em.persist(movie);
            return movie;
        });
    }

    @Override
    public Movie update(Movie movie) {
        return executeTransaction(
                em -> em.merge(movie)
        );
    }

    @Override
    public boolean delete(Movie movie) {
        return executeTransaction(em -> {
            Movie existing = em.find(
                    Movie.class,
                    movie.getId()
            );

            if (existing == null) {
                return false;
            }

            em.remove(existing);
            return true;
        });
    }

    private <R> R executeQuery(
            Function<EntityManager, R> action
    ) {
        try (EntityManager em =
                     emf.createEntityManager()) {

            return action.apply(em);
        }
    }

    private <R> R executeTransaction(
            Function<EntityManager, R> action
    ) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            R result = action.apply(em);

            em.getTransaction().commit();

            return result;

        } catch (RuntimeException exception) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw exception;

        } finally {
            em.close();
        }
    }
}