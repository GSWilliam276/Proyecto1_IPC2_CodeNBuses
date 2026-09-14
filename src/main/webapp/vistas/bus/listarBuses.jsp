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
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
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

<%-- Modal personalizado de confirmacion, con la marca CodeNBuses
     en vez del generico "localhost dice" del confirm() nativo --%>
<div class="modal fade" id="modalConfirmar" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="background-color: var(--azul-marino); color: white;">
                <h5 class="modal-title">CodeNBuses</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p id="modalConfirmarMensaje"></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-danger" id="btnConfirmarAccion">Confirmar</button>
            </div>
        </div>
    </div>
</div>

<script>
    //Se usa un modal personalizado de Bootstrap en vez del confirm()
    //nativo del navegador, para mostrar la marca CodeNBuses en vez
    //del generico "localhost dice". Se espera a que el HTML termine
    //de cargar (DOMContentLoaded) antes de crear el modal
    document.addEventListener('DOMContentLoaded', function() {
        var idSeleccionado = null;
        var modalConfirmar = new bootstrap.Modal(document.getElementById('modalConfirmar'));

        window.confirmarDesactivar = function(idBus) {
            idSeleccionado = idBus;
            document.getElementById('modalConfirmarMensaje').innerText = "¿Estás seguro de desactivar este bus?";
            modalConfirmar.show();
        };

        document.getElementById('btnConfirmarAccion').addEventListener('click', function() {
            document.getElementById('idBusDesactivar').value = idSeleccionado;
            document.getElementById('formDesactivar').submit();
        });
    });
</script>

<%@ include file="/vistas/comunes/footer.jsp" %>
