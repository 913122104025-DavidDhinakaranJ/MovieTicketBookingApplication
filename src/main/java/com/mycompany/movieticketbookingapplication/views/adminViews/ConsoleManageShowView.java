package com.mycompany.movieticketbookingapplication.views.adminViews;

import com.mycompany.movieticketbookingapplication.Exceptions.ShowTimeConflictException;
import com.mycompany.movieticketbookingapplication.controllers.interfaces.adminControllersInterfaces.IManageShowController;
import com.mycompany.movieticketbookingapplication.enums.menuOptions.adminMenuOptions.AdminOperationsOption;
import com.mycompany.movieticketbookingapplication.models.CinemaHall;
import com.mycompany.movieticketbookingapplication.models.Movie;
import com.mycompany.movieticketbookingapplication.models.Seat;
import com.mycompany.movieticketbookingapplication.models.Show;
import com.mycompany.movieticketbookingapplication.models.ShowSeat;
import com.mycompany.movieticketbookingapplication.models.Theatre;
import com.mycompany.movieticketbookingapplication.utils.ConsoleInputUtil;
import java.time.LocalDateTime;
import java.util.List;

public class ConsoleManageShowView {
    private final ConsoleInputUtil inputReader;
    private final IManageShowController showController;
    
    private boolean running;
    
    public ConsoleManageShowView(IManageShowController showController) {
        inputReader = new ConsoleInputUtil();
        this.showController = showController;
    }
    
    public void runShowView() {
        running = true;
        
        while(running) {
            AdminOperationsOption choice = getAdminOperationsOption();
            switch(choice) {
                case ADD -> handleAddShow();
                case VIEW -> handleViewShows();
                case EXIT -> handleExit();
                case INVALID -> handleInvalidChoice();
            }
        }
    }
    
    private AdminOperationsOption getAdminOperationsOption() {
        System.out.println("1. Add Show");
        System.out.println("2. View Shows");
        System.out.println("0. Exit");
        
        return switch(inputReader.readInt("Enter choice: ")) {
            case 1 -> AdminOperationsOption.ADD;
            case 2 -> AdminOperationsOption.VIEW;
            case 0 -> AdminOperationsOption.EXIT;
            default -> AdminOperationsOption.INVALID;
        };
    }
    
    private AdminOperationsOption getAdminOperationsOption(Show show) {
        System.out.println("1. Update Show Timing");
        System.out.println("2. Remove Show");
        System.out.println("0. Back");
        
        return switch(inputReader.readInt("Enter choice: ")) {
            case 1 -> AdminOperationsOption.UPDATE;
            case 2 -> AdminOperationsOption.DELETE;
            case 0 -> AdminOperationsOption.EXIT;
            default -> AdminOperationsOption.INVALID;
        };
    }
    
    private void handleViewShows() {
        Show currentShow = getShow();
        if(currentShow == null) return;
        
        displayShowDetails(currentShow);
        
        while(currentShow != null) {
            AdminOperationsOption choice = getAdminOperationsOption(currentShow);
            switch(choice) {
                case UPDATE -> handleUpdateShow(currentShow);
                case DELETE -> {
                    handleDeleteShow(currentShow);
                    currentShow = null;
                }
                case EXIT -> currentShow = null;
                case INVALID -> handleInvalidChoice();
            }
        }
    }
    
    private void handleAddShow() {
        Theatre theatre = getTheatre();
        if(theatre == null) return;
        
        CinemaHall cinemaHall = getCinemaHall(theatre);
        if(cinemaHall == null) return;
        
        Movie movie = getMovie();
        if(movie == null) return;
        
        double basePrice = getBasePrice();
        
        do {
            LocalDateTime startTime = getStartTime();
            int breakTime = getBreakTime();
            try {
                showController.addShow(movie, cinemaHall, theatre, startTime, breakTime, basePrice);
                System.out.println("Show added successfully.");
            } catch (ShowTimeConflictException e) {
                displayError("Show already exist at the given time slot.");
            }
        } while(inputReader.readBoolean("Do you want to continue to add shows?"));
    }

    private void handleUpdateShow(Show show) {
        LocalDateTime startTime = getStartTime();
        int breakTime = getBreakTime();
        
        try {
            showController.updateShow(show, startTime, breakTime);
            System.out.println("Show updated successfully.");
        } catch (ShowTimeConflictException e) {
            displayError("Show already exist at the given time slot.");
        }
    }

    private void handleDeleteShow(Show show) {
        showController.deleteShow(show);
        System.out.println("Show removed successfully.");
    }
    
    private void handleExit() {
        running = false;
    }
    
    private void handleInvalidChoice() {
        displayError("Invalid Choice");
    }
    
