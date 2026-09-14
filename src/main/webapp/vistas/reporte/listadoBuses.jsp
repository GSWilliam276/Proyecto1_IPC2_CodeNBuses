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
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<table class="table table-striped">
    <thead>
        <tr>
            <th>Placa</th>
            <th>Marca</th>
            <th>Modelo</th>
            <th>Capacidad</th>
            <th>Estado</th>
            <th>Kilometraje</th>
            <th>Chofer Actual</th>
            <th>Total de Viajes</th>
        </tr>
    </thead>
    <tbody>       
    <%
        //Cada fila trae: bus, chofer actual asignado, total de viajes
        //realizados (ya calculado en el Controlador)
        ArrayList<Object[]> filas = (ArrayList<Object[]>) request.getAttribute("filasReporte");
        if (filas != null) {
            for (Object[] fila : filas) {
                Bus bus = (Bus) fila[0];
                String choferActual = (String) fila[1];
                int totalViajes = (int) fila[2];
    %>
    <tr>
        <td><%= bus.getPlaca() %></td>
        <td><%= bus.getMarca() %></td>
        <td><%= bus.getModelo() %></td>
        <td><%= bus.getCapacidad() %></td>
        <td><%= bus.getEstadoOperativo() %></td>
        <td><%= bus.getKilometraje() %></td>
        <td><%= choferActual %></td>
        <td><%= totalViajes %></td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>
<a href="<%= request.getContextPath() %>/reporte?accion=menu">Volver al menú de reportes</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
