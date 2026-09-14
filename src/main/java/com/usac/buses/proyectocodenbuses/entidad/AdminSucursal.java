/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.entidad;

/**
 *
 * @author eduar
 */
public class AdminSucursal extends Usuario {
    //Atributo
    private Sucursal sucursal;

    public AdminSucursal() {
        super();
    }

    public AdminSucursal(String nombre, String nit, String dpi, String telefono, String direccion, String correo, String contrasena, Sucursal sucursal) {
        super(nombre, nit, dpi, telefono, direccion, correo, contrasena);
        this.sucursal = sucursal;
    }

    public Sucursal getSucursal() { return sucursal; }
    public void setSucursal(Sucursal sucursal) { this.sucursal = sucursal; }
}
