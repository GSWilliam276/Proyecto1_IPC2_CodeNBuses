<%-- 
    Document   : listarSucursales
    Created on : 6/09/2026, 20:48:22
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Sucursal"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Listado de Sucursales</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<a href="<%= request.getContextPath() %>/sucursal?accion=nuevo" class="btn btn-primary mb-3">Registrar nueva sucursal</a>
<table class="table table-striped">
    <thead>
        <tr>
            <th>Nombre</th>
            <th>Ubicación</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
    <%
        //Se recorre la lista de sucursales que el Controlador
        //ya trajo de la base de datos
        ArrayList<Sucursal> sucursales = (ArrayList<Sucursal>) request.getAttribute("sucursales");
        if (sucursales != null) {
            for (Sucursal sucursal : sucursales) {
    %>
    <tr>
        <td><%= sucursal.getNombre() %></td>
        <td><%= sucursal.getUbicacion() %></td>
        <td>
            <a href="<%= request.getContextPath() %>/sucursal?accion=editar&id=<%= sucursal.getIdSucursal() %>" class="btn btn-sm btn-outline-primary">Editar</a>
        </td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>

<%@ include file="/vistas/comunes/footer.jsp" %>
