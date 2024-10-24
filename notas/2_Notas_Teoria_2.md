### Inyección de Campo (Field Injection)

La **inyección de campo** es un tipo de inyección de dependencias en Spring que permite la inyección directa de un bean en una declaración de campo, sin necesidad de utilizar un constructor o un método setter. Esta forma de inyección es bastante simple y concisa, ya que permite a los desarrolladores declarar directamente la dependencia en el campo de la clase, lo que a menudo puede parecer más limpio.

### Características de la Inyección de Campo

1. **Simplicidad**: La inyección de campo reduce la cantidad de código que debes escribir, ya que no necesitas crear un constructor o un método setter para la inyección.
   
2. **Acceso Directo**: Permite el acceso inmediato a la dependencia desde el campo donde se ha inyectado.

3. **Menor Verbosidad**: Menos líneas de código en comparación con la inyección a través de constructores o setters.

### Desventajas de la Inyección de Campo

- **Dificultad para las Pruebas**: La inyección de campo puede dificultar las pruebas de los componentes de manera aislada. Esto se debe a que no puedes inyectar dependencias en un objeto de prueba sin un contenedor de Spring, lo que limita la capacidad de realizar pruebas unitarias efectivas.
  
- **Acoplamiento Alto**: Al inyectar directamente en los campos, la clase se acopla más estrechamente al contenedor de Spring, lo que puede dificultar el mantenimiento y la flexibilidad del código.

### Ejemplo de Inyección de Campo

Veamos un ejemplo simple para ilustrar la inyección de campo en una clase de servicio que utiliza un repositorio.

#### Paso 1: Definir la Interfaz del Repositorio

Primero, definimos una interfaz de repositorio.

```java
public interface UsuarioRepositorio {
    void guardarUsuario(String nombre);
}
```

#### Paso 2: Implementar el Repositorio

Luego, creamos una implementación del repositorio.

```java
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepositorioImpl implements UsuarioRepositorio {
    @Override
    public void guardarUsuario(String nombre) {
        System.out.println("Usuario " + nombre + " guardado.");
    }
}
```

#### Paso 3: Usar la Inyección de Campo en un Servicio

Ahora, creamos un servicio que utiliza el repositorio, usando la inyección de campo.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {
    @Autowired // Inyección de campo
    private UsuarioRepositorio usuarioRepositorio;

    public void registrarUsuario(String nombre) {
        usuarioRepositorio.guardarUsuario(nombre);
    }
}
```

### Explicación del Código

1. **Interfaz `UsuarioRepositorio`**: Define el método `guardarUsuario` para guardar un usuario.
   
2. **Implementación `UsuarioRepositorioImpl`**: Proporciona la lógica para guardar un usuario en la consola.
   
3. **Servicio `UsuarioServicio`**: Utiliza la inyección de campo para declarar la dependencia de `UsuarioRepositorio`. La anotación `@Autowired` se utiliza para indicar a Spring que debe inyectar una instancia de `UsuarioRepositorioImpl` en el campo `usuarioRepositorio`.

Aunque la inyección de campo es una forma rápida y fácil de inyectar dependencias en Spring, se recomienda utilizarla con precaución. Dado que puede dificultar las pruebas unitarias y aumentar el acoplamiento, es más apropiada para situaciones de prueba o cuando la simplicidad del código es más crítica que la testabilidad. En aplicaciones más grandes y complejas, es preferible optar por la inyección a través de constructores o métodos setter para mantener un diseño más limpio y flexible.

## Inyección de Métodos (Method Injection)

La **inyección de métodos** es un tipo de inyección de dependencias en Spring que permite establecer una o varias dependencias a través de un método específico en la clase. A diferencia de la inyección de campo o de constructor, la inyección de métodos proporciona una forma más explícita de recibir y configurar dependencias, lo que puede ser útil para realizar trabajos de inicialización adicionales si es necesario.

### Características de la Inyección de Métodos

1. **Flexibilidad**: Permite inyectar múltiples dependencias a través de un solo método, lo que puede ser útil cuando necesitas inicializar el objeto de una manera más compleja.

2. **Inicialización Personalizada**: Al utilizar un método para la inyección, puedes realizar trabajos adicionales de inicialización después de recibir las dependencias, lo que te permite personalizar el comportamiento del objeto de acuerdo a las dependencias que recibe.

3. **Control**: Ofrece más control sobre el proceso de inyección, ya que puedes manejar el flujo de inicialización de manera más explícita.

### Desventajas de la Inyección de Métodos

- **Complejidad**: Aunque ofrece flexibilidad, puede aumentar la complejidad del código al requerir un método adicional para manejar la inyección de dependencias.

- **Menor Popularidad**: No es tan común como la inyección de campo o constructor, lo que puede llevar a menos familiaridad entre los desarrolladores.

### Ejemplo de Inyección de Métodos

Veamos un ejemplo en el que una clase de servicio utiliza la inyección de métodos para recibir sus dependencias.

#### Paso 1: Definir la Interfaz del Repositorio

Primero, definimos una interfaz de repositorio.

```java
public interface ProductoRepositorio {
    void guardarProducto(String nombre);
}
```

#### Paso 2: Implementar el Repositorio

Luego, creamos una implementación del repositorio.

```java
import org.springframework.stereotype.Repository;

