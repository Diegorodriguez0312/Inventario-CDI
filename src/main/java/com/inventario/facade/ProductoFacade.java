package com.inventario.facade;

import com.inventario.cdi.MessageBean;
import com.inventario.model.Producto;
import com.inventario.persistence.ProductoDAO;
import com.inventario.domain.ValidadorProducto;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Named("productoFacade")
@RequestScoped //  Este bean vive solo durante una petición HTTP
public class ProductoFacade {

    @Inject
    private ProductoDAO dao;

    @Inject
    private ValidadorProducto validador;

    @Inject
    private MessageBean mensajeBean;

    /**
     * ✅ Lista todos los productos activos
     */
    public List<Producto> listar() {
        return dao.listarTodos();
    }

    /**
     * ✅ Crea un nuevo producto con validaciones
     */
    public void crear(Producto p) {
        try {
            // Validaciones de negocio
            validador.validarCodigo(p.getCodigo());
            validador.validarNombre(p.getNombre());
            validador.validarCategoria(p.getCategoria());
            validador.validarPrecio(p.getPrecio());
            validador.validarStock(p.getStock());

            // Verificación de duplicados
            if (dao.existePorCodigo(p.getCodigo())) {
                mensajeBean.setTextoError("❌ El código ya existe o el producto está inactivo.");
                return;
            }

            dao.insertar(p);
            mensajeBean.setTextoInfo("✅ Producto creado correctamente.");

        } catch (Exception e) {
            mensajeBean.setTextoError("⚠️ Error al crear producto: " + e.getMessage());
        }
    }

    /**
     * ✅ Actualiza un producto existente
     */
    public void actualizar(Producto p) {
        try {
            validador.validarNombre(p.getNombre());
            validador.validarCategoria(p.getCategoria());
            validador.validarPrecio(p.getPrecio());
            validador.validarStock(p.getStock());

            dao.actualizar(p);
            mensajeBean.setTextoInfo("✅ Producto actualizado correctamente.");

        } catch (Exception e) {
            mensajeBean.setTextoError("⚠️ Error al actualizar: " + e.getMessage());
        }
    }

    /**
     * ✅ Elimina (o desactiva) un producto
     */
    public void eliminar(int id) {
        try {
            dao.eliminar(id);
            mensajeBean.setTextoInfo("🗑️ Producto eliminado correctamente.");
        } catch (Exception e) {
            mensajeBean.setTextoError("⚠️ Error al eliminar: " + e.getMessage());
        }
    }
}
