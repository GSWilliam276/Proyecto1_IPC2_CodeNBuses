/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import java.util.ArrayList;
/**
 *
 * @author eduar
 */
public interface Persistencia<T> {
    boolean insertar(T objeto);
    boolean actualizar(T objeto);
    boolean eliminar(int id);
    T buscarPorId(int id);
    ArrayList<T> listarTodos();
}
