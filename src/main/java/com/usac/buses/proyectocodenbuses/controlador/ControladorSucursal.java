/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.controlador;

import com.usac.buses.proyectocodenbuses.entidad.Sucursal;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionFormatoInvalido;
import com.usac.buses.proyectocodenbuses.persistencia.SucursalPersistencia;
import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.usac.buses.proyectocodenbuses.entidad.AdminSistema;
import com.usac.buses.proyectocodenbuses.entidad.Usuario;
import jakarta.servlet.http.HttpSession;
import com.usac.buses.proyectocodenbuses.persistencia.AdminSucursalPersistencia;
import java.util.Optional;

@WebServlet(name = "ControladorSucursal", urlPatterns = {"/sucursal"})
public class ControladorSucursal extends HttpServlet {

    private SucursalPersistencia sucursalPersistencia = new SucursalPersistencia();
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
                listarSucursales(request, response);
                break;
            case "nuevo":
                request.getRequestDispatcher("/vistas/sucursal/registrarSucursal.jsp").forward(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            default:
                response.sendRedirect("sucursal?accion=listar");
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
                registrarSucursal(request, response);
                break;
            case "actualizar":
                actualizarSucursal(request, response);
                break;
            default:
                response.sendRedirect("sucursal?accion=listar");
        }
    }

    private void listarSucursales(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<Sucursal> sucursales = sucursalPersistencia.listarTodos();

        //Se verifica, por cada sucursal, si tiene al menos un
        //AdminSucursal asignado, tal como pide el enunciado
        ArrayList<Object[]> filasReporte = new ArrayList<>();
        for (Sucursal sucursal : sucursales) {
            boolean tieneAdmin = !adminSucursalPersistencia.listarPorSucursal(sucursal.getIdSucursal()).isEmpty();
            filasReporte.add(new Object[]{sucursal, tieneAdmin});
        }

        request.setAttribute("filasReporte", filasReporte);
        request.getRequestDispatcher("/vistas/sucursal/listarSucursales.jsp").forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("sucursal?accion=listar");
            return;
        }
        int id = Integer.parseInt(idParam);
        Optional<Sucursal> resultado = sucursalPersistencia.buscarPorId(id);

        if (resultado.isPresent()) {
            Sucursal sucursal = resultado.get();
            request.setAttribute("sucursal", sucursal);
            request.getRequestDispatcher("/vistas/sucursal/editarSucursal.jsp").forward(request, response);
        } else {
            response.sendRedirect("sucursal?accion=listar");
        }
    }

    private void registrarSucursal(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String nombre = request.getParameter("nombre");
            String ubicacion = request.getParameter("ubicacion");
            double latitud = Double.parseDouble(request.getParameter("latitud"));
            double longitud = Double.parseDouble(request.getParameter("longitud"));

            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ExcepcionFormatoInvalido("nombre", "El nombre de la sucursal es obligatorio");
            }
            if (ubicacion == null || ubicacion.trim().isEmpty()) {
                throw new ExcepcionFormatoInvalido("ubicacion", "La ubicación de la sucursal es obligatoria");
            }
            //Nueva validacion: no permitir nombres duplicados
            if (sucursalPersistencia.existeSucursalConNombre(nombre)) {
                throw new ExcepcionFormatoInvalido("nombre", "Ya existe una sucursal registrada con ese nombre");
            }

            Sucursal sucursal = new Sucursal(nombre, ubicacion, latitud, longitud);
            sucursalPersistencia.insertar(sucursal);

            response.sendRedirect("sucursal?accion=listar");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Latitud y longitud deben ser valores numéricos válidos");
            request.getRequestDispatcher("/vistas/sucursal/registrarSucursal.jsp").forward(request, response);
        } catch (ExcepcionFormatoInvalido e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/vistas/sucursal/registrarSucursal.jsp").forward(request, response);
        }
    }

    private void actualizarSucursal(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int idSucursal = Integer.parseInt(request.getParameter("idSucursal"));
            String nombre = request.getParameter("nombre");
            String ubicacion = request.getParameter("ubicacion");
            double latitud = Double.parseDouble(request.getParameter("latitud"));
            double longitud = Double.parseDouble(request.getParameter("longitud"));

            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ExcepcionFormatoInvalido("nombre", "El nombre de la sucursal es obligatorio");
            }

            Sucursal sucursal = new Sucursal(nombre, ubicacion, latitud, longitud);
            sucursal.setIdSucursal(idSucursal);
            sucursalPersistencia.actualizar(sucursal);

            response.sendRedirect("sucursal?accion=listar");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Latitud y longitud deben ser valores numéricos válidos");
            request.getRequestDispatcher("/vistas/sucursal/editarSucursal.jsp").forward(request, response);
        } catch (ExcepcionFormatoInvalido e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/vistas/sucursal/editarSucursal.jsp").forward(request, response);
        }
    }
}
