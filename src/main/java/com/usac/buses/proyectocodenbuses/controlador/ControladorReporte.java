/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.controlador;

import com.usac.buses.proyectocodenbuses.entidad.*;
import com.usac.buses.proyectocodenbuses.persistencia.*;
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

@WebServlet(name = "ControladorReporte", urlPatterns = {"/reporte"})
public class ControladorReporte extends HttpServlet {

    private BusPersistencia busPersistencia = new BusPersistencia();
    private ChoferPersistencia choferPersistencia = new ChoferPersistencia();
    private BoletoPersistencia boletoPersistencia = new BoletoPersistencia();
    private ViajePrivadoPersistencia viajePrivadoPersistencia = new ViajePrivadoPersistencia();
    private GastoPersistencia gastoPersistencia = new GastoPersistencia();
    private ViajePersistencia viajePersistencia = new ViajePersistencia();

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
            accion = "menu";
        }

        switch (accion) {
            case "menu":
                request.getRequestDispatcher("/vistas/reporte/menuReportes.jsp").forward(request, response);
                break;
            case "listadoBuses":
                reporteListadoBuses(request, response, usuario);
                break;
            case "listadoChoferes":
                reporteListadoChoferes(request, response, usuario);
                break;
            case "ingresosBoletos":
                reporteIngresosBoletos(request, response, usuario);
                break;
            case "ingresosAlquiler":
                reporteIngresosAlquiler(request, response, usuario);
                break;
            case "depreciacionPorBus":
                reporteDepreciacionPorBus(request, response, usuario);
                break;
            default:
                response.sendRedirect("reporte?accion=menu");
        }
    }

    
    //Listado general de buses, opcionalmente filtrado por estado
    private void reporteListadoBuses(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        if (!(usuario instanceof AdminSucursal)) {
            response.sendRedirect("reporte?accion=menu");
            return;
        }
        AdminSucursal admin = (AdminSucursal) usuario;

        ArrayList<Bus> buses = busPersistencia.listarPorSucursal(admin.getSucursal().getIdSucursal());
        request.setAttribute("buses", buses);
        request.getRequestDispatcher("/vistas/reporte/listadoBuses.jsp").forward(request, response);
    }

    //Listado general de choferes de la sucursal del admin
    private void reporteListadoChoferes(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        if (!(usuario instanceof AdminSucursal)) {
            response.sendRedirect("reporte?accion=menu");
            return;
        }
        AdminSucursal admin = (AdminSucursal) usuario;

        ArrayList<Chofer> choferes = choferPersistencia.listarPorSucursal(admin.getSucursal().getIdSucursal());
        request.setAttribute("choferes", choferes);
        request.getRequestDispatcher("/vistas/reporte/listadoChoferes.jsp").forward(request, response);
    }

    //Reporte de ingresos por venta de boletos en un intervalo de tiempo
    private void reporteIngresosBoletos(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        if (!(usuario instanceof AdminSucursal)) {
            response.sendRedirect("reporte?accion=menu");
            return;
        }

        try {
            Date desde = obtenerFechaOTodas(request, "desde", true);
            Date hasta = obtenerFechaOTodas(request, "hasta", false);

            ArrayList<Boleto> boletos = boletoPersistencia.listarTodos();
            //Nota: idealmente un metodo listarPorFechaYSucursal() en BoletoPersistencia,
            //filtrando tambien por sucursal a traves del viaje/ruta (mejora)

            request.setAttribute("boletos", boletos);
            request.getRequestDispatcher("/vistas/reporte/ingresosBoletos.jsp").forward(request, response);

        } catch (ParseException e) {
            request.setAttribute("error", "Formato de fecha inválido");
            request.getRequestDispatcher("/vistas/reporte/menuReportes.jsp").forward(request, response);
        }
    }

    //Reporte de ingresos por alquiler de buses en un intervalo de tiempo.
    private void reporteIngresosAlquiler(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        if (!(usuario instanceof AdminSucursal)) {
            response.sendRedirect("reporte?accion=menu");
            return;
        }

        ArrayList<ViajePrivado> alquileres = viajePrivadoPersistencia.listarTodos();
        request.setAttribute("alquileres", alquileres);
        request.getRequestDispatcher("/vistas/reporte/ingresosAlquiler.jsp").forward(request, response);
    }
    
    //Reporte de depreciación por bus.
    private void reporteDepreciacionPorBus(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        if (!(usuario instanceof AdminSucursal)) {
            response.sendRedirect("reporte?accion=menu");
            return;
        }
        AdminSucursal admin = (AdminSucursal) usuario;

        ArrayList<Bus> buses = busPersistencia.listarPorSucursal(admin.getSucursal().getIdSucursal());
        request.setAttribute("buses", buses);
        request.getRequestDispatcher("/vistas/reporte/depreciacionPorBus.jsp").forward(request, response);
    }

    private Date obtenerFechaOTodas(HttpServletRequest request, String parametro, boolean esInicio) throws ParseException {
        String fechaStr = request.getParameter(parametro);
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            //Si no se especifica, se toman en cuenta todos los registros 
            return esInicio ? new Date(0) : new Date();
        }
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        return formato.parse(fechaStr);
    }
}
