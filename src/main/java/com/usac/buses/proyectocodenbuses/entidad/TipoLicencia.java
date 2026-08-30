/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.entidad;

/**
 *
 * @author eduar
 */
public enum TipoLicencia {
    A, //carga pesada, colectivo y extraurbano de pasajeros (LA MAS OPTIMA)
    B, //transporte colectivo o carga liviana/mediana (puede aun asi aplicar)
    C, //vehiculos particulares, sin remuneración (No aplica a buses, tiene salario base)
    E, //maquinaria agrícola/industrial (no aplica a buses)
    M  //motocicletas (no aplica a buses)
}
