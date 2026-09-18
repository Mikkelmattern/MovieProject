package mikkelmattern.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import mikkelmattern.entities.Actor;

import java.util.function.Function;

public class ActorDAOImpl implements Dao<Actor> {

    private final EntityManagerFactory emf;

    public ActorDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Actor find(long id) {
        return executeQuery(em -> em.find(Actor.class, id));
    }

    @Override
    public Actor save(Actor actor) {
        return executeQuery(em -> {
            em.persist(actor);
            return actor;
        });
    }

    @Override
    public Actor update(Actor actor) {
        return executeTransaction(em -> em.merge(actor));
    }

    @Override
    public boolean delete(Actor actor) {
        return executeTransaction(em -> {
            Actor existing = em.find(Actor.class, actor.getId());

            if (existing == null) { return false; }

            em.remove(existing);

            return true;
        });
    }

    private <R> R executeQuery(Function<EntityManager, R> action) {
        try (EntityManager em = emf.createEntityManager()) {
            return action.apply(em);
        }
    }

    private <R> R executeTransaction(Function<EntityManager, R> action) {

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