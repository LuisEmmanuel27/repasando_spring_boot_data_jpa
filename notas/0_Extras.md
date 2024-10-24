# ¿Qué es un Stream en Java?

Un **`Stream`** en Java es una secuencia de elementos que se pueden procesar de manera **declarativa** (es decir, describiendo **qué** se quiere hacer, en lugar de **cómo** hacerlo). Los streams permiten trabajar con colecciones de datos de una manera fluida y sencilla. 

Los `Streams` introducen un enfoque funcional para procesar datos, permitiéndote escribir código más conciso y expresivo.

#### Conceptos clave de `Stream`:

1. **Declarativo**: En lugar de usar bucles tradicionales (`for`, `while`), puedes usar `Stream` para describir lo que deseas hacer con los datos (como filtrar, mapear, contar, etc.).
  
2. **Lazy (evaluación perezosa)**: Las operaciones intermedias en un `Stream` no se ejecutan inmediatamente, sino hasta que se encuentra una operación terminal (como `collect()` o `forEach()`).

3. **Inmutable**: El `Stream` no modifica la colección original; genera una nueva secuencia de resultados sin alterar los datos originales.

4. **Secuencial o Paralelo**: Los `Streams` pueden ejecutarse de manera **secuencial** o **paralela** para aprovechar múltiples núcleos de procesadores.

### ¿De dónde proviene un `Stream`?

Un `Stream` generalmente se obtiene de una **fuente de datos**, como una colección (listas, conjuntos, mapas) o un array. Ejemplos típicos incluyen:

- **Colecciones** (`List`, `Set`, etc.)
- **Arreglos** (`Arrays`)
- **Archivos** (`BufferedReader`)
- **Generación infinita de datos** (`Stream.generate()` o `Stream.iterate()`)

Ejemplo básico de creación de un `Stream` desde una colección:

```java
List<String> nombres = Arrays.asList("Ana", "Juan", "Pedro");
Stream<String> nombresStream = nombres.stream();
```

### Operaciones de un `Stream`

Las operaciones en los `Streams` se dividen en dos tipos principales:

1. **Operaciones intermedias**: Transforman el `Stream` pero no lo consumen. Son **perezosas** (es decir, no se ejecutan hasta que hay una operación terminal). Ejemplos: `map()`, `filter()`, `sorted()`.

2. **Operaciones terminales**: Estas son las que terminan el procesamiento del `Stream` y devuelven un resultado o realizan una acción. Ejemplos: `collect()`, `forEach()`, `reduce()`.

### Operaciones intermedias comunes

#### 1. `map()`
Transforma cada elemento del `Stream` en otro objeto, basado en una función de transformación.

Ejemplo:

```java
List<String> nombres = Arrays.asList("Ana", "Juan", "Pedro");
List<Integer> longitudes = nombres.stream()
    .map(String::length)  // Transforma cada nombre en su longitud
    .collect(Collectors.toList());  // Recoge los resultados en una lista

System.out.println(longitudes);  // [3, 4, 5]
```

Aquí, `map()` toma cada nombre en la lista original y lo convierte en su longitud. Luego, `collect()` recolecta esas longitudes en una nueva lista.

#### 2. `filter()`
Filtra los elementos del `Stream` basándose en una condición.

Ejemplo:

```java
List<String> nombres = Arrays.asList("Ana", "Juan", "Pedro", "Maria");
List<String> nombresFiltrados = nombres.stream()
    .filter(nombre -> nombre.length() > 3)  // Filtra los nombres con más de 3 letras
    .collect(Collectors.toList());

System.out.println(nombresFiltrados);  // [Juan, Pedro, Maria]
```

En este caso, `filter()` elimina todos los nombres con menos de 3 letras.

#### 3. `sorted()`
Ordena los elementos del `Stream`.

Ejemplo:

```java
List<Integer> numeros = Arrays.asList(5, 3, 8, 1, 9);
List<Integer> numerosOrdenados = numeros.stream()
    .sorted()  // Ordena los números en orden ascendente
    .collect(Collectors.toList());

System.out.println(numerosOrdenados);  // [1, 3, 5, 8, 9]
```

#### 4. `distinct()`
Elimina los elementos duplicados en un `Stream`.

