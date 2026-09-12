/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.Sucursal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
/**
 *
 * @author eduar
 */
public class SucursalPersistencia implements Persistencia<Sucursal> {
    private ConexionBase conexionBase = new ConexionBase();

    @Override
    public boolean insertar(Sucursal sucursal) {
        String sql = "INSERT INTO sucursal (nombre, ubicacion, latitud, longitud) VALUES (?, ?, ?, ?)";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, sucursal.getNombre());
            ps.setString(2, sucursal.getUbicacion());
            ps.setDouble(3, sucursal.getLatitud());
            ps.setDouble(4, sucursal.getLongitud());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
        System.err.println("Error al insertar sucursal: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Sucursal sucursal) {
        String sql = "UPDATE sucursal SET nombre = ?, ubicacion = ?, latitud = ?, longitud = ? WHERE id_sucursal = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, sucursal.getNombre());
            ps.setString(2, sucursal.getUbicacion());
            ps.setDouble(3, sucursal.getLatitud());
            ps.setDouble(4, sucursal.getLongitud());
            ps.setInt(5, sucursal.getIdSucursal());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar sucursal: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM sucursal WHERE id_sucursal = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar sucursal: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Sucursal> buscarPorId(int id) {
        String sql = "SELECT * FROM sucursal WHERE id_sucursal = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearSucursal(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error al buscar sucursal: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<Sucursal> listarTodos() {
        ArrayList<Sucursal> sucursales = new ArrayList<>();
        String sql = "SELECT * FROM sucursal";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sucursales.add(mapearSucursal(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar sucursales: " + e.getMessage());
        }
        return sucursales;
    }

    private Sucursal mapearSucursal(ResultSet rs) throws SQLException {
        Sucursal sucursal = new Sucursal();
        sucursal.setIdSucursal(rs.getInt("id_sucursal"));
        sucursal.setNombre(rs.getString("nombre"));
        sucursal.setUbicacion(rs.getString("ubicacion"));
        sucursal.setLatitud(rs.getDouble("latitud"));
        sucursal.setLongitud(rs.getDouble("longitud"));
        return sucursal;
    }
    
    public boolean existeSucursalConNombre(String nombre) {
        String sql = "SELECT COUNT(*) AS total FROM sucursal WHERE nombre = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al verificar nombre de sucursal: " + e.getMessage());
            return false;
        }
    }
}
