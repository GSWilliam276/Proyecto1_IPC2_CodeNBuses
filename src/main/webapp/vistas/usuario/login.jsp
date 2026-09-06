<%-- 
    Document   : login
    Created on : 6/09/2026, 11:26:23
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Iniciar Sesión - CodeNBuses</title>
</head>
<body>
    <h1>Iniciar Sesión</h1>

    <% if (request.getAttribute("error") != null) { %>
        <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <form method="POST" action="<%= request.getContextPath() %>/usuario">
        <input type="hidden" name="accion" value="login"/>

        <label>Correo:</label>
        <br>
        <input type="email" name="correo" required/>
        <br>

        <label>Contraseña:</label>
        <br>
        <input type="password" name="contraseña" required/>
        <br>

        <button type="submit">Ingresar</button>
    </form>

    <p>¿No tienes cuenta? <a href="<%= request.getContextPath() %>/usuario?accion=crearCuenta">Crea una aquí</a></p>
</body>
</html
