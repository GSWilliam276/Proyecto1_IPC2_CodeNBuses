/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.usac.buses.proyectocodenbuses.controlador;

import com.usac.buses.proyectocodenbuses.entidad.Cartera;
import com.usac.buses.proyectocodenbuses.entidad.ClienteRegular;
import com.usac.buses.proyectocodenbuses.entidad.Usuario;
import com.usac.buses.proyectocodenbuses.excepcion.ExcepcionFormatoInvalido;
import com.usac.buses.proyectocodenbuses.persistencia.CarteraPersistencia;
import com.usac.buses.proyectocodenbuses.persistencia.ClienteRegularPersistencia;
import com.usac.buses.proyectocodenbuses.persistencia.UsuarioPersistencia;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ControladorUsuario", urlPatterns = {"/usuario"})
public class ControladorUsuario extends HttpServlet {

    private UsuarioPersistencia usuarioPersistencia = new UsuarioPersistencia();
    private ClienteRegularPersistencia clienteRegularPersistencia = new ClienteRegularPersistencia();
    private CarteraPersistencia carteraPersistencia = new CarteraPersistencia();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "login";
        }

        switch (accion) {
            case "login":
                request.getRequestDispatcher("/vistas/usuario/login.jsp").forward(request, response);
                break;
            case "crearCuenta":
                request.getRequestDispatcher("/vistas/usuario/crearCuenta.jsp").forward(request, response);
                break;
            case "perfil":
                mostrarPerfil(request, response);
                break;
            case "logout":
                cerrarSesion(request, response);
                break;
            default:
                response.sendRedirect("usuario?accion=login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        switch (accion) {
            case "login":
                iniciarSesion(request, response);
                break;
            case "crearCuenta":
                crearCuenta(request, response);
                break;
            case "editarPerfil":
                editarPerfil(request, response);
                break;
            default:
                response.sendRedirect("usuario?accion=login");
        }
    }

    private void iniciarSesion(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");

        usuarioPersistencia.autenticar(correo, contrasena).ifPresentOrElse(
            usuario -> {
                HttpSession sesion = request.getSession();
                sesion.setAttribute("usuario", usuario);
                try {
                    response.sendRedirect("usuario?accion=perfil");
                } catch (IOException e) {
                    System.err.println("Error al redirigir: " + e.getMessage());
                }
            },
            () -> {
                request.setAttribute("error", "Correo o contraseña incorrectos");
                try {
                    request.getRequestDispatcher("/vistas/usuario/login.jsp").forward(request, response);
                } catch (ServletException | IOException e) {
                    System.err.println("Error al mostrar login: " + e.getMessage());
                }
            }
        );
    }

    private void crearCuenta(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String nit = request.getParameter("nit");
            String dpi = request.getParameter("dpi");
            String telefono = request.getParameter("telefono");
            String direccion = request.getParameter("direccion");
            String correo = request.getParameter("correo");
            String contrasena = request.getParameter("contrasena");

            if (dpi == null || dpi.trim().isEmpty()) {
                throw new ExcepcionFormatoInvalido("dpi", "El DPI es obligatorio");
            }
            if (!dpi.matches("\\d{13}")) {
                throw new ExcepcionFormatoInvalido("dpi", "El DPI debe contener exactamente 13 números");
            }
            //Validacion de DPI duplicado
            if (usuarioPersistencia.existeUsuarioConDpi(dpi)) {
                throw new ExcepcionFormatoInvalido("dpi", "Ya existe una cuenta registrada con ese DPI");
            }
            if (correo == null || correo.trim().isEmpty()) {
                throw new ExcepcionFormatoInvalido("correo", "El correo es obligatorio");
            }
            if (!nit.trim().isEmpty() && !nit.matches("\\d+")) {
                throw new ExcepcionFormatoInvalido("nit", "El NIT debe contener solo números");
            }

            //Validacion de correo duplicado
            if (usuarioPersistencia.existeUsuarioConCorreo(correo)) {
                throw new ExcepcionFormatoInvalido("correo", "Ya existe una cuenta registrada con ese correo");
            }

            ClienteRegular cliente = new ClienteRegular(nit, dpi, telefono, direccion, correo, contrasena);
            clienteRegularPersistencia.insertar(cliente);

            //Toda cuenta nueva inicia con su cartera en cero
            clienteRegularPersistencia.buscarPorCorreo(correo).ifPresent(clienteCreado -> {
                Cartera cartera = new Cartera(clienteCreado);
                carteraPersistencia.insertar(cartera);
            });

            response.sendRedirect("usuario?accion=login");

        } catch (ExcepcionFormatoInvalido e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/vistas/usuario/crearCuenta.jsp").forward(request, response);
        }
    }

    private void mostrarPerfil(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("usuario?accion=login");
            return;
        }

        request.setAttribute("usuario", usuario);
        request.getRequestDispatcher("/vistas/usuario/perfil.jsp").forward(request, response);
    }

    private void editarPerfil(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("usuario?accion=login");
            return;
        }

        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");

        usuarioPersistencia.editarPerfil(usuario.getIdUsuario(), telefono, direccion);

        //Actualiza tambien el objeto en sesion para que se refleje sin necesidad de volver a loguear
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);
        sesion.setAttribute("usuario", usuario);

        response.sendRedirect("usuario?accion=perfil");
    }

    private void cerrarSesion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession sesion = request.getSession(false);
        if (sesion != null) {
            sesion.invalidate();
        }
        response.sendRedirect("vistas/bienvenida.jsp");
    }
}
