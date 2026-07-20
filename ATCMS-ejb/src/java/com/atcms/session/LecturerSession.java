/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package com.atcms.session;

import com.atcms.persistence.entities.JobPosting; 
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class LecturerSession implements LecturerSessionLocal {

    @PersistenceContext(unitName = "ATCMS-PU") 
    private EntityManager em;

    @Override
    public void createJobPosting(JobPosting job) {
        em.persist(job);
    }

    @Override
    public List<JobPosting> getAllJobPostings() {
        
        return em.createQuery("SELECT j FROM JobPosting j", JobPosting.class).getResultList();
    }
    
    @Override
    public int getJobCount() {
        Query q = em.createQuery("SELECT count(j) FROM JobPosting j");
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public List<Object[]> getAllJobsSummary() {
        Query q = em.createQuery("SELECT j.jobTitle, j.companyName, j.location, j.applicationDeadline FROM JobPosting j ORDER BY j.postedDate DESC");
        return q.getResultList();
    }
}
