<%-- 
    Document   : registrarRuta
    Created on : 8/09/2026, 22:33:43
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Sucursal"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Registrar Nueva Ruta</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<%
    //Se recupera la lista de sucursales para armar los dos combobox
    //(origen y destino), ya que las rutas solo se pueden crear
    //entre sucursales existentes de la empresa
    ArrayList<Sucursal> sucursales = (ArrayList<Sucursal>) request.getAttribute("sucursales");
%>
<form method="POST" action="<%= request.getContextPath() %>/ruta">
    <input type="hidden" name="accion" value="registrar"/>

    <label>Sucursal de Origen:</label>
    <br>
    <% if (sucursales == null || sucursales.isEmpty()) { %>
        <p style="color: red;">No hay sucursales registradas. Contacta al administrador del sistema.</p>
    <% } else { %>
        <select name="idSucursalOrigen" class="form-control" required>
            <%
                for (Sucursal sucursal : sucursales) {
            %>
                <option value="<%= sucursal.getIdSucursal() %>"><%= sucursal.getNombre() %></option>
            <%
                }
            %>
        </select>
        <br>

        <label>Sucursal de Destino:</label>
        <br>
        <select name="idSucursalDestino" class="form-control" required>
            <%
                for (Sucursal sucursal : sucursales) {
            %>
                <option value="<%= sucursal.getIdSucursal() %>"><%= sucursal.getNombre() %></option>
            <%
                }
            %>
        </select>
    <% } %>
    <br>

    <label>Distancia (km):</label>
    <br>
    <input type="number" step="0.01" name="distanciaKm" class="form-control" required/>
    <br>

    <label>Precio del Boleto:</label>
    <br>
    <input type="number" step="0.01" name="precioBoleto" class="form-control" required/>
    <br>

    <button type="submit" class="btn btn-primary">Registrar Ruta</button>
</form>
<a href="<%= request.getContextPath() %>/ruta?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
