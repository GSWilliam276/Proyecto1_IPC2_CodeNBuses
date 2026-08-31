/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.persistencia;

import com.usac.buses.proyectocodenbuses.entidad.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
/**
 *
 * @author eduar
 */
public class BoletoPersistencia implements Persistencia<Boleto> {
    private ConexionBase conexionBase = new ConexionBase();
    private ViajeRegularPersistencia viajeRegularPersistencia = new ViajeRegularPersistencia();
    private ClienteRegularPersistencia clienteRegularPersistencia = new ClienteRegularPersistencia();

    @Override
    public boolean insertar(Boleto boleto) {
        String sql = "INSERT INTO boleto (id_viaje_regular, id_cliente, numero_asiento, fecha_pago, precio) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, boleto.getViaje().getIdViaje());
            ps.setInt(2, boleto.getCliente().getIdUsuario());
            ps.setInt(3, boleto.getNumeroAsiento());
            ps.setTimestamp(4, new Timestamp(boleto.getFechaPago().getTime()));
            ps.setDouble(5, boleto.getPrecio());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar boleto: " + e.getMessage());
            return false;
        }
    }

    
    //Compra un boleto completo: inserta el boleto, descuenta el saldo de la cartera,
    // y registra el movimiento, las 3 operaciones ocurren juntas o ninguna
    public boolean comprarBoleto(Boleto boleto, int idCartera, double saldoActual) {
        if (saldoActual < boleto.getPrecio()) {
            return false; //Saldo insuficiente, el Controlador decide lanzar ExcepcionCreditoInsuficiente
        }

        String sqlBoleto = "INSERT INTO boleto (id_viaje_regular, id_cliente, numero_asiento, fecha_pago, precio) "
                          + "VALUES (?, ?, ?, ?, ?)";
        String sqlDescontarCartera = "UPDATE cartera SET saldo = saldo - ? WHERE id_cartera = ?";
        String sqlMovimiento = "INSERT INTO movimiento_cartera (id_cartera, tipo, monto, fecha) "
                              + "VALUES (?, 'PAGO', ?, ?)";

        Connection conexion = null;
        try {
            conexion = conexionBase.obtenerConexion();
            conexion.setAutoCommit(false); //Inicia Transaccion: 3 operaciones deben ocurrir juntas

            //Insertar el boleto
            PreparedStatement psBoleto = conexion.prepareStatement(sqlBoleto);
            psBoleto.setInt(1, boleto.getViaje().getIdViaje());
            psBoleto.setInt(2, boleto.getCliente().getIdUsuario());
            psBoleto.setInt(3, boleto.getNumeroAsiento());
            psBoleto.setTimestamp(4, new Timestamp(boleto.getFechaPago().getTime()));
            psBoleto.setDouble(5, boleto.getPrecio());
            psBoleto.executeUpdate();

            //Descontar el saldo de la cartera
            PreparedStatement psCartera = conexion.prepareStatement(sqlDescontarCartera);
            psCartera.setDouble(1, boleto.getPrecio());
            psCartera.setInt(2, idCartera);
            psCartera.executeUpdate();

            //Registrar el movimiento de cartera
            PreparedStatement psMovimiento = conexion.prepareStatement(sqlMovimiento);
            psMovimiento.setInt(1, idCartera);
            psMovimiento.setDouble(2, boleto.getPrecio());
            psMovimiento.setTimestamp(3, new Timestamp(boleto.getFechaPago().getTime()));
            psMovimiento.executeUpdate();

            conexion.commit(); //Confirma Transaccion: las 3 operaciones quedan guardadas juntas
            return true;

        } catch (SQLException e) {
            if (conexion != null) {
                try {
                    conexion.rollback(); //Revierte Transaccion: si algo fallo, ninguna de las 3 queda guardada
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error al comprar boleto: " + e.getMessage());
            return false;
        } finally {
            conexionBase.cerrarConexion(conexion);
        }
    }

    @Override
    public boolean actualizar(Boleto boleto) {
        //Los boletos no se editan una vez comprados
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        //Los boletos no se eliminan una vez comprados
        return false;
    }

    @Override
    public Optional<Boleto> buscarPorId(int id) {
        String sql = "SELECT * FROM boleto WHERE id_boleto = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearBoleto(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error al buscar boleto: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<Boleto> listarTodos() {
        ArrayList<Boleto> boletos = new ArrayList<>();
        String sql = "SELECT * FROM boleto";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                boletos.add(mapearBoleto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar boletos: " + e.getMessage());
        }
        return boletos;
    }

    public ArrayList<Boleto> listarPorViaje(int idViaje) {
        ArrayList<Boleto> boletos = new ArrayList<>();
        String sql = "SELECT * FROM boleto WHERE id_viaje_regular = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idViaje);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                boletos.add(mapearBoleto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar boletos por viaje: " + e.getMessage());
        }
        return boletos;
    }

    public int contarPorViaje(int idViaje) {
        String sql = "SELECT COUNT(*) AS total FROM boleto WHERE id_viaje_regular = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idViaje);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;

        } catch (SQLException e) {
            System.err.println("Error al contar boletos por viaje: " + e.getMessage());
            return 0;
        }
    }

    public ArrayList<Integer> listarAsientosOcupados(int idViaje) {
        ArrayList<Integer> asientos = new ArrayList<>();
        String sql = "SELECT numero_asiento FROM boleto WHERE id_viaje_regular = ?";
        try (Connection conexion = conexionBase.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idViaje);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                asientos.add(rs.getInt("numero_asiento"));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar asientos ocupados: " + e.getMessage());
        }
        return asientos;
    }

    private Boleto mapearBoleto(ResultSet rs) throws SQLException {
        Boleto boleto = new Boleto();
        boleto.setIdBoleto(rs.getInt("id_boleto"));
        ViajeRegular viaje = viajeRegularPersistencia.buscarPorId(rs.getInt("id_viaje_regular")).orElse(null);
        boleto.setViaje(viaje);
        ClienteRegular cliente = clienteRegularPersistencia.buscarPorId(rs.getInt("id_cliente")).orElse(null);
        boleto.setCliente(cliente);
        boleto.setNumeroAsiento(rs.getInt("numero_asiento"));
        boleto.setFechaPago(new Date(rs.getTimestamp("fecha_pago").getTime()));
        boleto.setPrecio(rs.getDouble("precio"));
        return boleto;
    }
}
