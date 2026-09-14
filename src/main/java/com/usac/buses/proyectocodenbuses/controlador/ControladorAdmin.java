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
import com.usac.buses.proyectocodenbuses.persistencia.UsuarioPersistencia;
import com.usac.buses.proyectocodenbuses.persistencia.ConfiguracionPersistencia;
        
@WebServlet(name = "ControladorAdmin", urlPatterns = {"/admin"})
public class ControladorAdmin extends HttpServlet {

    private AdminSucursalPersistencia adminSucursalPersistencia = new AdminSucursalPersistencia();
    private UsuarioPersistencia usuarioPersistencia = new UsuarioPersistencia();
    private ConfiguracionPersistencia configuracionPersistencia = new ConfiguracionPersistencia();
    
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
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            case "configurarDepreciacion":
                mostrarConfiguracionDepreciacion(request, response);
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
            case "actualizar":
                actualizarAdminSucursal(request, response);
                break;
            case "guardarDepreciacion":
                guardarConfiguracionDepreciacion(request, response);
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
            String nombre = request.getParameter("nombre");
            String nit = request.getParameter("nit");
            String dpi = request.getParameter("dpi");
            String telefono = request.getParameter("telefono");
            String direccion = request.getParameter("direccion");
            String correo = request.getParameter("correo");
            String contrasena = request.getParameter("contrasena");
            int idSucursal = Integer.parseInt(request.getParameter("idSucursal"));

            //Validacion de nombre: obligatorio
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ExcepcionFormatoInvalido("nombre", "El nombre es obligatorio");
            }
            if (dpi == null || dpi.trim().isEmpty()) {
                throw new ExcepcionFormatoInvalido("dpi", "El DPI es obligatorio");
            }
            if (!dpi.matches("\\d{13}")) {
                throw new ExcepcionFormatoInvalido("dpi", "El DPI debe contener exactamente 13 números");
            }
            if (usuarioPersistencia.existeUsuarioConDpi(dpi)) {
                throw new ExcepcionFormatoInvalido("dpi", "Ya existe una cuenta registrada con ese DPI");
            }
            if (!nit.trim().isEmpty() && !nit.matches("\\d+")) {
                throw new ExcepcionFormatoInvalido("nit", "El NIT debe contener solo números");
            }

            //Validacion de correo duplicado
            if (usuarioPersistencia.existeUsuarioConCorreo(correo)) {
                throw new ExcepcionFormatoInvalido("correo", "Ya existe una cuenta registrada con ese correo");
            }

            Sucursal sucursal = new Sucursal();
            sucursal.setIdSucursal(idSucursal);

            AdminSucursal admin = new AdminSucursal(nombre, nit, dpi, telefono, direccion, correo, contrasena, sucursal);
            adminSucursalPersistencia.insertar(admin);

            response.sendRedirect("admin?accion=listar");

        } catch (NumberFormatException e) {
            ExcepcionFormatoInvalido excepcion = new ExcepcionFormatoInvalido("idSucursal", "Debe seleccionar una sucursal válida");
            request.setAttribute("error", excepcion.getMessage());
            request.getRequestDispatcher("/vistas/admin/registrarAdminSucursal.jsp").forward(request, response);
        } catch (ExcepcionFormatoInvalido e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/vistas/admin/registrarAdminSucursal.jsp").forward(request, response);
        }
    }

    private void actualizarAdminSucursal(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            String nombre = request.getParameter("nombre");
            String dpi = request.getParameter("dpi");
            String telefono = request.getParameter("telefono");
            String direccion = request.getParameter("direccion");
            String correo = request.getParameter("correo");
            int idSucursal = Integer.parseInt(request.getParameter("idSucursal"));

            //Validacion de nombre: obligatorio
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ExcepcionFormatoInvalido("nombre", "El nombre es obligatorio");
            }
            //Validacion de DPI: obligatorio y solo numeros
            if (dpi == null || dpi.trim().isEmpty()) {
                throw new ExcepcionFormatoInvalido("dpi", "El DPI es obligatorio");
            }
            if (!dpi.matches("\\d{13}")) {
                throw new ExcepcionFormatoInvalido("dpi", "El DPI debe contener exactamente 13 números");
            }

            //Se trae el admin actual primero, para no perder el NIT
            //que no viene en este formulario y evitar sobreescribirlo con null
            AdminSucursal adminActual = adminSucursalPersistencia.buscarPorId(idUsuario).orElse(null);
            if (adminActual == null) {
                response.sendRedirect("admin?accion=listar");
                return;
            }

            Sucursal sucursal = new Sucursal();
            sucursal.setIdSucursal(idSucursal);

            AdminSucursal admin = new AdminSucursal(nombre, adminActual.getNit(), dpi, telefono, direccion, correo, null, sucursal);
            admin.setIdUsuario(idUsuario);
            adminSucursalPersistencia.actualizar(admin);

            response.sendRedirect("admin?accion=listar");

        } catch (NumberFormatException e) {
            recargarFormularioConError(request, response, "Datos numéricos inválidos");
        } catch (ExcepcionFormatoInvalido e) {
            recargarFormularioConError(request, response, e.getMessage());
        }
    }

    private void recargarFormularioConError(HttpServletRequest request, HttpServletResponse response, String mensajeError)
            throws IOException, ServletException {
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        request.setAttribute("error", mensajeError);
        adminSucursalPersistencia.buscarPorId(idUsuario).ifPresent(admin -> request.setAttribute("admin", admin));
        request.setAttribute("sucursales", new SucursalPersistencia().listarTodos());
        request.getRequestDispatcher("/vistas/admin/editarAdminSucursal.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("admin?accion=listar");
            return;
        }
        int id = Integer.parseInt(idParam);
        adminSucursalPersistencia.buscarPorId(id).ifPresentOrElse(
            admin -> {
                request.setAttribute("admin", admin);
                request.setAttribute("sucursales", new SucursalPersistencia().listarTodos());
                try {
                    request.getRequestDispatcher("/vistas/admin/editarAdminSucursal.jsp").forward(request, response);
                } catch (ServletException | IOException e) {
                    System.err.println("Error al mostrar formulario de edición: " + e.getMessage());
                }
            },
            () -> {
                try {
                    response.sendRedirect("admin?accion=listar");
                } catch (IOException e) {
                    System.err.println("Error al redirigir: " + e.getMessage());
                }
            }
        );
    }
    
    private void mostrarConfiguracionDepreciacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        double montoActual = configuracionPersistencia.obtenerMontoDepreciacionActual();
        request.setAttribute("montoActual", montoActual);
        request.getRequestDispatcher("/vistas/admin/configurarDepreciacion.jsp").forward(request, response);
    }

    private void guardarConfiguracionDepreciacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            double monto = Double.parseDouble(request.getParameter("monto"));
            configuracionPersistencia.actualizarMontoDepreciacion(monto);
            response.sendRedirect("admin?accion=configurarDepreciacion");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Debe ingresar un valor numérico válido");
            request.getRequestDispatcher("/vistas/admin/configurarDepreciacion.jsp").forward(request, response);
        }
    }
}
