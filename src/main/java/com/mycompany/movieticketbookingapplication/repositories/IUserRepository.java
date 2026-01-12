package com.mycompany.movieticketbookingapplication.repositories;

import com.mycompany.authlib.repositories.IAuthenticatableUserRepository;
import com.mycompany.movieticketbookingapplication.models.users.Admin;
import com.mycompany.movieticketbookingapplication.models.users.Customer;
import com.mycompany.movieticketbookingapplication.models.users.User;
import java.util.List;

public interface IUserRepository extends IAuthenticatableUserRepository {
    List<Admin> getAllAdmins();
    
    User getUser(String username);

    List<Customer> getAllCustomers();
}