package com.mycompany.movieticketbookingapplication.controllers.interfaces.adminControllersInterfaces;

import com.mycompany.movieticketbookingapplication.enums.SeatType;
import com.mycompany.movieticketbookingapplication.models.Seat;
import java.util.List;

public interface IManageSeatController {
    void addSeats(int numberOfRows, int numberOfSeatsPerRow, SeatType type);
    void updateSeat(Seat seat, SeatType type);
    void deleteSeat(Seat seat);

    List<Seat> getSeats();
}