# Repasando Spring Boot y Spring Data JPA

**Spring Boot** es un marco de trabajo basado en Java que facilita la creación de aplicaciones independientes y listas para producción, sin necesidad de configurar manualmente muchos de los aspectos comunes de una aplicación Spring tradicional. Proporciona configuraciones automáticas, un servidor embebido y una estructura modular, lo que permite a los desarrolladores enfocarse en el desarrollo de funcionalidades sin preocuparse por detalles de configuración.

**Spring Data JPA** es un módulo de Spring que simplifica el acceso y manejo de bases de datos relacionales mediante la implementación de JPA (Java Persistence API). Proporciona una capa de abstracción para realizar consultas y transacciones de manera más sencilla, con soporte para repositorios que eliminan la necesidad de escribir código SQL manualmente en la mayoría de los casos.

## Core Features - Características principales

1. **IOC (Inversion of Control Container)**:  
   En Spring, la Inversión de Control (IoC) es un principio en el que el control de la creación y administración de objetos no está en manos del programador, sino que se delega al contenedor de IoC de Spring. Este contenedor es responsable de instanciar, configurar y ensamblar los objetos (o "beans") de la aplicación, facilitando el desacoplamiento de componentes. Esto permite que los objetos dependan de otros sin preocuparse por su creación, ya que el contenedor inyecta estas dependencias.

2. **AOP (Aspect-Oriented Programming)**:  
   La Programación Orientada a Aspectos (AOP) en Spring permite separar preocupaciones transversales, como la seguridad, transacciones, o el logging, del código de negocio principal. Con AOP, se pueden aplicar "aspectos" (funcionalidades comunes a diferentes partes de la aplicación) a los métodos de una clase, sin necesidad de modificar el código fuente de esa clase. Esto se logra a través de conceptos como "advice" (el código que se ejecuta), "pointcut" (donde se aplica) y "aspect" (la combinación de ambos).

3. **DAF (Data Access Framework)**:  
   El Data Access Framework de Spring proporciona un conjunto de herramientas y abstracciones para trabajar con bases de datos. Incluye soporte para tecnologías como JDBC, JPA, Hibernate, y otros ORM (Object-Relational Mapping), simplificando las operaciones de acceso a datos, manejo de transacciones y la interacción con diferentes bases de datos. Spring Data JPA es parte de este marco, lo que facilita el acceso a datos con repositorios.

4. **MVC (Spring MVC Framework)**:  
   Spring MVC es un marco que sigue el patrón Modelo-Vista-Controlador (MVC), el cual facilita la creación de aplicaciones web. Divide la aplicación en tres componentes principales:
   - **Modelo**: Maneja los datos y la lógica de negocio.
   - **Vista**: Presenta los datos al usuario, generalmente en formato HTML o JSON.
   - **Controlador**: Gestiona las solicitudes del usuario, interactúa con el modelo y selecciona la vista adecuada para devolver al usuario.  
   Spring MVC es altamente configurable y extensible, facilitando el desarrollo de aplicaciones web dinámicas y RESTful APIs.

## Spring Bean

**Objeto**:  
Un *Spring Bean* es un objeto gestionado por el marco de trabajo Spring dentro de una aplicación Java. En términos simples, un bean es un componente que forma parte del sistema IoC (Inversión de Control) de Spring. Los beans representan las piezas clave que Spring crea, configura y conecta en tiempo de ejecución, lo que permite construir aplicaciones de manera modular y flexible. Cada bean es un objeto de una clase Java que está registrado en el contenedor de Spring y puede ser inyectado donde sea necesario.

**Características**:  
El objetivo principal de los *Spring Beans* es simplificar el desarrollo de aplicaciones empresariales Java complejas. Spring ayuda a reducir el acoplamiento entre los componentes al permitir que las dependencias se gestionen de manera declarativa, a través de la inyección de dependencias (DI). Esto significa que el programador no necesita preocuparse por cómo crear o gestionar los objetos que interactúan entre sí. Además, Spring Beans facilitan otros servicios importantes como el manejo de transacciones, la seguridad, la gestión de eventos, entre otros, todos orquestados por el contenedor de Spring.

