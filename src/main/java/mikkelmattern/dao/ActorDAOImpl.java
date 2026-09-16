package mikkelmattern.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import mikkelmattern.entities.Actor;

import java.util.function.Function;

public class ActorDAOImpl implements ActorDAO {
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
        }, true);
    }

    @Override
    public void update(Actor actor) {
        executeQuery(em -> em.merge(actor), true);
    }

    @Override
    public boolean delete(Actor actor) {
        return executeQuery(em -> {
            if (!em.contains(actor)) {
                Actor existing = em.find(Actor.class, actor.getId());
                if (existing == null) {
                    return false;
                }
                em.remove(existing);
            } else {
                em.remove(actor);
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