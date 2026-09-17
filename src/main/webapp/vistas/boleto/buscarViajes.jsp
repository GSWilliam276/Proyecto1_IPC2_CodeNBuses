<%-- 
    Document   : buscarViajes
    Created on : 9/09/2026, 08:11:07
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.ViajeRegular"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Locale"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Viajes Disponibles</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<a href="<%= request.getContextPath() %>/boleto?accion=misBoletos" class="btn btn-outline-secondary mb-3">Ver mis boletos</a>

<%
    //Formato de fecha y hora en español
    SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy, HH:mm", new Locale("es", "ES"));
%>
<table class="table table-striped">
    <thead>
        <tr>
            <th>Ruta</th>
            <th>Salida</th>
            <th>Precio</th>
            <th>Bus</th>
            <th>Asientos Disponibles</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
    <%
        //Cada fila trae: viaje, cantidad de asientos disponibles,
        //y si el viaje ya paso su fecha de salida (ya calculado en el Controlador)
        ArrayList<Object[]> filas = (ArrayList<Object[]>) request.getAttribute("filasViajes");
        if (filas != null) {
            for (Object[] fila : filas) {
                ViajeRegular viaje = (ViajeRegular) fila[0];
                int disponibles = (int) fila[1];
                boolean yaPaso = (boolean) fila[2];
    %>
    <tr>
        <td><%= viaje.getRuta().getSucursalOrigen().getNombre() %> - <%= viaje.getRuta().getSucursalDestino().getNombre() %></td>
        <td><%= formatoFechaHora.format(viaje.getFechaHoraSalida()) %></td>
        <td><%= viaje.getRuta().getPrecioBoleto() %></td>
        <td><%= viaje.getBus().getPlaca() %> - <%= viaje.getBus().getMarca() %></td>
        <td>
            <%
                //Se muestra "No disponible" si el viaje ya paso su fecha
                //de salida, sin importar cuantos asientos quedaban libres
                if (yaPaso) {
            %>
                <span class="text-muted">No disponible</span>
            <%
                } else if (disponibles <= 0) {
            %>
                <span class="text-danger">Agotado</span>
            <% } else { %>
                <%= disponibles %>
            <% } %>
        </td>
        <td>
            <% if (!yaPaso && disponibles > 0) { %>
                <a href="<%= request.getContextPath() %>/boleto?accion=elegirAsiento&idViaje=<%= viaje.getIdViaje() %>" class="btn btn-sm btn-primary">Comprar Boleto</a>
            <% } %>
        </td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>

<%@ include file="/vistas/comunes/footer.jsp" %>
