<%-- 
    Document   : listarAdminsSucursal
    Created on : 10/09/2026, 23:31:03
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.AdminSucursal"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Administradores de Sucursal</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<a href="<%= request.getContextPath() %>/admin?accion=nuevo" class="btn btn-primary mb-3">Registrar nuevo administrador</a>
<table class="table table-striped">
    <tr>
        <th>Correo</th>
        <th>DPI</th>
        <th>Sucursal</th>
        <th>Acciones</th>
    </tr>
    <%
        //Se recorre la lista de administradores de sucursal
        //que el Controlador ya trajo de la base de datos
        ArrayList<AdminSucursal> admins = (ArrayList<AdminSucursal>) request.getAttribute("admins");
        if (admins != null) {
            for (AdminSucursal admin : admins) {
    %>
    <tr>
        <td><%= admin.getCorreo() %></td>
        <td><%= admin.getDpi() %></td>
        <td><%= admin.getSucursal().getNombre() %></td>
        <td>
            <a href="<%= request.getContextPath() %>/admin?accion=editar&id=<%= admin.getIdUsuario() %>" class="btn btn-sm btn-outline-primary">Editar</a>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>

<%@ include file="/vistas/comunes/footer.jsp" %>
