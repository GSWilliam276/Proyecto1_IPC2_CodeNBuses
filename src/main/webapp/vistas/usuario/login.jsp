<%-- 
    Document   : login
    Created on : 6/09/2026, 11:26:23
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Iniciar Sesión</h1>

<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<form method="POST" action="<%= request.getContextPath() %>/usuario">
    <input type="hidden" name="accion" value="login"/>

    <label>Correo:</label>
    <br>
    <input type="email" name="correo" class="form-control" required/>
    <br>

    <label>Contraseña:</label>
    <br>
    <input type="password" name="contrasena" class="form-control" required/>
    <br>

    <button type="submit" class="btn btn-primary">Ingresar</button>
</form>

<p>¿No tienes cuenta? <a href="<%= request.getContextPath() %>/usuario?accion=crearCuenta">Crea una aquí</a></p>

<%@ include file="/vistas/comunes/footer.jsp" %>