@Repository
public class ProductoRepositorioImpl implements ProductoRepositorio {
    @Override
    public void guardarProducto(String nombre) {
        System.out.println("Producto " + nombre + " guardado.");
    }
}
```

#### Paso 3: Usar la Inyección de Métodos en un Servicio

Ahora, creamos un servicio que utiliza la inyección de métodos para recibir su repositorio.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoServicio {
    private ProductoRepositorio productoRepositorio;

    // Método para la inyección de dependencias
    @Autowired
    public void setProductoRepositorio(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
        // Realizar trabajo de inicialización si es necesario
        System.out.println("ProductoRepositorio inyectado.");
    }

    public void registrarProducto(String nombre) {
        productoRepositorio.guardarProducto(nombre);
    }
}
```

### Explicación del Código

1. **Interfaz `ProductoRepositorio`**: Define el método `guardarProducto` para guardar un producto.
   
2. **Implementación `ProductoRepositorioImpl`**: Proporciona la lógica para guardar un producto en la consola.
   
3. **Servicio `ProductoServicio`**: Utiliza la inyección de métodos para recibir una instancia de `ProductoRepositorio`. La anotación `@Autowired` se utiliza en el método `setProductoRepositorio`, lo que permite a Spring inyectar la dependencia cuando se crea el bean. Este método también puede realizar trabajo adicional de inicialización si es necesario.

## Inyección de Setter (Setter Injection)

La **inyección de setter** es un tipo de inyección de dependencias en Spring que sigue la convención de nombres de JavaBeans para inyectar dependencias a través de métodos setter. Esta técnica permite establecer las dependencias de un bean después de que ha sido creado, lo que proporciona flexibilidad y la posibilidad de modificar las dependencias en tiempo de ejecución.

### Características de la Inyección de Setter

1. **Conformidad con JavaBeans**: Sigue la convención de nombres de JavaBeans, donde los métodos que establecen propiedades comienzan con "set" seguido del nombre de la propiedad. Esto permite que el contenedor de Spring reconozca automáticamente los métodos como candidatos para la inyección.

2. **Flexibilidad**: Permite que las dependencias se cambien en cualquier momento después de la creación del bean, lo que proporciona un nivel adicional de flexibilidad.

3. **Facilidad de Configuración**: La inyección de setter es fácil de configurar y entender, lo que la hace una opción popular para muchos desarrolladores.

### Desventajas de la Inyección de Setter

- **Dependencias Opcionales**: Las dependencias inyectadas a través de setters pueden ser opcionales, lo que puede dar lugar a errores si el desarrollador olvida inyectar una dependencia necesaria.

