<%-- 
    Document   : listarGastos
    Created on : 9/09/2026, 00:02:17
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Gasto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Locale"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Gastos de Taller y Repuestos</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<%
    //El Controlador ya filtro los gastos por el bus seleccionado
    Integer idBus = (Integer) request.getAttribute("idBus");

    //Formato de fecha en español, sin hora ya que Gasto solo
    //guarda la fecha del mantenimiento, no un horario especifico
    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
%>
<a href="<%= request.getContextPath() %>/gasto?accion=nuevo" class="btn btn-primary mb-3">Registrar nuevo gasto</a>
<table class="table table-striped">
    <thead>
        <tr>
            <th>Mano de Obra</th>
            <th>Repuestos</th>
            <th>Total</th>
            <th>Fecha</th>
        </tr>
    </thead>
    <tbody>    
    <%
        //Se recorre la lista de gastos que el Controlador
        //ya trajo de la base de datos, filtrados por bus
        ArrayList<Gasto> gastos = (ArrayList<Gasto>) request.getAttribute("gastos");
        if (gastos != null) {
            for (Gasto gasto : gastos) {
    %>
    <tr>
        <td><%= gasto.getMontoManoObra() %></td>
        <td><%= gasto.getMontoRepuestos() %></td>
        <td><%= gasto.getMontoTotal() %></td>
        <td><%= formatoFecha.format(gasto.getFecha()) %></td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>

<%@ include file="/vistas/comunes/footer.jsp" %>
