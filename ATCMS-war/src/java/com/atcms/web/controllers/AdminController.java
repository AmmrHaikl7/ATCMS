package com.atcms.web.controllers;

import com.atcms.ejb.services.AlumniService;
import com.atcms.persistence.entities.User;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named(value = "adminController")
@SessionScoped
public class AdminController implements Serializable {

    @EJB
    private AlumniService alumniService;

    // --- METHOD: For the top "Pending" table ---
    public List<User> getPendingUsers() {
        return alumniService.findPendingUsers();
    }

    // --- METHOD: For the bottom "User Directory" table ---
    public List<User> getAllUsers() {
        return alumniService.findAllUsers(); 
    }

    // --- ACTIONS ---
    public String approveUser(int userId) {
        alumniService.verifyUser(userId);
        return "admin_dashboard?faces-redirect=true";
    }

    public String rejectUser(int userId) {
        alumniService.rejectUser(userId);
        return "admin_dashboard?faces-redirect=true";
    }

    public String deleteUser(int userId) {
        alumniService.deleteUser(userId);
        return "admin_dashboard?faces-redirect=true";
    }
}