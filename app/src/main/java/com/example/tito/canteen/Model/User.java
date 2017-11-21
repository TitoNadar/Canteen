package com.example.tito.canteen.Model;

/**
 * Created by tito on 26/10/17.
 */

public class User {
    private String name;
    private String password;
    private String phone;
    private String IsStaff;

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String name, String password) {
        this.name = name;
       this.password = password;
        IsStaff="false";
    }

    public User() {
    }
}
