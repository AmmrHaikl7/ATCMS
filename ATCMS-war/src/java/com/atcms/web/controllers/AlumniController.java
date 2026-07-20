/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atcms.web.controllers;

import com.atcms.ejb.services.AlumniService;
import com.atcms.persistence.entities.AlumniProfile;
import com.atcms.persistence.entities.JobPosting;
import com.atcms.persistence.entities.User;
import com.atcms.web.utilities.JsfUtils;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;

@Named(value = "alumniController")
@SessionScoped
public class AlumniController implements Serializable {

    @EJB
    private AlumniService alumniService;

    private User user;
    private AlumniProfile profile;
    private List<JobPosting> allJobs;
    private JobPosting selectedJob;
    private Part uploadedResume;
    

    // Login form fields
    private String email;
    private String password;

    private Set<Long> appliedJobIds; 

    @PostConstruct
    public void init() {
        this.user = new User();
        this.user.setRole("Alumni");
        this.profile = new AlumniProfile();
        
        // Initialize the list
        this.appliedJobIds = new HashSet<>();
    }
    
    

    public String register() {
    try {
        // 1. Set the email and hash the password
        this.user.setEmail(this.email);
        this.user.setPasswordHash(this.password); 
        this.user.setStatus("Pending");
        
       
        
     
        // We let the JSF Dropdown determine if it is "Alumni" or "Lecturer".

        // 3. Logic: Only save the Profile if they are actually an Alumni
        if ("Alumni".equalsIgnoreCase(this.user.getRole())) {
            // Save User + Profile info
            alumniService.registerAlumni(user, profile);
        } else {
            alumniService.registerAlumni(user, null); 
        }

        JsfUtils.addSuccessMessage("Registration Successful!");

        return "login?faces-redirect=true";
    } catch (Exception e) {
        e.printStackTrace();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration Failed: " + e.getMessage(), null));
        return null;
    }
}

    public String login() {
        User foundUser = alumniService.validateUser(email, password);

        if (foundUser != null) {
            // --- 1. ADMIN BYPASS ---
            // If the user has the 'Admin' role, send them directly to the admin dashboard
            if ("Admin".equalsIgnoreCase(foundUser.getRole())) {
                this.user = foundUser;
                password = null; // Clear password for security
                return "admin_dashboard?faces-redirect=true";
            }

            // --- 2. ALUMNI VERIFICATION CHECK ---
            // If the user is an Alumni, check if they have been 'Verified' by the Admin
            if (!"Verified".equals(foundUser.getStatus())) {
                String statusMsg = "Your account is " + (foundUser.getStatus() == null ? "Pending" : foundUser.getStatus())
                        + ". Please wait for admin approval.";
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, statusMsg, null));
                return null; // Stay on the login page
            }

            if ("Lecturer".equalsIgnoreCase(foundUser.getRole())) {
                this.user = foundUser;
                return "lecturer_dashboard?faces-redirect=true";
            }

            // --- 3. SUCCESSFUL ALUMNI LOGIN ---
            this.user = foundUser;
            this.profile = alumniService.findProfileByUserId(foundUser.getUserID());

            if (this.profile == null) {
                this.profile = new AlumniProfile();
                this.profile.setUser(foundUser);
            }

            password = null;
            return "alumni_dashboard?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Email or Password", null));
            return null;
        }
    }

    public String updateProfile() {
        try {
            alumniService.updateAlumniProfile(profile);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile Updated Successfully", null));

            return "alumni_dashboard?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update Failed", null));
            return null;
        }
    }
    
    public String uploadResume() {
        try {
            if (uploadedResume != null) {
                // Get the filename
                String fileName = uploadedResume.getSubmittedFileName();
                
                // Convert the file content to bytes
                InputStream input = uploadedResume.getInputStream();
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                
                // Save to Profile object
                this.profile.setResumeFileName(fileName);
                this.profile.setResumeData(output.toByteArray());

                // Save to Database
                alumniService.updateAlumniProfile(profile);

                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Resume Uploaded: " + fileName, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upload Failed", null));
        }
        return null; // Stay on the same page
    }

    public List<JobPosting> getAllJobs() {
        if (allJobs == null) {
            allJobs = alumniService.findAllJobs();
        }
        return allJobs;
    }

    public String viewJobDetails(JobPosting job) {
        this.selectedJob = job;
        return "job_details?faces-redirect=true";
    }

    public String applyForJob() {
        if (profile.getResumeData() == null) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please upload a resume first!", null));
            return null;
        }

        // --- NEW LOGIC: Mark as Applied ---
        if (selectedJob != null) {
            appliedJobIds.add(selectedJob.getJobId()); // Add ID to the list
        }

        // Show Success Message
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Application Sent for: " + selectedJob.getJobTitle(), null));

        return "browse_jobs?faces-redirect=true";
    }

    // 3. New Helper Method for the HTML page
    // This checks if a specific job ID is inside our "Applied" list
    public boolean hasApplied(Long jobId) {
        return appliedJobIds != null && appliedJobIds.contains(jobId);
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login?faces-redirect=true";
    }

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AlumniProfile getProfile() {
        return profile;
    }

    public void setProfile(AlumniProfile profile) {
        this.profile = profile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JobPosting getSelectedJob() {
        return selectedJob;
    }

    public void setSelectedJob(JobPosting selectedJob) {
        this.selectedJob = selectedJob;
    }

    public Part getUploadedResume() {
        return uploadedResume;
    }

    public void setUploadedResume(Part uploadedResume) {
        this.uploadedResume = uploadedResume;
    }
    
    
}
