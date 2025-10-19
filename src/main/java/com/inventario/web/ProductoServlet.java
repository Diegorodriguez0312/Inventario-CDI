package com.inventario.web;

import com.inventario.facade.ProductoFacade;
import com.inventario.model.Producto;
import com.inventario.cdi.MessageBean;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/productos")
public class ProductoServlet extends HttpServlet {

    @Inject
    private ProductoFacade facade;

    @Inject
    private MessageBean messageBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ✅ Listar productos
        req.setAttribute("productos", facade.listar());

        // ✅ Mostrar mensajes si existen
        if (messageBean.getMensaje() != null) {
            req.setAttribute("mensaje", messageBean.getMensaje());
            req.setAttribute("tipo", messageBean.getTipo());
            messageBean.limpiar(); // Limpia después de mostrarse
        }

        req.getRequestDispatcher("productos.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("accion");

        try {
            switch (accion) {
                case "crear": {
                    Producto nuevo = new Producto();
                    nuevo.setCodigo(req.getParameter("codigo"));
                    nuevo.setNombre(req.getParameter("nombre"));
                    nuevo.setCategoria(req.getParameter("categoria"));
                    nuevo.setPrecio(Double.parseDouble(req.getParameter("precio")));
                    nuevo.setStock(Integer.parseInt(req.getParameter("stock")));

                    facade.crear(nuevo);
                    messageBean.setTextoInfo("✅ Producto creado correctamente.");
                    break;
                }

                case "actualizar": {
                    Producto actualizar = new Producto();
                    actualizar.setId(Integer.parseInt(req.getParameter("id")));
                    actualizar.setCodigo(req.getParameter("codigo"));
                    actualizar.setNombre(req.getParameter("nombre"));
                    actualizar.setCategoria(req.getParameter("categoria"));
                    actualizar.setPrecio(Double.parseDouble(req.getParameter("precio")));
                    actualizar.setStock(Integer.parseInt(req.getParameter("stock")));

                    facade.actualizar(actualizar);
                    messageBean.setTextoInfo("✅ Producto actualizado correctamente.");
                    break;
                }

                case "eliminar": {
                    int id = Integer.parseInt(req.getParameter("id"));
                    facade.eliminar(id);
                    messageBean.setTextoInfo("🗑️ Producto eliminado correctamente.");
                    break;
                }

                default:
                    messageBean.setTextoError("⚠️ Acción no reconocida.");
                    break;
            }

        } catch (RuntimeException e) {
            messageBean.setTextoError("❌ " + e.getMessage());
        } catch (Exception e) {
            messageBean.setTextoError("⚠️ Error inesperado: " + e.getMessage());
        }

        // ✅ Redirección tipo PRG (Post-Redirect-Get)
        resp.sendRedirect(req.getContextPath() + "/productos");
    }
}
