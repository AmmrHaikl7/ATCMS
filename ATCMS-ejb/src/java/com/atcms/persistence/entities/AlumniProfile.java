/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atcms.persistence.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Lob;
import javax.persistence.Column;

@Entity
@Table(name = "AlumniProfile")
public class AlumniProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int profileID;

    @OneToOne
    @JoinColumn(name = "userID")
    private User user;

    // --- FROM CLASS DIAGRAM (Identity) ---
    private String matricNo;

    // --- FROM ERD (Academic) ---
    private String graduationYear;
    private String degree;
    private String major;

    // --- MERGED (Career History) ---
    // Maps to 'currentEmployer' in ERD
    @Column(name = "currentEmployer")
    private String currentEmployer;

    // Added for Unified Model (logic from Class Diagram)
    private String jobTitle;

    // --- FROM CLASS DIAGRAM (Skills) ---
    private String skills;

    @Lob // Large Object (for storing the file bytes)
    private byte[] resumeData;

    @Column(length = 100)
    private String resumeFileName;

    // --- GETTERS AND SETTERS ---
    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMatricNo() {
        return matricNo;
    }

    public void setMatricNo(String matricNo) {
        this.matricNo = matricNo;
    }

    public String getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(String graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCurrentEmployer() {
        return currentEmployer;
    }

    public void setCurrentEmployer(String currentEmployer) {
        this.currentEmployer = currentEmployer;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public byte[] getResumeData() {
        return resumeData;
    }

    public void setResumeData(byte[] resumeData) {
        this.resumeData = resumeData;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }
}
