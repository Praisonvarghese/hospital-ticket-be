package com.hospital.tm.model;

public class Patient {
    private int id;
    private String name;
    private String ailment;

    public Patient(int id, String name, String ailment) {
        this.id = id;
        this.name = name;
        this.ailment = ailment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAilment() {
        return ailment;
    }

    public void setAilment(String ailment) {
        this.ailment = ailment;
    }
}