- **Mayor Verbosidad**: Puede resultar en un código más verboso, ya que se requieren métodos setter adicionales para cada dependencia.

### Ejemplo de Inyección de Setter

Veamos un ejemplo simple donde una clase de servicio utiliza la inyección de setter para recibir una dependencia.

#### Paso 1: Definir la Interfaz del Repositorio

Primero, definimos una interfaz de repositorio.

```java
public interface ClienteRepositorio {
    void guardarCliente(String nombre);
}
```

#### Paso 2: Implementar el Repositorio

Luego, creamos una implementación del repositorio.

```java
import org.springframework.stereotype.Repository;

@Repository
public class ClienteRepositorioImpl implements ClienteRepositorio {
    @Override
    public void guardarCliente(String nombre) {
        System.out.println("Cliente " + nombre + " guardado.");
    }
}
```

#### Paso 3: Usar la Inyección de Setter en un Servicio

Ahora, creamos un servicio que utiliza la inyección de setter para recibir su repositorio.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServicio {
    private ClienteRepositorio clienteRepositorio;

    // Método setter para la inyección de dependencias
    @Autowired
    public void setClienteRepositorio(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    public void registrarCliente(String nombre) {
        clienteRepositorio.guardarCliente(nombre);
    }
}
```

### Explicación del Código

1. **Interfaz `ClienteRepositorio`**: Define el método `guardarCliente` para guardar un cliente.
   
2. **Implementación `ClienteRepositorioImpl`**: Proporciona la lógica para guardar un cliente en la consola.
   
3. **Servicio `ClienteServicio`**: Utiliza la inyección de setter para recibir una instancia de `ClienteRepositorio`. La anotación `@Autowired` se utiliza en el método `setClienteRepositorio`, permitiendo que Spring inyecte la dependencia al crear el bean.

## Alcance de los Beans (Bean Scoping)

El **alcance de los beans** en Spring se refiere a la duración y visibilidad de un bean dentro del contexto de la aplicación. Dependiendo de cómo se configure un bean, puede tener diferentes alcances que determinan cuántas instancias del bean se crean y cómo se comparten a través de la aplicación.

### Tipos de Alcance de Beans

1. **Singleton**
   - **Descripción**: Este es el alcance predeterminado en Spring. Solo se crea una única instancia del bean en el contenedor de Spring, y esa instancia se comparte en toda la aplicación.
   - **Uso**: Ideal para beans que no mantienen estado o que son costosos de crear.

2. **Prototype**
   - **Descripción**: Se crea una nueva instancia del bean cada vez que se solicita. Esto significa que cada vez que se inyecta o se solicita el bean, se obtiene una instancia nueva.
   - **Uso**: Útil para beans que mantienen estado o que necesitan ser independientes entre sí.

3. **Request**
   - **Descripción**: Este alcance se utiliza en aplicaciones web. Se crea una nueva instancia del bean para cada solicitud HTTP. Una vez que la solicitud ha sido procesada, el bean se destruye.
   - **Uso**: Ideal para beans que deben manejar datos específicos de una única solicitud.

4. **Session**
   - **Descripción**: Similar al alcance de solicitud, pero este se crea una instancia del bean por cada sesión de usuario. La instancia persiste durante la sesión del usuario y se destruye cuando la sesión finaliza.
   - **Uso**: Adecuado para almacenar información que necesita ser mantenida a través de múltiples solicitudes dentro de una misma sesión de usuario.

5. **Global Session** *(solo en aplicaciones portlet)*
   - **Descripción**: Se utiliza en el contexto de aplicaciones portlet. Crea una instancia del bean que es compartida por todas las sesiones de los portlets en una aplicación.
   - **Uso**: Útil para beans que deben ser accesibles desde múltiples portlets dentro de la misma aplicación.

6. **WebSocket**
   - **Descripción**: Se utiliza en el contexto de aplicaciones que implementan WebSockets. Crea una nueva instancia del bean para cada conexión WebSocket. Este alcance es útil para manejar datos y estado específicos de cada conexión en tiempo real.
   - **Uso**: Adecuado para aplicaciones que requieren comunicación bidireccional en tiempo real, como chat o aplicaciones de notificaciones en tiempo real.

7. **Application**
   - **Descripción**: Crea una única instancia del bean que es compartida a través del ciclo de vida de la aplicación. Este alcance es similar al singleton, pero se usa en un contexto de aplicación más amplio, a menudo en aplicaciones Spring que se ejecutan en un contenedor.
   - **Uso**: Ideal para beans que necesitan ser accesibles y compartidos por toda la aplicación y deben mantenerse durante todo el ciclo de vida de la misma.

### Resumen de Alcances de Beans

| Tipo de Alcance | Descripción                                                               | Uso Principal                                    |
| --------------- | ------------------------------------------------------------------------- | ------------------------------------------------ |
| Singleton       | Una única instancia compartida en toda la aplicación.                     | Beans sin estado o costosos de crear.            |
| Prototype       | Nueva instancia creada cada vez que se solicita.                          | Beans que mantienen estado o son independientes. |
| Request         | Nueva instancia creada para cada solicitud HTTP.                          | Datos específicos de una única solicitud.        |
| Session         | Nueva instancia creada para cada sesión de usuario.                       | Información persistente durante la sesión.       |
| Global Session  | Instancia compartida por todas las sesiones de portlets.                  | Beans accesibles desde múltiples portlets.       |
| WebSocket       | Nueva instancia creada para cada conexión WebSocket.                      | Comunicación bidireccional en tiempo real.       |
| Application     | Una única instancia compartida durante el ciclo de vida de la aplicación. | Beans accesibles a lo largo de la aplicación.    |

### Ejemplos de Alcance de Beans en Spring

#### 1. Singleton

```java
import org.springframework.stereotype.Component;

@Component
public class ServicioSingleton {
    public void mostrarMensaje() {
        System.out.println("Soy un bean singleton.");
    }
}

// Uso
// Al inyectar ServicioSingleton en múltiples clases, se obtendrá la misma instancia.
```

#### 2. Prototype

```java
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ServicioPrototype {
    public void mostrarMensaje() {
        System.out.println("Soy un bean prototype.");
    }
}

// Uso
// Cada vez que se inyecte ServicioPrototype, se obtendrá una nueva instancia.
```

#### 3. Request

```java
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ServicioRequest {
    public void mostrarMensaje() {
        System.out.println("Soy un bean de solicitud.");
    }
}

// Uso
// Cada vez que se realice una nueva solicitud HTTP, se creará una nueva instancia de ServicioRequest.
```

#### 4. Session

```java
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class ServicioSession {
    public void mostrarMensaje() {
        System.out.println("Soy un bean de sesión.");
    }
}

// Uso
// Se crea una nueva instancia de ServicioSession por cada sesión de usuario.
```

#### 5. Global Session (solo en aplicaciones portlet)

```java
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "globalSession")
public class ServicioGlobalSession {
    public void mostrarMensaje() {
        System.out.println("Soy un bean de sesión global.");
    }
}

