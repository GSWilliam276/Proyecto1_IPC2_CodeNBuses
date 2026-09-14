<%-- 
    Document   : registrarAdminSucursal
    Created on : 11/09/2026, 08:36:58
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Sucursal"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Registrar Administrador de Sucursal</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<%-- Los administradores de sucursal solo pueden ser
     creados por un administrador del sistema --%>
<form method="POST" action="<%= request.getContextPath() %>/admin">
    <input type="hidden" name="accion" value="registrar"/>

    <label>Nombre Completo:</label>
    <br>
    <input type="text" name="nombre" class="form-control" required/>
    <br>

    <label>NIT:</label>
    <br>
    <input type="text" name="nit" class="form-control"/>
    <br>

    <label>DPI:</label>
    <br>
    <input type="text" name="dpi" class="form-control" required/>
    <br>

    <label>Teléfono:</label>
    <br>
    <input type="text" name="telefono" class="form-control"/>
    <br>

    <label>Dirección:</label>
    <br>
    <input type="text" name="direccion" class="form-control"/>
    <br>

    <label>Correo:</label>
    <br>
    <input type="email" name="correo" class="form-control" required/>
    <br>

    <label>Contraseña:</label>
    <br>
    <input type="password" name="contrasena" class="form-control" required/>
    <br>

    <label>Sucursal a Administrar:</label>
    <br>
    <%
        //Se recupera la lista de sucursales para armar el combobox
        ArrayList<Sucursal> sucursales = (ArrayList<Sucursal>) request.getAttribute("sucursales");
    %>
    <% if (sucursales == null || sucursales.isEmpty()) { %>
        <p style="color: red;">No hay sucursales registradas. Registra una sucursal primero.</p>
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

    <button type="submit" class="btn btn-primary">Registrar Administrador</button>
</form>
<a href="<%= request.getContextPath() %>/admin?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
