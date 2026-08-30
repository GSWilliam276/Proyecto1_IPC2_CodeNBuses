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
public class ViajeRegular extends Viaje{
    //Atributo
    private Ruta ruta;

    public ViajeRegular() {
        super();
    }

    public ViajeRegular(Bus bus, Chofer chofer, Date fechaHoraSalida, Date fechaHoraLlegadaEstimada, Ruta ruta) {
        super(bus, chofer, fechaHoraSalida, fechaHoraLlegadaEstimada);
        this.ruta = ruta;
    }

    @Override
    public double calcularSalarioChofer() {
        return getChofer().getSalarioBase();
    }

    //Getter y setter
    public Ruta getRuta() { return ruta; }
    public void setRuta(Ruta ruta) { this.ruta = ruta; }
}