**Configuración**:  
Los *Spring Beans* pueden configurarse de varias maneras:
- **XML**: En las primeras versiones de Spring, la configuración de los beans se hacía principalmente a través de archivos XML, donde se declaraban los beans y sus dependencias.
- **Anotaciones Java**: Con la evolución de Spring, las anotaciones se convirtieron en una forma popular y más concisa de definir beans y controlar su ciclo de vida, utilizando anotaciones como `@Component`, `@Autowired`, `@Bean`, entre otras.
- **Código Java**: Otra opción moderna es usar clases Java con anotaciones y métodos para definir la configuración, lo que se conoce como configuración basada en Java. Esto permite crear beans y configurar dependencias directamente a través de código, proporcionando más flexibilidad.

### Ciclo de vida de un Spring Bean

El **ciclo de vida de un Spring Bean** define las etapas por las que pasa un bean desde su creación hasta su destrucción dentro del contenedor de Spring. Este ciclo es gestionado por el contenedor IoC de Spring y garantiza que los beans se creen, configuren, inicialicen y destruyan adecuadamente. A continuación, se detallan las fases principales del ciclo de vida de un Spring Bean:

#### 1. **Instanciación**:
   - El ciclo de vida de un bean comienza cuando Spring lo instancia. Esto sucede cuando el contenedor de Spring crea el objeto de la clase que ha sido definida como un bean, ya sea mediante la configuración XML, anotaciones o configuración basada en código Java.
   - Spring utiliza el constructor del bean para crear la instancia del objeto.

#### 2. **Inyección de Dependencias**:
   - Después de instanciar el bean, Spring realiza la **inyección de dependencias**. Esto significa que Spring inyecta los objetos o valores que el bean necesita para funcionar, ya sean otros beans o configuraciones externas, que se establecen a través de campos, constructores o métodos.
   - Dependiendo de la configuración, esta inyección se puede realizar mediante inyección de constructor o inyección de setter.

#### 3. **Método `@PostConstruct` (Inicialización)**:
   - Una vez que el bean ha sido instanciado e inyectadas sus dependencias, Spring llama a cualquier método marcado con la anotación `@PostConstruct` (si existe) o el método definido como un método de inicialización en la configuración del bean.
   - Este es un punto clave donde se puede ejecutar lógica de inicialización adicional, como abrir conexiones o cargar datos esenciales para el funcionamiento del bean.
   
#### 4. **Método `afterPropertiesSet()` (si implementa `InitializingBean`)**:
   - Si el bean implementa la interfaz `InitializingBean`, Spring llamará al método `afterPropertiesSet()`. Este método también se usa para realizar cualquier configuración o inicialización personalizada del bean después de que se hayan inyectado todas las propiedades.

#### 5. **Método `custom-init` (si está configurado)**:
   - Si se especificó un método de inicialización personalizado en la configuración del bean, Spring también lo ejecutará en esta fase. El método de inicialización personalizado puede estar definido en un archivo XML o en una clase de configuración Java.

#### 6. **Bean en Uso**:
   - En esta etapa, el bean está completamente inicializado y listo para ser utilizado dentro de la aplicación. Este bean estará disponible para ser inyectado y llamado por otros componentes o servicios según sea necesario. Durante esta fase, el bean estará operativo y atenderá cualquier solicitud o función que se le requiera.
   
#### 7. **Método `@PreDestroy` (Destrucción)**:
   - Cuando el contenedor de Spring está en proceso de ser cerrado o el bean está siendo destruido, Spring llama a cualquier método marcado con `@PreDestroy` (si existe). Aquí es donde el bean puede realizar tareas de limpieza, como cerrar conexiones, liberar recursos o guardar datos finales.
   
#### 8. **Método `destroy()` (si implementa `DisposableBean`)**:
   - Si el bean implementa la interfaz `DisposableBean`, Spring llamará al método `destroy()`. Esto permite realizar operaciones de limpieza específicas antes de que el bean sea destruido por completo.
   
#### 9. **Método `custom-destroy` (si está configurado)**:
   - Similar a la inicialización, Spring también ejecutará un método de destrucción personalizado (si se especificó en la configuración del bean). Esto se puede configurar en XML o en clases Java y es útil para realizar tareas específicas antes de que el bean sea destruido.

#### 10. **Destrucción del Bean**:
   - Finalmente, el bean es destruido por el contenedor de Spring, liberando los recursos que ocupaba. Esto sucede cuando el contexto de la aplicación se cierra (por ejemplo, al apagar la aplicación o cuando el contenedor de Spring es destruido).

#### Resumen del Ciclo de Vida:
1. Instanciación del Bean.
2. Inyección de dependencias.
3. Inicialización (`@PostConstruct`, `afterPropertiesSet()`, o método personalizado).
4. Uso del Bean.
5. Destrucción (`@PreDestroy`, `destroy()`, o método personalizado).

