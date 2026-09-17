/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.controlador;

import com.usac.buses.proyectocodenbuses.entidad.*;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionFormatoInvalido;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionViajeNoEliminable;
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
import java.util.Optional;

@WebServlet(name = "ControladorViaje", urlPatterns = {"/viaje"})
public class ControladorViaje extends HttpServlet {

    private ViajeRegularPersistencia viajeRegularPersistencia = new ViajeRegularPersistencia();
    private ViajePersistencia viajePersistencia = new ViajePersistencia();
    private RegistroSalidaPersistencia registroSalidaPersistencia = new RegistroSalidaPersistencia();
    private RegistroLlegadaPersistencia registroLlegadaPersistencia = new RegistroLlegadaPersistencia();
    private ConfiguracionPersistencia configuracionPersistencia = new ConfiguracionPersistencia();
    private BoletoPersistencia boletoPersistencia = new BoletoPersistencia();

    private boolean verificarAcceso(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("usuario?accion=login");
            return false;
        }
        if (!(usuario instanceof AdminSucursal) && !(usuario instanceof Chofer)) {
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
                listarViajes(request, response);
                break;
            case "nuevo":
                request.setAttribute("buses", new BusPersistencia().listarTodos());
                request.setAttribute("choferes", new ChoferPersistencia().listarTodos());
                request.setAttribute("rutas", new RutaPersistencia().listarTodos());
                request.getRequestDispatcher("/vistas/viaje/registrarViaje.jsp").forward(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            case "registrarSalida":
                request.setAttribute("idViaje", request.getParameter("idViaje"));
                prepararTipoViaje(request);
                request.getRequestDispatcher("/vistas/viaje/registrarSalida.jsp").forward(request, response);
                break;
            case "registrarLlegada":
                request.setAttribute("idViaje", request.getParameter("idViaje"));
                prepararTipoViaje(request);
                request.getRequestDispatcher("/vistas/viaje/registrarLlegada.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("viaje?accion=listar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!verificarAcceso(request, response)) {
            return;
        }

        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        String accion = request.getParameter("accion");

        switch (accion) {
            case "registrar":
                registrarViaje(request, response);
                break;
            case "actualizar":
                actualizarViaje(request, response);
                break;
            case "eliminar":
                eliminarViaje(request, response);
                break;
            case "salida":
                registrarSalida(request, response, usuario);
                break;
            case "llegada":
                registrarLlegada(request, response, usuario);
                break;
            default:
                response.sendRedirect("viaje?accion=listar");
        }
    }

    private void listarViajes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        ArrayList<ViajeRegular> viajes;
        if (usuario instanceof Chofer) {
            //Un chofer solo debe ver sus propios viajes asignados,
            //no los de todos los demas choferes de la empresa
            viajes = viajeRegularPersistencia.listarPorChofer(usuario.getIdUsuario());
        } else {
            //AdminSucursal ve todos los viajes, sin restriccion
            viajes = viajeRegularPersistencia.listarTodos();
        }

        request.setAttribute("viajes", viajes);
        request.getRequestDispatcher("/vistas/viaje/listarViajes.jsp").forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("viaje?accion=listar");
            return;
        }
        int id = Integer.parseInt(idParam);
        Optional<ViajeRegular> resultado = viajeRegularPersistencia.buscarPorId(id);

