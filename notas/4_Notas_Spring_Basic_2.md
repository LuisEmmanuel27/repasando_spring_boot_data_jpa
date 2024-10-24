# Perfiles

Creamos un nuevo archivo en resources de nombre application-dev.properties, en el application.properties ponemos:

```properties
spring.profiles.active=dev
```

al inicio del documento y si compilamos el codigo veremos que toma los valores de las properties de dev.

Se pueden agregar mas perfiles, por ejemplo:

```properties
spring.profiles.active=dev,prod
```

Aún si no existen los archivos de los perfiles descritos el programa compilara con normalidad.

> [!WARNING]
> Solo debemos tomar en cuenta que spring sobre escribe los valores de los perfiles en el archivo application.properties en dirección a la derecha, por ejemplo si ponemos:
> ```properties
> spring.profiles.active=dev,prod,test
> ```
> Al momento de compilar veríamos los datos del perfil test, ya que primero obtendría los de dev, pero luego los sustituiría por los de prod y finalmente por los de test.

Si queremos definir con codigo que perfil queremos usar, debemos primero quitar del application.properties el elemento de spring.profiles.active y luego definir el perfil que queremos usar en el programa de la siguiente manera:

```java
@SpringBootApplication
public class ExampleApplication {

	public static void main(String[] args) {
		var app = new SpringApplication(ExampleApplication.class);
		app.setDefaultProperties(Collections.singletonMap("spring.profiles.active", "test"));
		var context = app.run(args);

		MyFirstService myFirstService = context.getBean(MyFirstService.class);
		System.out.println(myFirstService.tellAStory());
		System.out.println(myFirstService.getCustomProperty());
		System.out.println(myFirstService.getCustomPropertyInt());
	}
}
```

De esa forma le estamos indicando que el perfil que queremos usar es test.

# Bean para perfil específico

Usamos la etiqueta @Profile:

```java
package com.alibou.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ApplicationConfig {

  @Bean("bean1")
  @Profile("dev")
  MyFirstClass myFirstBean() {
    return new MyFirstClass("First Bean");
  }

  @Bean
  @Profile("test")
  MyFirstClass mySecondBean() {
    return new MyFirstClass("Second Bean");
  }

  @Bean
  MyFirstClass myThirdClass() {
    return new MyFirstClass("Third Bean");
  }
}
```

Si en `MyFirstService` estamos inyectando el "bean1" y ejecutamos el programa en el perfil dev, funcionara con normalidad, pero si lo ejecutamos en perfil test, no funcionará.

---

También podemos asignar la etiqueta @Profile a la clase completa:

```java
@Configuration
@Profile("dev")
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

Sucede lo mismo si ejecutamos el programa, funcionará en perfil dev, pero no en test.