package com.example.attendance;

public class StudentDataModelClass {


    private String Name;
    private String Status;

    public StudentDataModelClass(String name, String status) {
        Name = name;
        Status = status;
    }

    public StudentDataModelClass(){

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
