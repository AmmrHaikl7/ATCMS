package com.atcms.web.controllers;

import com.atcms.persistence.entities.JobPosting;
import com.atcms.session.LecturerSessionLocal;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ReportServlet", urlPatterns = {"/ReportServlet"})
public class ReportServlet extends HttpServlet {

    @EJB
    private LecturerSessionLocal lecturerSession;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Fetch Data
        List<JobPosting> jobs = lecturerSession.getAllJobPostings();
        
        // 2. Prepare Data for the Report
        request.setAttribute("jobList", jobs);
        request.setAttribute("totalJobsCount", jobs.size());
        request.setAttribute("generatedDate", new Date()); // Send today's date
        
        // 3. Forward to the JSF Page
        // "Forward" keeps the data alive. "Redirect" kills it.
        request.getRequestDispatcher("report.xhtml").forward(request, response);
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