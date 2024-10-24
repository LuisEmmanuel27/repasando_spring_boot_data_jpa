# Introducción a los Tests en Spring Boot

Cuando trabajamos en aplicaciones con Spring Boot, los **tests** son una parte fundamental del desarrollo de software. Nos ayudan a garantizar que el código funcione correctamente y que siga funcionando a medida que hacemos cambios o añadimos nuevas funcionalidades. En este contexto, Spring Boot ofrece un conjunto de herramientas muy útil para facilitar la escritura y ejecución de pruebas.

Existen diferentes tipos de tests en Spring Boot, y cada uno tiene un propósito particular:

1. **Unit Tests (Pruebas Unitarias):**  
   Estas pruebas están diseñadas para probar unidades individuales de código, como métodos o clases, de forma aislada. No interactúan con otras partes del sistema, por lo que suelen "mockear" (simular) dependencias externas como bases de datos o servicios.

2. **Integration Tests (Pruebas de Integración):**  
   Estas pruebas verifican que diferentes partes de la aplicación funcionen bien juntas. En lugar de probar una unidad de código aislada, una prueba de integración examina cómo diferentes componentes (como controladores, servicios, y repositorios) interactúan entre sí.

3. **End-to-End Tests (Pruebas de extremo a extremo):**  
   Este tipo de prueba se realiza a nivel de toda la aplicación, probando flujos completos, desde la entrada del usuario hasta el resultado final, incluyendo la interacción con bases de datos y otros servicios.

---

### Configuración para Pruebas en Spring Boot

Antes de escribir nuestras pruebas, es importante asegurarse de tener la configuración adecuada en nuestro proyecto.

1. **Dependencias:**  
   Spring Boot ofrece un soporte excelente para pruebas a través de JUnit (el framework de pruebas más común para Java) y Mockito (una librería para mockear dependencias).

   Asegúrate de incluir las siguientes dependencias en tu archivo `pom.xml` para habilitar las pruebas:

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-test</artifactId>
       <scope>test</scope>
   </dependency>
   ```

   La dependencia `spring-boot-starter-test` incluye todo lo que necesitamos para realizar pruebas con **JUnit 5**, **Mockito**, y otras herramientas útiles como **Hamcrest** y **AssertJ**.

---

### 1. Pruebas Unitarias (Unit Tests)

Las pruebas unitarias verifican el funcionamiento de clases o métodos individuales sin involucrar otros componentes del sistema. Usualmente, las dependencias externas (como un repositorio o un servicio) se "mockean" para simular su comportamiento.

#### Ejemplo de una Prueba Unitaria

Vamos a escribir una prueba para un servicio de ejemplo, `StudentService`, que tiene una dependencia hacia un repositorio `StudentRepository`.

```java
// Servicio de ejemplo
@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student findStudentById(Long id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Student not found"));
    }
}
```

#### Escribiendo la Prueba Unitaria

Usaremos Mockito para simular el comportamiento de `StudentRepository` y escribir una prueba para `StudentService`.

```java
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa los mocks
    }

    @Test
    void findStudentById_StudentExists_ReturnsStudent() {
        // Dado que el estudiante con ID 1 existe
        Student student = new Student(1L, "John", "Doe");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Cuando buscamos al estudiante
        Student result = studentService.findStudentById(1L);

        // Entonces debe devolver el estudiante correcto
        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    void findStudentById_StudentDoesNotExist_ThrowsException() {
        // Dado que el estudiante con ID 2 no existe
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        // Cuando buscamos al estudiante, esperamos que se lance una excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentService.findStudentById(2L);
        });

        assertEquals("Student not found", exception.getMessage());
    }
}
```

#### Explicación del Test:

- Usamos `@Mock` para simular el repositorio (`StudentRepository`).
- `@InjectMocks` inyecta el mock en la clase de servicio que estamos probando (`StudentService`).
- `MockitoAnnotations.openMocks(this)` inicializa los mocks antes de cada prueba.
- Usamos `when(...).thenReturn(...)` para especificar el comportamiento del mock.
- Usamos `assertNotNull` y `assertEquals` para verificar los resultados esperados.

### 2. Pruebas de Integración

A diferencia de las pruebas unitarias, las **pruebas de integración** verifican cómo interactúan diferentes componentes de la aplicación en conjunto, utilizando la configuración real del contexto de Spring. Estas pruebas generalmente requieren un entorno más complejo, ya que pueden interactuar con bases de datos o servicios externos reales o simulados.

#### Ejemplo de Prueba de Integración

Veamos cómo escribir una prueba de integración que verifique la interacción entre un servicio y un repositorio real.

```java
@SpringBootTest
public class StudentServiceIntegrationTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testCreateAndFindStudent() {
        // Crear un nuevo estudiante y guardarlo
        Student newStudent = new Student(null, "Jane", "Doe");
        studentRepository.save(newStudent);

