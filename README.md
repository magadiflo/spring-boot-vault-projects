# Spring Boot + Spring Cloud Config Server + Vault

---

- En este repositorio se encontrará varios proyectos relacionados con la integración de `Spring Boot y Vault`.
- En el siguiente
  repositorio [spring-microservices-in-action-2021](https://github.com/magadiflo/spring-microservices-in-action-2021/blob/main/05.configuration-with-spring-cloud-config-server.md)
  se trabaja con `Spring Boot`, `Spring Cloud Config Server` y `Vault`.

## Proyectos

1. En el proyecto `spring-demo` se realiza la integración de `Spring Boot` con `Vault` y se agregan las configuraciones
   a `Vault` a través de la línea de comandos. Se usa por defecto el `Secrets Engines` llamado `secret`. Se crean
   los distintos perfiles para nuestra aplicación de Spring Boot.


2. En el proyecto `spring-boot-vault` se hace la integración de `Spring Boot` con `Vault` a través de la
   `interfaz gráfica` de `Vault`. En este proyecto se crea el `Secrets Engines` llamado `spring-microservices`. Se crean
   los distintos perfiles para nuestra aplicación de Spring Boot. Al finalizar este proyecto se hace uso de la línea de
   comando de `Vault` para crear un nuevo perfil apuntando, obviamente al `spring-microservices` como `secrets engines`.


3. El el directorio `/microservices` tenemos un proyecto que incluye `spring boot app` + `spring cloud config server` +
   `vault`. Se crean perfiles en `Vault` que es totalmente distinto a como hemos creado en los proyectos
   `spring-demo` y `spring-boot-vault`. Es decir, trabajar con `Spring Boot App + Vault` es distinto a trabajar con
   `Spring Boot App + Spring Cloud Config Server + Vault`.


4. En el directorio `/shared-config` hemos creado dos aplicaciones `spring-boot-test-vault` y `spring-boot-test`. Ambas
   aplicaciones tienen dos configuraciones en común, o en otras palabras utilizan los mismos valores para esas
   configuraciones y además cada aplicación tiene configuraciones que son propias de él. Entonces, lo que se hizo fue
   crear en `Vault` un `default-context` llamado `shared-config` para alojar las configuraciones compartidas y además se
   uso la configuración `application-name` para especificar el nombre de la aplicación donde se alojará las
   configuraciones propias de la aplicación. 
