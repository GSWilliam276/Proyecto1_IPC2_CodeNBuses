<%-- 
    Document   : listarAlquileres
    Created on : 9/09/2026, 07:24:59
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.ViajePrivado"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.AdminSucursal"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Usuario"%>
<%@page import="com.usac.buses.proyectocodenbuses.persistencia.RegistroSalidaPersistencia"%>
<%@page import="com.usac.buses.proyectocodenbuses.persistencia.RegistroLlegadaPersistencia"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Locale"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Alquileres Privados</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>
<%
    //Se verifica el rol del usuario en sesion para decidir si
    //mostrar el boton de "Confirmar Precio", exclusivo de AdminSucursal
    Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
    boolean esAdminSucursal = usuarioActual instanceof AdminSucursal;

    //Formato de fecha y hora en español
    SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy, HH:mm", new Locale("es", "ES"));

    ArrayList<ViajePrivado> alquileres = (ArrayList<ViajePrivado>) request.getAttribute("alquileres");

    //Se verifica si el usuario (no AdminSucursal) tiene al menos
    //un alquiler ya confirmado, para mostrarle un aviso destacado
    boolean tieneAlquilerConfirmado = false;
    if (!esAdminSucursal && alquileres != null) {
        for (ViajePrivado v : alquileres) {
            if (v.getPrecioConfirmado() > 0) {
                tieneAlquilerConfirmado = true;
                break;
            }
        }
    }

    //Se usan para saber, por cada alquiler ya confirmado, si ya tiene
    //registro de salida y/o llegada, igual patron que en listarViajes.jsp
    RegistroSalidaPersistencia registroSalidaPersistencia = new RegistroSalidaPersistencia();
    RegistroLlegadaPersistencia registroLlegadaPersistencia = new RegistroLlegadaPersistencia();
%>
<% if (tieneAlquilerConfirmado) { %>
    <div class="alert alert-success" role="alert">
        <i class="bi bi-check-circle-fill"></i> ¡Tienes alquileres ya confirmados! Revisa el precio final en la tabla.
    </div>
<% } %>
<a href="<%= request.getContextPath() %>/alquiler?accion=solicitar" class="btn btn-primary mb-3">Solicitar Alquiler</a>
<table class="table table-striped">
    <thead>
        <tr>
            <th>Origen</th>
            <th>Destino</th>
            <th>Pasajeros</th>
            <th>Salida</th>
            <th>Bus</th>
            <th>Chofer</th>
            <th>Precio Estimado</th>
            <th>Precio Confirmado</th>
            <% if (esAdminSucursal) { %>
                <th>Acciones</th>
            <% } %>
        </tr>
    </thead>
    <tbody>
    <%
        if (alquileres != null) {
            for (ViajePrivado viaje : alquileres) {
                //Solo tiene sentido consultar salida/llegada si ya
                //tiene bus y chofer asignados (ya fue confirmado)
                boolean tieneBusChofer = viaje.getBus() != null && viaje.getChofer() != null;
                boolean yaTieneSalida = tieneBusChofer && registroSalidaPersistencia.buscarPorViaje(viaje.getIdViaje()).isPresent();
                boolean yaTieneLlegada = tieneBusChofer && registroLlegadaPersistencia.buscarPorViaje(viaje.getIdViaje()).isPresent();
    %>
    <tr>
        <td><%= viaje.getOrigen() %></td>
        <td><%= viaje.getDestino() %></td>
        <td><%= viaje.getPasajeros() %></td>
        <td><%= formatoFechaHora.format(viaje.getFechaHoraSalida()) %></td>
        <td><%= viaje.getBus() != null ? viaje.getBus().getMarca() : "Sin asignar" %></td>
        <td><%= viaje.getChofer() != null ? viaje.getChofer().getNombre() : "Sin asignar" %></td>
        <td><%= viaje.getPrecioEstimado() %></td>
        <td><%= viaje.getPrecioConfirmado() > 0 ? viaje.getPrecioConfirmado() : "Pendiente" %></td>
        <%
            //El boton de confirmar precio solo se muestra si el usuario
            //en sesion es AdminSucursal, ya que solo el puede confirmar
            //o cambiar el precio estimado de un alquiler
            if (esAdminSucursal) {
        %>
        <td>
            <%
                //Solo se muestra el boton de confirmar si el precio aun
                //no ha sido confirmado (sigue en su valor por defecto de 0)
                if (viaje.getPrecioConfirmado() == 0) {
            %>
                <a href="<%= request.getContextPath() %>/alquiler?accion=confirmar&id=<%= viaje.getIdViaje() %>" class="btn btn-sm btn-outline-primary">Confirmar Precio</a>
            <%
                } else {
                    //Una vez confirmado y con bus/chofer asignados, se
                    //puede registrar salida y llegada, igual que un viaje regular
                    if (!yaTieneSalida) {
            %>
                <a href="<%= request.getContextPath() %>/viaje?accion=registrarSalida&idViaje=<%= viaje.getIdViaje() %>" class="btn btn-sm btn-outline-success">Salida</a>
            <%
                    } else if (!yaTieneLlegada) {
            %>
                <a href="<%= request.getContextPath() %>/viaje?accion=registrarLlegada&idViaje=<%= viaje.getIdViaje() %>" class="btn btn-sm btn-outline-warning">Llegada</a>
            <%
                    } else {
            %>
                <span class="text-muted">Completado</span>
            <%
                    }
                }
            %>
        </td>
        <% } %>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>

<%@ include file="/vistas/comunes/footer.jsp" %>
