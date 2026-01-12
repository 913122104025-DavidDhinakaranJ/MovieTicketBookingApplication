package com.mycompany.movieticketbookingapplication.views.adminViews;

import com.mycompany.authlib.controller.AuthController;
import com.mycompany.authlib.views.ConsoleAuthView;
import com.mycompany.authlib.views.IAuthView;
import com.mycompany.movieticketbookingapplication.contexts.ApplicationContext;
import com.mycompany.movieticketbookingapplication.controllers.interfaces.adminControllersInterfaces.IManageAdminController;
import com.mycompany.movieticketbookingapplication.enums.Privilege;
import com.mycompany.movieticketbookingapplication.enums.menuOptions.adminMenuOptions.AdminManageOption;
import com.mycompany.movieticketbookingapplication.models.users.Admin;
import com.mycompany.movieticketbookingapplication.utils.ConsoleInputUtil;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class ConsoleManageAdminView {
    private final ConsoleInputUtil inputReader;
    private final ApplicationContext appContext;
    private final IManageAdminController manageAdminController;
    
    private boolean running;
    
    public ConsoleManageAdminView(IManageAdminController manageAdminController) {
        this.inputReader = new ConsoleInputUtil();
        this.appContext = ApplicationContext.getInstance();
        this.manageAdminController = manageAdminController;
    }
    
    public void runManageAdminView() {
        running = true;
        while(running) {
            AdminManageOption choice = getAdminManageOption();
            switch(choice) {
                case ADD_ADMIN -> handleAddAdmin();
                case VIEW_ADMINS -> handleViewAdmins();
                case EXIT -> handleExit();
                case INVALID -> handleInvalidChoice();
            }
        }
    }
    
    private AdminManageOption getAdminManageOption() {
        System.out.println("1. Add New Admin");
        System.out.println("2. View Admins");
        System.out.println("0. Exit");
        
        return switch(inputReader.readInt("Enter choice: ")) {
            case 1 -> AdminManageOption.ADD_ADMIN;
            case 2 -> AdminManageOption.VIEW_ADMINS;
            case 0 -> AdminManageOption.EXIT;
            default -> AdminManageOption.INVALID;
        };
    }
    
    private AdminManageOption getAdminManageOption(Admin admin) {
        boolean isBlockedAdmin = admin.isBlocked();
        System.out.println("1. Manage Privileges");
        System.out.println("2. " + (isBlockedAdmin ? "Unblock Admin" :"Block Admin"));
        System.out.println("0. Back");
        
        return switch(inputReader.readInt("Enter choice: ")) {
            case 1 -> AdminManageOption.MANAGE_PRIVILEGES;
            case 2 -> isBlockedAdmin ? AdminManageOption.UNBLOCK_ADMIN : AdminManageOption.BLOCK_ADMIN;
            case 0 -> AdminManageOption.EXIT;
            default -> AdminManageOption.INVALID;
        };
    }

    private void handleAddAdmin() {
        IAuthView authView = new ConsoleAuthView(new AuthController(appContext.getUserRepository(), appContext.getAdminFactory()));
        authView.handleRegistration();
    }
    
    private void handleViewAdmins() {
        Admin currentAdmin = getAdmin();
        if(currentAdmin == null) return;
        
        displayAdminDetails(currentAdmin);
        
        while(currentAdmin != null) {
            AdminManageOption choice = getAdminManageOption(currentAdmin);
            switch(choice) {
                case MANAGE_PRIVILEGES -> handleManagePrivileges(currentAdmin);
                case BLOCK_ADMIN -> handleBlockAdmin(currentAdmin);
                case UNBLOCK_ADMIN -> handleUnblockAdmin(currentAdmin);
                case EXIT -> currentAdmin = null;
                case INVALID -> handleInvalidChoice();
            }
        }
    }

    private void handleManagePrivileges(Admin admin) {
        List<Privilege> existingPrivileges = admin.getPrivileges();
        List<Privilege> nonExistingPrivileges = Arrays.stream(Privilege.values()).filter(p -> !existingPrivileges.contains(p)).toList();
        
        Set<Privilege> grantPrivileges = EnumSet.noneOf(Privilege.class);
        Set<Privilege> revokePrivileges = EnumSet.noneOf(Privilege.class);
        
        if(!nonExistingPrivileges.isEmpty()) {
            System.out.println("Grant Privileges: ");
            grantPrivileges = getPrivileges(nonExistingPrivileges);
        }
        
        if(!existingPrivileges.isEmpty()) {
            System.out.println("Revoke Privileges: ");
            revokePrivileges = getPrivileges(existingPrivileges);
        }
        
        manageAdminController.updatePrivileges(admin, grantPrivileges, revokePrivileges);
        
        System.out.println("Privileges updated Successfully.");
    }

    private void handleBlockAdmin(Admin admin) {
        manageAdminController.blockAdmin(admin);
    }

    private void handleUnblockAdmin(Admin admin) {
        manageAdminController.unblockAdmin(admin);
    }

    private void handleExit() {
        running = false;
    }

    private void handleInvalidChoice() {
        displayError("Invalid Choice");
    }
    
    private Admin getAdmin() {
        List<Admin> admins = manageAdminController.getAllAdmins();
        
        for(int i = 0; i < admins.size();i++) {
            System.out.println(i + 1 + ". " + admins.get(i).getUsername());
        }
        System.out.println("0. Back");
        
        while(true) {
            int choice = inputReader.readInt("Enter Admin Choice: ");
            if(choice == 0) return null;

            if(choice < 1 || choice > admins.size()) {
                displayError("Invalid Admin Choice");
                continue;
            }
            
            return admins.get(choice - 1);
        }
    }
    
    private Set<Privilege> getPrivileges(List<Privilege> privileges) {
        for(int i = 0; i < privileges.size();i++) {
            System.out.println(i + 1 + ". " + privileges.get(i));
        }
        
        String privilegesChoice = inputReader.readString("Enter Privilege Choices (Space Seperated): ", true);
        if(privilegesChoice.isBlank()) return EnumSet.noneOf(Privilege.class);
        
        Set<Privilege> privilegeList = EnumSet.noneOf(Privilege.class);
        for(String choice : privilegesChoice.trim().split("\\s+")) {
            try {
                int index = Integer.parseInt(choice);

                if(index <= 0 || index > privileges.size()) {
                    displayError("Invalid Privilege Choice: " + index + " Omitted");
                    continue;
                }
                
                privilegeList.add(privileges.get(index - 1));
            } catch(NumberFormatException e) {
                displayError("Invalid input");
            }
        }
        
        return privilegeList;
    }
    
    private void displayAdminDetails(Admin admin) {
        System.out.println("Admin UserName: " + admin.getUsername());
        System.out.println("Status: " + (admin.isBlocked() ? "Blocked" : "Active"));
        System.out.println("Privileges: ");
        for(Privilege privilege : Privilege.values()) {
            System.out.println((admin.hasPrivilege(privilege) ? "+ " : "- ") + privilege);
        }
    }
    
    private void displayError(String message) {
        System.out.println("Error: " + message);
    }
}