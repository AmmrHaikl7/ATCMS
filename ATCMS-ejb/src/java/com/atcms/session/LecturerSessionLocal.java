/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package com.atcms.session;

import com.atcms.persistence.entities.JobPosting; 
import java.util.List;
import javax.ejb.Local;

@Local
public interface LecturerSessionLocal {
    
    // Method to save a new job posting to MySQL
    void createJobPosting(JobPosting job);
    
    List<JobPosting> getAllJobPostings();

    // Method to get total count for the dashboard
    int getJobCount();

    // Method to get summary data for the jobs table
    List<Object[]> getAllJobsSummary();
}
