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
public class RegistroSalida { //Inmutable
    //Atributos
    private final int idRegistroSalida;
    private final Viaje viaje;
    private final Date horaSalidaReal;
    private final double kilometrajeSalida;

    public RegistroSalida(Viaje viaje, Date horaSalidaReal, double kilometrajeSalida) {
        this.idRegistroSalida = 0;
        this.viaje = viaje;
        this.horaSalidaReal = horaSalidaReal;
        this.kilometrajeSalida = kilometrajeSalida;
    }

    //Sin setters solo getters
    public int getIdRegistroSalida() { return idRegistroSalida; }
    public Viaje getViaje() { return viaje; }
    public Date getHoraSalidaReal() { return horaSalidaReal; }
    public double getKilometrajeSalida() { return kilometrajeSalida; }
}
