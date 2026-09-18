package mikkelmattern.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import mikkelmattern.entities.Genre;

import java.util.function.Function;

public class GenreDAOImpl implements Dao<Genre> {

    private final EntityManagerFactory emf;

    public GenreDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Genre find(long id) {
        return executeQuery(
                em -> em.find(Genre.class, id)
        );
    }

    @Override
    public Genre save(Genre genre) {
        return executeTransaction(em -> {
            em.persist(genre);
            return genre;
        });
    }

    @Override
    public Genre update(Genre genre) {
        return executeTransaction(
                em -> em.merge(genre)
        );
    }

    @Override
    public boolean delete(Genre genre) {
        return executeTransaction(em -> {
            Genre existing = em.find(
                    Genre.class,
                    genre.getId()
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