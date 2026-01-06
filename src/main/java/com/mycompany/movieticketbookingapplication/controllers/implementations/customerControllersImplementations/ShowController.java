package com.mycompany.movieticketbookingapplication.controllers.implementations.customerControllersImplementations;

import com.mycompany.movieticketbookingapplication.controllers.interfaces.customerControllersInterfaces.IShowController;
import com.mycompany.movieticketbookingapplication.models.Booking;
import com.mycompany.movieticketbookingapplication.models.Show;
import com.mycompany.movieticketbookingapplication.models.ShowSeat;
import com.mycompany.movieticketbookingapplication.models.users.Customer;
import com.mycompany.movieticketbookingapplication.repositories.IBookingRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class ShowController implements IShowController {
    private final Show show;
    private final IBookingRepository bookingRepository;

    public ShowController(Show show, IBookingRepository bookingRepository) {
        this.show = show;
        this.bookingRepository = bookingRepository;
    } 

   @Override
    public Show getShow() {
        return show;
    }
    
    @Override
    public boolean isShowStarted() {
        return show.getStartTime().isBefore(LocalDateTime.now());
    }
    
    @Override
    public List<ShowSeat> getAvailableSeats() {
        return show.getAvailableSeats();
    }

    @Override
    public Booking createBooking(Customer customer, Set<ShowSeat> selectedSeats) {
        Booking booking = new Booking(customer, show, (List<ShowSeat>) selectedSeats);
        return booking;
    }

    @Override
    public void confirmBooking(Booking booking) {
        booking.updateStatusToConfirmed();
        bookingRepository.saveBooking(booking);
    }
}