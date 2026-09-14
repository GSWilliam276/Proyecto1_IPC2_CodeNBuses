<%-- 
    Document   : ingresosBoletos
    Created on : 11/09/2026, 09:16:14
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Boleto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Locale"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Reporte: Ingresos por Venta de Boletos</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
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

<%
    //Formato de fecha y hora en español
    SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy, HH:mm", new Locale("es", "ES"));
%>
<table class="table table-striped">
    <thead>
        <tr>
            <th>Ruta</th>
            <th>Fecha de Salida</th>
            <th>Asiento</th>
            <th>Precio</th>
            <th>Fecha de Pago</th>
        </tr>
    </thead>
    <tbody>  
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
        <td><%= formatoFechaHora.format(boleto.getViaje().getFechaHoraSalida()) %></td>
        <td><%= boleto.getNumeroAsiento() %></td>
        <td><%= boleto.getPrecio() %></td>
        <td><%= formatoFechaHora.format(boleto.getFechaPago()) %></td>
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
    </tbody>
</table>
<a href="<%= request.getContextPath() %>/reporte?accion=menu">Volver al menú de reportes</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
