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
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<a href="<%= request.getContextPath() %>/chofer?accion=nuevo" class="btn btn-primary mb-3">Registrar nuevo chofer</a>
<table class="table table-striped">
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
</table>

<%-- Formulario oculto para desactivar chofer, igual patron que Bus --%>
<form id="formDesactivar" method="POST" action="<%= request.getContextPath() %>/chofer" style="display:none;">
    <input type="hidden" name="accion" value="desactivar"/>
    <input type="hidden" id="idChoferDesactivar" name="id"/>
</form>
<script>
    function confirmarDesactivar(idChofer) {
        if (confirm("¿Está seguro de desactivar este chofer?")) {
            document.getElementById("idChoferDesactivar").value = idChofer;
            document.getElementById("formDesactivar").submit();
        }
    }
</script>

<%@ include file="/vistas/comunes/footer.jsp" %>
