<%-- 
    Document   : exportarHTML
    Created on : 14/09/2026, 18:11:49
    Author     : eduar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    //Si viene el parametro exportar=true, se cambian los headers
    //para que el navegador descargue esta pagina como archivo HTML
    //en vez de solo mostrarla en pantalla
    if ("true".equals(request.getParameter("exportar"))) {
        String nombreArchivo = request.getParameter("nombreReporte");
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            nombreArchivo = "reporte";
        }
        response.setContentType("text/html");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + nombreArchivo + ".html\"");
    }
%>
