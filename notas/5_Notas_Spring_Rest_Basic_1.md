# Antes de comenzar

Eliminamos todas las clases antes creadas y los properties, solo dejando la principal `ExampleApplication` y el archivo `application.properties`.

# Primer Controller, @RestController, @GetMapping y @ResponseStatus

Se crea la clase `FirstController`:

```java
package com.alibou.example;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
public class FirstController {
  @GetMapping("hello")
  public String sayHello() {
    return "Hellor from FirstController";
  }

  @GetMapping("hello2")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public String sayHello2() {
    return "Hellor from FirstController 2";
  }
}
```

Este código es un ejemplo básico de una **API REST** en **Spring Boot**, donde se crean dos endpoints que responden a solicitudes HTTP del tipo `GET`. A continuación te explico cada parte:

### 1. **Anotación `@RestController`**:
   - Esta anotación indica que la clase **`FirstController`** es un **controlador REST**, lo cual significa que va a manejar peticiones HTTP y devolver respuestas en formato texto o JSON (en este caso, simplemente texto).
   - Combina la funcionalidad de `@Controller` y `@ResponseBody`, es decir, automáticamente convierte la respuesta a un formato adecuado (normalmente JSON o texto plano).

### 2. **Método `sayHello` con `@GetMapping("hello")`**:
   - **`@GetMapping("hello")`**: Esta anotación indica que este método se activará cuando alguien realice una solicitud **HTTP GET** a la ruta **`/hello`** del servidor.
   - **`sayHello()`**: Este método se ejecuta cuando se accede a la URL `/hello`. Lo único que hace es devolver el texto `"Hellor from FirstController"`.
   - **Devuelve un `String`**: Este `String` será el cuerpo de la respuesta HTTP, es decir, lo que el cliente verá en su navegador o al realizar una solicitud.

### 3. **Método `sayHello2` con `@GetMapping("hello2")` y `@ResponseStatus(HttpStatus.ACCEPTED)`**:
   - **`@GetMapping("hello2")`**: Similar al caso anterior, esta anotación define que este método responderá a solicitudes **HTTP GET**, pero en este caso a la ruta **`/hello2`**.
   - **`@ResponseStatus(HttpStatus.ACCEPTED)`**: Esta anotación se utiliza para indicar explícitamente qué **código de estado HTTP** devolverá este endpoint. En este caso, el código de estado será **202 Accepted**, lo que indica que la solicitud fue recibida y entendida, pero que la acción aún no ha sido completada (aunque en este ejemplo simple, la respuesta es inmediata).
     - Por defecto, si no se especifica esta anotación, Spring devuelve un **200 OK**, que significa que la solicitud fue exitosa.
   - **Método `sayHello2()`**: Este método devuelve el texto `"Hellor from FirstController 2"` como respuesta.

### ¿Qué es una API REST?

Una **API REST** (Representational State Transfer) permite a los diferentes sistemas comunicarse a través de peticiones HTTP. Los métodos más comunes de HTTP incluyen:

- **GET**: Para leer datos.
- **POST**: Para enviar datos y crear nuevos recursos.
- **PUT**: Para actualizar recursos.
- **DELETE**: Para eliminar recursos.

### ¿Cómo funciona en este ejemplo?

- Cuando se hace una solicitud HTTP GET a **`/hello`**, el servidor devuelve un texto plano con `"Hellor from FirstController"`.
- Cuando se accede a **`/hello2`**, el servidor también devuelve un texto, pero además responde con un código de estado 202 en lugar del 200 por defecto.

# Primer POST y @RequestBody

Se modifica el códgio de la siguiente manera:

```java
package com.alibou.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {
  @GetMapping("/hello")
  public String sayHello() {
    return "Hellor from FirstController";
  }

  @PostMapping("/post")
  public String post(String message) {
    return "Message received: " + message;
  }
}
```

Si hacemos una solicitud HTTP POST a la ruta `/post`, procuramos usar el Body pero en la sección de Form, ya que de otra manera no recibira el mensaje.

Si queremos hacer uso de otras formas de envar datos, podemos usar la anotación `@RequestBody`:

```java
package com.alibou.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {
  @GetMapping("/hello")
  public String sayHello() {
    return "Hellor from FirstController";
  }

  @PostMapping("/post")
  public String post(@RequestBody String message) {
    return "Message received: " + message;
  }
}
```

Con dicha anotación, el cliente podrá enviar el mensaje en cualquier formato disponible, como por ejemplo JSON o XML.

