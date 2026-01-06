package com.mycompany.movieticketbookingapplication.utils;

import com.mycompany.movieticketbookingapplication.enums.Genre;
import com.mycompany.movieticketbookingapplication.enums.Language;
import com.mycompany.movieticketbookingapplication.enums.Rating;
import com.mycompany.movieticketbookingapplication.enums.SeatType;
import com.mycompany.movieticketbookingapplication.models.CinemaHall;
import com.mycompany.movieticketbookingapplication.models.Movie;
import com.mycompany.movieticketbookingapplication.models.Show;
import com.mycompany.movieticketbookingapplication.models.Theatre;
import com.mycompany.movieticketbookingapplication.repositories.IMovieRepository;
import com.mycompany.movieticketbookingapplication.repositories.IShowRepository;
import com.mycompany.movieticketbookingapplication.repositories.ITheatreRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SampleDataLoader {
    private final IMovieRepository movieRepository;
    private final ITheatreRepository theatreRepository;
    private final IShowRepository showRepository;
    
    public SampleDataLoader(IMovieRepository movieRepository, ITheatreRepository theatreRepository, IShowRepository showRepository) {
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
        this.showRepository = showRepository;
    }

    public void loadData() {
        loadSampleMovies();
        loadTheatres();
        loadShows();
    }
    
    private void loadSampleMovies() {
        // Movie 1: Inception
        Movie inception = new Movie("Inception", Rating.UA);
        inception.addGenre(Genre.SCI_FI);
        inception.addGenre(Genre.THRILLER);
        inception.addGenre(Genre.ACTION);
        inception.addLanguage(Language.ENGLISH);
        inception.setDurationInMinutes(148);
        inception.setReleaseDate(LocalDate.of(2010, 7, 16));
        movieRepository.addMovie(inception);

        // Movie 2: Interstellar
        Movie interstellar = new Movie("Interstellar", Rating.UA);
        interstellar.addGenre(Genre.SCI_FI);
        interstellar.addGenre(Genre.DRAMA);
        interstellar.addGenre(Genre.ADVENTURE);
        interstellar.addLanguage(Language.ENGLISH);
        interstellar.setDurationInMinutes(169);
        interstellar.setReleaseDate(LocalDate.of(2014, 11, 7));
        movieRepository.addMovie(interstellar);

        // Movie 3: Vikram
        Movie vikram = new Movie("Vikram", Rating.A);
        vikram.addGenre(Genre.ACTION);
        vikram.addGenre(Genre.THRILLER);
        vikram.addGenre(Genre.CRIME);
        vikram.addLanguage(Language.TAMIL);
        vikram.setDurationInMinutes(175);
        vikram.setReleaseDate(LocalDate.of(2022, 6, 3));
        movieRepository.addMovie(vikram);

        // Movie 4: Spirited Away
        Movie spiritedAway = new Movie("Spirited Away", Rating.U);
        spiritedAway.addGenre(Genre.ANIMATION);
        spiritedAway.addGenre(Genre.FANTASY);
        spiritedAway.addGenre(Genre.FAMILY);
        spiritedAway.addLanguage(Language.JAPANESE);
        spiritedAway.setDurationInMinutes(125);
        spiritedAway.setReleaseDate(LocalDate.of(2001, 7, 20));
        movieRepository.addMovie(spiritedAway);

        // Movie 5: Dangal
        Movie dangal = new Movie("Dangal", Rating.U);
        dangal.addGenre(Genre.DRAMA);
        dangal.addGenre(Genre.FAMILY);
        dangal.addGenre(Genre.DOCUMENTARY);
        dangal.addLanguage(Language.HINDI);
        dangal.setDurationInMinutes(161);
        dangal.setReleaseDate(LocalDate.of(2016, 12, 23));
        movieRepository.addMovie(dangal);
    }

    private void loadTheatres() {
        Theatre inox = new Theatre(
                "INOX Marina Mall",
                "OMR, Chennai"
        );

        inox.addHall("Screen 1");
        inox.addHall("Screen 2");

        CinemaHall screen1 = inox.getHalls().get(0);
        CinemaHall screen2 = inox.getHalls().get(1);

        screen1.generateSeats(5, 12, SeatType.REGULAR);   // A–E
        screen1.generateSeats(2, 10, SeatType.PREMIUM);   // F–G
        screen1.generateSeats(1, 8, SeatType.VIP);   // H

        screen2.generateSeats(4, 10, SeatType.REGULAR);   // A–D
        screen2.generateSeats(3, 8, SeatType.PREMIUM);    // E–G

        theatreRepository.addTheatre(inox);

        Theatre pvr = new Theatre(
                "PVR Phoenix Mall",
                "Velachery, Chennai"
        );

        pvr.addHall("Audi 1");
        pvr.addHall("Audi 2");

        CinemaHall audi1 = pvr.getHalls().get(0);
        CinemaHall audi2 = pvr.getHalls().get(1);

        audi1.generateSeats(6, 14, SeatType.REGULAR);   // A–F
        audi1.generateSeats(2, 12, SeatType.PREMIUM);   // G–H

        audi2.generateSeats(3, 10, SeatType.PREMIUM);   // A–C
        audi2.generateSeats(2, 6, SeatType.VIP);   // D–E

        theatreRepository.addTheatre(pvr);
    }
    
    private void loadShows() {
        Movie inception = movieRepository.getAllMovies().get(0);
        Movie interstellar = movieRepository.getAllMovies().get(1);
        Movie vikram = movieRepository.getAllMovies().get(2);

        Theatre inox = theatreRepository.getAllTheatres().get(0);
        CinemaHall inoxScreen1 = inox.getHalls().get(0);
        CinemaHall inoxScreen2 = inox.getHalls().get(1);

        Theatre pvr = theatreRepository.getAllTheatres().get(1);
        CinemaHall pvrAudi1 = pvr.getHalls().get(0);

        // ---------- INOX SCREEN 1 ----------

        Show inoxMorningShow = new Show(
                inception,
                inoxScreen1,
                inox,
                180.00
        );
        inoxMorningShow.setStartTime(
                LocalDateTime.of(2026, 1, 15, 9, 30)
        );
        inoxMorningShow.setEndTime(
                LocalDateTime.of(2026, 1, 15, 12, 0)
        );
        showRepository.addShow(inoxMorningShow);

        Show inoxEveningShow = new Show(
                inception,
                inoxScreen1,
                inox,
                250.00
        );
        inoxEveningShow.setStartTime(
                LocalDateTime.of(2026, 1, 15, 18, 30)
        );
        inoxEveningShow.setEndTime(
                LocalDateTime.of(2026, 1, 15, 21, 0)
        );
        showRepository.addShow(inoxEveningShow);

        // ---------- INOX SCREEN 2 ----------

        Show inoxLateShow = new Show(
                interstellar,
                inoxScreen2,
                inox,
                220.00
        );
        inoxLateShow.setStartTime(
                LocalDateTime.of(2026, 1, 15, 22, 0)
        );
        inoxLateShow.setEndTime(
                LocalDateTime.of(2026, 1, 16, 1, 0)
        );
        showRepository.addShow(inoxLateShow);

        // ---------- PVR AUDI 1 ----------

        Show pvrAfternoonShow = new Show(
                vikram,
                pvrAudi1,
                pvr,
                200.00
        );
        pvrAfternoonShow.setStartTime(
                LocalDateTime.of(2026, 1, 15, 14, 15)
        );
        pvrAfternoonShow.setEndTime(
                LocalDateTime.of(2026, 1, 15, 17, 15)
        );
        showRepository.addShow(pvrAfternoonShow);
    }

}