<%-- 
    Document   : registrarLlegada
    Created on : 8/09/2026, 23:17:25
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Registrar Llegada de Viaje</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<%-- Al igual que el registro de salida, este dato es INMUTABLE
     y ademas dispara automaticamente el calculo de depreciacion
     del bus segun el kilometraje ingresado --%>
<div class="alert alert-warning">
    Atención: una vez registrada la llegada, estos datos no podrán modificarse.
    Se calculará automáticamente la depreciación del bus según el kilometraje.
    Verifique que los datos sean correctos antes de continuar.
</div>

<form method="POST" action="<%= request.getContextPath() %>/viaje" onsubmit="return confirmarEnvio();">
    <input type="hidden" name="accion" value="llegada"/>
    <input type="hidden" name="idViaje" value="<%= request.getAttribute("idViaje") %>"/>

    <label>Kilometraje de Llegada:</label>
    <br>
    <input type="number" step="0.01" name="kilometrajeLlegada" class="form-control" required/>
    <br>

    <label>Gasto en Combustible:</label>
    <br>
    <input type="number" step="0.01" name="gastoCombustible" class="form-control" required/>
    <br>

    <button type="submit" class="btn btn-primary">Registrar Llegada</button>
</form>
<a href="<%= request.getContextPath() %>/viaje?accion=listar">Volver al listado</a>

<script>
    function confirmarEnvio() {
        return confirm("¿Está seguro de los datos ingresados? No podrán modificarse una vez guardados.");
    }
</script>

<%@ include file="/vistas/comunes/footer.jsp" %>
