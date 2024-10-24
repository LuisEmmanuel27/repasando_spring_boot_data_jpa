# Ordenando por Funcionalidad (Feature)

Creamos 2 paquetes nuevos, `school`, `student` y `studentprofile` para organizar el código, dentro del paquete `school` colocamos todos los códigos correspondientes a la entidad `School`, y dentro del paquete `student` colocamos los códigos correspondientes a la entidad `Student`, no olvidando que `StudentProfile` es una entidad distinta asi que esa la colocamos en el paquete `studentprofile`.

Aparecerán un par de errores en algunos códigos pero esto es debido a que ahora debemos importar las entidades necesarias donde se necesiten, asi que solo debemos realizar esos cambios y todo funcionará como se espera.

Prueba ejecutando el proyecto para verificar que todo esta en orden.

# Data Validation

Primero agregamos una nueva dependencia:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

En el `StudentDto` agregamos nuevas etiquetas de validación:

```java
package com.alibou.example.student;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record StudentDto(
    @NotEmpty String name,
    @NotEmpty String lastname,
    @NotNull @NotEmpty String email,
    Integer SchoolId) {

}
```

Seguido de eso en el controller agregamos un @Validate:

```java
import jakarta.validation.Valid;

@PostMapping("/students")
public StudentResponseDto createStudent(@RequestBody @Valid StudentDto studentDto) {
  return studentService.saveStudent(studentDto);
}
```

Ejecutamos y probamos, si no enviamos el name, lastname o email, obtendremos un error de validación.

---

La validación de datos en Spring Boot permite verificar la entrada de datos antes de procesarla. Esto se logra mediante anotaciones específicas que definen restricciones en los campos de los objetos (como DTOs). La validación asegura que los datos enviados por el cliente cumplan con ciertos criterios, lo que previene errores o entradas malformadas.

### Proceso Rápido:
1. **Dependencia**: Se añade la dependencia `spring-boot-starter-validation` para habilitar la validación.
2. **Anotaciones en DTO**: Se agregan anotaciones de validación como `@NotEmpty` y `@NotNull` en el DTO `StudentDto` para asegurarse de que los campos no estén vacíos.
3. **Validación en el Controller**: En el controlador, la anotación `@Valid` se usa en el cuerpo del método para asegurarse de que el DTO cumpla con las restricciones antes de que el servicio lo procese.
4. **Resultado**: Si los datos no cumplen con las restricciones (por ejemplo, si un campo está vacío), se lanza un error de validación, informando al usuario sobre la naturaleza del error.

### Tabla de Anotaciones Comunes de Validación:

| **Anotación**     | **Descripción**                                                          | **Ejemplo**                                |
| ----------------- | ------------------------------------------------------------------------ | ------------------------------------------ |
| `@NotNull`        | El valor no puede ser `null`.                                            | `@NotNull String name;`                    |
| `@NotEmpty`       | El valor no puede estar vacío (aplica a Strings o colecciones).          | `@NotEmpty String lastname;`               |
| `@NotBlank`       | El valor no puede ser vacío o solo espacios en blanco (aplica a String). | `@NotBlank String address;`                |
| `@Size(min, max)` | Define un tamaño mínimo y máximo para Strings, arrays o colecciones.     | `@Size(min=2, max=30) String name;`        |
| `@Email`          | Valida que el campo tenga el formato de un correo electrónico.           | `@Email String email;`                     |
| `@Min(value)`     | Define un valor mínimo para un número.                                   | `@Min(18) Integer age;`                    |
| `@Max(value)`     | Define un valor máximo para un número.                                   | `@Max(65) Integer age;`                    |
| `@Pattern(regex)` | Valida que el campo cumpla con una expresión regular.                    | `@Pattern(regexp="\\d{10}") String phone;` |
| `@Positive`       | El valor debe ser positivo.                                              | `@Positive Integer score;`                 |
| `@Negative`       | El valor debe ser negativo.                                              | `@Negative Integer temperature;`           |

Este enfoque permite que las entradas se verifiquen automáticamente antes de que lleguen a la capa de servicio, reduciendo la necesidad de validación manual en el código y mejorando la robustez del sistema.

# Manejando errores

Dentro de `StudentController` agregamos un método que maneja excepciones de validación de datos:

```java
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
    var errors = new HashMap<String, String>();
    exp.getBindingResult().getAllErrors().forEach(error -> {
      var field = ((FieldError) error).getField();
      var errorMessage = error.getDefaultMessage();
      errors.put(field, errorMessage);
    });

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }
```

### Manejo de Excepciones Personalizado para Validación de Datos en el Controller

En este caso, se ha agregado un método al controlador que se encarga de capturar y manejar de manera personalizada las excepciones que ocurren cuando un argumento no cumple con las validaciones definidas en el DTO (en este caso, `StudentDto`). Este método es clave para mejorar la experiencia del usuario, ya que convierte los errores de validación en respuestas claras y estructuradas.

