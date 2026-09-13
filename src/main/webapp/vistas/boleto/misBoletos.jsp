<%-- 
    Document   : misBoletos
    Created on : 9/09/2026, 08:18:04
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Boleto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Locale"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Mis Boletos</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>

<a href="<%= request.getContextPath() %>/boleto?accion=buscarViajes" class="btn btn-outline-secondary mb-3">Buscar más viajes</a>

<%
    //Formato de fecha y hora en español
    SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy, HH:mm", new Locale("es", "ES"));
%>
<table class="table table-striped">
    <thead>
        <tr>
            <th>Ruta</th>
            <th>Salida</th>
            <th>Asiento</th>
            <th>Precio Pagado</th>
            <th>Fecha de Pago</th>
        </tr>
    </thead>
    <tbody>
    <%
        //Se recorre la lista de boletos del cliente en sesion,
        //que el Controlador ya filtro con listarPorCliente()
        ArrayList<Boleto> boletos = (ArrayList<Boleto>) request.getAttribute("boletos");
        if (boletos != null) {
            for (Boleto boleto : boletos) {
    %>
    <tr>
        <td><%= boleto.getViaje().getRuta().getSucursalOrigen().getNombre() %> - <%= boleto.getViaje().getRuta().getSucursalDestino().getNombre() %></td>
        <td><%= formatoFechaHora.format(boleto.getViaje().getFechaHoraSalida()) %></td>
        <td><%= boleto.getNumeroAsiento() %></td>
        <td><%= boleto.getPrecio() %></td>
        <td><%= formatoFechaHora.format(boleto.getFechaPago()) %></td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>

<%@ include file="/vistas/comunes/footer.jsp" %>
