package com.mycompany.movieticketbookingapplication.repositories;

import com.mycompany.movieticketbookingapplication.models.CinemaHall;
import com.mycompany.movieticketbookingapplication.models.Movie;
import com.mycompany.movieticketbookingapplication.models.Show;
import com.mycompany.movieticketbookingapplication.models.Theatre;
import java.time.LocalDateTime;
import java.util.List;

public interface IShowRepository {
    void addShow(Show show);
    void deleteShow(Show show);
    
    List<Show> getShows(Movie movie);
    List<Show> getFutureShows(Movie movie);
    boolean isShowTimeConflicting(Theatre theatre, CinemaHall cinemaHall, LocalDateTime startTime, LocalDateTime endTime);
    List<Show> getAllShows();
}