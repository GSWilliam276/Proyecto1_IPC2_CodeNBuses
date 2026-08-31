/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.AdminSucursal;
import com.usac.buses.proyectocodenbuses.entidad.Sucursal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
/**
 *
 * @author eduar
 */
public class AdminSucursalPersistencia implements Persistencia<AdminSucursal> {
    private ConexionBase conexionBase = new ConexionBase();
    private SucursalPersistencia sucursalPersistencia = new SucursalPersistencia();

    @Override
    public boolean insertar(AdminSucursal admin) {
        String sqlUsuario = "INSERT INTO usuario (nit, dpi, telefono, direccion, correo, contrasena, tipo, activo) "
                           + "VALUES (?, ?, ?, ?, ?, ?, 'ADMIN_SUCURSAL', ?)";
        String sqlAdmin = "INSERT INTO admin_sucursal (id_usuario, id_sucursal) VALUES (?, ?)";

        Connection conexion = null;
        try {
            conexion = conexionBase.obtenerConexion();
            conexion.setAutoCommit(false); //Inicia Transaccion: 2 tablas involucradas (usuario + admin_sucursal)

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

            //Insertar en la tabla admin_sucursal, usando el mismo id + la sucursal asignada
            PreparedStatement psAdmin = conexion.prepareStatement(sqlAdmin);
            psAdmin.setInt(1, idUsuarioGenerado);
            psAdmin.setInt(2, admin.getSucursal().getIdSucursal());
            psAdmin.executeUpdate();

            conexion.commit(); //Confirma Transaccion: ambas inserciones quedan guardadas juntas
            return true;

        } catch (SQLException e) {
            if (conexion != null) {
                try {
                    conexion.rollback(); //Revierte Transaccion: si algo fallo, ninguna de las 2 queda guardada
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error al insertar admin sucursal: " + e.getMessage());
            return false;
        } finally {
            conexionBase.cerrarConexion(conexion);
        }
    }

    @Override
    public boolean actualizar(AdminSucursal admin) {
        Connection conexion = null;
        String sqlUsuario = "UPDATE usuario SET nit = ?, dpi = ?, telefono = ?, direccion = ?, correo = ? "
                           + "WHERE id_usuario = ?";
        String sqlAdmin = "UPDATE admin_sucursal SET id_sucursal = ? WHERE id_usuario = ?";

        try {
            conexion = conexionBase.obtenerConexion();
            conexion.setAutoCommit(false); //Inicia Transaccion: actualiza 2 tablas juntas

            PreparedStatement psUsuario = conexion.prepareStatement(sqlUsuario);
            psUsuario.setString(1, admin.getNit());
            psUsuario.setString(2, admin.getDpi());
            psUsuario.setString(3, admin.getTelefono());
            psUsuario.setString(4, admin.getDireccion());
            psUsuario.setString(5, admin.getCorreo());
            psUsuario.setInt(6, admin.getIdUsuario());
            psUsuario.executeUpdate();

            PreparedStatement psAdmin = conexion.prepareStatement(sqlAdmin);
            psAdmin.setInt(1, admin.getSucursal().getIdSucursal());
            psAdmin.setInt(2, admin.getIdUsuario());
            psAdmin.executeUpdate();

            conexion.commit(); //Confirma Transaccion
            return true;

        } catch (SQLException e) {
            if (conexion != null) {
                try {
                    conexion.rollback(); //Revierte Transaccion
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error al actualizar admin sucursal: " + e.getMessage());
            return false;
        } finally {
            conexionBase.cerrarConexion(conexion);
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
            System.err.println("Error al desactivar admin sucursal: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<AdminSucursal> buscarPorId(int id) {
        String sql = "SELECT u.*, a.id_sucursal FROM usuario u "
                + "JOIN admin_sucursal a ON u.id_usuario = a.id_usuario WHERE u.id_usuario = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearAdminSucursal(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            System.err.println("Error al buscar admin sucursal: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<AdminSucursal> listarTodos() {
        ArrayList<AdminSucursal> admins = new ArrayList<>();
        String sql = "SELECT u.*, a.id_sucursal FROM usuario u "
                   + "JOIN admin_sucursal a ON u.id_usuario = a.id_usuario";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                admins.add(mapearAdminSucursal(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar admins sucursal: " + e.getMessage());
        }
        return admins;
    }

    public ArrayList<AdminSucursal> listarPorSucursal(int idSucursal) {
        ArrayList<AdminSucursal> admins = new ArrayList<>();
        String sql = "SELECT u.*, a.id_sucursal FROM usuario u "
                   + "JOIN admin_sucursal a ON u.id_usuario = a.id_usuario WHERE a.id_sucursal = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                admins.add(mapearAdminSucursal(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar admins por sucursal: " + e.getMessage());
        }
        return admins;
    }

   private AdminSucursal mapearAdminSucursal(ResultSet rs) throws SQLException {
        AdminSucursal admin = new AdminSucursal();
        admin.setIdUsuario(rs.getInt("id_usuario"));
        admin.setNit(rs.getString("nit"));
        admin.setDpi(rs.getString("dpi"));
        admin.setTelefono(rs.getString("telefono"));
        admin.setDireccion(rs.getString("direccion"));
        admin.setCorreo(rs.getString("correo"));
        admin.setContrasena(rs.getString("contrasena"));
        admin.setActivo(rs.getBoolean("activo"));
        Sucursal sucursal = sucursalPersistencia.buscarPorId(rs.getInt("id_sucursal")).orElse(null);
        admin.setSucursal(sucursal);
        return admin;
    }
}
