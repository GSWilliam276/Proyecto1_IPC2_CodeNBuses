<%-- 
    Document   : editarAdminSucursal
    Created on : 11/09/2026, 23:51:48
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.AdminSucursal"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Sucursal"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Editar Administrador de Sucursal</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<%
    //El Controlador ya busco el admin por su ID y lo guardo en el request,
    //junto con la lista de sucursales para armar el combobox
    AdminSucursal admin = (AdminSucursal) request.getAttribute("admin");
    ArrayList<Sucursal> sucursales = (ArrayList<Sucursal>) request.getAttribute("sucursales");
%>
<form method="POST" action="<%= request.getContextPath() %>/admin">
    <input type="hidden" name="accion" value="actualizar"/>
    <input type="hidden" name="idUsuario" value="<%= admin.getIdUsuario() %>"/>

    <label>DPI:</label>
    <br>
    <input type="text" name="dpi" value="<%= admin.getDpi() %>" class="form-control" required/>
    <br>

    <label>Teléfono:</label>
    <br>
    <input type="text" name="telefono" value="<%= admin.getTelefono() %>" class="form-control"/>
    <br>

    <label>Dirección:</label>
    <br>
    <input type="text" name="direccion" value="<%= admin.getDireccion() %>" class="form-control"/>
    <br>

    <label>Correo:</label>
    <br>
    <input type="email" name="correo" value="<%= admin.getCorreo() %>" class="form-control" required/>
    <br>

    <label>Sucursal:</label>
    <br>
    <select name="idSucursal" class="form-control" required>
        <%
            for (Sucursal sucursal : sucursales) {
                boolean seleccionada = sucursal.getIdSucursal() == admin.getSucursal().getIdSucursal();
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
<a href="<%= request.getContextPath() %>/admin?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
