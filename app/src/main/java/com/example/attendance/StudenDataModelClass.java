package com.example.attendance;

public class StudenDataModelClass {


    private String Name;
    private String Status;

    public StudenDataModelClass(String name, String status) {
        Name = name;
        Status = status;
    }

    public StudenDataModelClass(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}

