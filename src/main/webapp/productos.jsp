<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Gestión de Productos</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #f4f6f8;
                margin: 0;
                padding: 20px;
            }
            h2 {
                text-align: center;
                color: #333;
            }
            form {
                margin-bottom: 20px;
                text-align: center;
            }
            input, button {
                padding: 8px;
                margin: 4px;
                border: 1px solid #ccc;
                border-radius: 6px;
            }
            .mensaje {
                animation: aparecer 0.4s ease;
            }

            @keyframes aparecer {
                from {
                    opacity: 0;
                    transform: translateY(-10px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            button {
                background-color: #3498db;
                color: white;
                cursor: pointer;
                transition: 0.2s;
            }
            button:hover {
                background-color: #2980b9;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                background: white;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            }
            th {
                background: #3498db;
                color: white;
                padding: 10px;
            }
            td {
                text-align: center;
                padding: 8px;
                border-bottom: 1px solid #ddd;
            }
            tr:hover {
                background-color: #f1f1f1;
            }

            /* Mensajes */
            .mensaje {
                width: 80%;
                margin: 10px auto;
                padding: 12px;
                border-radius: 8px;
                font-weight: bold;
                text-align: center;
            }
            .mensaje.success {
                background-color: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
            }
            .mensaje.error {
                background-color: #f8d7da;
                color: #721c24;
                border: 1px solid #f5c6cb;
            }
        </style>
    </head>
    <body>
        <h2>Gestión de Productos</h2>

        <!-- ✅ Bloque de mensajes -->
        <c:if test="${not empty mensaje}">
            <div class="mensaje ${tipo}">
                ${mensaje}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/productos" method="post">
            <input type="hidden" name="accion" value="crear" />
            <input type="text" name="codigo" placeholder="Código" required />
            <input type="text" name="nombre" placeholder="Nombre" required />
            <input type="text" name="categoria" placeholder="Categoría" required />
            <input type="number" step="0.01" name="precio" placeholder="Precio" required />
            <input type="number" name="stock" placeholder="Stock" required />
            <button type="submit">Agregar</button>
        </form>

        <table>
            <thead>
                <tr>
                    <th>ID</th><th>Código</th><th>Nombre</th><th>Categoría</th><th>Precio</th><th>Stock</th><th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${productos}">
                    <tr>
                <form action="${pageContext.request.contextPath}/productos" method="post">
                    <input type="hidden" name="id" value="${p.id}" />
                    <td>${p.id}</td>
                    <td><input type="text" name="codigo" value="${p.codigo}" /></td>
                    <td><input type="text" name="nombre" value="${p.nombre}" /></td>
                    <td><input type="text" name="categoria" value="${p.categoria}" /></td>
                    <td><input type="number" step="0.01" name="precio" value="${p.precio}" /></td>
                    <td><input type="number" name="stock" value="${p.stock}" /></td>
                    <td>
                        <button type="submit" name="accion" value="actualizar">Actualizar</button>
                        <button type="submit" name="accion" value="eliminar">Eliminar</button>
                    </td>
                </form>
            </tr>
        </c:forEach>
    </tbody>
</table>
<script>
    // Espera 5 segundos (5000 ms) y oculta el mensaje
    setTimeout(() => {
        const mensaje = document.querySelector('.mensaje');
        if (mensaje) {
            mensaje.style.transition = "opacity 0.5s ease";
            mensaje.style.opacity = "0";
            setTimeout(() => mensaje.remove(), 500); // Lo quita del DOM después del efecto
        }
    }, 5000);
</script>

</body>
</html>
