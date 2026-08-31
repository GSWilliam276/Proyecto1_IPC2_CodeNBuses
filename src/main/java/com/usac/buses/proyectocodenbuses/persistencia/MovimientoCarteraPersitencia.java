/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.Cartera;
import com.usac.buses.proyectocodenbuses.entidad.MovimientoCartera;
import com.usac.buses.proyectocodenbuses.entidad.TipoMovimiento;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
/**
 *
 * @author eduar
 */
public class MovimientoCarteraPersitencia implements Persistencia<MovimientoCartera> {
    private ConexionBase conexionBase = new ConexionBase();

    @Override
    public boolean insertar(MovimientoCartera movimiento) {
        String sql = "INSERT INTO movimiento_cartera (id_cartera, tipo, monto, fecha) VALUES (?, ?, ?, ?)";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, movimiento.getCartera().getIdCartera());
            ps.setString(2, movimiento.getTipo().name());
            ps.setDouble(3, movimiento.getMonto());
            ps.setTimestamp(4, new Timestamp(movimiento.getFecha().getTime()));
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar movimiento de cartera: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(MovimientoCartera movimiento) {
        //Los movimientos de cartera no se editan, son un historial inmutable
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        //Los movimientos de cartera no se eliminan, son un historial inmutable
        return false;
    }

    @Override
    public Optional<MovimientoCartera> buscarPorId(int id) {
        String sql = "SELECT * FROM movimiento_cartera WHERE id_movimiento = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearMovimiento(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error al buscar movimiento de cartera: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<MovimientoCartera> listarTodos() {
        ArrayList<MovimientoCartera> movimientos = new ArrayList<>();
        String sql = "SELECT * FROM movimiento_cartera";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                movimientos.add(mapearMovimiento(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar movimientos de cartera: " + e.getMessage());
        }
        return movimientos;
    }

    public ArrayList<MovimientoCartera> listarPorCartera(int idCartera) {
        ArrayList<MovimientoCartera> movimientos = new ArrayList<>();
        String sql = "SELECT * FROM movimiento_cartera WHERE id_cartera = ? ORDER BY fecha DESC";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCartera);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                movimientos.add(mapearMovimiento(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar movimientos por cartera: " + e.getMessage());
        }
        return movimientos;
    }

    private MovimientoCartera mapearMovimiento(ResultSet rs) throws SQLException {
        MovimientoCartera movimiento = new MovimientoCartera();
        movimiento.setIdMovimiento(rs.getInt("id_movimiento"));
        Cartera cartera = new Cartera();
        cartera.setIdCartera(rs.getInt("id_cartera"));
        movimiento.setCartera(cartera);
        movimiento.setTipo(TipoMovimiento.valueOf(rs.getString("tipo")));
        movimiento.setMonto(rs.getDouble("monto"));
        movimiento.setFecha(new Date(rs.getTimestamp("fecha").getTime()));
        return movimiento;
    }
}
