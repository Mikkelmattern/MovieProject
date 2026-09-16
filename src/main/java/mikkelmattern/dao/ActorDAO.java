package mikkelmattern.dao;

import mikkelmattern.entities.Actor;

public interface ActorDAO {
    Actor find(long id);

    Actor save(Actor actor);

    void update(Actor actor);

    boolean delete(Actor actor);
}