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
<script>
    function confirmarEliminar(idRuta) {
        if (confirm("¿Está seguro de eliminar esta ruta?")) {
            document.getElementById("idRutaEliminar").value = idRuta;
            document.getElementById("formEliminar").submit();
        }
    }
</script>

<%@ include file="/vistas/comunes/footer.jsp" %>
