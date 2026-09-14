/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author eduar
 */
public class ConexionBase {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_buses?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "gs_william237";
    private static final String CONTRASENA = "59487059@"; 
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
     public Connection obtenerConexion() throws SQLException {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de MySQL: " + e.getMessage());
        }
    }
 
    public void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
