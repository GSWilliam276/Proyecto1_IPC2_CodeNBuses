/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.controlador;

import com.usac.buses.proyectocodenbuses.entidad.*;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionFormatoInvalido;
import com.usac.buses.proyectocodenbuses.persistencia.ViajePrivadoPersistencia;
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

@WebServlet(name = "ControladorAlquiler", urlPatterns = {"/alquiler"})
public class ControladorAlquiler extends HttpServlet {

    private ViajePrivadoPersistencia viajePrivadoPersistencia = new ViajePrivadoPersistencia();

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
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                listarAlquileres(request, response, usuario);
                break;
            case "solicitar":
                request.getRequestDispatcher("/vistas/alquiler/solicitarAlquiler.jsp").forward(request, response);
                break;
            case "confirmar":
                mostrarFormularioConfirmar(request, response, usuario);
                break;
            default:
                response.sendRedirect("alquiler?accion=listar");
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

        switch (accion) {
            case "solicitar":
                solicitarAlquiler(request, response, usuario);
                break;
            case "confirmarPrecio":
                confirmarPrecio(request, response, usuario);
                break;
            case "asignarBusChofer":
                asignarBusYChofer(request, response, usuario);
                break;
            default:
                response.sendRedirect("alquiler?accion=listar");
        }
    }

    private void listarAlquileres(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        ArrayList<ViajePrivado> alquileres;
        if (usuario instanceof AdminSucursal) {
            alquileres = viajePrivadoPersistencia.listarTodos();
        } else {
            alquileres = viajePrivadoPersistencia.listarPorSolicitante(usuario.getIdUsuario());
        }
        request.setAttribute("alquileres", alquileres);
        request.getRequestDispatcher("/vistas/alquiler/listarAlquileres.jsp").forward(request, response);
    }

    private void mostrarFormularioConfirmar(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        //Solo el AdminSucursal puede confirmar/cambiar precio
        if (!(usuario instanceof AdminSucursal)) {
            response.sendRedirect("alquiler?accion=listar");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        viajePrivadoPersistencia.buscarPorId(id).ifPresentOrElse(
            viaje -> {
                request.setAttribute("viaje", viaje);
                try {
                    request.getRequestDispatcher("/vistas/alquiler/confirmarPrecio.jsp").forward(request, response);
                } catch (ServletException | IOException e) {
                    System.err.println("Error al mostrar formulario: " + e.getMessage());
                }
            },
            () -> {
                try {
                    response.sendRedirect("alquiler?accion=listar");
                } catch (IOException e) {
                    System.err.println("Error al redirigir: " + e.getMessage());
                }
            }
        );
    }

    //El cliente solicita el alquiler indicando origen, destino, fecha y pasajeros
    //El sistema calcula un precio estimado automaticamente.
    private void solicitarAlquiler(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException, ServletException {
        //Cualquier tipo de usuario puede solicitar un alquiler, 
        //ya que todos los usuarios pueden ser clientes
        //de cualquier servicio, sin importar su rol principal
        try {
            String origen = request.getParameter("origen");
            String destino = request.getParameter("destino");
            int pasajeros = Integer.parseInt(request.getParameter("pasajeros"));
            String fechaSalidaStr = request.getParameter("fechaHoraSalida");
            String fechaLlegadaStr = request.getParameter("fechaHoraLlegadaEstimada");

            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date fechaSalida = formato.parse(fechaSalidaStr);
            Date fechaLlegada = formato.parse(fechaLlegadaStr);

            //Validacion de sentido comun: no se puede solicitar con fecha pasada
            if (fechaSalida.before(new Date())) {
                mostrarError(request, response, "fechaHoraSalida", "La fecha de salida no puede ser en el pasado", "/vistas/alquiler/solicitarAlquiler.jsp");
                return;
            }
            //Validacion de sentido comun: la llegada debe ser posterior a la salida
            if (!fechaLlegada.after(fechaSalida)) {
                mostrarError(request, response, "fechaHoraLlegadaEstimada", "La fecha de llegada debe ser posterior a la fecha de salida", "/vistas/alquiler/solicitarAlquiler.jsp");
                return;
            }

            double precioEstimado = calcularPrecioEstimado(pasajeros, fechaSalida, fechaLlegada);

            ViajePrivado viaje = new ViajePrivado(null, null, fechaSalida, fechaLlegada,
                    origen, destino, pasajeros, precioEstimado);

            //Se guarda quien solicito este alquiler, para poder filtrar
            //despues "mis alquileres" sin mostrar los de otros usuarios
            viaje.setSolicitante(usuario);

            viajePrivadoPersistencia.insertar(viaje);

            response.sendRedirect("alquiler?accion=listar");

        } catch (NumberFormatException e) {
            mostrarError(request, response, "pasajeros", "Debe ingresar un número válido de pasajeros", "/vistas/alquiler/solicitarAlquiler.jsp");
        } catch (ParseException e) {
            mostrarError(request, response, "fecha", "Formato de fecha inválido", "/vistas/alquiler/solicitarAlquiler.jsp");
        }
    }

    private double calcularPrecioEstimado(int pasajeros, Date fechaSalida, Date fechaLlegada) {
        double precioBasePorPasajero = 50.0;
        long duracionHoras = (fechaLlegada.getTime() - fechaSalida.getTime()) / (1000 * 60 * 60);
        double precioPorHora = 30.0;

        return (pasajeros * precioBasePorPasajero) + (duracionHoras * precioPorHora);
    }
    
    //El AdminSucursal confirma o cambia el precio estimado
    private void confirmarPrecio(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException, ServletException {
        if (!(usuario instanceof AdminSucursal)) {
            response.sendRedirect("alquiler?accion=listar");
            return;
        }

        try {
            int idViaje = Integer.parseInt(request.getParameter("idViaje"));
            double precioConfirmado = Double.parseDouble(request.getParameter("precioConfirmado"));

            viajePrivadoPersistencia.confirmarPrecio(idViaje, precioConfirmado);
            response.sendRedirect("alquiler?accion=listar");

        } catch (NumberFormatException e) {
            mostrarError(request, response, "precioConfirmado", "Debe ingresar un valor numérico válido", "/vistas/alquiler/confirmarPrecio.jsp");
        }
    }

    
    //El AdminSucursal asigna bus y chofer una vez el cliente ya pago el precio confirmado
    private void asignarBusYChofer(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException, ServletException {
        if (!(usuario instanceof AdminSucursal)) {
            response.sendRedirect("alquiler?accion=listar");
            return;
        }

        try {
            int idViaje = Integer.parseInt(request.getParameter("idViaje"));
            int idBus = Integer.parseInt(request.getParameter("idBus"));
            int idChofer = Integer.parseInt(request.getParameter("idChofer"));

            viajePrivadoPersistencia.buscarPorId(idViaje).ifPresent(viaje -> {
                Bus bus = new Bus();
                bus.setIdBus(idBus);
                Chofer chofer = new Chofer();
                chofer.setIdUsuario(idChofer);

                viaje.setBus(bus);
                viaje.setChofer(chofer);
                viajePrivadoPersistencia.actualizar(viaje);
            });

            response.sendRedirect("alquiler?accion=listar");

        } catch (NumberFormatException e) {
            mostrarError(request, response, "idBus/idChofer", "Debe seleccionar valores válidos", "/vistas/alquiler/listarAlquileres.jsp");
        }
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response,
                               String campo, String mensaje, String vista) throws IOException, ServletException {
        ExcepcionFormatoInvalido excepcion = new ExcepcionFormatoInvalido(campo, mensaje);
        request.setAttribute("error", excepcion.getMessage() + " (Campo: " + excepcion.getCampo() + ")");
        request.getRequestDispatcher(vista).forward(request, response);
    }
}
