<%-- 
    Document   : registrarSalida
    Created on : 8/09/2026, 23:17:11
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Registrar Salida de Viaje</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<%-- Aviso importante: este registro es INMUTABLE una vez guardado,
     no se podra modificar ni eliminar. Por eso se pide confirmacion
     explicita antes de enviar el formulario --%>
<div class="alert alert-warning">
    Atención: una vez registrada la salida, estos datos no podrán modificarse.
    Verifique que sean correctos antes de continuar.
</div>

<form method="POST" action="<%= request.getContextPath() %>/viaje" onsubmit="return confirmarEnvio();">
    <input type="hidden" name="accion" value="salida"/>
    <input type="hidden" name="idViaje" value="<%= request.getAttribute("idViaje") %>"/>

    <label>Kilometraje de Salida:</label>
    <br>
    <input type="number" step="0.01" name="kilometrajeSalida" class="form-control" required/>
    <br>

    <button type="submit" class="btn btn-primary">Registrar Salida</button>
</form>
<a href="<%= request.getContextPath() %>/viaje?accion=listar">Volver al listado</a>

<script>
    //Modal de confirmacion antes de guardar, ya que el registro
    //de salida es inmutable y no se puede corregir despues
    function confirmarEnvio() {
        return confirm("¿Está seguro de los datos ingresados? No podrán modificarse una vez guardados.");
    }
</script>

<%@ include file="/vistas/comunes/footer.jsp" %>