## @configuration ejemplo

En Spring, las anotaciones `@Configuration` y `@Bean` se utilizan para definir beans de manera programática utilizando clases Java. Estas anotaciones permiten crear una configuración basada en Java para gestionar los objetos (beans) que serán manejados por el contenedor IoC de Spring.

### **@Configuration**

- La anotación `@Configuration` se utiliza para marcar una clase como una clase de configuración. Es equivalente a un archivo de configuración XML en versiones más antiguas de Spring, pero en este caso se define la configuración directamente en código Java.
- Dentro de una clase anotada con `@Configuration`, podemos definir uno o varios métodos con la anotación `@Bean`, que indican a Spring que esos métodos devuelven objetos que deben ser gestionados como beans.

### **@Bean**

- La anotación `@Bean` se utiliza para marcar un método dentro de una clase anotada con `@Configuration`. Este método debe devolver un objeto que será registrado como un bean en el contenedor de Spring.
- Spring gestionará el ciclo de vida del objeto devuelto por este método y lo inyectará en otras partes de la aplicación según sea necesario.
- Es una forma programática de declarar beans, en lugar de utilizar XML o anotaciones como `@Component`.

### Ejemplo de código explicado

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Marcamos esta clase como una clase de configuración de Spring
@Configuration
public class AppConfig {

    // Este método devuelve un objeto de tipo Servicio que será manejado por el contenedor de Spring
    @Bean
    public Servicio servicio() {
        return new Servicio();
    }

    // Este método devuelve otro bean que depende del bean 'servicio'
    @Bean
    public Cliente cliente(Servicio servicio) {
        return new Cliente(servicio); // Inyectamos el bean Servicio en el constructor del Cliente
    }
}
```

### Explicación:

1. **@Configuration**: 
   La clase `AppConfig` está anotada con `@Configuration`, lo que significa que Spring la tratará como una fuente de definiciones de beans. Aquí es donde se configuran los beans que el contenedor gestionará.

2. **@Bean en `servicio()`**:
   El método `servicio()` está anotado con `@Bean`, lo que le indica a Spring que debe registrar el objeto `Servicio` que devuelve este método como un bean en el contenedor. Cada vez que otra parte de la aplicación necesite un objeto de tipo `Servicio`, Spring inyectará este bean en lugar de crear una nueva instancia.

3. **@Bean en `cliente(Servicio servicio)`**:
   Este método también está anotado con `@Bean`, pero aquí recibe como argumento un objeto `Servicio`. Spring es lo suficientemente inteligente como para inyectar el bean `Servicio` creado previamente en este método cuando necesite instanciar un objeto `Cliente`. Esto muestra cómo Spring puede manejar las dependencias entre beans.

### Uso en una aplicación:

```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        // Creamos el contexto de Spring usando la clase de configuración
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Solicitamos el bean Cliente y Spring se encarga de proporcionarlo con las dependencias inyectadas
        Cliente cliente = context.getBean(Cliente.class);
        
        // Usamos el bean Cliente
        cliente.realizarServicio();
    }
}

class Cliente {
    private final Servicio servicio;

    public Cliente(Servicio servicio) {
        this.servicio = servicio;
    }

    public void realizarServicio() {
        servicio.ejecutar();
    }
}

class Servicio {
    public void ejecutar() {
        System.out.println("Ejecutando el servicio...");
    }
}
```

### Explicación del flujo:

1. En el método `main()`, creamos un contexto de aplicación de Spring (`ApplicationContext`) basado en la clase de configuración `AppConfig`. Esto carga los beans definidos en esa clase.
2. Solicitamos un bean de tipo `Cliente` al contexto. Spring creará el bean `Cliente`, y al hacerlo, inyectará el bean `Servicio` en su constructor.
3. Finalmente, invocamos un método en el `Cliente` (`realizarServicio`), que a su vez llama al método `ejecutar()` del `Servicio`.

### Beneficios:
- **Desacoplamiento**: Las dependencias como `Servicio` y `Cliente` están desacopladas. El contenedor IoC se encarga de su creación y configuración.
- **Modularidad**: La configuración está en una clase separada, lo que facilita la gestión y la evolución del código.
- **Facilidad de prueba**: Al utilizar esta configuración basada en Java, es más sencillo realizar pruebas unitarias y de integración, ya que se puede controlar la creación de los beans y sus dependencias en tiempo de prueba.

## Spring Components

En Spring, los **componentes** son objetos que son gestionados por el contenedor de Inversión de Control (IoC). Spring proporciona una serie de anotaciones que ayudan a definir diferentes tipos de componentes dentro de la aplicación y permiten inyectar dependencias entre ellos. A continuación se detallan las principales anotaciones para componentes de Spring: `@Component`, `@Autowired`, `@Repository`, `@Service` y `@Controller`.

### **@Component**

- **Descripción**: 
  La anotación `@Component` es la anotación genérica en Spring para definir un bean. Marca una clase como un componente que debe ser registrado en el contenedor de Spring. Cualquier clase anotada con `@Component` será detectada automáticamente por el escaneo de componentes y gestionada como un bean por Spring.
  
- **Uso**:
  Se utiliza cuando una clase no tiene una función específica como repositorio, servicio o controlador, pero se desea que sea gestionada por Spring.

#### Ejemplo:
```java
import org.springframework.stereotype.Component;

