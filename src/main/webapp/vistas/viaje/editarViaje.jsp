<%-- 
    Document   : editarViaje
    Created on : 8/09/2026, 23:44:00
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Bus"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Chofer"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Ruta"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.ViajeRegular"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Editar Viaje</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>
<%
    //El Controlador ya busco el viaje por su ID y lo guardo en el request,
    //junto con las listas de bus, chofer y ruta para armar los combobox
    ViajeRegular viaje = (ViajeRegular) request.getAttribute("viaje");
    ArrayList<Bus> buses = (ArrayList<Bus>) request.getAttribute("buses");
    ArrayList<Chofer> choferes = (ArrayList<Chofer>) request.getAttribute("choferes");
    ArrayList<Ruta> rutas = (ArrayList<Ruta>) request.getAttribute("rutas");
%>
<%-- Nota: como se usa especificamente ViajeRegular, el tipo de viaje nunca puede cambiar aqui --%>
<form method="POST" action="<%= request.getContextPath() %>/viaje">
    <input type="hidden" name="accion" value="actualizar"/>
    <input type="hidden" name="idViaje" value="<%= viaje.getIdViaje() %>"/>

    <label>Bus:</label>
    <br>
    <select name="idBus" class="form-control" required>
        <%
            for (Bus bus : buses) {
                boolean seleccionado = bus.getIdBus() == viaje.getBus().getIdBus();
        %>
            <option value="<%= bus.getIdBus() %>" <%= seleccionado ? "selected" : "" %>>
                <%= bus.getPlaca() %> - <%= bus.getMarca() %>
            </option>
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
                boolean seleccionado = chofer.getIdUsuario() == viaje.getChofer().getIdUsuario();
        %>
            <option value="<%= chofer.getIdUsuario() %>" <%= seleccionado ? "selected" : "" %>>
                <%= chofer.getNombre() %>
            </option>
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
                boolean seleccionada = ruta.getIdRuta() == viaje.getRuta().getIdRuta();
        %>
            <option value="<%= ruta.getIdRuta() %>" <%= seleccionada ? "selected" : "" %>>
                <%= ruta.getSucursalOrigen().getNombre() %> - <%= ruta.getSucursalDestino().getNombre() %>
            </option>
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

    <button type="submit" class="btn btn-primary">Guardar Cambios</button>
</form>
<a href="<%= request.getContextPath() %>/viaje?accion=listar">Volver al listado</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
