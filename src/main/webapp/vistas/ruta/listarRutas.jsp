<%-- 
    Document   : listarRutas
    Created on : 8/09/2026, 00:17:27
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Ruta"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.AdminSucursal"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Usuario"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Listado de Rutas</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>
<%
    //Se verifica el rol del usuario en sesion para decidir si
    //mostrar las acciones de gestion, exclusivas de AdminSucursal
    Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
    boolean esAdminSucursal = usuarioActual instanceof AdminSucursal;
%>
<% if (esAdminSucursal) { %>
    <a href="<%= request.getContextPath() %>/ruta?accion=nuevo" class="btn btn-primary mb-3">Registrar nueva ruta</a>
<% } %>
<table class="table table-striped">
    <thead>
        <tr>
            <th>Origen</th>
            <th>Destino</th>
            <th>Distancia (km)</th>
            <th>Precio Boleto</th>
            <% if (esAdminSucursal) { %>
                <th>Acciones</th>
            <% } %>
        </tr>
    </thead>
    <tbody>
    <%
        //Se recorre la lista de rutas que el Controlador
        //ya trajo de la base de datos
        ArrayList<Ruta> rutas = (ArrayList<Ruta>) request.getAttribute("rutas");
        if (rutas != null) {
            for (Ruta ruta : rutas) {
    %>
    <tr>
        <td><%= ruta.getSucursalOrigen().getNombre() %></td>
        <td><%= ruta.getSucursalDestino().getNombre() %></td>
        <td><%= ruta.getDistanciaKm() %></td>
        <td><%= ruta.getPrecioBoleto() %></td>
        <%
            if (esAdminSucursal) {
        %>
        <td>
            <a href="<%= request.getContextPath() %>/ruta?accion=editar&id=<%= ruta.getIdRuta() %>" class="btn btn-sm btn-outline-primary">Editar</a>
            <a href="#" class="btn btn-sm btn-outline-danger" onclick="confirmarEliminar(<%= ruta.getIdRuta() %>)">Eliminar</a>
        </td>
        <% } %>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>

<%-- Formulario oculto para eliminar ruta. El Controlador ya valida
     internamente si tiene viajes asociados antes de permitirlo,
     lanzando ExcepcionRutaNoEliminable si corresponde --%>
<form id="formEliminar" method="POST" action="<%= request.getContextPath() %>/ruta" style="display:none;">
    <input type="hidden" name="accion" value="eliminar"/>
    <input type="hidden" id="idRutaEliminar" name="id"/>
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

        window.confirmarEliminar = function(idRuta) {
            idSeleccionado = idRuta;
            document.getElementById('modalConfirmarMensaje').innerText = "¿Está seguro de eliminar esta ruta?";
            modalConfirmar.show();
        };

        document.getElementById('btnConfirmarAccion').addEventListener('click', function() {
            document.getElementById('idRutaEliminar').value = idSeleccionado;
            document.getElementById('formEliminar').submit();
        });
    });
</script>

<%@ include file="/vistas/comunes/footer.jsp" %>
