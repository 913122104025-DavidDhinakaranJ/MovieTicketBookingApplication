package com.mycompany.movieticketbookingapplication.views.adminViews;

import com.mycompany.movieticketbookingapplication.Exceptions.TheatreNameConflictException;
import com.mycompany.movieticketbookingapplication.controllers.implementations.adminControllersImplementations.ManageCinemaHallController;
import com.mycompany.movieticketbookingapplication.enums.menuOptions.adminMenuOptions.AdminOperationsOption;
import com.mycompany.movieticketbookingapplication.models.Theatre;
import com.mycompany.movieticketbookingapplication.utils.ConsoleInputUtil;
import java.util.List;
import com.mycompany.movieticketbookingapplication.controllers.interfaces.adminControllersInterfaces.IManageTheatreController;

public class ConsoleManageTheatreView {
    private final ConsoleInputUtil inputReader;
    private final IManageTheatreController theatreController;
    
    private boolean running;
    
    public ConsoleManageTheatreView(IManageTheatreController theatreController) {
        inputReader = new ConsoleInputUtil();
        this.theatreController = theatreController;
    }
    
    public void runTheatreView() {
        running = true;
        
        while(running) {
            AdminOperationsOption choice = getAdminOperationsOption();
            switch(choice) {
                case VIEW -> handleViewTheatres();
                case ADD -> handleAddTheatre();
                case UPDATE -> handleUpdateTheatre();
                case DELETE -> handleDeleteTheatre();
                case EXIT -> handleExit();
                case INVALID -> handleInvalidChoice();
            }
        }
    }
    
    private AdminOperationsOption getAdminOperationsOption() {
        System.out.println("1. View Theatres");
        System.out.println("2. Add Theatre");
        System.out.println("3. Manage Cinema Halls");
        System.out.println("4. Remove Theatre");
        System.out.println("0. Exit");
        
        return switch(inputReader.readInt("Enter choice: ")) {
            case 1 -> AdminOperationsOption.VIEW;
            case 2 -> AdminOperationsOption.ADD;
            case 3 -> AdminOperationsOption.UPDATE;
            case 4 -> AdminOperationsOption.DELETE;
            case 0 -> AdminOperationsOption.EXIT;
            default -> AdminOperationsOption.INVALID;
        };
    }
    
    private void handleViewTheatres() {
        Theatre theatre = getTheatre();
        if(theatre == null) return;
        
        displayTheatreDetails(theatre);
    }
    
    private void handleAddTheatre() {
        String theatreName = getTheatreName();
        String theatreAddress = getTheatreAddress();
        
        try {
            Theatre theatre = theatreController.addTheatre(theatreName, theatreAddress);
            handleManageCinemaHall(theatre);
            System.out.println("Theatre added successfully.");
        } catch (TheatreNameConflictException ex) {
            displayError("Theatre with given name already exist.");
        }
    }

    private void handleUpdateTheatre() {
        Theatre theatre = getTheatre();
        if(theatre == null) return;
        
        ConsoleManageCinemaHallView cinemaHallView = new ConsoleManageCinemaHallView(new ManageCinemaHallController(theatre));
        cinemaHallView.runCinemaHallView();
    }

    private void handleDeleteTheatre() {
        Theatre theatre = getTheatre();
        if(theatre == null) return;
        
        theatreController.deleteTheatre(theatre);
        System.out.println("Theatre removed successfully.");
    }
    
    private void handleExit() {
        running = false;
    }
    
    private void handleInvalidChoice() {
        displayError("Invalid Choice");
    }
    
    private void handleManageCinemaHall(Theatre theatre) {
        while(inputReader.readBoolean("Do you want to continue to manage cinema halls?")) {
            ConsoleManageCinemaHallView cinemaHallView = new ConsoleManageCinemaHallView(new ManageCinemaHallController(theatre));
            cinemaHallView.runCinemaHallView();
        }
    }
    
    private String getTheatreName() {
        return inputReader.readString("Enter Theatre Name: ", false);
    }
    
    private String getTheatreAddress() {
        return inputReader.readString("Enter Theatre Address: ", false);
    }
    
    private Theatre getTheatre() {
        List<Theatre> theatreList = theatreController.getAllTheatres();
        if(theatreList.isEmpty()) {
            System.out.println("No Theatre found.");
            return null;
        }
        
        for(int i = 0;i < theatreList.size();i++) {
            System.out.println(i + 1 + ". " + theatreList.get(i).getName());
        }
        System.out.println("0. Back");
        
        while(true) {
            int theatreChoice = inputReader.readInt("Enter Theatre Choice: ");
            if(theatreChoice == 0) return null;

            if(theatreChoice < 1 || theatreChoice > theatreList.size()) {
                displayError("Invalid Theatre Choice.");
                continue;
            }

            return theatreList.get(theatreChoice - 1);
        }
    }
    
    private void displayTheatreDetails(Theatre theatre) {
        System.out.println("Theatre Name: " + theatre.getName());
        System.out.println("Theatre Address: " + theatre.getAddress());
    }
    
    private void displayError(String message) {
        System.out.println("Error: " + message);
    }
}