/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.controlador;

import com.usac.buses.proyectocodenbuses.entidad.Bus;
import com.usac.buses.proyectocodenbuses.entidad.Sucursal;
import com.usac.buses.proyectocodenbuses.persistencia.BusPersistencia;
import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionFormatoInvalido;

@WebServlet(name = "ControladorBus", urlPatterns = {"/bus"})
public class ControladorBus extends HttpServlet {

    private BusPersistencia busPersistencia = new BusPersistencia();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                listarBuses(request, response);
                break;
            case "nuevo":
                request.getRequestDispatcher("/vistas/bus/registrarBus.jsp").forward(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            default:
                response.sendRedirect("bus?accion=listar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        switch (accion) {
            case "registrar":
                registrarBus(request, response);
                break;
            case "actualizar":
                actualizarBus(request, response);
                break;
            case "desactivar":
                desactivarBus(request, response);
                break;
            default:
                response.sendRedirect("bus?accion=listar");
        }
    }

    private void listarBuses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<Bus> buses = busPersistencia.listarTodos();
        request.setAttribute("buses", buses);
        request.getRequestDispatcher("/vistas/bus/listarBuses.jsp").forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        busPersistencia.buscarPorId(id).ifPresentOrElse(
            bus -> {
                request.setAttribute("bus", bus);
                try {
                    request.getRequestDispatcher("/vistas/bus/editarBus.jsp").forward(request, response);
                } catch (ServletException | IOException e) {
                    System.err.println("Error al mostrar formulario de edición: " + e.getMessage());
                }
            },
            () -> {
                try {
                    response.sendRedirect("bus?accion=listar");
                } catch (IOException e) {
                    System.err.println("Error al redirigir: " + e.getMessage());
                }
            }
        );
    }

    private void registrarBus(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        try {
            String placa = request.getParameter("placa");
            String marca = request.getParameter("marca");
            String modelo = request.getParameter("modelo");
            int anio;
            int capacidad;

            try {
                anio = Integer.parseInt(request.getParameter("anio"));
                capacidad = Integer.parseInt(request.getParameter("capacidad"));
            } catch (NumberFormatException e) {
                throw new ExcepcionFormatoInvalido("anio/capacidad", "Debe ingresar valores numéricos válidos");
            }

            String foto = request.getParameter("foto");
            int idSucursal = Integer.parseInt(request.getParameter("idSucursal"));

            Sucursal sucursal = new Sucursal();
            sucursal.setIdSucursal(idSucursal);

            Bus bus = new Bus(sucursal, placa, marca, modelo, anio, capacidad, foto);
            busPersistencia.insertar(bus);

            response.sendRedirect("bus?accion=listar");

        } catch (ExcepcionFormatoInvalido e) {
            request.setAttribute("error", e.getMessage() + " (Campo: " + e.getCampo() + ")");
            request.getRequestDispatcher("/vistas/bus/registrarBus.jsp").forward(request, response);
        }
    }

    private void actualizarBus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idBus = Integer.parseInt(request.getParameter("idBus"));
        String placa = request.getParameter("placa");
        String marca = request.getParameter("marca");
        String modelo = request.getParameter("modelo");
        int anio = Integer.parseInt(request.getParameter("anio"));
        int capacidad = Integer.parseInt(request.getParameter("capacidad"));
        int idSucursal = Integer.parseInt(request.getParameter("idSucursal"));

        Sucursal sucursal = new Sucursal();
        sucursal.setIdSucursal(idSucursal);

        Bus bus = new Bus(sucursal, placa, marca, modelo, anio, capacidad, null);
        bus.setIdBus(idBus);

        busPersistencia.actualizar(bus);
        response.sendRedirect("bus?accion=listar");
    }

    private void desactivarBus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        busPersistencia.eliminar(id); 
        response.sendRedirect("bus?accion=listar");
    }
}
