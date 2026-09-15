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
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>
<a href="<%= request.getContextPath() %>/sucursal?accion=nuevo" class="btn btn-primary mb-3">Registrar nueva sucursal</a>
<table class="table table-striped">
    <thead>
        <tr>
            <th>Nombre</th>
            <th>Ubicación</th>
            <th>Administrador Asignado</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
    <%
        //Cada fila trae: sucursal, si tiene al menos un
        //AdminSucursal asignado (ya calculado en el Controlador)
        ArrayList<Object[]> filas = (ArrayList<Object[]>) request.getAttribute("filasReporte");
        if (filas != null) {
            for (Object[] fila : filas) {
                Sucursal sucursal = (Sucursal) fila[0];
                boolean tieneAdmin = (boolean) fila[1];
    %>
    <tr>
        <td><%= sucursal.getNombre() %></td>
        <td><%= sucursal.getUbicacion() %></td>
        <td>
            <%
                //Se avisa visualmente si la sucursal aun no tiene
                //ningun AdminSucursal asignado
                if (tieneAdmin) {
            %>
                <span class="badge bg-success">Sí</span>
            <% } else { %>
                <span class="badge bg-warning text-dark">Sin asignar</span>
            <% } %>
        </td>
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
