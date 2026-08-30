/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.entidad;

import java.util.Date;
/**
 *
 * @author eduar
 */
public class Gasto {
    //Atributos
    private int idGasto;
    private Bus bus;
    private double montoManoObra;
    private double montoRepuestos;
    private Date fecha;

    public Gasto() {
    }

    public Gasto(Bus bus, double montoManoObra, double montoRepuestos, Date fecha) {
        this.bus = bus;
        this.montoManoObra = montoManoObra;
        this.montoRepuestos = montoRepuestos;
        this.fecha = fecha;
    }

    public double getMontoTotal() {
        return montoManoObra + montoRepuestos;
    }

    //Getters y setters
    public int getIdGasto() { return idGasto; }
    public void setIdGasto(int idGasto) { this.idGasto = idGasto; }

    public Bus getBus() { return bus; }
    public void setBus(Bus bus) { this.bus = bus; }

    public double getMontoManoObra() { return montoManoObra; }
    public void setMontoManoObra(double montoManoObra) { this.montoManoObra = montoManoObra; }

    public double getMontoRepuestos() { return montoRepuestos; }
    public void setMontoRepuestos(double montoRepuestos) { this.montoRepuestos = montoRepuestos; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
}
