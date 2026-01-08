package com.mycompany.movieticketbookingapplication.controllers.implementations.adminControllersImplementations;

import com.mycompany.movieticketbookingapplication.Exceptions.ShowTimeConflictException;
import com.mycompany.movieticketbookingapplication.controllers.interfaces.adminControllersInterfaces.IManageShowController;
import com.mycompany.movieticketbookingapplication.models.CinemaHall;
import com.mycompany.movieticketbookingapplication.models.Movie;
import com.mycompany.movieticketbookingapplication.models.Show;
import com.mycompany.movieticketbookingapplication.models.Theatre;
import com.mycompany.movieticketbookingapplication.repositories.IMovieRepository;
import com.mycompany.movieticketbookingapplication.repositories.IShowRepository;
import com.mycompany.movieticketbookingapplication.repositories.ITheatreRepository;
import java.time.LocalDateTime;
import java.util.List;

public class ManageShowController implements IManageShowController {
    private final IShowRepository showRepository;
    private final IMovieRepository movieRepository;
    private final ITheatreRepository theatreRepository;
    
    public ManageShowController(IShowRepository showRepository, IMovieRepository movieRepository, ITheatreRepository theatreRepository) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
    }

    @Override
    public void addShow(Movie movie, CinemaHall cinemaHall, Theatre theatre, LocalDateTime startTime, int breakTime, double basePrice) throws ShowTimeConflictException {
        LocalDateTime endTime = startTime.plusMinutes(breakTime + movie.getDurationInMinutes());
        if(showRepository.isShowTimeConflicting(theatre, cinemaHall, startTime, endTime)) {
            throw new ShowTimeConflictException();
        }
        Show show = new Show(movie, cinemaHall, theatre, basePrice);
        
        show.setStartTime(startTime);
        show.setEndTime(endTime);
        
        showRepository.addShow(show);
    }
    
    @Override
    public void updateShow(Show show, LocalDateTime startTime, int breakTime) throws ShowTimeConflictException {
        showRepository.deleteShow(show);
        LocalDateTime endTime = startTime.plusMinutes(breakTime + show.getMovie().getDurationInMinutes());
        
        if(showRepository.isShowTimeConflicting(show.getTheatre(), show.getCinemaHall(), startTime, endTime)) {
            showRepository.addShow(show);
            throw new ShowTimeConflictException();
        }
        
        show.setStartTime(startTime);
        show.setEndTime(endTime);
        
        showRepository.addShow(show);
    }
    
    @Override
    public void deleteShow(Show show) {
        showRepository.deleteShow(show);
    }
    
    @Override
    public List<Show> getAllShows() {
        return showRepository.getAllShows();
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.getAllMovies();
    }

    @Override
    public List<Theatre> getAllTheatres() {
        return theatreRepository.getAllTheatres();
    }

    @Override
    public List<CinemaHall> getCinemaHalls(Theatre theatre) {
        return theatre.getHalls();
    }
}