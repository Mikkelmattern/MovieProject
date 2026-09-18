package mikkelmattern.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import mikkelmattern.entities.Actor;
import mikkelmattern.entities.Genre;
import mikkelmattern.entities.Movie;

import java.util.function.Function;

public class GenreDAOImpl implements Dao<Genre> {

    private final EntityManagerFactory emf;
    public GenreDAOImpl(EntityManagerFactory emf){
        this.emf = emf;
    }
    @Override
    public Genre find(long id) {
        return executeQuery(em -> em.find(Genre.class, id));
    }

    @Override
    public Genre save(Genre genre) {
        return executeQuery(em -> {
            em.persist(genre);
            return genre;
        }, true);
    }

    @Override
    public void update(Genre genre) {
        executeQuery(em -> em.merge(genre), true);
    }

    @Override
    public boolean delete(Genre genre) {
        return executeQuery(em -> {
            if (!em.contains(genre)) {
                Movie existing = em.find(Movie.class, genre.getId());
                if (existing == null) {
                    return false;
                }
                em.remove(existing);
            } else {
                em.remove(genre);
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
