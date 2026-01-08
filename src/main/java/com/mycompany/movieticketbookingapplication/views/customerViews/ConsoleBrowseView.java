package com.mycompany.movieticketbookingapplication.views.customerViews;

import com.mycompany.movieticketbookingapplication.contexts.ApplicationContext;
import com.mycompany.movieticketbookingapplication.controllers.implementations.customerControllersImplementations.CustomerController;
import com.mycompany.movieticketbookingapplication.controllers.implementations.customerControllersImplementations.MovieController;
import com.mycompany.movieticketbookingapplication.models.Movie;
import com.mycompany.movieticketbookingapplication.models.users.Customer;
import com.mycompany.movieticketbookingapplication.utils.ConsoleInputUtil;
import java.util.List;

public class ConsoleBrowseView {
    private final ConsoleInputUtil inputReader;
    private final ApplicationContext appContext;
    private final List<Movie> movies;
    
    public ConsoleBrowseView(List<Movie> movies) {
        inputReader = new ConsoleInputUtil();
        appContext = ApplicationContext.getInstance();
        this.movies = movies;
    }
    
    public void runBrowseView() {
        displayMovies();
        handleMoviesListSelection();
        
        Customer customer = (Customer) appContext.getSessionContext().getCurrentUser().orElse(null);
        if(customer != null) {
            ConsoleCustomerView customerView = new ConsoleCustomerView(new CustomerController(customer));
            customerView.runCustomerView();
        }
    }
    
    private void displayMovies() {
        for(int i = 0;i < movies.size();i++) {
            System.out.println(i + 1 + ". " + movies.get(i).getTitle());
        }
    }
    
    private void handleMoviesListSelection() {
        Movie movie = getMovieChoice();
        if(movie == null) return;
        
        ConsoleMovieView movieView = new ConsoleMovieView(new MovieController(movie, appContext.getShowRepository()));
        movieView.runMovieView();
    }
    
    private Movie getMovieChoice() {
        System.out.println("0. Back");
        
        while(true) {
            int movieChoice = inputReader.readInt("Enter Movie Choice: ");
            if(movieChoice == 0) return null;
            
            if(movieChoice < 1 || movieChoice > movies.size()) {
                displayError("Invalid Movie Choice.");
                continue;
            }

            return movies.get(movieChoice - 1);
        }
    }
    
    private void displayError(String message) {
        System.out.println("Error: " + message);
    }
}