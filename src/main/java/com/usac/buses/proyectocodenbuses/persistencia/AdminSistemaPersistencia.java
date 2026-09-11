/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.AdminSistema;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
/**
 *
 * @author eduar
 */
public class AdminSistemaPersistencia implements Persistencia<AdminSistema> {
    private ConexionBase conexionBase = new ConexionBase();

    @Override
    public boolean insertar(AdminSistema admin) {
        String sqlUsuario = "INSERT INTO usuario (nit, dpi, telefono, direccion, correo, contrasena, tipo, activo) "
                           + "VALUES (?, ?, ?, ?, ?, ?, 'ADMIN_SISTEMA', ?)";
        String sqlAdmin = "INSERT INTO admin_sistema (id_usuario) VALUES (?)";

        Connection conexion = null;
        try {
            conexion = conexionBase.obtenerConexion();
            conexion.setAutoCommit(false); //Inicia la transaccion: 2 tablas involucradas (usuario + admin_sistema)

            //Insertar en la tabla usuario
            PreparedStatement psUsuario = conexion.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
            psUsuario.setString(1, admin.getNit());
            psUsuario.setString(2, admin.getDpi());
            psUsuario.setString(3, admin.getTelefono());
            psUsuario.setString(4, admin.getDireccion());
            psUsuario.setString(5, admin.getCorreo());
            psUsuario.setString(6, admin.getContrasena());
            psUsuario.setBoolean(7, admin.isActivo());
            psUsuario.executeUpdate();

            //Obtener el id generado para reutilizarlo en la segunda tabla
            ResultSet generatedKeys = psUsuario.getGeneratedKeys();
            int idUsuarioGenerado = 0;
            if (generatedKeys.next()) {
                idUsuarioGenerado = generatedKeys.getInt(1);
            }

            //Insertar en la tabla admin_sistema, usando el mismo id
            PreparedStatement psAdmin = conexion.prepareStatement(sqlAdmin);
            psAdmin.setInt(1, idUsuarioGenerado);
            psAdmin.executeUpdate();

            conexion.commit(); //Cofirma transaccion: ambas inserciones quedan guardadas juntas
            return true;

        } catch (SQLException e) {
            if (conexion != null) {
                try {
                    conexion.rollback(); //Revierte Transaccion: si algo fallo, ninguna de las 2 queda guardada
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error al insertar admin sistema: " + e.getMessage());
            return false;
        } finally {
            conexionBase.cerrarConexion(conexion);
        }
    }

    @Override
    public boolean actualizar(AdminSistema admin) {
        //Solo se actualizan datos heredados de Usuario, admin_sistema no tiene campos propios que editar
        String sql = "UPDATE usuario SET nit = ?, dpi = ?, telefono = ?, direccion = ?, correo = ? "
                   + "WHERE id_usuario = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, admin.getNit());
            ps.setString(2, admin.getDpi());
            ps.setString(3, admin.getTelefono());
            ps.setString(4, admin.getDireccion());
            ps.setString(5, admin.getCorreo());
            ps.setInt(6, admin.getIdUsuario());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar admin sistema: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "UPDATE usuario SET activo = false WHERE id_usuario = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al desactivar admin sistema: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<AdminSistema> buscarPorId(int id) {
        String sql = "SELECT u.* FROM usuario u "
                + "JOIN admin_sistema a ON u.id_usuario = a.id_usuario WHERE u.id_usuario = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearAdminSistema(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error al buscar admin sistema: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<AdminSistema> listarTodos() {
        ArrayList<AdminSistema> admins = new ArrayList<>();
        String sql = "SELECT u.* FROM usuario u "
                   + "JOIN admin_sistema a ON u.id_usuario = a.id_usuario";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                admins.add(mapearAdminSistema(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar admins sistema: " + e.getMessage());
        }
        return admins;
    }

    private AdminSistema mapearAdminSistema(ResultSet rs) throws SQLException {
        AdminSistema admin = new AdminSistema();
        admin.setIdUsuario(rs.getInt("id_usuario"));
        admin.setNit(rs.getString("nit"));
        admin.setDpi(rs.getString("dpi"));
        admin.setTelefono(rs.getString("telefono"));
        admin.setDireccion(rs.getString("direccion"));
        admin.setCorreo(rs.getString("correo"));
        admin.setContrasena(rs.getString("contrasena"));
        admin.setActivo(rs.getBoolean("activo"));
        return admin;
    }
    
    public Optional<AdminSistema> buscarPorCorreo(String correo) {
        String sql = "SELECT u.* FROM usuario u "
                + "JOIN admin_sistema a ON u.id_usuario = a.id_usuario WHERE u.correo = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearAdminSistema(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            System.err.println("Error al buscar admin sistema por correo: " + e.getMessage());
            return Optional.empty();
        }
    }
}