// Uso
// Se comparte una instancia de ServicioGlobalSession entre todas las sesiones de portlets en la aplicación.
```

#### 6. WebSocket

```java
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("websocket")
public class ServicioWebSocket {
    public void mostrarMensaje() {
        System.out.println("Soy un bean de WebSocket.");
    }
}

// Uso
// Cada conexión WebSocket crea una nueva instancia de ServicioWebSocket.
```

#### 7. Application

```java
import org.springframework.stereotype.Component;

@Component
public class ServicioApplication {
    public void mostrarMensaje() {
        System.out.println("Soy un bean de aplicación.");
    }
}

// Uso
// Una única instancia de ServicioApplication se comparte durante todo el ciclo de vida de la aplicación.
```

### Resumen de Ejemplos

Cada uno de estos ejemplos representa un tipo de bean con su respectivo alcance. En la práctica, el alcance se elige en función de las necesidades específicas de la aplicación:

- **Singleton**: Ideal para servicios que no mantienen estado.
- **Prototype**: Útil para servicios que requieren instancias independientes.
- **Request**: Para manejar datos específicos de cada solicitud HTTP.
- **Session**: Para información persistente dentro de una sesión de usuario.
- **Global Session**: Para beans accesibles en el contexto de portlets.
- **WebSocket**: Para comunicación en tiempo real en aplicaciones que usan WebSockets.
- **Application**: Para beans que deben ser accesibles a lo largo de toda la aplicación.

## Special Beans - Enviroment

El bean **Environment** en Spring es una parte fundamental de la infraestructura de configuración del marco. Proporciona una abstracción que permite acceder a las propiedades de configuración de la aplicación, como variables de entorno, propiedades definidas en archivos `.properties` o `.yaml`, y otros parámetros de configuración. 

#### 1. Environment Abstraction

La **abstracción de entorno** en Spring permite a los desarrolladores acceder a la configuración de la aplicación de manera flexible y coherente. El bean Environment ofrece métodos para obtener propiedades de diferentes fuentes, lo que permite que la aplicación sea más dinámica y adaptable a diferentes entornos (por ejemplo, desarrollo, prueba y producción).

- **Acceso a propiedades**: Puedes acceder a propiedades específicas utilizando métodos como `getProperty(String key)`.
- **Perfiles**: El Environment también gestiona perfiles, que permiten activar diferentes configuraciones basadas en el entorno de ejecución.

#### 2. Injectable

El bean Environment es **inyectable** en cualquier clase gestionada por Spring. Esto significa que puedes utilizar la inyección de dependencias para obtener una instancia del Environment y acceder a las propiedades definidas en tu aplicación sin necesidad de hacer una búsqueda manual.

- **Inyección**: Puedes inyectar el Environment directamente en tus componentes, servicios o controladores, lo que te permite acceder a las propiedades de manera sencilla y directa.

### Ejemplo Simple

Aquí tienes un ejemplo sencillo que ilustra cómo usar el bean Environment para acceder a propiedades de configuración en una aplicación Spring.

#### Paso 1: Configuración de Propiedades

Primero, crea un archivo `application.properties` en tu directorio de recursos (`src/main/resources`):

```properties
app.name=Mi Aplicación
app.version=1.0.0
```

#### Paso 2: Crear un Servicio que Utiliza Environment

Luego, crea un servicio que inyecte el bean Environment y acceda a las propiedades definidas en `application.properties`:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class MiServicio {

    private final Environment environment;

    @Autowired
    public MiServicio(Environment environment) {
        this.environment = environment;
    }

    public void mostrarInformacion() {
        String appName = environment.getProperty("app.name");
        String appVersion = environment.getProperty("app.version");
        
        System.out.println("Nombre de la Aplicación: " + appName);
        System.out.println("Versión de la Aplicación: " + appVersion);
    }
}
```

