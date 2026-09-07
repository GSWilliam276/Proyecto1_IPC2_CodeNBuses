<%-- 
    Document   : listarSucursales
    Created on : 6/09/2026, 20:48:22
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Sucursal"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
<head>
    <title>Listado de Sucursales</title>
</head>
<body>
    <h1>Listado de Sucursales</h1>

    <% if (request.getAttribute("error") != null) { %>
        <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <a href="<%= request.getContextPath() %>/sucursal?accion=nuevo">Registrar nueva sucursal</a>

    <table border="1">
        <tr>
            <th>Nombre</th>
            <th>Ubicación</th>
            <th>Acciones</th>
        </tr>
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
                <a href="<%= request.getContextPath() %>/sucursal?accion=editar&id=<%= sucursal.getIdSucursal() %>">Editar</a>
            </td>
        </tr>
        <%
                }
            }
        %>
    </table>
</body>
</html>
