import jakarta.persistence.EntityManagerFactory;
import mikkelmattern.config.HibernateConfig;
import mikkelmattern.tmdb.TmdbClient;

public class Main {

    public static void main(String[] args) {
        TmdbClient client = new TmdbClient();

        String json = client.get(
                "/movie/11?language=en-US"
        );

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    }
}