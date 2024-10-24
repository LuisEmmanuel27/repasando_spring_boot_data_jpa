# DTO Pattern

El **DTO Pattern** o **Patrón de Objeto de Transferencia de Datos** (Data Transfer Object) es un patrón de diseño utilizado en la arquitectura de software para transferir datos entre diferentes capas de una aplicación, especialmente entre la capa de negocio y la capa de presentación (por ejemplo, entre el servidor y el cliente, o entre componentes de una API y el frontend). Los DTOs están diseñados específicamente para contener solo los datos necesarios para realizar una determinada operación, **sin incluir la lógica de negocio o comportamientos complejos**.


**Ejemplo básico de un DTO:**

Supongamos que tienes una aplicación que gestiona estudiantes. Un **DTO de Estudiante** podría verse así:

```java
public class StudentDTO {
    private Integer id;
    private String name;
    private String email;

    // Getters y Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```

Este `StudentDTO` contiene solo los campos que queremos transferir, sin lógica adicional.

### 1. **¿Por qué usar DTOs?**

Existen varias razones por las que el patrón DTO es útil en una aplicación:

#### **A. Separación de Concerns (Separación de Responsabilidades)**

En muchas aplicaciones, existen varias capas como la capa de negocio (donde se gestionan las reglas de la aplicación) y la capa de presentación (donde se manejan las vistas y la interacción con el usuario). Usar DTOs permite separar los datos que manejan estas capas de los **detalles de implementación** o **lógica compleja** que tienen las entidades del dominio.

Por ejemplo, una entidad `Student` podría tener relaciones con otras entidades (como `School`, `Profile`, etc.), y podría tener lógica compleja dentro de la entidad. Al utilizar DTOs, puedes **aislar** esos detalles y **controlar qué datos específicos se transfieren** entre capas, sin exponer toda la lógica interna de las entidades.

#### **B. Mejora en el rendimiento**

Los DTOs ayudan a **reducir la cantidad de datos** que se transfieren en las solicitudes y respuestas. Si una entidad contiene muchas propiedades o relaciones con otras entidades, puedes crear un DTO que incluya solo los datos necesarios para una operación específica.

Por ejemplo, al listar todos los estudiantes, podrías no necesitar enviar toda la información detallada de cada estudiante, sino solo su `id`, `nombre` y `email`.

#### **C. Prevención de Exposición de Datos Sensibles**

Usar DTOs te permite evitar la exposición de datos que no deberían ser visibles para todos los usuarios. Por ejemplo, una entidad `User` podría tener datos sensibles como contraseñas o detalles financieros. Al crear un `UserDTO`, puedes **filtrar** qué datos son accesibles y proteger la información privada.

#### **D. Mejor Mantenimiento del Código**

Los DTOs permiten cambiar la estructura de las entidades del dominio sin afectar las interfaces públicas de la aplicación, porque los DTOs actúan como intermediarios. Si cambias la lógica o estructura interna de una entidad, los cambios no afectarán la capa de presentación o las APIs si los DTOs se mantienen consistentes.

### 2. **Cuándo y Dónde Usar DTOs**

El uso de DTOs es particularmente útil en las siguientes situaciones:

#### **A. En APIs RESTful**

En una API REST, cuando manejas solicitudes HTTP entre un cliente y un servidor, los DTOs se utilizan para definir qué datos se envían y se reciben en los endpoints. Puedes crear un DTO para manejar el cuerpo de una solicitud (`RequestDTO`) y otro para la respuesta (`ResponseDTO`).

Por ejemplo:

```java
public class StudentResponseDTO {
    private Integer id;
    private String name;
    private String email;
    // Getters y Setters
}
```

Aquí, cuando un cliente solicita la lista de estudiantes, el servidor puede devolver solo la información relevante a través de un `StudentResponseDTO`, en lugar de devolver todo el objeto `Student`, que podría contener más datos de los necesarios.

#### **B. En Aplicaciones Grandes**

En aplicaciones grandes o de arquitectura de microservicios, donde existen muchas capas y servicios independientes, los DTOs ayudan a mantener una **comunicación clara y eficiente** entre los diferentes servicios.

#### **C. Al Exponer Entidades a través de una API**

Si tienes una entidad `Student` con muchas relaciones (como perfil de estudiante, escuela, etc.), puede ser peligroso devolver la entidad completa como respuesta en una API. Con DTOs, puedes **controlar y personalizar** qué datos de la entidad se exponen en las diferentes operaciones.

### 3. **Conversión entre Entidades y DTOs**

Cuando se usa el patrón DTO, es importante convertir entre **entidades del dominio** y **DTOs**. Esto puede hacerse manualmente o con herramientas que automatizan este proceso (como **MapStruct**, un generador de código en Java).

#### **Conversión manual**:

Puedes escribir métodos para convertir una entidad a un DTO y viceversa:

```java
public class StudentMapper {
    // Conversión de Entidad a DTO
    public static StudentDTO toDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        return dto;
    }

    // Conversión de DTO a Entidad
    public static Student toEntity(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        return student;
    }
}
```

