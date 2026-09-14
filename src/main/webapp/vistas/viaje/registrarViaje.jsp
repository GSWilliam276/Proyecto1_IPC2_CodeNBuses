<%-- 
    Document   : registrarViaje
    Created on : 8/09/2026, 23:30:49
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Bus"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Chofer"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Ruta"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Registrar Nuevo Viaje</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>
<%
    //Se recuperan las 3 listas necesarias para armar los combobox
    //de bus, chofer y ruta
    ArrayList<Bus> buses = (ArrayList<Bus>) request.getAttribute("buses");
    ArrayList<Chofer> choferes = (ArrayList<Chofer>) request.getAttribute("choferes");
    ArrayList<Ruta> rutas = (ArrayList<Ruta>) request.getAttribute("rutas");
%>
<form method="POST" action="<%= request.getContextPath() %>/viaje">
    <input type="hidden" name="accion" value="registrar"/>

    <label>Bus:</label>
    <br>
    <select name="idBus" class="form-control" required>
        <%
            for (Bus bus : buses) {
        %>
            <option value="<%= bus.getIdBus() %>"><%= bus.getPlaca() %> - <%= bus.getMarca() %></option>
        <%
            }
        %>
    </select>
    <br>

    <label>Chofer:</label>
    <br>
    <select name="idChofer" class="form-control" required>
        <%
            for (Chofer chofer : choferes) {
        %>
            <option value="<%= chofer.getIdUsuario() %>"><%= chofer.getCorreo() %></option>
        <%
            }
        %>
    </select>
    <br>

    <label>Ruta:</label>
    <br>
    <select name="idRuta" class="form-control" required>
        <%
            for (Ruta ruta : rutas) {
        %>
            <option value="<%= ruta.getIdRuta() %>"><%= ruta.getSucursalOrigen().getNombre() %> - <%= ruta.getSucursalDestino().getNombre() %></option>
        <%
            }
        %>
    </select>
    <br>

    <label>Fecha y Hora de Salida:</label>
    <br>
    <input type="datetime-local" name="fechaHoraSalida" class="form-control" required/>
    <br>

    <label>Fecha y Hora Estimada de Llegada:</label>
    <br>
    <input type="datetime-local" name="fechaHoraLlegadaEstimada" class="form-control" required/>
    <br>

    <button type="submit" class="btn btn-primary">Registrar Viaje</button>
</form>
<a href="<%= request.getContextPath() %>/viaje?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
