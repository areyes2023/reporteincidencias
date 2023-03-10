package com.aplicacion.incidencias.model;

public class Incidencias {
    private String descripcion;
    private String imagen;

    public Incidencias() {
    }

    public Incidencias(String descripcion, String imagen, Integer id) {
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
