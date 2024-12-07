package com.hospital.tm.model;

public class ArchivedPatient {
    private Patient patient;
    private String completedDate;

    public ArchivedPatient(String completedDate,Patient patient) {
        this.patient = patient;
        this.completedDate = completedDate;
    }

    public ArchivedPatient() {
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }
}
