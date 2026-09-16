import jakarta.persistence.EntityManagerFactory;
import mikkelmattern.config.HibernateConfig;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    }
}
