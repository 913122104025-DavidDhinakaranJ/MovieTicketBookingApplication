package com.mycompany.movieticketbookingapplication.views.adminViews;

import com.mycompany.movieticketbookingapplication.controllers.interfaces.adminControllersInterfaces.IManageCustomerController;
import com.mycompany.movieticketbookingapplication.enums.menuOptions.adminMenuOptions.CustomerManageOption;
import com.mycompany.movieticketbookingapplication.models.users.Customer;
import com.mycompany.movieticketbookingapplication.utils.ConsoleInputUtil;
import java.util.List;

public class ConsoleManageCustomerView {
    private final ConsoleInputUtil inputReader;
    private final IManageCustomerController manageCustomerController;
    
    private boolean running;
    
    public ConsoleManageCustomerView(IManageCustomerController manageCustomerController) {
        inputReader = new ConsoleInputUtil();
        this.manageCustomerController = manageCustomerController;
    }
    
    public void runManageCustomerView() {
        running = true;
        while(running) {
            Customer currentCustomer = getCustomer();
            if(currentCustomer == null) handleExit();
            
            while(currentCustomer != null) {
                CustomerManageOption choice = getCustomerManageOption(currentCustomer);
                switch(choice) {
                    case BLOCK -> handleBlockCustomer(currentCustomer);
                    case UNBLOCK -> handleUnblockCustomer(currentCustomer);
                    case BACK -> currentCustomer = null;
                    case INVALID -> handleInvalidChoice();
                }
            }
        }
    }
    
    private CustomerManageOption getCustomerManageOption(Customer customer) {
        boolean isBlockedCustomer = customer.isBlocked();
        System.out.println("1. " + (isBlockedCustomer ? "Unblock Customer" : "Block Customer"));
        System.out.println("0. Back");
        
        return switch(inputReader.readInt("Enter choice: ")) {
            case 1 -> isBlockedCustomer ? CustomerManageOption.UNBLOCK : CustomerManageOption.BLOCK;
            case 0 -> CustomerManageOption.BACK;
            default -> CustomerManageOption.INVALID;
        };
    }
    
    private void handleBlockCustomer(Customer customer) {
        manageCustomerController.blockCustomer(customer);
    }

    private void handleUnblockCustomer(Customer customer) {
        manageCustomerController.unblockCustomer(customer);
    }

    private void handleExit() {
        running = false;
    }

    private void handleInvalidChoice() {
        displayError("Invalid Choice");
    }
    
    private Customer getCustomer() {
        List<Customer> customers = manageCustomerController.getAllCustomers();
        
        for(int i = 0; i < customers.size();i++) {
            System.out.println(i + 1 + ". " + customers.get(i).getUsername());
        }
        System.out.println("0. Back");
        
        while(true) {
            int choice = inputReader.readInt("Enter Customer Choice: ");
            if(choice == 0) return null;

            if(choice < 1 || choice > customers.size()) {
                displayError("Invalid Customer Choice");
                continue;
            }
            
            return customers.get(choice - 1);
        }
    }
    
    private void displayError(String message) {
        System.out.println("Error: " + message);
    }
}