@Component
public class Notificador {
    public void enviarMensaje(String mensaje) {
        System.out.println("Enviando mensaje: " + mensaje);
    }
}
```
En este ejemplo, la clase `Notificador` se define como un componente genérico que Spring gestionará como un bean.

### **@Autowired**

- **Descripción**: 
  La anotación `@Autowired` se utiliza para inyectar dependencias automáticamente en Spring. Cuando se marca un constructor, un método o un campo con `@Autowired`, Spring intentará resolver e inyectar un bean adecuado en esa dependencia.

- **Uso**:
  Facilita la inyección de dependencias sin necesidad de establecer manualmente los objetos, ya que el contenedor de Spring se encarga de gestionarlos.

#### Ejemplo:
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlertaServicio {

    private final Notificador notificador;

    // Inyección de dependencia mediante el constructor
    @Autowired
    public AlertaServicio(Notificador notificador) {
        this.notificador = notificador;
    }

    public void enviarAlerta(String mensaje) {
        notificador.enviarMensaje(mensaje);
    }
}
```
En este ejemplo, la clase `AlertaServicio` inyecta una instancia de `Notificador` utilizando `@Autowired` en su constructor.

### **@Repository**

- **Descripción**: 
  `@Repository` es una especialización de `@Component` que se utiliza para marcar clases que interactúan directamente con la base de datos. Representa la capa de acceso a datos (DAO). Spring proporciona soporte adicional para el manejo de excepciones en las clases marcadas con `@Repository`, convirtiendo excepciones específicas de base de datos en excepciones de Spring.

- **Uso**:
  Se usa para clases que manejan la lógica de persistencia, como consultas o transacciones de base de datos.

#### Ejemplo:
```java
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepositorio {

    public Usuario encontrarPorId(int id) {
        // Simulación de una consulta a la base de datos
        return new Usuario(id, "Juan Pérez");
    }
}
```
Aquí, `UsuarioRepositorio` está marcado como un repositorio, lo que indica que contiene lógica de acceso a la base de datos para la entidad `Usuario`.

### **@Service**

- **Descripción**: 
  `@Service` es otra especialización de `@Component` que se utiliza para marcar las clases de servicio. Representa la capa de lógica de negocio en la aplicación. Aunque funcionalmente es similar a `@Component`, su semántica indica que la clase contiene lógica de negocio, haciendo el código más legible.

- **Uso**:
  Se utiliza para las clases que implementan la lógica de negocio o las reglas de la aplicación.

#### Ejemplo:
```java
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;

    @Autowired
    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Usuario obtenerUsuarioPorId(int id) {
        return usuarioRepositorio.encontrarPorId(id);
    }
}
```
En este ejemplo, `UsuarioServicio` maneja la lógica de negocio, utilizando `UsuarioRepositorio` para acceder a los datos.

### **@Controller**

- **Descripción**: 
  `@Controller` es una especialización de `@Component` que se utiliza para marcar las clases que manejan las solicitudes HTTP y responden con vistas en aplicaciones Spring MVC. Representa la capa de presentación, donde se controlan las interacciones con el usuario.

- **Uso**:
  Se utiliza para las clases que manejan peticiones web, generalmente en aplicaciones que siguen el patrón MVC (Model-View-Controller).

#### Ejemplo:
```java
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;

    @Autowired
    public UsuarioControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("/usuario")
    public String obtenerUsuario(Model model) {
        Usuario usuario = usuarioServicio.obtenerUsuarioPorId(1);
        model.addAttribute("usuario", usuario);
        return "usuarioVista";  // Retorna el nombre de la vista
    }
}
```
En este ejemplo, `UsuarioControlador` es un controlador que maneja una solicitud HTTP GET para obtener información sobre un usuario y la pasa a una vista.

