/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.entidad;

/**
 *
 * @author eduar
 */
public class Ruta {
    //Atributos
    private int idRuta;
    private Sucursal sucursalOrigen;
    private Sucursal sucursalDestino;
    private double distanciaKm;
    private double precioBoleto;

    public Ruta() {
    }

    public Ruta(Sucursal sucursalOrigen, Sucursal sucursalDestino, double distanciaKm, double precioBoleto) {
        this.sucursalOrigen = sucursalOrigen;
        this.sucursalDestino = sucursalDestino;
        this.distanciaKm = distanciaKm;
        this.precioBoleto = precioBoleto;
    }

    public boolean tieneViajesAsociados() {
        //logica que luego ira en RutaPersistencia,
        return false;
    }

    //Getters y setters
    public int getIdRuta() { return idRuta; }
    public void setIdRuta(int idRuta) { this.idRuta = idRuta; }

    public Sucursal getSucursalOrigen() { return sucursalOrigen; }
    public void setSucursalOrigen(Sucursal sucursalOrigen) { this.sucursalOrigen = sucursalOrigen; }

    public Sucursal getSucursalDestino() { return sucursalDestino; }
    public void setSucursalDestino(Sucursal sucursalDestino) { this.sucursalDestino = sucursalDestino; }

    public double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(double distanciaKm) { this.distanciaKm = distanciaKm; }

    public double getPrecioBoleto() { return precioBoleto; }
    public void setPrecioBoleto(double precioBoleto) { this.precioBoleto = precioBoleto; }
}
