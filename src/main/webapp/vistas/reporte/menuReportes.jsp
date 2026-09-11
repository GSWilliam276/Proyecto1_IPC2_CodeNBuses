<%-- 
    Document   : menuReportes
    Created on : 11/09/2026, 08:51:58
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.AdminSucursal"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.AdminSistema"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Usuario"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Reportes</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>

<%
    //Se verifica el rol para mostrar solo los reportes que le
    //corresponden a cada tipo de usuario
    Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
    boolean esAdminSucursal = usuarioActual instanceof AdminSucursal;
    boolean esAdminSistema = usuarioActual instanceof AdminSistema;
%>

<% if (esAdminSucursal) { %>
    <h3>Reportes de mi Sucursal</h3>
    <ul>
        <li><a href="<%= request.getContextPath() %>/reporte?accion=listadoBuses">Listado de Buses</a></li>
        <li><a href="<%= request.getContextPath() %>/reporte?accion=listadoChoferes">Listado de Choferes</a></li>
        <li><a href="<%= request.getContextPath() %>/reporte?accion=ingresosBoletos">Ingresos por Venta de Boletos</a></li>
        <li><a href="<%= request.getContextPath() %>/reporte?accion=ingresosAlquiler">Ingresos por Alquiler</a></li>
        <li><a href="<%= request.getContextPath() %>/reporte?accion=depreciacionPorBus">Depreciación por Bus</a></li>
    </ul>
<% } %>

<% if (esAdminSistema) { %>
    <h3>Reportes del Sistema</h3>
    <ul>
        <li><a href="<%= request.getContextPath() %>/reporte?accion=ganancias">Reporte de Ganancias</a></li>
        <li><a href="<%= request.getContextPath() %>/reporte?accion=rutasDemandadas">Rutas Más Demandadas</a></li>
        <li><a href="<%= request.getContextPath() %>/reporte?accion=costosOperativos">Costos Operativos</a></li>
        <li><a href="<%= request.getContextPath() %>/reporte?accion=mapaRutas">Mapa de Rutas</a></li>
    </ul>
<% } %>

<%@ include file="/vistas/comunes/footer.jsp" %>
