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
public class Chofer extends Usuario {
    //Atributos
    private String numeroLicencia;
    private TipoLicencia tipoLicencia;
    private Date fechaVencimiento;
    private double salarioBase;
    private Sucursal sucursal;

    public Chofer() {
        super();
    }

    public Chofer(String nit, String dpi, String telefono, String direccion, String correo, String contrasena,
                  String numeroLicencia, TipoLicencia tipoLicencia, Date fechaVencimiento, double salarioBase, Sucursal sucursal) {
        super(nit, dpi, telefono, direccion, correo, contrasena);
        this.numeroLicencia = numeroLicencia;
        this.tipoLicencia = tipoLicencia;
        this.fechaVencimiento = fechaVencimiento;
        this.salarioBase = salarioBase;
        this.sucursal = sucursal;
    }

    //Getters y Setters
    public String getNumeroLicencia() { return numeroLicencia; }
    public void setNumeroLicencia(String numeroLicencia) { this.numeroLicencia = numeroLicencia; }

    public TipoLicencia getTipoLicencia() { return tipoLicencia; }
    public void setTipoLicencia(TipoLicencia tipoLicencia) { this.tipoLicencia = tipoLicencia; }

    public Date getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(Date fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public double getSalarioBase() { return salarioBase; }
    public void setSalarioBase(double salarioBase) { this.salarioBase = salarioBase; }

    public Sucursal getSucursal() { return sucursal; }
    public void setSucursal(Sucursal sucursal) { this.sucursal = sucursal; }
}