    private Show getShow() {
        List<Show> showList = showController.getAllShows();
        if(showList.isEmpty()) {
            System.out.println("No Shows Found");
            return null;
        }
        
        for(int i = 0;i < showList.size();i++) {
            Show show = showList.get(i);
            System.out.println(i + 1 + ". Theatre: " + show.getTheatre().getName()
                    + "\tCinema Hall: " + show.getCinemaHall().getName()
                    + "\tMovie: " + show.getMovie().getTitle()
                    + "\tTime: " + inputReader.formatDateTime(show.getStartTime()) + 
                    " - " + inputReader.formatDateTime(show.getEndTime()));
        }
        System.out.println("0. Back");
        
        while(true) {
            int showChoice = inputReader.readInt("Enter Show Choice: ");
            if(showChoice == 0) return null;

            if(showChoice < 1 || showChoice > showList.size()) {
                displayError("Invalid Show Choice.");
                continue;
            }

            return showList.get(showChoice - 1);
        }
    }
    
    private Movie getMovie() {
        List<Movie> movieList = showController.getAllMovies();
        if(movieList.isEmpty()) {
            System.out.println("No Movie found.");
            return null;
        }
        
        for(int i = 0;i < movieList.size();i++) {
            System.out.println(i + 1 + ". " + movieList.get(i).getTitle());
        }
        
        while(true) {
            int movieChoice = inputReader.readInt("Enter Movie Choice: ");
        
            if(movieChoice < 1 || movieChoice > movieList.size()) {
                displayError("Invalid Movie Choice.");
                continue;
            }

            return movieList.get(movieChoice - 1);
        }
    }
    
    private Theatre getTheatre() {
        List<Theatre> theatreList = showController.getAllTheatres();
        if(theatreList.isEmpty()) {
            System.out.println("No Theatre found.");
            return null;
        }
        
        for(int i = 0;i < theatreList.size();i++) {
            System.out.println(i + 1 + ". " + theatreList.get(i).getName());
        }
        
        while(true) {
            int theatreChoice = inputReader.readInt("Enter Theatre Choice: ");

            if(theatreChoice < 1 || theatreChoice > theatreList.size()) {
                displayError("Invalid Theatre Choice.");
                continue;
            }

            return theatreList.get(theatreChoice - 1);
        }
    }

    private CinemaHall getCinemaHall(Theatre theatre) {
        List<CinemaHall> cinemaHallList = showController.getCinemaHalls(theatre);
        if(cinemaHallList.isEmpty()) {
            System.out.println("No Cinema Hall found.");
            return null;
        }
        
        for(int i = 0;i < cinemaHallList.size();i++) {
            System.out.println(i + 1 + ". " + cinemaHallList.get(i).getName());
        }
        
        while(true) {
            int cinemaHallChoice = inputReader.readInt("Enter Cinema Hall Choice: ");

            if(cinemaHallChoice < 1 || cinemaHallChoice > cinemaHallList.size()) {
                displayError("Invalid Cinema Hall Choice.");
                continue;
            }

            return cinemaHallList.get(cinemaHallChoice - 1);
        }
    }
    
    private LocalDateTime getStartTime() {
        while(true) {
            LocalDateTime startTime = inputReader.readDateTime("Enter Start Time: ");
            
            if(startTime.isAfter(LocalDateTime.now())) return startTime;
            displayError("Start Time must be the time in the future.");
        }
    }
    
    private int getBreakTime() {
        return inputReader.readInt("Enter Break Time (in Minutes): ");
    }
    
    private double getBasePrice() {
        return inputReader.readAmount("Enter Base Price: ");
    }
    
    private void displayShowDetails(Show show) {
        System.out.println("Movie: " + show.getMovie().getTitle());
        System.out.println("Theatre: " + show.getTheatre().getName());
        System.out.println("Cinema Hall: " + show.getCinemaHall().getName());
        System.out.println("Starting Time: " + inputReader.formatDateTime(show.getStartTime()));
        System.out.println("Ending Time: " + inputReader.formatDateTime(show.getEndTime()));
        System.out.println("Base Price: " + show.getPrice());
        System.out.println("Available Seats: ");
        List<ShowSeat> availableSeats = show.getAvailableSeats();
        int seatCount = 0;
        for(ShowSeat showSeat : availableSeats) {
            Seat seat = showSeat.getSeat();
            System.out.print(seat.getRow() + seat.getSeatNumber() + " - " + seat.getSeatType() + "\t");
            if(++seatCount % 5 == 0) System.out.println();
        }
        
        if(seatCount % 5 != 0) System.out.println();
    }

    private void displayError(String message) {
        System.out.println("Error: " + message);
    }
}