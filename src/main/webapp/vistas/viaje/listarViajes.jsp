<%-- 
    Document   : listarViajes
    Created on : 8/09/2026, 22:58:47
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.ViajeRegular"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.AdminSucursal"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Usuario"%>
<%@page import="com.usac.buses.proyectocodenbuses.persistencia.RegistroSalidaPersistencia"%>
<%@page import="com.usac.buses.proyectocodenbuses.persistencia.RegistroLlegadaPersistencia"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Listado de Viajes</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<%
    //Se verifica el rol para decidir que acciones mostrar:
    //AdminSucursal puede editar/eliminar, ademas de registrar salida y llegada
    Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
    boolean esAdminSucursal = usuarioActual instanceof AdminSucursal;

    //Se usan para consultar, por cada viaje, si ya tiene registro
    //de salida y/o llegada, y asi ocultar los botones que ya no aplican
    RegistroSalidaPersistencia registroSalidaPersistencia = new RegistroSalidaPersistencia();
    RegistroLlegadaPersistencia registroLlegadaPersistencia = new RegistroLlegadaPersistencia();
%>
<% if (esAdminSucursal) { %>
    <a href="<%= request.getContextPath() %>/viaje?accion=nuevo" class="btn btn-primary mb-3">Registrar nuevo viaje</a>
<% } %>
<table class="table table-striped">
    <thead>
        <tr>
            <th>Ruta</th>
            <th>Bus</th>
            <th>Chofer</th>
            <th>Salida</th>
            <th>Llegada Estimada</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
    <%
        //Se recorre la lista de viajes regulares que el Controlador
        //ya trajo de la base de datos
        ArrayList<ViajeRegular> viajes = (ArrayList<ViajeRegular>) request.getAttribute("viajes");
        if (viajes != null) {
            for (ViajeRegular viaje : viajes) {
                boolean yaTieneSalida = registroSalidaPersistencia.buscarPorViaje(viaje.getIdViaje()).isPresent();
                boolean yaTieneLlegada = registroLlegadaPersistencia.buscarPorViaje(viaje.getIdViaje()).isPresent();
    %>
    <tr>
        <td><%= viaje.getRuta().getSucursalOrigen().getNombre() %> - <%= viaje.getRuta().getSucursalDestino().getNombre() %></td>
        <td><%= viaje.getBus().getPlaca() %></td>
        <td><%= viaje.getChofer().getCorreo() %></td>
        <td><%= viaje.getFechaHoraSalida() %></td>
        <td><%= viaje.getFechaHoraLlegadaEstimada() %></td>
        <td>
            <%
                //Editar y eliminar son exclusivos del AdminSucursal
                if (esAdminSucursal) {
            %>
                <a href="<%= request.getContextPath() %>/viaje?accion=editar&id=<%= viaje.getIdViaje() %>" class="btn btn-sm btn-outline-primary">Editar</a>
            <% } %>
            <%
                //Salida solo se muestra si aun no se ha registrado
                if (!yaTieneSalida) {
            %>
                <a href="<%= request.getContextPath() %>/viaje?accion=registrarSalida&idViaje=<%= viaje.getIdViaje() %>" class="btn btn-sm btn-outline-success">Salida</a>
            <% } %>
            <%
                //Llegada solo se muestra si ya salio pero aun no ha llegado
                if (yaTieneSalida && !yaTieneLlegada) {
            %>
                <a href="<%= request.getContextPath() %>/viaje?accion=registrarLlegada&idViaje=<%= viaje.getIdViaje() %>" class="btn btn-sm btn-outline-warning">Llegada</a>
            <% } %>
            <%
                if (esAdminSucursal) {
            %>
                <a href="#" class="btn btn-sm btn-outline-danger" onclick="confirmarEliminar(<%= viaje.getIdViaje() %>)">Eliminar</a>
            <% } %>
        </td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>

<%-- Formulario oculto para eliminar viaje. El Controlador ya valida
     internamente si tiene registro de salida antes de permitirlo --%>
<form id="formEliminar" method="POST" action="<%= request.getContextPath() %>/viaje" style="display:none;">
    <input type="hidden" name="accion" value="eliminar"/>
    <input type="hidden" id="idViajeEliminar" name="id"/>
</form>
<script>
    function confirmarEliminar(idViaje) {
        if (confirm("¿Está seguro de eliminar este viaje?")) {
            document.getElementById("idViajeEliminar").value = idViaje;
            document.getElementById("formEliminar").submit();
        }
    }
</script>

<%@ include file="/vistas/comunes/footer.jsp" %>
