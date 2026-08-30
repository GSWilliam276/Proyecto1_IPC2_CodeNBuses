/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.excepcion;

/**
 *
 * @author eduar
 */
public class ExcepcionFormatoInvalido extends ExcepcionCodeNBuses {
    private String campo;
    
    public ExcepcionFormatoInvalido(String mensaje) {
        super(mensaje);
        this.campo = campo;
    }
    
    public String getCampo(){
        return campo;
    }
}
