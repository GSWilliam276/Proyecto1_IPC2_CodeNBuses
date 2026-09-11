/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.Chofer;
import com.usac.buses.proyectocodenbuses.entidad.Sucursal;
import com.usac.buses.proyectocodenbuses.entidad.TipoLicencia;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
/**
 *
 * @author eduar
 */
public class ChoferPersistencia implements Persistencia<Chofer> {
    private ConexionBase conexionBase = new ConexionBase();
    private SucursalPersistencia sucursalPersistencia = new SucursalPersistencia();

    @Override
    public boolean insertar(Chofer chofer) {
        String sqlUsuario = "INSERT INTO usuario (nit, dpi, telefono, direccion, correo, contrasena, tipo, activo) "
                           + "VALUES (?, ?, ?, ?, ?, ?, 'CHOFER', ?)";
        String sqlChofer = "INSERT INTO chofer (id_usuario, numero_licencia, tipo_licencia, fecha_vencimiento, "
                          + "salario_base, id_sucursal) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conexion = null;
        try {
            conexion = conexionBase.obtenerConexion();
            conexion.setAutoCommit(false); //Inicia la transacción

            //Insertar en la tabla usuario
            PreparedStatement psUsuario = conexion.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
            psUsuario.setString(1, chofer.getNit());
            psUsuario.setString(2, chofer.getDpi());
            psUsuario.setString(3, chofer.getTelefono());
            psUsuario.setString(4, chofer.getDireccion());
            psUsuario.setString(5, chofer.getCorreo());
            psUsuario.setString(6, chofer.getContrasena());
            psUsuario.setBoolean(7, chofer.isActivo());
            psUsuario.executeUpdate();

            //Obtener el id generado automaticamente para usarlo en la tabla chofer
            ResultSet generatedKeys = psUsuario.getGeneratedKeys();
            int idUsuarioGenerado = 0;
            if (generatedKeys.next()) {
                idUsuarioGenerado = generatedKeys.getInt(1);
            }

            //Insertar en la tabla chofer, usando el mismo id
            PreparedStatement psChofer = conexion.prepareStatement(sqlChofer);
            psChofer.setInt(1, idUsuarioGenerado);
            psChofer.setString(2, chofer.getNumeroLicencia());
            psChofer.setString(3, chofer.getTipoLicencia().name());
            psChofer.setDate(4, new java.sql.Date(chofer.getFechaVencimiento().getTime()));
            psChofer.setDouble(5, chofer.getSalarioBase());
            psChofer.setInt(6, chofer.getSucursal().getIdSucursal());
            psChofer.executeUpdate();

            conexion.commit(); //ambas inserciones salieron bien, se confirman juntas
            return true;

        } catch (SQLException e) {
            if (conexion != null) {
                try {
                    conexion.rollback(); //algo fallo, se revierte TODO (ni usuario ni chofer quedan guardados)
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error al insertar chofer: " + e.getMessage());
            return false;
        } finally {
            conexionBase.cerrarConexion(conexion);
        }
    }

    @Override
    public boolean actualizar(Chofer chofer) {
        String sql = "UPDATE chofer SET numero_licencia = ?, tipo_licencia = ?, fecha_vencimiento = ?, "
                   + "salario_base = ?, id_sucursal = ? WHERE id_usuario = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, chofer.getNumeroLicencia());
            ps.setString(2, chofer.getTipoLicencia().name());
            ps.setDate(3, new java.sql.Date(chofer.getFechaVencimiento().getTime()));
            ps.setDouble(4, chofer.getSalarioBase());
            ps.setInt(5, chofer.getSucursal().getIdSucursal());
            ps.setInt(6, chofer.getIdUsuario());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar chofer: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        //Los choferes no se eliminan, solo se activan/desactivan
        String sql = "UPDATE usuario SET activo = false WHERE id_usuario = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al desactivar chofer: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Chofer> buscarPorId(int id) {
        String sql = "SELECT u.*, c.numero_licencia, c.tipo_licencia, c.fecha_vencimiento, "
                + "c.salario_base, c.id_sucursal FROM usuario u "
                + "JOIN chofer c ON u.id_usuario = c.id_usuario WHERE u.id_usuario = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearChofer(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error al buscar chofer: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<Chofer> listarTodos() {
        ArrayList<Chofer> choferes = new ArrayList<>();
        String sql = "SELECT u.*, c.numero_licencia, c.tipo_licencia, c.fecha_vencimiento, "
                   + "c.salario_base, c.id_sucursal FROM usuario u "
                   + "JOIN chofer c ON u.id_usuario = c.id_usuario";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                choferes.add(mapearChofer(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar choferes: " + e.getMessage());
        }
        return choferes;
    }

    public ArrayList<Chofer> listarPorSucursal(int idSucursal) {
        ArrayList<Chofer> choferes = new ArrayList<>();
        String sql = "SELECT u.*, c.numero_licencia, c.tipo_licencia, c.fecha_vencimiento, "
                   + "c.salario_base, c.id_sucursal FROM usuario u "
                   + "JOIN chofer c ON u.id_usuario = c.id_usuario WHERE c.id_sucursal = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                choferes.add(mapearChofer(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar choferes por sucursal: " + e.getMessage());
        }
        return choferes;
    }

    public ArrayList<Chofer> listarActivos() {
        ArrayList<Chofer> choferes = new ArrayList<>();
        String sql = "SELECT u.*, c.numero_licencia, c.tipo_licencia, c.fecha_vencimiento, "
                   + "c.salario_base, c.id_sucursal FROM usuario u "
                   + "JOIN chofer c ON u.id_usuario = c.id_usuario WHERE u.activo = true";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                choferes.add(mapearChofer(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar choferes activos: " + e.getMessage());
        }
        return choferes;
    }

    private Chofer mapearChofer(ResultSet rs) throws SQLException {
        Chofer chofer = new Chofer();
        chofer.setIdUsuario(rs.getInt("id_usuario"));
        chofer.setNit(rs.getString("nit"));
        chofer.setDpi(rs.getString("dpi"));
        chofer.setTelefono(rs.getString("telefono"));
        chofer.setDireccion(rs.getString("direccion"));
        chofer.setCorreo(rs.getString("correo"));
        chofer.setContrasena(rs.getString("contrasena"));
        chofer.setActivo(rs.getBoolean("activo"));
        chofer.setNumeroLicencia(rs.getString("numero_licencia"));
        chofer.setTipoLicencia(TipoLicencia.valueOf(rs.getString("tipo_licencia")));
        chofer.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
        chofer.setSalarioBase(rs.getDouble("salario_base"));
        Sucursal sucursal = sucursalPersistencia.buscarPorId(rs.getInt("id_sucursal")).orElse(null);
        chofer.setSucursal(sucursal);
        return chofer;
    }
    
    public Optional<Chofer> buscarPorCorreo(String correo) {
        String sql = "SELECT u.*, c.numero_licencia, c.tipo_licencia, c.fecha_vencimiento, "
                + "c.salario_base, c.id_sucursal FROM usuario u "
                + "JOIN chofer c ON u.id_usuario = c.id_usuario WHERE u.correo = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearChofer(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            System.err.println("Error al buscar chofer por correo: " + e.getMessage());
            return Optional.empty();
        }
    }
}
