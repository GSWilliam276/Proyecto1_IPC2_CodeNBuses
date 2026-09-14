<%-- 
    Document   : ingresosBoletos
    Created on : 11/09/2026, 09:16:14
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Ruta"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Bus"%>
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

<%
    ArrayList<Ruta> rutas = (ArrayList<Ruta>) request.getAttribute("rutas");
    ArrayList<Bus> buses = (ArrayList<Bus>) request.getAttribute("buses");
%>

<%-- Filtro opcional de fechas, ruta y bus: si no se especifica,
     se toman en cuenta todos los registros --%>
<form method="GET" action="<%= request.getContextPath() %>/reporte">
    <input type="hidden" name="accion" value="ingresosBoletos"/>
    <label>Desde:</label>
    <input type="date" name="desde"/>
    <label>Hasta:</label>
    <input type="date" name="hasta"/>

    <label>Ruta (opcional):</label>
    <select name="idRuta">
        <option value="">Todas</option>
        <% if (rutas != null) { for (Ruta ruta : rutas) { %>
            <option value="<%= ruta.getIdRuta() %>">
                <%= ruta.getSucursalOrigen().getNombre() %> - <%= ruta.getSucursalDestino().getNombre() %>
            </option>
        <% } } %>
    </select>

    <label>Bus (opcional):</label>
    <select name="idBus">
        <option value="">Todos</option>
        <% if (buses != null) { for (Bus bus : buses) { %>
            <option value="<%= bus.getIdBus() %>"><%= bus.getPlaca() %></option>
        <% } } %>
    </select>

    <button type="submit" class="btn btn-secondary">Filtrar</button>
</form>
<br>

<%
    //Formato de fecha en español
    SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy, HH:mm", new Locale("es", "ES"));
%>
<table class="table table-striped">
    <thead>
        <tr>
            <th>Ruta</th>
            <th>Fecha de Salida</th>
            <th>Boletos Vendidos</th>
            <th>Ingreso Total</th>
        </tr>
    </thead>
    <tbody>  
    <%
        //Cada fila ya viene agrupada por viaje desde el Controlador:
        //[idViaje, ruta, fecha, cantidadBoletos, ingresoTotal]
        ArrayList<Object[]> filas = (ArrayList<Object[]>) request.getAttribute("filasReporte");
        double totalIngresos = 0;
        if (filas != null) {
            for (Object[] fila : filas) {
                double ingreso = (double) fila[4];
                totalIngresos += ingreso;
    %>
    <tr>
        <td><%= fila[1] %></td>
        <td><%= formatoFechaHora.format((java.util.Date) fila[2]) %></td>
        <td><%= fila[3] %></td>
        <td><%= ingreso %></td>
    </tr>
    <%
            }
        }
    %>
    <tr>
        <td colspan="3"></td>
        <td><strong>Total: <%= totalIngresos %></strong></td>
    </tr>
    </tbody>
</table>
<a href="<%= request.getContextPath() %>/reporte?accion=menu">Volver al menú de reportes</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
