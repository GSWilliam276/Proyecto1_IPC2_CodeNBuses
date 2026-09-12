/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.entidad;

/**
 *
 * @author eduar
 */
public class Sucursal {
    //Atributos
    private int idSucursal;
    private String nombre;
    private String ubicacion;
    private double latitud;
    private double longitud;

    public Sucursal() {
    }

    public Sucursal(String nombre, String ubicacion, double latitud, double longitud) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    //Getters y Setters
    public int getIdSucursal() { return idSucursal; }
    public void setIdSucursal(int idSucursal) { this.idSucursal = idSucursal; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
}