        if (resultado.isPresent()) {
            ViajeRegular viaje = resultado.get();
            request.setAttribute("viaje", viaje);
            request.setAttribute("buses", new BusPersistencia().listarTodos());
            request.setAttribute("choferes", new ChoferPersistencia().listarTodos());
            request.setAttribute("rutas", new RutaPersistencia().listarTodos());
            request.getRequestDispatcher("/vistas/viaje/editarViaje.jsp").forward(request, response);
        } else {
            response.sendRedirect("viaje?accion=listar");
        }
    }

    private void registrarViaje(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        //Se cargan las listas de buses, choferes y rutas ANTES de cualquier
        //validacion, para que esten disponibles si hay que recargar el
        //formulario con un mensaje de error
        request.setAttribute("buses", new BusPersistencia().listarTodos());
        request.setAttribute("choferes", new ChoferPersistencia().listarTodos());
        request.setAttribute("rutas", new RutaPersistencia().listarTodos());

        try {
            int idBus = Integer.parseInt(request.getParameter("idBus"));
            int idChofer = Integer.parseInt(request.getParameter("idChofer"));
            int idRuta = Integer.parseInt(request.getParameter("idRuta"));
            String fechaSalidaStr = request.getParameter("fechaHoraSalida");
            String fechaLlegadaStr = request.getParameter("fechaHoraLlegadaEstimada");

            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date fechaSalida = formato.parse(fechaSalidaStr);
            Date fechaLlegada = formato.parse(fechaLlegadaStr);

            //Validacion: no se puede programar un viaje con
            //fecha de salida en el pasado
            if (fechaSalida.before(new Date())) {
                mostrarError(request, response, "fechaHoraSalida", "La fecha de salida no puede ser en el pasado", "/vistas/viaje/registrarViaje.jsp");
                return;
            }

            //Validacion: la llegada estimada debe ser posterior a la salida
            if (!fechaLlegada.after(fechaSalida)) {
                mostrarError(request, response, "fechaHoraLlegadaEstimada", "La fecha de llegada debe ser posterior a la fecha de salida", "/vistas/viaje/registrarViaje.jsp");
                return;
            }

            //Validacion: el chofer no puede tener otro viaje en ese horario
            if (viajeRegularPersistencia.choferTieneViajeEnHorario(idChofer, fechaSalida, fechaLlegada, null)) {
                mostrarError(request, response, "idChofer", "El chofer seleccionado ya tiene otro viaje asignado en ese horario", "/vistas/viaje/registrarViaje.jsp");
                return;
            }

            //Validacion: el bus no puede tener otro viaje en ese horario
            if (viajeRegularPersistencia.busTieneViajeEnHorario(idBus, fechaSalida, fechaLlegada, null)) {
                mostrarError(request, response, "idBus", "El bus seleccionado ya tiene otro viaje asignado en ese horario", "/vistas/viaje/registrarViaje.jsp");
                return;
            }

            Bus bus = new Bus();
            bus.setIdBus(idBus);
            Chofer chofer = new Chofer();
            chofer.setIdUsuario(idChofer);
            Ruta ruta = new Ruta();
            ruta.setIdRuta(idRuta);

            ViajeRegular viaje = new ViajeRegular(bus, chofer, fechaSalida, fechaLlegada, ruta);
            viajeRegularPersistencia.insertar(viaje);

            response.sendRedirect("viaje?accion=listar");

        } catch (NumberFormatException e) {
            mostrarError(request, response, "idBus/idChofer/idRuta", "Debe seleccionar valores válidos", "/vistas/viaje/registrarViaje.jsp");
        } catch (ParseException e) {
            mostrarError(request, response, "fecha", "Formato de fecha inválido", "/vistas/viaje/registrarViaje.jsp");
        }
    }
    
    private void actualizarViaje(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        //Se cargan las listas necesarias y el viaje actual ANTES de
        //cualquier validacion, para que esten disponibles si hay que
        //recargar el formulario con un mensaje de error
        int idViaje = Integer.parseInt(request.getParameter("idViaje"));
        request.setAttribute("buses", new BusPersistencia().listarTodos());
        request.setAttribute("choferes", new ChoferPersistencia().listarTodos());
        request.setAttribute("rutas", new RutaPersistencia().listarTodos());

        Optional<ViajeRegular> viajeActualOpt = viajeRegularPersistencia.buscarPorId(idViaje);
        if (viajeActualOpt.isPresent()) {
            request.setAttribute("viaje", viajeActualOpt.get());
        }

        try {
            int idBus = Integer.parseInt(request.getParameter("idBus"));
            int idChofer = Integer.parseInt(request.getParameter("idChofer"));
            int idRuta = Integer.parseInt(request.getParameter("idRuta"));
            String fechaSalidaStr = request.getParameter("fechaHoraSalida");
            String fechaLlegadaStr = request.getParameter("fechaHoraLlegadaEstimada");

            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date fechaSalida = formato.parse(fechaSalidaStr);
            Date fechaLlegada = formato.parse(fechaLlegadaStr);

            //Validacion: la llegada estimada debe ser posterior
            //a la salida (aplica siempre, sin importar si es creacion o edicion)
            if (!fechaLlegada.after(fechaSalida)) {
                mostrarError(request, response, "fechaHoraLlegadaEstimada", "La fecha de llegada debe ser posterior a la fecha de salida", "/vistas/viaje/editarViaje.jsp");
                return;
            }

            //Validacion: el chofer no puede tener otro viaje en ese horario
            //(se excluye este mismo viaje de la comparacion, ya que se esta editando)
            if (viajeRegularPersistencia.choferTieneViajeEnHorario(idChofer, fechaSalida, fechaLlegada, idViaje)) {
                mostrarError(request, response, "idChofer", "El chofer seleccionado ya tiene otro viaje asignado en ese horario", "/vistas/viaje/editarViaje.jsp");
                return;
            }

            //Validacion: el bus no puede tener otro viaje en ese horario
            if (viajeRegularPersistencia.busTieneViajeEnHorario(idBus, fechaSalida, fechaLlegada, idViaje)) {
                mostrarError(request, response, "idBus", "El bus seleccionado ya tiene otro viaje asignado en ese horario", "/vistas/viaje/editarViaje.jsp");
                return;
            }

            Bus bus = new Bus();
            bus.setIdBus(idBus);
            Chofer chofer = new Chofer();
            chofer.setIdUsuario(idChofer);
            Ruta ruta = new Ruta();
            ruta.setIdRuta(idRuta);

            ViajeRegular viaje = new ViajeRegular(bus, chofer, fechaSalida, fechaLlegada, ruta);
            viaje.setIdViaje(idViaje);
            viajeRegularPersistencia.actualizar(viaje);

            response.sendRedirect("viaje?accion=listar");

        } catch (NumberFormatException e) {
            mostrarError(request, response, "idBus/idChofer/idRuta", "Debe seleccionar valores válidos", "/vistas/viaje/editarViaje.jsp");
        } catch (ParseException e) {
            mostrarError(request, response, "fecha", "Formato de fecha inválido", "/vistas/viaje/editarViaje.jsp");
        }
    }

    private void eliminarViaje(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));

        //Solo se puede eliminar si no ha sido iniciado (sin registro de salida), ni pagado
        boolean tieneSalida = registroSalidaPersistencia.buscarPorViaje(id).isPresent();
        boolean tieneBoletosPagados = boletoPersistencia.contarPorViaje(id) > 0;

        if (tieneSalida || tieneBoletosPagados) {
            //Se arma un mensaje mas preciso segun la razon real, en vez de
            //un mensaje generico que puede confundir (ej. decir "ya fue
            //iniciado" cuando en realidad el viaje ya esta completado)
            String mensaje;
            if (tieneSalida && tieneBoletosPagados) {
                mensaje = "No se puede eliminar el viaje porque ya fue iniciado y tiene boletos vendidos";
            } else if (tieneSalida) {
                mensaje = "No se puede eliminar el viaje porque ya fue iniciado o completado";
            } else {
                mensaje = "No se puede eliminar el viaje porque ya tiene boletos vendidos";
            }

            ExcepcionViajeNoEliminable excepcion = new ExcepcionViajeNoEliminable(mensaje);
            request.setAttribute("error", excepcion.getMessage());
            listarViajes(request, response);
            return;
        }

        viajeRegularPersistencia.eliminar(id);
        response.sendRedirect("viaje?accion=listar");
    }

    private void registrarSalida(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException, ServletException {
        try {
            int idViaje = Integer.parseInt(request.getParameter("idViaje"));

            Viaje viaje = viajePersistencia.buscarPorId(idViaje).orElse(null);
            if (viaje == null) {
                response.sendRedirect("viaje?accion=listar");
                return;
            }

            //Se determina a que listado redirigir segun el tipo de viaje
            String listadoDestino = (viaje instanceof ViajePrivado) ? "alquiler?accion=listar" : "viaje?accion=listar";

            //Validacion de permiso: solo el AdminSucursal o el chofer
            //especificamente asignado a este viaje pueden registrar su salida
            boolean esAdminSucursal = usuario instanceof AdminSucursal;
            boolean esChoferAsignado = usuario instanceof Chofer
                    && viaje.getChofer().getIdUsuario() == usuario.getIdUsuario();
            if (!esAdminSucursal && !esChoferAsignado) {
                response.sendRedirect(listadoDestino);
                return;
            }

            //Validacion: no se puede registrar salida si ya existe una
            if (registroSalidaPersistencia.buscarPorViaje(idViaje).isPresent()) {
                request.setAttribute("error", "Este viaje ya tiene registrada su salida");
                response.sendRedirect(listadoDestino);
                return;
            }

            double kilometrajeSalida = Double.parseDouble(request.getParameter("kilometrajeSalida"));

            //Validacion: el kilometraje no puede ser negativo
            if (kilometrajeSalida < 0) {
                mostrarError(request, response, "kilometrajeSalida", "El kilometraje no puede ser un valor negativo", "/vistas/viaje/registrarSalida.jsp");
                return;
            }

            RegistroSalida registro = new RegistroSalida(viaje, new Date(), kilometrajeSalida);
            registroSalidaPersistencia.insertar(registro);

            response.sendRedirect(listadoDestino);

        } catch (NumberFormatException e) {
            mostrarError(request, response, "kilometrajeSalida", "Debe ingresar un valor numérico válido", "/vistas/viaje/registrarSalida.jsp");
        }
    }

    private void registrarLlegada(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException, ServletException {
        try {
            int idViaje = Integer.parseInt(request.getParameter("idViaje"));

            Viaje viaje = viajePersistencia.buscarPorId(idViaje).orElse(null);
            if (viaje == null) {
                response.sendRedirect("viaje?accion=listar");
                return;
            }

            //Se determina a que listado redirigir segun el tipo de viaje
            String listadoDestino = (viaje instanceof ViajePrivado) ? "alquiler?accion=listar" : "viaje?accion=listar";

            //Validacion de permiso: solo el AdminSucursal o el chofer
            //especificamente asignado a este viaje pueden registrar su llegada
            boolean esAdminSucursal = usuario instanceof AdminSucursal;
            boolean esChoferAsignado = usuario instanceof Chofer
                    && viaje.getChofer().getIdUsuario() == usuario.getIdUsuario();
            if (!esAdminSucursal && !esChoferAsignado) {
                response.sendRedirect(listadoDestino);
                return;
            }

            //Validacion: no se puede registrar llegada si ya existe una
            if (registroLlegadaPersistencia.buscarPorViaje(idViaje).isPresent()) {
                request.setAttribute("error", "Este viaje ya tiene registrada su llegada");
                response.sendRedirect(listadoDestino);
                return;
            }

            double kilometrajeLlegada = Double.parseDouble(request.getParameter("kilometrajeLlegada"));
            double gastoCombustible = Double.parseDouble(request.getParameter("gastoCombustible"));

            //Validacion: los valores no pueden ser negativos
            if (kilometrajeLlegada < 0 || gastoCombustible < 0) {
                mostrarError(request, response, "kilometrajeLlegada/gastoCombustible", "Los valores no pueden ser negativos", "/vistas/viaje/registrarLlegada.jsp");
                return;
            }

            //Se necesita el kilometraje de salida para calcular correctamente
            //los kilometros recorridos en ESTE viaje especifico
            RegistroSalida salida = registroSalidaPersistencia.buscarPorViaje(idViaje).orElse(null);
            if (salida == null) {
                request.setAttribute("error", "Este viaje no tiene registrada su salida todavía");
                response.sendRedirect(listadoDestino);
                return;
            }

            //Validacion: el kilometraje de llegada no puede ser menor al de salida
            if (kilometrajeLlegada < salida.getKilometrajeSalida()) {
                mostrarError(request, response, "kilometrajeLlegada", "El kilometraje de llegada no puede ser menor al de salida", "/vistas/viaje/registrarLlegada.jsp");
                return;
            }

            double montoDepreciacionPorKm = configuracionPersistencia.obtenerMontoDepreciacionActual();

            RegistroLlegada registro = new RegistroLlegada(viaje, new Date(), kilometrajeLlegada,
                    salida.getKilometrajeSalida(), gastoCombustible, montoDepreciacionPorKm);

            registroLlegadaPersistencia.registrarLlegada(registro, viaje.getBus().getIdBus());

            response.sendRedirect(listadoDestino);

        } catch (NumberFormatException e) {
            mostrarError(request, response, "kilometrajeLlegada/gastoCombustible", "Debe ingresar valores numéricos válidos", "/vistas/viaje/registrarLlegada.jsp");
        }
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response,
                            String campo, String mensaje, String vista) throws IOException, ServletException {
        ExcepcionFormatoInvalido excepcion = new ExcepcionFormatoInvalido(campo, mensaje);
        request.setAttribute("error", excepcion.getMessage());
        request.getRequestDispatcher(vista).forward(request, response);
    }
    
    private void prepararTipoViaje(HttpServletRequest request) {
        int idViaje = Integer.parseInt(request.getParameter("idViaje"));
        Viaje viaje = viajePersistencia.buscarPorId(idViaje).orElse(null);
        if (viaje instanceof ViajePrivado) {
            request.setAttribute("listadoDestino", "alquiler?accion=listar");
        } else {
            request.setAttribute("listadoDestino", "viaje?accion=listar");
        }
    }
}
