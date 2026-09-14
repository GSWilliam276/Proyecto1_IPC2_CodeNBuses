<%-- 
    Document   : registrarBus
    Created on : 6/09/2026, 20:18:42
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Sucursal"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Registrar Nuevo Bus</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>
<form method="POST" action="<%= request.getContextPath() %>/bus" enctype="multipart/form-data">
    <input type="hidden" name="accion" value="registrar"/>

    <label>Placa:</label>
    <br>
    <input type="text" name="placa" class="form-control" required/>
    <br>
    <label>Marca:</label>
    <br>
    <input type="text" name="marca" class="form-control" required/>
    <br>
    <label>Modelo:</label>
    <br>
    <input type="text" name="modelo" class="form-control" required/>
    <br>
    <label>Año:</label>
    <br>
    <input type="number" name="anio" class="form-control" required/>
    <br>
    <label>Capacidad:</label>
    <br>
    <input type="number" name="capacidad" class="form-control" required/>
    <br>

    <label>Foto:</label>
    <br>
    <input type="file" name="foto" accept="image/*" class="form-control"/>
    <br>

    <label>Sucursal:</label>
    <br>
    <%
        //Se recupera la lista de sucursales que el Controlador ya consultó
        //en la base de datos, para armar el combobox sin que el usuario
        //tenga que adivinar o memorizar un numero de ID
        ArrayList<Sucursal> sucursales = (ArrayList<Sucursal>) request.getAttribute("sucursales");
    %>
    <% if (sucursales == null || sucursales.isEmpty()) { %>
        <p style="color: red;">No hay sucursales registradas. Contacta al administrador del sistema.</p>
    <% } else { %>
        <select name="idSucursal" class="form-control" required>
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
    <button type="submit" class="btn btn-primary">Registrar Bus</button>
</form>
<a href="<%= request.getContextPath() %>/bus?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
