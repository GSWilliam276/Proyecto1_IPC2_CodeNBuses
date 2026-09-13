<%-- 
    Document   : ingresosAlquiler
    Created on : 11/09/2026, 09:34:31
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.ViajePrivado"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Reporte: Ingresos por Alquiler</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>

<form method="GET" action="<%= request.getContextPath() %>/reporte">
    <input type="hidden" name="accion" value="ingresosAlquiler"/>
    <label>Desde:</label>
    <input type="date" name="desde"/>
    <label>Hasta:</label>
    <input type="date" name="hasta"/>
    <button type="submit" class="btn btn-secondary">Filtrar</button>
</form>
<br>

<table class="table table-striped">
    <thead>
        <tr>
            <th>Origen</th>
            <th>Destino</th>
            <th>Fecha de Salida</th>
            <th>Precio Confirmado</th>
        </tr>
    </thead>
    <tbody>      
    <%
        ArrayList<ViajePrivado> alquileres = (ArrayList<ViajePrivado>) request.getAttribute("alquileres");
        double totalIngresos = 0;
        if (alquileres != null) {
            for (ViajePrivado viaje : alquileres) {
                totalIngresos += viaje.getPrecioConfirmado();
    %>
    <tr>
        <td><%= viaje.getOrigen() %></td>
        <td><%= viaje.getDestino() %></td>
        <td><%= viaje.getFechaHoraSalida() %></td>
        <td><%= viaje.getPrecioConfirmado() %></td>
    </tr>
    <%
            }
        }
    %>
    <tr>
        <td colspan="3"></td>
        <td><strong>Total: <%= totalIngresos %></strong></td>
    </tr>
    </tbody>
</table>
<a href="<%= request.getContextPath() %>/reporte?accion=menu">Volver al menú de reportes</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