Ejemplo:

```java
List<Integer> numeros = Arrays.asList(1, 2, 2, 3, 4, 4, 5);
List<Integer> numerosUnicos = numeros.stream()
    .distinct()  // Elimina los duplicados
    .collect(Collectors.toList());

System.out.println(numerosUnicos);  // [1, 2, 3, 4, 5]
```

### Operaciones terminales comunes

#### 1. `collect()`
Recoge el resultado de un `Stream` en una colección o estructura de datos.

Ejemplo (convertir el `Stream` en una lista):

```java
List<String> nombres = Arrays.asList("Ana", "Juan", "Pedro");
List<String> nombresFiltrados = nombres.stream()
    .filter(nombre -> nombre.startsWith("A"))  // Filtra nombres que empiezan con "A"
    .collect(Collectors.toList());  // Recoge los resultados en una lista

System.out.println(nombresFiltrados);  // [Ana]
```

#### 2. `forEach()`
Ejecuta una acción sobre cada elemento del `Stream`.

Ejemplo:

```java
List<String> nombres = Arrays.asList("Ana", "Juan", "Pedro");
nombres.stream()
    .forEach(System.out::println);  // Imprime cada nombre en la consola
```

#### 3. `reduce()`
Combina los elementos de un `Stream` en un solo valor usando una operación acumulativa.

Ejemplo (suma de una lista de números):

```java
List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5);
int suma = numeros.stream()
    .reduce(0, Integer::sum);  // Suma todos los números

System.out.println(suma);  // 15
```

Aquí, `reduce()` combina los elementos del `Stream` en un solo valor (la suma de todos los números).

### Streams Paralelos

Si necesitas mejorar el rendimiento de las operaciones de `Stream` para procesar datos grandes, puedes usar un **Stream paralelo**. Esto permite dividir el procesamiento entre varios núcleos del procesador.

Ejemplo de cómo hacer un `Stream` paralelo:

```java
List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5);
numeros.parallelStream()
    .forEach(System.out::println);  // Procesa en paralelo (el orden de impresión puede variar)
```

### Evaluación perezosa en `Stream`

Una de las características más importantes de los `Streams` es que las **operaciones intermedias** como `filter()`, `map()`, y `sorted()` son **perezosas**. Esto significa que el procesamiento no comienza hasta que se invoca una operación terminal, como `collect()` o `forEach()`.

Ejemplo de evaluación perezosa:

```java
List<String> nombres = Arrays.asList("Ana", "Juan", "Pedro");

nombres.stream()
    .filter(nombre -> {
        System.out.println("Filtrando: " + nombre);
        return nombre.length() > 3;
    })
    .map(nombre -> {
        System.out.println("Mapeando: " + nombre);
        return nombre.toUpperCase();
    })
    .collect(Collectors.toList());
```

**Salida esperada**:
```
Filtrando: Ana
Filtrando: Juan
Mapeando: Juan
Filtrando: Pedro
Mapeando: Pedro
```

Aquí puedes ver que la evaluación perezosa significa que solo se evalúan los elementos necesarios. Por ejemplo, "Ana" no pasa el filtro, por lo que no se aplica la operación de mapeo en ella.

### Resumen:

- **Streams** proporcionan una forma funcional y declarativa de procesar colecciones en Java.
- Permiten realizar **transformaciones**, **filtrados** y otras operaciones sin modificar los datos originales.
- Existen dos tipos de operaciones en los `Streams`: **intermedias** y **terminales**.
- Las operaciones intermedias son perezosas, mientras que las terminales disparan el procesamiento del `Stream`.
- **Streams** pueden ser secuenciales o paralelos, lo que mejora la eficiencia en el procesamiento de grandes cantidades de datos.

Los `Streams` son muy poderosos para hacer que el código sea más legible y expresivo, especialmente al trabajar con colecciones.

---

# Manejador de errores global

Un **manejador de errores global** centraliza la lógica para capturar y manejar **cualquier excepción** que ocurra en la aplicación, no solo errores de validación, sino también otros tipos de excepciones que pueden surgir en la ejecución del programa, como errores de base de datos, violaciones de integridad, errores de negocio, etc.

