/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atcms.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "jobposting")
public class JobPosting implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jobID")
    private int jobID;

    @Column(name = "jobTitle")
    private String jobTitle;

    @Column(name = "companyName")
    private String companyName;

    @Column(name = "jobDescription")
    private String jobDescription;

    @Column(name = "location")
    private String location;

    @Column(name = "postedByUserID")
    private Integer postedByUserID;

    @Temporal(TemporalType.DATE)
    @Column(name = "postedDate")
    private java.util.Date postedDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "applicationDeadline")
    private java.util.Date applicationDeadline;

    // --- GETTERS AND SETTERS ---
    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPostedByUserID(Integer postedByUserID) {
        this.postedByUserID = postedByUserID;
    }

    public void setPostedDate(java.util.Date postedDate) {
        this.postedDate = postedDate;
    }

    public void setApplicationDeadline(java.util.Date applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public Date getApplicationDeadline() {
        return applicationDeadline;
    }

    public Long getJobId() {
        return Long.valueOf(this.jobID);
    }
    
}
