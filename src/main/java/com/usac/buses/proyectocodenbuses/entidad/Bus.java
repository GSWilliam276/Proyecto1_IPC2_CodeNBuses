/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.entidad;

/**
 *
 * @author eduar
 */
public class Bus {
    //Atributos
    private int idBus;
    private Sucursal sucursal;
    private String placa;
    private String marca;
    private String modelo;
    private int anio;
    private int capacidad;
    private EstadoOperativo estadoOperativo;
    private double kilometraje;
    private String foto;
    private boolean activo;

    public Bus() {
    }

    public Bus(Sucursal sucursal, String placa, String marca, String modelo, int anio, int capacidad, String foto) {
        this.sucursal = sucursal;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.capacidad = capacidad;
        this.foto = foto;
        this.estadoOperativo = EstadoOperativo.DISPONIBLE;
        this.kilometraje = 0;
        this.activo = true;
    }

    public void activar() {
        this.activo = true;
    }

    public void desactivar() {
        this.activo = false;
    }

    public void actualizarKilometraje(double km) {
        this.kilometraje += km;
    }

    //Getters y setters
    public int getIdBus() { return idBus; }
    public void setIdBus(int idBus) { this.idBus = idBus; }

    public Sucursal getSucursal() { return sucursal; }
    public void setSucursal(Sucursal sucursal) { this.sucursal = sucursal; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public EstadoOperativo getEstadoOperativo() { return estadoOperativo; }
    public void setEstadoOperativo(EstadoOperativo estadoOperativo) { this.estadoOperativo = estadoOperativo; }

    public double getKilometraje() { return kilometraje; }
    public void setKilometraje(double kilometraje) { this.kilometraje = kilometraje; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