#### Paso 3: Uso del Servicio en la Aplicación

Finalmente, puedes usar este servicio en tu aplicación, por ejemplo, en un controlador:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MiControlador {

    private final MiServicio miServicio;

    @Autowired
    public MiControlador(MiServicio miServicio) {
        this.miServicio = miServicio;
    }

    @GetMapping("/info")
    public String obtenerInformacion() {
        miServicio.mostrarInformacion();
        return "Información mostrada en la consola.";
    }
}
```

### Explicación del Ejemplo

1. **Configuración de Propiedades**: En el archivo `application.properties`, definimos dos propiedades: `app.name` y `app.version`.

2. **Servicio `MiServicio`**: Creamos un servicio llamado `MiServicio` que inyecta el bean Environment a través del constructor. Dentro del método `mostrarInformacion()`, utilizamos `environment.getProperty()` para acceder a las propiedades definidas.

3. **Controlador `MiControlador`**: Creamos un controlador que expone un endpoint `/info`. Cuando se accede a este endpoint, se llama al método `mostrarInformacion()` del servicio, que imprime el nombre y la versión de la aplicación en la consola.

## Special Beans - Bean Profiles

Los **Bean Profiles** en Spring son una característica que permite definir y activar configuraciones específicas para diferentes entornos de ejecución. Esto es especialmente útil en aplicaciones que se despliegan en múltiples entornos, como desarrollo, pruebas y producción. Los perfiles permiten organizar las configuraciones y asegurarse de que los componentes correctos se carguen en el contexto de la aplicación según el entorno activo.

1. **Definición de Perfiles**: Los perfiles permiten agrupar beans que deben ser utilizados en un contexto específico. Puedes definir múltiples perfiles y activar el que desees en función de las necesidades.

2. **Activación de Perfiles**: Los perfiles se pueden activar de varias maneras, incluyendo a través de propiedades en el archivo `application.properties`, variables de entorno o directamente desde el código.

3. **Condiciones de Carga**: Solo se cargan los beans asociados con el perfil activo. Esto permite que diferentes configuraciones coexistan en la misma base de código, evitando conflictos y facilitando la implementación de diferentes entornos.

### Ejemplo Simple

A continuación, se presenta un ejemplo que ilustra cómo utilizar Bean Profiles para gestionar diferentes configuraciones en una aplicación Spring.

#### Paso 1: Configuración de Perfiles

Define dos archivos de propiedades: `application-dev.properties` y `application-prod.properties`.

**application-dev.properties** (para el entorno de desarrollo):

```properties
app.name=Mi Aplicación (Desarrollo)
app.version=1.0.0-DEV
```

**application-prod.properties** (para el entorno de producción):

```properties
app.name=Mi Aplicación (Producción)
app.version=1.0.0-PROD
```

#### Paso 2: Crear Beans con Perfiles

Ahora, crea un servicio que use la anotación `@Profile` para definir qué configuración debe usarse en cada entorno:

```java
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev") // Este bean se activa solo en el perfil 'dev'
public class ServicioDesarrollo {