### Resumen de las Anotaciones:

- **@Component**: Marca cualquier clase genérica como un componente gestionado por Spring.
- **@Autowired**: Inyecta automáticamente dependencias en un campo, método o constructor.
- **@Repository**: Especialización de `@Component` para la capa de acceso a datos.
- **@Service**: Especialización de `@Component` para la lógica de negocio.
- **@Controller**: Especialización de `@Component` para manejar solicitudes HTTP en aplicaciones Spring MVC.

## Nombrando Beans

En Spring, al definir beans mediante la anotación `@Bean`, es posible asignarles un nombre específico utilizando `@Bean("nombreBean")`. Esto es útil cuando deseas identificar y acceder a un bean particular por su nombre, en lugar de depender únicamente del tipo de la clase. El nombramiento de los beans permite tener varios beans del mismo tipo en el contexto de Spring y seleccionar cuál usar por nombre cuando sea necesario.

### ¿Por qué nombrar un bean?

- **Identificación específica**: Al nombrar un bean, puedes distinguir entre múltiples beans del mismo tipo.
- **Inyección de dependencia por nombre**: En algunos casos, puede haber más de un bean del mismo tipo, y al nombrarlo puedes controlar cuál se inyecta en otra clase.
- **Mejora de legibilidad y organización**: Nombrar beans con nombres lógicos puede hacer que el código sea más legible y fácil de seguir.

### Ejemplo básico de nombramiento de beans

Supongamos que tienes una aplicación donde necesitas crear diferentes implementaciones de una misma clase `Servicio`.

#### Ejemplo de código:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // Definimos dos beans de tipo Servicio, pero con diferentes nombres
    @Bean("servicioA")
    public Servicio servicioA() {
        return new Servicio("Servicio A");
    }

    @Bean("servicioB")
    public Servicio servicioB() {
        return new Servicio("Servicio B");
    }
}
```

En este ejemplo, hemos creado dos beans del mismo tipo (`Servicio`), pero con nombres diferentes: `"servicioA"` y `"servicioB"`. Ahora, podemos inyectar el bean que necesitamos usando su nombre.

### Inyección de Beans Nombrados

Para inyectar un bean específico por su nombre en una clase, podemos utilizar la anotación `@Qualifier` junto con `@Autowired`.

#### Ejemplo de inyección de un bean específico:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Cliente {

    private final Servicio servicio;

    // Inyectamos el bean "servicioA" usando @Qualifier
    @Autowired
    public Cliente(@Qualifier("servicioA") Servicio servicio) {
        this.servicio = servicio;
    }

    public void ejecutarServicio() {
        System.out.println(servicio.getNombre());
    }
}

class Servicio {
    private String nombre;

    public Servicio(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
```

### Explicación del código:

1. **Definición de beans con nombre**:
   - Hemos definido dos beans de la clase `Servicio` en la configuración `AppConfig`. Cada uno tiene un nombre específico: `"servicioA"` y `"servicioB"`.

2. **Inyección con @Qualifier**:
   - En la clase `Cliente`, utilizamos `@Qualifier("servicioA")` junto con `@Autowired` para indicar a Spring que debe inyectar el bean llamado `"servicioA"`.
   - Esto es necesario porque hay más de un bean del mismo tipo (`Servicio`) en el contenedor de Spring, y debemos ser específicos sobre cuál queremos inyectar.

3. **Salida esperada**:
   - Cuando se ejecute el método `ejecutarServicio()` de `Cliente`, se imprimirá `"Servicio A"`, ya que es el bean inyectado mediante el uso de `@Qualifier("servicioA")`.

### Beneficios de nombrar beans:

- **Evitar conflictos**: Si tienes múltiples beans del mismo tipo, puedes evitar conflictos indicando exactamente cuál necesitas inyectar.
- **Modularidad**: Puedes tener diferentes implementaciones de una misma clase para diferentes contextos y seleccionarlas según el caso.
- **Flexibilidad**: Facilita la configuración dinámica, ya que puedes decidir qué bean usar en diferentes partes de la aplicación, controlando el comportamiento específico sin cambiar el código.

## Inyección de dependencias

### 1. Inyección por Constructor (Constructor Injection)

**Descripción**: Este método utiliza el constructor de la clase para inyectar las dependencias necesarias. Cuando se crea un objeto, se pasan las dependencias como parámetros al constructor. Esto asegura que el objeto no pueda existir sin sus dependencias.

