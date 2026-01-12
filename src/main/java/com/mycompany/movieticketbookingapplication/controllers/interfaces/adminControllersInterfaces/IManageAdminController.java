package com.mycompany.movieticketbookingapplication.controllers.interfaces.adminControllersInterfaces;

import com.mycompany.movieticketbookingapplication.enums.Privilege;
import com.mycompany.movieticketbookingapplication.models.users.Admin;
import java.util.List;
import java.util.Set;

public interface IManageAdminController {
    List<Admin> getAllAdmins();
    
    void updatePrivileges(Admin admin, Set<Privilege> grantPrivileges, Set<Privilege> revokePrivileges);
    void blockAdmin(Admin admin);
    void unblockAdmin(Admin admin);
}