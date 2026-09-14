<%-- 
    Document   : registrarSucursal
    Created on : 6/09/2026, 20:51:59
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Registrar Nueva Sucursal</h1>

<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<%-- Ayuda para el AdminSistema: como obtener las coordenadas reales
     de la ubicacion desde Google Maps, sin necesidad de saberlas
     de memoria --%>
<div class="alert alert-info">
    Para obtener la latitud y longitud: Busca la ubicación en
    <a href="https://www.google.com/maps" target="_blank">Google Maps</a>,
    haz clic derecho sobre el punto exacto, y copia las coordenadas
    que aparecen (ejemplo: 14.8443, -91.5198).
</div>

<form method="POST" action="<%= request.getContextPath() %>/sucursal">
    <input type="hidden" name="accion" value="registrar"/>

    <label>Nombre:</label>
    <br>
    <input type="text" name="nombre" class="form-control" required/>
    <br>

    <label>Ubicación:</label>
    <br>
    <input type="text" name="ubicacion" class="form-control" required/>
    <br>

    <label>Latitud:</label>
    <br>
    <input type="number" step="any" name="latitud" class="form-control" required/>
    <br>

    <label>Longitud:</label>
    <br>
    <input type="number" step="any" name="longitud" class="form-control" required/>
    <br>

    <button type="submit" class="btn btn-primary">Registrar Sucursal</button>
</form>

<a href="<%= request.getContextPath() %>/sucursal?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
