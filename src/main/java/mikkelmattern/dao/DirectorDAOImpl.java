package mikkelmattern.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import mikkelmattern.entities.Director;

import java.util.List;
import java.util.function.Function;

public class DirectorDAOImpl implements Dao<Director> {

    private final EntityManagerFactory emf;

    public DirectorDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Director find(long id) {
        return executeQuery(
                em -> em.find(Director.class, id)
        );
    }

    public List<Director> searchByName(
            String searchText
    ) {
        if (searchText == null || searchText.isBlank()) {
            return List.of();
        }

        return executeQuery(em ->
            em.createQuery(
                """
                SELECT director
                FROM Director director
                WHERE LOWER(director.name)
                    LIKE LOWER(:searchText)
                ORDER BY director.name
                """,
            Director.class
                )
                .setParameter(
                    "searchText",
                    "%" + searchText.trim() + "%"
                )
                .getResultList()
        );
    }

    @Override
    public Director save(Director director) {
        return executeTransaction(em -> {
            em.persist(director);
            return director;
        });
    }

    @Override
    public Director update(Director director) {
        return executeTransaction(
                em -> em.merge(director)
        );
    }

    @Override
    public boolean delete(Director director) {
        return executeTransaction(em -> {
            Director existing = em.find(
                    Director.class,
                    director.getId()
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