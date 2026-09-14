<%-- 
    Document   : verCartera
    Created on : 9/09/2026, 08:54:14
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Cartera"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Mi Cartera</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<%
    //El Controlador ya busco la cartera del usuario en sesion
    Cartera cartera = (Cartera) request.getAttribute("cartera");
%>

<div class="alert alert-info">
    <h3>Saldo actual: <%= cartera.getSaldo() %></h3>
</div>

<a href="<%= request.getContextPath() %>/cartera?accion=recargar" class="btn btn-primary">Recargar Cartera</a>
<a href="<%= request.getContextPath() %>/cartera?accion=historial" class="btn btn-outline-secondary">Ver Historial de Movimientos</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
