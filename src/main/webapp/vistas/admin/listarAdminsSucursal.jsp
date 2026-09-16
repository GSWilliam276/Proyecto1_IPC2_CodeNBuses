<%-- 
    Document   : listarAdminsSucursal
    Created on : 10/09/2026, 23:31:03
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.AdminSucursal"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Administradores de Sucursal</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>
<a href="<%= request.getContextPath() %>/admin?accion=nuevo" class="btn btn-primary mb-3">Registrar nuevo administrador</a>
<table class="table table-striped">
    <thead>
        <tr>
            <th>Nombre</th>
            <th>Correo</th>
            <th>DPI</th>
            <th>Sucursal</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>

    <%
        //Se recorre la lista de administradores de sucursal
        //que el Controlador ya trajo de la base de datos
        ArrayList<AdminSucursal> admins = (ArrayList<AdminSucursal>) request.getAttribute("admins");
        if (admins != null) {
            for (AdminSucursal admin : admins) {
    %>
    <tr>
        <td><%= admin.getNombre() %></td>
        <td><%= admin.getCorreo() %></td>
        <td><%= admin.getDpi() %></td>
        <td><%= admin.getSucursal().getNombre() %></td>
        <td><%= admin.isActivo() ? "Activo" : "Inactivo" %></td>
        <td>
            <a href="<%= request.getContextPath() %>/admin?accion=editar&id=<%= admin.getIdUsuario() %>" class="btn btn-sm btn-outline-primary">Editar</a>
            <%
                //Se muestra "Desactivar" si esta activo, o "Reactivar"
                //si esta inactivo, nunca ambos a la vez
                if (admin.isActivo()) {
            %>
                <a href="#" class="btn btn-sm btn-outline-danger" onclick="confirmarDesactivar(<%= admin.getIdUsuario() %>)">Desactivar</a>
            <% } else { %>
                <a href="#" class="btn btn-sm btn-outline-success" onclick="confirmarReactivar(<%= admin.getIdUsuario() %>)">Reactivar</a>
            <% } %>
        </td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>

<%-- Formulario oculto para desactivar admin --%>
<form id="formDesactivar" method="POST" action="<%= request.getContextPath() %>/admin" style="display:none;">
    <input type="hidden" name="accion" value="desactivar"/>
    <input type="hidden" id="idAdminDesactivar" name="id"/>
</form>

<%-- Formulario oculto para reactivar admin --%>
<form id="formReactivar" method="POST" action="<%= request.getContextPath() %>/admin" style="display:none;">
    <input type="hidden" name="accion" value="reactivar"/>
    <input type="hidden" id="idAdminReactivar" name="id"/>
</form>

<%-- Modal personalizado de confirmacion, con la marca CodeNBuses --%>
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
    document.addEventListener('DOMContentLoaded', function() {
        var idSeleccionado = null;
        var accionSeleccionada = null;
        var modalConfirmar = new bootstrap.Modal(document.getElementById('modalConfirmar'));

        window.confirmarDesactivar = function(idAdmin) {
            idSeleccionado = idAdmin;
            accionSeleccionada = 'desactivar';
            document.getElementById('modalConfirmarMensaje').innerText = "¿Está seguro de desactivar este administrador?";
            modalConfirmar.show();
        };

        window.confirmarReactivar = function(idAdmin) {
            idSeleccionado = idAdmin;
            accionSeleccionada = 'reactivar';
            document.getElementById('modalConfirmarMensaje').innerText = "¿Está seguro de reactivar este administrador?";
            modalConfirmar.show();
        };

        document.getElementById('btnConfirmarAccion').addEventListener('click', function() {
            if (accionSeleccionada === 'desactivar') {
                document.getElementById('idAdminDesactivar').value = idSeleccionado;
                document.getElementById('formDesactivar').submit();
            } else if (accionSeleccionada === 'reactivar') {
                document.getElementById('idAdminReactivar').value = idSeleccionado;
                document.getElementById('formReactivar').submit();
            }
        });
    });
</script>

<%@ include file="/vistas/comunes/footer.jsp" %>
