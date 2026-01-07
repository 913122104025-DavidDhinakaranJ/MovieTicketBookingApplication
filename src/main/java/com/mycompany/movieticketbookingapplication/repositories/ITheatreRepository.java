package com.mycompany.movieticketbookingapplication.repositories;

import com.mycompany.movieticketbookingapplication.models.Theatre;
import java.util.List;

public interface ITheatreRepository {
    void addTheatre(Theatre theatre);
    void deleteTheatre(Theatre theatre);
    
    boolean isTheatreAlreadyExist(String theatreName);
    List<Theatre> getAllTheatres();
}