/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.Cartera;
import com.usac.buses.proyectocodenbuses.entidad.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
/**
 *
 * @author eduar
 */
public class CarteraPersistencia implements Persistencia<Cartera> {
    private ConexionBase conexionBase = new ConexionBase();

    @Override
    public boolean insertar(Cartera cartera) {
        String sql = "INSERT INTO cartera (id_usuario, saldo) VALUES (?, ?)";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, cartera.getUsuario().getIdUsuario());
            ps.setDouble(2, cartera.getSaldo());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar cartera: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Cartera cartera) {
        String sql = "UPDATE cartera SET saldo = ? WHERE id_cartera = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setDouble(1, cartera.getSaldo());
            ps.setInt(2, cartera.getIdCartera());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar cartera: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        //Las carteras no se eliminan, estan ligadas al ciclo de vida del usuario
        return false;
    }

    @Override
    public Optional<Cartera> buscarPorId(int id) {
        String sql = "SELECT * FROM cartera WHERE id_cartera = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearCartera(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error al buscar cartera: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<Cartera> listarTodos() {
        ArrayList<Cartera> carteras = new ArrayList<>();
        String sql = "SELECT * FROM cartera";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                carteras.add(mapearCartera(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar carteras: " + e.getMessage());
        }
        return carteras;
    }

    public Optional<Cartera> buscarPorUsuario(int idUsuario) {
        String sql = "SELECT * FROM cartera WHERE id_usuario = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearCartera(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            System.err.println("Error al buscar cartera por usuario: " + e.getMessage());
            return Optional.empty();
        }
    }

    public boolean recargar(int idCartera, double monto) {
        String sql = "UPDATE cartera SET saldo = saldo + ? WHERE id_cartera = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setDouble(1, monto);
            ps.setInt(2, idCartera);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al recargar cartera: " + e.getMessage());
            return false;
        }
    }

    private Cartera mapearCartera(ResultSet rs) throws SQLException {
        Cartera cartera = new Cartera();
        cartera.setIdCartera(rs.getInt("id_cartera"));
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        cartera.setUsuario(usuario);
        cartera.setSaldo(rs.getDouble("saldo"));
        return cartera;
    }
}
