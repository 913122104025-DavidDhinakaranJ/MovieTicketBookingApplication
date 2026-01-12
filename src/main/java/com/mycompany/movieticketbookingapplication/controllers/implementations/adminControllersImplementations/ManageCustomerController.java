package com.mycompany.movieticketbookingapplication.controllers.implementations.adminControllersImplementations;

import com.mycompany.movieticketbookingapplication.controllers.interfaces.adminControllersInterfaces.IManageCustomerController;
import com.mycompany.movieticketbookingapplication.models.users.Customer;
import com.mycompany.movieticketbookingapplication.repositories.IUserRepository;
import java.util.List;

public class ManageCustomerController implements IManageCustomerController {
    private final IUserRepository userRepository;
    
    public ManageCustomerController(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void blockCustomer(Customer customer) {
        customer.blockUser();
    }

    @Override
    public void unblockCustomer(Customer customer) {
        customer.unblockUser();
    }

    @Override
    public List<Customer> getAllCustomers() {
        return userRepository.getAllCustomers();
    }
    
}