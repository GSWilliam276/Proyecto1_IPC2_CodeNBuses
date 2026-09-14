<%-- 
    Document   : login
    Created on : 6/09/2026, 11:26:23
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<div class="formulario-con-icono">
    <div>
        <i class="bi bi-person-check bienvenida-imagen"></i>
    </div>

    <div class="formulario-compacto">
        <h1>Iniciar Sesión</h1>
        <p class="text-muted">Nos alegra verte de nuevo</p>

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

        <p class="mt-3">¿No tienes cuenta? <a href="<%= request.getContextPath() %>/usuario?accion=crearCuenta">Crea una aquí</a></p>
    </div>
</div>

<%@ include file="/vistas/comunes/footer.jsp" %>
