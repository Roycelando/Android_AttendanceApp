package com.example.attendance;

public class DBHelper {

    String Name, role, username, password, location;

    public DBHelper(String Name, String role, String username, String password, String location) {
        this.Name = Name;
        this.role = role;
        this.username = username;
        this.password = password;
        this.location = location;
    }

    public DBHelper() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String fName) {
        this.Name = fName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void getLocation(String location){
        this.location = location;
    }

    public String getLocation(){
        return location;
    }
}



