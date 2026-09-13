/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.controlador;

import com.usac.buses.proyectocodenbuses.entidad.Ruta;
import com.usac.buses.proyectocodenbuses.entidad.Sucursal;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionFormatoInvalido;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionRutaNoEliminable;
import com.usac.buses.proyectocodenbuses.persistencia.RutaPersistencia;
import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.usac.buses.proyectocodenbuses.entidad.AdminSucursal;
import com.usac.buses.proyectocodenbuses.entidad.Usuario;
import jakarta.servlet.http.HttpSession;
import com.usac.buses.proyectocodenbuses.persistencia.SucursalPersistencia;

@WebServlet(name = "ControladorRuta", urlPatterns = {"/ruta"})
public class ControladorRuta extends HttpServlet {

    private RutaPersistencia rutaPersistencia = new RutaPersistencia();
    
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
                listarRutas(request, response);
                break;
            case "nuevo":
                request.setAttribute("sucursales", new SucursalPersistencia().listarTodos());
                request.getRequestDispatcher("/vistas/ruta/registrarRuta.jsp").forward(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            default:
                response.sendRedirect("ruta?accion=listar");
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
                registrarRuta(request, response);
                break;
            case "actualizar":
                actualizarRuta(request, response);
                break;
            case "eliminar":
                eliminarRuta(request, response);
                break;
            default:
                response.sendRedirect("ruta?accion=listar");
        }
    }

    private void listarRutas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<Ruta> rutas = rutaPersistencia.listarTodos();
        request.setAttribute("rutas", rutas);
        request.getRequestDispatcher("/vistas/ruta/listarRutas.jsp").forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("ruta?accion=listar");
            return;
        }
        int id = Integer.parseInt(idParam);
        rutaPersistencia.buscarPorId(id).ifPresentOrElse(
            ruta -> {
                request.setAttribute("ruta", ruta);
                //Se pasa tambien la lista de sucursales, necesaria para
                //armar los combobox de origen y destino en el JSP de edicion
                request.setAttribute("sucursales", new SucursalPersistencia().listarTodos());
                try {
                    request.getRequestDispatcher("/vistas/ruta/editarRuta.jsp").forward(request, response);
                } catch (ServletException | IOException e) {
                    System.err.println("Error al mostrar formulario de edición: " + e.getMessage());
                }
            },
            () -> {
                try {
                    response.sendRedirect("ruta?accion=listar");
                } catch (IOException e) {
                    System.err.println("Error al redirigir: " + e.getMessage());
                }
            }
        );
    }

    private void registrarRuta(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int idSucursalOrigen = Integer.parseInt(request.getParameter("idSucursalOrigen"));
            int idSucursalDestino = Integer.parseInt(request.getParameter("idSucursalDestino"));
            double distanciaKm = Double.parseDouble(request.getParameter("distanciaKm"));
            double precioBoleto = Double.parseDouble(request.getParameter("precioBoleto"));

            //Validacion de que una ruta no puede tener el mismo origen y destino
            if (idSucursalOrigen == idSucursalDestino) {
                ExcepcionFormatoInvalido excepcion = new ExcepcionFormatoInvalido(
                    "idSucursalDestino", "La sucursal de destino debe ser distinta a la de origen");
                request.setAttribute("error", excepcion.getMessage() + " (Campo: " + excepcion.getCampo() + ")");
                request.setAttribute("sucursales", new SucursalPersistencia().listarTodos());
                request.getRequestDispatcher("/vistas/ruta/registrarRuta.jsp").forward(request, response);
                return;
            }

            Sucursal origen = new Sucursal();
            origen.setIdSucursal(idSucursalOrigen);
            Sucursal destino = new Sucursal();
            destino.setIdSucursal(idSucursalDestino);

            Ruta ruta = new Ruta(origen, destino, distanciaKm, precioBoleto);
            rutaPersistencia.insertar(ruta);

            response.sendRedirect("ruta?accion=listar");

        } catch (NumberFormatException e) {
            ExcepcionFormatoInvalido excepcion = new ExcepcionFormatoInvalido("distanciaKm/precioBoleto", "Debe ingresar valores numéricos válidos");
            request.setAttribute("error", excepcion.getMessage() + " (Campo: " + excepcion.getCampo() + ")");
            request.getRequestDispatcher("/vistas/ruta/registrarRuta.jsp").forward(request, response);
        }
    }

    private void actualizarRuta(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int idRuta = Integer.parseInt(request.getParameter("idRuta"));
            int idSucursalOrigen = Integer.parseInt(request.getParameter("idSucursalOrigen"));
            int idSucursalDestino = Integer.parseInt(request.getParameter("idSucursalDestino"));
            double distanciaKm = Double.parseDouble(request.getParameter("distanciaKm"));
            double precioBoleto = Double.parseDouble(request.getParameter("precioBoleto"));

            //Misma validacion que en registrarRuta()
            if (idSucursalOrigen == idSucursalDestino) {
                ExcepcionFormatoInvalido excepcion = new ExcepcionFormatoInvalido(
                    "idSucursalDestino", "La sucursal de destino debe ser distinta a la de origen");
                request.setAttribute("error", excepcion.getMessage() + " (Campo: " + excepcion.getCampo() + ")");
                //Se vuelve a cargar la ruta y las sucursales para no dejar
                //el formulario de edicion vacio al mostrar el error
                rutaPersistencia.buscarPorId(idRuta).ifPresent(r -> request.setAttribute("ruta", r));
                request.setAttribute("sucursales", new SucursalPersistencia().listarTodos());
                request.getRequestDispatcher("/vistas/ruta/editarRuta.jsp").forward(request, response);
                return;
            }

            Sucursal origen = new Sucursal();
            origen.setIdSucursal(idSucursalOrigen);
            Sucursal destino = new Sucursal();
            destino.setIdSucursal(idSucursalDestino);
            Ruta ruta = new Ruta(origen, destino, distanciaKm, precioBoleto);
            ruta.setIdRuta(idRuta);
            rutaPersistencia.actualizar(ruta);
            response.sendRedirect("ruta?accion=listar");

        } catch (NumberFormatException e) {
            ExcepcionFormatoInvalido excepcion = new ExcepcionFormatoInvalido("distanciaKm/precioBoleto", "Debe ingresar valores numéricos válidos");
            request.setAttribute("error", excepcion.getMessage() + " (Campo: " + excepcion.getCampo() + ")");
            request.getRequestDispatcher("/vistas/ruta/editarRuta.jsp").forward(request, response);
        }
    }

    private void eliminarRuta(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));

            if (rutaPersistencia.tieneViajesAsociados(id)) {
                throw new ExcepcionRutaNoEliminable("No se puede eliminar la ruta porque tiene viajes asociados");
            }

            rutaPersistencia.eliminar(id);
            response.sendRedirect("ruta?accion=listar");

        } catch (ExcepcionRutaNoEliminable e) {
            request.setAttribute("error", e.getMessage());
            listarRutas(request, response);
        }
    }
}
