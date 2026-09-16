import mikkelmattern.tmdb.TmdbClient;

public class Main {

    public static void main(String[] args) {
        TmdbClient client = new TmdbClient();

        String json = client.get(
                "/movie/11?language=en-US"
        );

        System.out.println(json);
    }
}