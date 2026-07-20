package com.atcms.web.controllers;

import com.atcms.persistence.entities.JobPosting; 
import com.atcms.persistence.entities.User;       
import com.atcms.session.LecturerSessionLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LecturerServlet", urlPatterns = {"/LecturerServlet"})
public class LecturerServlet extends HttpServlet {

    @EJB
    private LecturerSessionLocal lecturerSession;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        try {
            // --- STEP 1: CAPTURE DATA ---
            String title = request.getParameter("jobTitle");
            String company = request.getParameter("companyName");
            String loc = request.getParameter("location");
            String desc = request.getParameter("description");
            String deadlineStr = request.getParameter("deadline");

            // --- STEP 2: PARSE DATE ---
            Date deadlineDate = null;
            if (deadlineStr != null && !deadlineStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                deadlineDate = sdf.parse(deadlineStr);
            } else {
                throw new Exception("Date is empty!");
            }
            
            // --- STEP 3: CREATE ENTITY ---
            JobPosting newJob = new JobPosting();
            newJob.setJobTitle(title);
            newJob.setCompanyName(company);
            newJob.setLocation(loc);
            newJob.setJobDescription(desc);
            newJob.setApplicationDeadline(deadlineDate);
            newJob.setPostedDate(new Date());

            // --- STEP 4: HANDLE USER ID ---
            HttpSession session = request.getSession(false);
            User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
            
            if (currentUser != null) {
                newJob.setPostedByUserID(currentUser.getUserID());
            } else {
                // Fallback for testing
                newJob.setPostedByUserID(4); 
            }

            // --- STEP 5: SAVE TO DB ---
            lecturerSession.createJobPosting(newJob);

            // --- STEP 6: SUCCESS & REDIRECT ---
            // 1. Pack the data so the XHTML page can display it
            request.setAttribute("postedTitle", title);
            request.setAttribute("postedCompany", company);
            
            // 2. Forward to the Success Page
            request.getRequestDispatcher("post_job_success.xhtml").forward(request, response);

        } catch (Exception e) {
            // --- ERROR HANDLING ---
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                e.printStackTrace();
                out.println("<html><body>");
                out.println("<h1>Error Occurred!</h1>");
                out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
                out.println("<pre>");
                e.printStackTrace(out);
                out.println("</pre>");
                out.println("<br><a href='newJob.xhtml'>Go Back</a>");
                out.println("</body></html>");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}