package com.example.proyectofirebase.models;

import com.google.firebase.database.PropertyName;

public class Item {
    private String id;
    private String nombre;
    private String descripcion;
    private String imagenUrl;

    // Constructor vacío requerido por Firebase
    public Item() {}

    public Item(String id, String nombre, String descripcion, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
    }

    public String getId() { return id; }
    @PropertyName("producto")
    public String getNombre() { return nombre; }
    @PropertyName("descripcion")
    public String getDescripcion() { return descripcion; }
    @PropertyName("imagen")
    public String getImagenUrl() { return imagenUrl; }

}
