<%-- 
    Document   : perfil
    Created on : 6/09/2026, 12:59:51
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Usuario"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
%>

<h1>Bienvenido, <%= usuario.getCorreo() %></h1>

<p>Tipo de usuario: <%= usuario.getClass().getSimpleName() %></p>

<h2>Mis datos</h2>
<p>NIT: <%= usuario.getNit() %></p>
<p>DPI: <%= usuario.getDpi() %></p>
<p>Teléfono: <%= usuario.getTelefono() %></p>
<p>Dirección: <%= usuario.getDireccion() %></p>

<%@ include file="/vistas/comunes/footer.jsp" %>
