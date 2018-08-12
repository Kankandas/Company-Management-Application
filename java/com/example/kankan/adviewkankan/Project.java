package com.example.kankan.adviewkankan;

public class Project {
    private String projectDetails,projectName,submissionDate,uploadDate,projectImage,depertment,id,projectStatus;

    public Project() {
    }

    public Project(String projectDetails, String projectName, String submissionDate, String uploadDate, String projectImage, String depertment, String id
                    ,String projectStatus) {
        this.projectDetails = projectDetails;
        this.projectName = projectName;
        this.submissionDate = submissionDate;
        this.uploadDate = uploadDate;
        this.projectImage = projectImage;
        this.depertment = depertment;
        this.id = id;
        this.projectStatus=projectStatus;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(String projectDetails) {
        this.projectDetails = projectDetails;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public String getDepertment() {
        return depertment;
    }

    public void setDepertment(String depertment) {
        this.depertment = depertment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
