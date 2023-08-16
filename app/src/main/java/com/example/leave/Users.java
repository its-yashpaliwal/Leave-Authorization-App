package com.example.leave;

public class Users {
    private String Name, Email, Password, Roll, lastName;
    private boolean Student;
    int Num_app;

    public Users(String name, String email, String password, String roll, String lastName, boolean student, int num_app) {
        Name = name;
        Email = email;
        Password = password;
        Roll = roll;
        this.lastName = lastName;
        Student = student;
        Num_app = num_app;
    }

    public Users() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRoll() {
        return Roll;
    }

    public void setRoll(String roll) {
        Roll = roll;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean getStudent() {
        return Student;
    }

    public void setStudent(boolean student) {
        Student = student;
    }

    public int getNum_app() {
        return Num_app;
    }

    public void setNum_app(int num_app) {
        Num_app = num_app;
    }
}