**Ventajas**: Fomenta la inmutabilidad y facilita la creación de pruebas, ya que todas las dependencias se pueden proporcionar en el momento de la creación del objeto.

---

### 2. Inyección por Campo (Field Injection)

**Descripción**: En este enfoque, las dependencias se inyectan directamente en los campos de la clase utilizando la anotación `@Autowired`. Esto significa que el contenedor de Spring asignará los valores a los campos de forma automática sin necesidad de un constructor o método setter.

**Ventajas**: Es un método más sencillo y conciso, ya que evita la necesidad de un constructor adicional. Sin embargo, puede hacer que la clase sea más difícil de probar y menos flexible.

---

### 3. Métodos de Configuración (Configuration Methods)

**Descripción**: Este método implica definir un bean dentro de una clase de configuración usando la anotación `@Bean`. Las dependencias se pueden inyectar en los métodos de configuración, lo que permite crear y configurar beans de manera explícita.

**Ventajas**: Permite un control total sobre la creación y configuración de los beans, y es útil cuando se necesita realizar configuraciones complejas o personalizadas.

---

### 4. Inyección por Métodos Setter (Setter Methods Injection)

**Descripción**: En este enfoque, las dependencias se inyectan mediante métodos setter. Después de crear el objeto, se llaman a los métodos setter para proporcionar las dependencias necesarias.

**Ventajas**: Permite la modificación de dependencias en cualquier momento después de la creación del objeto y proporciona una manera de inyectar valores opcionales.

---

## Constructor Injection

La **inyección por constructor** es un patrón utilizado en Spring para proporcionar las dependencias necesarias a una clase a través de su constructor. Este enfoque garantiza que un objeto no pueda ser creado sin sus dependencias, lo que ayuda a mantener la inmutabilidad y facilita las pruebas unitarias.

### Características Principales

- **Requiere todas las dependencias**: Las dependencias deben ser proporcionadas en el momento de la creación del objeto, lo que asegura que el objeto esté completamente inicializado y listo para usar.
- **Facilita las pruebas**: Al inyectar las dependencias a través del constructor, es más fácil sustituirlas por objetos simulados (mocks) durante las pruebas unitarias.
- **Clases más limpias**: Se evita el uso de setters, lo que reduce el riesgo de que un objeto se quede en un estado inconsistente.

### Ejemplo Completo

Vamos a crear una pequeña aplicación que maneje usuarios. Tendremos tres componentes principales: un controlador (`UsuarioControlador`), un servicio (`UsuarioServicio`), y un repositorio (`UsuarioRepositorio`). 

### Paso 1: Definir la Entidad Usuario

Primero, definimos una clase simple `Usuario`.

```java
public class Usuario {
    private int id;
    private String nombre;

    public Usuario(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
```

### Paso 2: Crear el Repositorio

El repositorio manejará las operaciones de acceso a datos para la entidad `Usuario`. Utilizaremos la anotación `@Repository` para marcar esta clase como un componente que maneja la persistencia.

```java
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UsuarioRepositorio {
    private final Map<Integer, Usuario> usuarios = new HashMap<>();

    public UsuarioRepositorio() {
        // Inicializamos algunos usuarios
        usuarios.put(1, new Usuario(1, "Juan Pérez"));
        usuarios.put(2, new Usuario(2, "Ana Gómez"));
    }

    public Usuario encontrarPorId(int id) {
        return usuarios.get(id);
    }
}
```

### Paso 3: Crear el Servicio

El servicio contendrá la lógica de negocio relacionada con los usuarios. Utilizaremos la anotación `@Service` para marcar esta clase como un componente de servicio.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {
    private final UsuarioRepositorio usuarioRepositorio;

    // Inyección de dependencias a través del constructor
    @Autowired
    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Usuario obtenerUsuarioPorId(int id) {
        return usuarioRepositorio.encontrarPorId(id);
    }
}
```

### Paso 4: Crear el Controlador

El controlador manejará las solicitudes web y usará el servicio para obtener los datos del usuario. Utilizaremos la anotación `@Controller` para marcar esta clase como un componente de controlador.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UsuarioControlador {
    private final UsuarioServicio usuarioServicio;

    // Inyección de dependencias a través del constructor
    @Autowired
    public UsuarioControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("/usuario/{id}")
    @ResponseBody
    public String obtenerUsuario(@PathVariable int id) {
        Usuario usuario = usuarioServicio.obtenerUsuarioPorId(id);
        if (usuario != null) {
            return "Usuario encontrado: " + usuario.getNombre();
        } else {
            return "Usuario no encontrado.";
        }
    }
}
```

