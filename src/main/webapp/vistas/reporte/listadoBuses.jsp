<%-- 
    Document   : listadoBuses
    Created on : 11/09/2026, 09:06:16
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Bus"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Reporte: Listado de Buses</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>

<table class="table table-striped">
    <tr>
        <th>Placa</th>
        <th>Marca</th>
        <th>Modelo</th>
        <th>Capacidad</th>
        <th>Estado</th>
        <th>Kilometraje</th>
    </tr>
    <%
        //Se recorre la lista de buses de la sucursal del AdminSucursal
        //en sesion, ya filtrada por el Controlador
        ArrayList<Bus> buses = (ArrayList<Bus>) request.getAttribute("buses");
        if (buses != null) {
            for (Bus bus : buses) {
    %>
    <tr>
        <td><%= bus.getPlaca() %></td>
        <td><%= bus.getMarca() %></td>
        <td><%= bus.getModelo() %></td>
        <td><%= bus.getCapacidad() %></td>
        <td><%= bus.getEstadoOperativo() %></td>
        <td><%= bus.getKilometraje() %></td>
    </tr>
    <%
            }
        }
    %>
</table>
<a href="<%= request.getContextPath() %>/reporte?accion=menu">Volver al menú de reportes</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
