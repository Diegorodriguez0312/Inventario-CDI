Proyecto Inventario CDI
Resumen de la investigación

Este proyecto implementa un sistema básico de gestión de productos utilizando Jakarta EE con CDI (Contexts and Dependency Injection), aplicando buenas prácticas de arquitectura en capas (modelo, fachada y web).

El objetivo fue refactorizar un proyecto tradicional basado en servlets y DAOs, integrando el uso de inyección de dependencias, Managed Beans y scopes de CDI para mejorar la modularidad, la reutilización y la mantenibilidad del código.

Se reemplazaron dependencias manuales por anotaciones de inyección (@Inject), eliminando la necesidad de crear objetos explícitamente dentro de los servlets o controladores.

## Explicación de los Scopes utilizados
@RequestScoped

Dónde se usó: En MessageBean.

Qué hace: Crea una nueva instancia del bean por cada solicitud HTTP (GET o POST).

Por qué se usó: Para que los mensajes mostrados al usuario solo existan durante la petición actual y no persistan en otras vistas.

@ApplicationScoped

Dónde podría usarse: En componentes que deban compartirse en toda la aplicación, como un servicio de configuración o cache de datos.

Qué hace: Mantiene una única instancia viva durante toda la ejecución de la aplicación.

Ejemplo práctico: Un ConfigBean o un LoggerBean que se use desde cualquier servlet.

@SessionScoped

Dónde podría aplicarse: En un bean de sesión de usuario (login).

Qué hace: Mantiene los datos mientras el usuario tenga la sesión activa.

Ejemplo práctico: Guardar los datos del usuario autenticado o su rol.

@Dependent

Qué hace: Es el scope predeterminado. El ciclo de vida depende del objeto que lo inyecta.

Uso: Ideal cuando un bean debe crearse y destruirse junto con el que lo contiene.

##Flujo general de funcionamiento

El usuario accede a la ruta /productos.

ProductoServlet usa ProductoFacade (inyectado con @Inject) para obtener la lista de productos.

El resultado se envía a productos.jsp, donde se muestran los datos.

Cuando el usuario crea, actualiza o elimina un producto, el servlet llama al método correspondiente de ProductoFacade.

MessageBean almacena mensajes de éxito o error, que se muestran en la vista.

##Fragmentos de código ilustrativos
Inyección de dependencias en el servlet:
@Inject
private ProductoFacade facade;

@Inject
private MessageBean messageBean;

Ejemplo de uso del MessageBean:
messageBean.setMensaje("✅ Producto creado correctamente.");
messageBean.setTipo("success");

Ejemplo de fachada con CDI:
@ApplicationScoped
public class ProductoFacade {

    @Inject
    private ProductoDAO dao;

    public List<Producto> listar() {
        return dao.findAll();
    }
}

🖼️ Capturas de funcionamiento

(las capturas las anexe a el pdf)


👨‍💻 Tecnologías utilizadas

Jakarta EE 10

CDI (jakarta.inject, jakarta.enterprise.context)

JSP / Servlets

GlassFish Server 7

Maven

Java 17

📄 Autores

-Diego Alexander Rodríguez Jaimes
-ivan david sanchez garcia
-wilson fernando gelves salazar
Proyecto académico – Ingeniería de Sistemas
Universidad / Curso: Programación Empresarial con Jakarta EE
