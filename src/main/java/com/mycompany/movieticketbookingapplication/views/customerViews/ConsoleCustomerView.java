package com.mycompany.movieticketbookingapplication.views.customerViews;

import com.mycompany.authlib.controller.AuthController;
import com.mycompany.authlib.views.ConsoleAuthView;
import com.mycompany.authlib.views.IAuthView;
import com.mycompany.movieticketbookingapplication.contexts.ApplicationContext;
import com.mycompany.movieticketbookingapplication.controllers.implementations.customerControllersImplementations.BookingController;
import com.mycompany.movieticketbookingapplication.controllers.implementations.customerControllersImplementations.SearchController;
import com.mycompany.movieticketbookingapplication.controllers.interfaces.customerControllersInterfaces.ICustomerController;
import com.mycompany.movieticketbookingapplication.enums.menuOptions.customerMenuOptions.CustomerMenuOption;
import com.mycompany.movieticketbookingapplication.models.Booking;
import com.mycompany.movieticketbookingapplication.models.Show;
import com.mycompany.movieticketbookingapplication.utils.ConsoleInputUtil;
import java.util.List;

public class ConsoleCustomerView {
    private final ConsoleInputUtil inputReader;
    private final ApplicationContext appContext;
    private final ICustomerController customerController;
    
    private boolean login;
    
    public ConsoleCustomerView(ICustomerController customerController) {
        inputReader = new ConsoleInputUtil();
        appContext = ApplicationContext.getInstance();
        this.customerController = customerController;
    }
    
    public void runCustomerView() {
        login = true;
        while(login) {
            CustomerMenuOption choice = getCustomerMenuOption();
            switch(choice) {
                case SEARCH_MOVIE -> handleSearchMovie();
                case BROWSE_MOVIE -> handleBrowseMovie();
                case VIEW_BOOKING_HISTORY -> handleViewBookHistory();
                case CHANGE_PASSWORD -> handleChangePassword();
                case LOGOUT -> handleLogout();
                case INVALID -> handleInvalidChoice();
            }
        }
    }
    
    private CustomerMenuOption getCustomerMenuOption() {
        System.out.println("1. Search Movie");
        System.out.println("2. Browse Movie");
        System.out.println("3. View Booking History");
        System.out.println("4. Change Password");
        System.out.println("0. Logout");
        
        return switch(inputReader.readInt("Enter choice: ")) {
            case 1 -> CustomerMenuOption.SEARCH_MOVIE;
            case 2 -> CustomerMenuOption.BROWSE_MOVIE;
            case 3 -> CustomerMenuOption.VIEW_BOOKING_HISTORY;
            case 4 -> CustomerMenuOption.CHANGE_PASSWORD;
            case 0 -> CustomerMenuOption.LOGOUT;
            default -> CustomerMenuOption.INVALID;
        };
    }
    
    private void handleSearchMovie() {
        ConsoleSearchView searchView = new ConsoleSearchView(new SearchController(appContext.getMovieRepository()));
        searchView.runSearchView();
    }
    
    private void handleBrowseMovie() {
        ConsoleBrowseView browseView = new ConsoleBrowseView(appContext.getMovieRepository().getAllMovies());
        browseView.runBrowseView();
    }
    
    private void handleViewBookHistory() {
        List<Booking> bookings = customerController.getBookingHistory();
        if(bookings.isEmpty()) {
            System.out.println("No Booking History Found.");
        } else {
            System.out.println("My Bookings:");
            displayBookHistory(bookings);
            handleBookingSelection(bookings);
        }
    }
    
    private void handleChangePassword() {
        IAuthView authView = new ConsoleAuthView(new AuthController(appContext.getUserRepository(), appContext.getCustomerFactory()));
        authView.changePassword(appContext.getSessionContext().getCurrentUser().get());
    }
    
    private void handleLogout() {
        login = false;
        System.out.println("Logged out Successfully.");
    }
    
    private void handleInvalidChoice() {
        displayError("Invalid Choice");
    }
    
    private void displayError(String message) {
        System.out.println("Error: " + message);
    }
    
    private void displayBookHistory(List<Booking> bookings) {
        int i = 1;
        for(Booking booking : bookings) {
            Show show = booking.getShow();
            System.out.println(i + ". Date:" + inputReader.formatDateTime(booking.getBookingDate())
                    + "\tMovie: " + show.getMovie().getTitle()
                    + "\tTheatre: " + show.getTheatre().getName()
                    + "\tStatus: " + booking.getBookingStatus());
            i++;
        }
    }
    
    private void handleBookingSelection(List<Booking> bookings) {
        Booking booking = getBookingChoice(bookings);
        if(booking == null) return;
        
        ConsoleBookingView bookingView = new ConsoleBookingView(new BookingController(booking));
        bookingView.runBookingView();
    }
    
    private Booking getBookingChoice(List<Booking> bookings) {
        System.out.println("0. Back");
        int bookingChoice = inputReader.readInt("Enter Booking Choice: ");
        
        if(bookingChoice == 0) {
            return null;
        }
        
        while(true) {
            if(bookingChoice < 1 || bookingChoice > bookings.size()) {
                displayError("Invalid Booking Choice.");
                continue;
            }
            
            return bookings.get(bookingChoice - 1);
        }
    }
}