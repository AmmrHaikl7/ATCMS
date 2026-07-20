package com.atcms.web.controllers;

import com.atcms.persistence.entities.JobPosting;
import com.atcms.session.LecturerSessionLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AnalyticsServlet", urlPatterns = {"/AnalyticsServlet"})
public class AnalyticsServlet extends HttpServlet {

    @EJB
    private LecturerSessionLocal lecturerSession;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Fetch Data using the NEW method in LecturerSession
        List<JobPosting> allJobs = lecturerSession.getAllJobPostings(); 
        
        // 2. Calculate Statistics
        int totalJobs = allJobs.size();
        
        long totalCompanies = allJobs.stream()
                .map(JobPosting::getCompanyName)
                .distinct()
                .count();

        // 3. Send to JSP
        request.setAttribute("totalJobsCount", totalJobs);
        request.setAttribute("totalCompaniesCount", totalCompanies);
        request.setAttribute("jobList", allJobs); 

        request.getRequestDispatcher("analytics.xhtml").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}