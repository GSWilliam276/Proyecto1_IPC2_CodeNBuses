<%-- 
    Document   : buscarViajes
    Created on : 9/09/2026, 08:11:07
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.ViajeRegular"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Viajes Disponibles</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<a href="<%= request.getContextPath() %>/boleto?accion=misBoletos" class="btn btn-outline-secondary mb-3">Ver mis boletos</a>

<table class="table table-striped">
    <tr>
        <th>Ruta</th>
        <th>Salida</th>
        <th>Precio</th>
        <th>Bus</th>
        <th>Acciones</th>
    </tr>
    <%
        //Se recorre la lista de viajes regulares disponibles
        //que el Controlador ya trajo de la base de datos
        ArrayList<ViajeRegular> viajes = (ArrayList<ViajeRegular>) request.getAttribute("viajes");
        if (viajes != null) {
            for (ViajeRegular viaje : viajes) {
    %>
    <tr>
        <td><%= viaje.getRuta().getSucursalOrigen().getNombre() %> - <%= viaje.getRuta().getSucursalDestino().getNombre() %></td>
        <td><%= viaje.getFechaHoraSalida() %></td>
        <td><%= viaje.getRuta().getPrecioBoleto() %></td>
        <td><%= viaje.getBus().getPlaca() %></td>
        <td>
            <a href="<%= request.getContextPath() %>/boleto?accion=elegirAsiento&idViaje=<%= viaje.getIdViaje() %>" class="btn btn-sm btn-primary">Comprar Boleto</a>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>

<%@ include file="/vistas/comunes/footer.jsp" %>
