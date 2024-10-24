# @GeneratedValue

Agregamos la anotación `@GeneratedValue` a la clase Student:

```java
@Entity
@Table(name = "T_STUDENT")
public class Student {

  @Id
  @GeneratedValue
  private Integer id;
  // ...
}
```

### **¿Qué es `@GeneratedValue`?**

`@GeneratedValue` es una anotación que le dice a JPA que el valor de la clave primaria **debe ser generado automáticamente**. Cuando guardamos una nueva entidad en la base de datos, **no necesitamos asignar manualmente un valor para la propiedad `id`**. En lugar de eso, la base de datos, con ayuda de JPA, se encargará de generar el valor único y adecuado para la clave primaria.

### **¿Cómo funciona?**

Cuando usamos `@GeneratedValue`, JPA delega la generación del valor de la clave primaria a la base de datos o a la propia JPA, dependiendo de la estrategia que se elija. Esto facilita el trabajo, ya que no tenemos que preocuparnos por asignar claves primarias únicas y consistentes manualmente.

### **Estrategias de generación de valores:**

La anotación `@GeneratedValue` puede utilizar diferentes estrategias para generar el valor de la clave primaria. Algunas de las más comunes son:

1. **AUTO** (por defecto):
   - JPA elige automáticamente la estrategia de generación según el tipo de base de datos que estés utilizando.
   - En la mayoría de los casos, esto significa que JPA dejará que la base de datos determine cómo generar el valor, pero podría usar otros métodos según el contexto.

   ```java
   @GeneratedValue(strategy = GenerationType.AUTO)
   ```

   **AUTO** es útil cuando no nos importa cómo se genera el valor y dejamos que JPA seleccione la estrategia más apropiada. Esta es la opción predeterminada si no especificamos ninguna estrategia.

2. **IDENTITY**:
   - Utiliza una columna auto-incremental en la base de datos. Es decir, la base de datos generará un valor único automáticamente, incrementándolo por cada nuevo registro.
   - Ideal para bases de datos que soportan columnas auto-incrementales, como MySQL o PostgreSQL.

   ```java
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   ```

   Aquí, la base de datos será responsable de generar un valor único para la columna `id` al insertar un nuevo registro. Es adecuado cuando quieres que la base de datos gestione por completo el incremento de las claves primarias.

3. **SEQUENCE**:
   - JPA utiliza una **secuencia** en la base de datos para generar el valor de la clave primaria. Las secuencias son objetos en la base de datos que generan valores consecutivos.
   - Este enfoque es muy común en bases de datos como Oracle y PostgreSQL.

   ```java
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
   @SequenceGenerator(name = "student_seq", sequenceName = "student_sequence", allocationSize = 1)
   ```

   En este caso, estamos utilizando una secuencia llamada `student_sequence`. Cada vez que se inserta un nuevo registro, la secuencia genera el siguiente valor disponible.

4. **TABLE**:
   - Esta estrategia utiliza una tabla especial en la base de datos que guarda los valores que se deben usar para las claves primarias.
   - Es menos común hoy en día, pero puede ser útil si tu base de datos no soporta secuencias o auto-incrementos.

   ```java
   @GeneratedValue(strategy = GenerationType.TABLE)
   ```

   Aquí, JPA mantiene una tabla especial donde guarda los próximos valores para las claves primarias y los asigna conforme se van creando nuevas entidades.

### **Explicación en el contexto del ejemplo:**

```java
@Entity
@Table(name = "T_STUDENT")
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // <-- Usamos la estrategia IDENTITY
  private Integer id;
}
```

En este ejemplo:

- Usamos `@GeneratedValue(strategy = GenerationType.IDENTITY)` para decirle a JPA que deje que la **base de datos maneje el incremento de los valores del campo `id`**. En bases de datos como PostgreSQL o MySQL, esto hará que la columna `id` sea auto-incremental, es decir, que cada nuevo registro tenga un `id` mayor que el anterior.
- No necesitamos asignar el `id` manualmente al crear un nuevo estudiante; JPA y la base de datos lo generarán automáticamente.

### **¿Qué pasa si no usamos `@GeneratedValue`?**

Si no usamos `@GeneratedValue`, JPA esperará que **nosotros mismos asignemos manualmente un valor a la clave primaria** cuando creemos una nueva entidad. Esto puede ser poco práctico y propenso a errores, ya que tendríamos que asegurarnos de que los valores de `id` sean únicos y consecutivos, lo cual es complicado.

### **Comparación de todas las estrategias:**

