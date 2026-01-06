package com.mycompany.movieticketbookingapplication.controllers.interfaces.adminControllersInterfaces;

import com.mycompany.movieticketbookingapplication.models.CinemaHall;
import java.util.List;

public interface IManageCinemaHallController {
    CinemaHall addCinemaHall(String cinemaHallName);

    List<CinemaHall> getCinemaHalls();

    void deleteCinemaHall(CinemaHall cinemaHall);
}