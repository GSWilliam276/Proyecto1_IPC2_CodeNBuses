<%-- 
    Document   : registrarGasto
    Created on : 9/09/2026, 00:03:38
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Bus"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Registrar Gasto de Taller</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<form method="POST" action="<%= request.getContextPath() %>/gasto">
    <input type="hidden" name="accion" value="registrar"/>

    <label>Bus:</label>
    <br>
    <%
        //El Controlador pasa la lista de buses al 
        //mostrar este formulario, igual patron
        //que se aplico en los demas modulos con combobox
        ArrayList<Bus> buses = (ArrayList<Bus>) request.getAttribute("buses");
    %>
    <% if (buses == null || buses.isEmpty()) { %>
        <p style="color: red;">No hay buses registrados.</p>
    <% } else { %>
        <select name="idBus" class="form-control" required>
            <%
                for (Bus bus : buses) {
            %>
                <option value="<%= bus.getIdBus() %>"><%= bus.getPlaca() %> - <%= bus.getMarca() %></option>
            <%
                }
            %>
        </select>
    <% } %>
    <br>

    <label>Monto de Mano de Obra:</label>
    <br>
    <input type="number" step="0.01" name="montoManoObra" class="form-control" required/>
    <br>

    <label>Monto de Repuestos:</label>
    <br>
    <input type="number" step="0.01" name="montoRepuestos" class="form-control" required/>
    <br>

    <label>Fecha:</label>
    <br>
    <input type="date" name="fecha" class="form-control" required/>
    <br>

    <button type="submit" class="btn btn-primary">Registrar Gasto</button>
</form>
<a href="<%= request.getContextPath() %>/gasto?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
