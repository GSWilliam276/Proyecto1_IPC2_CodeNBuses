<%-- 
    Document   : editarPerfil
    Created on : 14/09/2026, 14:06:40
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Usuario"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Editar Perfil</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<%
    Usuario usuario = (Usuario) request.getAttribute("usuario");
%>

<div class="formulario-compacto">
    <form method="POST" action="<%= request.getContextPath() %>/usuario">
        <input type="hidden" name="accion" value="editarPerfil"/>

        <label>Nombre:</label>
        <br>
        <input type="text" name="nombre" value="<%= usuario.getNombre() %>" class="form-control" required/>
        <br>

        <label>NIT:</label>
        <br>
        <input type="text" name="nit" value="<%= usuario.getNit() != null ? usuario.getNit() : "" %>" class="form-control"/>
        <br>

        <label>DPI:</label>
        <br>
        <input type="text" name="dpi" value="<%= usuario.getDpi() %>" class="form-control" required/>
        <br>

        <label>Teléfono:</label>
        <br>
        <input type="text" name="telefono" value="<%= usuario.getTelefono() %>" class="form-control"/>
        <br>

        <label>Dirección:</label>
        <br>
        <input type="text" name="direccion" value="<%= usuario.getDireccion() %>" class="form-control"/>
        <br>

        <button type="submit" class="btn btn-primary">Guardar Cambios</button>
    </form>
</div>

<a href="<%= request.getContextPath() %>/usuario?accion=perfil">Volver al perfil</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