    public void mostrarInformacion() {
        System.out.println("Configuración: Desarrollo");
    }
}

@Service
@Profile("prod") // Este bean se activa solo en el perfil 'prod'
public class ServicioProduccion {

    public void mostrarInformacion() {
        System.out.println("Configuración: Producción");
    }
}
```

#### Paso 3: Usar el Servicio en la Aplicación

Crea un controlador que utilice uno de estos servicios basándose en el perfil activo:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MiControlador {

    private final Object servicio; // Usamos Object para permitir ambos tipos

    @Autowired
    public MiControlador(ServicioDesarrollo servicioDesarrollo, ServicioProduccion servicioProduccion) {
        // Seleccionamos el servicio basado en el perfil activo
        if (servicioDesarrollo != null) {
            this.servicio = servicioDesarrollo;
        } else {
            this.servicio = servicioProduccion;
        }
    }

    @GetMapping("/info")
    public String obtenerInformacion() {
        if (servicio instanceof ServicioDesarrollo) {
            ((ServicioDesarrollo) servicio).mostrarInformacion();
        } else if (servicio instanceof ServicioProduccion) {
            ((ServicioProduccion) servicio).mostrarInformacion();
        }
        return "Información mostrada en la consola.";
    }
}
```

### Explicación del Ejemplo

1. **Archivos de Propiedades**: Hemos definido dos archivos de propiedades que contienen configuraciones diferentes para desarrollo y producción.

2. **Servicios con Perfiles**: Creamos dos servicios (`ServicioDesarrollo` y `ServicioProduccion`) que están anotados con `@Profile("dev")` y `@Profile("prod")`, respectivamente. Cada servicio tiene un método que imprime información sobre la configuración.

