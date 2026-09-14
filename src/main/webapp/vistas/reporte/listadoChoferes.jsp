<%-- 
    Document   : listadoChoferes
    Created on : 11/09/2026, 09:10:21
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Chofer"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Reporte: Listado de Choferes</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<table class="table table-striped">
    <thead>
        <tr>
            <th>Correo</th>
            <th>Número de Licencia</th>
            <th>Tipo de Licencia</th>
            <th>Vencimiento</th>
            <th>Estado</th>
        </tr>
    </thead>
    <tbody>      
    <%
        //Se recorre la lista de choferes de la sucursal del AdminSucursal
        //en sesion, ya filtrada por el Controlador
        ArrayList<Chofer> choferes = (ArrayList<Chofer>) request.getAttribute("choferes");
        if (choferes != null) {
            for (Chofer chofer : choferes) {
    %>
    <tr>
        <td><%= chofer.getCorreo() %></td>
        <td><%= chofer.getNumeroLicencia() %></td>
        <td><%= chofer.getTipoLicencia() %></td>
        <td><%= chofer.getFechaVencimiento() %></td>
        <td><%= chofer.isActivo() ? "Activo" : "Inactivo" %></td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>
<a href="<%= request.getContextPath() %>/reporte?accion=menu">Volver al menú de reportes</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
