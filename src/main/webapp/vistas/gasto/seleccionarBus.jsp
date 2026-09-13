<%-- 
    Document   : seleccionarBus
    Created on : 12/09/2026, 17:37:34
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Bus"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Gastos de Taller</h1>
<p>Seleccione un bus para ver sus gastos registrados:</p>

<form method="GET" action="<%= request.getContextPath() %>/gasto">
    <input type="hidden" name="accion" value="listar"/>
    <select name="idBus" class="form-control" required>
        <%
            ArrayList<Bus> buses = (ArrayList<Bus>) request.getAttribute("buses");
            if (buses != null) {
                for (Bus bus : buses) {
        %>
            <option value="<%= bus.getIdBus() %>"><%= bus.getPlaca() %> - <%= bus.getMarca() %></option>
        <%
                }
            }
        %>
    </select>
    <button type="submit" class="btn btn-primary">Ver Gastos</button>
</form>

<%@ include file="/vistas/comunes/footer.jsp" %>