3. **Controlador `MiControlador`**: En el controlador, inyectamos ambos servicios. Luego, seleccionamos cuál utilizar en función del perfil activo. Al acceder al endpoint `/info`, se llamará al método `mostrarInformacion()` del servicio correspondiente.

### Activación de Perfiles - Programáticamente

La **activación de perfiles programáticamente** en Spring permite habilitar o deshabilitar perfiles de beans en el código, en lugar de depender únicamente de configuraciones externas como archivos de propiedades o variables de entorno. Esto ofrece más flexibilidad para controlar el comportamiento de la aplicación en diferentes contextos.

#### Ejemplo de Activación de Perfiles Programáticamente

A continuación, se presenta un ejemplo de cómo activar un perfil en Spring mediante código.

#### Paso 1: Crear una Configuración de Aplicación

Primero, crea una clase de configuración que incluya un método para activar el perfil deseado:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Configuration
public class MiConfiguracion {

    @Bean
    public void activarPerfil() {
        // Aquí se activa el perfil 'dev' programáticamente
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("dev");
        context.refresh();
    }
}
```

#### Paso 2: Crear un Servicio

Luego, crea un servicio que dependa del perfil activado:

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev") // Este bean solo se cargará si el perfil 'dev' está activo
public class MiServicio {

    @Value("${app.name}")
    private String appName;

    public void mostrarInformacion() {
        System.out.println("Nombre de la Aplicación (Desarrollo): " + appName);
    }
}
```

#### Paso 3: Configurar y Ejecutar la Aplicación

Finalmente, configura el contexto de la aplicación y ejecuta el método para activar el perfil:

```java
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Aplicacion {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MiConfiguracion.class);
        MiServicio miServicio = context.getBean(MiServicio.class);
        miServicio.mostrarInformacion();
        context.close();
    }
}
```

### Explicación del Ejemplo

1. **Configuración de la Clase**: En `MiConfiguracion`, se define un método que activa el perfil `dev` programáticamente. Utilizamos `AnnotationConfigApplicationContext` para obtener el contexto y establecer el perfil activo.

2. **Servicio `MiServicio`**: Este servicio está anotado con `@Profile("dev")`, lo que significa que solo se cargará cuando el perfil `dev` esté activo. El método `mostrarInformacion()` imprime el nombre de la aplicación.

3. **Ejecución de la Aplicación**: En la clase `Aplicacion`, creamos un contexto de aplicación basado en la configuración. Al ejecutar el método `activarPerfil()`, se activa el perfil `dev`, y el servicio `MiServicio` se carga y se ejecuta, mostrando la información correspondiente.

## Special Beans - Anotación @Value

La anotación **`@Value`** en Spring se utiliza para inyectar valores en campos, métodos o parámetros de constructor desde propiedades externas, como archivos de configuración (`application.properties`, `application.yml`) o variables de entorno. Esta funcionalidad permite que las aplicaciones sean más flexibles y configurables, ya que los valores pueden cambiar sin necesidad de modificar el código fuente.

#### Uso Básico de @Value

1. **Inyección de Valores Literales**: Puedes utilizar `@Value` para inyectar valores literales directamente.

2. **Inyección desde Archivos de Propiedades**: Puedes inyectar valores que están definidos en archivos de propiedades.

3. **Expresiones SpEL (Spring Expression Language)**: Permite realizar inyecciones más complejas utilizando expresiones.