#### Explicación Paso a Paso:

1. **La excepción `MethodArgumentNotValidException`:**  
   Este tipo de excepción se lanza cuando uno o más argumentos en la petición no cumplen con las reglas de validación definidas en el DTO. En nuestro caso, los campos como `name`, `lastname` o `email` tienen anotaciones de validación, y si uno de estos no cumple (por ejemplo, si están vacíos), se dispara esta excepción.

2. **La anotación `@ExceptionHandler`:**  
   La anotación `@ExceptionHandler(MethodArgumentNotValidException.class)` indica que este método se encargará de manejar cualquier excepción del tipo `MethodArgumentNotValidException`. Es decir, cuando se detecte que hay una falla en las validaciones de los datos de entrada, este método se ejecutará automáticamente.

3. **Captura de Errores en un `HashMap`:**  
   Dentro del método, creamos una variable `errors` que es un `HashMap<String, String>`. Este `HashMap` almacenará los nombres de los campos que fallaron en la validación como clave, y el mensaje de error correspondiente como valor.

   ```java
   var errors = new HashMap<String, String>();
   ```

4. **Recorrido de los errores:**  
   Utilizamos `exp.getBindingResult().getAllErrors().forEach(...)` para obtener y recorrer todos los errores de validación que ocurrieron. Cada error se procesa dentro de la función anónima:

   ```java
   exp.getBindingResult().getAllErrors().forEach(error -> {
     var field = ((FieldError) error).getField();
     var errorMessage = error.getDefaultMessage();
     errors.put(field, errorMessage);
   });
   ```

   - **`error.getField()`:** Aquí se obtiene el nombre del campo que generó el error de validación, como `name` o `email`.
   - **`error.getDefaultMessage()`:** Se obtiene el mensaje de error correspondiente, que será algo como "no debe estar vacío", debido a la anotación `@NotEmpty`.

5. **Creación de la respuesta `ResponseEntity`:**  
   Finalmente, construimos un `ResponseEntity`, que es una clase de Spring que nos permite configurar la respuesta HTTP, incluyendo el cuerpo de la respuesta y el código de estado. En este caso, la respuesta tendrá un código de estado `400 BAD_REQUEST`, que es adecuado para indicar que hubo un error con los datos proporcionados por el usuario.

   ```java
   return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
   ```

6. **Salida JSON de los errores:**  
   Cuando el cliente (como Postman o una aplicación web) envía una solicitud que no cumple con las validaciones, este manejador de excepciones devolverá una respuesta JSON con los nombres de los campos que fallaron como claves, y sus mensajes de error como valores:

   ```json
   {
     "name": "no debe estar vacío",
     "email": "no debe estar vacío",
     "lastname": "no debe estar vacío"
   }
   ```

#### ¿Por Qué es Importante Este Manejador?

Este manejo personalizado de errores mejora la **usabilidad** y **claridad** de las respuestas de la API. Sin este manejador, Spring devolvería una respuesta de error menos comprensible y más técnica, que podría ser difícil de interpretar para un cliente que esté utilizando la API. Ahora, con este enfoque, los desarrolladores que consumen la API reciben un mensaje claro que les indica qué campos están fallando y por qué, lo que les permite corregir sus solicitudes de manera más eficiente.

### Ventajas de Este Enfoque:
- **Claridad:** Los mensajes de error son claros y están estructurados de manera comprensible para los usuarios.
- **Mantenimiento:** Si en el futuro se agregan más validaciones o se modifican los mensajes, este método capturará automáticamente esos cambios sin necesidad de modificar mucho código.
- **Reutilización:** Este mismo enfoque se puede aplicar para manejar otras excepciones y en diferentes controladores.
  
#### Ejemplo Visual:

Imagina que el usuario envía la siguiente petición:

```json
{
  "name": "",
  "email": "",
  "lastname": ""
}
```

El servidor devolverá:

```json
{
  "name": "no debe estar vacío",
  "email": "no debe estar vacío",
  "lastname": "no debe estar vacío"
}
```

Esto le permite al usuario saber exactamente qué campos debe corregir, facilitando la interacción con la API y mejorando la calidad de los datos ingresados.

> [!NOTE]
> Vistazo previo a posible forma de manejo de errores de forma global: [notas](0_Extras.md/#manejador-de-errores-global)

# Customizando mensajes de error

En el `StudentDto` hacemos los siguientes cambios:

```java
public record StudentDto(
    @NotEmpty(message = "El nombre no puede estar vacío!") String name,
    @NotEmpty(message = "El apellido no puede estar vacío!") String lastname,
    @NotNull(message = "El email no puede estar vacío!!") @NotEmpty(message = "El email no puede estar vacío!") String email,
    Integer SchoolId) {

}
```

Ejecutamos y probamos, veremos los mensajes que colocamos en las notas anteriores.