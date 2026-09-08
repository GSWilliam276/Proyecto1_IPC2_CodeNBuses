<%-- 
    Document   : header
    Created on : 7/09/2026, 22:44:46
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Usuario"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CodeNBuses</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/recursos/css/estilos.css">
</head>
<body>
    <%
        //Se verifica si hay un usuario en sesion, para decidir que
        //mostrar en la barra de navegacion (menu de usuario logueado
        //vs. enlaces de "Iniciar sesion" / "Crear cuenta").
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
    %>

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="<%= request.getContextPath() %>/vistas/bienvenida.jsp">CodeNBuses</a>

            <% if (usuarioSesion != null) { %>
                <div>
                    <span class="navbar-text text-white me-3">
                        <%= usuarioSesion.getCorreo() %>
                    </span>
                    <a class="btn btn-outline-light btn-sm" href="<%= request.getContextPath() %>/usuario?accion=perfil">Mi Perfil</a>
                    <a class="btn btn-outline-light btn-sm" href="<%= request.getContextPath() %>/usuario?accion=logout">Cerrar Sesión</a>
                </div>
            <% } else { %>
                <div>
                    <a class="btn btn-outline-light btn-sm" href="<%= request.getContextPath() %>/usuario?accion=login">Iniciar Sesión</a>
                </div>
            <% } %>
        </div>
    </nav>

    <div class="container mt-4">
        