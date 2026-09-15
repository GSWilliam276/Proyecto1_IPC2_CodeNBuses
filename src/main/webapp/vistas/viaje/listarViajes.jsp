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
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Locale"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Listado de Viajes</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
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

    //Formato de fecha y hora en español, ya que la hora de salida
    //y llegada estimada si son relevantes para un viaje
    SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy, HH:mm", new Locale("es", "ES"));
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
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
    <%//Se recorre la lista de viajes regulares que el Controlador
        //ya trajo de la base de datos
        ArrayList<ViajeRegular> viajes = (ArrayList<ViajeRegular>) request.getAttribute("viajes");
        if (viajes != null) {
            for (ViajeRegular viaje : viajes) {
                boolean yaTieneSalida = registroSalidaPersistencia.buscarPorViaje(viaje.getIdViaje()).isPresent();
                boolean yaTieneLlegada = registroLlegadaPersistencia.buscarPorViaje(viaje.getIdViaje()).isPresent();

                //Se determina el estado visual del viaje segun sus registros
                String estadoTexto;
                String estadoClase;
                if (yaTieneLlegada) {
                    estadoTexto = "Completado";
                    estadoClase = "bg-secondary";
                } else if (yaTieneSalida) {
                    estadoTexto = "En Tránsito";
                    estadoClase = "bg-warning text-dark";
                } else {
                    estadoTexto = "Programado";
                    estadoClase = "bg-primary";
                }
    %>
    <tr>
        <td><%= viaje.getRuta().getSucursalOrigen().getNombre() %> - <%= viaje.getRuta().getSucursalDestino().getNombre() %></td>
        <td><%= viaje.getBus().getPlaca() %></td>
        <td><%= viaje.getChofer().getCorreo() %></td>
        <td><%= formatoFechaHora.format(viaje.getFechaHoraSalida()) %></td>
        <td><%= formatoFechaHora.format(viaje.getFechaHoraLlegadaEstimada()) %></td>
        <td><span class="badge <%= estadoClase %>"><%= estadoTexto %></span></td>
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

        window.confirmarEliminar = function(idViaje) {
            idSeleccionado = idViaje;
            document.getElementById('modalConfirmarMensaje').innerText = "¿Está seguro de eliminar este viaje?";
            modalConfirmar.show();
        };

        document.getElementById('btnConfirmarAccion').addEventListener('click', function() {
            document.getElementById('idViajeEliminar').value = idSeleccionado;
            document.getElementById('formEliminar').submit();
        });
    });
</script>

<%@ include file="/vistas/comunes/footer.jsp" %>
