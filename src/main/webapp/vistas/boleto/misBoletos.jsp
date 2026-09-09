<%-- 
    Document   : misBoletos
    Created on : 9/09/2026, 08:18:04
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Boleto"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Mis Boletos</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>

<a href="<%= request.getContextPath() %>/boleto?accion=buscarViajes" class="btn btn-outline-secondary mb-3">Buscar más viajes</a>

<table class="table table-striped">
    <tr>
        <th>Ruta</th>
        <th>Salida</th>
        <th>Asiento</th>
        <th>Precio Pagado</th>
        <th>Fecha de Pago</th>
    </tr>
    <%
        //Se recorre la lista de boletos del cliente en sesion,
        //que el Controlador ya filtro con listarPorCliente()
        ArrayList<Boleto> boletos = (ArrayList<Boleto>) request.getAttribute("boletos");
        if (boletos != null) {
            for (Boleto boleto : boletos) {
    %>
    <tr>
        <td><%= boleto.getViaje().getRuta().getSucursalOrigen().getNombre() %> - <%= boleto.getViaje().getRuta().getSucursalDestino().getNombre() %></td>
        <td><%= boleto.getViaje().getFechaHoraSalida() %></td>
        <td><%= boleto.getNumeroAsiento() %></td>
        <td><%= boleto.getPrecio() %></td>
        <td><%= boleto.getFechaPago() %></td>
    </tr>
    <%
            }
        }
    %>
</table>

<%@ include file="/vistas/comunes/footer.jsp" %>
