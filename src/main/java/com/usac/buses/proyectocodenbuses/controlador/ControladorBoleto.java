/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.controlador;

import com.usac.buses.proyectocodenbuses.entidad.*;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionAsientoNoDisponible;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionCreditoInsuficiente;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionFormatoInvalido;
import com.usac.buses.proyectocodenbuses.persistencia.BoletoPersistencia;
import com.usac.buses.proyectocodenbuses.persistencia.CarteraPersistencia;
import com.usac.buses.proyectocodenbuses.persistencia.ViajeRegularPersistencia;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ControladorBoleto", urlPatterns = {"/boleto"})
public class ControladorBoleto extends HttpServlet {

    private BoletoPersistencia boletoPersistencia = new BoletoPersistencia();
    private ViajeRegularPersistencia viajeRegularPersistencia = new ViajeRegularPersistencia();
    private CarteraPersistencia carteraPersistencia = new CarteraPersistencia();

    private Usuario obtenerUsuarioSesion(HttpServletRequest request) {
        HttpSession sesion = request.getSession();
        return (Usuario) sesion.getAttribute("usuario");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            response.sendRedirect("usuario?accion=login");
            return;
        }

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "buscarViajes";
        }

        switch (accion) {
            case "buscarViajes":
                buscarViajesDisponibles(request, response);
                break;
            case "elegirAsiento":
                mostrarSeleccionAsiento(request, response);
                break;
            case "misBoletos":
                listarMisBoletos(request, response, usuario);
                break;
            default:
                response.sendRedirect("boleto?accion=buscarViajes");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            response.sendRedirect("usuario?accion=login");
            return;
        }
        if (!(usuario instanceof ClienteRegular)) {
            response.sendRedirect("boleto?accion=buscarViajes");
            return;
        }

        String accion = request.getParameter("accion");

        if ("comprar".equals(accion)) {
            comprarBoleto(request, response, (ClienteRegular) usuario);
        } else {
            response.sendRedirect("boleto?accion=buscarViajes");
        }
    }

    private void buscarViajesDisponibles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<ViajeRegular> viajes = viajeRegularPersistencia.listarTodos();

        //Se arma cada fila con los asientos disponibles calculados,
        //tal como pide el enunciado en el listado de viajes
        ArrayList<Object[]> filasReporte = new ArrayList<>();
        for (ViajeRegular viaje : viajes) {
            int disponibles = boletoPersistencia.contarAsientosDisponibles(
                viaje.getIdViaje(), viaje.getBus().getCapacidad());
            filasReporte.add(new Object[]{viaje, disponibles});
        }

        request.setAttribute("filasViajes", filasReporte);
        request.getRequestDispatcher("/vistas/boleto/buscarViajes.jsp").forward(request, response);
    }

    private void mostrarSeleccionAsiento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idViaje = Integer.parseInt(request.getParameter("idViaje"));

        viajeRegularPersistencia.buscarPorId(idViaje).ifPresentOrElse(
            viaje -> {
                ArrayList<Integer> asientosOcupados = boletoPersistencia.listarAsientosOcupados(idViaje);
                request.setAttribute("viaje", viaje);
                request.setAttribute("asientosOcupados", asientosOcupados);
                try {
                    request.getRequestDispatcher("/vistas/boleto/elegirAsiento.jsp").forward(request, response);
                } catch (ServletException | IOException e) {
                    System.err.println("Error al mostrar selección de asiento: " + e.getMessage());
                }
            },
            () -> {
                try {
                    response.sendRedirect("boleto?accion=buscarViajes");
                } catch (IOException e) {
                    System.err.println("Error al redirigir: " + e.getMessage());
                }
            }
        );
    }

    private void listarMisBoletos(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        ArrayList<Boleto> boletos = boletoPersistencia.listarPorCliente(usuario.getIdUsuario());
        request.setAttribute("boletos", boletos);
        request.getRequestDispatcher("/vistas/boleto/misBoletos.jsp").forward(request, response);
    }

    
    //Flujo completo de compra: valida asiento disponible, valida saldo,
    //y ejecuta la transacción (boleto + descuento cartera + movimiento)
    private void comprarBoleto(HttpServletRequest request, HttpServletResponse response, ClienteRegular cliente)
            throws IOException, ServletException {
        try {
            int idViaje = Integer.parseInt(request.getParameter("idViaje"));
            int numeroAsiento = Integer.parseInt(request.getParameter("numeroAsiento"));

            ViajeRegular viaje = viajeRegularPersistencia.buscarPorId(idViaje).orElse(null);
            if (viaje == null) {
                response.sendRedirect("boleto?accion=buscarViajes");
                return;
            }

            //Validar capacidad del bus
            if (numeroAsiento < 1 || numeroAsiento > viaje.getBus().getCapacidad()) {
                throw new ExcepcionFormatoInvalido("numeroAsiento", "El número de asiento no es válido para este bus");
            }

            //Validar que el asiento no este ya ocupado
            ArrayList<Integer> asientosOcupados = boletoPersistencia.listarAsientosOcupados(idViaje);
            if (asientosOcupados.contains(numeroAsiento)) {
                throw new ExcepcionAsientoNoDisponible("El asiento " + numeroAsiento + " ya está ocupado");
            }

            //Validar saldo en cartera
            Cartera cartera = carteraPersistencia.buscarPorUsuario(cliente.getIdUsuario()).orElse(null);
            if (cartera == null || cartera.getSaldo() < viaje.getRuta().getPrecioBoleto()) {
                throw new ExcepcionCreditoInsuficiente("Saldo insuficiente en la cartera para completar la compra");
            }

            Boleto boleto = new Boleto(viaje, cliente, numeroAsiento, new Date(), viaje.getRuta().getPrecioBoleto());
            boolean exito = boletoPersistencia.comprarBoleto(boleto, cartera.getIdCartera(), cartera.getSaldo());

            if (exito) {
                response.sendRedirect("boleto?accion=misBoletos");
            } else {
                request.setAttribute("error", "No se pudo completar la compra, intente nuevamente");
                mostrarSeleccionAsiento(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Datos numéricos inválidos");
            mostrarSeleccionAsiento(request, response);
        } catch (ExcepcionAsientoNoDisponible | ExcepcionCreditoInsuficiente | ExcepcionFormatoInvalido e) {
            request.setAttribute("error", e.getMessage());
            mostrarSeleccionAsiento(request, response);
        }
    }
}
