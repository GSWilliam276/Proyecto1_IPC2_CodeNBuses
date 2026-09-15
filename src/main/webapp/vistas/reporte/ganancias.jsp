<%-- 
    Document   : ganancias
    Created on : 11/09/2026, 15:54:07
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Sucursal"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>
<%@ include file="/vistas/comunes/exportarHTML.jsp" %>

<h1>Reporte de Ganancias</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<%
    //Se recuperan los filtros actuales (si existen) para que la
    //exportacion respete el mismo filtro que se esta viendo en pantalla
    String desdeParam = request.getParameter("desde");
    String hastaParam = request.getParameter("hasta");
    String idSucursalParam = request.getParameter("idSucursal");
    String parametrosFiltro = "";
    if (desdeParam != null && !desdeParam.isEmpty()) {
        parametrosFiltro += "&desde=" + desdeParam;
    }
    if (hastaParam != null && !hastaParam.isEmpty()) {
        parametrosFiltro += "&hasta=" + hastaParam;
    }
    if (idSucursalParam != null && !idSucursalParam.isEmpty()) {
        parametrosFiltro += "&idSucursal=" + idSucursalParam;
    }
%>

<a href="<%= request.getContextPath() %>/reporte?accion=ganancias&exportar=true&nombreReporte=ganancias<%= parametrosFiltro %>" class="btn btn-outline-success mb-3" target="_blank">
    <i class="bi bi-download"></i> Exportar a HTML
</a>

<%-- Filtro opcional: por rango de fecha y, opcionalmente, por sucursal --%>
<form method="GET" action="<%= request.getContextPath() %>/reporte">
    <input type="hidden" name="accion" value="ganancias"/>
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
            <th>Ingresos Boletos</th>
            <th>Ingresos Alquiler</th>
            <th>Costos Totales</th>
            <th>Ganancia Neta</th>
        </tr>
    </thead>
    <tbody>     
    <%
        //Cada fila trae: nombre sucursal, ingresos boletos, ingresos
        //alquiler, costos totales, ganancia neta (ya calculado en el Controlador)
        ArrayList<Object[]> filas = (ArrayList<Object[]>) request.getAttribute("filasReporte");
        if (filas != null) {
            for (Object[] fila : filas) {
    %>
    <tr>
        <td><%= fila[0] %></td>
        <td><%= fila[1] %></td>
        <td><%= fila[2] %></td>
        <td><%= fila[3] %></td>
        <td><%= fila[4] %></td>
    </tr>
    <%
            }
        }
    %>
    <tr>
        <td><strong>TOTALES</strong></td>
        <td colspan="2"><strong>Ingresos: <%= request.getAttribute("totalIngresos") %></strong></td>
        <td><strong>Costos: <%= request.getAttribute("totalCostos") %></strong></td>
        <td><strong>Ganancia: <%= request.getAttribute("totalGanancia") %></strong></td>
    </tr>
    </tbody>
</table>
<a href="<%= request.getContextPath() %>/reporte?accion=menu">Volver al menú de reportes</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
