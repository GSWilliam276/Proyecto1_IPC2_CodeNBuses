/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.entidad;

/**
 *
 * @author eduar
 */
public class Cartera {
    //Atributos
    private int idCartera;
    private Usuario usuario;
    private double saldo;

    public Cartera() {
    }

    public Cartera(Usuario usuario) {
        this.usuario = usuario;
        this.saldo = 0;
    }

    public void recargar(double monto) {
        this.saldo += monto;
    }

    public boolean descontar(double monto) {
        if (monto > this.saldo) {
            return false;
        }
        this.saldo -= monto;
        return true;
    }

    //Getters y setters
    public int getIdCartera() { return idCartera; }
    public void setIdCartera(int idCartera) { this.idCartera = idCartera; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
}
