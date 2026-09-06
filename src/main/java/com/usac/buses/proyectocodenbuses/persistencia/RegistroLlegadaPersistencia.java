/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.RegistroLlegada;
import com.usac.buses.proyectocodenbuses.entidad.Viaje;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
/**
 *
 * @author eduar
 */
public class RegistroLlegadaPersistencia implements Persistencia<RegistroLlegada> {
    private ConexionBase conexionBase = new ConexionBase();
    private ViajePersistencia viajePersistencia = new ViajePersistencia();

    @Override
    public boolean insertar(RegistroLlegada registro) {
        String sql = "INSERT INTO registro_llegada (id_viaje, hora_llegada_real, kilometraje_llegada, "
                   + "gasto_combustible, depreciacion_calculada) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, registro.getViaje().getIdViaje());
            ps.setTimestamp(2, new Timestamp(registro.getHoraLlegadaReal().getTime()));
            ps.setDouble(3, registro.getKilometrajeLlegada());
            ps.setDouble(4, registro.getGastoCombustible());
            ps.setDouble(5, registro.getDepreciacionCalculada());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar registro de llegada: " + e.getMessage());
            return false;
        }
    }

    //Registra la llegada completa: inserta el registro (con su depreciacion ya calculada
    //en el constructor de la entidad) y actualiza el kilometraje acumulado del bus
    //Ambas operaciones deben ocurrir juntas
    public boolean registrarLlegada(RegistroLlegada registro, int idBus) {
        String sqlRegistro = "INSERT INTO registro_llegada (id_viaje, hora_llegada_real, kilometraje_llegada, "
                            + "gasto_combustible, depreciacion_calculada) VALUES (?, ?, ?, ?, ?)";
        String sqlActualizarKm = "UPDATE bus SET kilometraje = kilometraje + ? WHERE id_bus = ?";

        Connection conexion = null;
        try {
            conexion = conexionBase.obtenerConexion();
            conexion.setAutoCommit(false); //Inicia Transaccion: registro + actualización de km del bus

            PreparedStatement psRegistro = conexion.prepareStatement(sqlRegistro);
            psRegistro.setInt(1, registro.getViaje().getIdViaje());
            psRegistro.setTimestamp(2, new Timestamp(registro.getHoraLlegadaReal().getTime()));
            psRegistro.setDouble(3, registro.getKilometrajeLlegada());
            psRegistro.setDouble(4, registro.getGastoCombustible());
            psRegistro.setDouble(5, registro.getDepreciacionCalculada());
            psRegistro.executeUpdate();

            PreparedStatement psBus = conexion.prepareStatement(sqlActualizarKm);
            psBus.setDouble(1, registro.getKilometrajeLlegada());
            psBus.setInt(2, idBus);
            psBus.executeUpdate();

            conexion.commit(); //Confirmar Transaccion
            return true;

        } catch (SQLException e) {
            if (conexion != null) {
                try {
                    conexion.rollback(); //Revierte Transaccion
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error al registrar llegada: " + e.getMessage());
            return false;
        } finally {
            conexionBase.cerrarConexion(conexion);
        }
    }

    @Override
    public boolean actualizar(RegistroLlegada registro) {
        return false; //Inmutable
    }

    @Override
    public boolean eliminar(int id) {
        return false; //Inmutable
    }

    @Override
    public Optional<RegistroLlegada> buscarPorId(int id) {
        String sql = "SELECT * FROM registro_llegada WHERE id_registro_llegada = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearRegistroLlegada(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error al buscar registro de llegada: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<RegistroLlegada> listarTodos() {
        ArrayList<RegistroLlegada> registros = new ArrayList<>();
        String sql = "SELECT * FROM registro_llegada";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(mapearRegistroLlegada(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar registros de llegada: " + e.getMessage());
        }
        return registros;
    }

    public Optional<RegistroLlegada> buscarPorViaje(int idViaje) {
        String sql = "SELECT * FROM registro_llegada WHERE id_viaje = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idViaje);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearRegistroLlegada(rs));
            }
            return Optional.empty(); //El viaje sigue en transito, caso normal, no es un error

        } catch (SQLException e) {
            System.err.println("Error al buscar registro de llegada por viaje: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    private RegistroLlegada mapearRegistroLlegada(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_registro_llegada");
        Viaje viaje = viajePersistencia.buscarPorId(rs.getInt("id_viaje")).orElse(null);
        Date horaLlegada = new Date(rs.getTimestamp("hora_llegada_real").getTime());
        double km = rs.getDouble("kilometraje_llegada");
        double combustible = rs.getDouble("gasto_combustible");
        double depreciacion = rs.getDouble("depreciacion_calculada");

        //Uso de el segundo constructor: reconstruye el objeto tal como estaba guardado,
        //sin recalcular la depreciacion
        return new RegistroLlegada(id, viaje, horaLlegada, km, combustible, depreciacion);
    }
    
    public double obtenerCombustiblePorSucursalYFecha(int idSucursal, Date desde, Date hasta) {
        String sql = "SELECT COALESCE(SUM(rl.gasto_combustible), 0) AS total FROM registro_llegada rl "
                + "JOIN viaje v ON rl.id_viaje = v.id_viaje "
                + "JOIN bus bu ON v.id_bus = bu.id_bus "
                + "WHERE bu.id_sucursal = ? AND v.fecha_hora_salida BETWEEN ? AND ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSucursal);
            ps.setTimestamp(2, new Timestamp(desde.getTime()));
            ps.setTimestamp(3, new Timestamp(hasta.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
            return 0;

        } catch (SQLException e) {
            System.err.println("Error al obtener combustible por sucursal: " + e.getMessage());
            return 0;
        }
    }   

    public double obtenerDepreciacionPorSucursalYFecha(int idSucursal, Date desde, Date hasta) {
        String sql = "SELECT COALESCE(SUM(rl.depreciacion_calculada), 0) AS total FROM registro_llegada rl "
                + "JOIN viaje v ON rl.id_viaje = v.id_viaje "
                + "JOIN bus bu ON v.id_bus = bu.id_bus "
                + "WHERE bu.id_sucursal = ? AND v.fecha_hora_salida BETWEEN ? AND ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSucursal);
            ps.setTimestamp(2, new Timestamp(desde.getTime()));
            ps.setTimestamp(3, new Timestamp(hasta.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
            return 0;

        } catch (SQLException e) {
            System.err.println("Error al obtener depreciación por sucursal: " + e.getMessage());
            return 0;
        }
    }
}
