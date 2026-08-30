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
public class MovimientoCartera {
    //Atributos
    private int idMovimiento;
    private Cartera cartera;
    private TipoMovimiento tipo;
    private double monto;
    private Date fecha;

    public MovimientoCartera() {
    }

    public MovimientoCartera(Cartera cartera, TipoMovimiento tipo, double monto, Date fecha) {
        this.cartera = cartera;
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = fecha;
    }

    //Getters y setters
    public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }

    public Cartera getCartera() { return cartera; }
    public void setCartera(Cartera cartera) { this.cartera = cartera; }

    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
}
