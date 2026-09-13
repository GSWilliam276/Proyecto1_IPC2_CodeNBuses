/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.controlador;

import com.usac.buses.proyectocodenbuses.entidad.AdminSucursal;
import com.usac.buses.proyectocodenbuses.entidad.Bus;
import com.usac.buses.proyectocodenbuses.entidad.Sucursal;
import com.usac.buses.proyectocodenbuses.entidad.Usuario;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionFormatoInvalido;
import com.usac.buses.proyectocodenbuses.persistencia.BusPersistencia;
import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.usac.buses.proyectocodenbuses.persistencia.SucursalPersistencia;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;

@WebServlet(name = "ControladorBus", urlPatterns = {"/bus"})
@MultipartConfig(maxFileSize = 5242880) //limite de 5MB por archivo
public class ControladorBus extends HttpServlet {

    private BusPersistencia busPersistencia = new BusPersistencia();

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
                listarBuses(request, response);
                break;
            case "nuevo":
                request.setAttribute("sucursales", new SucursalPersistencia().listarTodos());
                request.getRequestDispatcher("/vistas/bus/registrarBus.jsp").forward(request, response);
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

        if (!verificarAcceso(request, response)) {
            return;
        }

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
        String idParam = request.getParameter("id");

        //Si no viene el id en la URL, se redirige al listado en vez
        //de intentar parsear null y romper con NumberFormatException
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("bus?accion=listar");
            return;
        }

        int id = Integer.parseInt(idParam);
        busPersistencia.buscarPorId(id).ifPresentOrElse(
            bus -> {
                request.setAttribute("bus", bus);
                request.setAttribute("sucursales", new SucursalPersistencia().listarTodos());
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

            //Validacion de placa duplicada
            if (busPersistencia.existeBusConPlaca(placa)) {
                ExcepcionFormatoInvalido excepcion = new ExcepcionFormatoInvalido("placa", "Ya existe un bus registrado con esa placa");
                request.setAttribute("error", excepcion.getMessage() + " (Campo: " + excepcion.getCampo() + ")");
                request.setAttribute("sucursales", new SucursalPersistencia().listarTodos());
                request.getRequestDispatcher("/vistas/bus/registrarBus.jsp").forward(request, response);
                return;
            }

            int anio;
            int capacidad;

            try {
                anio = Integer.parseInt(request.getParameter("anio"));
                capacidad = Integer.parseInt(request.getParameter("capacidad"));
            } catch (NumberFormatException e) {
                throw new ExcepcionFormatoInvalido("anio/capacidad", "Debe ingresar valores numéricos válidos");
            }

            //Se procesa el archivo de imagen subido, si el usuario selecciono uno
            String foto = guardarFoto(request);
            if ("FORMATO_INVALIDO".equals(foto)) {
                ExcepcionFormatoInvalido excepcion = new ExcepcionFormatoInvalido("foto", "El archivo debe ser una imagen (jpg, jpeg, png o gif)");
                request.setAttribute("error", excepcion.getMessage() + " (Campo: " + excepcion.getCampo() + ")");
                request.setAttribute("sucursales", new SucursalPersistencia().listarTodos());
                request.getRequestDispatcher("/vistas/bus/registrarBus.jsp").forward(request, response);
                return;
            }

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
    
    //Lee el archivo de imagen del formulario multipart y lo guarda en el
    //servidor, dentro de la carpeta recursos/imagenes. Devuelve el nombre
    //generado para guardarlo en la base de datos, o null si no se subio nada.
    private String guardarFoto(HttpServletRequest request) throws IOException, ServletException {
        Part part = request.getPart("foto");
        if (part == null || part.getSize() == 0) {
            return null; // el usuario no selecciono ninguna imagen
        }

        String nombreOriginal = part.getSubmittedFileName();
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf(".")).toLowerCase();

        //Validacion real del lado del servidor: solo se aceptan imagenes,
        //sin depender solo del atributo accept del input (que es solo
        //una sugerencia visual y se puede saltar facilmente)
        if (!extension.equals(".jpg") && !extension.equals(".jpeg")
                && !extension.equals(".png") && !extension.equals(".gif")) {
            return "FORMATO_INVALIDO"; //Señal especial para que el Controlador la detecte
        }

        String nombreUnico = java.util.UUID.randomUUID().toString() + extension;

        String rutaCarpeta = request.getServletContext().getRealPath("/recursos/imagenes");
        java.io.File carpeta = new java.io.File(rutaCarpeta);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        part.write(rutaCarpeta + java.io.File.separator + nombreUnico);
        return nombreUnico;
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
