package com.example.proyectofirebase.models;

public class User {
    private String email;
    private String password;

    // Constructor vacío requerido por Firebase
    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
