<%-- 
    Document   : registrarSucursal
    Created on : 6/09/2026, 20:51:59
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Registrar Nueva Sucursal</h1>

<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>

<form method="POST" action="<%= request.getContextPath() %>/sucursal">
    <input type="hidden" name="accion" value="registrar"/>

    <label>Nombre:</label>
    <br>
    <input type="text" name="nombre" class="form-control" required/>
    <br>

    <label>Ubicación:</label>
    <br>
    <input type="text" name="ubicacion" class="form-control" required/>
    <br>

    <button type="submit" class="btn btn-primary">Registrar Sucursal</button>
</form>

<a href="<%= request.getContextPath() %>/sucursal?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
