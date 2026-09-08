<%-- 
    Document   : bienvenida
    Created on : 5/09/2026, 23:09:58
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>CodeNBuses</h1>
<p>
    Viaja con la confianza de una flota moderna y horarios pensados para llegar
    a tiempo. Reserva boletos para tus rutas favoritas, sigue el estado de tus
    viajes y accede a servicios de alquiler privado para grupos, todo desde
    un mismo lugar.
</p>

<a href="<%= request.getContextPath() %>/usuario?accion=login">
    <button class="btn btn-primary">Iniciar Sesión</button>
</a>

<a href="<%= request.getContextPath() %>/usuario?accion=crearCuenta">
    <button class="btn btn-secondary">Crear Cuenta</button>
</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