#### **Automatización con librerías**:

Hay librerías como **MapStruct** o **ModelMapper** que pueden automatizar este proceso, reduciendo la cantidad de código repetitivo. Con estas librerías, puedes definir mappings automáticos entre entidades y DTOs, lo que puede ser muy útil en aplicaciones grandes.

Ejemplo con MapStruct:

```java
@Mapper
public interface StudentMapper {
    StudentDTO toDTO(Student student);
    Student toEntity(StudentDTO studentDTO);
}
```

### 4. **Ventajas y Desventajas de usar DTOs**

#### **Ventajas**:
- **Reducción de datos transferidos**: Puedes enviar solo los datos necesarios.
- **Mejor rendimiento**: Al transferir menos datos, se mejora el rendimiento en redes.
- **Seguridad**: Puedes proteger datos sensibles evitando que se expongan.
- **Flexibilidad**: Facilita la evolución de la lógica del negocio sin afectar la interfaz pública.
- **Claridad**: Simplifica la estructura de los datos que se intercambian entre capas.

#### **Desventajas**:
- **Código adicional**: Necesitas escribir más código para los DTOs y su conversión.
- **Complejidad**: En aplicaciones pequeñas o simples, puede añadir complejidad innecesaria.
- **Duplicación**: Si no se organiza bien, puedes acabar con una duplicación de código entre los DTOs y las entidades.

### 5. **Ejemplo Práctico: Implementación en una API**

Supongamos que tienes una API REST que maneja estudiantes y escuelas. Sin usar DTOs, podrías exponer demasiados datos o tener relaciones bidireccionales que causan problemas como loops infinitos (como el que viste anteriormente). 

Usando DTOs, puedes estructurar tus respuestas de manera controlada y clara:

1. **DTO para listar estudiantes**:

```java
public class StudentDTO {
    private Integer id;
    private String name;
    private String email;
    private String schoolName; // Solo incluimos el nombre de la escuela, no la entidad completa
}
```

2. **DTO para obtener detalles de una escuela**:

```java
public class SchoolDTO {
    private Integer id;
    private String name;
    private List<StudentDTO> students; // Solo incluimos una lista de estudiantes simplificada
}
```

Con estos DTOs, puedes devolver datos bien estructurados y ligeros, evitando exponer toda la lógica de las entidades.

# Aplicando DTOs - Students

Creamos un `Record` de nombre `StudentDto` para representar los datos de un estudiante:

```java
public record StudentDto(
    String name,
    String lastname,
    String email,
    Integer SchoolId) {

}
```

Este `StudentDto` contiene solo los campos que queremos transferir, sin lógica adicional.

**¿Qué significa esto?**

- Este `record` tiene **cuatro campos**: `name`, `lastname`, `email`, y `SchoolId`.
- Se usa para **recibir datos** desde una solicitud, como cuando alguien envía un formulario o hace una llamada POST en tu API.
- **No tiene lógica de negocio**, solo almacena datos. Esto es clave en los DTOs: su único propósito es la transferencia de datos entre diferentes capas de la aplicación.

### 1. **El controlador `StudentController`**

El controlador se encarga de manejar las solicitudes HTTP, en este caso una solicitud **POST** que crea un nuevo estudiante en la base de datos. Aquí está el código del controlador:

```java
@RestController
public class StudentController {
  private final StudentRepository studentRepository;

  public StudentController(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  // Metodo para crear el Estudiante en base al DTO
  @PostMapping("/students")
  public Student post(@RequestBody StudentDto studentDto) {
    var student = toStudent(studentDto);
    return studentRepository.save(student);
  }

  // Metodo para crear el Estudiante en base al DTO
  private Student toStudent(StudentDto dto) {
    var student = new Student();
    student.setName(dto.name());
    student.setLastname(dto.lastname());
    student.setEmail(dto.email());

    var school = new School();
    school.setId(dto.SchoolId());

    student.setSchool(school);

    return student;
  }
}
```

- **`@RequestBody StudentDto studentDto`**: Esta anotación indica que Spring tomará el cuerpo de la solicitud POST (en formato JSON), lo convertirá en una instancia de `StudentDto` y lo pasará como argumento al método.

> **Ejemplo de una solicitud POST**:
> ```json
> {
>   "name": "Juan",
>   "lastname": "Pérez",
>   "email": "juan.perez@example.com",
>   "SchoolId": 1
> }
> ```

#### A. **Conversión de `StudentDto` a `Student`**

El DTO solo contiene los datos necesarios, pero la entidad `Student` necesita algo más: probablemente relaciones con otras entidades (en este caso, una `School`). Por eso, el método `toStudent` se encarga de **convertir** el DTO en una entidad `Student` que pueda ser guardada en la base de datos.

Aquí está el método de conversión:

```java
private Student toStudent(StudentDto dto) {
    var student = new Student();
    student.setName(dto.name());
    student.setLastname(dto.lastname());
    student.setEmail(dto.email());

    var school = new School();
    school.setId(dto.SchoolId());

    student.setSchool(school);

    return student;
}
```

