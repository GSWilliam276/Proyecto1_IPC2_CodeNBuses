<%-- 
    Document   : listarBuses
    Created on : 6/09/2026, 19:20:22
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Bus"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Listado de Buses</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<a href="<%= request.getContextPath() %>/bus?accion=nuevo" class="btn btn-primary mb-3">Registrar nuevo bus</a>
<table class="table table-striped">
    <thead>
        <tr>
            <th>Foto</th>
            <th>Placa</th>
            <th>Marca</th>
            <th>Modelo</th>
            <th>Capacidad</th>
            <th>Estado</th>
            <th>Kilometraje</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
    <%
        //El Controlador ya consulto la base de datos y guardo la lista
        //completa de buses en el request bajo el nombre "buses".
        //Aqui solo se recupera y recorre para pintar una
        //fila <tr> por cada bus encontrado
        ArrayList<Bus> buses = (ArrayList<Bus>) request.getAttribute("buses");
        if (buses != null) {
            for (Bus bus : buses) {
    %>
    <tr>
        <td>
            <% if (bus.getFoto() != null) { %>
                <img src="<%= request.getContextPath() %>/recursos/imagenes/<%= bus.getFoto() %>" width="80"/>
            <% } else { %>
                Sin foto
            <% } %>
        </td>
        <td><%= bus.getPlaca() %></td>
        <td><%= bus.getMarca() %></td>
        <td><%= bus.getModelo() %></td>
        <td><%= bus.getCapacidad() %></td>
        <td><%= bus.getEstadoOperativo() %></td>
        <td><%= bus.getKilometraje() %></td>
        <td>
            <a href="<%= request.getContextPath() %>/bus?accion=editar&id=<%= bus.getIdBus() %>" class="btn btn-sm btn-outline-primary">Editar</a>
            <%
                //Solo se muestra la opcion de desactivar si el bus
                //sigue activo; uno ya desactivado no necesita este enlace
                if (bus.isActivo()) {
            %>
                <a href="#" class="btn btn-sm btn-outline-danger" onclick="confirmarDesactivar(<%= bus.getIdBus() %>)">Desactivar</a>
            <% } %>
        </td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>
<%-- Formulario oculto que se llena y envia por JavaScript cuando el
     usuario confirma que quiere desactivar un bus especifico --%>
<form id="formDesactivar" method="POST" action="<%= request.getContextPath() %>/bus" style="display:none;">
    <input type="hidden" name="accion" value="desactivar"/>
    <input type="hidden" id="idBusDesactivar" name="id"/>
</form>
<script>
    //Muestra un modal de confirmacion nativo del navegador antes de
    //desactivar el bus. Si el usuario acepta, rellena el ID en el
    //formulario oculto y lo envía por POST hacia el Controlador
    function confirmarDesactivar(idBus) {
        if (confirm("¿Estas seguro de desactivar este bus?")) {
            document.getElementById("idBusDesactivar").value = idBus;
            document.getElementById("formDesactivar").submit();
        }
    }
</script>

<%@ include file="/vistas/comunes/footer.jsp" %>
