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

## Tests para `StudentMapper`

