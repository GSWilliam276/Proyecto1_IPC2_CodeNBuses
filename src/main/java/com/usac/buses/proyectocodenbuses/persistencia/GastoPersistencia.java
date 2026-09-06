/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.Bus;
import com.usac.buses.proyectocodenbuses.entidad.Gasto;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
/**
 *
 * @author eduar
 */
public class GastoPersistencia  implements Persistencia<Gasto>{
    private ConexionBase conexionBase = new ConexionBase();
    private BusPersistencia busPersistencia = new BusPersistencia();

    @Override
    public boolean insertar(Gasto gasto) {
        String sql = "INSERT INTO gasto (id_bus, monto_mano_obra, monto_repuestos, fecha) VALUES (?, ?, ?, ?)";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, gasto.getBus().getIdBus());
            ps.setDouble(2, gasto.getMontoManoObra());
            ps.setDouble(3, gasto.getMontoRepuestos());
            ps.setDate(4, new java.sql.Date(gasto.getFecha().getTime()));
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar gasto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Gasto gasto) {
        String sql = "UPDATE gasto SET monto_mano_obra = ?, monto_repuestos = ?, fecha = ? WHERE id_gasto = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setDouble(1, gasto.getMontoManoObra());
            ps.setDouble(2, gasto.getMontoRepuestos());
            ps.setDate(3, new java.sql.Date(gasto.getFecha().getTime()));
            ps.setInt(4, gasto.getIdGasto());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar gasto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM gasto WHERE id_gasto = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar gasto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Gasto> buscarPorId(int id) {
        String sql = "SELECT * FROM gasto WHERE id_gasto = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearGasto(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            System.err.println("Error al buscar gasto: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<Gasto> listarTodos() {
        ArrayList<Gasto> gastos = new ArrayList<>();
        String sql = "SELECT * FROM gasto";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                gastos.add(mapearGasto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar gastos: " + e.getMessage());
        }
        return gastos;
    }

    public ArrayList<Gasto> listarPorBus(int idBus) {
        ArrayList<Gasto> gastos = new ArrayList<>();
        String sql = "SELECT * FROM gasto WHERE id_bus = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idBus);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                gastos.add(mapearGasto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar gastos por bus: " + e.getMessage());
        }
        return gastos;
    }

    public ArrayList<Gasto> listarPorSucursalYFecha(int idSucursal, Date desde, Date hasta) {
        ArrayList<Gasto> gastos = new ArrayList<>();
        String sql = "SELECT g.* FROM gasto g "
                   + "JOIN bus b ON g.id_bus = b.id_bus "
                   + "WHERE b.id_sucursal = ? AND g.fecha BETWEEN ? AND ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSucursal);
            ps.setDate(2, new java.sql.Date(desde.getTime()));
            ps.setDate(3, new java.sql.Date(hasta.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                gastos.add(mapearGasto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar gastos por sucursal y fecha: " + e.getMessage());
        }
        return gastos;
    }

    private Gasto mapearGasto(ResultSet rs) throws SQLException {
        Gasto gasto = new Gasto();
        gasto.setIdGasto(rs.getInt("id_gasto"));
        Bus bus = busPersistencia.buscarPorId(rs.getInt("id_bus")).orElse(null);
        gasto.setBus(bus);
        gasto.setMontoManoObra(rs.getDouble("monto_mano_obra"));
        gasto.setMontoRepuestos(rs.getDouble("monto_repuestos"));
        gasto.setFecha(new Date(rs.getTimestamp("fecha").getTime()));
        return gasto;
    }
    
    public double obtenerTotalPorSucursalYFecha(int idSucursal, Date desde, Date hasta) {
        String sql = "SELECT COALESCE(SUM(g.monto_mano_obra + g.monto_repuestos), 0) AS total FROM gasto g "
                + "JOIN bus b ON g.id_bus = b.id_bus "
                + "WHERE b.id_sucursal = ? AND g.fecha BETWEEN ? AND ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSucursal);
            ps.setDate(2, new java.sql.Date(desde.getTime()));
            ps.setDate(3, new java.sql.Date(hasta.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
            return 0;

        } catch (SQLException e) {
            System.err.println("Error al obtener gastos de taller por sucursal: " + e.getMessage());
            return 0;
        }
    }
}
