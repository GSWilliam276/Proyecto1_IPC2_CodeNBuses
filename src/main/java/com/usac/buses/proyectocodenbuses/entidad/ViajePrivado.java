/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.entidad;

import java.util.Date;
import com.usac.buses.proyectocodenbuses.utilidades.Constantes;
/**
 *
 * @author eduar
 */
public class ViajePrivado extends Viaje {
    //Atributos
    private String origen;
    private String destino;
    private int pasajeros;
    private double precioEstimado;
    private double precioConfirmado;
    private Usuario solicitante;

    public ViajePrivado() {
        super();
    }

    public ViajePrivado(Bus bus, Chofer chofer, Date fechaHoraSalida, Date fechaHoraLlegadaEstimada,
                         String origen, String destino, int pasajeros, double precioEstimado) {
        super(bus, chofer, fechaHoraSalida, fechaHoraLlegadaEstimada);
        this.origen = origen;
        this.destino = destino;
        this.pasajeros = pasajeros;
        this.precioEstimado = precioEstimado;
    }

    @Override
    public double calcularSalarioChofer() {
        return getChofer().getSalarioBase() * Constantes.PORCENTAJE_EXTRA_PRIVADO;
    }

    public void confirmarPrecio(double monto) {
        this.precioConfirmado = monto;
    }

    //Getters y setters
    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public int getPasajeros() { return pasajeros; }
    public void setPasajeros(int pasajeros) { this.pasajeros = pasajeros; }

    public double getPrecioEstimado() { return precioEstimado; }
    public void setPrecioEstimado(double precioEstimado) { this.precioEstimado = precioEstimado; }

    public double getPrecioConfirmado() { return precioConfirmado; }
    public void setPrecioConfirmado(double precioConfirmado) { this.precioConfirmado = precioConfirmado; }
    
    public Usuario getSolicitante() { return solicitante; }
    public void setSolicitante(Usuario solicitante) { this.solicitante = solicitante; }
}
