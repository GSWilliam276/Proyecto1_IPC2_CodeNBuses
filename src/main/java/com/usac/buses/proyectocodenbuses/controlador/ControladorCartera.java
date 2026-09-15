/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.controlador;

import com.usac.buses.proyectocodenbuses.entidad.*;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionFormatoInvalido;
import com.usac.buses.proyectocodenbuses.persistencia.CarteraPersistencia;
import com.usac.buses.proyectocodenbuses.persistencia.MovimientoCarteraPersistencia;
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
import java.util.Optional;

@WebServlet(name = "ControladorCartera", urlPatterns = {"/cartera"})
public class ControladorCartera extends HttpServlet {

    private CarteraPersistencia carteraPersistencia = new CarteraPersistencia();
    private MovimientoCarteraPersistencia movimientoCarteraPersistencia = new MovimientoCarteraPersistencia();

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
            accion = "ver";
        }

        switch (accion) {
            case "ver":
                verCartera(request, response, usuario);
                break;
            case "recargar":
                request.getRequestDispatcher("/vistas/cartera/recargar.jsp").forward(request, response);
                break;
            case "historial":
                verHistorial(request, response, usuario);
                break;
            default:
                response.sendRedirect("cartera?accion=ver");
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

        String accion = request.getParameter("accion");

        if ("recargar".equals(accion)) {
            recargarCartera(request, response, usuario);
        } else {
            response.sendRedirect("cartera?accion=ver");
        }
    }

    private void verCartera(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        Optional<Cartera> resultado = carteraPersistencia.buscarPorUsuario(usuario.getIdUsuario());

        if (resultado.isPresent()) {
            Cartera cartera = resultado.get();
            request.setAttribute("cartera", cartera);
            request.getRequestDispatcher("/vistas/cartera/verCartera.jsp").forward(request, response);
        } else {
            response.sendRedirect("usuario?accion=perfil");
        }
    }

    private void verHistorial(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        Optional<Cartera> resultado = carteraPersistencia.buscarPorUsuario(usuario.getIdUsuario());

        if (resultado.isPresent()) {
            Cartera cartera = resultado.get();
            ArrayList<MovimientoCartera> movimientos = movimientoCarteraPersistencia.listarPorCartera(cartera.getIdCartera());
            request.setAttribute("movimientos", movimientos);
            request.getRequestDispatcher("/vistas/cartera/historial.jsp").forward(request, response);
        } else {
            response.sendRedirect("usuario?accion=perfil");
        }
    }

    //El usuario recarga su cartera manualmente, indicando el monto y la fecha
    private void recargarCartera(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException, ServletException {
        try {
            double monto = Double.parseDouble(request.getParameter("monto"));
            String fechaStr = request.getParameter("fecha");

            if (monto <= 0) {
                throw new ExcepcionFormatoInvalido("monto", "El monto a recargar debe ser mayor a cero");
            }

            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = formato.parse(fechaStr);

            Cartera cartera = carteraPersistencia.buscarPorUsuario(usuario.getIdUsuario()).orElse(null);
            if (cartera == null) {
                response.sendRedirect("usuario?accion=perfil");
                return;
            }

            carteraPersistencia.recargar(cartera.getIdCartera(), monto);

            MovimientoCartera movimiento = new MovimientoCartera(cartera, TipoMovimiento.RECARGA, monto, fecha);
            movimientoCarteraPersistencia.insertar(movimiento);

            response.sendRedirect("cartera?accion=ver");

        } catch (NumberFormatException e) {
            mostrarError(request, response, "monto", "Debe ingresar un valor numérico válido", "/vistas/cartera/recargar.jsp");
        } catch (ParseException e) {
            mostrarError(request, response, "fecha", "Formato de fecha inválido", "/vistas/cartera/recargar.jsp");
        } catch (ExcepcionFormatoInvalido e) {
            mostrarError(request, response, e.getCampo(), e.getMessage(), "/vistas/cartera/recargar.jsp");
        }
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response,
                            String campo, String mensaje, String vista) throws IOException, ServletException {
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher(vista).forward(request, response);
    }
}
