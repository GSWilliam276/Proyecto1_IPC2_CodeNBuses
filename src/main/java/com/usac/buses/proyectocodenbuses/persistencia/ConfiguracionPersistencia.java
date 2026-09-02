/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import java.sql.*;
/**
 *
 * @author eduar
 */
public class ConfiguracionPersistencia {
    private ConexionBase conexionBase = new ConexionBase();

    public double obtenerMontoDepreciacionActual() {
        String sql = "SELECT monto_depreciacion_km FROM configuracion ORDER BY fecha_actualizacion DESC LIMIT 1";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("monto_depreciacion_km");
            }
            return 0;

        } catch (SQLException e) {
            System.err.println("Error al obtener monto de depreciación: " + e.getMessage());
            return 0;
        }
    }

    public boolean actualizarMontoDepreciacion(double monto) {
        String sql = "INSERT INTO configuracion (monto_depreciacion_km, fecha_actualizacion) VALUES (?, NOW())";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setDouble(1, monto);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar monto de depreciación: " + e.getMessage());
            return false;
        }
    }
}
