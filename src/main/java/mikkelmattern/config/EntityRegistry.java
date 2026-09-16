package mikkelmattern.config;

import mikkelmattern.entities.Actor;
import mikkelmattern.entities.Genre;
import mikkelmattern.entities.Movie;
import org.hibernate.cfg.Configuration;

final class EntityRegistry {

    private EntityRegistry() {}

    static void registerEntities(Configuration configuration) {
        configuration.addAnnotatedClass(Movie.class);
        configuration.addAnnotatedClass(Actor.class);
        configuration.addAnnotatedClass(Genre.class);
    }
}