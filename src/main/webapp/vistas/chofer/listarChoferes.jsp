<%-- 
    Document   : listarChoferes
    Created on : 8/09/2026, 00:07:01
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Chofer"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Listado de Choferes</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>
<a href="<%= request.getContextPath() %>/chofer?accion=nuevo" class="btn btn-primary mb-3">Registrar nuevo chofer</a>
<table class="table table-striped">
    <thead>
        <tr>
            <th>Foto</th>
            <th>Nombre/Correo</th>
            <th>Licencia</th>
            <th>Tipo</th>
            <th>Vencimiento</th>
            <th>Salario Base</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
    <%
        //Se recorre la lista de choferes que el Controlador
        //ya trajo de la base de datos
        ArrayList<Chofer> choferes = (ArrayList<Chofer>) request.getAttribute("choferes");
        if (choferes != null) {
            for (Chofer chofer : choferes) {
    %>
    <tr>
        <td>
            <% if (chofer.getFoto() != null) { %>
                <img src="<%= request.getContextPath() %>/recursos/imagenes/<%= chofer.getFoto() %>" width="80"/>
            <% } else { %>
                Sin foto
            <% } %>
        </td>
        <td><%= chofer.getCorreo() %></td>
        <td><%= chofer.getNumeroLicencia() %></td>
        <td><%= chofer.getTipoLicencia() %></td>
        <td><%= chofer.getFechaVencimiento() %></td>
        <td><%= chofer.getSalarioBase() %></td>
        <td><%= chofer.isActivo() ? "Activo" : "Inactivo" %></td>
        <td>
            <a href="<%= request.getContextPath() %>/chofer?accion=editar&id=<%= chofer.getIdUsuario() %>" class="btn btn-sm btn-outline-primary">Editar</a>
            <%
                //Solo se muestra la opcion de desactivar si el chofer
                //sigue activo
                if (chofer.isActivo()) {
            %>
                <a href="#" class="btn btn-sm btn-outline-danger" onclick="confirmarDesactivar(<%= chofer.getIdUsuario() %>)">Desactivar</a>
            <% } %>
        </td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>

<%-- Formulario oculto para desactivar chofer, igual patron que Bus --%>
<form id="formDesactivar" method="POST" action="<%= request.getContextPath() %>/chofer" style="display:none;">
    <input type="hidden" name="accion" value="desactivar"/>
    <input type="hidden" id="idChoferDesactivar" name="id"/>
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

        window.confirmarDesactivar = function(idChofer) {
            idSeleccionado = idChofer;
            document.getElementById('modalConfirmarMensaje').innerText = "¿Está seguro de desactivar este chofer?";
            modalConfirmar.show();
        };

        document.getElementById('btnConfirmarAccion').addEventListener('click', function() {
            document.getElementById('idChoferDesactivar').value = idSeleccionado;
            document.getElementById('formDesactivar').submit();
        });
    });
</script>

<%@ include file="/vistas/comunes/footer.jsp" %>