| **Estrategia**                 | **Descripción**                                                               | **Cuándo usarla**                                                                                     |
| ------------------------------ | ----------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------- |
| `GenerationType.AUTO`          | JPA selecciona automáticamente la estrategia adecuada según la base de datos. | Cuando no te preocupa la estrategia exacta y confías en la decisión automática de JPA.                |
| `GenerationType.IDENTITY`      | La base de datos usa una columna auto-incremental para generar los valores.   | Cuando tu base de datos soporta auto-incrementos (ej. MySQL, PostgreSQL).                             |
| `GenerationType.SEQUENCE`      | Usa una secuencia en la base de datos para generar valores consecutivos.      | Cuando trabajas con bases de datos que soportan secuencias (ej. PostgreSQL, Oracle).                  |
| `GenerationType.TABLE`         | Usa una tabla especial para guardar los valores de las claves primarias.      | Cuando la base de datos no soporta secuencias ni auto-incrementos. Menos eficiente.                   |
| **UUID (manual)**              | Usa un valor UUID generado de manera manual dentro del código Java.           | Cuando quieres claves primarias únicas globales y no dependes de las capacidades de la base de datos. |
| `UUID (Hibernate)`             | Hibernate puede generar automáticamente UUIDs si se configura.                | Cuando deseas claves primarias únicas globales sin generar los UUIDs manualmente.                     |
| **Custom Generation Strategy** | Define tu propia estrategia de generación de claves.                          | Cuando necesitas una lógica personalizada para generar claves primarias.                              |

### Ejemplo de generación con UUID:

Para usar UUID como clave primaria:

```java
import java.util.UUID;
import jakarta.persistence.*;

@Entity
public class Student {

  @Id
  @GeneratedValue
  private UUID id;  // Usar UUID en lugar de Integer
}
```

# JpaRepository - Persist Data

Se crea la interface de nombre `StudentRepository` para manejar las operaciones de persistencia de datos en la base de datos:

```java
package com.alibou.example;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
```

En este ejemplo, estamos creando una interfaz `StudentRepository` que hereda de `JpaRepository`.

Seguido de ello modificamos el controller de la siguiente manera:

```java
package com.alibou.example;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {
  private final StudentRepository studentRepository;

  public FirstController(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  @PostMapping("/students")
  public Student post(@RequestBody Student student) {
    return studentRepository.save(student);
  }
}
```

### **JpaRepository**

En **Spring Data JPA**, la interfaz `JpaRepository` es una de las herramientas más importantes, ya que proporciona métodos predefinidos para interactuar con la base de datos sin necesidad de escribir SQL manualmente. Vamos a entenderlo más a fondo.

#### ¿Qué es `JpaRepository`?

`JpaRepository` es una interfaz que extiende de otras interfaces clave como `CrudRepository` y `PagingAndSortingRepository`. Proporciona métodos genéricos para realizar operaciones CRUD (Crear, Leer, Actualizar y Eliminar) sobre las entidades de tu base de datos.

### Descripción de las funciones del `JpaRepository`

- **JpaRepository<Student, Integer>**: En este caso, estás definiendo un repositorio para la entidad `Student` y especificas que el tipo de la clave primaria es `Integer`. Esto le indica a Spring Data JPA que maneje las operaciones relacionadas con la entidad `Student`.

- **Métodos comunes**:
  - `save()`: Guarda una entidad en la base de datos. Si la entidad ya existe (determinada por el valor de su clave primaria), la actualiza. Si no, la crea.
  - `findAll()`: Recupera todas las instancias de la entidad de la base de datos.
  - `findById()`: Busca una entidad por su clave primaria.
  - `delete()`: Elimina una entidad específica de la base de datos.
  - Entre muchos otros métodos.

Este enfoque sigue el patrón **Repository**, lo que te permite delegar la interacción con la base de datos en esta capa, separando las preocupaciones de acceso a datos y lógica de negocio.

### **StudentRepository**

Aquí tienes la definición del repositorio:

```java
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
```

Esta interfaz extiende `JpaRepository`, lo que significa que hereda todos los métodos proporcionados por Spring Data JPA. No necesitas implementar nada manualmente; Spring hace el trabajo pesado por ti.

#### Parámetros de `JpaRepository`:
- `Student`: La clase de la entidad que será gestionada (en este caso, `Student`).
- `Integer`: El tipo de la clave primaria de la entidad.

### **FirstController**

El controlador tiene un endpoint que maneja una solicitud POST para agregar un nuevo estudiante en la base de datos:

```java
@RestController
public class FirstController {
  private final StudentRepository studentRepository;

  public FirstController(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  @PostMapping("/students")
  public Student post(@RequestBody Student student) {
    return studentRepository.save(student);
  }
}
```

- **Constructor**: El repositorio `StudentRepository` es inyectado en el controlador usando inyección de dependencias. Esto permite que el controlador acceda a los métodos del repositorio para interactuar con la base de datos.

