<%-- 
    Document   : recargar
    Created on : 9/09/2026, 08:56:15
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Recargar Cartera</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>

<form method="POST" action="<%= request.getContextPath() %>/cartera">
    <input type="hidden" name="accion" value="recargar"/>

    <label>Monto a Recargar:</label>
    <br>
    <input type="number" step="0.01" min="0.01" name="monto" class="form-control" required/>
    <br>

    <%-- Las fechas se ingresan de forma manual en todas las operaciones que lo requieran --%>
    <label>Fecha:</label>
    <br>
    <input type="date" name="fecha" class="form-control" required/>
    <br>

    <button type="submit" class="btn btn-primary">Recargar</button>
</form>
<a href="<%= request.getContextPath() %>/cartera?accion=ver">Volver a mi cartera</a>
