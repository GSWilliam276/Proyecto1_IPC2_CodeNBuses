<%-- 
    Document   : perfil
    Created on : 6/09/2026, 12:59:51
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Usuario"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.AdminSistema"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.AdminSucursal"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Chofer"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.ClienteRegular"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");

    //Se traduce el nombre tecnico de la clase Java a un texto
    //amigable para el usuario final, en vez de mostrar "ClienteRegular",
    //"AdminSucursal", etc. tal cual
    String tipoUsuarioTexto;
    String iconoRol;
    if (usuario instanceof AdminSistema) {
        tipoUsuarioTexto = "Administrador del Sistema";
        iconoRol = "bi-shield-lock";
    } else if (usuario instanceof AdminSucursal) {
        tipoUsuarioTexto = "Administrador de Sucursal";
        iconoRol = "bi-building";
    } else if (usuario instanceof Chofer) {
        tipoUsuarioTexto = "Chofer";
        iconoRol = "bi-bus-front";
    } else {
        tipoUsuarioTexto = "Cliente";
        iconoRol = "bi-person";
    }
%>

<div class="d-flex align-items-center gap-3 mb-3">
    <i class="bi <%= iconoRol %>" style="font-size: 3rem; color: var(--azul-marino);"></i>
    <div>
        <h1 class="mb-0">Bienvenido, <%= usuario.getCorreo() %></h1>
        <p class="mb-0">Tipo de usuario: <%= tipoUsuarioTexto %></p>
    </div>
</div>

<h2>Mis datos</h2>
<p>NIT: <%= usuario.getNit() %></p>
<p>DPI: <%= usuario.getDpi() %></p>
<p>Teléfono: <%= usuario.getTelefono() %></p>
<p>Dirección: <%= usuario.getDireccion() %></p>

<hr>

<%-- Menu de opciones segun el rol del usuario en sesion --%>
<% if (usuario instanceof AdminSistema) { %>
    <h3>Administración del Sistema</h3>
    <a href="<%= request.getContextPath() %>/sucursal?accion=listar" class="btn btn-outline-primary">Gestionar Sucursales</a>
    <a href="<%= request.getContextPath() %>/admin?accion=listar" class="btn btn-outline-primary">Administradores de Sucursal</a>
    <a href="<%= request.getContextPath() %>/admin?accion=configurarDepreciacion" class="btn btn-outline-primary">Configurar Depreciación</a>
    <a href="<%= request.getContextPath() %>/reporte?accion=menu" class="btn btn-outline-primary">Reportes</a>
    <a href="<%= request.getContextPath() %>/alquiler?accion=listar" class="btn btn-outline-primary">Alquiler Privado</a>
<% } %>

<% if (usuario instanceof AdminSucursal) { %>
    <h3>Administración de Sucursal</h3>
    <a href="<%= request.getContextPath() %>/bus?accion=listar" class="btn btn-outline-primary">Gestionar Buses</a>
    <a href="<%= request.getContextPath() %>/chofer?accion=listar" class="btn btn-outline-primary">Gestionar Choferes</a>
    <a href="<%= request.getContextPath() %>/ruta?accion=listar" class="btn btn-outline-primary">Gestionar Rutas</a>
    <a href="<%= request.getContextPath() %>/viaje?accion=listar" class="btn btn-outline-primary">Gestionar Viajes</a>
    <a href="<%= request.getContextPath() %>/gasto?accion=listar" class="btn btn-outline-primary">Gastos de Taller</a>
    <a href="<%= request.getContextPath() %>/alquiler?accion=listar" class="btn btn-outline-primary">Alquileres</a>
    <a href="<%= request.getContextPath() %>/reporte?accion=menu" class="btn btn-outline-primary">Reportes</a>
<% } %>

<% if (usuario instanceof Chofer) { %>
    <h3>Mis Viajes</h3>
    <a href="<%= request.getContextPath() %>/viaje?accion=listar" class="btn btn-outline-primary">Ver Viajes</a>
    <a href="<%= request.getContextPath() %>/alquiler?accion=listar" class="btn btn-outline-primary">Alquiler Privado</a>
<% } %>

<% if (usuario instanceof ClienteRegular) { %>
    <h3>Servicios</h3>
    <a href="<%= request.getContextPath() %>/boleto?accion=buscarViajes" class="btn btn-outline-primary">Comprar Boleto</a>
    <a href="<%= request.getContextPath() %>/alquiler?accion=listar" class="btn btn-outline-primary">Alquiler Privado</a>
    <a href="<%= request.getContextPath() %>/cartera?accion=ver" class="btn btn-outline-primary">Mi Cartera</a>
<% } %>

<%@ include file="/vistas/comunes/footer.jsp" %>
