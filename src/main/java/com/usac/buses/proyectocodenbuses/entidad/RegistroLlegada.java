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
public class RegistroLlegada { //Inmutable
    //Atributos
    private final int idRegistroLlegada;
    private final Viaje viaje;
    private final Date horaLlegadaReal;
    private final double kilometrajeLlegada;
    private final double gastoCombustible;
    private final double depreciacionCalculada;

    public RegistroLlegada(Viaje viaje, Date horaLlegadaReal, double kilometrajeLlegada,
                            double gastoCombustible, double montoDepreciacionPorKm) {
        this.idRegistroLlegada = 0;
        this.viaje = viaje;
        this.horaLlegadaReal = horaLlegadaReal;
        this.kilometrajeLlegada = kilometrajeLlegada;
        this.gastoCombustible = gastoCombustible;
        this.depreciacionCalculada = calcularDepreciacion(kilometrajeLlegada, montoDepreciacionPorKm);
    }

    private double calcularDepreciacion(double km, double montoPorKm) {
        return km * montoPorKm;
    }

    //Sin setters solo getters
    public int getIdRegistroLlegada() { return idRegistroLlegada; }
    public Viaje getViaje() { return viaje; }
    public Date getHoraLlegadaReal() { return horaLlegadaReal; }
    public double getKilometrajeLlegada() { return kilometrajeLlegada; }
    public double getGastoCombustible() { return gastoCombustible; }
    public double getDepreciacionCalculada() { return depreciacionCalculada; }
}
