package com.mycompany.movieticketbookingapplication.views.adminViews;

import com.mycompany.movieticketbookingapplication.Exceptions.CinemaHallNameConflictException;
import com.mycompany.movieticketbookingapplication.controllers.implementations.adminControllersImplementations.ManageSeatController;
import com.mycompany.movieticketbookingapplication.controllers.interfaces.adminControllersInterfaces.IManageCinemaHallController;
import com.mycompany.movieticketbookingapplication.enums.menuOptions.adminMenuOptions.AdminOperationsOption;
import com.mycompany.movieticketbookingapplication.models.CinemaHall;
import com.mycompany.movieticketbookingapplication.utils.ConsoleInputUtil;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConsoleManageCinemaHallView {
    private final ConsoleInputUtil inputReader;
    private final IManageCinemaHallController cinemaHallController;
    
    private boolean running;
    
    public ConsoleManageCinemaHallView(IManageCinemaHallController cinemaHallController) {
        inputReader = new ConsoleInputUtil();
        this.cinemaHallController = cinemaHallController;
    }
    
    public void runCinemaHallView() {
        running = true;
        
        while(running) {
            AdminOperationsOption choice = getAdminOperationsOption();
            switch(choice) {
                case ADD -> handleAddCinemaHall();
                case VIEW -> handleViewCinemaHalls();
                case EXIT -> handleExit();
                case INVALID -> handleInvalidChoice();
            }
        }
    }
    
    private AdminOperationsOption getAdminOperationsOption() {
        System.out.println("1. Add Cinema Hall");
        System.out.println("2. View Cinema Halls");
        System.out.println("0. Exit");
        
        return switch(inputReader.readInt("Enter choice: ")) {
            case 1 -> AdminOperationsOption.ADD;
            case 2 -> AdminOperationsOption.VIEW;
            case 0 -> AdminOperationsOption.EXIT;
            default -> AdminOperationsOption.INVALID;
        };
    }
    
    private AdminOperationsOption getAdminOperationsOption(CinemaHall cinemaHall) {
        System.out.println("1. Manage Seats");
        System.out.println("2. Remove Cinema Hall");
        System.out.println("0. Exit");
        
        return switch(inputReader.readInt("Enter choice: ")) {
            case 1 -> AdminOperationsOption.UPDATE;
            case 2 -> AdminOperationsOption.DELETE;
            case 0 -> AdminOperationsOption.EXIT;
            default -> AdminOperationsOption.INVALID;
        };
    }

    private void handleAddCinemaHall() {
        String cinemaHallName = getCinemaHallName();
        
        try {
            CinemaHall cinemaHall = cinemaHallController.addCinemaHall(cinemaHallName);
            handleManageSeats(cinemaHall); 
            System.out.println("Cinema Hall added successfully.");
        } catch (CinemaHallNameConflictException e) {
            displayError("Cinema Hall with given name already exist.");
        }
    }
    
    private void handleViewCinemaHalls() {
        CinemaHall cinemaHall = getCinemaHall();
        if(cinemaHall == null) return;
        
        displayCinemaHallDetails(cinemaHall);
        
        while(cinemaHall != null) {
            AdminOperationsOption choice = getAdminOperationsOption(cinemaHall);
            switch(choice) {
                case UPDATE -> handleUpdateCinemaHall(cinemaHall);
                case DELETE -> {
                    handleDeleteCinemaHall(cinemaHall);
                    cinemaHall = null;
                }
                case EXIT -> cinemaHall = null;
                case INVALID -> handleInvalidChoice();
            }
        }
    }

    private void handleUpdateCinemaHall(CinemaHall cinemaHall) {
        ConsoleManageSeatView seatView = new ConsoleManageSeatView(new ManageSeatController(cinemaHall));
        seatView.runSeatView();
    }

    private void handleDeleteCinemaHall(CinemaHall cinemaHall) {
        cinemaHallController.deleteCinemaHall(cinemaHall);
        System.out.println("Cinema Hall removed successfully.");
    }

    private void handleExit() {
        running = false;
    }
    
    private void handleInvalidChoice() {
        displayError("Invalid Choice");
    }
    
    private void handleManageSeats(CinemaHall cinemaHall) {
        if(inputReader.readBoolean("Do you want to continue to manage seats?")) {
            ConsoleManageSeatView seatView = new ConsoleManageSeatView(new ManageSeatController(cinemaHall));
            seatView.runSeatView();
        }
    }
    
    private String getCinemaHallName() {
        return inputReader.readString("Enter Cinema Hall Name: ", false);
    }
    
    private CinemaHall getCinemaHall() {
        List<CinemaHall> cinemaHallList = cinemaHallController.getCinemaHalls();
        if(cinemaHallList.isEmpty()) {
            System.out.println("No Cinema Hall found.");
            return null;
        }
        
        for(int i = 0;i < cinemaHallList.size();i++) {
            System.out.println(i + 1 + ". " + cinemaHallList.get(i).getName());
        }
        System.out.println("0. Back");
        
        while(true) {
            int cinemaHallChoice = inputReader.readInt("Enter CinemaHall Choice: ");
            if(cinemaHallChoice == 0) return null;

            if(cinemaHallChoice < 1 || cinemaHallChoice > cinemaHallList.size()) {
                displayError("Invalid CinemaHall Choice.");
                continue;
            }

            return cinemaHallList.get(cinemaHallChoice - 1);
        }
    }
    
    private void displayCinemaHallDetails(CinemaHall cinemaHall) {
        System.out.println("Cinema Hall Name: " + cinemaHall.getName());
        System.out.println("Total Seats: " + cinemaHall.getTotalSeats());
    } 
    
    private void displayError(String message) {
        System.out.println("Error: " + message);
    }
}