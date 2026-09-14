/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.controlador;

import com.usac.buses.proyectocodenbuses.entidad.*;
import com.usac.buses.proyectocodenbuses.persistencia.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ControladorReporte", urlPatterns = {"/reporte"})
public class ControladorReporte extends HttpServlet {

    private BusPersistencia busPersistencia = new BusPersistencia();
    private ChoferPersistencia choferPersistencia = new ChoferPersistencia();
    private BoletoPersistencia boletoPersistencia = new BoletoPersistencia();
    private ViajePrivadoPersistencia viajePrivadoPersistencia = new ViajePrivadoPersistencia();
    private GastoPersistencia gastoPersistencia = new GastoPersistencia();
    private ViajePersistencia viajePersistencia = new ViajePersistencia();
    private RegistroLlegadaPersistencia registroLlegadaPersistencia = new RegistroLlegadaPersistencia();
    private SucursalPersistencia sucursalPersistencia = new SucursalPersistencia();
    private RutaPersistencia rutaPersistencia = new RutaPersistencia();
    private ViajeRegularPersistencia viajeRegularPersistencia = new ViajeRegularPersistencia();

    private Usuario obtenerUsuarioSesion(HttpServletRequest request) {
        HttpSession sesion = request.getSession();
        return (Usuario) sesion.getAttribute("usuario");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            response.sendRedirect("usuario?accion=login");
            return;
        }

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "menu";
        }

        try {
            switch (accion) {
                case "menu":
                    request.getRequestDispatcher("/vistas/reporte/menuReportes.jsp").forward(request, response);
                    break;
                case "listadoBuses":
                    reporteListadoBuses(request, response, usuario);
                    break;
                case "listadoChoferes":
                    reporteListadoChoferes(request, response, usuario);
                    break;
                case "ingresosBoletos":
                    reporteIngresosBoletos(request, response, usuario);
                    break;
                case "ingresosAlquiler":
                    reporteIngresosAlquiler(request, response, usuario);
                    break;
                case "depreciacionPorBus":
                    reporteDepreciacionPorBus(request, response, usuario);
                    break;
                case "ganancias":
                    reporteGanancias(request, response, usuario);
                    break;
                case "rutasDemandadas":
                    reporteRutasMasDemandadas(request, response, usuario);
                    break;
                case "costosOperativos":
                    reporteCostosOperativos(request, response, usuario);
                    break;
                case "mapaRutas":
                    reporteMapaRutas(request, response, usuario);
                    break;
                default:
                    response.sendRedirect("reporte?accion=menu");
            }
        } catch (ParseException e) {
            request.setAttribute("error", "Formato de fecha inválido");
            request.getRequestDispatcher("/vistas/reporte/menuReportes.jsp").forward(request, response);
        }
    }

    //Reportes de AdminSurcursal
    private void reporteListadoBuses(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        if (!(usuario instanceof AdminSucursal)) {
            response.sendRedirect("reporte?accion=menu");
            return;
        }
        AdminSucursal admin = (AdminSucursal) usuario;
        ArrayList<Bus> buses = busPersistencia.listarPorSucursal(admin.getSucursal().getIdSucursal());

        //Se arma cada fila con la informacion extra que pide el enunciado:
        //chofer asignado actualmente y total de viajes realizados
        ArrayList<Object[]> filasReporte = new ArrayList<>();
        for (Bus bus : buses) {
            String choferActual = viajeRegularPersistencia.obtenerChoferActualPorBus(bus.getIdBus());
            int totalViajes = viajeRegularPersistencia.contarViajesPorBus(bus.getIdBus());
            filasReporte.add(new Object[]{bus, choferActual, totalViajes});
        }

        request.setAttribute("filasReporte", filasReporte);
        request.getRequestDispatcher("/vistas/reporte/listadoBuses.jsp").forward(request, response);
    }

    private void reporteListadoChoferes(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        if (!(usuario instanceof AdminSucursal)) {
            response.sendRedirect("reporte?accion=menu");
            return;
        }
        AdminSucursal admin = (AdminSucursal) usuario;
        ArrayList<Chofer> choferes = choferPersistencia.listarPorSucursal(admin.getSucursal().getIdSucursal());

        //Se arma cada fila con el total de viajes realizados
        ArrayList<Object[]> filasReporte = new ArrayList<>();
        for (Chofer chofer : choferes) {
            int totalViajes = viajeRegularPersistencia.contarViajesPorChofer(chofer.getIdUsuario());
            filasReporte.add(new Object[]{chofer, totalViajes});
        }

        request.setAttribute("filasReporte", filasReporte);
        request.getRequestDispatcher("/vistas/reporte/listadoChoferes.jsp").forward(request, response);
    }

    private void reporteIngresosBoletos(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException, ParseException {
        if (!(usuario instanceof AdminSucursal)) {
            response.sendRedirect("reporte?accion=menu");
            return;
        }
        AdminSucursal admin = (AdminSucursal) usuario;
        Date desde = obtenerFechaOTodas(request, "desde", true);
        Date hasta = obtenerFechaOTodas(request, "hasta", false);

        //Filtros opcionales de ruta y bus
        String idRutaParam = request.getParameter("idRuta");
        String idBusParam = request.getParameter("idBus");
        Integer idRutaFiltro = (idRutaParam != null && !idRutaParam.trim().isEmpty()) ? Integer.parseInt(idRutaParam) : null;
        Integer idBusFiltro = (idBusParam != null && !idBusParam.trim().isEmpty()) ? Integer.parseInt(idBusParam) : null;

        ArrayList<Object[]> filasReporte = boletoPersistencia.reporteAgrupadoPorViaje(
            admin.getSucursal().getIdSucursal(), desde, hasta, idRutaFiltro, idBusFiltro);

        //Se pasan las listas de rutas y buses de la sucursal, para armar
        //los combobox de filtro en el JSP
        request.setAttribute("rutas", rutaPersistencia.listarPorSucursal(admin.getSucursal().getIdSucursal()));
        request.setAttribute("buses", busPersistencia.listarPorSucursal(admin.getSucursal().getIdSucursal()));
        request.setAttribute("filasReporte", filasReporte);
        request.getRequestDispatcher("/vistas/reporte/ingresosBoletos.jsp").forward(request, response);
    }

    private void reporteIngresosAlquiler(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException, ParseException {
        if (!(usuario instanceof AdminSucursal)) {
            response.sendRedirect("reporte?accion=menu");
            return;
        }
        AdminSucursal admin = (AdminSucursal) usuario;
        Date desde = obtenerFechaOTodas(request, "desde", true);
        Date hasta = obtenerFechaOTodas(request, "hasta", false);

        ArrayList<ViajePrivado> alquileres = viajePrivadoPersistencia.listarPorSucursalYFecha(
            admin.getSucursal().getIdSucursal(), desde, hasta);
        request.setAttribute("alquileres", alquileres);
        request.getRequestDispatcher("/vistas/reporte/ingresosAlquiler.jsp").forward(request, response);
    }

    private void reporteDepreciacionPorBus(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        if (!(usuario instanceof AdminSucursal)) {
            response.sendRedirect("reporte?accion=menu");
            return;
        }
        AdminSucursal admin = (AdminSucursal) usuario;
        ArrayList<Bus> buses = busPersistencia.listarPorSucursal(admin.getSucursal().getIdSucursal());

        //Se calcula la depreciacion real acumulada de cada bus, sumando
        //lo que ya quedo guardado en cada RegistroLlegada (cada uno con
        //el monto que estaba vigente en su momento), no es una aproximacion
        //con el monto actual
        ArrayList<Object[]> filasReporte = new ArrayList<>();
        for (Bus bus : buses) {
            double depreciacionAcumulada = registroLlegadaPersistencia.obtenerDepreciacionAcumuladaPorBus(bus.getIdBus());
            filasReporte.add(new Object[]{bus.getPlaca(), bus.getKilometraje(), depreciacionAcumulada});
        }

        request.setAttribute("filasReporte", filasReporte);
        request.getRequestDispatcher("/vistas/reporte/depreciacionPorBus.jsp").forward(request, response);
    }

    //Reportes de AdminSistema

    //Reporte de ganancias: combina ingresos de boletos, ingresos de alquiler
    //y costos operativos (combustible + taller + depreciación) por sucursal,
    //ordenadas alfabeticamente. Opcionalmente filtrado por sucursal especifica
    private void reporteGanancias(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException, ParseException {
        if (!(usuario instanceof AdminSistema)) {
            response.sendRedirect("reporte?accion=menu");
            return;
        }

        Date desde = obtenerFechaOTodas(request, "desde", true);
        Date hasta = obtenerFechaOTodas(request, "hasta", false);
        String idSucursalParam = request.getParameter("idSucursal");

        //Lista COMPLETA de sucursales, siempre, para armar el combobox del filtro
        ArrayList<Sucursal> todasLasSucursales = sucursalPersistencia.listarTodos();
        todasLasSucursales.sort((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));

        //Lista para el REPORTE en si: si hay filtro, solo esa sucursal
        ArrayList<Sucursal> sucursalesParaReporte;
        if (idSucursalParam != null && !idSucursalParam.trim().isEmpty()) {
            ArrayList<Sucursal> filtro = new ArrayList<>();
            sucursalPersistencia.buscarPorId(Integer.parseInt(idSucursalParam)).ifPresent(filtro::add);
            sucursalesParaReporte = filtro;
        } else {
            sucursalesParaReporte = todasLasSucursales;
        }

        ArrayList<Object[]> filasReporte = new ArrayList<>();
        double totalIngresos = 0;
        double totalCostos = 0;

        for (Sucursal sucursal : sucursalesParaReporte) {
            int idSucursal = sucursal.getIdSucursal();

            double ingresosBoletos = boletoPersistencia.obtenerIngresosPorSucursalYFecha(idSucursal, desde, hasta);
            double ingresosAlquiler = viajePrivadoPersistencia.obtenerIngresosPorSucursalYFecha(idSucursal, desde, hasta);
            double costoCombustible = registroLlegadaPersistencia.obtenerCombustiblePorSucursalYFecha(idSucursal, desde, hasta);
            double costoTaller = gastoPersistencia.obtenerTotalPorSucursalYFecha(idSucursal, desde, hasta);
            double costoDepreciacion = registroLlegadaPersistencia.obtenerDepreciacionPorSucursalYFecha(idSucursal, desde, hasta);

            double ingresosTotales = ingresosBoletos + ingresosAlquiler;
            double costosTotales = costoCombustible + costoTaller + costoDepreciacion;
            double gananciaNeta = ingresosTotales - costosTotales;

            totalIngresos += ingresosTotales;
            totalCostos += costosTotales;

            filasReporte.add(new Object[]{
                sucursal.getNombre(), ingresosBoletos, ingresosAlquiler,
                costosTotales, gananciaNeta
            });
        }

        request.setAttribute("filasReporte", filasReporte);
        request.setAttribute("sucursales", todasLasSucursales);
        request.setAttribute("totalIngresos", totalIngresos);
        request.setAttribute("totalCostos", totalCostos);
        request.setAttribute("totalGanancia", totalIngresos - totalCostos);
        request.getRequestDispatcher("/vistas/reporte/ganancias.jsp").forward(request, response);
    }

    
    //Reporte de rutas mas demandadas en un intervalo de tiempo,
    //ordenadas de mayor a menor, cantidad de boletos vendidos
    private void reporteRutasMasDemandadas(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException, ParseException {
        if (!(usuario instanceof AdminSistema)) {
            response.sendRedirect("reporte?accion=menu");
            return;
        }

        Date desde = obtenerFechaOTodas(request, "desde", true);
        Date hasta = obtenerFechaOTodas(request, "hasta", false);

        ArrayList<Object[]> rutasDemandadas = viajePersistencia.listarRutasMasDemandadas(desde, hasta);
        request.setAttribute("rutasDemandadas", rutasDemandadas);
        request.getRequestDispatcher("/vistas/reporte/rutasDemandadas.jsp").forward(request, response);
    }

    
    //Reporte de costos operativos por sucursal: combustible, taller/repuestos
    //y depreciacion acumulada, con totales por categoria y gran total
    private void reporteCostosOperativos(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException, ParseException {
        if (!(usuario instanceof AdminSistema)) {
            response.sendRedirect("reporte?accion=menu");
            return;
        }

        Date desde = obtenerFechaOTodas(request, "desde", true);
        Date hasta = obtenerFechaOTodas(request, "hasta", false);
        String idSucursalParam = request.getParameter("idSucursal");

        //Lista COMPLETA para armar el combobox del filtro
        ArrayList<Sucursal> todasLasSucursales = sucursalPersistencia.listarTodos();
        todasLasSucursales.sort((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));

        //Lista para el REPORTE en si: si hay filtro, solo esa sucursal
        ArrayList<Sucursal> sucursalesParaReporte;
        if (idSucursalParam != null && !idSucursalParam.trim().isEmpty()) {
            ArrayList<Sucursal> filtro = new ArrayList<>();
            sucursalPersistencia.buscarPorId(Integer.parseInt(idSucursalParam)).ifPresent(filtro::add);
            sucursalesParaReporte = filtro;
        } else {
            sucursalesParaReporte = todasLasSucursales;
        }

        ArrayList<Object[]> filasReporte = new ArrayList<>();
        double totalCombustible = 0;
        double totalTaller = 0;
        double totalDepreciacion = 0;

        for (Sucursal sucursal : sucursalesParaReporte) {
            int idSucursal = sucursal.getIdSucursal();
            double combustible = registroLlegadaPersistencia.obtenerCombustiblePorSucursalYFecha(idSucursal, desde, hasta);
            double taller = gastoPersistencia.obtenerTotalPorSucursalYFecha(idSucursal, desde, hasta);
            double depreciacion = registroLlegadaPersistencia.obtenerDepreciacionPorSucursalYFecha(idSucursal, desde, hasta);

            totalCombustible += combustible;
            totalTaller += taller;
            totalDepreciacion += depreciacion;

            filasReporte.add(new Object[]{sucursal.getNombre(), combustible, taller, depreciacion});
        }

        request.setAttribute("filasReporte", filasReporte);
        request.setAttribute("sucursales", todasLasSucursales);
        request.setAttribute("totalCombustible", totalCombustible);
        request.setAttribute("totalTaller", totalTaller);
        request.setAttribute("totalDepreciacion", totalDepreciacion);
        request.setAttribute("granTotal", totalCombustible + totalTaller + totalDepreciacion);
        request.getRequestDispatcher("/vistas/reporte/costosOperativos.jsp").forward(request, response);
    }

    //Mapa de rutas filtrado por sucursal de origen (se grafica en el JSP)
    private void reporteMapaRutas(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        if (!(usuario instanceof AdminSistema)) {
            response.sendRedirect("reporte?accion=menu");
            return;
        }

        String idSucursalParam = request.getParameter("idSucursal");
        ArrayList<Sucursal> sucursales = sucursalPersistencia.listarTodos();
        request.setAttribute("sucursales", sucursales);

        if (idSucursalParam != null && !idSucursalParam.trim().isEmpty()) {
            int idSucursal = Integer.parseInt(idSucursalParam);
            ArrayList<Ruta> rutas = rutaPersistencia.listarPorSucursal(idSucursal);
            request.setAttribute("rutas", rutas);
        }

        request.getRequestDispatcher("/vistas/reporte/mapaRutas.jsp").forward(request, response);
    }

    private Date obtenerFechaOTodas(HttpServletRequest request, String parametro, boolean esInicio) throws ParseException {
        String fechaStr = request.getParameter(parametro);
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return esInicio ? new Date(0) : new Date();
        }
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        return formato.parse(fechaStr);
    }
}
