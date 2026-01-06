package com.mycompany.movieticketbookingapplication.controllers.interfaces.adminControllersInterfaces;

import com.mycompany.movieticketbookingapplication.models.Theatre;
import java.util.List;

public interface IManageTheatreController {
    Theatre addTheatre(String theatreName, String theatreAddress);
    void deleteTheatre(Theatre theatre);
    
    List<Theatre> getAllTheatres();
}