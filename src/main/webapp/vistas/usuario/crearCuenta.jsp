<%-- 
    Document   : crearCuenta
    Created on : 6/09/2026, 12:23:46
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Crear Cuenta</h1>

<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<form method="POST" action="<%= request.getContextPath() %>/usuario">
    <input type="hidden" name="accion" value="crearCuenta"/>

    <label>NIT:</label>
    <br>
    <input type="text" name="nit" class="form-control"/>
    <br>

    <label>DPI:</label>
    <br>
    <input type="text" name="dpi" class="form-control" required/>
    <br>

    <label>Teléfono:</label>
    <br>
    <input type="text" name="telefono" class="form-control"/>
    <br>

    <label>Dirección:</label>
    <br>
    <input type="text" name="direccion" class="form-control"/>
    <br>

    <label>Correo:</label>
    <br>
    <input type="email" name="correo" class="form-control" required/>
    <br>

    <label>Contraseña:</label>
    <br>
    <input type="password" name="contrasena" class="form-control" required/>
    <br>

    <button type="submit" class="btn btn-primary">Crear cuenta</button>
</form>

<p>¿Ya tienes cuenta? <a href="<%= request.getContextPath() %>/usuario?accion=login">Inicia sesión aquí</a></p>

<%@ include file="/vistas/comunes/footer.jsp" %>
