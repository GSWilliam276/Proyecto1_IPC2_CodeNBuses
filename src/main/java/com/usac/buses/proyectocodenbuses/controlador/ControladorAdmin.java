/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.controlador;

import com.usac.buses.proyectocodenbuses.entidad.AdminSistema;
import com.usac.buses.proyectocodenbuses.entidad.AdminSucursal;
import com.usac.buses.proyectocodenbuses.entidad.Sucursal;
import com.usac.buses.proyectocodenbuses.entidad.Usuario;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionFormatoInvalido;
import com.usac.buses.proyectocodenbuses.persistencia.AdminSucursalPersistencia;
import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.usac.buses.proyectocodenbuses.persistencia.SucursalPersistencia;
        
@WebServlet(name = "ControladorAdmin", urlPatterns = {"/admin"})
public class ControladorAdmin extends HttpServlet {

    private AdminSucursalPersistencia adminSucursalPersistencia = new AdminSucursalPersistencia();

    private boolean verificarAcceso(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("usuario?accion=login");
            return false;
        }
        if (!(usuario instanceof AdminSistema)) {
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
                listarAdminsSucursal(request, response);
                break;
            case "nuevo":
                request.setAttribute("sucursales", new SucursalPersistencia().listarTodos());
                request.getRequestDispatcher("/vistas/admin/registrarAdminSucursal.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("admin?accion=listar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!verificarAcceso(request, response)) {
            return;
        }

        String accion = request.getParameter("accion");

        switch (accion) {
            case "registrar":
                registrarAdminSucursal(request, response);
                break;
            default:
                response.sendRedirect("admin?accion=listar");
        }
    }

    private void listarAdminsSucursal(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<AdminSucursal> admins = adminSucursalPersistencia.listarTodos();
        request.setAttribute("admins", admins);
        request.getRequestDispatcher("/vistas/admin/listarAdminsSucursal.jsp").forward(request, response);
    }

    private void registrarAdminSucursal(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String nit = request.getParameter("nit");
            String dpi = request.getParameter("dpi");
            String telefono = request.getParameter("telefono");
            String direccion = request.getParameter("direccion");
            String correo = request.getParameter("correo");
            String contrasena = request.getParameter("contrasena");
            int idSucursal = Integer.parseInt(request.getParameter("idSucursal"));

            if (dpi == null || dpi.trim().isEmpty()) {
                throw new ExcepcionFormatoInvalido("dpi", "El DPI es obligatorio");
            }

            Sucursal sucursal = new Sucursal();
            sucursal.setIdSucursal(idSucursal);

            AdminSucursal admin = new AdminSucursal(nit, dpi, telefono, direccion, correo, contrasena, sucursal);
            adminSucursalPersistencia.insertar(admin);

            response.sendRedirect("admin?accion=listar");

        } catch (NumberFormatException e) {
            ExcepcionFormatoInvalido excepcion = new ExcepcionFormatoInvalido("idSucursal", "Debe seleccionar una sucursal válida");
            request.setAttribute("error", excepcion.getMessage() + " (Campo: " + excepcion.getCampo() + ")");
            request.getRequestDispatcher("/vistas/admin/registrarAdminSucursal.jsp").forward(request, response);
        } catch (ExcepcionFormatoInvalido e) {
            request.setAttribute("error", e.getMessage() + " (Campo: " + e.getCampo() + ")");
            request.getRequestDispatcher("/vistas/admin/registrarAdminSucursal.jsp").forward(request, response);
        }
    }
}
