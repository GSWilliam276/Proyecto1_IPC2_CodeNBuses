/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.controlador;

import com.usac.buses.proyectocodenbuses.entidad.*;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionFormatoInvalido;
import com.usac.buses.proyectocodenbuses.persistencia.GastoPersistencia;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.usac.buses.proyectocodenbuses.persistencia.BusPersistencia;

@WebServlet(name = "ControladorGasto", urlPatterns = {"/gasto"})
public class ControladorGasto extends HttpServlet {

    private GastoPersistencia gastoPersistencia = new GastoPersistencia();

    private boolean verificarAcceso(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("usuario?accion=login");
            return false;
        }
        if (!(usuario instanceof AdminSucursal)) {
            response.sendRedirect("usuario?accion=perfil");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!verificarAcceso(request, response)) {
            return;
        }

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                listarGastosPorBus(request, response);
                break;
            case "nuevo":
                request.setAttribute("buses", new BusPersistencia().listarTodos());
                request.getRequestDispatcher("/vistas/gasto/registrarGasto.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("gasto?accion=listar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!verificarAcceso(request, response)) {
            return;
        }

        String accion = request.getParameter("accion");

        if ("registrar".equals(accion)) {
            registrarGasto(request, response);
        } else {
            response.sendRedirect("gasto?accion=listar");
        }
    }

    private void listarGastosPorBus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idBus = Integer.parseInt(request.getParameter("idBus"));
        ArrayList<Gasto> gastos = gastoPersistencia.listarPorBus(idBus);
        request.setAttribute("gastos", gastos);
        request.setAttribute("idBus", idBus);
        request.getRequestDispatcher("/vistas/gasto/listarGastos.jsp").forward(request, response);
    }

    private void registrarGasto(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int idBus = Integer.parseInt(request.getParameter("idBus"));
            double montoManoObra = Double.parseDouble(request.getParameter("montoManoObra"));
            double montoRepuestos = Double.parseDouble(request.getParameter("montoRepuestos"));
            String fechaStr = request.getParameter("fecha");

            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = formato.parse(fechaStr);

            Bus bus = new Bus();
            bus.setIdBus(idBus);

            Gasto gasto = new Gasto(bus, montoManoObra, montoRepuestos, fecha);
            gastoPersistencia.insertar(gasto);

            response.sendRedirect("gasto?accion=listar&idBus=" + idBus);

        } catch (NumberFormatException e) {
            mostrarError(request, response, "montoManoObra/montoRepuestos", "Debe ingresar valores numéricos válidos", "/vistas/gasto/registrarGasto.jsp");
        } catch (ParseException e) {
            mostrarError(request, response, "fecha", "Formato de fecha inválido", "/vistas/gasto/registrarGasto.jsp");
        }
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response,
                               String campo, String mensaje, String vista) throws IOException, ServletException {
        ExcepcionFormatoInvalido excepcion = new ExcepcionFormatoInvalido(campo, mensaje);
        request.setAttribute("error", excepcion.getMessage() + " (Campo: " + excepcion.getCampo() + ")");
        request.getRequestDispatcher(vista).forward(request, response);
    }
}
