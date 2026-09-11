<%-- 
    Document   : depreciacionPorBus
    Created on : 11/09/2026, 15:38:56
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Reporte: Depreciación por Bus</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>

<table class="table table-striped">
    <tr>
        <th>Placa</th>
        <th>Kilometraje Total</th>
        <th>Depreciación Acumulada Real</th>
    </tr>
    <%
        //Cada fila trae: placa, kilometraje, depreciacion acumulada real
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
    </tr>
    <%
            }
        }
    %>
</table>
<a href="<%= request.getContextPath() %>/reporte?accion=menu">Volver al menú de reportes</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
