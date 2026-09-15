<%-- 
    Document   : listadoChoferes
    Created on : 11/09/2026, 09:10:21
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Chofer"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>
<%@ include file="/vistas/comunes/exportarHTML.jsp" %>

<h1>Reporte: Listado de Choferes</h1>
<a href="<%= request.getContextPath() %>/reporte?accion=listadoChoferes&exportar=true&nombreReporte=listado_choferes" class="btn btn-outline-success mb-3" target="_blank">
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
            <th>Nombre Completo</th>
            <th>Número de Licencia</th>
            <th>Tipo de Licencia</th>
            <th>Vencimiento</th>
            <th>Estado</th>
            <th>Total de Viajes</th>
        </tr>
    </thead>
    <tbody>      
    <%
        //Cada fila trae: chofer, total de viajes realizados
        //(ya calculado en el Controlador)
        ArrayList<Object[]> filas = (ArrayList<Object[]>) request.getAttribute("filasReporte");
        if (filas != null) {
            for (Object[] fila : filas) {
                Chofer chofer = (Chofer) fila[0];
                int totalViajes = (int) fila[1];
    %>
    <tr>
        <td><%= chofer.getNombre() %></td>
        <td><%= chofer.getNumeroLicencia() %></td>
        <td><%= chofer.getTipoLicencia() %></td>
        <td><%= chofer.getFechaVencimiento() %></td>
        <td><%= chofer.isActivo() ? "Activo" : "Inactivo" %></td>
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