# Recibiendo un objeto

Se crea la clase Order

```java
package com.alibou.example;

public class Order {
  private String customerName;
  private String productName;
  private int quantity;

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return "Order [customerName=" + customerName + ", productName=" + productName + ", quantity=" + quantity + "]";
  }
}
```

Mientras que en el Controller se agrega

```java
@PostMapping("/post-order")
public String postOrder(@RequestBody Order order) {
  return "Order received: " + order.toString();
}
```

### Explicación de `/post-order`:

#### 1. **Método `postOrder` con `@PostMapping("/post-order")`**:
   - **`@PostMapping("/post-order")`**: Esta anotación indica que este método se ejecutará cuando el servidor reciba una solicitud **HTTP POST** en la ruta **`/post-order`**.
   - **`@RequestBody Order order`**: Esta parte es clave. Le está diciendo a Spring que debe tomar el cuerpo de la solicitud **POST** (que contiene datos en formato JSON, XML u otro formato) y convertirlo en una instancia de la clase **`Order`**. Esta conversión automática la maneja Spring, siempre que los datos recibidos coincidan con los atributos de la clase `Order`.

#### 2. **Clase `Order`**:
   - La clase `Order` define un objeto que tiene tres propiedades:
     - **`customerName`**: nombre del cliente (tipo `String`).
     - **`productName`**: nombre del producto (tipo `String`).
     - **`quantity`**: cantidad de producto (tipo `int`).
   - La clase tiene los métodos **getters** y **setters** necesarios para cada una de estas propiedades. Esto permite a Spring mapear los valores enviados en la solicitud a los atributos del objeto `Order`.
   - También tiene un método **`toString()`** que genera una representación en texto del objeto `Order`. Esto es útil cuando queremos ver la información del pedido de manera legible.

#### 3. **Flujo de una solicitud a `/post-order`**:
   1. Un cliente envía una solicitud **POST** a la ruta `/post-order` con un cuerpo JSON que representa una orden. Ejemplo del JSON enviado:
      ```json
      {
        "customerName": "John Doe",
        "productName": "Laptop",
        "quantity": 2
      }
      ```
   2. Spring recibe este JSON y, gracias a la anotación **`@RequestBody`**, lo convierte en un objeto de la clase **`Order`**. Así, los valores de **`customerName`**, **`productName`** y **`quantity`** se asignan a las respectivas propiedades del objeto `Order`.
   3. El método **`postOrder()`** recibe este objeto `Order` y devuelve una cadena de texto que incluye el resultado de llamar al método `toString()` del objeto. La respuesta sería algo como:
      ```
      Order received: Order [customerName=John Doe, productName=Laptop, quantity=2]
      ```

### Resumen del flujo:

1. **El cliente envía un objeto JSON** en el cuerpo de la solicitud HTTP POST.
2. **Spring Boot mapea automáticamente ese JSON a un objeto Java**, en este caso, un objeto de la clase `Order`.
3. **El servidor responde con una representación de ese objeto**, confirmando que ha recibido el pedido.

### Ventajas:
- Este enfoque permite enviar y recibir **objetos más complejos** en las solicitudes y respuestas, en lugar de solo cadenas de texto. Esto es muy útil para manejar entidades reales en una aplicación, como pedidos, usuarios, productos, etc.
- **`@RequestBody`** hace el trabajo pesado de convertir los datos entrantes al tipo correcto, lo que facilita el manejo de datos estructurados.

# Customizando nombres para el JSON con @JsonProperty

Se modifica el códgio de la siguiente manera:

```java
package com.alibou.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {
  @JsonProperty("c-name")
  private String customerName;

  @JsonProperty("p-name")
  private String productName;

  @JsonProperty("quantity")
  private int quantity;

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return "Order [customerName=" + customerName + ", productName=" + productName + ", quantity=" + quantity + "]";
  }
}
```

Se utiliza la etiqueta @JsonProperty para indicar que el nombre del campo en el JSON debe ser "c-name" en lugar de "customerName" y "p-name" en lugar de "productName". Modificamos el JSON enviado para que coincida con estos nombres y probamos.

# Record

Creamos OrderRecord

```java
package com.alibou.example;

public record OrderRecord(
    String customerName,
    String productName,
    int quantity) {

}
```

Agregamos al controller el siguiente método:

