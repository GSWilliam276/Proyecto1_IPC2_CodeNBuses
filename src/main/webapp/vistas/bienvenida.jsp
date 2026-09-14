<%-- 
    Document   : bienvenida
    Created on : 5/09/2026, 23:09:58
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<div class="bienvenida-contenedor">
    <div>
        <h1 class="bienvenida-titulo">Bienvenido a CodeNBuses</h1>
        <p class="bienvenida-texto">
            En CodeNBuses creemos que viajar debe ser simple y seguro. Por eso
            cuidamos cada detalle de tu recorrido, desde la salida hasta la
            llegada, para que solo te preocupes de disfrutar el camino.
        </p>

        <a href="<%= request.getContextPath() %>/usuario?accion=login">
            <button class="btn btn-primary">Iniciar Sesión</button>
        </a>

        <a href="<%= request.getContextPath() %>/usuario?accion=crearCuenta">
            <button class="btn btn-secondary">Crear Cuenta</button>
        </a>
    </div>

    <i class="bi bi-bus-front bienvenida-imagen"></i>
</div>

<%-- Tarjetas de servicios principales, para llenar el espacio y dar
     un vistazo rapido de que puede hacer el usuario en el sistema --%>
<div class="row mt-5 text-center">
    <div class="col-md-4">
        <div class="tarjeta-servicio">
            <i class="bi bi-ticket-perforated"></i>
            <h4>Compra de Boletos</h4>
            <p>Elige tu ruta y asiento en segundos</p>
        </div>
    </div>
    <div class="col-md-4">
        <div class="tarjeta-servicio">
            <i class="bi bi-people"></i>
            <h4>Alquiler Privado</h4>
            <p>Ideal para grupos y viajes de trabajo</p>
        </div>
    </div>
    <div class="col-md-4">
        <div class="tarjeta-servicio">
            <i class="bi bi-geo-alt"></i>
            <h4>Rutas por todo el país</h4>
            <p>Conectamos distintas regiones de Guatemala</p>
        </div>
    </div>
</div>

<%@ include file="/vistas/comunes/footer.jsp" %>