### Paso 5: Configurar la Aplicación

Asegúrate de que tu aplicación Spring esté configurada para escanear componentes, generalmente mediante la anotación `@SpringBootApplication` en tu clase principal.

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Aplicacion {
    public static void main(String[] args) {
        SpringApplication.run(Aplicacion.class, args);
    }
}
```

### Explicación del Ejemplo

1. **Entidad `Usuario`**: Representa un usuario con un ID y un nombre.
2. **Repositorio `UsuarioRepositorio`**: Utiliza un `HashMap` para simular el almacenamiento de usuarios. Tiene un método `encontrarPorId` para obtener un usuario por su ID.
3. **Servicio `UsuarioServicio`**: Dependiendo de `UsuarioRepositorio`, este servicio utiliza la inyección por constructor para recibir una instancia de `UsuarioRepositorio`. Tiene un método `obtenerUsuarioPorId` que delega la llamada al repositorio.
4. **Controlador `UsuarioControlador`**: Dependiendo de `UsuarioServicio`, este controlador utiliza la inyección por constructor para recibir una instancia de `UsuarioServicio`. Maneja las solicitudes HTTP GET para `/usuario/{id}` y utiliza el servicio para devolver el nombre del usuario correspondiente.
5. **Configuración de la Aplicación**: La clase `Aplicacion` arranca la aplicación Spring, que escaneará los componentes anotados.

## Usando @Qualifier

La anotación `@Qualifier` se utiliza en Spring para resolver ambigüedades cuando hay múltiples beans del mismo tipo en el contexto de la aplicación. Permite especificar cuál bean debe ser inyectado cuando hay más de uno que cumple con el tipo requerido. Esto es particularmente útil en la inyección de dependencias por constructor o por métodos setter.

### ¿Por qué usar `@Qualifier`?

- **Resolución de ambigüedades**: Si tienes múltiples beans del mismo tipo, `@Qualifier` te permite especificar cuál deseas inyectar.
- **Claridad**: Facilita la comprensión de qué bean se está utilizando, mejorando la legibilidad del código.

### Ejemplo de Uso con `@Bean` y `@Qualifier`

Vamos a crear un ejemplo en el que tenemos dos implementaciones diferentes de una interfaz de servicio llamada `NotificacionServicio`. Usaremos `@Bean` para definir ambos servicios y `@Qualifier` para inyectar uno de ellos en un controlador.

#### Paso 1: Definir la Interfaz

```java
public interface NotificacionServicio {
    void enviarNotificacion(String mensaje);
}
```

#### Paso 2: Implementaciones de la Interfaz

Definamos dos implementaciones de esta interfaz: `NotificacionEmail` y `NotificacionSMS`.

```java
import org.springframework.stereotype.Service;

@Service
public class NotificacionEmail implements NotificacionServicio {
    @Override
    public void enviarNotificacion(String mensaje) {
        System.out.println("Enviando notificación por Email: " + mensaje);
    }
}

@Service
public class NotificacionSMS implements NotificacionServicio {
    @Override
    public void enviarNotificacion(String mensaje) {
        System.out.println("Enviando notificación por SMS: " + mensaje);
    }
}
```

#### Paso 3: Configuración de Beans con `@Bean`

Ahora, vamos a definir ambos servicios como beans en una clase de configuración y utilizar `@Qualifier` para darles nombres específicos.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionNotificaciones {

    @Bean
    @Qualifier("email")
    public NotificacionServicio notificacionEmail() {
        return new NotificacionEmail();
    }

    @Bean
    @Qualifier("sms")
    public NotificacionServicio notificacionSMS() {
        return new NotificacionSMS();
    }
}
```

En este caso, hemos definido dos beans de `NotificacionServicio` y les hemos dado los nombres `"email"` y `"sms"` mediante la anotación `@Qualifier`.

#### Paso 4: Inyección en el Controlador

