/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.ClienteRegular;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
/**
 *
 * @author eduar
 */
public class ClienteRegularPersistencia implements Persistencia<ClienteRegular> {
    private ConexionBase conexionBase = new ConexionBase();

    @Override
    public boolean insertar(ClienteRegular cliente) {
        String sqlUsuario = "INSERT INTO usuario (nit, dpi, telefono, direccion, correo, contrasena, tipo, activo) "
                           + "VALUES (?, ?, ?, ?, ?, ?, 'CLIENTE', ?)";
        String sqlCliente = "INSERT INTO cliente_regular (id_usuario) VALUES (?)";

        Connection conexion = null;
        try {
            conexion = conexionBase.obtenerConexion();
            conexion.setAutoCommit(false); //Inicia Transaccion: 2 tablas involucradas (usuario + cliente_regular)

            PreparedStatement psUsuario = conexion.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
            psUsuario.setString(1, cliente.getNit());
            psUsuario.setString(2, cliente.getDpi());
            psUsuario.setString(3, cliente.getTelefono());
            psUsuario.setString(4, cliente.getDireccion());
            psUsuario.setString(5, cliente.getCorreo());
            psUsuario.setString(6, cliente.getContrasena());
            psUsuario.setBoolean(7, cliente.isActivo());
            psUsuario.executeUpdate();

            ResultSet generatedKeys = psUsuario.getGeneratedKeys();
            int idUsuarioGenerado = 0;
            if (generatedKeys.next()) {
                idUsuarioGenerado = generatedKeys.getInt(1);
            }

            PreparedStatement psCliente = conexion.prepareStatement(sqlCliente);
            psCliente.setInt(1, idUsuarioGenerado);
            psCliente.executeUpdate();

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
            System.err.println("Error al insertar cliente: " + e.getMessage());
            return false;
        } finally {
            conexionBase.cerrarConexion(conexion);
        }
    }

    @Override
    public boolean actualizar(ClienteRegular cliente) {
        String sql = "UPDATE usuario SET nit = ?, dpi = ?, telefono = ?, direccion = ?, correo = ? "
                   + "WHERE id_usuario = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, cliente.getNit());
            ps.setString(2, cliente.getDpi());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getDireccion());
            ps.setString(5, cliente.getCorreo());
            ps.setInt(6, cliente.getIdUsuario());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
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
            System.err.println("Error al desactivar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<ClienteRegular> buscarPorId(int id) {
        String sql = "SELECT u.* FROM usuario u "
                + "JOIN cliente_regular c ON u.id_usuario = c.id_usuario WHERE u.id_usuario = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearCliente(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<ClienteRegular> listarTodos() {
        ArrayList<ClienteRegular> clientes = new ArrayList<>();
        String sql = "SELECT u.* FROM usuario u "
                   + "JOIN cliente_regular c ON u.id_usuario = c.id_usuario";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return clientes;
    }

    public Optional<ClienteRegular> buscarPorCorreo(String correo) {
        String sql = "SELECT u.* FROM usuario u "
                + "JOIN cliente_regular c ON u.id_usuario = c.id_usuario WHERE u.correo = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearCliente(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por correo: " + e.getMessage());
            return Optional.empty();
        }
    }

    private ClienteRegular mapearCliente(ResultSet rs) throws SQLException {
        ClienteRegular cliente = new ClienteRegular();
        cliente.setIdUsuario(rs.getInt("id_usuario"));
        cliente.setNit(rs.getString("nit"));
        cliente.setDpi(rs.getString("dpi"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setCorreo(rs.getString("correo"));
        cliente.setContrasena(rs.getString("contrasena"));
        cliente.setActivo(rs.getBoolean("activo"));
        return cliente;
    }
}
