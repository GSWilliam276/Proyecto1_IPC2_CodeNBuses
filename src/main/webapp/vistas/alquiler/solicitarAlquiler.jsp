<%-- 
    Document   : solicitarAlquiler
    Created on : 9/09/2026, 07:54:37
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Solicitar Alquiler Privado</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<%-- Nota: el precio se calcula automaticamente segun pasajeros y
     duracion estimada del viaje (ver ControladorAlquiler.calcularPrecioEstimado),
     el AdminSucursal lo confirmara o ajustara despues --%>
<div class="alert alert-info">
    El sistema calculará un precio estimado según la cantidad de pasajeros
    y la duración del viaje. El precio final será confirmado por un
    administrador de sucursal.
</div>

<form method="POST" action="<%= request.getContextPath() %>/alquiler">
    <input type="hidden" name="accion" value="solicitar"/>

    <label>Origen:</label>
    <br>
    <input type="text" name="origen" class="form-control" required/>
    <br>

    <label>Destino:</label>
    <br>
    <input type="text" name="destino" class="form-control" required/>
    <br>

    <label>Número de Pasajeros:</label>
    <br>
    <input type="number" name="pasajeros" class="form-control" required/>
    <br>

    <label>Fecha y Hora de Salida:</label>
    <br>
    <input type="datetime-local" name="fechaHoraSalida" class="form-control" required/>
    <br>

    <label>Fecha y Hora Estimada de Llegada:</label>
    <br>
    <input type="datetime-local" name="fechaHoraLlegadaEstimada" class="form-control" required/>
    <br>

    <button type="submit" class="btn btn-primary">Solicitar Alquiler</button>
</form>
<a href="<%= request.getContextPath() %>/alquiler?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
