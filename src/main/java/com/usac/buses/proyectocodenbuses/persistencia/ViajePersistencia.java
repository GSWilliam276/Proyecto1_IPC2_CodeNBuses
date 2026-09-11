/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.Viaje;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
/**
 *
 * @author eduar
 */
public class ViajePersistencia {
    private ConexionBase conexionBase = new ConexionBase();
    
    //Actua como despachador: revisa el tipo de viaje en la tabla base,
    //y delega el mapeo completo a la clase específica correspondiente
    public Optional<Viaje> buscarPorId(int id) {
        String sql = "SELECT tipo FROM viaje WHERE id_viaje = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String tipo = rs.getString("tipo");
                if (tipo.equals("REGULAR")) {
                    ViajeRegularPersistencia viajeRegularPersistencia = new ViajeRegularPersistencia();
                    return viajeRegularPersistencia.buscarPorId(id).map(v -> (Viaje) v);
                } else {
                    ViajePrivadoPersistencia viajePrivadoPersistencia = new ViajePrivadoPersistencia();
                    return viajePrivadoPersistencia.buscarPorId(id).map(v -> (Viaje) v);
                }
            }
            return Optional.empty();

        } catch (SQLException e) {
            System.err.println("Error al buscar viaje: " + e.getMessage());
            return Optional.empty();
        }
    }

    //Lista todos los viajes (regulares y privados mezclados), usando el mismo
    //patron despachador para cada fila encontrada
    public ArrayList<Viaje> listarTodos() {
        ArrayList<Viaje> viajes = new ArrayList<>();
        String sql = "SELECT id_viaje FROM viaje";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                buscarPorId(rs.getInt("id_viaje")).ifPresent(viajes::add);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar viajes: " + e.getMessage());
        }
        return viajes;
    }

    public ArrayList<Object[]> listarRutasMasDemandadas(Date desde, Date hasta) {
        ArrayList<Object[]> resultado = new ArrayList<>();
        String sql = "SELECT so.nombre AS origen, sd.nombre AS destino, "
                + "COUNT(b.id_boleto) AS total_boletos "
                + "FROM viaje_regular vr "
                + "JOIN viaje v ON vr.id_viaje = v.id_viaje "
                + "JOIN ruta r ON vr.id_ruta = r.id_ruta "
                + "JOIN sucursal so ON r.id_sucursal_origen = so.id_sucursal "
                + "JOIN sucursal sd ON r.id_sucursal_destino = sd.id_sucursal "
                + "JOIN boleto b ON vr.id_viaje = b.id_viaje_regular "
                + "WHERE v.fecha_hora_salida BETWEEN ? AND ? "
                + "GROUP BY r.id_ruta, so.nombre, sd.nombre "
                + "ORDER BY total_boletos DESC";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setTimestamp(1, new Timestamp(desde.getTime()));
            ps.setTimestamp(2, new Timestamp(hasta.getTime()));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                resultado.add(new Object[]{
                    rs.getString("origen"),
                    rs.getString("destino"),
                    rs.getInt("total_boletos")
                });
            }

        } catch (SQLException e) {
            System.err.println("Error al listar rutas mas demandadas: " + e.getMessage());
        }
        return resultado;
    }
}
