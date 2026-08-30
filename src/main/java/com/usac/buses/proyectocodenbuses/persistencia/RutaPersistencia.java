/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.Ruta;
import com.usac.buses.proyectocodenbuses.entidad.Sucursal;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author eduar
 */
public class RutaPersistencia implements Persistencia<Ruta> {
    private ConexionBase conexionBase = new ConexionBase();
    private SucursalPersistencia sucursalPersistencia = new SucursalPersistencia();

    @Override
    public boolean insertar(Ruta ruta) {
        String sql = "INSERT INTO ruta (id_sucursal_origen, id_sucursal_destino, distancia_km, precio_boleto) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, ruta.getSucursalOrigen().getIdSucursal());
            ps.setInt(2, ruta.getSucursalDestino().getIdSucursal());
            ps.setDouble(3, ruta.getDistanciaKm());
            ps.setDouble(4, ruta.getPrecioBoleto());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar ruta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Ruta ruta) {
        String sql = "UPDATE ruta SET id_sucursal_origen = ?, id_sucursal_destino = ?, "
                   + "distancia_km = ?, precio_boleto = ? WHERE id_ruta = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, ruta.getSucursalOrigen().getIdSucursal());
            ps.setInt(2, ruta.getSucursalDestino().getIdSucursal());
            ps.setDouble(3, ruta.getDistanciaKm());
            ps.setDouble(4, ruta.getPrecioBoleto());
            ps.setInt(5, ruta.getIdRuta());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar ruta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        //Solo se permite eliminar si no tiene viajes asociados (se valida antes, en el Controlador)
        String sql = "DELETE FROM ruta WHERE id_ruta = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar ruta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Ruta buscarPorId(int id) {
        String sql = "SELECT * FROM ruta WHERE id_ruta = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearRuta(rs);
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Error al buscar ruta: " + e.getMessage());
            return null;
        }
    }

    @Override
    public ArrayList<Ruta> listarTodos() {
        ArrayList<Ruta> rutas = new ArrayList<>();
        String sql = "SELECT * FROM ruta";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rutas.add(mapearRuta(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar rutas: " + e.getMessage());
        }
        return rutas;
    }

    public ArrayList<Ruta> listarPorSucursal(int idSucursal) {
        ArrayList<Ruta> rutas = new ArrayList<>();
        String sql = "SELECT * FROM ruta WHERE id_sucursal_origen = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rutas.add(mapearRuta(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar rutas por sucursal: " + e.getMessage());
        }
        return rutas;
    }

    public boolean tieneViajesAsociados(int idRuta) {
        String sql = "SELECT COUNT(*) AS total FROM viaje_regular WHERE id_ruta = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idRuta);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al verificar viajes asociados: " + e.getMessage());
            return false;
        }
    }

    private Ruta mapearRuta(ResultSet rs) throws SQLException {
        Ruta ruta = new Ruta();
        ruta.setIdRuta(rs.getInt("id_ruta"));
        Sucursal origen = sucursalPersistencia.buscarPorId(rs.getInt("id_sucursal_origen"));
        Sucursal destino = sucursalPersistencia.buscarPorId(rs.getInt("id_sucursal_destino"));
        ruta.setSucursalOrigen(origen);
        ruta.setSucursalDestino(destino);
        ruta.setDistanciaKm(rs.getDouble("distancia_km"));
        ruta.setPrecioBoleto(rs.getDouble("precio_boleto"));
        return ruta;
    }
}
