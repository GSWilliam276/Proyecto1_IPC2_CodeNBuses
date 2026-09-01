/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.controlador;

import com.usac.buses.proyectocodenbuses.entidad.Chofer;
import com.usac.buses.proyectocodenbuses.entidad.Sucursal;
import com.usac.buses.proyectocodenbuses.entidad.TipoLicencia;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionFormatoInvalido;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionLicenciaNoValidaParaBus;
import com.usac.buses.proyectocodenbuses.persistencia.ChoferPersistencia;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.usac.buses.proyectocodenbuses.entidad.AdminSucursal;
import com.usac.buses.proyectocodenbuses.entidad.Usuario;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ControladorChofer", urlPatterns = {"/chofer"})
public class ControladorChofer extends HttpServlet {

    private ChoferPersistencia choferPersistencia = new ChoferPersistencia();
    
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
                listarChoferes(request, response);
                break;
            case "nuevo":
                request.getRequestDispatcher("/vistas/chofer/registrarChofer.jsp").forward(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            default:
                response.sendRedirect("chofer?accion=listar");
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
                registrarChofer(request, response);
                break;
            case "actualizar":
                actualizarChofer(request, response);
                break;
            case "desactivar":
                desactivarChofer(request, response);
                break;
            default:
                response.sendRedirect("chofer?accion=listar");
        }
    }

    private void listarChoferes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<Chofer> choferes = choferPersistencia.listarTodos();
        request.setAttribute("choferes", choferes);
        request.getRequestDispatcher("/vistas/chofer/listarChoferes.jsp").forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        choferPersistencia.buscarPorId(id).ifPresentOrElse(
            chofer -> {
                request.setAttribute("chofer", chofer);
                try {
                    request.getRequestDispatcher("/vistas/chofer/editarChofer.jsp").forward(request, response);
                } catch (ServletException | IOException e) {
                    System.err.println("Error al mostrar formulario de edición: " + e.getMessage());
                }
            },
            () -> {
                try {
                    response.sendRedirect("chofer?accion=listar");
                } catch (IOException e) {
                    System.err.println("Error al redirigir: " + e.getMessage());
                }
            }
        );
    }

    private void registrarChofer(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String nit = request.getParameter("nit");
            String dpi = request.getParameter("dpi");
            String telefono = request.getParameter("telefono");
            String direccion = request.getParameter("direccion");
            String correo = request.getParameter("correo");
            String contrasena = request.getParameter("contrasena");
            String numeroLicencia = request.getParameter("numeroLicencia");
            String tipoLicenciaStr = request.getParameter("tipoLicencia");
            String fechaVencimientoStr = request.getParameter("fechaVencimiento");
            double salarioBase = Double.parseDouble(request.getParameter("salarioBase"));
            int idSucursal = Integer.parseInt(request.getParameter("idSucursal"));

            TipoLicencia tipoLicencia = TipoLicencia.valueOf(tipoLicenciaStr);

            //Validacion de regla de negocio: solo licencias A o B pueden conducir bus extraurbano
            if (tipoLicencia != TipoLicencia.A && tipoLicencia != TipoLicencia.B) {
                ExcepcionLicenciaNoValidaParaBus excepcion = new ExcepcionLicenciaNoValidaParaBus(
                    "La licencia debe ser tipo A o B para conducir transporte extraurbano de pasajeros");
                request.setAttribute("error", excepcion.getMessage());
                request.getRequestDispatcher("/vistas/chofer/registrarChofer.jsp").forward(request, response);
                return;
            }

            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaVencimiento = formato.parse(fechaVencimientoStr);

            Sucursal sucursal = new Sucursal();
            sucursal.setIdSucursal(idSucursal);

            Chofer chofer = new Chofer(nit, dpi, telefono, direccion, correo, contrasena,
                    numeroLicencia, tipoLicencia, fechaVencimiento, salarioBase, sucursal);

            choferPersistencia.insertar(chofer);
            response.sendRedirect("chofer?accion=listar");

        } catch (NumberFormatException e) {
            ExcepcionFormatoInvalido excepcion = new ExcepcionFormatoInvalido("salarioBase", "El salario debe ser un valor numérico válido");
            request.setAttribute("error", excepcion.getMessage() + " (Campo: " + excepcion.getCampo() + ")");
            request.getRequestDispatcher("/vistas/chofer/registrarChofer.jsp").forward(request, response);
        } catch (java.text.ParseException e) {
            ExcepcionFormatoInvalido excepcion = new ExcepcionFormatoInvalido("fechaVencimiento", "La fecha ingresada no es válida");
            request.setAttribute("error", excepcion.getMessage() + " (Campo: " + excepcion.getCampo() + ")");
            request.getRequestDispatcher("/vistas/chofer/registrarChofer.jsp").forward(request, response);
        }
    }

    private void actualizarChofer(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            String numeroLicencia = request.getParameter("numeroLicencia");
            TipoLicencia tipoLicencia = TipoLicencia.valueOf(request.getParameter("tipoLicencia"));
            String fechaVencimientoStr = request.getParameter("fechaVencimiento");
            double salarioBase = Double.parseDouble(request.getParameter("salarioBase"));
            int idSucursal = Integer.parseInt(request.getParameter("idSucursal"));

            if (tipoLicencia != TipoLicencia.A && tipoLicencia != TipoLicencia.B) {
                ExcepcionLicenciaNoValidaParaBus excepcion = new ExcepcionLicenciaNoValidaParaBus(
                    "La licencia debe ser tipo A o B para conducir transporte extraurbano de pasajeros");
                request.setAttribute("error", excepcion.getMessage());
                request.getRequestDispatcher("/vistas/chofer/editarChofer.jsp").forward(request, response);
                return;
            }

            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaVencimiento = formato.parse(fechaVencimientoStr);

            Sucursal sucursal = new Sucursal();
            sucursal.setIdSucursal(idSucursal);

            Chofer chofer = new Chofer(null, null, null, null, null, null,
                    numeroLicencia, tipoLicencia, fechaVencimiento, salarioBase, sucursal);
            chofer.setIdUsuario(idUsuario);

            choferPersistencia.actualizar(chofer);
            response.sendRedirect("chofer?accion=listar");

        } catch (NumberFormatException e) {
            ExcepcionFormatoInvalido excepcion = new ExcepcionFormatoInvalido("salarioBase", "El salario debe ser un valor numérico válido");
            request.setAttribute("error", excepcion.getMessage() + " (Campo: " + excepcion.getCampo() + ")");
            request.getRequestDispatcher("/vistas/chofer/editarChofer.jsp").forward(request, response);
        } catch (java.text.ParseException e) {
            ExcepcionFormatoInvalido excepcion = new ExcepcionFormatoInvalido("fechaVencimiento", "La fecha ingresada no es válida");
            request.setAttribute("error", excepcion.getMessage() + " (Campo: " + excepcion.getCampo() + ")");
            request.getRequestDispatcher("/vistas/chofer/editarChofer.jsp").forward(request, response);
        }
    }

    private void desactivarChofer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        choferPersistencia.eliminar(id);
        response.sendRedirect("chofer?accion=listar");
    }
}