- **Método `post`**:
  - **`@PostMapping("/students")`**: Define que este método responderá a solicitudes HTTP de tipo POST en la URL `/students`.
  - **`@RequestBody`**: Indica que el cuerpo de la solicitud contiene un objeto `Student`. Spring convierte el JSON enviado en el cuerpo de la solicitud a un objeto Java `Student`.
  - **`studentRepository.save(student)`**: Usa el repositorio para guardar el estudiante en la base de datos. Si el estudiante tiene un `id` no nulo y existe, se actualizará el registro. Si no tiene un `id`, se crea uno nuevo.
  - **Retorno del método**: Devuelve el objeto `Student` que fue guardado, lo que generalmente incluye el `id` generado automáticamente si el estudiante es nuevo.

### **Flujo del método `post`**:

1. El cliente (puede ser un frontend o una herramienta como Postman) envía una solicitud HTTP POST con los detalles de un nuevo estudiante en formato JSON.
   
   Ejemplo de cuerpo JSON:
   ```json
   {
     "name": "Juan",
     "lastname": "Perez",
     "email": "juan.perez@example.com",
     "age": 22
   }
   ```

2. Spring convierte ese JSON en un objeto `Student` y pasa ese objeto al método `post` del controlador.

3. El método invoca `studentRepository.save(student)`, lo que guarda el objeto en la base de datos. Si el estudiante es nuevo, se le asignará un `id` automáticamente gracias a la anotación `@GeneratedValue` en la entidad `Student`.

4. El estudiante guardado (con su `id` si es nuevo) se devuelve como respuesta al cliente.

### Ventajas de usar `JpaRepository`

- **Abstracción**: Te abstrae de escribir consultas SQL, ya que los métodos comunes ya están implementados.
- **Flexibilidad**: Si necesitas consultas personalizadas, puedes agregarlas de forma sencilla dentro de tu repositorio.
- **Optimización**: La integración con Hibernate y otras herramientas de JPA permite optimizaciones automáticas como el uso de cachés y mejoras en el rendimiento de consultas.
- **Escalabilidad**: Puedes usar características avanzadas de paginación, ordenación y manejo de transacciones sin necesidad de implementarlas desde cero.

---

Ejecutamos la aplicación y usamos thunder client para probar, usa un JSON como este:

```json
{
  "name": "Juan",
  "lastname": "Perez",
  "email": "juan.perez@example.com",
  "age": 22
}
```

Revisa la BD para ver si efectivamente se ha creado el estudiante.

# JpaRepository - Get Data

### 1. Modificación de `StudentRepository`

```java
package com.alibou.example;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
  // find students by name
  List<Student> findByName(String name);
}
```

**Explicación**:

- **`List<Student> findByName(String name)`**: Este método adicional permite buscar estudiantes por su nombre. Spring Data JPA proporciona la funcionalidad de generar automáticamente la implementación del método basándose en el nombre del método. Por ejemplo, al llamar a `findByName("John")`, Spring buscará todos los registros de estudiantes cuyo nombre sea "John". La implementación concreta de este método se generará en tiempo de ejecución.

### 2. Modificación de `FirstController`

```java
package com.alibou.example;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {
  private final StudentRepository studentRepository;

  public FirstController(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  @PostMapping("/students")
  public Student post(@RequestBody Student student) {
    return studentRepository.save(student);
  }

  @GetMapping("/students")
  public List<Student> getAllStudents() {
    return studentRepository.findAll();
  }

  @GetMapping("/students/{student-id}")
  public Student getStudentById(@PathVariable("student-id") Integer id) {
    return studentRepository.findById(id).orElse(null);
  }

  @GetMapping("/students/search/{student-name}")
  public List<Student> getStudentsByName(@PathVariable("student-name") String name) {
    return studentRepository.findByName(name);
  }
}
```

### Métodos GET:

1. **`@GetMapping("/students")`**:
   - **Método `getAllStudents`**: Este método maneja las solicitudes GET en la ruta `/students`. Devuelve una lista de todos los estudiantes almacenados en la base de datos llamando a `studentRepository.findAll()`. Esto permite obtener un listado completo de los estudiantes.

2. **`@GetMapping("/students/{student-id}")`**:
   - **Método `getStudentById`**: Este método maneja las solicitudes GET en la ruta `/students/{student-id}`, donde `{student-id}` es un parámetro de ruta que representa el ID del estudiante. Utiliza `@PathVariable` para obtener el ID del estudiante de la URL y luego busca al estudiante en la base de datos usando `studentRepository.findById(id)`. Si encuentra al estudiante, lo devuelve; si no, devuelve `null`.

3. **`@GetMapping("/students/search/{student-name}")`**:
   - **Método `getStudentsByName`**: Este método maneja las solicitudes GET en la ruta `/students/search/{student-name}`. Aquí, `{student-name}` es un parámetro que se extrae de la URL. Este método utiliza el método `findByName` que definimos en `StudentRepository` para buscar estudiantes por su nombre. Devuelve una lista de estudiantes que coinciden con el nombre proporcionado.