```java
@PostMapping("/post-order-record")
public String postOrderRecord(@RequestBody OrderRecord order) {
  return "Order received: " + order.toString();
}
```

El nuevo código introduce el uso de **records** en Java con el objetivo de simplificar la estructura de datos para las APIs. En lugar de usar una clase tradicional como `Order`, ahora se utiliza un **record**, que es una forma concisa y moderna de definir clases de datos inmutables en Java. Vamos a centrarnos en el endpoint `/post-order-record` y cómo trabaja con el **`OrderRecord`**.

### Explicación del `OrderRecord`:

#### 1. **¿Qué es un record?**
   - Los **records** en Java son una característica introducida en **Java 14** para definir de manera sencilla y concisa **clases inmutables**. En lugar de escribir una clase completa con atributos, constructores, getters, y métodos como `equals`, `hashCode` o `toString`, un **record** lo genera automáticamente por ti.
   - En este caso, **`OrderRecord`** es un record que define tres campos:
     - **`customerName`**: nombre del cliente (tipo `String`).
     - **`productName`**: nombre del producto (tipo `String`).
     - **`quantity`**: cantidad del producto (tipo `int`).

   ```java
   public record OrderRecord(
       String customerName,
       String productName,
       int quantity) {}
   ```

   Este record automáticamente proporciona:
   - Un **constructor** que toma los tres parámetros y los asigna a los campos.
   - Los **getters** para cada campo, que en el caso de records no tienen prefijo `get`, sino que son simplemente `customerName()`, `productName()`, y `quantity()`.
   - Un método **`toString()`** que devuelve una representación legible del objeto.

#### 2. **Endpoint `/post-order-record`**:
   - Este es el nuevo endpoint que maneja solicitudes **POST** en la ruta **`/post-order-record`**.
   - La anotación **`@PostMapping("/post-order-record")`** indica que este método responderá a solicitudes POST.
   - **`@RequestBody OrderRecord order`**: Aquí, en lugar de recibir un objeto de la clase `Order`, se recibe un objeto del **`OrderRecord`**. Al igual que con las clases, Spring convierte automáticamente el **JSON** recibido en una instancia de `OrderRecord`, gracias a **`@RequestBody`**.

#### 3. **Flujo de `/post-order-record`**:
   1. Un cliente envía una solicitud **POST** a la ruta `/post-order-record` con un cuerpo en formato **JSON** que contiene los datos de un pedido, por ejemplo:
      ```json
      {
        "customerName": "Alice",
        "productName": "Smartphone",
        "quantity": 3
      }
      ```
   2. Spring convierte este JSON en una instancia de **`OrderRecord`**, mapeando automáticamente los campos del JSON a los campos del record.
   3. El método **`postOrderRecord()`** recibe el `OrderRecord` y utiliza el método `toString()` del record para generar una respuesta legible. En este caso, la respuesta sería algo como:
      ```
      Order received: OrderRecord[customerName=Alice, productName=Smartphone, quantity=3]
      ```

### Diferencias clave entre **`Order`** y **`OrderRecord`**:

- **Simplicidad**: Usar un record reduce el código significativamente. En lugar de definir manualmente getters, setters, y `toString()`, el record se encarga de todo.
- **Inmutabilidad**: Los records son **inmutables** por defecto. Esto significa que, una vez creado, el estado del objeto no puede cambiar. Esto puede ser útil en muchas situaciones donde quieres evitar que los datos se modifiquen después de ser creados.
- **Uso más claro y limpio**: En lugar de tener métodos como `getCustomerName()`, simplemente usas `customerName()`, lo cual es más directo.

### Ventajas del uso de `OrderRecord`:
- **Menos código repetitivo**: No necesitas escribir métodos que Java ya puede generar automáticamente.
- **Eficiencia y legibilidad**: Hace que el código sea más fácil de leer y mantener.
- **Más seguridad**: Al ser inmutable, reduce el riesgo de cambios inesperados en los datos.

# Recibiendo query params

Agregamos un nuevo método al controlador:

```java
@GetMapping("/hello-query")
public String sayHelloQuery(@RequestParam String name) {
  return "Hello " + name;
}
```

Este nuevo método en el controlador, **`sayHelloName`**, está diseñado para manejar solicitudes **GET** a la ruta **`/hello/{name}`**, donde **`{name}`** es una variable que puede cambiar según lo que el cliente envíe en la URL.

### Explicación paso a paso:

1. **Anotación `@GetMapping("/hello/{name}")`**:
   - Indica que este método responderá a solicitudes **GET**.
   - La ruta **`/hello/{name}`** tiene una parte variable: **`{name}`**. Esto significa que cualquier valor colocado en esa parte de la URL será capturado y pasado al método como un parámetro.

2. **Uso de `@PathVariable`**:
   - **`@PathVariable String name`**: La anotación **`@PathVariable`** se utiliza para decirle a Spring que el valor dentro de **`{name}`** en la URL debe ser capturado y asignado al parámetro **`name`** del método.
   - Por ejemplo, si se hace una solicitud a **`/hello/Alice`**, el valor `"Alice"` será capturado y asignado a la variable **`name`**.

3. **Lógica del método**:
   - El método simplemente toma el valor de **`name`** y lo concatena con la cadena `"Hello "`, devolviendo un saludo personalizado.
   - Si un cliente hace una solicitud a **`/hello/Alice`**, la respuesta será:
     ```text
     Hello Alice
     ```

4. **Ejemplo de solicitud**:
   - **URL**: `http://localhost:8080/hello/Alice`
   - **Respuesta**: `Hello Alice`

Otra forma de definir el metodo:

```java
@GetMapping("/hello/{user-name}")
public String sayHelloName(@PathVariable("user-name") String name) {
  return "Hello " + name;
}
```

En este caso, se utiliza la sintaxis **`@PathVariable("user-name")`** para indicar que el valor de **`{user-name}`** en la URL debe ser capturado y asignado al parámetro **`name`** del método.

# @RequestParam

Se agrega el método siguiente al controlador:

```java
@GetMapping("/hello3")
public String sayHelloFullName(
    @RequestParam("user-name") String name,
    @RequestParam("last-name") String lastName) {
  return "Hello " + name + " " + lastName;
}
```

Este nuevo método **`sayHelloFullName`** en el controlador permite manejar solicitudes **GET** que aceptan parámetros de consulta en la URL para construir un saludo completo con nombre y apellido.

### Explicación paso a paso:

1. **Anotación `@GetMapping("/hello3")`**:
   - Indica que este método responderá a solicitudes **GET** enviadas a la ruta **`/hello3`**.
   - A diferencia del método anterior, este no tiene variables en la ruta, sino que recibe parámetros de consulta en la URL.

2. **Uso de `@RequestParam`**:
   - **`@RequestParam("user-name") String name`**: Captura el parámetro de consulta **`user-name`** de la URL y lo asigna a la variable **`name`**.
   - **`@RequestParam("last-name") String lastName`**: Captura el parámetro de consulta **`last-name`** de la URL y lo asigna a la variable **`lastName`**.
   - Estos parámetros de consulta deben estar presentes en la URL como parte de la solicitud, con una estructura como esta: **`/hello3?user-name=John&last-name=Doe`**.

3. **Lógica del método**:
   - El método toma el valor de los parámetros **`user-name`** y **`last-name`** y los concatena con la cadena `"Hello "`, devolviendo un saludo completo con el nombre y el apellido.
   - Por ejemplo, si los parámetros son **`user-name=John`** y **`last-name=Doe`**, la respuesta será:
     ```text
     Hello John Doe
     ```

4. **Ejemplo de solicitud**:
   - **URL**: `http://localhost:8080/hello3?user-name=John&last-name=Doe`
   - **Respuesta**: `Hello John Doe`

# Diferencias entre @RequestParam y @PathVariable

### 1. **Ubicación en la URL:**

- **`@PathVariable`**:
  - Extrae valores de **la ruta** directamente.
  - Los parámetros son parte del **segmento de la URL**.
  - Ejemplo: Para la ruta `/hello/{name}`, el valor de `name` se obtiene directamente de la URL como parte de su estructura.
  - **Ejemplo de uso**:
    ```java
    @GetMapping("/hello/{name}")
    public String sayHello(@PathVariable String name) {
        return "Hello " + name;
    }
    ```
    **URL**: `http://localhost:8080/hello/John` → Respuesta: `Hello John`

- **`@RequestParam`**:
  - Extrae valores de **los parámetros de consulta**.
  - Los parámetros son **opciones en la URL** que vienen después del símbolo `?`.
  - Ejemplo: Para la ruta `/hello3`, los parámetros se pasarían en la URL como `/hello3?user-name=John&last-name=Doe`.
  - **Ejemplo de uso**:
    ```java
    @GetMapping("/hello3")
    public String sayHello(@RequestParam("user-name") String name, @RequestParam("last-name") String lastName) {
        return "Hello " + name + " " + lastName;
    }
    ```
    **URL**: `http://localhost:8080/hello3?user-name=John&last-name=Doe` → Respuesta: `Hello John Doe`