##### **Paso 1: Crear un nuevo objeto `Student`**
```java
var student = new Student();
```
Se crea una nueva instancia de `Student`, que es la entidad que será persistida en la base de datos.

##### **Paso 2: Asignar datos del DTO a la entidad**
```java
student.setName(dto.name());
student.setLastname(dto.lastname());
student.setEmail(dto.email());
```
Cada campo del DTO (`name`, `lastname`, `email`) se asigna a su respectiva propiedad en el objeto `Student`. 

Recuerda que en los `records` de Java, puedes acceder a los campos usando `dto.name()`, `dto.lastname()`, etc.

##### **Paso 3: Asociar el `Student` con una `School`**
```java
var school = new School();
school.setId(dto.SchoolId());
student.setSchool(school);
```
Aquí se crea una instancia de la entidad `School` y se le asigna el `SchoolId` del DTO. Esto es necesario porque `Student` probablemente tiene una relación con `School`, lo que significa que necesita referenciar una escuela, pero solo el **ID de la escuela** es suficiente en este caso. No se necesita cargar toda la entidad `School`, solo su ID para referenciarla.

> Esto evita **consultas innecesarias** a la base de datos o la sobrecarga de información no necesaria.

##### **Paso 4: Retornar el objeto `Student`**
```java
return student;
```
Finalmente, se devuelve la entidad `Student` completamente construida, que luego será guardada en la base de datos.

#### B. **Guardar el estudiante en la base de datos**

```java
return studentRepository.save(student);
```
Este paso guarda el objeto `Student` en la base de datos usando el método `save` de `StudentRepository`. Si todo funciona correctamente, se devuelve el objeto `Student` que fue guardado, lo cual será enviado al cliente en formato JSON como respuesta a la solicitud.

---

Ejecutamos el proyecto y probamos en thunder client

# Aplicando DTOs - Students Response

Se crea el nuevo Record `StudentResponseDto` que representa el formato de respuesta del Student.

```java
package com.alibou.example;

public record StudentResponseDto(
    String name,
    String lastname,
    String email) {

}
```

Seguido de ello modificamos el metodo Post en el controller para que devuelva un StudentResponseDto en lugar de Student.

```java
@PostMapping("/students")
public StudentResponseDto post(@RequestBody StudentDto studentDto) {
    var student = toStudent(studentDto);
    var savedStudent = studentRepository.save(student);
    return studentResponseDto(savedStudent);
}
```

Y el método `studentResponseDto` que crea el StudentResponseDto a partir del Student.

```java
private StudentResponseDto studentResponseDto(Student student) {
    return new StudentResponseDto(student.getName(), student.getLastname(), student.getEmail());
}
```

Ejecutamos el proyecto y probamos en thunder client para ver el resultado.

# Aplicando DTOs - School

Se crea el Record `SchoolDto` que representa el formato de entrada del School.

```java
package com.alibou.example;

public record SchoolDto(
  String name) {

}
```

Seguido de ello modificamos en el controller de School el metodo Post para que reciba un SchoolDto en lugar de un School y responda con el mismo SchoolDto.

```java
@PostMapping("/schools")
public SchoolDto createSchool(@RequestBody SchoolDto dto) {
  var school = toSchool(dto);
  schoolRepository.save(school);
  return dto;
}

// Metodo agregado para hacer uso del DTO
private School toSchool(SchoolDto dto) {
  return new School(dto.name());
}
```

Ejecutamos la aplicación y probamos creando una nueva escuela.

---

Anteriormente, al obtener la información de las escuelas a través del método `GET`, el resultado mostraba **toda la información** de las escuelas, incluidos los estudiantes asociados. Esto podía causar un problema de rendimiento o un bucle infinito, ya que la relación bidireccional entre las entidades de **escuela** y **estudiante** hacía que los datos se repitieran indefinidamente.

Para simplificar la respuesta y evitar estos problemas, se ha modificado el código para que **solo se muestre el nombre de las escuelas** y no todos sus datos ni la información de los estudiantes asociados.

### 1. Método `getSchools()`

```java
@GetMapping("/schools")
public List<SchoolDto> getSchools() {
    return schoolRepository.findAll()  // Obtiene todas las escuelas de la base de datos
        .stream()  // Convierte la lista de escuelas en un flujo de datos (Stream)
        .map(this::schoolDto)  // Aplica el método 'schoolDto' a cada escuela para convertirla en un DTO
        .collect(Collectors.toList());  // Recoge el flujo transformado en una lista
}
```

#### Explicación:

Este método **recupera todas las escuelas** desde el repositorio (`schoolRepository`) y **convierte** cada una en un objeto **SchoolDto**. En lugar de devolver toda la información de la escuela (incluyendo estudiantes, datos adicionales, etc.), solo devuelve una lista con **los nombres** de las escuelas.

1. **`schoolRepository.findAll()`**: Este método recupera todas las escuelas desde la base de datos.
   