### Resumen:
- **Interacción con la base de datos**: La interfaz `StudentRepository` y el controlador `FirstController` permiten gestionar estudiantes a través de una API REST. Puedes crear nuevos estudiantes (POST), obtener todos los estudiantes (GET), buscar un estudiante por ID (GET), y buscar estudiantes por nombre (GET).
  
- **Uso de Spring Data JPA**: La magia de Spring Data JPA simplifica enormemente la interacción con la base de datos, permitiendo a los desarrolladores enfocarse en la lógica de negocio sin preocuparse por las consultas SQL complejas.

# JpaRepository - Delete Data

En el controller agregamos el método `deleteStudent` para eliminar un estudiante:

```java
@DeleteMapping("/students/{student-id}")
@ResponseStatus(HttpStatus.OK)
public void deleteStudent(@PathVariable("student-id") Integer id) {
  studentRepository.deleteById(id);
}
```

### Explicación:

- `@DeleteMapping("/students/{student-id}")`: Este método maneja las solicitudes DELETE en la ruta `/students/{student-id}`.
- `@ResponseStatus(HttpStatus.OK)`: Este método establece el código de respuesta HTTP en 200 (OK) para indicar que la solicitud se ha procesado correctamente.
- `studentRepository.deleteById(id)`: Este método llama al método `deleteById` del repositorio `StudentRepository` para eliminar el estudiante con el ID especificado en la URL.

# Relaciones entre entidades

Se crean las entidades `School` y `StudentProfile`:

```java
package com.alibou.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class School {
  @Id
  @GeneratedValue
  private Integer id;
  private String name;

  // Constructors
  public School() {
  }

  public School(String name) {
    this.name = name;
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
}
```

```java
package com.alibou.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class StudentProfile {
  @Id
  @GeneratedValue
  private Integer id;
  private String bio;

  // Constructors
  public StudentProfile() {
  }

  public StudentProfile(String bio) {
    this.bio = bio;
  }

  // Getters and Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }
}
```

De momneto no necesitamos más explicaciones, todo ya lo hemos visto con anteriroridad. Ahora si debemos proceder a agregar una relación entre las entidades `Student` y `StudentProfile`, siguiendo la lógica primero de pensar que una escuela tiene muchos estudiantes y un estudiante tiene un perfil.

### ¿Qué es una relación @OneToOne?

Cuando se habla de una relación **Uno a Uno** (`@OneToOne`) en una base de datos, significa que **cada fila** de una tabla está vinculada con **una única fila** de otra tabla. En nuestro ejemplo:

- **Un `Student`** tiene **un único `StudentProfile`**.
- **Un `StudentProfile`** está relacionado con **un único `Student`**.

La anotación `@OneToOne` en JPA es la forma de expresar esta relación. En términos simples:
- Cada estudiante (`Student`) tiene un único perfil (`StudentProfile`).
- Cada perfil (`StudentProfile`) pertenece a un único estudiante (`Student`).

Ahora desglosaremos el código que has proporcionado para entender cómo se configura esta relación.

---

### Clase `Student`

```java
@Entity
@Table(name = "T_STUDENT")
public class Student {

  @Id
  @GeneratedValue
  private Integer id;

  @Column(name = "c_fname", length = 20)
  private String name;
  private String lastname;

  @Column(unique = true)
  private String email;
  private int age;

  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDate createdAt;

  // Propiedades de la relación agregadas
  @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
  private StudentProfile studentProfile;
}
```

#### Explicación de la relación en `Student`:

- **`@OneToOne(mappedBy = "student")`**: Esto indica que la relación Uno a Uno está siendo **mapeada** por el campo `student` en la entidad `StudentProfile`. En otras palabras, la entidad `StudentProfile` tiene la "clave foránea" que conecta ambas entidades.
  - **`mappedBy`**: Significa que **`Student` no es la propietaria de la relación**. La tabla de `StudentProfile` contiene la columna de clave foránea (`student_id`), lo cual lo convierte en el lado dueño de la relación.
  
- **`cascade = CascadeType.ALL`**: Esto significa que cualquier operación realizada en un `Student` (como guardar, actualizar o eliminar) se aplicará automáticamente al `StudentProfile` relacionado. Por ejemplo, si eliminas un `Student`, también se eliminará el `StudentProfile` vinculado.

---

### Clase `StudentProfile`

```java
@Entity
public class StudentProfile {
  
  @Id
  @GeneratedValue
  private Integer id;
  
  private String bio;

  // Propiedades de la relación agregadas
  @OneToOne
  @JoinColumn(name = "student_id")
  private Student student;
}
```

#### Explicación de la relación en `StudentProfile`:

- **`@OneToOne`**: Aquí indicamos que `StudentProfile` tiene una relación Uno a Uno con `Student`. Como se mencionó antes, esta anotación crea el vínculo directo entre las dos entidades.

- **`@JoinColumn(name = "student_id")`**: Esta es una anotación muy importante, ya que define **cómo** se almacenará la relación en la base de datos.
  - **`@JoinColumn`**: Le dice a JPA que la columna `student_id` en la tabla de `StudentProfile` es la **clave foránea** que hace referencia al estudiante correspondiente.
  - **`name = "student_id"`**: Especifica el nombre de la columna en la tabla `StudentProfile` que contendrá el ID del `Student`. Así, cuando se guarde un perfil, se almacenará el ID del estudiante en esta columna.

#### ¿Qué sucede en la base de datos?

Al ejecutar este código, JPA creará dos tablas:

1. **Tabla `T_STUDENT`** (para los estudiantes):
   - Columnas: `id`, `name`, `lastname`, `email`, `age`, `created_at`
   
2. **Tabla `student_profile`** (para los perfiles de estudiantes):
   - Columnas: `id`, `bio`, `student_id`
   - **`student_id`** será una clave foránea que apunta al `id` de la tabla `T_STUDENT`.

---

### Resumen de la Relación Uno a Uno (`@OneToOne`):

1. **Propietario de la Relación**: En una relación Uno a Uno, una de las entidades debe ser la "propietaria" de la relación. En este caso, **`StudentProfile`** es la entidad propietaria, ya que tiene la clave foránea `student_id`. Esto se define mediante la anotación `@JoinColumn`.

2. **`mappedBy`**: Indica que el campo `studentProfile` en la clase `Student` está siendo controlado por la columna `student_id` en la clase `StudentProfile`. Es decir, `Student` no necesita la clave foránea, porque la clave foránea está en `StudentProfile`.

3. **Cascading (`cascade = CascadeType.ALL`)**: Este es un mecanismo poderoso para manejar automáticamente la persistencia y eliminación de objetos relacionados. Por ejemplo, al eliminar un `Student`, su `StudentProfile` será automáticamente eliminado de la base de datos debido al `CascadeType.ALL`.

4. **Clave foránea (`@JoinColumn`)**: `JoinColumn` se utiliza para especificar el nombre de la columna que se usará como clave foránea para relacionar las dos tablas en la base de datos.

### Ejemplo Visual:

Imagina dos tablas:

- `T_STUDENT` (estudiante)
- `student_profile` (perfil del estudiante)

En la tabla `student_profile`, habrá una columna llamada `student_id`, que contendrá el `id` de la tabla `T_STUDENT`. Esto es lo que permite vincular ambos registros en las dos tablas.

---

Ahora pasaremos a modificar la entidad `School` y `Student` para agregar su relación de uno a muchos (para el caso de School se crean Getters y Setters para la nueva propiedad de `students`).

### **Relación `@OneToMany` y `@ManyToOne`**:

Estas dos anotaciones son la base para modelar relaciones entre entidades donde **un objeto puede estar relacionado con muchos otros**. En este caso:

- **Una escuela (`School`) puede tener muchos estudiantes (`Student`)**.
- **Cada estudiante (`Student`) está asociado a una única escuela (`School`)**.

Esta relación se expresa en JPA utilizando una combinación de las anotaciones `@OneToMany` y `@ManyToOne`.

---

### Clase `School`:

```java
@Entity
public class School {
  
  @Id
  @GeneratedValue
  private Integer id;
  
  private String name;

  // Relación agregada
  @OneToMany(mappedBy = "school")
  private List<Student> students;

  // Getters y setters
  public List<Student> getStudents() {
    return students;
  }

  public void setStudents(List<Student> students) {
    this.students = students;
  }
}
```

#### Explicación de la relación en `School`:

- **`@OneToMany(mappedBy = "school")`**: Esto significa que **una escuela puede tener muchos estudiantes**. La relación Uno a Muchos queda representada en la lista `students`, donde se almacenarán todos los estudiantes asociados a una escuela específica.
  - **`mappedBy`**: Significa que esta entidad `School` **no es la propietaria** de la relación. En su lugar, es la entidad `Student` la que tiene la clave foránea (`school_id`) para indicar a qué escuela pertenece. La columna `school_id` estará en la tabla de `Student`, no en la de `School`.

Esta es una **relación bidireccional**, lo que significa que desde la entidad `School` podemos acceder a todos los estudiantes de esa escuela, y desde la entidad `Student` podemos acceder a la escuela a la que pertenece un estudiante específico.

---

### Clase `Student`:

```java
@Entity
@Table(name = "T_STUDENT")
public class Student {

  @Id
  @GeneratedValue
  private Integer id;

  @Column(name = "c_fname", length = 20)
  private String name;
  
  private String lastname;

  @Column(unique = true)
  private String email;
  
  private int age;

  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDate createdAt;

  @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
  private StudentProfile studentProfile;

  // Relación agregada
  @ManyToOne
  @JoinColumn(name = "school_id")
  private School school;

  // Getters y Setters nuevos
  public StudentProfile getStudentProfile() {
    return studentProfile;
  }

  public void setStudentProfile(StudentProfile studentProfile) {
    this.studentProfile = studentProfile;
  }

  public School getSchool() {
    return school;
  }

  public void setSchool(School school) {
    this.school = school;
  }
}
```

#### Explicación de la relación en `Student`:

- **`@ManyToOne`**: Esto significa que **muchos estudiantes pueden pertenecer a una sola escuela**. En otras palabras, esta es la relación **Muchos a Uno**. Muchos estudiantes están asociados con una sola entidad `School`.
  
- **`@JoinColumn(name = "school_id")`**: Esta anotación le dice a JPA que la columna `school_id` en la tabla `T_STUDENT` será la clave foránea que conecta al estudiante con una escuela.
  - **`school_id`** es la columna que almacenará el ID de la escuela correspondiente. Esto es lo que permite que múltiples estudiantes estén asociados a la misma escuela.

---

### Ejemplo en la Base de Datos:

Al ejecutar este código, JPA creará dos tablas:

1. **Tabla `school`** (para las escuelas):
   - Columnas: `id`, `name`

2. **Tabla `T_STUDENT`** (para los estudiantes):
   - Columnas: `id`, `name`, `lastname`, `email`, `age`, `created_at`, `school_id`
   - **`school_id`** será una clave foránea que apunta al `id` de la tabla `school`.

Esto establece la relación entre las dos tablas: un registro en `school` puede estar asociado con muchos registros en `T_STUDENT`, mientras que cada estudiante (en `T_STUDENT`) solo puede estar relacionado con una única escuela (a través de `school_id`).

---

### **Relación Uno a Muchos (One-to-Many)**:

La relación **Uno a Muchos** significa que **una entidad tiene muchas otras entidades relacionadas**. En este caso, **una escuela** tiene **muchos estudiantes**. Esto se refleja en la entidad `School` con la lista de `students`:

- **`@OneToMany`** en `School`: Esta anotación indica que una escuela tiene múltiples estudiantes.
- **`mappedBy = "school"`**: Indica que la entidad `Student` tiene la clave foránea (`school_id`) y, por tanto, `Student` es la entidad propietaria de la relación.

### **Relación Muchos a Uno (Many-to-One)**:

Por el otro lado, la relación **Muchos a Uno** significa que **muchas entidades están relacionadas con una sola**. En este caso, **muchos estudiantes** están relacionados con **una sola escuela**. Esto se refleja en la entidad `Student` con la anotación `@ManyToOne`:

- **`@ManyToOne`** en `Student`: Indica que muchos estudiantes están vinculados a una única escuela.
- **`@JoinColumn(name = "school_id")`**: Especifica que la columna `school_id` en la tabla `T_STUDENT` es la clave foránea que conecta con la tabla `school`.

---

### Resumen de la Relación Uno a Muchos (`@OneToMany`) y Muchos a Uno (`@ManyToOne`):

1. **Relación bidireccional**:
   - **Una escuela** puede tener **muchos estudiantes** (`@OneToMany` en `School`).
   - **Cada estudiante** pertenece a **una única escuela** (`@ManyToOne` en `Student`).

2. **Clave foránea**: En la tabla `T_STUDENT`, la columna `school_id` actúa como clave foránea que conecta a un estudiante con una escuela.

3. **`mappedBy`**: Indica que la clave foránea se gestiona desde el lado de `Student`, lo que significa que `School` no tiene la clave foránea directamente en su tabla, pero puede acceder a todos sus estudiantes mediante la lista `students`.

---

### Ejemplo práctico:

Imagina dos tablas en la base de datos:

- **Tabla `school`**:
  - Registro 1: `id=1, name='Escuela Primaria'`
  - Registro 2: `id=2, name='Escuela Secundaria'`

- **Tabla `T_STUDENT`**:
  - Registro 1: `id=1, name='Juan', school_id=1`
  - Registro 2: `id=2, name='María', school_id=1`
  - Registro 3: `id=3, name='Pedro', school_id=2`

En este caso, `Juan` y `María` pertenecen a la "Escuela Primaria" (con `school_id=1`), mientras que `Pedro` pertenece a la "Escuela Secundaria" (con `school_id=2`). Así es como se refleja la relación **Uno a Muchos** en la base de datos.

---

Ejecutamos la aplicación y revisamos la BD para ver que todo este en orden.

# Más Controllers

Creamos el `SchoolController`:

```java
package com.alibou.example;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class SchoolController {

  private final SchoolRepository schoolRepository;

  public SchoolController(SchoolRepository schoolRepository) {
    this.schoolRepository = schoolRepository;
  }

  @PostMapping("/schools")
  public School createSchool(@RequestBody School school) {
    return schoolRepository.save(school);
  }

  @GetMapping("/schools")
  public List<School> getSchools() {
    return schoolRepository.findAll();
  }

}
```

Renombramos el `FirstController` a `StudentController` para mantener la consistencia de nombres.

Ejecutamos la aplicación y usamos thunder client para probar la inserción de escuelas y estudiantes.

---

Ejemplo de JSON para crear escuela:

```JSON
{
  "name": "UAMEX"
}
```

Respuesta:

```JSON
{
  "id": 2,
  "name": "UAMEX",
  "students": null
}
```

---

Ejemplo de JSON para agregar estudiante:

```JSON
{
  "name": "Lucas",
  "lastname": "Lee",
  "email": "lucas.lee@example.com",
  "age": 24,
  "school": {
    "id": 1
  }
}
```

Respuesta:

```JSON
{
  "id": 1,
  "name": "Lucas",
  "lastname": "Lee",
  "email": "lucas.lee@example.com",
  "age": 24,
  "studentProfile": null,
  "school": {
    "id": 1,
    "name": null,
    "students": null
  }
}
```

---

Si intentamos hacer un GET de las escuelas tendremos un problema:

```JSON
[{"id":1,"name":"UAM","students":[{"id":1,"name":"Lucas","lastname":"Lee","email":"lucas.lee@example.com","age":24,"studentProfile":null,"school":{"id":1,"name":"UAM","students":[{"id":1,"name":"Lucas","lastname":"Lee","email":"lucas.lee@example.com","age":24,"studentProfile":null,"school":{"id":1,"name":"UAM","students":[{"id":1,"name":"Lucas","lastname":"Lee","email":"lucas.lee@example.com","age":24,"studentProfile":null,"school":{"id":1,"name":"UAM","students":[{"id":1,"name":.............
```

El **loop infinito** que estás observando al hacer un GET para obtener las escuelas ocurre debido a la **relación bidireccional** entre las entidades `School` y `Student`. Para entender mejor por qué ocurre esto, veamos cómo funciona el mapeo de relaciones y cómo influye en la serialización (conversión de objetos Java a JSON) de estas entidades.

### 1. **Relación bidireccional entre `School` y `Student`**:

- En la clase `School`, tienes una relación `@OneToMany` con `Student`. Esto significa que una escuela puede tener muchos estudiantes, y esta relación se expresa mediante una lista de `students` en la entidad `School`.
  
  ```java
  @OneToMany(mappedBy = "school")
  private List<Student> students;
  ```

- En la clase `Student`, tienes una relación `@ManyToOne` con `School`. Esto significa que muchos estudiantes pueden estar asociados a una única escuela, y esta relación está representada por la propiedad `school` en la entidad `Student`.

  ```java
  @ManyToOne
  @JoinColumn(name = "school_id")
  private School school;
  ```

Esto genera una relación bidireccional: **una escuela tiene estudiantes**, y **cada estudiante tiene una referencia a la escuela a la que pertenece**.

### 2. **El problema del loop infinito**:

Cuando intentas convertir estas entidades a JSON para enviarlas como respuesta (por ejemplo, al hacer un GET para obtener todas las escuelas), el serializador (como Jackson o Gson, que son librerías usadas comúnmente en Java para convertir objetos a JSON) no sabe cómo detener la serialización recursiva.

El problema radica en que:

- **La escuela** tiene una lista de **estudiantes**.
- **Cada estudiante** tiene una referencia a su **escuela**.
- Entonces, cuando el serializador convierte a JSON la entidad `School`, intenta serializar también la lista de estudiantes.
- Pero cada estudiante, a su vez, tiene una referencia de vuelta a la escuela, lo que provoca que el serializador intente volver a serializar la escuela.
- Y como esa escuela tiene la lista de estudiantes, el proceso se repite, causando un **loop infinito** en la serialización.

Este es el ciclo que genera el loop infinito:

1. Serialización de la **escuela** → contiene una lista de **estudiantes**.
2. Serialización del **primer estudiante** → contiene una referencia a la **escuela**.
3. Serialización de la **escuela** (otra vez) → vuelve a incluir la lista de **estudiantes**.
4. Serialización del **primer estudiante** (otra vez) → de nuevo referencia a la **escuela**, y así sucesivamente.

Este ciclo continúa indefinidamente hasta que se agota la memoria o se interrumpe el proceso.

### 3. **Soluciones al loop infinito**:

Existen varias formas de evitar este comportamiento recursivo:

#### Opción 1: Usar la anotación `@JsonIgnore`