### 2. **Sintaxis en la URL:**

- **`@PathVariable`**:
  - Los valores van directamente dentro de la ruta en la URL, reemplazando un placeholder.
  - Ejemplo: `/hello/John`, donde `John` es el valor de la variable de ruta.
  
- **`@RequestParam`**:
  - Los valores son parte de los parámetros de consulta, después del símbolo `?`, y cada uno se especifica en pares clave-valor.
  - Ejemplo: `/hello3?user-name=John&last-name=Doe`.

### 3. **Obligatoriedad:**

- **`@PathVariable`**:
  - Es **obligatorio** cuando se define como parte de la ruta, ya que la ruta debe coincidir con la plantilla definida.
  
- **`@RequestParam`**:
  - Puede ser **opcional** si se define con un valor por defecto o se usa el atributo `required = false`.
  - Ejemplo:
    ```java
    @GetMapping("/hello3")
    public String sayHello(
        @RequestParam(value = "user-name", required = false, defaultValue = "Guest") String name) {
        return "Hello " + name;
    }
    ```

### 4. **Caso de uso:**

- **`@PathVariable`**:
  - Se usa cuando los datos son parte esencial de la URL.
  - Ejemplo: `/users/{id}` para acceder a un usuario específico según su `id`.

- **`@RequestParam`**:
  - Se usa cuando los datos son parámetros adicionales u opcionales que afectan la solicitud.
  - Ejemplo: Filtros de búsqueda o paginación, como `/search?query=Spring&page=1`.

### Resumen:

| Característica          | `@PathVariable`                             | `@RequestParam`                           |
| ----------------------- | ------------------------------------------- | ----------------------------------------- |
| **Ubicación en la URL** | Parte de la ruta (segmento del path)        | Parámetros de consulta (query string)     |
| **Sintaxis**            | `/hello/{name}`                             | `/hello3?user-name=John&last-name=Doe`    |
| **Obligatoriedad**      | Siempre necesario (al ser parte de la ruta) | Puede ser opcional                        |
| **Casos de uso**        | Identificación de recursos específicos      | Filtros, paginación, o valores opcionales |

# Limpiando codigo para nuevos temas

Borramos OrderRecord y Order, mientras que en FirstController eliminamos todos los metodos a excepción de el primer get y el primer post.

# PostgreSQL

Instalamos PostgreSQL en Windows y nos conectamos a la base de datos con VSCode. Creamos una nueva BD de nombre `demo_db`.

# Agregando dependencias a Spring

De momento solo agregamos la dependenci de JPA:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>runtime</scope>
</dependency>
```

El application.properties lo volveremos un yaml y agregaremos las siguientes propiedades:

```yaml	
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/demo_db
    username: postgres
    password: Pantera09?
    driver-class-name: org.postgresql.Driver
