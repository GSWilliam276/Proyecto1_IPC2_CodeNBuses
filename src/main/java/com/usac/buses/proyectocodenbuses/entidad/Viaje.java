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
public abstract class Viaje  {
    //Atributos
    private int idViaje;
    private Bus bus;
    private Chofer chofer;
    private Date fechaHoraSalida;
    private Date fechaHoraLlegadaEstimada;

    public Viaje() {
    }

    public Viaje(Bus bus, Chofer chofer, Date fechaHoraSalida, Date fechaHoraLlegadaEstimada) {
        this.bus = bus;
        this.chofer = chofer;
        this.fechaHoraSalida = fechaHoraSalida;
        this.fechaHoraLlegadaEstimada = fechaHoraLlegadaEstimada;
    }

    public abstract double calcularSalarioChofer();

    //Getters y setters
    public int getIdViaje() { return idViaje; }
    public void setIdViaje(int idViaje) { this.idViaje = idViaje; }

    public Bus getBus() { return bus; }
    public void setBus(Bus bus) { this.bus = bus; }

    public Chofer getChofer() { return chofer; }
    public void setChofer(Chofer chofer) { this.chofer = chofer; }

    public Date getFechaHoraSalida() { return fechaHoraSalida; }
    public void setFechaHoraSalida(Date fechaHoraSalida) { this.fechaHoraSalida = fechaHoraSalida; }

    public Date getFechaHoraLlegadaEstimada() { return fechaHoraLlegadaEstimada; }
    public void setFechaHoraLlegadaEstimada(Date fechaHoraLlegadaEstimada) { this.fechaHoraLlegadaEstimada = fechaHoraLlegadaEstimada; }
}
