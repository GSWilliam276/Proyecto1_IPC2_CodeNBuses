/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.Bus;
import com.usac.buses.proyectocodenbuses.entidad.EstadoOperativo;
import com.usac.buses.proyectocodenbuses.entidad.Sucursal;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author eduar
 */
public class BusPersistencia implements Persistencia<Bus> {
    private ConexionBase conexionBase = new ConexionBase();
    private SucursalPersistencia sucursalPersistencia = new SucursalPersistencia();

    @Override
    public boolean insertar(Bus bus) {
        String sql = "INSERT INTO bus (id_sucursal, placa, marca, modelo, anio, capacidad, "
                   + "estado_operativo, kilometraje, foto, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, bus.getSucursal().getIdSucursal());
            ps.setString(2, bus.getPlaca());
            ps.setString(3, bus.getMarca());
            ps.setString(4, bus.getModelo());
            ps.setInt(5, bus.getAnio());
            ps.setInt(6, bus.getCapacidad());
            ps.setString(7, bus.getEstadoOperativo().name());
            ps.setDouble(8, bus.getKilometraje());
            ps.setString(9, bus.getFoto());
            ps.setBoolean(10, bus.isActivo());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar bus: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Bus bus) {
        String sql = "UPDATE bus SET id_sucursal = ?, placa = ?, marca = ?, modelo = ?, anio = ?, "
                   + "capacidad = ?, estado_operativo = ?, kilometraje = ?, foto = ?, activo = ? "
                   + "WHERE id_bus = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, bus.getSucursal().getIdSucursal());
            ps.setString(2, bus.getPlaca());
            ps.setString(3, bus.getMarca());
            ps.setString(4, bus.getModelo());
            ps.setInt(5, bus.getAnio());
            ps.setInt(6, bus.getCapacidad());
            ps.setString(7, bus.getEstadoOperativo().name());
            ps.setDouble(8, bus.getKilometraje());
            ps.setString(9, bus.getFoto());
            ps.setBoolean(10, bus.isActivo());
            ps.setInt(11, bus.getIdBus());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar bus: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        //Los buses no se eliminan, solo se desactivan 
        String sql = "UPDATE bus SET activo = false WHERE id_bus = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al desactivar bus: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Bus buscarPorId(int id) {
        String sql = "SELECT * FROM bus WHERE id_bus = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearBus(rs);
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Error al buscar bus: " + e.getMessage());
            return null;
        }
    }

    @Override
    public ArrayList<Bus> listarTodos() {
        ArrayList<Bus> buses = new ArrayList<>();
        String sql = "SELECT * FROM bus";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                buses.add(mapearBus(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar buses: " + e.getMessage());
        }
        return buses;
    }

    public ArrayList<Bus> listarPorSucursal(int idSucursal) {
        ArrayList<Bus> buses = new ArrayList<>();
        String sql = "SELECT * FROM bus WHERE id_sucursal = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                buses.add(mapearBus(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar buses por sucursal: " + e.getMessage());
        }
        return buses;
    }

    public ArrayList<Bus> listarPorEstado(EstadoOperativo estado) {
        ArrayList<Bus> buses = new ArrayList<>();
        String sql = "SELECT * FROM bus WHERE estado_operativo = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estado.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                buses.add(mapearBus(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar buses por estado: " + e.getMessage());
        }
        return buses;
    }

    private Bus mapearBus(ResultSet rs) throws SQLException {
        Bus bus = new Bus();
        bus.setIdBus(rs.getInt("id_bus"));
        Sucursal sucursal = sucursalPersistencia.buscarPorId(rs.getInt("id_sucursal"));
        bus.setSucursal(sucursal);
        bus.setPlaca(rs.getString("placa"));
        bus.setMarca(rs.getString("marca"));
        bus.setModelo(rs.getString("modelo"));
        bus.setAnio(rs.getInt("anio"));
        bus.setCapacidad(rs.getInt("capacidad"));
        bus.setEstadoOperativo(EstadoOperativo.valueOf(rs.getString("estado_operativo")));
        bus.setKilometraje(rs.getDouble("kilometraje"));
        bus.setFoto(rs.getString("foto"));
        bus.setActivo(rs.getBoolean("activo"));
        return bus;
    }
}
