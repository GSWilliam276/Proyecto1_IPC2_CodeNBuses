<%-- 
    Document   : rutasDemandadas
    Created on : 11/09/2026, 16:03:13
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>
<%@ include file="/vistas/comunes/exportarHTML.jsp" %>

<h1>Reporte: Rutas Más Demandadas</h1>
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
    String parametrosFiltro = "";
    if (desdeParam != null && !desdeParam.isEmpty()) {
        parametrosFiltro += "&desde=" + desdeParam;
    }
    if (hastaParam != null && !hastaParam.isEmpty()) {
        parametrosFiltro += "&hasta=" + hastaParam;
    }
%>

<a href="<%= request.getContextPath() %>/reporte?accion=rutasDemandadas&exportar=true&nombreReporte=rutas_demandadas<%= parametrosFiltro %>" class="btn btn-outline-success mb-3" target="_blank">
    <i class="bi bi-download"></i> Exportar a HTML
</a>

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
    <thead>
        <tr>
            <th>Origen</th>
            <th>Destino</th>
            <th>Total de Boletos Vendidos</th>
        </tr>
    </thead>
    <tbody>
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
    </tbody>
</table>
<a href="<%= request.getContextPath() %>/reporte?accion=menu">Volver al menú de reportes</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
