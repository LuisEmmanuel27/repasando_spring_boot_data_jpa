# Creando un proyecto con Spring

1. Vamos a Spring Initializr: [web](https://start.spring.io/)
2. Seleccionamos Maven, Versión actual de Spring, nombre de grupo com.alibou y en artifact colocamos example. Seleccionamos la versión de Java, en este caso la 23.
3. Agregamos las dependencias (por el momento):
   1. Spring Web
4. Generamos el proyecto.
5. Por simple estetica podemos modificar el nombre spring que aparece en la consola al momento de compilar el proyecto, solo hacemos lo siguiente (paso completamente omitible):
   1. Ir a una web como: [patorjk](https://www.patorjk.com/software/taag/#p=display&f=Graffiti&t=Type%20Something%20)
   2. Generar el nombre deseado con el estilo que se le quiera dar y copiarlo.
   3. En la carpeta `resources` creamos el archivo `banner.txt` y lo rellenamos con el nombre que se le haya generado.
   4. Compilamos la aplicación y deberiamos ver el nombre que se le haya generado en la consola.

# Primeras pruebas con Bean

1. Creamos dos clases dentro del com.alibou.example package:
   1. MyFirstClass.java
   2. ApplicationConfig.java
2. Agregamos los siguientes codigos:

```java
package com.alibou.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(ExampleApplication.class, args);

        MyFirstClass myFirstClass = context.getBean("myFirstBean", MyFirstClass.class);
        System.out.println(myFirstClass.sayHello());
    }
}
```
- **`@SpringBootApplication`**: Esta anotación es una combinación de varias anotaciones, que incluye `@Configuration`, `@EnableAutoConfiguration`, y `@ComponentScan`. Define la clase principal de la aplicación Spring Boot.
  
- **`SpringApplication.run(ExampleApplication.class, args)`**: Este comando inicia la aplicación Spring Boot. Crea un contexto de aplicación, que es como un contenedor que maneja los beans y otras configuraciones necesarias.

- **`context.getBean("myFirstBean", MyFirstClass.class)`**: Aquí, se está solicitando un bean de tipo `MyFirstClass` con el nombre `"myFirstBean"`. Este bean ha sido definido en la configuración de la aplicación (veremos cómo en la siguiente parte).

- **`System.out.println(myFirstClass.sayHello())`**: Luego de obtener el bean, se llama al método `sayHello()` para imprimir el mensaje.

```java
package com.alibou.example;

public class MyFirstClass {
  public String sayHello() {
    return "Hi from My First Class";
  }
}
```
- Esta es una clase muy sencilla con un método llamado `sayHello()` que devuelve un mensaje.
  
- Este será el objeto que se utilizará como bean en el contenedor de Spring.

```java
package com.alibou.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Bean
  MyFirstClass myFirstBean() {
    return new MyFirstClass();
  }
}
```
- **`@Configuration`**: Indica que esta clase contiene definiciones de beans que serán administrados por el contenedor de Spring.
  
- **`@Bean`**: Declara que el método `myFirstBean()` produce un bean gestionado por Spring. Este bean se llama `myFirstBean` (el nombre del método es el nombre por defecto del bean) y es de tipo `MyFirstClass`.

### Flujo General:
1. La aplicación arranca con `SpringApplication.run()`, creando un contexto de aplicación.
2. El contexto carga la configuración desde la clase `ApplicationConfig`, donde encuentra el bean `myFirstBean` de tipo `MyFirstClass`.
3. Cuando se necesita la instancia de `MyFirstClass`, Spring la provee desde el contexto.
4. Finalmente, se llama al método `sayHello()` de `MyFirstClass` y se imprime su mensaje.

# Agregando un service

### 1. **Clase principal `ExampleApplication` modificada**

```java
package com.alibou.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(ExampleApplication.class, args);

        MyFirstService myFirstService = context.getBean(MyFirstService.class);
        System.out.println(myFirstService.tellAStory());
    }

}
```

#### Cambios:
- **Uso de `MyFirstService`**:
   En la versión anterior, estábamos directamente obteniendo un bean de `MyFirstClass`. Ahora, hemos introducido un **servicio** llamado `MyFirstService`, y usamos el método `getBean` para obtener una instancia de este servicio desde el contexto de Spring. El servicio es responsable de manejar la lógica que antes estaba en la clase principal.

#### Explicación:
- **`MyFirstService` como bean**: En lugar de manejar la lógica directamente en la clase principal, ahora se encapsula en un servicio. Esto es una mejora, ya que sigue el principio de **separación de responsabilidades**, lo que hace el código más modular y fácil de mantener.
  
- **`context.getBean(MyFirstService.class)`**: Estamos solicitando al contexto de Spring que nos proporcione una instancia de `MyFirstService`, que es un bean gestionado por Spring.

- **`tellAStory()`**: En lugar de llamar a `sayHello()` directamente desde `MyFirstClass`, ahora lo estamos haciendo a través de `tellAStory()` en `MyFirstService`, lo que encapsula mejor la funcionalidad.

---

### 2. **Clase `MyFirstClass` modificada**

```java
package com.alibou.example;

public class MyFirstClass {
  private String myVar;

  public MyFirstClass(String myVar) {
    this.myVar = myVar;
  }

  public String sayHello() {
    return "Hi from My First Class ===> myVar = " + myVar;
  }
}
```

#### Cambios:
- **Uso de un campo `myVar`**:
   Ahora la clase `MyFirstClass` tiene un campo privado `myVar`, que se inicializa a través de su constructor. Este valor se usa en el método `sayHello()`.

#### Explicación:
- **`myVar` como parámetro**: Añadir un parámetro al constructor le da flexibilidad a la clase `MyFirstClass`. Ahora puede recibir información externa cuando se crea el objeto, lo que permite modificar el comportamiento de la clase en función de la información proporcionada.
  
- **Inyección de dependencias**: Aunque en este ejemplo el valor de `myVar` no se inyecta directamente desde Spring, esta estructura es útil para cuando necesites que Spring te inyecte valores externos, como propiedades configurables.

---

### 3. **Nueva clase `MyFirstService`**

```java
package com.alibou.example;

import org.springframework.stereotype.Service;

@Service
public class MyFirstService {

  private MyFirstClass myFirstClass;

   // @Autowired - ya no es necesario en este caso
  public MyFirstService(MyFirstClass myFirstClass) {
    this.myFirstClass = myFirstClass;
  }

  public String tellAStory() {
    return "the dependency is saying: " + myFirstClass.sayHello();
  }
}
```

#### Cambios:
- **Nueva clase `MyFirstService`**:
   Esta clase ha sido añadida para actuar como un servicio dentro de la aplicación. Spring se encarga de crearla y gestionarla como un bean gracias a la anotación `@Service`.

#### Explicación:
- **`@Service`**: Es una anotación de Spring que indica que esta clase es un servicio. Un servicio es un componente de la capa de lógica de negocio de la aplicación, donde generalmente se colocan las reglas de negocio y operaciones complejas.

- **Inyección de `MyFirstClass`**:
   Aquí estamos utilizando **inyección de dependencias por constructor**. `MyFirstService` depende de `MyFirstClass`, y cuando Spring crea una instancia de `MyFirstService`, automáticamente inyecta `MyFirstClass` en el constructor.

- **¿Por qué no se necesita `@Autowired`?**:
   En versiones recientes de Spring, **si una clase tiene un solo constructor**, Spring **automáticamente** usará ese constructor para inyectar las dependencias necesarias. Es por esto que no necesitamos la anotación `@Autowired` en el constructor de `MyFirstService`.

   En versiones anteriores de Spring (anteriores a 4.3), habría sido necesario agregar `@Autowired` para indicarle a Spring que debía inyectar `MyFirstClass` en el constructor. Ahora, Spring lo hace automáticamente si hay un único constructor.

#### Flujo del servicio:
- El servicio obtiene una instancia de `MyFirstClass` como una dependencia a través de su constructor.
- Luego, llama al método `sayHello()` de `MyFirstClass` en el método `tellAStory()` y devuelve un mensaje que incluye el saludo generado por `MyFirstClass`.

---

### Reflexión sobre las modificaciones

1. **Modularización**:
   - La principal mejora en este código es la separación de la lógica de negocio en un servicio (`MyFirstService`) y la delegación de responsabilidades a diferentes clases. Esto sigue el principio de **Single Responsibility**, lo que hace que el código sea más fácil de mantener y escalar.

2. **Inyección de dependencias por constructor**:
   - Usar inyección de dependencias por constructor es considerado una buena práctica, ya que asegura que las dependencias se proporcionen en el momento en que se crea la instancia del servicio. También facilita la prueba de las clases, ya que se puede crear una instancia de `MyFirstService` con una dependencia simulada (`mock`).

3. **Evitando `@Autowired` innecesario**:
   - Como vimos, el uso de `@Autowired` es innecesario cuando se tiene un solo constructor en la clase, lo que simplifica el código sin sacrificar funcionalidad. Spring gestiona automáticamente la inyección de dependencias en este caso, lo que hace el código más limpio y reduce el "ruido" de anotaciones.

---

### Resumen General

- **Inyección de dependencias**: Es el mecanismo mediante el cual Spring gestiona y proporciona instancias de clases (beans) a otros componentes de la aplicación. En este caso, `MyFirstClass` es inyectado en `MyFirstService`.
  
- **Inyección por constructor**: Es el método preferido de inyección de dependencias, ya que promueve un código más claro y testable. No necesitamos `@Autowired` cuando hay un único constructor.

- **Separación de responsabilidades**: Mover la lógica a un servicio es una forma de seguir las mejores prácticas de diseño de software, ya que hace que el código sea más modular y fácil de modificar o escalar en el futuro.

# Segundo Bean y Qualifier

### 1. **Clase `ApplicationConfig` modificada**

```java
package com.alibou.example;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Bean
  @Qualifier("bean1")
  MyFirstClass myFirstBean() {
    return new MyFirstClass("First Bean");
  }

  @Bean
  @Qualifier("bean2")
  MyFirstClass mySecondBean() {
    return new MyFirstClass("Second Bean");
  }
}
```

#### Cambios:

- **Dos beans de `MyFirstClass` con `@Qualifier`**:
   Ahora estamos definiendo dos instancias de `MyFirstClass` dentro de la configuración, cada una con un valor diferente para `myVar` (uno es `"First Bean"` y el otro es `"Second Bean"`). Ambos están anotados con `@Qualifier` para diferenciarlos.

#### Explicación:

- **¿Por qué usar `@Qualifier`?**:
   Cuando se tienen múltiples beans del **mismo tipo** (en este caso, `MyFirstClass`), Spring no sabe cuál de ellos inyectar por defecto. `@Qualifier` se usa para **desambiguar** estas situaciones, especificando qué bean debe ser inyectado en un lugar determinado.

   - En este caso, tenemos dos beans:
     1. `myFirstBean` con el `@Qualifier("bean1")`.
     2. `mySecondBean` con el `@Qualifier("bean2")`.

   - Si no tuviéramos los calificadores, Spring no podría decidir automáticamente cuál de estos dos beans usar, lo que resultaría en un error.

- **Creación de beans**:
   En este código, estamos creando los beans con el método `@Bean`, lo cual indica a Spring que estas instancias deben ser gestionadas por el contenedor y estarán disponibles para ser inyectadas en otros lugares de la aplicación.

---

### 2. **Clase `MyFirstService` modificada**

```java
package com.alibou.example;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MyFirstService {

  private MyFirstClass myFirstClass;

  public MyFirstService(@Qualifier("bean2") MyFirstClass myFirstClass) {
    this.myFirstClass = myFirstClass;
  }

  public String tellAStory() {
    return "the dependency is saying: " + myFirstClass.sayHello();
  }
}
```

#### Cambios:

- **Uso de `@Qualifier` en el constructor**:
   En el constructor de `MyFirstService`, ahora estamos usando `@Qualifier("bean2")` para indicar que queremos inyectar el bean que corresponde a `"bean2"`.

#### Explicación:

- **¿Por qué es necesario `@Qualifier` aquí?**:
   Dado que hay **dos beans del mismo tipo** (`MyFirstClass`) en el contexto de Spring, el contenedor no sabe cuál inyectar por defecto. Si no usamos `@Qualifier`, obtendríamos un error de ambigüedad al tratar de inyectar `MyFirstClass`, porque Spring no puede decidir cuál de los dos beans debería proporcionar a `MyFirstService`.

   Al usar `@Qualifier("bean2")`, le estamos diciendo explícitamente a Spring que inyecte el bean identificado como `"bean2"` (es decir, el que tiene `new MyFirstClass("Second Bean")`).

#### Flujo:

1. **Creación de beans**: Spring crea ambos beans `myFirstBean` y `mySecondBean`.
2. **Inyección con `@Qualifier`**: En `MyFirstService`, Spring inyecta `mySecondBean` en el constructor gracias a la anotación `@Qualifier("bean2")`.
3. **Método `tellAStory()`**: Cuando llamamos a `tellAStory()`, este método utiliza `mySecondBean`, por lo que la salida será `"Hi from My First Class ===> myVar = Second Bean"`.

---

### Alternativa: Usar `@Primary`

Otra manera de resolver la ambigüedad entre múltiples beans es usar la anotación `@Primary`. 

#### Ejemplo de `@Primary`:

```java
@Configuration
public class ApplicationConfig {

  @Bean
  @Primary
  MyFirstClass myFirstBean() {
    return new MyFirstClass("First Bean");
  }

  @Bean
  MyFirstClass mySecondBean() {
    return new MyFirstClass("Second Bean");
  }
}
```

En este caso:

- **`@Primary`**: Anotamos uno de los beans como **primario**, lo que significa que cuando Spring encuentre múltiples beans del mismo tipo, **usará este bean por defecto**. 
   - Si inyectas `MyFirstClass` en cualquier lugar sin especificar un `@Qualifier`, Spring usará el bean marcado como `@Primary`.

#### Diferencias entre `@Primary` y `@Qualifier`:

- **`@Primary`**:
  - Es útil cuando tienes un bean que debería ser el predeterminado en la mayoría de los casos.
  - Se utiliza cuando no quieres especificar un `@Qualifier` en todas partes y tienes un bean que debería ser preferido sobre otros cuando hay ambigüedad.
  - **Limitación**: No puedes usarlo para manejar inyecciones específicas en lugares donde necesitas un bean diferente.

- **`@Qualifier`**:
  - Te permite ser explícito sobre qué bean inyectar cuando hay varias opciones.
  - Es más flexible cuando tienes múltiples beans y necesitas inyectar uno en un lugar específico, mientras inyectas otro en otro lugar.
  - Requiere que lo uses en todas partes donde la inyección no es obvia (es decir, cuando hay múltiples beans del mismo tipo).

### Resumen General:

1. **Múltiples Beans**: Cuando tienes múltiples beans del mismo tipo, necesitas desambiguar cuál usar. Esto se puede hacer con `@Qualifier` o `@Primary`.
2. **`@Qualifier`**: Te permite ser específico y flexible, indicando exactamente cuál bean inyectar en cada caso.
3. **`@Primary`**: Es una solución general para definir un bean predeterminado, útil si la mayoría de las veces deseas usar el mismo bean sin tener que especificarlo en cada lugar.
4. **Inyección de dependencias**: En este caso, `@Qualifier("bean2")` asegura que el bean correcto sea inyectado en `MyFirstService`, resolviendo la ambigüedad entre los dos beans de tipo `MyFirstClass`.

# Otra forma de usar `@Qualifier`

### 1. **Clase `ApplicationConfig` Modificada**

```java
package com.alibou.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Bean("bean1")
  MyFirstClass myFirstBean() {
    return new MyFirstClass("First Bean");
  }

  @Bean
  MyFirstClass mySecondBean() {
    return new MyFirstClass("Second Bean");
  }

  @Bean
  MyFirstClass myThirdClass() {
    return new MyFirstClass("Third Bean");
  }
}
```

#### Cambios:

- **Uso del nombre en el método `@Bean("bean1")`**:
   En lugar de usar `@Qualifier` para identificar el bean `myFirstBean`, ahora se asigna un nombre directamente en la anotación `@Bean("bean1")`.

- **Otros Beans sin nombres explícitos**:
   Para los otros dos beans (`mySecondBean` y `myThirdClass`), no se asignan nombres personalizados. Spring usará los nombres por defecto que corresponden a los nombres de los métodos (`mySecondBean` y `myThirdClass`).

#### Explicación:

- **Uso alternativo de `@Qualifier`**:
   Al asignar el nombre `"bean1"` directamente en el método `@Bean`, no es necesario usar `@Qualifier` en las clases que requieran este bean, porque Spring sabrá cómo referirse a ese bean por su nombre.

   - **Con `@Bean("bean1")`**, estamos proporcionando un identificador personalizado para el bean, por lo que si lo necesitamos en otro lugar, podemos referirnos a él como `"bean1"`.

- **Uso del nombre por defecto**:
   Cuando no especificas un nombre explícito en el método `@Bean`, Spring toma el nombre del método (por ejemplo, `mySecondBean`), que puede ser usado de forma implícita o explícita en otras partes de la aplicación, como en la inyección de dependencias.

---

### 2. **Clase `MyFirstService` Modificada**

```java
package com.alibou.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MyFirstService {

  @Autowired
  @Qualifier("mySecondBean")
  private MyFirstClass myFirstClass;

  public String tellAStory() {
    return "the dependency is saying: " + myFirstClass.sayHello();
  }
}
```

#### Cambios:

- **Inyección mediante campo**:
   Ahora se ha movido la inyección de `MyFirstClass` desde el constructor a un campo de la clase, usando `@Autowired` directamente sobre la propiedad `myFirstClass`.

- **Uso de `@Qualifier` para el bean `"mySecondBean"`**:
   Se especifica `@Qualifier("mySecondBean")` en la propiedad para que se inyecte este bean en particular.

#### Explicación:

- **Inyección por campo**:
   Aunque la inyección de dependencias por campo no es la más recomendada en términos de mejores prácticas (ya que no permite la inmutabilidad), es una opción válida y común en proyectos más simples. Spring se encargará de inyectar automáticamente el bean necesario en este campo.

- **Uso de `@Qualifier` con el nombre predeterminado del bean**:
   Aquí estamos utilizando `@Qualifier("mySecondBean")` para indicarle a Spring que queremos inyectar específicamente el bean que se creó en el método `mySecondBean` de la clase `ApplicationConfig`.

   - **Nombre predeterminado**: Como no se especificó un nombre explícito con `@Bean("nombre")` para `mySecondBean`, Spring usa el nombre del método por defecto, que es `"mySecondBean"`. Por lo tanto, debemos usar este nombre en el `@Qualifier`.

---

### Diferencia con el código anterior

1. **Uso de `@Bean("bean1")` en lugar de `@Qualifier("bean1")`**:
   En la versión anterior, usábamos `@Qualifier` para identificar el bean `myFirstBean`. En esta versión, en su lugar, se usa el nombre directamente dentro de la anotación `@Bean`, lo que simplifica el proceso al no tener que usar `@Qualifier` en todas partes para este bean específico.

2. **Inyección por campo en lugar de por constructor**:
   La inyección de dependencias se hace directamente en el campo `myFirstClass` con `@Autowired`, en lugar de inyectarla a través del constructor. Aunque esto hace que el código sea más corto, como mencioné antes, la inyección por constructor generalmente se prefiere porque fomenta la inmutabilidad y facilita las pruebas unitarias.

---

### Resumen:

- **Uso de `@Bean("nombre")**: Al nombrar los beans directamente en el método `@Bean`, reducimos la necesidad de `@Qualifier` en lugares donde inyectamos estos beans. Esto puede hacer que el código sea más conciso y claro, especialmente cuando solo algunos beans necesitan diferenciación.
  
- **Inyección de dependencias**: La inyección de dependencias por campo con `@Autowired` funciona bien en proyectos sencillos, pero en proyectos más grandes o cuando se requiere inmutabilidad, la inyección por constructor es la práctica recomendada.

- **Alternativa con `@Primary`**: Si solo quisiéramos que un bean sea el predeterminado sin necesidad de usar `@Qualifier` en todas partes, podríamos marcar uno de los beans como `@Primary`. Esto resolvería las inyecciones de forma predeterminada para ese tipo de bean.

# Inyección de dependencias

### **Clase `MyFirstService` Modificada**

```java
package com.alibou.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MyFirstService {

  private MyFirstClass myFirstClass;

  @Autowired
  public void injectDependencies(@Qualifier("bean1") MyFirstClass myFirstClass) {
    this.myFirstClass = myFirstClass;
  }

  public String tellAStory() {
    return "the dependency is saying: " + myFirstClass.sayHello();
  }
}
```

### **Cambios Principales**:

1. **Inyección a través de un método setter en lugar de constructor o campo**:
   - En lugar de inyectar la dependencia `MyFirstClass` directamente en el constructor o como un campo con `@Autowired`, ahora se usa un **método setter** para inyectar la dependencia. Este método se llama `injectDependencies` y está anotado con `@Autowired`.
   
2. **Uso de `@Qualifier` dentro del método setter**:
   - **`@Qualifier("bean1")`** se usa para especificar que el bean llamado `"bean1"` (que probablemente se define en la clase de configuración) debe ser inyectado en la propiedad `myFirstClass`.

---

### **Explicación del Código**:

#### **Inyección por método setter**:

- **¿Qué es?**:
  En Spring, se pueden inyectar dependencias a través de tres formas principales:
  1. **Constructor**: Inyección mediante el constructor de la clase.
  2. **Campos**: Inyección directamente en los atributos de la clase con `@Autowired`.
  3. **Setter**: Inyección a través de un método setter que asigna el valor a un atributo privado.

  En este caso, se usa un método setter (`injectDependencies`) para inyectar el bean `MyFirstClass`.

- **Ventajas del método setter**:
  - **Flexibilidad**: La inyección por setter es útil cuando deseas tener la opción de cambiar la dependencia después de que el objeto ha sido creado.
  - **Fácil de probar**: Al separar la lógica de inyección, puedes probar fácilmente la clase con diferentes dependencias usando este método.
  - **Opcionalidad**: Los setters permiten que las dependencias no sean requeridas inmediatamente al crear el objeto. A diferencia del constructor, que obliga a pasar las dependencias al instanciar.

#### **Uso de `@Qualifier`**:

- **¿Por qué se usa?**:
  El uso de `@Qualifier("bean1")` es necesario porque hay múltiples beans de tipo `MyFirstClass` definidos en la aplicación (como `"bean1"`, `"mySecondBean"`, `"myThirdClass"`). Si no especificas un `@Qualifier`, Spring no sabrá cuál bean inyectar y lanzará una excepción debido a la ambigüedad.

- **Función de `@Qualifier` en este contexto**:
  En el método `injectDependencies`, el `@Qualifier("bean1")` le dice a Spring que queremos específicamente inyectar el bean llamado `"bean1"`, que probablemente fue definido en una clase de configuración usando `@Bean("bean1")`.

#### **Función de `@Autowired`**:

- **Automatización de la inyección**:
  La anotación `@Autowired` le indica a Spring que debe inyectar automáticamente la dependencia en el método `injectDependencies`. Spring detecta este método durante el ciclo de vida del contenedor y se asegura de que el bean correspondiente se pase como argumento del método.

---

### **Comparación con las versiones anteriores**:

1. **Inyección por setter vs. por constructor/campo**:
   - Anteriormente, se usaba inyección en el constructor y luego inyección directa en el campo. Ahora se está utilizando la inyección por método setter. Este patrón es útil cuando quieres un control más detallado sobre cómo y cuándo se asignan las dependencias.

2. **Relevancia de `@Qualifier`**:
   - El uso de `@Qualifier` sigue siendo esencial aquí, ya que el proyecto define múltiples beans de tipo `MyFirstClass`. Sin `@Qualifier`, Spring no podría resolver cuál bean inyectar.

3. **Flexibilidad adicional**:
   - Este enfoque con un setter proporciona mayor flexibilidad. Si en el futuro quieres cambiar el bean que se inyecta sin modificar el constructor, puedes hacerlo llamando de nuevo a `injectDependencies` con un nuevo bean.

# Otro ejemplo de inyección de dependencias

Solo modificamos en el MyFirstService:

```java
  @Autowired
  public void setMyFirstClass(@Qualifier("bean1") MyFirstClass myFirstClass) {
    this.myFirstClass = myFirstClass;
  }
```

En esta versión del código, la dependencia de `MyFirstClass` se inyecta mediante un **método setter** llamado `setMyFirstClass`. El uso de `@Qualifier("bean1")` sigue siendo necesario para especificar qué bean debe inyectarse, ya que hay múltiples definiciones de `MyFirstClass`.

### **Diferencias clave**:
1. **Método setter**: El método `setMyFirstClass` se encarga de asignar el bean `MyFirstClass` a la variable `myFirstClass`.
   
2. **Inyección con `@Autowired`**: Spring inyecta automáticamente el bean usando el método setter en lugar de hacerlo a través de un constructor.

3. **Uso de `@Qualifier`**: Todavía se utiliza `@Qualifier("bean1")` para garantizar que se inyecte el bean específico llamado `"bean1"`.

### **Resumen**:
Este enfoque es similar a la versión anterior, pero utiliza un **setter** en lugar de inyección por constructor o directamente en el campo. Es otra forma válida de inyectar dependencias en Spring, manteniendo la flexibilidad para modificar la dependencia después de la creación del objeto si es necesario.

# Vistazo a Enviroment

En esta nueva versión, el código ha sido modificado para incluir el uso de la clase **`Environment`** de Spring, que es una herramienta poderosa para acceder a las propiedades del sistema y configuraciones del entorno en tiempo de ejecución. A continuación, explicaré cada modificación y su propósito:

---

### 1. **Inyección del objeto `Environment`**:
   ```java
   @Autowired
   public void setEnvironment(Environment environment) {
     this.environment = environment;
   }
   ```
   - **`Environment`** es una interfaz proporcionada por Spring que permite acceder a las propiedades del sistema (como la versión de Java, el nombre del sistema operativo) y a propiedades personalizadas definidas en los archivos de configuración, como `application.properties`.
   - Aquí, se inyecta la dependencia de `Environment` mediante un método **setter**, lo que significa que Spring inyecta automáticamente el objeto de entorno en la variable `environment`.

### 2. **Uso del `Environment` para leer propiedades**:
   El servicio ahora incluye métodos para acceder a propiedades del entorno y propiedades personalizadas.
   ```java
   public String getJavaVersion() {
     return environment.getProperty("java.version");
   }
   
   public String getOsName() {
     return environment.getProperty("os.name");
   }
   
   public String readProperty() {
     return environment.getProperty("my.custom.property");
   }
   ```
   - **`getJavaVersion()`**: Accede a la propiedad del sistema que indica la versión de Java que está ejecutando la aplicación.
   - **`getOsName()`**: Accede a la propiedad del sistema que indica el nombre del sistema operativo.
   - **`readProperty()`**: Lee una propiedad personalizada llamada `"my.custom.property"`, que se debe definir en un archivo de configuración como `application.properties`.

   > Ejemplo de configuración en `application.properties`:
   ```properties
   my.custom.property=This is a custom property!
   ```

---

### Cambios en `ExampleApplication`:
   ```java
   public class ExampleApplication {
     public static void main(String[] args) {
       var context = SpringApplication.run(ExampleApplication.class, args);
       MyFirstService myFirstService = context.getBean(MyFirstService.class);
       System.out.println(myFirstService.tellAStory());
       System.out.println(myFirstService.getJavaVersion());
       System.out.println(myFirstService.getOsName());
       System.out.println(myFirstService.readProperty());
     }
   }
   ```
   - El **`main`** ahora invoca varios métodos del servicio:
     1. **`tellAStory()`**: Muestra el comportamiento esperado de `MyFirstClass`, donde se imprime la variable inyectada.
     2. **`getJavaVersion()`**: Imprime la versión de Java que se está utilizando.
     3. **`getOsName()`**: Imprime el nombre del sistema operativo.
     4. **`readProperty()`**: Imprime el valor de la propiedad personalizada **`my.custom.property`**, que debería estar definida en el archivo de configuración.

---

### **Aspectos clave a resaltar**:

- **Uso de `Environment`**: Esta es una técnica útil para acceder tanto a propiedades del sistema como a configuraciones personalizadas. Esto puede ser esencial cuando queremos que nuestra aplicación dependa de ciertos valores configurables sin necesidad de modificar el código fuente.

---

### **¿Por qué usar `Environment`?**

El uso de **`Environment`** facilita el acceso a configuraciones de manera flexible. Esto permite:
1. **Acceder a propiedades del sistema**: como la versión de Java o el sistema operativo, lo cual es útil si necesitas ajustar la lógica según el entorno en el que se ejecuta la aplicación.
2. **Configurar la aplicación sin modificar el código**: Al usar archivos de configuración externos como `application.properties`, puedes cambiar el comportamiento de la aplicación sin recompilarla.

# Usando @Value y @PropertySource

### 1. **Uso de `@PropertySource`**:
   ```java
   @PropertySource("classpath:custom.properties")
   public class MyFirstService {
   ```
   - **`@PropertySource`** es una anotación de Spring que se utiliza para indicar que se deben cargar las propiedades de un archivo específico. En este caso, Spring cargará el archivo **`custom.properties`** que se encuentra en la carpeta `resources` del proyecto.
   - **`classpath:`** indica que el archivo está ubicado dentro del classpath de la aplicación (generalmente en la carpeta `src/main/resources`).

### 2. **Inyección de propiedades con `@Value`**:
   Dentro del servicio `MyFirstService`, se utilizan varias anotaciones **`@Value`** para inyectar valores directamente desde el archivo de propiedades o como valores literales.
   ```java
   @Value("Hello People!!!")
   private String customProperty;
   
   @Value("${my.prop}")
   private String customPropertyFromAnotherFile;
   
   @Value("123")
   private Integer customPropertyInt;
   ```
   - **`@Value("Hello People!!!")`**: Este es un valor literal, que inyecta el texto **"Hello People!!!"** directamente en el campo **`customProperty`**.
   - **`@Value("${my.prop}")`**: Este valor se inyecta desde el archivo de propiedades **`custom.properties`** usando la clave **`my.prop`**. Spring buscará esta clave en el archivo cargado y asignará su valor a la variable **`customPropertyFromAnotherFile`**.
   - **`@Value("123")`**: Este valor literal inyecta el número **123** en la variable **`customPropertyInt`**, que es de tipo `Integer`.

---

### 3. **Archivo `custom.properties`**:
   Se creó un archivo adicional llamado **`custom.properties`** que contiene las propiedades que la aplicación necesita para funcionar. Un ejemplo del contenido de este archivo podría ser:
   ```properties
   my.prop=Alibou Demo
   ```
   - **`my.prop`**: Esta clave es referenciada en el código con `@Value("${my.prop}")`, y el valor correspondiente será inyectado en el campo **`customPropertyFromAnotherFile`**.

---

### 4. **Métodos `getters` para acceder a las propiedades**:
   Se han creado métodos **getter** para obtener los valores inyectados mediante las anotaciones **`@Value`**.
   ```java
   public String getCustomProperty() {
     return customProperty;
   }

   public String getCustomPropertyFromAnotherFile() {
     return customPropertyFromAnotherFile;
   }

   public Integer getCustomPropertyInt() {
     return customPropertyInt;
   }
   ```
   Estos métodos se utilizan para acceder a los valores de las propiedades inyectadas y los imprimen en la consola en el archivo **`ExampleApplication`**.

---

### 5. **Cambios en `ExampleApplication`**:
   ```java
   public class ExampleApplication {
     public static void main(String[] args) {
       var context = SpringApplication.run(ExampleApplication.class, args);
       
       MyFirstService myFirstService = context.getBean(MyFirstService.class);
       System.out.println(myFirstService.tellAStory());
       System.out.println(myFirstService.getCustomProperty());
       System.out.println(myFirstService.getCustomPropertyFromAnotherFile());
       System.out.println(myFirstService.getCustomPropertyInt());
     }
   }
   ```
   - Aquí, se muestra cómo se accede a los valores inyectados en `MyFirstService` utilizando los métodos getter. La aplicación imprimirá en la consola:
     1. El mensaje generado por el método **`tellAStory()`**, que utiliza el bean inyectado de **`MyFirstClass`**.
     2. El valor literal **"Hello People!!!"** inyectado en **`customProperty`**.
     3. El valor de la propiedad **`my.prop`** obtenido del archivo `custom.properties`.
     4. El valor entero **123** inyectado en **`customPropertyInt`**.

---

### **Aspectos clave a resaltar**:

- **`@PropertySource`**: Permite a la aplicación cargar un archivo de propiedades específico. Es útil para centralizar configuraciones y permitir que la aplicación sea más flexible sin cambiar el código fuente.
  
- **`@Value`**: Permite inyectar tanto valores literales como propiedades externas. Es una forma conveniente de obtener configuraciones sin necesidad de acceder directamente a los archivos de propiedades en el código.

- **Acceso a valores desde archivos de propiedades**: Esto mejora la configuración de la aplicación, ya que puedes definir distintos archivos de propiedades para diferentes entornos (producción, desarrollo, etc.) y ajustar el comportamiento de la aplicación sin modificar su lógica interna.

# @PropertySources

```java
package com.alibou.example;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySources({
    @PropertySource("classpath:custom.properties"),
    @PropertySource("classpath:custom-2.properties")
})
public class MyFirstService {

  private final MyFirstClass myFirstClass;

  @Value("Hello People!!!")
  private String customProperty;

  @Value("${my.prop}")
  private String customPropertyFromAnotherFile;

  @Value("${my.prop.2}")
  private String customPropertyFromAnotherFile2;

  @Value("123")
  private Integer customPropertyInt;

  // Getters
  public Integer getCustomPropertyInt() {
    return customPropertyInt;
  }

  public String getCustomProperty() {
    return customProperty;
  }

  public String getCustomPropertyFromAnotherFile() {
    return customPropertyFromAnotherFile;
  }

  public String getCustomPropertyFromAnotherFile2() {
    return customPropertyFromAnotherFile2;
  }

  // Methods
  public MyFirstService(@Qualifier("bean1") MyFirstClass myFirstClass) {
    this.myFirstClass = myFirstClass;
  }

  public String tellAStory() {
    return "the dependency is saying: " + myFirstClass.sayHello();
  }

}
```

### Cambios realizados:

1. **Uso de `@PropertySources`**:
   - Se reemplazó la anotación **`@PropertySource`** por **`@PropertySources`**, que permite cargar múltiples archivos de propiedades.
   - Se añadieron dos archivos de propiedades:
     ```java
     @PropertySources({
         @PropertySource("classpath:custom.properties"),
         @PropertySource("classpath:custom-2.properties")
     })
     ```
     Ahora se cargan tanto **`custom.properties`** como **`custom-2.properties`**.

2. **Nueva propiedad inyectada**:
   - Se agregó una nueva propiedad inyectada desde **`custom-2.properties`**:
     ```java
     @Value("${my.prop.2}")
     private String customPropertyFromAnotherFile2;
     ```
     Esta propiedad toma su valor de **`my.prop.2`** que se espera esté definido en **`custom-2.properties`**.

3. **Nuevo método getter**:
   - Se añadió un getter para la nueva propiedad **`customPropertyFromAnotherFile2`**:
     ```java
     public String getCustomPropertyFromAnotherFile2() {
       return customPropertyFromAnotherFile2;
     }
     ```

[Continuación](4_Notas_Spring_Basic_2.md)