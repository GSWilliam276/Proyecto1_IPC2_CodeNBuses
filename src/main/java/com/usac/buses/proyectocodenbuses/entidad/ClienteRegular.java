/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.entidad;

/**
 *
 * @author eduar
 */
public class ClienteRegular extends Usuario {
    public ClienteRegular() {
        super();
    }

    public ClienteRegular(String nombre, String nit, String dpi, String telefono, String direccion, String correo, String contrasena) {
        super(nombre, nit, dpi, telefono, direccion, correo, contrasena);
    }
}
