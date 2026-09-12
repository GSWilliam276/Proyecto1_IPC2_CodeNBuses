<%-- 
    Document   : configurarDepreciacion
    Created on : 12/09/2026, 15:54:38
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Configurar Monto de Depreciación</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>

<%-- El monto que se configure aplica solo hacia adelante, sin afectar
     los registros de llegada ya calculados --%>
<div class="alert alert-info">
    Monto actual por kilómetro: <%= request.getAttribute("montoActual") %>
</div>

<form method="POST" action="<%= request.getContextPath() %>/admin">
    <input type="hidden" name="accion" value="guardarDepreciacion"/>

    <label>Nuevo Monto por Kilómetro:</label>
    <br>
    <input type="number" step="0.01" name="monto" class="form-control" required/>
    <br>

    <button type="submit" class="btn btn-primary">Guardar</button>
</form>
<a href="<%= request.getContextPath() %>/usuario?accion=perfil">Volver al perfil</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