```

Ejecutamos la aplicación para serciorarnos de que todo funciona correctamente.

# Creando una tabla

Creamos una nueva clase de nombre Student:

```java
package com.alibou.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Student {

  @Id
  private Integer id;
  private String name;
  private String lastname;
  private String email;
  private int age;

  // Constructors
  public Student() {
  }

  public Student(String name, String lastname, String email, int age) {
    this.name = name;
    this.lastname = lastname;
    this.email = email;
    this.age = age;
  }

  // Getters and Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}
```

Si compilamos la aplicación, no creara la tabla ya que faltan configuraciones por agregar al application.yaml:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/demo_db
    username: postgres
    password: Pantera09?
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    database: postgresql
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

## Explicaciones

### 1. **Archivo `application.yaml`: Configuración de la base de datos**

El archivo `application.yaml` se utiliza para configurar las propiedades de la aplicación en Spring Boot. Aquí estamos configurando una conexión a una base de datos PostgreSQL.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/demo_db
    username: postgres
    password: Pantera09?
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    database: postgresql
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

Si ejecutamos la aplicación veremos en consola la query que ejecuta para crear la tabla y veremos en la BD que efectivamente se creó la tabla `Student`.

#### **Explicación:**

- **`datasource`**:
  - **`url`**: Define la URL de conexión de la base de datos. En este caso, estamos conectándonos a una base de datos PostgreSQL que se ejecuta localmente en el puerto `5432`, con el nombre de base de datos `demo_db`.
  - **`username`**: El nombre de usuario de la base de datos, que en este caso es `postgres`.
  - **`password`**: La contraseña del usuario de la base de datos.
  - **`driver-class-name`**: Define el controlador JDBC que se utilizará para la conexión, que en este caso es `org.postgresql.Driver` para PostgreSQL.

- **`jpa`**: Configuración relacionada con JPA (Java Persistence API), que se encarga de gestionar la interacción con la base de datos usando objetos de Java.
  - **`hibernate.ddl-auto`**: Define cómo manejar el esquema de la base de datos. El valor `create` significa que cada vez que inicie la aplicación, se **crearán** las tablas de la base de datos desde cero (borrando las tablas si existen). Esto es útil en ambientes de desarrollo, pero **no recomendado** en producción.
  - **`show-sql`**: Si es `true`, muestra las consultas SQL que Hibernate ejecuta en la consola. Esto es útil para depurar o verificar qué consultas se están generando.
  - **`properties.hibernate.format_sql`**: Hace que el SQL mostrado sea más legible, formateándolo adecuadamente.
  - **`database`** y **`database-platform`**: Especifica que se está utilizando una base de datos PostgreSQL, con el dialecto correspondiente (`PostgreSQLDialect`), que Hibernate utiliza para generar SQL compatible con PostgreSQL.

---

### 2. **Clase `Student`: Definición de la Entidad**

```java
package com.alibou.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Student {

  @Id
  private Integer id;
  private String name;
  private String lastname;
  private String email;
  private int age;

  // Constructors
  public Student() {
  }

  public Student(String name, String lastname, String email, int age) {
    this.name = name;
    this.lastname = lastname;
    this.email = email;
    this.age = age;
  }

  // Getters and Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  // Demas metodos getters y setters

  public void setEmail(String email) {
    this.email = email;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}
```

Esta clase define un **modelo de datos** llamado `Student`, que representa a un estudiante con varios atributos. Al ser una **entidad**, esta clase se corresponde con una tabla en la base de datos.

- **`@Entity`**: Esta anotación marca la clase como una **entidad de JPA**, lo que significa que Spring gestionará esta clase como una tabla en la base de datos. Cada instancia de esta clase corresponderá a una fila de la tabla.
- **`@Id`**: Marca el campo `id` como la **clave primaria** de la tabla. Este campo identificará de manera única a cada estudiante.

#### **Atributos:**
- **`id`**: Es la clave primaria, que debe ser un valor único por cada estudiante.
- **`name`**: El nombre del estudiante.
- **`lastname`**: El apellido del estudiante.
- **`email`**: El correo electrónico del estudiante.
- **`age`**: La edad del estudiante.

#### **Constructores:**
- **Constructor sin parámetros (`Student()`)**: Es necesario para que JPA pueda crear instancias de la clase de forma automática.
- **Constructor con parámetros**: Permite crear instancias de la clase `Student` pasando los valores de sus atributos al momento de su creación.

#### **Getters y Setters:**
- Los **getters** permiten acceder a los valores de los atributos.
- Los **setters** permiten modificar los valores de los atributos.
  
Estos métodos son fundamentales en una aplicación orientada a objetos, ya que permiten acceder y modificar los datos de manera controlada.

---

### **Resumiendo:**

1. **Configuración de la base de datos**: Se ha configurado una conexión a una base de datos PostgreSQL en el archivo `application.yaml`, indicando las credenciales y el tipo de base de datos.
2. **Entidad `Student`**: Esta clase mapea la tabla `Student` en la base de datos y define los atributos que tendrán las filas (estudiantes), como su nombre, apellido, email y edad. Además, cuenta con un constructor, getters y setters para manejar la creación y manipulación de los datos de los estudiantes.

# @Table

Agregamos lo siguiente a la clase Student:

```java
package com.alibou.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "T_STUDENT") // <-- Agregamos esta línea
public class Student {

  @Id
  private Integer id;
  private String name;
  private String lastname;
  private String email;
  private int age;

//....
```

Cuando agregamos la anotación `@Table` en una clase marcada como `@Entity` en JPA, como en el ejemplo modificado, estamos especificando detalles adicionales sobre cómo esa clase debe ser mapeada a una tabla en la base de datos. A continuación, te explico la diferencia entre usar solo `@Entity` y agregar `@Table`, así como otros conceptos importantes que pueden ser útiles.

### **Explicación de `@Table`:**

- **`@Entity`**: Como mencionamos antes, marca la clase como una **entidad** en JPA. Esto significa que la clase `Student` se corresponderá con una tabla en la base de datos. Cuando usamos **solo `@Entity`**, el nombre de la tabla en la base de datos será igual al nombre de la clase **por defecto**, es decir, en este caso, la tabla se llamaría `Student`.

- **`@Table`**: Esta anotación te permite especificar detalles adicionales sobre la tabla de la base de datos, como el nombre de la tabla. Al usar `@Table`, podemos **personalizar** cómo queremos que se llame la tabla en la base de datos.
  
  - **name**: En este caso, estamos diciendo que la clase `Student` debe mapearse a una tabla llamada `"T_STUDENT"`, en lugar de usar el nombre por defecto `"Student"`.

### **Diferencias clave entre solo `@Entity` y agregar `@Table`:**

1. **Sin `@Table`** (solo con `@Entity`):
   - JPA automáticamente mapea la entidad a una tabla cuyo nombre es el mismo que el de la clase. En este caso, la tabla sería `Student`.
   - Si no deseas personalizar el nombre de la tabla o cualquier otro aspecto, solo `@Entity` es suficiente.

2. **Con `@Table`**:
   - Puedes personalizar el nombre de la tabla mediante el atributo `name`. En este caso, estamos especificando que la tabla se llamará `"T_STUDENT"`.
   - **Ventaja**: Te da más control sobre cómo se crean o asocian las tablas en la base de datos, lo cual es útil si ya tienes una base de datos con convenciones de nombres específicas (por ejemplo, si las tablas deben empezar con un prefijo o si las tablas tienen nombres diferentes al de las entidades).
   
   - Además de `name`, puedes personalizar otros aspectos de la tabla, como **`schema`** (el esquema de base de datos en el que reside la tabla) y **`catalog`** (una agrupación de esquemas en sistemas de bases de datos grandes).

### **Otros atributos importantes de `@Table`:**

- **`schema`**: Especifica el esquema en el que la tabla se encuentra dentro de la base de datos. Un esquema es una forma de organizar tablas dentro de una base de datos. Si no se especifica, JPA usará el esquema por defecto del usuario de la base de datos.
  
  ```java
  @Table(name = "T_STUDENT", schema = "school")
  ```

  Aquí, la tabla `T_STUDENT` residiría en el esquema `school`.

- **`catalog`**: En bases de datos más complejas (como MySQL), un **catálogo** es un nivel de organización por encima de los esquemas. Puedes especificar en qué catálogo debe residir la tabla.

  ```java
  @Table(name = "T_STUDENT", catalog = "university")
  ```

  Aquí, la tabla `T_STUDENT` residiría en el catálogo `university`.

---

### **Beneficios de usar `@Table`:**

1. **Control total del nombre de la tabla**: Si tienes convenciones específicas para el nombre de las tablas en tu organización o si estás trabajando con una base de datos existente, puedes asegurarte de que el nombre de la tabla en la base de datos se ajuste a esas convenciones.

2. **Flexibilidad con esquemas y catálogos**: En bases de datos complejas, `@Table` te permite indicar en qué esquema o catálogo específico se encuentra la tabla. Esto es especialmente útil cuando trabajas con grandes aplicaciones que usan múltiples esquemas o catálogos para organizar sus datos.

3. **Compatibilidad con bases de datos existentes**: Si estás integrando una aplicación con una base de datos ya existente (que no puedes cambiar), puedes mapear tus entidades a las tablas y esquemas correctos utilizando `@Table`.

---

### **Conclusión:**

- Si solo usas `@Entity`, JPA automáticamente asignará la tabla con el mismo nombre que la clase (`Student`).
- Al usar `@Table(name = "T_STUDENT")`, le estás indicando a JPA que en lugar de crear o usar una tabla llamada `Student`, debe usar una tabla llamada `T_STUDENT`.

# @Column

Se agrega la nota `@Column` a la clase Student:

```java
@Entity
@Table(name = "T_STUDENT")
public class Student {

  @Id
  private Integer id;

  @Column(name = "c_fname") // <-- Agregamos esta línea
  private String name;
```

Al agregar la anotación `@Column` en la clase `Student`, estamos indicando de manera explícita cómo se debe mapear el campo de una entidad a una columna específica en la tabla de la base de datos. A continuación te explico en detalle lo que significa esta anotación y cómo funciona, manteniendo una perspectiva clara y fácil de comprender.

### **¿Qué es `@Column`?**

La anotación **`@Column`** se usa en JPA (Java Persistence API) para definir cómo un campo de una clase se debe mapear a una columna en la base de datos. En este caso, estamos diciendo que el campo `name` de la clase `Student` se debe almacenar en una columna llamada **`c_fname`** en la tabla `T_STUDENT`.

Por defecto, si no usamos `@Column`, JPA asignará automáticamente el nombre del campo de la clase al nombre de la columna. Sin embargo, cuando usamos `@Column`, podemos **personalizar** cómo queremos que se almacenen los datos en la base de datos.

### **¿Qué sucede en este ejemplo?**

- **Sin `@Column`**: Si no usáramos `@Column`, JPA automáticamente intentaría mapear el campo `name` de la clase a una columna llamada `name` en la tabla `T_STUDENT`.
  
- **Con `@Column(name = "c_fname")`**: Al agregar esta anotación, estamos indicando explícitamente que, en lugar de usar una columna llamada `name`, queremos que el valor de `name` se almacene en una columna llamada **`c_fname`**.

### **¿Por qué usar `@Column`?**

1. **Personalización del nombre de la columna**: En muchas ocasiones, las bases de datos tienen convenciones de nombres diferentes a las usadas en las clases Java. Por ejemplo, en una base de datos heredada, las columnas podrían tener nombres abreviados o diferentes convenciones de formato, como prefijos (`c_` en este caso). Usar `@Column` permite **alinear la estructura de la base de datos con las entidades de Java** sin necesidad de cambiar la base de datos.

2. **Compatibilidad con bases de datos existentes**: Si estás integrando una aplicación con una base de datos existente, puedes ajustar tu clase Java para que coincida con las columnas de la base de datos sin cambiar los nombres de los campos de la clase. Esto es muy útil cuando no tienes control sobre los nombres de las columnas.

### **Opciones adicionales de `@Column`**

Además de `name`, `@Column` ofrece otros atributos que puedes usar para personalizar más aspectos del mapeo:

1. **`nullable`**: Indica si la columna puede ser `null` en la base de datos. Si `nullable = false`, la columna será `NOT NULL`, lo que significa que siempre debe tener un valor.

   ```java
   @Column(name = "c_fname", nullable = false)
   private String name;
   ```

   En este caso, la columna `c_fname` en la base de datos **no puede ser nula**, es decir, debe tener un valor en cada registro.

2. **`length`**: Define la longitud máxima de la columna, lo que es útil para los tipos de datos como `VARCHAR`. Por defecto, el valor suele ser 255, pero puedes ajustarlo.

   ```java
   @Column(name = "c_fname", length = 50)
   private String name;
   ```

   Aquí estamos especificando que la columna `c_fname` puede almacenar un valor de hasta **50 caracteres**.

3. **`unique`**: Si `unique = true`, establece una restricción de **unicidad** en la columna. Esto significa que no puede haber dos registros con el mismo valor en esa columna.

   ```java
   @Column(name = "email", unique = true)
   private String email;
   ```

   En este caso, la columna `email` en la base de datos no puede tener valores duplicados.

4. **`insertable` y `updatable`**: Controlan si la columna se puede usar en operaciones de **inserción** o **actualización**.
   - `insertable = false`: La columna no se incluirá al insertar nuevos registros.
   - `updatable = false`: La columna no se podrá modificar una vez que se haya insertado un valor.

   ```java
   @Column(name = "created_at", insertable = false, updatable = false)
   private LocalDateTime createdAt;
   ```

   En este ejemplo, la columna `created_at` no se puede modificar después de la creación del registro, ya que se usa solo para almacenar la fecha de creación y no se puede actualizar más adelante.

### **Ejemplo completo con atributos adicionales de `@Column`:**

```java
@Entity
@Table(name = "T_STUDENT")
public class Student {

  @Id
  private Integer id;

  @Column(name = "c_fname", nullable = false, length = 50) // No puede ser nulo y tiene longitud máxima de 50
  private String name;

  @Column(name = "email", unique = true) // Email debe ser único
  private String email;

  @Column(name = "created_at", insertable = false, updatable = false) // La fecha no puede ser modificada
  private LocalDateTime createdAt;

  // Getters y Setters
}
```

[Continuación](6_Notas_Spring_Rest_Basic_2.md)