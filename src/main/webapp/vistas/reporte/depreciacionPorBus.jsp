<%-- 
    Document   : depreciacionPorBus
    Created on : 11/09/2026, 15:38:56
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>
<%@ include file="/vistas/comunes/exportarHTML.jsp" %>

<h1>Reporte: Depreciación por Bus</h1>
<a href="<%= request.getContextPath() %>/reporte?accion=depreciacionPorBus&exportar=true&nombreReporte=depreciacion_por_bus" class="btn btn-outline-success mb-3" target="_blank">
    <i class="bi bi-download"></i> Exportar a HTML
</a>
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
            <th>Kilometraje Total</th>
            <th>Depreciación Acumulada Real</th>
        </tr>
    </thead>
    <tbody>      
    <%
        //Cada fila trae: placa, marca, kilometraje, depreciacion acumulada real
        //(sumada desde los registros de llegada ya guardados, respetando
        //que cada uno uso el monto por km vigente en su momento)
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
    </tbody>
</table>
<a href="<%= request.getContextPath() %>/reporte?accion=menu">Volver al menú de reportes</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
