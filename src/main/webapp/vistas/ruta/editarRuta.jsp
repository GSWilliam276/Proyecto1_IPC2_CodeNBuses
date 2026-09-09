<%-- 
    Document   : editarRuta
    Created on : 8/09/2026, 22:51:16
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Ruta"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Sucursal"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Editar Ruta</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<%
    //El Controlador ya busco la ruta por su ID y la guardo en el request,
    //junto con la lista de sucursales para armar los combobox
    Ruta ruta = (Ruta) request.getAttribute("ruta");
    ArrayList<Sucursal> sucursales = (ArrayList<Sucursal>) request.getAttribute("sucursales");
%>
<form method="POST" action="<%= request.getContextPath() %>/ruta">
    <input type="hidden" name="accion" value="actualizar"/>
    <input type="hidden" name="idRuta" value="<%= ruta.getIdRuta() %>"/>

    <label>Sucursal de Origen:</label>
    <br>
    <select name="idSucursalOrigen" class="form-control" required>
        <%
            //Se marca como "selected" la sucursal de origen que la ruta
            //ya tiene asignada
            for (Sucursal sucursal : sucursales) {
                boolean seleccionada = sucursal.getIdSucursal() == ruta.getSucursalOrigen().getIdSucursal();
        %>
            <option value="<%= sucursal.getIdSucursal() %>" <%= seleccionada ? "selected" : "" %>>
                <%= sucursal.getNombre() %>
            </option>
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
                boolean seleccionada = sucursal.getIdSucursal() == ruta.getSucursalDestino().getIdSucursal();
        %>
            <option value="<%= sucursal.getIdSucursal() %>" <%= seleccionada ? "selected" : "" %>>
                <%= sucursal.getNombre() %>
            </option>
        <%
            }
        %>
    </select>
    <br>

    <label>Distancia (km):</label>
    <br>
    <input type="number" step="0.01" name="distanciaKm" value="<%= ruta.getDistanciaKm() %>" class="form-control" required/>
    <br>

    <label>Precio del Boleto:</label>
    <br>
    <input type="number" step="0.01" name="precioBoleto" value="<%= ruta.getPrecioBoleto() %>" class="form-control" required/>
    <br>

    <button type="submit" class="btn btn-primary">Guardar Cambios</button>
</form>
<a href="<%= request.getContextPath() %>/ruta?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