Puedes evitar que el serializador intente serializar una parte de la relación bidireccional utilizando la anotación `@JsonIgnore`. Por ejemplo, si deseas evitar que la propiedad `school` dentro de `Student` sea serializada para romper el ciclo, puedes agregar `@JsonIgnore` en la propiedad `school` de la clase `Student`:

```java
@ManyToOne
@JoinColumn(name = "school_id")
@JsonIgnore
private School school;
```

Esto le indicará al serializador que **ignore** la propiedad `school` al convertir la entidad `Student` a JSON. Con esto, los estudiantes se seguirán serializando cuando pidas una escuela, pero **sin incluir la referencia a la escuela** en la que están inscritos, rompiendo así el ciclo.

#### Opción 2: Usar `@JsonManagedReference` y `@JsonBackReference`

Otra solución es utilizar las anotaciones `@JsonManagedReference` y `@JsonBackReference`, que te permiten gestionar de manera más fina las relaciones bidireccionales en la serialización.

- **`@JsonManagedReference`** se coloca en el lado **dueño** de la relación (en este caso, en la lista `students` dentro de `School`).
- **`@JsonBackReference`** se coloca en el lado inverso de la relación (en este caso, en el atributo `school` dentro de `Student`).

Esto crea una relación "padre-hijo" en la serialización y evita el ciclo.

```java
@Entity
public class School {
  @Id
  @GeneratedValue
  private Integer id;
  private String name;

  @OneToMany(mappedBy = "school")
  @JsonManagedReference
  private List<Student> students;
}

@Entity
public class Student {
  @Id
  @GeneratedValue
  private Integer id;

  private String name;
  private String lastname;

  @ManyToOne
  @JoinColumn(name = "school_id")
  @JsonBackReference
  private School school;
}
```

Con estas anotaciones:
- **`@JsonManagedReference`**: Se asegura de que cuando se serialice una `School`, se incluirá su lista de `students`.
- **`@JsonBackReference`**: Impide que, al serializar un `Student`, se vuelva a serializar la `School`, rompiendo así el ciclo.

#### Opción 3: Utilizar DTOs (Data Transfer Objects)

Otra práctica común para evitar este tipo de problemas es crear **clases DTO** (objetos de transferencia de datos) que sólo incluyan los datos necesarios para la respuesta y no toda la estructura de las entidades. Esto te permite controlar explícitamente qué datos se serializan.

Por ejemplo, podrías crear un `SchoolDTO` que no incluya la lista de estudiantes, o un `StudentDTO` que sólo incluya el ID de la escuela, y no la entidad completa.

```java
public class SchoolDTO {
  private Integer id;
  private String name;
  // Puedes omitir la lista de estudiantes si no es necesaria en la respuesta
}

public class StudentDTO {
  private Integer id;
  private String name;
  private String lastname;
  private Integer schoolId; // solo el ID de la escuela, no toda la entidad
}
```

Luego, al hacer las consultas, en lugar de devolver las entidades completas, puedes devolver estas versiones simplificadas que no contienen las referencias cíclicas.

# Solucionando Loop con @JsonManagedReference y @JsonBackReference

Agregamos la anotación `@JsonManagedReference` a la propiedad `students` de la entidad `School`:

```java
@Entity
public class School {
  @Id
  @GeneratedValue
  private Integer id;
  private String name;

  @OneToMany(mappedBy = "school")
  @JsonManagedReference
  private List<Student> students;
}
```

Luego agregamos la anotación `@JsonBackReference` a la propiedad `school` de la entidad `Student`:

```java
@Entity
public class Student {
  @Id
  @GeneratedValue
  private Integer id;
  private String name;
  private String lastname;

  @ManyToOne
  @JoinColumn(name = "school_id")
  @JsonBackReference
  private School school;
}
```

### Explicación:

- `@JsonManagedReference`: Esta anotación indica que el objeto referenciado por la propiedad no debe ser serializado por defecto. En este caso, el objeto `Student` no debe ser serializado por defecto, ya que es una entidad de la base de datos.
- `@JsonBackReference`: Esta anotación indica que el objeto referenciado por la propiedad debe ser serializado por defecto. En este caso, el objeto `School` debe ser serializado por defecto, ya que es una entidad de la base de datos.

---

Volvemos a ejecutar la aplicación y probamos en thunder client de nuevo y veremos algunas diferencias cuando se crea al estudiante pero lo importante es que se soluciona el loop infinito ahora recibiendo una respuesta como esta:

```JSON
[
  {
    "id": 1,
    "name": "UAMEX",
    "students": [
      {
        "id": 1,
        "name": "Lucas",
        "lastname": "Lee",
        "email": "lucas.lee@example.com",
        "age": 24,
        "studentProfile": null
      }
    ]
  },
  {
    "id": 2,
    "name": "UAM",
    "students": []
  }
]
```

[Continuar](7_Notas_Spring_Rest_Basic_3.md)