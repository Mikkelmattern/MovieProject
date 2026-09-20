package mikkelmattern.controller;

import jakarta.persistence.EntityManagerFactory;
import mikkelmattern.dao.ActorDAOImpl;
import mikkelmattern.dao.DirectorDAOImpl;
import mikkelmattern.dao.MovieDAOImpl;
import mikkelmattern.entities.Movie;
import mikkelmattern.service.MovieService;
import mikkelmattern.tmdb.TmdbClient;

import java.util.List;
import java.util.Scanner;

public class TerminalController {
    private final MovieDAOImpl movieDAO;
    private final ActorDAOImpl actorDAO;
    private final DirectorDAOImpl directorDAO;
    private final TmdbClient client;
    private final EntityManagerFactory emf;

    public TerminalController(MovieDAOImpl movieDAO,
                              ActorDAOImpl actorDAO,
                              DirectorDAOImpl directorDAO,
                              TmdbClient client, EntityManagerFactory emf
    ) {
        this.movieDAO = movieDAO;
        this.actorDAO = actorDAO;
        this.directorDAO = directorDAO;
        this.client = client;
        this.emf = emf;
    }

    private int printOptions() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("1. Search for a movie\n2. Search for an actor\n3. Search for a director");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                searchMovie(scanner);
            case 2:
                searchActor();
            case 3:
                searchDirector();
            default:
                return 0;
        }
    }

    private void searchMovie(Scanner scanner) {
        MovieService ms = new MovieService(client, emf);
        System.out.println("Enter the name of the movie you want to search for:");
        String text = scanner.nextLine();
        List<Movie> foundMovies = ms.searchByTitle(text);


    }

    private void searchActor() {

    }

    private void searchDirector() {

    }

    private <T> void printResult(List<T> items){
        for(int i = 0; i < items.size(); i++){
            System.out.println(i + ":" + items.get(i));
        }
    }

    private void getInfoOnMovie(MovieService ms, Movie chosenMovie){
        ms.getDirectors()
    }
}
