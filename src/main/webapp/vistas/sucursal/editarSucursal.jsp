<%-- 
    Document   : editarSucursal
    Created on : 6/09/2026, 20:56:44
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Sucursal"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Editar Sucursal</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<%
    //El Controlador ya busco la sucursal por su ID y la guardo
    //en el request para poder precargar el formulario
    Sucursal sucursal = (Sucursal) request.getAttribute("sucursal");
%>
<form method="POST" action="<%= request.getContextPath() %>/sucursal">
    <input type="hidden" name="accion" value="actualizar"/>
    <input type="hidden" name="idSucursal" value="<%= sucursal.getIdSucursal() %>"/>
    <label>Nombre:</label>
    <br>
    <input type="text" name="nombre" value="<%= sucursal.getNombre() %>" class="form-control" required/>
    <br>
    <label>Ubicación:</label>
    <br>
    <input type="text" name="ubicacion" value="<%= sucursal.getUbicacion() %>" class="form-control" required/>
    <br>
    <button type="submit" class="btn btn-primary">Guardar Cambios</button>
</form>
<a href="<%= request.getContextPath() %>/sucursal?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