### Ejemplo 1: Inyección de un Valor Literal

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MiComponente {

    @Value("Hola, mundo!")
    private String saludo;

    public void mostrarSaludo() {
        System.out.println(saludo);
    }
}
```

#### Explicación
- En este ejemplo, la anotación `@Value` se usa para inyectar un valor literal "Hola, mundo!" en el campo `saludo`. 
- Cuando se llama al método `mostrarSaludo()`, se imprime el saludo.

### Ejemplo 2: Inyección desde Archivos de Propiedades

Supongamos que tienes un archivo `application.properties` con el siguiente contenido:

```properties
app.nombre=Mi Aplicación
app.version=1.0.0
```

Ahora, puedes inyectar estos valores en tu componente:

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfiguracionApp {

    @Value("${app.nombre}")
    private String nombre;

    @Value("${app.version}")
    private String version;

    public void mostrarInformacion() {
        System.out.println("Nombre: " + nombre);
        System.out.println("Versión: " + version);
    }
}
```

#### Explicación
- Aquí, `@Value("${app.nombre}")` y `@Value("${app.version}")` se utilizan para inyectar valores desde el archivo de propiedades. 
- Cuando se llama al método `mostrarInformacion()`, se imprime el nombre y la versión de la aplicación.

### Ejemplo 3: Inyección con Expresiones SpEL

También puedes usar expresiones SpEL para realizar operaciones o acceder a propiedades más complejas:

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MiComponenteSpEL {

    @Value("#{2 * 3}") // Multiplicación de 2 y 3
    private int resultado;

    @Value("#{T(java.lang.Math).PI}") // Valor de PI
    private double pi;

    public void mostrarValores() {
        System.out.println("Resultado: " + resultado);
        System.out.println("Valor de PI: " + pi);
    }
}
```

#### Explicación
- En este caso, `@Value("#{2 * 3}")` inyecta el resultado de la multiplicación de 2 y 3.
- `@Value("#{T(java.lang.Math).PI}")` inyecta el valor de PI utilizando la clase `Math` de Java.
- Al llamar al método `mostrarValores()`, se imprimirán el resultado de la multiplicación y el valor de PI.

# Mejores Prácticas

## Split Configuration

La **división de configuración** (Split Configuration) en Spring se refiere a la práctica de organizar la configuración de tu aplicación en múltiples clases de configuración, en lugar de mantener toda la configuración en una sola clase. Esto no solo mejora la legibilidad del código, sino que también facilita el mantenimiento y la escalabilidad del proyecto a medida que crece.

### Ventajas de la División de Configuración

1. **Modularidad**: Permite agrupar la configuración relacionada en clases separadas, lo que facilita la comprensión y el mantenimiento del código.
2. **Reutilización**: Puedes importar configuraciones comunes en diferentes contextos, evitando la duplicación de código.
3. **Pruebas**: Las configuraciones separadas pueden facilitar la realización de pruebas unitarias y la gestión de perfiles.

### Ejemplo de División de Configuración

#### Paso 1: Crear Configuraciones Separadas

Imagina que tienes una aplicación que necesita configurar una base de datos y un servicio. Puedes dividir estas configuraciones en dos clases diferentes.

**Configuración de la Base de Datos**

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/mi_base_de_datos");
        dataSource.setUsername("usuario");
        dataSource.setPassword("contraseña");
        return dataSource;
    }
}
```

**Configuración del Servicio**

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public MiServicio miServicio() {
        return new MiServicio();
    }
}
```

#### Paso 2: Importar Configuraciones

Puedes crear una clase de configuración principal que importe estas configuraciones:

```java
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DatabaseConfig.class, ServiceConfig.class})
@ComponentScan(basePackages = "com.ejemplo") // Escanea componentes en el paquete especificado
public class AppConfig {
}
```

#### Explicación del Ejemplo

1. **Base de Datos**: En la clase `DatabaseConfig`, se define un bean para la fuente de datos (`DataSource`) que se encargará de la conexión a la base de datos. 
2. **Servicio**: En la clase `ServiceConfig`, se define un bean para el servicio (`MiServicio`), que puede contener la lógica de negocio.
3. **Configuración Principal**: En `AppConfig`, se utilizan las anotaciones `@Import` para incluir las configuraciones de base de datos y servicio. Además, `@ComponentScan` permite a Spring buscar automáticamente componentes en el paquete especificado.