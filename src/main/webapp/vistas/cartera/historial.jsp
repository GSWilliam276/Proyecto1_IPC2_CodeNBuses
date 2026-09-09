<%-- 
    Document   : historial
    Created on : 9/09/2026, 08:58:36
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.MovimientoCartera"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Historial de Movimientos</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>

<table class="table table-striped">
    <tr>
        <th>Tipo</th>
        <th>Monto</th>
        <th>Fecha</th>
    </tr>
    <%
        //Se recorre la lista de movimientos de la cartera del usuario
        //en sesion, ordenados del mas reciente al mas antiguo
        //(ver MovimientoCarteraPersistencia.listarPorCartera)
        ArrayList<MovimientoCartera> movimientos = (ArrayList<MovimientoCartera>) request.getAttribute("movimientos");
        if (movimientos != null) {
            for (MovimientoCartera movimiento : movimientos) {
    %>
    <tr>
        <td><%= movimiento.getTipo() %></td>
        <td><%= movimiento.getMonto() %></td>
        <td><%= movimiento.getFecha() %></td>
    </tr>
    <%
            }
        }
    %>
</table>
<a href="<%= request.getContextPath() %>/cartera?accion=ver">Volver a mi cartera</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
