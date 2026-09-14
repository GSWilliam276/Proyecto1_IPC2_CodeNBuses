<%-- 
    Document   : editarBus
    Created on : 6/09/2026, 20:32:43
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Bus"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Sucursal"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Editar Bus</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>
<%
    //El Controlador ya busco el bus por su ID y lo guardo en el request,
    //junto con la lista de sucursales para armar el combobox
    Bus bus = (Bus) request.getAttribute("bus");
    ArrayList<Sucursal> sucursales = (ArrayList<Sucursal>) request.getAttribute("sucursales");
%>
<form method="POST" action="<%= request.getContextPath() %>/bus">
    <input type="hidden" name="accion" value="actualizar"/>
    <input type="hidden" name="idBus" value="<%= bus.getIdBus() %>"/>
    <label>Placa:</label>
    <br>
    <input type="text" name="placa" value="<%= bus.getPlaca() %>" class="form-control" required/>
    <br>
    <label>Marca:</label>
    <br>
    <input type="text" name="marca" value="<%= bus.getMarca() %>" class="form-control" required/>
    <br>
    <label>Modelo:</label>
    <br>
    <input type="text" name="modelo" value="<%= bus.getModelo() %>" class="form-control" required/>
    <br>
    <label>Año:</label>
    <br>
    <input type="number" name="anio" value="<%= bus.getAnio() %>" class="form-control" required/>
    <br>
    <label>Capacidad:</label>
    <br>
    <input type="number" name="capacidad" value="<%= bus.getCapacidad() %>" class="form-control" required/>
    <br>
    <label>Sucursal:</label>
    <br>
    <select name="idSucursal" class="form-control" required>
        <%
            //Se marca como "selected" la sucursal que el bus ya tiene
            //asignada, para que el combobox no aparezca vacio al editar
            for (Sucursal sucursal : sucursales) {
                boolean seleccionada = sucursal.getIdSucursal() == bus.getSucursal().getIdSucursal();
        %>
            <option value="<%= sucursal.getIdSucursal() %>" <%= seleccionada ? "selected" : "" %>>
                <%= sucursal.getNombre() %>
            </option>
        <%
            }
        %>
    </select>
    <br>
    <button type="submit" class="btn btn-primary">Guardar Cambios</button>
</form>
<a href="<%= request.getContextPath() %>/bus?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
