<%-- 
    Document   : mapaRutas
    Created on : 11/09/2026, 22:22:25
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Ruta"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Sucursal"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/vistas/comunes/header.jsp" %>

<h1>Mapa de Rutas</h1>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

<%-- Leaflet: libreria de mapas basada en OpenStreetMap --%>
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>

<%-- Filtro obligatorio por sucursal, tal como pide el enunciado --%>
<form method="GET" action="<%= request.getContextPath() %>/reporte">
    <input type="hidden" name="accion" value="mapaRutas"/>
    <label>Sucursal:</label>
    <select name="idSucursal" required>
        <option value="">Seleccione una sucursal</option>
        <%
            ArrayList<Sucursal> sucursales = (ArrayList<Sucursal>) request.getAttribute("sucursales");
            if (sucursales != null) {
                for (Sucursal sucursal : sucursales) {
        %>
            <option value="<%= sucursal.getIdSucursal() %>"><%= sucursal.getNombre() %></option>
        <%
                }
            }
        %>
    </select>
    <button type="submit" class="btn btn-secondary">Ver Rutas</button>
</form>
<br>

<%-- Contenedor del mapa: necesita altura definida para que Leaflet
     lo pueda dibujar correctamente --%>
<div id="mapa" style="height: 500px;"></div>

<script>
    //Se centra el mapa en Guatemala como punto de partida general
    var mapa = L.map('mapa').setView([14.6349, -90.5069], 7);

    //Capa base del mapa, con las imagenes que vienen de OpenStreetMap
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
    }).addTo(mapa);

    <%
        //Por cada ruta, se imprime codigo JavaScript que agrega los
        //marcadores de origen/destino y la linea que los conecta.
        //Los valores de latitud/longitud vienen de la base de datos,
        //impresos directamente dentro del script con scriptlets
        ArrayList<Ruta> rutas = (ArrayList<Ruta>) request.getAttribute("rutas");
        if (rutas != null) {
            for (Ruta ruta : rutas) {
                Sucursal origen = ruta.getSucursalOrigen();
                Sucursal destino = ruta.getSucursalDestino();
    %>
        L.marker([<%= origen.getLatitud() %>, <%= origen.getLongitud() %>])
            .addTo(mapa)
            .bindPopup("<%= origen.getNombre() %>");

        L.marker([<%= destino.getLatitud() %>, <%= destino.getLongitud() %>])
            .addTo(mapa)
            .bindPopup("<%= destino.getNombre() %>");

        L.polyline([
            [<%= origen.getLatitud() %>, <%= origen.getLongitud() %>],
            [<%= destino.getLatitud() %>, <%= destino.getLongitud() %>]
        ], {color: 'blue'}).addTo(mapa);
    <%
            }
        }
    %>
</script>

<a href="<%= request.getContextPath() %>/reporte?accion=menu">Volver al menú de reportes</a>

<%@ include file="/vistas/comunes/footer.jsp" %>
