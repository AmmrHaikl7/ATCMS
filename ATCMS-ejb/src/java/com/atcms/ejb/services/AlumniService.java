/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atcms.ejb.services;

import com.atcms.persistence.entities.AlumniProfile;
import com.atcms.persistence.entities.JobPosting;
import com.atcms.persistence.entities.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException; 
import javax.persistence.PersistenceContext;

@Stateless
public class AlumniService {

    @PersistenceContext(unitName = "ATCMS-PU")
    private EntityManager em;

    public void registerAlumni(User user, AlumniProfile profile) {
        em.persist(user); // Always save the user
        
        // SAFETY CHECK: Only save profile if it exists
        if (profile != null) { 
            profile.setUser(user);
            em.persist(profile);
        }
    }

    public User validateUser(String email, String password) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.passwordHash = :password", User.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void updateAlumniProfile(AlumniProfile profile) {
        em.merge(profile);
    }

    public AlumniProfile findProfileByUserId(int userId) {
        try {
            return em.createQuery("SELECT p FROM AlumniProfile p WHERE p.user.userID = :uid", AlumniProfile.class)
                    .setParameter("uid", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<JobPosting> findAllJobs() {
        return em.createQuery("SELECT j FROM JobPosting j", JobPosting.class).getResultList();
    }

    public List<User> findPendingUsers() {
        return em.createQuery("SELECT u FROM User u WHERE u.status = 'Pending'", User.class).getResultList();
    }

    // --- NEW METHOD ADDED HERE ---
    // This allows the Admin to see everyone (Verified, Pending, Rejected)
    public List<User> findAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }
    // -----------------------------

    public void verifyUser(int userId) {
        User u = em.find(User.class, userId);
        if (u != null) {
            u.setStatus("Verified");
            em.merge(u); 
        }
    }

    public void rejectUser(int userId) {
        User u = em.find(User.class, userId);
        if (u != null) {
            u.setStatus("Rejected"); 
            em.merge(u);
        }
    }

    public void deleteUser(int userId) {
        User u = em.find(User.class, userId);
        if (u != null) {
            em.remove(u);
        }
    }
}