package com.inventario.persistence;

import com.inventario.model.Producto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.sql.*;
import java.util.*;
import javax.sql.DataSource;

@ApplicationScoped
public class ProductoDAO {

    @Inject
    private DataSource ds; // provisto por DataSourceProducer

    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE activo = 1";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setCategoria(rs.getString("categoria"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                p.setActivo(rs.getBoolean("activo"));
                lista.add(p);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos: " + e.getMessage(), e);
        }
        return lista;
    }

    public boolean existePorCodigo(String codigo) {
        String sql = "SELECT COUNT(*) FROM productos WHERE codigo = ?";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar código: " + e.getMessage(), e);
        }
        return false;
    }

    public void insertar(Producto p) {
        // si ya existe un producto activo con ese código, lanzar excepción controlada
        if (existePorCodigo(p.getCodigo())) {
            throw new RuntimeException("Ya existe un producto activo con ese código.");
        }

        String sql = "INSERT INTO productos (codigo, nombre, categoria, precio, stock, activo) VALUES (?, ?, ?, ?, ?, 1)";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.executeUpdate();
        } catch (SQLException e) {
            // detectar duplicate por si existe inactivo con constraint compuesta (si aplica)
            if (e.getMessage().contains("Duplicate")) {
                throw new RuntimeException("Código duplicado (constraint).");
            }
            throw new RuntimeException("Error al insertar producto: " + e.getMessage(), e);
        }
    }

    public void actualizar(Producto p) {
        String sql = "UPDATE productos SET codigo=?, nombre=?, categoria=?, precio=?, stock=? WHERE id=?";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setInt(6, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        String sql = "UPDATE productos SET activo = 0 WHERE id = ?";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto: " + e.getMessage(), e);
        }
    }
}
