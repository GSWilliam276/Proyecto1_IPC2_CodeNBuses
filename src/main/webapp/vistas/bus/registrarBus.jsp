<%-- 
    Document   : registrarBus
    Created on : 6/09/2026, 20:18:42
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.usac.buses.proyectocodenbuses.entidad.Sucursal"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
<head>
    <title>Registrar Bus - CodeNBuses</title>
</head>
<body>
    <h1>Registrar Nuevo Bus</h1>

    <% if (request.getAttribute("error") != null) { %>
        <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <form method="POST" action="<%= request.getContextPath() %>/bus">
        <input type="hidden" name="accion" value="registrar"/>

        <label>Placa:</label>
        <br>
        <input type="text" name="placa" required/>
        <br>

        <label>Marca:</label>
        <br>
        <input type="text" name="marca" required/>
        <br>

        <label>Modelo:</label>
        <br>
        <input type="text" name="modelo" required/>
        <br>

        <label>Año:</label>
        <br>
        <input type="number" name="anio" required/>
        <br>

        <label>Capacidad:</label>
        <br>
        <input type="number" name="capacidad" required/>
        <br>

        <%-- Por el momento se deja colocar la ruta de la imagen, luego se implementara la seleccion de imagenes --%> 
        <label>Foto (URL o nombre de archivo):</label>
        <br>
        <input type="text" name="foto"/>
        <br>

        <label>Sucursal:</label>
        <br>
        <%
            //Se recupera la lista de sucursales que el Controlador ya consultó
            //en la base de datos, para armar el combobox sin que el usuario
            //tenga que adivinar o memorizar un numero de ID
            ArrayList<Sucursal> sucursales = (ArrayList<Sucursal>) request.getAttribute("sucursales");
        %>
        <% if (sucursales == null || sucursales.isEmpty()) { %>
            <%-- Caso borde: si todavia no existe ninguna sucursal registrada,
                 no tiene sentido mostrar un combobox vacio. Avisamos al usuario
                 en vez de dejarlo con una lista sin opciones. --%>
            <p style="color: red;">No hay sucursales registradas. Contacta al administrador del sistema.</p>
        <% } else { %>
            <select name="idSucursal" required>
                <%
                    //Se recorre la lista y se genera una <option> por cada
                    //sucursal, mostrando el nombre pero enviando el ID real
                    for (Sucursal sucursal : sucursales) {
                %>
                    <option value="<%= sucursal.getIdSucursal() %>"><%= sucursal.getNombre() %></option>
                <%
                    }
                %>
            </select>
        <% } %>
        <br>

        <button type="submit">Registrar Bus</button>
    </form>

    <a href="<%= request.getContextPath() %>/bus?accion=listar">Volver al listado</a>
</body>
</html>
