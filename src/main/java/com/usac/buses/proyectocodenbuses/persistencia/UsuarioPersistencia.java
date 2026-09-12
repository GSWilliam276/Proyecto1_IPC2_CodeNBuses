/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.Usuario;
import java.sql.*;
import java.util.Optional;
/**
 *
 * @author eduar
 */
public class UsuarioPersistencia {
    private ConexionBase conexionBase = new ConexionBase();

    
    //Actualiza solo los datos heredados de Usuario (nit, dpi, telefono, direccion),
    //sin importar si es Chofer, AdminSucursal, AdminSistema o ClienteRegular.
    //Esto es lo que usa "Editar perfil", separado de la gestión operativa
    //que hace cada Controlador especifico (ej. ControladorChofer solo toca
    //licencia/salario, nunca los datos de Usuario)
    public boolean editarPerfil(int idUsuario, String telefono, String direccion) {
        String sql = "UPDATE usuario SET telefono = ?, direccion = ? WHERE id_usuario = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, telefono);
            ps.setString(2, direccion);
            ps.setInt(3, idUsuario);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al editar perfil: " + e.getMessage());
            return false;
        }
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM usuario WHERE correo = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearUsuario(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por correo: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Usuario> autenticar(String correo, String contrasena) {
        String sql = "SELECT tipo FROM usuario WHERE correo = ? AND contrasena = ? AND activo = true";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String tipo = rs.getString("tipo");
                //Se despacha hacia la clase especifica correcta, aprovechando
                //que cada Persistencia ya sabe construir su propio objeto completo
                switch (tipo) {
                    case "ADMIN_SISTEMA":
                        return new AdminSistemaPersistencia().buscarPorCorreo(correo).map(u -> (Usuario) u);
                    case "ADMIN_SUCURSAL":
                        return new AdminSucursalPersistencia().buscarPorCorreo(correo).map(u -> (Usuario) u);
                    case "CHOFER":
                        return new ChoferPersistencia().buscarPorCorreo(correo).map(u -> (Usuario) u);
                    case "CLIENTE":
                        return new ClienteRegularPersistencia().buscarPorCorreo(correo).map(u -> (Usuario) u);
                    default:
                        return Optional.empty();
                }
            }
            return Optional.empty();

        } catch (SQLException e) {
            System.err.println("Error al autenticar usuario: " + e.getMessage());
            return Optional.empty();
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNit(rs.getString("nit"));
        usuario.setDpi(rs.getString("dpi"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setDireccion(rs.getString("direccion"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setActivo(rs.getBoolean("activo"));
        return usuario;
    }
    
    public boolean existeUsuarioConCorreo(String correo) {
        String sql = "SELECT COUNT(*) AS total FROM usuario WHERE correo = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al verificar correo: " + e.getMessage());
            return false;
        }
    }
    
    public boolean existeUsuarioConDpi(String dpi) {
        String sql = "SELECT COUNT(*) AS total FROM usuario WHERE dpi = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, dpi);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al verificar DPI: " + e.getMessage());
            return false;
        }
    }
}
