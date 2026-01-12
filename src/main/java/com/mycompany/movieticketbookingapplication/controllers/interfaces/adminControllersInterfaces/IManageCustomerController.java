package com.mycompany.movieticketbookingapplication.controllers.interfaces.adminControllersInterfaces;

import com.mycompany.movieticketbookingapplication.models.users.Customer;
import java.util.List;

public interface IManageCustomerController {
    void blockCustomer(Customer customer);
    void unblockCustomer(Customer customer);

    List<Customer> getAllCustomers();
}