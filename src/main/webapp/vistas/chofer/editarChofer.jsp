<%-- 
    Document   : editarChofer
    Created on : 8/09/2026, 00:14:28
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Chofer"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Sucursal"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Editar Chofer</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>
<%
    //El Controlador ya busco el chofer por su ID y lo guardo en el request,
    //junto con la lista de sucursales para armar el combobox
    Chofer chofer = (Chofer) request.getAttribute("chofer");
    ArrayList<Sucursal> sucursales = (ArrayList<Sucursal>) request.getAttribute("sucursales");
%>
<%-- Aqui solo se editan los datos especificos del chofer
     (licencia, salario, sucursal) que gestiona el AdminSucursal.
     Los datos heredados de Usuario (nit, dpi, telefono, direccion)
     se editan por separado desde "Mi Perfil", ya que le pertenecen
     al propio usuario, no al AdminSucursal --%>
<form method="POST" action="<%= request.getContextPath() %>/chofer">
    <input type="hidden" name="accion" value="actualizar"/>
    <input type="hidden" name="idUsuario" value="<%= chofer.getIdUsuario() %>"/>

    <label>Número de Licencia:</label>
    <br>
    <input type="text" name="numeroLicencia" value="<%= chofer.getNumeroLicencia() %>" class="form-control" required/>
    <br>

    <label>Tipo de Licencia:</label>
    <br>
    <select name="tipoLicencia" class="form-control" required>
        <option value="A" <%= chofer.getTipoLicencia().name().equals("A") ? "selected" : "" %>>A - Carga pesada / Extraurbano de pasajeros</option>
        <option value="B" <%= chofer.getTipoLicencia().name().equals("B") ? "selected" : "" %>>B - Transporte colectivo / Carga liviana</option>
    </select>
    <br>

    <label>Fecha de Vencimiento de Licencia:</label>
    <br>
    <input type="date" name="fechaVencimiento" class="form-control" required/>
    <br>

    <label>Salario Base:</label>
    <br>
    <input type="number" step="0.01" name="salarioBase" value="<%= chofer.getSalarioBase() %>" class="form-control" required/>
    <br>

    <label>Sucursal:</label>
    <br>
    <select name="idSucursal" class="form-control" required>
        <%
            for (Sucursal sucursal : sucursales) {
                boolean seleccionada = sucursal.getIdSucursal() == chofer.getSucursal().getIdSucursal();
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
<a href="<%= request.getContextPath() %>/chofer?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
