/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.Bus;
import com.usac.buses.proyectocodenbuses.entidad.Chofer;
import com.usac.buses.proyectocodenbuses.entidad.ViajePrivado;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
/**
 *
 * @author eduar
 */
public class ViajePrivadoPersistencia implements Persistencia<ViajePrivado> {
    private ConexionBase conexionBase = new ConexionBase();
    private BusPersistencia busPersistencia = new BusPersistencia();
    private ChoferPersistencia choferPersistencia = new ChoferPersistencia();

    @Override
    public boolean insertar(ViajePrivado viaje) {
        String sqlViaje = "INSERT INTO viaje (id_bus, id_chofer, fecha_hora_salida, "
                + "fecha_hora_llegada_estimada, tipo) VALUES (?, ?, ?, ?, 'PRIVADO')";
        String sqlViajePrivado = "INSERT INTO viaje_privado (id_viaje, origen, destino, pasajeros, "
                + "precio_estimado, precio_confirmado) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conexion = null;
        try {
            conexion = conexionBase.obtenerConexion();
            conexion.setAutoCommit(false); //Inicia Transaccion: 2 tablas involucradas (viaje + viaje_privado)

            //Insertar en la tabla viaje (datos comunes)
            PreparedStatement psViaje = conexion.prepareStatement(sqlViaje, Statement.RETURN_GENERATED_KEYS);

            //Se permite que bus y chofer aun no esten asignados al momento
            //de solicitar el alquiler; se asignan despues con asignarBusYChofer()
            if (viaje.getBus() != null) {
                psViaje.setInt(1, viaje.getBus().getIdBus());
            } else {
                psViaje.setNull(1, java.sql.Types.INTEGER);
            }
            if (viaje.getChofer() != null) {
                psViaje.setInt(2, viaje.getChofer().getIdUsuario());
            } else {
                psViaje.setNull(2, java.sql.Types.INTEGER);
            }

            psViaje.setTimestamp(3, new Timestamp(viaje.getFechaHoraSalida().getTime()));
            psViaje.setTimestamp(4, new Timestamp(viaje.getFechaHoraLlegadaEstimada().getTime()));
            psViaje.executeUpdate();

            //Obtener el id generado para reutilizarlo en la segunda tabla
            ResultSet generatedKeys = psViaje.getGeneratedKeys();
            int idViajeGenerado = 0;
            if (generatedKeys.next()) {
                idViajeGenerado = generatedKeys.getInt(1);
            }

            //Insertar en la tabla viaje_privado, usando el mismo id + sus datos propios
            PreparedStatement psViajePrivado = conexion.prepareStatement(sqlViajePrivado);
            psViajePrivado.setInt(1, idViajeGenerado);
            psViajePrivado.setString(2, viaje.getOrigen());
            psViajePrivado.setString(3, viaje.getDestino());
            psViajePrivado.setInt(4, viaje.getPasajeros());
            psViajePrivado.setDouble(5, viaje.getPrecioEstimado());
            psViajePrivado.setDouble(6, viaje.getPrecioConfirmado());
            psViajePrivado.executeUpdate();

            conexion.commit(); //Confirma Transaccion ambas inserciones quedan guardadas juntas
            return true;

        } catch (SQLException e) {
            if (conexion != null) {
                try {
                    conexion.rollback(); //Revierte Transaccion: si algo fallo, ninguna de las 2 queda guardada
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error al insertar viaje privado: " + e.getMessage());
            return false;
        } finally {
            conexionBase.cerrarConexion(conexion);
        }
    }

    @Override
    public boolean actualizar(ViajePrivado viaje) {
        Connection conexion = null;
        String sqlViaje = "UPDATE viaje SET id_bus = ?, id_chofer = ?, fecha_hora_salida = ?, "
                        + "fecha_hora_llegada_estimada = ? WHERE id_viaje = ?";
        String sqlViajePrivado = "UPDATE viaje_privado SET origen = ?, destino = ?, pasajeros = ?, "
                                + "precio_estimado = ?, precio_confirmado = ? WHERE id_viaje = ?";

        try {
            conexion = conexionBase.obtenerConexion();
            conexion.setAutoCommit(false); //Inicia Transaccion actualiza 2 tablas juntas

            PreparedStatement psViaje = conexion.prepareStatement(sqlViaje);
            psViaje.setInt(1, viaje.getBus().getIdBus());
            psViaje.setInt(2, viaje.getChofer().getIdUsuario());
            psViaje.setTimestamp(3, new Timestamp(viaje.getFechaHoraSalida().getTime()));
            psViaje.setTimestamp(4, new Timestamp(viaje.getFechaHoraLlegadaEstimada().getTime()));
            psViaje.setInt(5, viaje.getIdViaje());
            psViaje.executeUpdate();

            PreparedStatement psViajePrivado = conexion.prepareStatement(sqlViajePrivado);
            psViajePrivado.setString(1, viaje.getOrigen());
            psViajePrivado.setString(2, viaje.getDestino());
            psViajePrivado.setInt(3, viaje.getPasajeros());
            psViajePrivado.setDouble(4, viaje.getPrecioEstimado());
            psViajePrivado.setDouble(5, viaje.getPrecioConfirmado());
            psViajePrivado.setInt(6, viaje.getIdViaje());
            psViajePrivado.executeUpdate();

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
            System.err.println("Error al actualizar viaje privado: " + e.getMessage());
            return false;
        } finally {
            conexionBase.cerrarConexion(conexion);
        }
    }

    @Override
    public boolean eliminar(int id) {
        Connection conexion = null;
        String sqlViajePrivado = "DELETE FROM viaje_privado WHERE id_viaje = ?";
        String sqlViaje = "DELETE FROM viaje WHERE id_viaje = ?";

        try {
            conexion = conexionBase.obtenerConexion();
            conexion.setAutoCommit(false); //Inicia transaccion: se borran 2 tablas relacionadas, en orden

            //Primero se borra la fila hija, porque tiene la clave foranea
            //que apunta hacia la tabla padre "viaje"
            PreparedStatement psViajePrivado = conexion.prepareStatement(sqlViajePrivado);
            psViajePrivado.setInt(1, id);
            psViajePrivado.executeUpdate();

            //Luego se puede borrar la fila padre sin violar la integridad referencial
            PreparedStatement psViaje = conexion.prepareStatement(sqlViaje);
            psViaje.setInt(1, id);
            psViaje.executeUpdate();

            conexion.commit(); //Confirma transaccion
            return true;

        } catch (SQLException e) {
            if (conexion != null) {
                try {
                    conexion.rollback(); //Revierte transaccion
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error al eliminar viaje privado: " + e.getMessage());
            return false;
        } finally {
            conexionBase.cerrarConexion(conexion);
        }
    }

    @Override
    public Optional<ViajePrivado> buscarPorId(int id) {
        String sql = "SELECT v.*, vp.origen, vp.destino, vp.pasajeros, vp.precio_estimado, "
                + "vp.precio_confirmado FROM viaje v "
                + "JOIN viaje_privado vp ON v.id_viaje = vp.id_viaje WHERE v.id_viaje = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearViajePrivado(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error al buscar viaje privado: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<ViajePrivado> listarTodos() {
        ArrayList<ViajePrivado> viajes = new ArrayList<>();
        String sql = "SELECT v.*, vp.origen, vp.destino, vp.pasajeros, vp.precio_estimado, "
                   + "vp.precio_confirmado FROM viaje v "
                   + "JOIN viaje_privado vp ON v.id_viaje = vp.id_viaje";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                viajes.add(mapearViajePrivado(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar viajes privados: " + e.getMessage());
        }
        return viajes;
    }

    
    //Actualiza solo el precio confirmado de un viaje privado
    //caso de uso: AdminSucursal confirma o cambia el precio estimado)
    public boolean confirmarPrecio(int idViaje, double montoConfirmado) {
        String sql = "UPDATE viaje_privado SET precio_confirmado = ? WHERE id_viaje = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setDouble(1, montoConfirmado);
            ps.setInt(2, idViaje);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al confirmar precio: " + e.getMessage());
            return false;
        }
    }

    private ViajePrivado mapearViajePrivado(ResultSet rs) throws SQLException {
        ViajePrivado viaje = new ViajePrivado();
        viaje.setIdViaje(rs.getInt("id_viaje"));
        Bus bus = busPersistencia.buscarPorId(rs.getInt("id_bus")).orElse(null);
        viaje.setBus(bus);
        Chofer chofer = choferPersistencia.buscarPorId(rs.getInt("id_chofer")).orElse(null);
        viaje.setChofer(chofer);
        viaje.setFechaHoraSalida(new Date(rs.getTimestamp("fecha_hora_salida").getTime()));
        viaje.setFechaHoraLlegadaEstimada(new Date(rs.getTimestamp("fecha_hora_llegada_estimada").getTime()));
        viaje.setOrigen(rs.getString("origen"));
        viaje.setDestino(rs.getString("destino"));
        viaje.setPasajeros(rs.getInt("pasajeros"));
        viaje.setPrecioEstimado(rs.getDouble("precio_estimado"));
        viaje.setPrecioConfirmado(rs.getDouble("precio_confirmado"));
        return viaje;
    }
    
    public double obtenerIngresosPorSucursalYFecha(int idSucursal, Date desde, Date hasta) {
        String sql = "SELECT COALESCE(SUM(vp.precio_confirmado), 0) AS total FROM viaje_privado vp "
                + "JOIN viaje v ON vp.id_viaje = v.id_viaje "
                + "JOIN bus bu ON v.id_bus = bu.id_bus "
                + "WHERE bu.id_sucursal = ? AND v.fecha_hora_salida BETWEEN ? AND ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSucursal);
            ps.setTimestamp(2, new Timestamp(desde.getTime()));
            ps.setTimestamp(3, new Timestamp(hasta.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
            return 0;

        } catch (SQLException e) {
            System.err.println("Error al obtener ingresos de alquiler por sucursal: " + e.getMessage());
            return 0;
        }
    }
    
    public ArrayList<ViajePrivado> listarPorSucursalYFecha(int idSucursal, Date desde, Date hasta) {
        ArrayList<ViajePrivado> viajes = new ArrayList<>();
        String sql = "SELECT v.*, vp.origen, vp.destino, vp.pasajeros, vp.precio_estimado, "
                + "vp.precio_confirmado FROM viaje v "
                + "JOIN viaje_privado vp ON v.id_viaje = vp.id_viaje "
                + "JOIN bus bu ON v.id_bus = bu.id_bus "
                + "WHERE bu.id_sucursal = ? AND v.fecha_hora_salida BETWEEN ? AND ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSucursal);
            ps.setTimestamp(2, new Timestamp(desde.getTime()));
            ps.setTimestamp(3, new Timestamp(hasta.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                viajes.add(mapearViajePrivado(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar alquileres por sucursal y fecha: " + e.getMessage());
        }
        return viajes;
    }
}