2. **[.stream()](0_Extras.md/#qué-es-un-stream-en-java)**: Al convertir la lista de escuelas en un **Stream**, puedes procesar los datos de una forma más declarativa y flexible, aplicando operaciones como `map()` y `collect()`.

3. **`.map(this::schoolDto)`**: Esta línea es clave. Aplica el método `schoolDto` (explicado más adelante) a cada escuela del flujo. El método `schoolDto` toma un objeto `School` y lo convierte en un **DTO**. Esto permite **transformar** los objetos `School` en objetos más simples, **SchoolDto**, que contienen solo los datos que deseas mostrar (en este caso, solo el nombre de la escuela).

4. **`.collect(Collectors.toList())`**: Una vez que el `Stream` ha aplicado la conversión a `SchoolDto`, usa `collect` para volver a formar una lista a partir del flujo de objetos `SchoolDto`.

### 2. Método `schoolDto(School school)`

```java
private SchoolDto schoolDto(School school) {
    return new SchoolDto(school.getName());
}
```

#### Explicación:

Este es el método que se utiliza para **convertir un objeto `School`** en un **DTO** de tipo `SchoolDto`. Aquí es donde decides **qué información incluir** en la respuesta.

- **Parámetro `School school`**: Recibe un objeto `School` como argumento. Este objeto tiene varios atributos, como `id`, `name` y una lista de estudiantes asociados.

- **`return new SchoolDto(school.getName())`**: Aquí se crea y devuelve una nueva instancia de `SchoolDto`, pero solo con el **nombre** de la escuela. No se incluyen otros datos como el `id` o la lista de estudiantes, lo que simplifica la información que se envía al cliente.

Esto es útil porque el objeto `School` puede tener muchas propiedades que no siempre quieres exponer en una solicitud `GET`. En este caso, solo se está exponiendo el nombre de la escuela, lo que hace la respuesta más ligera y evita problemas de rendimiento o de relaciones cíclicas (bucle infinito).

# Organizando Código - Service Layer

Creamos dos clases nuevas `StudentMapper` y `StudentService` para organizar el código y mantener la lógica de negocio separada de la lógica de presentación.

### 1. StudentMapper

El **mapper** es un objeto que se encarga de **mapear** objetos de dominio a objetos de transferencia de datos (DTO). En este caso, el mapper se encarga de convertir un objeto `Student` a un objeto `StudentDto`.

```java
@Service
public class StudentMapper {

  // Metodo para crear el Estudiante en base al DTO
  public Student toStudent(StudentDto dto) {
    var student = new Student();
    student.setName(dto.name());
    student.setLastname(dto.lastname());
    student.setEmail(dto.email());

    var school = new School();
    school.setId(dto.SchoolId());

    student.setSchool(school);

    return student;
  }

  // Metodo para responder con el Estudiante en base al DTO
  public StudentResponseDto studentResponseDto(Student student) {
    return new StudentResponseDto(student.getName(), student.getLastname(), student.getEmail());
  }
}
```

### 2. StudentService

El **service** es un objeto que se encarga de **manejar** las operaciones de negocio. En este caso, el service se encarga de guardar un objeto `Student` en la base de datos, obtener todos los objetos `Student` y obtener un objeto `Student` por ID.

```java
@Service
public class StudentService {
  private final StudentRepository studentRepository;
  private final StudentMapper studentMapper;

  public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
    this.studentRepository = studentRepository;
    this.studentMapper = studentMapper;
  }

  public StudentResponseDto saveStudent(StudentDto dto) {
    var student = studentMapper.toStudent(dto);
    var savedStudent = studentRepository.save(student);
    return studentMapper.studentResponseDto(savedStudent);
  }

  public List<Student> getAllStudents() {
    return studentRepository.findAll();
  }

  public Student getStudentById(Integer id) {
    return studentRepository.findById(id).orElse(null);
  }

  public List<Student> getStudentsByName(String name) {
    return studentRepository.findByName(name);
  }

  public void deleteStudent(Integer id) {
    studentRepository.deleteById(id);
  }
}
```

### 3. Modificaciones en el controlador

```java
@RestController
public class StudentController {
  private final StudentService studentService;

  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }

  @PostMapping("/students")
  public StudentResponseDto createStudent(@RequestBody StudentDto studentDto) {
    return this.studentService.saveStudent(studentDto);
  }

  @GetMapping("/students")
  public List<Student> getAllStudents() {
    return this.studentService.getAllStudents();
  }

  @GetMapping("/students/{student-id}")
  public Student getStudentById(@PathVariable("student-id") Integer id) {
    return this.studentService.getStudentById(id);
  }

  @GetMapping("/students/search/{student-name}")
  public List<Student> getStudentsByName(@PathVariable("student-name") String name) {
    return this.studentService.getStudentsByName(name);
  }

  @DeleteMapping("/students/{student-id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteStudent(@PathVariable("student-id") Integer id) {
    this.studentService.deleteStudent(id);
  }

}
```

---

Ejecutamos el proyecto y probamos en thunder client que todo funciona como esperábamos.

## Explicación detallada

El concepto de **Service Layer** (capa de servicio) es un patrón de diseño que organiza la lógica de negocio de una aplicación en un nivel intermedio, separando las responsabilidades entre los controladores (encargados de gestionar las solicitudes HTTP) y los repositorios (encargados de interactuar con la base de datos). Su objetivo es lograr una arquitectura más limpia, escalable y mantenible.

En el código se ha aplicado este patrón mediante la introducción de las clases **`StudentService`** y **`StudentMapper`**. Vamos a ver qué es el **Service Layer** y cómo funciona en este caso específico.

### ¿Qué es el Service Layer?

El **Service Layer** es una capa intermedia que actúa como un puente entre el **Controller** (que maneja las peticiones de los usuarios) y el **Repository** (que maneja la interacción con la base de datos). Su principal responsabilidad es contener la **lógica de negocio**. Es decir, en esta capa se procesan las reglas y operaciones específicas de la aplicación antes de que los datos lleguen al controlador o se guarden en la base de datos.

#### Ventajas del Service Layer:
1. **Separación de responsabilidades**: El controlador se encarga solo de las solicitudes HTTP y no de la lógica de negocio, lo que permite mantener un código más limpio.
2. **Reutilización**: La lógica de negocio en los servicios puede ser reutilizada por otros controladores o componentes.
3. **Fácil mantenimiento**: Al tener la lógica separada en servicios, es más sencillo hacer cambios y probar las funcionalidades.
4. **Escalabilidad**: Facilita la extensión de la lógica de negocio sin necesidad de alterar el controlador o las interacciones directas con la base de datos.

### Explicación del código con el Service Layer

#### 1. **Controller (`StudentController`)**
El controlador sigue siendo responsable de recibir y manejar las solicitudes HTTP. Sin embargo, la lógica relacionada con los estudiantes ha sido movida a la clase `StudentService`. El controlador ahora es una capa delgada, lo que significa que únicamente delega las acciones a los servicios apropiados.

- **Responsabilidad del controlador**: Solo maneja las solicitudes HTTP, convirtiendo las solicitudes y respuestas (en JSON) en objetos de Java y viceversa. Luego, delega la lógica de negocio al servicio.

Ejemplo de código relevante del controlador:

```java
@RestController
public class StudentController {
  private final StudentService studentService;  // Inyección de dependencia

  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }

  @PostMapping("/students")
  public StudentResponseDto createStudent(@RequestBody StudentDto studentDto) {
    return this.studentService.saveStudent(studentDto);  // Lógica delegada a StudentService
  }

  // Otros métodos también delegan las acciones a StudentService
}
```

#### 2. **Servicio (`StudentService`)**
Aquí es donde el **Service Layer** se aplica. En la clase `StudentService`, se maneja la lógica de negocio y las operaciones relacionadas con los estudiantes. 

- **Responsabilidad del servicio**: Proporcionar las funcionalidades relacionadas con los estudiantes, como crear un nuevo estudiante, obtener todos los estudiantes, buscar por nombre, eliminar, etc. En este caso, `StudentService` también utiliza la clase `StudentMapper` para convertir los datos del DTO a las entidades de dominio o viceversa.

Ejemplo de código relevante del servicio:

```java
@Service
public class StudentService {
  private final StudentRepository studentRepository;
  private final StudentMapper studentMapper;

  public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
    this.studentRepository = studentRepository;
    this.studentMapper = studentMapper;
  }

  public StudentResponseDto saveStudent(StudentDto dto) {
    var student = studentMapper.toStudent(dto);  // Convierte DTO en Student
    var savedStudent = studentRepository.save(student);  // Guarda el estudiante en la base de datos
    return studentMapper.studentResponseDto(savedStudent);  // Convierte Student a StudentResponseDto
  }

  public List<Student> getAllStudents() {
    return studentRepository.findAll();
  }

  public Student getStudentById(Integer id) {
    return studentRepository.findById(id).orElse(null);
  }

  public List<Student> getStudentsByName(String name) {
    return studentRepository.findByName(name);
  }

  public void deleteStudent(Integer id) {
    studentRepository.deleteById(id);
  }
}
```

Aquí podemos observar que toda la lógica relacionada con el manejo de estudiantes está contenida en el servicio, no en el controlador. Por ejemplo:
- **Crear un estudiante**: Se valida y convierte un `StudentDto` en un `Student` a través del `StudentMapper`, luego se guarda utilizando el `studentRepository`, y finalmente se retorna una respuesta `StudentResponseDto`.
- **Obtener estudiantes**: El servicio se encarga de interactuar con el repositorio para obtener la lista de todos los estudiantes o buscar uno por nombre.

#### 3. **Mapper (`StudentMapper`)**
La clase `StudentMapper` se encarga de convertir los datos entre las diferentes capas. 

- **Responsabilidad del Mapper**: Convertir objetos de un tipo (como `StudentDto`) en otro tipo (`Student`) y viceversa. En este caso, el mapper se utiliza para convertir el DTO en la entidad de estudiante que la base de datos maneja, y para convertir la entidad de estudiante en el formato de respuesta `StudentResponseDto`.

Ejemplo de código relevante del mapper:

```java
@Service
public class StudentMapper {

  // Convierte un StudentDto en una entidad Student
  public Student toStudent(StudentDto dto) {
    var student = new Student();
    student.setName(dto.name());
    student.setLastname(dto.lastname());
    student.setEmail(dto.email());

    var school = new School();
    school.setId(dto.SchoolId());
    student.setSchool(school);

    return student;
  }

  // Convierte un Student en un StudentResponseDto para responder
  public StudentResponseDto studentResponseDto(Student student) {
    return new StudentResponseDto(student.getName(), student.getLastname(), student.getEmail());
  }
}
```

El **`Mapper`** abstrae la complejidad de convertir objetos entre diferentes capas. Por ejemplo, cuando el controlador recibe un `StudentDto`, el mapper convierte ese DTO en un objeto `Student`, y cuando se necesita devolver la respuesta al usuario, el mapper convierte el `Student` en un `StudentResponseDto`.

# Aplicando DTOs en StudentService

Modificamos `StudenController` y `StudentService` para que el resto de metodos usen DTOs en lugar de objetos `Student`.

```java
@RestController
public class StudentController {
  private final StudentService studentService;

  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }

  @PostMapping("/students")
  public StudentResponseDto createStudent(@RequestBody StudentDto studentDto) {
    return studentService.saveStudent(studentDto);
  }

  @GetMapping("/students")
  public List<StudentResponseDto> getAllStudents() {
    return studentService.getAllStudents();
  }

  @GetMapping("/students/{student-id}")
  public StudentResponseDto getStudentById(@PathVariable("student-id") Integer id) {
    return studentService.getStudentById(id);
  }

  @GetMapping("/students/search/{student-name}")
  public List<StudentResponseDto> getStudentsByName(@PathVariable("student-name") String name) {
    return studentService.getStudentsByName(name);
  }

  @DeleteMapping("/students/{student-id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteStudent(@PathVariable("student-id") Integer id) {
    studentService.deleteStudent(id);
  }

}
```

```java
@Service
public class StudentService {
  private final StudentRepository studentRepository;
  private final StudentMapper studentMapper;

  public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
    this.studentRepository = studentRepository;
    this.studentMapper = studentMapper;
  }

  public StudentResponseDto saveStudent(StudentDto dto) {
    var student = studentMapper.toStudent(dto);
    var savedStudent = studentRepository.save(student);
    return studentMapper.studentResponseDto(savedStudent);
  }

  public List<StudentResponseDto> getAllStudents() {
    return studentRepository.findAll()
        .stream()
        .map(studentMapper::studentResponseDto)
        .collect(Collectors.toList());
  }

  public StudentResponseDto getStudentById(Integer id) {
    return studentRepository.findById(id)
        .map(studentMapper::studentResponseDto)
        .orElse(null);
  }

  public List<StudentResponseDto> getStudentsByName(String name) {
    return studentRepository.findByName(name)
        .stream()
        .map(studentMapper::studentResponseDto)
        .collect(Collectors.toList());
  }

  public void deleteStudent(Integer id) {
    studentRepository.deleteById(id);
  }
}
```

Ejecutamos el proyecto y probamos en thunder client que todo funciona como esperábamos.

# Service Layer en School

Repetimos el proceso de refactorización de código para la entidad de `School`. Realizados los cambios quedan de la siguiente manera:

```java
// Controller
@RestController
public class SchoolController {
  private final SchoolService schoolService;

  public SchoolController(SchoolService schoolService) {
    this.schoolService = schoolService;
  }

  @PostMapping("/schools")
  public SchoolDto createSchool(@RequestBody SchoolDto dto) {
    return schoolService.createSchool(dto);
  }

  @GetMapping("/schools")
  public List<SchoolDto> getSchools() {
    return schoolService.getSchools();
  }
}
```

```java
// Service
@Service
public class SchoolService {
  private final SchoolRepository schoolRepository;
  private final SchoolMapper schoolMapper;

  public SchoolService(SchoolRepository schoolRepository, SchoolMapper schoolMapper) {
    this.schoolRepository = schoolRepository;
    this.schoolMapper = schoolMapper;
  }

  public SchoolDto createSchool(SchoolDto dto) {
    var school = schoolMapper.toSchool(dto);
    schoolRepository.save(school);
    return dto;
  }

  public List<SchoolDto> getSchools() {
    return schoolRepository.findAll()
        .stream()
        .map(schoolMapper::toSchoolDto)
        .collect(Collectors.toList());
  }
}
```

```java
// Mapper
@Service
public class SchoolMapper {
  public School toSchool(SchoolDto dto) {
    return new School(dto.name());
  }

  public SchoolDto toSchoolDto(School school) {
    return new SchoolDto(school.getName());
  }
}
```

Ejecutamos el proyecto y probamos en thunder client que todo funciona como esperábamos.

# Formas de Organizar el Código

La organización de paquetes en un proyecto es crucial para mantener la escalabilidad, mantenibilidad y claridad del código. Existen diferentes enfoques que pueden aplicarse según la naturaleza del proyecto y el equipo de desarrollo. A continuación, se explican los enfoques más comunes para organizar paquetes:

### 1. **By Feature (Por Funcionalidad)**

En el enfoque *by feature*, los paquetes están organizados en función de las funcionalidades específicas o módulos del sistema. Cada funcionalidad se agrupa en un solo paquete que contiene todas las capas (controladores, servicios, repositorios, etc.) relacionadas con esa funcionalidad.

#### Ventajas:
- **Alta cohesión**: Todas las partes de una funcionalidad específica están en un solo lugar, lo que facilita el mantenimiento y la localización del código relacionado.
- **Escalabilidad por funcionalidad**: Ideal para sistemas modulares donde diferentes equipos pueden trabajar en diferentes funcionalidades sin interferencia.
- **Facilita la adición de nuevas funcionalidades**: Es más fácil agregar nuevas características sin afectar otras partes del sistema.

#### Ejemplo de Estructura:
```
com.example.app
├── student
│   ├── StudentController.java
│   ├── StudentService.java
│   ├── StudentRepository.java
│   └── StudentDto.java
├── school
│   ├── SchoolController.java
│   ├── SchoolService.java
│   ├── SchoolRepository.java
│   └── SchoolDto.java
```

En este ejemplo, cada paquete (`student`, `school`) contiene todas las clases necesarias para la funcionalidad específica.

#### Aplicación:
- **Sistemas modulares**: Donde las funcionalidades están claramente separadas, como aplicaciones de comercio electrónico con módulos para usuarios, pedidos, productos, etc.
- **Equipos grandes**: Donde varios equipos pueden trabajar simultáneamente en diferentes funcionalidades sin interferirse entre sí.

### 2. **Layered Approach (Por Capas)**

En el enfoque *by layer*, las clases están organizadas en función de la capa en la que operan: controladores, servicios, repositorios, etc. Este enfoque sigue el patrón clásico de capas (como MVC) en el que cada capa tiene una responsabilidad específica.

#### Ventajas:
- **Separación clara de responsabilidades**: Cada capa tiene una responsabilidad clara (ej. controladores manejan peticiones HTTP, servicios manejan la lógica de negocio, repositorios manejan el acceso a datos).
- **Facilita la reutilización**: Los servicios y repositorios pueden reutilizarse en diferentes controladores.
- **Simplicidad inicial**: Ideal para proyectos pequeños o medianos donde las funcionalidades están altamente interrelacionadas.

#### Ejemplo de Estructura:
```
com.example.app
├── controller
│   ├── StudentController.java
│   └── SchoolController.java
├── service
│   ├── StudentService.java
│   └── SchoolService.java
├── repository
│   ├── StudentRepository.java
│   └── SchoolRepository.java
├── dto
│   ├── StudentDto.java
│   └── SchoolDto.java
```

Aquí, las clases se agrupan por su función en la arquitectura de capas, separando claramente la lógica de control, servicio y persistencia.

#### Aplicación:
- **Proyectos pequeños o medianos**: Con funcionalidades estrechamente relacionadas.
- **Simplicidad inicial**: Este enfoque es más fácil de entender para equipos pequeños o cuando las funcionalidades no son completamente independientes.

### 3. **By Domain (o Domain-Driven Design, DDD)**

En *by domain* o DDD, la organización se basa en los dominios del negocio. Un dominio es un área central del negocio, y cada dominio tiene su propio paquete que contiene entidades, servicios, repositorios y controladores relacionados con ese dominio. La idea principal detrás de DDD es capturar la lógica de negocio de una manera que refleje cómo funciona el negocio en la vida real.

#### Ventajas:
- **Modelo de negocio claro**: Captura con precisión el modelo del dominio del negocio, lo que facilita a los desarrolladores comprender la lógica empresarial.
- **Escalabilidad en dominios complejos**: Este enfoque es altamente escalable en aplicaciones grandes donde los dominios son complejos y están bien definidos.
- **Facilita la comunicación con expertos de dominio**: El código refleja cómo funciona el negocio, facilitando la colaboración entre desarrolladores y expertos de dominio.

#### Ejemplo de Estructura:
```
com.example.app
├── domain
│   ├── student
│   │   ├── Student.java
│   │   ├── StudentService.java
│   │   ├── StudentRepository.java
│   │   └── StudentController.java
│   ├── school
│   │   ├── School.java
│   │   ├── SchoolService.java
│   │   ├── SchoolRepository.java
│   │   └── SchoolController.java
```

Cada dominio (`student`, `school`) encapsula todos los aspectos relacionados con ese dominio específico, como entidades, servicios, controladores y repositorios.

#### Aplicación:
- **Proyectos complejos**: Ideal para aplicaciones grandes y complejas donde hay varios dominios de negocio, como sistemas bancarios, ERP o CRM.
- **Lógica de negocio compleja**: Cuando la lógica de negocio es compleja y tiene que estar modelada de manera precisa.

### 4. **By Component (Por Componente)**

En este enfoque, los paquetes están organizados por componentes reutilizables o independientes. Un componente puede representar un conjunto de funcionalidades o un módulo que puede ser reutilizado o reemplazado de manera independiente. Este enfoque es común en proyectos basados en microservicios, donde cada componente es autónomo.

#### Ventajas:
- **Reutilización de componentes**: Los componentes pueden ser independientes y reutilizables en diferentes partes del sistema o incluso en otros sistemas.
- **Escalabilidad horizontal**: Facilita la migración hacia una arquitectura de microservicios, donde cada componente puede evolucionar de forma independiente.
- **Independencia**: Cada componente puede evolucionar sin afectar a otros componentes.

#### Ejemplo de Estructura:
```
com.example.app
├── student-management
│   ├── StudentController.java
│   ├── StudentService.java
│   ├── StudentRepository.java
│   └── StudentDto.java
├── school-management
│   ├── SchoolController.java
│   ├── SchoolService.java
│   ├── SchoolRepository.java
│   └── SchoolDto.java
```

Aquí, los componentes (`student-management`, `school-management`) son unidades completas que encapsulan toda la lógica relacionada con su área funcional.

#### Aplicación:
- **Microservicios**: Este enfoque es ideal para proyectos basados en microservicios, donde cada componente o servicio puede ser desarrollado y desplegado de manera independiente.
- **Sistemas modulares**: Aplicaciones donde se desea un alto nivel de modularidad y la capacidad de extraer o cambiar componentes fácilmente.

---

| **Enfoque**          | **Descripción**                                                                                                                         | **Ventajas**                                                                                                                          | **Desventajas**                                                                                      | **Aplicación Ideal**                                                                                                                                                                | **Ejemplo de Estructura**                                                                                                                                                                                                                          |
| -------------------- | --------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **By Feature**       | Organiza los paquetes por funcionalidad o módulos. Cada paquete contiene todas las capas relacionadas con una funcionalidad específica. | - Alta cohesión<br>- Facilita la colaboración entre equipos<br>- Escalabilidad por funcionalidad                                      | - Posible duplicación de código entre funcionalidades<br>- Menos reutilización entre módulos         | Sistemas modulares, donde las funcionalidades están claramente separadas. Ej: Aplicaciones de comercio electrónico, con módulos de `products`, `orders`, `users`.                   | ``` com.app.product  <br>  ├── ProductController.java  <br>  ├── ProductService.java  <br>  └── ProductRepository.java  <br> com.app.order  <br>  ├── OrderController.java  <br>  ├── OrderService.java  <br>  └── OrderRepository.java```         |
| **Layered Approach** | Organiza los paquetes por capas, siguiendo un patrón de arquitectura como MVC (Model-View-Controller).                                  | - Separación clara de responsabilidades<br>- Fácil de entender para proyectos pequeños<br>- Reutilización de servicios y repositorios | - Puede llevar a dependencias cruzadas entre capas<br>- No escalable para proyectos grandes          | Proyectos pequeños o medianos, con funcionalidades interrelacionadas. Ej: Aplicación de blog con capas de `controller`, `service`, `repository`.                                    | ``` com.app.controller  <br>  ├── PostController.java  <br>  ├── CommentController.java  <br> com.app.service  <br>  ├── PostService.java  <br>  └── CommentService.java```                                                                        |
| **By Domain (DDD)**  | Organiza los paquetes en función de los dominios del negocio, capturando la lógica empresarial dentro de los dominios.                  | - Modelo claro de negocio<br>- Facilita la comunicación con expertos del dominio<br>- Escalable en aplicaciones grandes               | - Complejidad inicial<br>- Requiere una comprensión profunda del negocio                             | Aplicaciones grandes con dominios complejos. Ej: Sistemas bancarios con dominios como `accounts`, `transactions`, `loans`.                                                          | ``` com.bank.account  <br>  ├── AccountController.java  <br>  ├── AccountService.java  <br>  └── AccountRepository.java  <br> com.bank.loan  <br>  ├── LoanController.java  <br>  ├── LoanService.java  <br>  └── LoanRepository.java```           |
| **By Component**     | Organiza los paquetes en componentes reutilizables, donde cada componente encapsula una funcionalidad completa.                         | - Componentes independientes y reutilizables<br>- Escalabilidad horizontal<br>- Facilita la migración a microservicios                | - Aumento de la complejidad inicial<br>- Gestión de dependencias entre componentes puede ser difícil | Microservicios o sistemas modulares, donde cada componente puede desarrollarse de manera autónoma. Ej: Un sistema de pagos con componentes de `payment-gateway`, `fraud-detection`. | ``` com.payment.gateway  <br>  ├── GatewayController.java  <br>  ├── GatewayService.java  <br>  └── GatewayRepository.java  <br> com.payment.fraud  <br>  ├── FraudController.java  <br>  ├── FraudService.java  <br>  └── FraudRepository.java``` |

[continuar](8_Notas_Spring_Rest_Basic_4.md)