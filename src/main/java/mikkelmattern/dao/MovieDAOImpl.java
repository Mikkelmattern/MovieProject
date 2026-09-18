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
        return executeQuery(em -> em.find(Movie.class, id));
    }

    @Override
    public Movie save(Movie movie) {
        return executeQuery(em -> {
            em.persist(movie);
            return movie;
        }, true);
    }

    @Override
    public void update(Movie movie) {
        executeQuery(em -> em.merge(movie), true);
    }

    @Override
    public boolean delete(Movie movie) {
        return executeQuery(em -> {
            if (!em.contains(movie)) {
                Movie existing = em.find(Movie.class, movie.getId());
                if (existing == null) {
                    return false;
                }
                em.remove(existing);
            } else {
                em.remove(movie);
            }
            return true;
        }, true);
    }

    private <R> R executeQuery(Function<EntityManager, R> action) {
        try (EntityManager em = emf.createEntityManager()) {
            return action.apply(em);
        }
    }

    private <R> R executeQuery(Function<EntityManager, R> action, boolean isTransactional) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            R result = action.apply(em);
            em.getTransaction().commit();
            return result;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}