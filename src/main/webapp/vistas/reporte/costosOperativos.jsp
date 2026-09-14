<%-- 
    Document   : costosOperativos
    Created on : 11/09/2026, 16:09:42
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Sucursal"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Reporte: Costos Operativos</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<form method="GET" action="<%= request.getContextPath() %>/reporte">
    <input type="hidden" name="accion" value="costosOperativos"/>
    <label>Desde:</label>
    <input type="date" name="desde"/>
    <label>Hasta:</label>
    <input type="date" name="hasta"/>
    <label>Sucursal (opcional):</label>
    <select name="idSucursal">
        <option value="">Todas</option>
        <%
            ArrayList<Sucursal> sucursales = (ArrayList<Sucursal>) request.getAttribute("sucursales");
            if (sucursales != null) {
                for (Sucursal sucursal : sucursales) {
        %>
            <option value="<%= sucursal.getIdSucursal() %>"><%= sucursal.getNombre() %></option>
        <%
                }
            }
        %>
    </select>
    <button type="submit" class="btn btn-secondary">Filtrar</button>
</form>
<br>

<table class="table table-striped">
    <thead>
        <tr>
            <th>Sucursal</th>
            <th>Combustible</th>
            <th>Taller/Repuestos</th>
            <th>Depreciación</th>
        </tr>
    </thead>
    <tbody>    
    <%
        //Cada fila trae: nombre sucursal, combustible, taller, depreciacion
        //(ya calculado por separado en el Controlador, por categoria)
        ArrayList<Object[]> filas = (ArrayList<Object[]>) request.getAttribute("filasReporte");
        if (filas != null) {
            for (Object[] fila : filas) {
    %>
    <tr>
        <td><%= fila[0] %></td>
        <td><%= fila[1] %></td>
        <td><%= fila[2] %></td>
        <td><%= fila[3] %></td>
    </tr>
    <%
            }
        }
    %>
    <tr>
        <td><strong>TOTALES</strong></td>
        <td><strong><%= request.getAttribute("totalCombustible") %></strong></td>
        <td><strong><%= request.getAttribute("totalTaller") %></strong></td>
        <td><strong><%= request.getAttribute("totalDepreciacion") %></strong></td>
    </tr>
    <tr>
        <td colspan="3"><strong>GRAN TOTAL</strong></td>
        <td><strong><%= request.getAttribute("granTotal") %></strong></td>
    </tr>
    </tbody>
</table>
<a href="<%= request.getContextPath() %>/reporte?accion=menu">Volver al menú de reportes</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
