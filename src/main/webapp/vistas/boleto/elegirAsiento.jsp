<%-- 
    Document   : elegirAsiento
    Created on : 9/09/2026, 08:12:57
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.ViajeRegular"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Elegir Asiento</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>

<%
    //El Controlador ya trajo el viaje y la lista de asientos
    //ocupados para este viaje especifico
    ViajeRegular viaje = (ViajeRegular) request.getAttribute("viaje");
    ArrayList<Integer> asientosOcupados = (ArrayList<Integer>) request.getAttribute("asientosOcupados");
    int capacidad = viaje.getBus().getCapacidad();
%>

<p><strong>Ruta:</strong> <%= viaje.getRuta().getSucursalOrigen().getNombre() %> - <%= viaje.getRuta().getSucursalDestino().getNombre() %></p>
<p><strong>Precio:</strong> <%= viaje.getRuta().getPrecioBoleto() %></p>

<form method="POST" action="<%= request.getContextPath() %>/boleto">
    <input type="hidden" name="accion" value="comprar"/>
    <input type="hidden" name="idViaje" value="<%= viaje.getIdViaje() %>"/>

    <label>Seleccione un asiento disponible:</label>
    <br>
    <select name="numeroAsiento" class="form-control" required>
        <%
            //Se generan las opciones del 1 hasta la capacidad del bus,
            //omitiendo los asientos que ya aparecen en la lista de ocupados
            for (int i = 1; i <= capacidad; i++) {
                if (!asientosOcupados.contains(i)) {
        %>
                    <option value="<%= i %>">Asiento <%= i %></option>
        <%
                }
            }
        %>
    </select>
    <br>

    <button type="submit" class="btn btn-primary">Comprar Boleto</button>
</form>
<a href="<%= request.getContextPath() %>/boleto?accion=buscarViajes">Volver a viajes disponibles</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
