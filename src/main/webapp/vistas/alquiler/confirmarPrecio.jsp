<%-- 
    Document   : confirmarPrecio
    Created on : 9/09/2026, 08:00:53
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.ViajePrivado"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Locale"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Confirmar Precio de Alquiler</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<%
    //El Controlador ya busco el viaje privado por su ID y lo guardo
    //en el request para mostrar los datos de la solicitud
    ViajePrivado viaje = (ViajePrivado) request.getAttribute("viaje");

    //Formato de fecha y hora en español
    SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy, HH:mm", new Locale("es", "ES"));
%>

<p><strong>Origen:</strong> <%= viaje.getOrigen() %></p>
<p><strong>Destino:</strong> <%= viaje.getDestino() %></p>
<p><strong>Pasajeros:</strong> <%= viaje.getPasajeros() %></p>
<p><strong>Fecha de Salida:</strong> <%= formatoFechaHora.format(viaje.getFechaHoraSalida()) %></p>
<p><strong>Precio Estimado:</strong> <%= viaje.getPrecioEstimado() %></p>

<%-- El AdminSucursal puede aceptar el precio estimado tal cual,
     o cambiarlo si lo considera necesario --%>
<form method="POST" action="<%= request.getContextPath() %>/alquiler">
    <input type="hidden" name="accion" value="confirmarPrecio"/>
    <input type="hidden" name="idViaje" value="<%= viaje.getIdViaje() %>"/>

    <label>Precio Confirmado:</label>
    <br>
    <input type="number" step="0.01" name="precioConfirmado" value="<%= viaje.getPrecioEstimado() %>" class="form-control" required/>
    <br>

    <button type="submit" class="btn btn-primary">Confirmar Precio</button>
</form>
<a href="<%= request.getContextPath() %>/alquiler?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