        // Buscar el estudiante por ID
        Student foundStudent = studentService.findStudentById(newStudent.getId());

        // Verificar que los datos sean correctos
        assertNotNull(foundStudent);
        assertEquals("Jane", foundStudent.getName());
    }
}
```

#### Explicación de la Prueba de Integración:

- Usamos `@SpringBootTest` para cargar el **contexto de Spring Boot** completo, permitiendo que todos los componentes (servicios, repositorios, etc.) estén disponibles.
- En lugar de simular el repositorio, aquí estamos usando un **repositorio real**.
- La prueba verifica si el servicio puede interactuar correctamente con el repositorio para crear y buscar estudiantes.

### 3. MockMvc: Pruebas de Controladores

Spring Boot ofrece una herramienta muy útil llamada **MockMvc** para simular solicitudes HTTP y probar controladores sin tener que desplegar toda la aplicación.

#### Ejemplo de Test de Controlador con MockMvc

```java
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetStudents() throws Exception {
        mockMvc.perform(get("/students"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }
}
```

#### Explicación:

- `@WebMvcTest` carga solo los componentes necesarios para probar el controlador.
- `MockMvc` nos permite simular una petición HTTP (`get("/students")`) y verificar el resultado esperado.

---

### Resumen:

| Tipo de Test         | ¿Qué Prueba?                         | Dependencias Usadas        | Ejemplo de Herramientas |
| -------------------- | ------------------------------------ | -------------------------- | ----------------------- |
| **Unit Test**        | Métodos o clases individuales        | Mockito, JUnit             | Simulación con Mockito  |
| **Integration Test** | Interacción entre varios componentes | JUnit, Spring Boot Context | `@SpringBootTest`       |
| **MockMvc**          | Controladores y rutas HTTP           | MockMvc, JUnit, Mockito    | `@WebMvcTest`, MockMvc  |

# Testing - StudentMapper

Si revisamos los diferentes componentes relacionados con la entidad `Student`, notaremos que **no tiene mucho sentido escribir pruebas unitarias** para algunos de ellos, como:

- `Student`
- `StudentResponseDto`
- `StudentDto`
- `StudentRepository`

Aquí está el porqué:

1. **`Student`, `StudentResponseDto` y `StudentDto`**:
   Estos son simplemente **objetos de datos** (también llamados DTOs, Data Transfer Objects). Su propósito es únicamente **almacenar información** o **transferir datos** entre capas. No contienen lógica compleja ni procesos que justifiquen la creación de pruebas, ya que solo contienen getters, setters o constructores. Al no realizar cálculos ni modificaciones en los datos, no hay mucho que probar en ellos.

2. **`StudentRepository`**:
   Es un componente generado y manejado automáticamente por **Spring Data JPA**, que ya viene probado y garantizado por Spring. No es necesario que nosotros probemos su funcionamiento, ya que no estamos escribiendo la lógica de acceso a datos manualmente.

### ¿Por qué comenzar con `StudentMapper`?
Por el contrario, la clase `StudentMapper` **sí contiene lógica**. Esta clase es responsable de transformar un objeto `Student` en un `StudentResponseDto` (o viceversa), lo que implica cierta lógica de negocio para asegurarse de que los datos se transfieren correctamente entre los diferentes tipos de objetos. 

Es por esto que vale la pena escribir pruebas unitarias para el `StudentMapper`, ya que aquí necesitamos asegurarnos de que las conversiones entre objetos están ocurriendo de forma correcta.

## Primeros tests

Dentro del paquete ya creado de `test` vamos a crear, por buenas practicas, un paquete de nombre `student` y dentro de este colocaremos los tests que involucren a la entidad `Student`. En este caso comenzando por `StudentMapperTest` y agregamos de momento el siguiente código:

```java
package com.alibou.example.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StudentMapperTest {
  @BeforeEach
  void setUp() {
    System.out.println("Antes que cualquier test");
  }

  @Test
  void testMethod1() {
    System.out.println("Mi primer test");
  }

  @Test
  void testMethod2() {
    System.out.println("Mi segundo test");
  }
}
```

Este código es un ejemplo básico de una clase de prueba en **JUnit 5** para la clase `StudentMapperTest`. Vamos a desglosarlo de manera sencilla:

1. **Paquete:**
   ```java
   package com.alibou.example.student;
   ```
   Esto indica que la clase de prueba está dentro del paquete `com.alibou.example.student`. Es una convención organizar las pruebas en paquetes paralelos a los del código principal.

2. **Anotación `@BeforeEach`:**
   ```java
   @BeforeEach
   void setUp() {
       System.out.println("Antes que cualquier test");
   }
   ```
   Este método está anotado con `@BeforeEach`, lo que significa que se ejecuta **antes de cada test**. En este caso, simplemente imprime "Antes que cualquier test", pero en pruebas reales, aquí podrías inicializar variables o configurar el entorno de pruebas.

3. **Métodos de prueba `@Test`:**
   ```java
   @Test
   void testMethod1() {
       System.out.println("Mi primer test");
   }

   @Test
   void testMethod2() {
       System.out.println("Mi segundo test");
   }
   ```
   Estos dos métodos están anotados con `@Test`, lo que indica que son pruebas unitarias que serán ejecutadas por el framework de JUnit. Ambos métodos imprimen un mensaje ("Mi primer test" y "Mi segundo test") para demostrar que están siendo ejecutados.

## @AfterEach

El método anotado con `@AfterEach` se ejecuta después de cada test individual. Es útil para limpiar o liberar recursos que fueron inicializados en `@BeforeEach` o dentro del propio test. También puede servir para restablecer el estado del entorno de pruebas.

```java
@BeforeEach(){...}

@AfterEach
  void tearDown() {
  System.out.println("Despues de cada test");
}

// Tests unitarios
```

## @BeforeAll

El método anotado con `@BeforeAll` se ejecuta una vez antes de que se ejecuten todos los tests en la clase de prueba. Es útil para configurar recursos compartidos que deben inicializarse solo una vez, como conexiones a bases de datos o configuraciones globales. El método anotado con `@BeforeAll` debe ser `static` porque no depende de la instancia de la clase de prueba.

```java
@BeforeAll
static void setUpClass() {
    System.out.println("Antes de todos los tests");
}

@BeforeEach(){...}
@AfterEach(){...}

// Tests unitarios
```

## @AfterAll

El método anotado con `@AfterAll` se ejecuta una vez después de que todos los tests en la clase de prueba se han ejecutado. Es útil para liberar recursos compartidos que fueron inicializados en `@BeforeAll`, como cerrar conexiones o limpiar datos persistentes. Al igual que `@BeforeAll`, este método debe ser `static`.

```java
@BeforeAll(){...}

@AfterAll
static void tearDownClass() {
  System.out.println("Despues de todos los tests");
}

@BeforeEach(){...}
.
.
```

## Tests para `StudentMapper` - toStudent

Eliminamos los test antes creados y agregamos lo siguiente:

```java
package com.alibou.example.student;

import org.springframework.stereotype.Service;

import com.alibou.example.school.School;

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

Este código es un buen ejemplo de cómo escribir pruebas unitarias para un método de mapeo, en este caso el método `toStudent` del `StudentMapper`. Vamos a desglosarlo paso por paso para entender lo que hace y cómo está estructurado el test.

### 1. **Clase de prueba `StudentMapperTest`**
La clase `StudentMapperTest` es donde se definen los tests unitarios para la clase `StudentMapper`. Este tipo de test tiene como objetivo verificar que el comportamiento del método de mapeo sea correcto, es decir, que el método `toStudent` convierta un `StudentDto` en una entidad `Student` de manera correcta.

### 2. **Inicialización del mapper**
```java
@BeforeEach
void setUp() {
  studentMapper = new StudentMapper();
}
```
- **`@BeforeEach`**: Esta anotación indica que el método `setUp()` se ejecutará **antes de cada prueba**. En este caso, lo que hace es inicializar una nueva instancia de `StudentMapper` para cada test, asegurándose de que cada prueba comience con un estado limpio y sin efectos de pruebas anteriores.

### 3. **Método de prueba `shouldMapStudentDtoToStudent`**
```java
@Test
void shouldMapStudentDtoToStudent() {
  StudentDto studentDto = new StudentDto(
      "John", "Doe", "john@doe.com", 1);
  
  Student student = studentMapper.toStudent(studentDto);
  
  assertEquals(studentDto.name(), student.getName());
  assertEquals(studentDto.lastname(), student.getLastname());
  assertEquals(studentDto.email(), student.getEmail());
  assertNotNull(student.getSchool());
  assertEquals(studentDto.SchoolId(), student.getSchool().getId());
}
```
Este es el corazón de nuestro test, y se descompone en varios pasos importantes:

- **Creación del objeto `StudentDto`**: 
   ```java
   StudentDto studentDto = new StudentDto("John", "Doe", "john@doe.com", 1);
   ```
   Aquí se crea una instancia de `StudentDto`, que es el objeto de datos que será pasado al mapper. El DTO tiene los valores `"John"`, `"Doe"`, `"john@doe.com"` como nombre, apellido, y email respectivamente, y el `SchoolId` es `1`.

- **Invocación del método a probar**:
   ```java
   Student student = studentMapper.toStudent(studentDto);
   ```
   El método `toStudent` del `StudentMapper` es invocado con el `studentDto` como argumento. Esto devuelve una instancia de `Student` que debería haber sido correctamente mapeada a partir del DTO.

- **Verificación de los resultados**:
   Después de la conversión, se utilizan varias aserciones para verificar que los datos se han mapeado correctamente:
   - **`assertEquals(studentDto.name(), student.getName())`**: Verifica que el nombre en el DTO es igual al nombre en el objeto `Student`.
   - **`assertEquals(studentDto.lastname(), student.getLastname())`**: Verifica que el apellido en el DTO es igual al apellido en el `Student`.
   - **`assertEquals(studentDto.email(), student.getEmail())`**: Verifica que el email en el DTO es igual al email en el `Student`.
   - **`assertNotNull(student.getSchool())`**: Verifica que el objeto `School` no sea `null`. Esto garantiza que la escuela ha sido creada e inyectada correctamente en el estudiante.
   - **`assertEquals(studentDto.SchoolId(), student.getSchool().getId())`**: Verifica que el ID de la escuela en el DTO coincide con el ID de la escuela en el objeto `Student`.

### 4. **Explicación de las aserciones**
Las aserciones son fundamentales en las pruebas unitarias, ya que son las que nos permiten verificar si el comportamiento del método es el esperado. 

- **`assertEquals(expected, actual)`**: Esta aserción verifica que el valor esperado (en este caso, tomado del `StudentDto`) coincida con el valor actual (el que ha sido mapeado al objeto `Student`). Si los valores no coinciden, el test fallará, lo que indica un problema en el mapeo.

- **`assertNotNull(actual)`**: Verifica que el objeto no sea `null`. En este caso, se asegura de que el estudiante tenga asociada una escuela, lo que significa que el mapeo fue correcto y la relación entre `Student` y `School` fue establecida.

## Tests para `StudentMapper` - studentResponseDto

Agregamos el siguiente Test:

```java
@Test
void shouldMapStudentToStudentResponseDto() {
  // Given
  Student student = new Student("Jhon", "Doe", "jhon@doe.com", 27);

  // When
  StudentResponseDto studentResponseDto = studentMapper.studentResponseDto(student);

  // Then
  assertEquals(student.getName(), studentResponseDto.name());
  assertEquals(student.getLastname(), studentResponseDto.lastname());
  assertEquals(student.getEmail(), studentResponseDto.email());
}
```

Este nuevo test verifica que el método `studentResponseDto` del `StudentMapper` funcione correctamente, asegurándose de que un objeto `Student` se convierta adecuadamente en un `StudentResponseDto`. Aquí está el desglose del test:

### 1. **Preparación (`Given`)**
```java
Student student = new Student("Jhon", "Doe", "jhon@doe.com", 27);
```
Aquí creamos un objeto `Student` con nombre, apellido, email y edad. Este objeto será el que el método `studentResponseDto` tomará para convertir en un `StudentResponseDto`.

### 2. **Acción (`When`)**
```java
StudentResponseDto studentResponseDto = studentMapper.studentResponseDto(student);
```
En esta línea se invoca el método `studentResponseDto` del mapper, pasándole el objeto `Student` creado previamente. Esto devuelve un objeto `StudentResponseDto` que contendrá los mismos valores de nombre, apellido y email que el `Student`.

### 3. **Verificación (`Then`)**
```java
assertEquals(student.getName(), studentResponseDto.name());
assertEquals(student.getLastname(), studentResponseDto.lastname());
assertEquals(student.getEmail(), studentResponseDto.email());
```
Aquí se verifica que los valores del objeto `StudentResponseDto` coincidan con los del objeto `Student` original. Cada aserción (`assertEquals`) comprueba que el nombre, apellido y email del `StudentResponseDto` son correctos en relación con el `Student`.

## Tests para `StudentMapper` - Test para Excepciones

Primero, intentamos crear un test para verificar qué sucede cuando pasamos un `StudentDto` nulo al método `toStudent` del `StudentMapper`:

```java
@Test
void shoud_map_studentsDto_to_student_when_studentDto_is_null() {
  Student student = studentMapper.toStudent(null);
  assertEquals("", student.getName());
  assertEquals("", student.getLastname());
}
```
**Error**: 
- Al ejecutar este test, el programa lanzó una excepción `NullPointerException` porque el código intentaba acceder a los métodos de un objeto `null`. 
- Esto sucedió porque el método `toStudent` no manejaba correctamente el caso de que el `dto` fuera `null`.

#### 2. **Ajuste en el Mapper**
Para resolver el problema, agregamos una verificación al método `toStudent` para que lance una excepción clara si el `StudentDto` es `null`:

```java
public Student toStudent(StudentDto dto) {
    if (dto == null)  // <-- Verificación de nulo añadida
      throw new NullPointerException("The studentDto should not be null");

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
**Explicación del ajuste**:
- Antes de ejecutar cualquier lógica, el método ahora verifica si el `dto` es `null`.
- Si lo es, lanzamos una `NullPointerException` con un mensaje claro: `"The studentDto should not be null"`. Esto nos ayuda a manejar este caso de manera explícita y evitar errores inesperados.

#### 3. **Modificación del Test**
Después de ajustar el código, modificamos el test para asegurarnos de que el método lance la excepción correcta cuando reciba un `StudentDto` nulo:

```java
@Test
void shoud_throw_null_pointer_exception_when_studentDto_is_null() {
  var exp = assertThrows(NullPointerException.class, () -> studentMapper.toStudent(null));
  assertEquals("The studentDto should not be null", exp.getMessage());
}
```
**Explicación del test**:
- Usamos `assertThrows` para verificar que se lance una excepción `NullPointerException` cuando el `StudentDto` sea `null`.
- Además, comprobamos que el mensaje de la excepción sea exactamente `"The studentDto should not be null"`, asegurándonos de que la excepción sea descriptiva y fácil de entender.

#### 4. **Resultado Final**
Después de estos ajustes, el test pasa correctamente. Ahora el código maneja el caso de un `StudentDto` nulo de manera clara y con una excepción apropiada.

# 