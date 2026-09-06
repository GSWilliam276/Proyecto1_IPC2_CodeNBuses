<%-- 
    Document   : crearCuenta
    Created on : 6/09/2026, 12:23:46
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Crear Cuenta - CodeNBuses</title>
</head>
<body>
    <h1>Crear Cuenta</h1>

    <% if (request.getAttribute("error") != null) { %>
        <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <form method="POST" action="usuario">
        <input type="hidden" name="accion" value="crearCuenta"/>

        <label>NIT:</label>
        <br>
        <input type="text" name="nit"/>
        <br>

        <label>DPI:</label>
        <br>
        <input type="text" name="dpi" required/>
        <br>

        <label>Teléfono:</label>
        <br>
        <input type="text" name="telefono"/>
        <br>

        <label>Dirección:</label>
        <br>
        <input type="text" name="direccion"/>
        <br>

        <label>Correo:</label>
        <br>
        <input type="email" name="correo" required/>
        <br>

        <label>Contraseña:</label>
        <br>
        <input type="password" name="contrasena" required/>
        <br>

        <button type="submit">Crear cuenta</button>
    </form>

    <p>¿Ya tienes cuenta? <a href="usuario?accion=login">Inicia sesión aquí</a></p>
</body>
</html>
