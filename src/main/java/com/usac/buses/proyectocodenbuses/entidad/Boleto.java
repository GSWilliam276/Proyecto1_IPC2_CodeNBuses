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
public class Boleto {
    //Atributos
    private int idBoleto;
    private ViajeRegular viaje;
    private ClienteRegular cliente;
    private int numeroAsiento;
    private Date fechaPago;
    private double precio;

    public Boleto() {
    }

    public Boleto(ViajeRegular viaje, ClienteRegular cliente, int numeroAsiento, Date fechaPago, double precio) {
        this.viaje = viaje;
        this.cliente = cliente;
        this.numeroAsiento = numeroAsiento;
        this.fechaPago = fechaPago;
        this.precio = precio;
    }

    //Getters y setters
    public int getIdBoleto() { return idBoleto; }
    public void setIdBoleto(int idBoleto) { this.idBoleto = idBoleto; }

    public ViajeRegular getViaje() { return viaje; }
    public void setViaje(ViajeRegular viaje) { this.viaje = viaje; }

    public ClienteRegular getCliente() { return cliente; }
    public void setCliente(ClienteRegular cliente) { this.cliente = cliente; }

    public int getNumeroAsiento() { return numeroAsiento; }
    public void setNumeroAsiento(int numeroAsiento) { this.numeroAsiento = numeroAsiento; }

    public Date getFechaPago() { return fechaPago; }
    public void setFechaPago(Date fechaPago) { this.fechaPago = fechaPago; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}
