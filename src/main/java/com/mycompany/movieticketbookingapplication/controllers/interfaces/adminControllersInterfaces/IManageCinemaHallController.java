package com.mycompany.movieticketbookingapplication.controllers.interfaces.adminControllersInterfaces;

import com.mycompany.movieticketbookingapplication.Exceptions.CinemaHallNameConflictException;
import com.mycompany.movieticketbookingapplication.models.CinemaHall;
import java.util.List;

public interface IManageCinemaHallController {
    CinemaHall addCinemaHall(String cinemaHallName) throws CinemaHallNameConflictException;

    List<CinemaHall> getCinemaHalls();

    void deleteCinemaHall(CinemaHall cinemaHall);
}