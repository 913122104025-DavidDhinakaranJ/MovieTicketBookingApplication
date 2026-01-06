package com.mycompany.movieticketbookingapplication.controllers.interfaces.customerControllersInterfaces;

import com.mycompany.movieticketbookingapplication.models.Booking;
import com.mycompany.movieticketbookingapplication.models.Show;
import com.mycompany.movieticketbookingapplication.models.ShowSeat;
import com.mycompany.movieticketbookingapplication.models.users.Customer;
import java.util.List;
import java.util.Set;

public interface IShowController {
    Show getShow();
    
    boolean isShowStarted();
    
    List<ShowSeat> getAvailableSeats();
        
    Booking createBooking(Customer customer, Set<ShowSeat> selectedSeats);

    void confirmBooking(Booking booking);
}