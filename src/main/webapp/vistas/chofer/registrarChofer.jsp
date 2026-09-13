<%-- 
    Document   : registrarChofer
    Created on : 7/09/2026, 23:55:45
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Sucursal"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Registrar Nuevo Chofer</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<form method="POST" action="<%= request.getContextPath() %>/chofer" enctype="multipart/form-data">
    <input type="hidden" name="accion" value="registrar"/>

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

    <label>Número de Licencia:</label>
    <br>
    <input type="text" name="numeroLicencia" class="form-control" required/>
    <br>

    <label>Tipo de Licencia:</label>
    <br>
    <%-- Solo se permiten A y B: son las unicas que autorizan transporte
         colectivo/extraurbano de pasajeros con remuneracion, segun
         la clasificacion oficial de licencias en Guatemala --%>
    <select name="tipoLicencia" class="form-control" required>
        <option value="A">A - Carga pesada / Extraurbano de pasajeros</option>
        <option value="B">B - Transporte colectivo / Carga liviana</option>
    </select>
    <br>

    <label>Fecha de Vencimiento de Licencia:</label>
    <br>
    <input type="date" name="fechaVencimiento" class="form-control" required/>
    <br>

    <label>Salario Base:</label>
    <br>
    <input type="number" step="0.01" name="salarioBase" class="form-control" required/>
    <br>

    <label>Foto:</label>
    <br>
    <input type="file" name="foto" accept="image/*" class="form-control"/>
    <br>

    <label>Sucursal:</label>
    <br>
    <%
        //Se recupera la lista de sucursales para armar el combobox
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

    <button type="submit" class="btn btn-primary">Registrar Chofer</button>
</form>
<a href="<%= request.getContextPath() %>/chofer?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
