<%-- 
    Document   : listarAlquileres
    Created on : 9/09/2026, 07:24:59
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.ViajePrivado"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.AdminSucursal"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Locale"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Alquileres Privados</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<%
    //Se verifica el rol del usuario en sesion para decidir si
    //mostrar el boton de "Confirmar Precio", exclusivo de AdminSucursal
    Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
    boolean esAdminSucursal = usuarioActual instanceof AdminSucursal;

    //Formato de fecha y hora en español
    SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy, HH:mm", new Locale("es", "ES"));
%>
<a href="<%= request.getContextPath() %>/alquiler?accion=solicitar" class="btn btn-primary mb-3">Solicitar Alquiler</a>
<table class="table table-striped">
    <thead>
        <tr>
            <th>Origen</th>
            <th>Destino</th>
            <th>Pasajeros</th>
            <th>Salida</th>
            <th>Precio Estimado</th>
            <th>Precio Confirmado</th>
            <% if (esAdminSucursal) { %>
                <th>Acciones</th>
            <% } %>
        </tr>
    </thead>
    <tbody>
    <%
        ArrayList<ViajePrivado> alquileres = (ArrayList<ViajePrivado>) request.getAttribute("alquileres");
        if (alquileres != null) {
            for (ViajePrivado viaje : alquileres) {
    %>
    <tr>
        <td><%= viaje.getOrigen() %></td>
        <td><%= viaje.getDestino() %></td>
        <td><%= viaje.getPasajeros() %></td>
        <td><%= formatoFechaHora.format(viaje.getFechaHoraSalida()) %></td>
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
            %>
                <span class="text-muted">Ya confirmado</span>
            <%
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
