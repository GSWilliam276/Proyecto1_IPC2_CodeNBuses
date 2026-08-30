/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.entidad;

/**
 *
 * @author eduar
 */
public class Usuario {
    //Atributos
    private int idUsuario;
    private String nit;
    private String dpi;
    private String telefono;
    private String direccion;
    private String correo;
    private String contrasena;
    private boolean activo;
    
    //Constructor
    public Usuario() {
    }
    
    //Inicializacion
    public Usuario(String nit, String dpi, String telefono, String direccion, String correo, String contrasena) {
        this.nit = nit;
        this.dpi = dpi;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
        this.contrasena = contrasena;
        this.activo = true;
    }
    
    public void editarPerfil(String telefono, String direccion) {
        this.telefono = telefono;
        this.direccion = direccion;
    }

    //Getters y setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getDpi() { return dpi; }
    public void setDpi(String dpi) { this.dpi = dpi; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