Ahora, en nuestro controlador, podemos inyectar una de las implementaciones utilizando `@Qualifier`.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NotificacionControlador {
    private final NotificacionServicio notificacionServicio;

    // Inyectamos el servicio de notificación por email
    @Autowired
    public NotificacionControlador(@Qualifier("email") NotificacionServicio notificacionServicio) {
        this.notificacionServicio = notificacionServicio;
    }

    @GetMapping("/enviar")
    @ResponseBody
    public String enviarNotificacion() {
        notificacionServicio.enviarNotificacion("Hola, este es un mensaje de prueba.");
        return "Notificación enviada!";
    }
}
```

#### Explicación del Código:

1. **Interfaz `NotificacionServicio`**: Define el método `enviarNotificacion`.
2. **Implementaciones**: `NotificacionEmail` y `NotificacionSMS` implementan la interfaz, proporcionando diferentes formas de enviar notificaciones.
3. **Configuración**: En `ConfiguracionNotificaciones`, definimos dos beans y les asignamos nombres mediante `@Qualifier`.
4. **Controlador**: En `NotificacionControlador`, inyectamos la implementación de `NotificacionServicio` que corresponde a `"email"` utilizando `@Qualifier`. Al acceder a la ruta `/enviar`, se enviará una notificación por email.

La anotación `@Primary` en Spring se utiliza para indicar que un bean debe ser considerado como la opción principal cuando hay múltiples beans del mismo tipo disponibles en el contexto de la aplicación. Cuando se utiliza en combinación con `@Bean`, se le está diciendo a Spring que este bean debe ser preferido sobre otros beans del mismo tipo al inyectar dependencias.

## Usando @Primary

### ¿Por qué usar `@Primary`?

- **Preferencia en la inyección**: Cuando hay más de un bean del mismo tipo, `@Primary` permite especificar cuál se debe utilizar como predeterminado.
- **Simplicidad**: Reduce la necesidad de utilizar `@Qualifier` para cada inyección, simplificando el código.

### Ejemplo Completo de Uso con `@Primary`

Vamos a crear un ejemplo en el que tenemos dos implementaciones diferentes de una interfaz de servicio llamada `PagoServicio`. Definiremos una de ellas como la implementación primaria usando `@Primary`.

#### Paso 1: Definir la Interfaz

Primero, definamos una interfaz de servicio:

```java
public interface PagoServicio {
    void procesarPago(double monto);
}
```

#### Paso 2: Implementaciones de la Interfaz

Luego, crearemos dos implementaciones de la interfaz: `PagoTarjeta` y `PagoPayPal`.

```java
import org.springframework.stereotype.Service;

@Service
public class PagoTarjeta implements PagoServicio {
    @Override
    public void procesarPago(double monto) {
        System.out.println("Procesando pago de " + monto + " por tarjeta.");
    }
}

@Service
public class PagoPayPal implements PagoServicio {
    @Override
    public void procesarPago(double monto) {
        System.out.println("Procesando pago de " + monto + " por PayPal.");
    }
}
```

#### Paso 3: Configuración de Beans con `@Bean` y `@Primary`

Ahora, definimos ambos servicios en una clase de configuración y utilizamos `@Primary` en la implementación de tarjeta para que sea la preferida.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConfiguracionPagos {

    @Bean
    @Primary  // Indica que este bean es el preferido
    public PagoServicio pagoTarjeta() {
        return new PagoTarjeta();
    }

    @Bean
    public PagoServicio pagoPayPal() {
        return new PagoPayPal();
    }
}
```

#### Paso 4: Inyección en el Controlador

Finalmente, inyectamos el servicio en un controlador.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PagoControlador {
    private final PagoServicio pagoServicio;

    // Inyección automática del servicio preferido (pagoTarjeta)
    @Autowired
    public PagoControlador(PagoServicio pagoServicio) {
        this.pagoServicio = pagoServicio;
    }

    @GetMapping("/pagar")
    @ResponseBody
    public String realizarPago(@RequestParam double monto) {
        pagoServicio.procesarPago(monto);
        return "Pago procesado!";
    }
}
```

#### Explicación del Código:

1. **Interfaz `PagoServicio`**: Define el método `procesarPago`.
2. **Implementaciones**: `PagoTarjeta` y `PagoPayPal` implementan la interfaz, ofreciendo diferentes métodos de pago.
3. **Configuración**: En `ConfiguracionPagos`, usamos `@Bean` para definir ambos servicios. `pagoTarjeta` se marca con `@Primary`, lo que lo convierte en la implementación preferida.
4. **Controlador**: En `PagoControlador`, inyectamos `PagoServicio`. Debido a `@Primary`, Spring inyectará automáticamente una instancia de `PagoTarjeta` cuando se requiera un `PagoServicio`. Al acceder a la ruta `/pagar`, se procesará el pago usando la implementación preferida.

[Continuar a la parte 2](2_Notas_Teoria_2.md)