### ¿Cómo se puede implementar un manejador global de errores?

Spring proporciona una manera fácil de crear un manejador de errores global utilizando la anotación `@ControllerAdvice`. Con esto, puedes capturar todas las excepciones en un solo lugar y personalizar las respuestas en función del tipo de error.

#### Implementación de un Manejador Global de Excepciones

Aquí te muestro cómo hacerlo de una manera sencilla:

1. **Crear una clase global de manejo de excepciones:**

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Manejo de excepciones de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Manejo de excepciones de tipo de argumento incorrecto (por ejemplo, un valor de parámetro inválido)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String errorMessage = String.format("El parámetro '%s' debería ser de tipo '%s'", ex.getName(), ex.getRequiredType().getSimpleName());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    // Manejo de excepciones genéricas (otras excepciones no controladas explícitamente)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>("Ocurrió un error interno en el servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

### Explicación:

1. **`@ControllerAdvice`:**  
   Esta anotación convierte la clase en un **manejador global de excepciones** que se aplicará a todos los controladores de la aplicación. Permite capturar y manejar excepciones de manera centralizada.

2. **Método `handleValidationExceptions`:**  
   Similar al ejemplo que has implementado antes, este método captura todas las excepciones de validación (como campos faltantes o vacíos). Recoge todos los errores y devuelve un `Map` que contiene el nombre del campo y el mensaje de error.

3. **Método `handleTypeMismatchException`:**  
   Este método maneja errores cuando el usuario pasa un tipo incorrecto de parámetro en una petición. Por ejemplo, si el usuario espera un número y pasa una cadena en su lugar. Devuelve un mensaje claro indicando cuál es el parámetro incorrecto y el tipo de dato esperado.

   **Ejemplo de error capturado:**  
   Si en un endpoint esperas un `Integer` como parámetro y el cliente envía una cadena, recibirás un mensaje como:
   ```json
   {
     "message": "El parámetro 'id' debería ser de tipo 'Integer'."
   }
   ```

4. **Método `handleGlobalException`:**  
   Este método captura **cualquier otra excepción** que no haya sido manejada de manera explícita. Aquí puedes devolver un mensaje genérico o un mensaje más detallado según la situación.

   **Ejemplo de error capturado:**  
   Si ocurre una excepción que no has manejado explícitamente (como una excepción de base de datos), el cliente recibirá:
   ```json
   {
     "message": "Ocurrió un error interno en el servidor."
   }
   ```

### Ventajas del Manejador de Excepciones Global:
1. **Centralización del manejo de errores:**  
   Todo el manejo de excepciones está en un solo lugar, lo que facilita el mantenimiento y la coherencia en las respuestas.
   
2. **Reutilización:**  
   No tienes que repetir el mismo manejo de errores en cada controlador. El manejador global se encarga de todos los controladores automáticamente.

3. **Flexibilidad:**  
   Puedes manejar diferentes tipos de excepciones de manera adecuada, proporcionando respuestas personalizadas basadas en el tipo de error.

4. **Mejor experiencia de usuario:**  
   Devuelves mensajes de error claros y consistentes, mejorando la comunicación entre la API y sus consumidores.

5. **Evolutividad:**  
   Si en el futuro necesitas cambiar la manera en la que manejas los errores (por ejemplo, agregando logs o cambiando los mensajes de respuesta), puedes hacerlo en un solo lugar.

### Tabla Comparativa de los Diferentes Tipos de Excepciones Manejadas

| Tipo de Excepción                     | Método de Manejo              | Descripción                                                            | Código de Estado          |
| ------------------------------------- | ----------------------------- | ---------------------------------------------------------------------- | ------------------------- |
| `MethodArgumentNotValidException`     | `handleValidationExceptions`  | Captura errores de validación de datos en los DTOs                     | 400 BAD REQUEST           |
| `MethodArgumentTypeMismatchException` | `handleTypeMismatchException` | Captura errores donde los parámetros no coinciden con el tipo esperado | 400 BAD REQUEST           |
| `Exception` (genérica)                | `handleGlobalException`       | Captura cualquier otra excepción no manejada explícitamente            | 500 INTERNAL SERVER ERROR |