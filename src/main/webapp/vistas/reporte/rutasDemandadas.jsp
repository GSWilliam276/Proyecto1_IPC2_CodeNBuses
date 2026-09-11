<%-- 
    Document   : rutasDemandadas
    Created on : 11/09/2026, 16:03:13
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Reporte: Rutas Más Demandadas</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>

<form method="GET" action="<%= request.getContextPath() %>/reporte">
    <input type="hidden" name="accion" value="rutasDemandadas"/>
    <label>Desde:</label>
    <input type="date" name="desde"/>
    <label>Hasta:</label>
    <input type="date" name="hasta"/>
    <button type="submit" class="btn btn-secondary">Filtrar</button>
</form>
<br>

<table class="table table-striped">
    <tr>
        <th>Origen</th>
        <th>Destino</th>
        <th>Total de Boletos Vendidos</th>
    </tr>
    <%
        //Cada fila es un Object[] con: origen, destino, total_boletos
        ArrayList<Object[]> rutasDemandadas = (ArrayList<Object[]>) request.getAttribute("rutasDemandadas");
        if (rutasDemandadas != null) {
            for (Object[] fila : rutasDemandadas) {
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
