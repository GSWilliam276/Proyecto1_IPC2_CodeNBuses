/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.RegistroSalida;
import com.usac.buses.proyectocodenbuses.entidad.Viaje;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
/**
 *
 * @author eduar
 */
public class RegistroSalidaPersistencia implements Persistencia<RegistroSalida> {
    private ConexionBase conexionBase = new ConexionBase();
    private ViajePersistencia viajePersistencia = new ViajePersistencia();

    @Override
    public boolean insertar(RegistroSalida registro) {
        String sql = "INSERT INTO registro_salida (id_viaje, hora_salida_real, kilometraje_salida) "
                   + "VALUES (?, ?, ?)";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, registro.getViaje().getIdViaje());
            ps.setTimestamp(2, new Timestamp(registro.getHoraSalidaReal().getTime()));
            ps.setDouble(3, registro.getKilometrajeSalida());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar registro de salida: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(RegistroSalida registro) {
        //No se permite modificar: los registros de salida son inmutables
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        //No se permite eliminar: los registros de salida son inmutables
        return false;
    }

    @Override
    public Optional<RegistroSalida> buscarPorId(int id) {
        String sql = "SELECT * FROM registro_salida WHERE id_registro_salida = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearRegistroSalida(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error al buscar registro de salida: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<RegistroSalida> listarTodos() {
        ArrayList<RegistroSalida> registros = new ArrayList<>();
        String sql = "SELECT * FROM registro_salida";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(mapearRegistroSalida(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar registros de salida: " + e.getMessage());
        }
        return registros;
    }

    public Optional<RegistroSalida> buscarPorViaje(int idViaje) {
        String sql = "SELECT * FROM registro_salida WHERE id_viaje = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idViaje);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearRegistroSalida(rs));
            }
            return Optional.empty(); //El viaje aun no ha salido, es un caso normal, no un error

        } catch (SQLException e) {
            System.err.println("Error al buscar registro de salida por viaje: " + e.getMessage());
            return Optional.empty();
        }
    }

    private RegistroSalida mapearRegistroSalida(ResultSet rs) throws SQLException {
        Viaje viaje = viajePersistencia.buscarPorId(rs.getInt("id_viaje")).orElse(null);
        Date horaSalida = new Date(rs.getTimestamp("hora_salida_real").getTime());
        double km = rs.getDouble("kilometraje_salida");

        //Como la clase es inmutable, se arma completa con el constructor, no con setters
        return new RegistroSalida(viaje, horaSalida, km);
    }
}
