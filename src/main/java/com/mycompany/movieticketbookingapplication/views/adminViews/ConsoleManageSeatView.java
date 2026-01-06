package com.mycompany.movieticketbookingapplication.views.adminViews;

import com.mycompany.movieticketbookingapplication.controllers.interfaces.adminControllersInterfaces.IManageSeatController;
import com.mycompany.movieticketbookingapplication.enums.SeatType;
import com.mycompany.movieticketbookingapplication.enums.menuOptions.adminMenuOptions.AdminOperationsOption;
import com.mycompany.movieticketbookingapplication.models.Seat;
import com.mycompany.movieticketbookingapplication.utils.ConsoleInputUtil;
import java.util.List;

public class ConsoleManageSeatView {
    private final ConsoleInputUtil inputReader;
    private final IManageSeatController seatController;
    
    private boolean running;
    
    public ConsoleManageSeatView(IManageSeatController seatController) {
        inputReader = new ConsoleInputUtil();
        this.seatController = seatController;
    }
    
    public void runSeatView() {
        running = true;
        
        while(running) {
            AdminOperationsOption choice = getAdminOperationsOption();
            switch(choice) {
                case ADD -> handleAddSeats();
                case UPDATE -> handleUpdateSeat();
                case DELETE -> handleDeleteSeat();
                case EXIT -> handleExit();
                case INVALID -> handleInvalidChoice();
            }
        }
    }
    
    private AdminOperationsOption getAdminOperationsOption() {
        System.out.println("1. Add Seats");
        System.out.println("2. Update Seats Type");
        System.out.println("3. Remove Seats");
        System.out.println("0. Exit");
        
        return switch(inputReader.readInt("Enter choice: ")) {
            case 1 -> AdminOperationsOption.ADD;
            case 2 -> AdminOperationsOption.UPDATE;
            case 3 -> AdminOperationsOption.DELETE;
            case 0 -> AdminOperationsOption.EXIT;
            default -> AdminOperationsOption.INVALID;
        };
    }

    private void handleAddSeats() {
        SeatType type = getSeatType();
        int numberOfRows = getNumberOfRows();
        int numberOfSeatsPerRow = getNumberOfSeatsPerRow();
        seatController.addSeats(numberOfRows, numberOfSeatsPerRow, type);
        
        System.out.println("Seats added successfully.");
    }

    private void handleUpdateSeat() {
        Seat seat = getSeat();
        if(seat == null) return;
        
        SeatType type = getSeatType();
        
        seatController.updateSeat(seat, type);
        System.out.println("Seat updated successfully.");
    }

    private void handleDeleteSeat() {
        Seat seat = getSeat();
        if(seat == null) return;
        
        seatController.deleteSeat(seat);
        System.out.println("Seat removed successfully.");
    }

    private void handleExit() {
        running = false;
    }

    private void handleInvalidChoice() {
        displayError("Invalid Choice");
    }

    private int getNumberOfRows() {
        return inputReader.readPositiveInt("Enter Number Of Rows: ");
    }

    private int getNumberOfSeatsPerRow() {
        return inputReader.readInt("Enter Number of Seats Per Row: ");
    }
    
    private Seat getSeat() {
        List<Seat> seatList = seatController.getSeats();
        if(seatList.isEmpty()) {
            System.out.println("No Seat found.");
            return null;
        }
        
        for(int i = 0;i < seatList.size();i++) {
            Seat seat = seatList.get(i);
            System.out.println(i + 1 + ". " + seat.getRow()
                    + seat.getSeatNumber() + "-"
                    + seat.getSeatType());
        }
        System.out.println("0. Exit");
        
        while(true) {
            int seatChoice = inputReader.readInt("Enter Seat Choice: ");
            if(seatChoice == 0) return null;

            if(seatChoice < 1 || seatChoice > seatList.size()) {
                displayError("Invalid Seat Choice.");
                continue;
            }

            return seatList.get(seatChoice - 1);
        }
    }

    private SeatType getSeatType() {
        SeatType[] seatTypes = SeatType.values();
        for(int i = 0;i < seatTypes.length;i++) {
            System.out.println(i + 1 + ". " + seatTypes[i]);
        }
        
        while(true) {
            int SeatTypeChoice = inputReader.readInt("Enter SeatType Choice: ");

            if(SeatTypeChoice < 1 || SeatTypeChoice > seatTypes.length) {
                displayError("Invalid SeatType Choice.");
                continue;
            }

            return seatTypes[SeatTypeChoice - 1];
        }
    }
    
    private void displayError(String message) {
        System.out.println("Error: " + message);
    }
}