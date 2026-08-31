/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.Bus;
import com.usac.buses.proyectocodenbuses.entidad.Chofer;
import com.usac.buses.proyectocodenbuses.entidad.Ruta;
import com.usac.buses.proyectocodenbuses.entidad.ViajeRegular;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
/**
 *
 * @author eduar
 */
public class ViajeRegularPersistencia implements Persistencia<ViajeRegular> {
    private ConexionBase conexionBase = new ConexionBase();
    private BusPersistencia busPersistencia = new BusPersistencia();
    private ChoferPersistencia choferPersistencia = new ChoferPersistencia();
    private RutaPersistencia rutaPersistencia = new RutaPersistencia();

    @Override
    public boolean insertar(ViajeRegular viaje) {
        String sqlViaje = "INSERT INTO viaje (id_bus, id_chofer, fecha_hora_salida, "
                        + "fecha_hora_llegada_estimada, tipo) VALUES (?, ?, ?, ?, 'REGULAR')";
        String sqlViajeRegular = "INSERT INTO viaje_regular (id_viaje, id_ruta) VALUES (?, ?)";

        Connection conexion = null;
        try {
            conexion = conexionBase.obtenerConexion();
            conexion.setAutoCommit(false); //Inicia Transaccion: 2 tablas involucradas (viaje + viaje_regular)

            //Insertar en la tabla viaje (datos comunes)
            PreparedStatement psViaje = conexion.prepareStatement(sqlViaje, Statement.RETURN_GENERATED_KEYS);
            psViaje.setInt(1, viaje.getBus().getIdBus());
            psViaje.setInt(2, viaje.getChofer().getIdUsuario());
            psViaje.setTimestamp(3, new Timestamp(viaje.getFechaHoraSalida().getTime()));
            psViaje.setTimestamp(4, new Timestamp(viaje.getFechaHoraLlegadaEstimada().getTime()));
            psViaje.executeUpdate();

            //Obtener el id generado para reutilizarlo en la segunda tabla
            ResultSet generatedKeys = psViaje.getGeneratedKeys();
            int idViajeGenerado = 0;
            if (generatedKeys.next()) {
                idViajeGenerado = generatedKeys.getInt(1);
            }

            //Insertar en la tabla viaje_regular, usando el mismo id + la ruta asignada
            PreparedStatement psViajeRegular = conexion.prepareStatement(sqlViajeRegular);
            psViajeRegular.setInt(1, idViajeGenerado);
            psViajeRegular.setInt(2, viaje.getRuta().getIdRuta());
            psViajeRegular.executeUpdate();

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
            System.err.println("Error al insertar viaje regular: " + e.getMessage());
            return false;
        } finally {
            conexionBase.cerrarConexion(conexion);
        }
    }

    @Override
    public boolean actualizar(ViajeRegular viaje) {
        //No se permite modificar el tipo de viaje (se valida en el Controlador antes de llegar aquí)
        Connection conexion = null;
        String sqlViaje = "UPDATE viaje SET id_bus = ?, id_chofer = ?, fecha_hora_salida = ?, "
                        + "fecha_hora_llegada_estimada = ? WHERE id_viaje = ?";
        String sqlViajeRegular = "UPDATE viaje_regular SET id_ruta = ? WHERE id_viaje = ?";

        try {
            conexion = conexionBase.obtenerConexion();
            conexion.setAutoCommit(false); //Inicia Transaccion: actualiza 2 tablas juntas

            PreparedStatement psViaje = conexion.prepareStatement(sqlViaje);
            psViaje.setInt(1, viaje.getBus().getIdBus());
            psViaje.setInt(2, viaje.getChofer().getIdUsuario());
            psViaje.setTimestamp(3, new Timestamp(viaje.getFechaHoraSalida().getTime()));
            psViaje.setTimestamp(4, new Timestamp(viaje.getFechaHoraLlegadaEstimada().getTime()));
            psViaje.setInt(5, viaje.getIdViaje());
            psViaje.executeUpdate();

            PreparedStatement psViajeRegular = conexion.prepareStatement(sqlViajeRegular);
            psViajeRegular.setInt(1, viaje.getRuta().getIdRuta());
            psViajeRegular.setInt(2, viaje.getIdViaje());
            psViajeRegular.executeUpdate();

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
            System.err.println("Error al actualizar viaje regular: " + e.getMessage());
            return false;
        } finally {
            conexionBase.cerrarConexion(conexion);
        }
    }

    @Override
    public boolean eliminar(int id) {
        //Solo se permite eliminar si el viaje no ha sido iniciado ni pagado (se valida en el Controlador)
        String sql = "DELETE FROM viaje WHERE id_viaje = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar viaje regular: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<ViajeRegular> buscarPorId(int id) {
        String sql = "SELECT v.*, vr.id_ruta FROM viaje v "
                + "JOIN viaje_regular vr ON v.id_viaje = vr.id_viaje WHERE v.id_viaje = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearViajeRegular(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error al buscar viaje regular: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<ViajeRegular> listarTodos() {
        ArrayList<ViajeRegular> viajes = new ArrayList<>();
        String sql = "SELECT v.*, vr.id_ruta FROM viaje v "
                   + "JOIN viaje_regular vr ON v.id_viaje = vr.id_viaje";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                viajes.add(mapearViajeRegular(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar viajes regulares: " + e.getMessage());
        }
        return viajes;
    }

    private ViajeRegular mapearViajeRegular(ResultSet rs) throws SQLException {
        ViajeRegular viaje = new ViajeRegular();
        viaje.setIdViaje(rs.getInt("id_viaje"));
        Bus bus = busPersistencia.buscarPorId(rs.getInt("id_bus")).orElse(null);
        viaje.setBus(bus);
        Chofer chofer = choferPersistencia.buscarPorId(rs.getInt("id_chofer")).orElse(null);
        viaje.setChofer(chofer);
        viaje.setFechaHoraSalida(new Date(rs.getTimestamp("fecha_hora_salida").getTime()));
        viaje.setFechaHoraLlegadaEstimada(new Date(rs.getTimestamp("fecha_hora_llegada_estimada").getTime()));
        Ruta ruta = rutaPersistencia.buscarPorId(rs.getInt("id_ruta")).orElse(null);
        viaje.setRuta(ruta);
        return viaje;
    }
}
