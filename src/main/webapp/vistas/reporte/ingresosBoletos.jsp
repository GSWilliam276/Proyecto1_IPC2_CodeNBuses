<%-- 
    Document   : ingresosBoletos
    Created on : 11/09/2026, 09:16:14
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Boleto"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Reporte: Ingresos por Venta de Boletos</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>

<%-- Filtro opcional de fechas: si no se especifica, se toman en cuenta todos los registros--%>
<form method="GET" action="<%= request.getContextPath() %>/reporte">
    <input type="hidden" name="accion" value="ingresosBoletos"/>
    <label>Desde:</label>
    <input type="date" name="desde"/>
    <label>Hasta:</label>
    <input type="date" name="hasta"/>
    <button type="submit" class="btn btn-secondary">Filtrar</button>
</form>
<br>

<table class="table table-striped">
    <tr>
        <th>Ruta</th>
        <th>Fecha de Salida</th>
        <th>Asiento</th>
        <th>Precio</th>
        <th>Fecha de Pago</th>
    </tr>
    <%
        //Se recorre la lista de boletos que el Controlador ya trajo
        ArrayList<Boleto> boletos = (ArrayList<Boleto>) request.getAttribute("boletos");
        double totalIngresos = 0;
        if (boletos != null) {
            for (Boleto boleto : boletos) {
                totalIngresos += boleto.getPrecio();
    %>
    <tr>
        <td><%= boleto.getViaje().getRuta().getSucursalOrigen().getNombre() %> - <%= boleto.getViaje().getRuta().getSucursalDestino().getNombre() %></td>
        <td><%= boleto.getViaje().getFechaHoraSalida() %></td>
        <td><%= boleto.getNumeroAsiento() %></td>
        <td><%= boleto.getPrecio() %></td>
        <td><%= boleto.getFechaPago() %></td>
    </tr>
    <%
            }
        }
    %>
    <tr>
        <td colspan="3"></td>
        <td><strong>Total: <%= totalIngresos %></strong></td>
        <td></td>
    </tr>
</table>
<a href="<%= request.getContextPath() %>/reporte?accion=menu">Volver al menú de reportes</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
