# Spring Boot App + Spring Cloud Config Server + Vault

Se tomó como referencia el tutorial desarrollado en este repositorio
[spring-microservices-in-action-2021](https://github.com/magadiflo/spring-microservices-in-action-2021/blob/main/05.configuration-with-spring-cloud-config-server.md)

---

# Vault Server

---

Iniciamos el servidor de `Vault` asignándole `root` como su token id.

````bash
C:\vault_1.18.1
$ vault server -dev -dev-root-token-id root

==> Vault server configuration:

Administrative Namespace:
             Api Address: http://127.0.0.1:8200
                     Cgo: disabled
         Cluster Address: https://127.0.0.1:8201
   Environment Variables: , , , , , , , ALLUSERSPROFILE, ANSICON, ANSICON_DEF, APPDATA, CLINK_COMPLETIONS_DIR, CMDER_ALIASES, CMDER_CLINK, CMDER_CONFIGURED, CMDER_CONFIG_DIR, CMDER_INIT_END, CMDER_INIT_START, CMDER_ROOT, CMDER_SHELL, CMDER_USER_FLAGS, COLUMNS, COMPUTERNAME, ChocolateyInstall, ChocolateyLastPathUpdate, ComSpec, CommonProgramFiles, CommonProgramFiles(x86), CommonProgramW6432, ConEmuANSI, ConEmuAnsiLog, ConEmuArgs, ConEmuArgs2, ConEmuBackHWND, ConEmuBaseDir, ConEmuBaseDirShort, ConEmuBuild, ConEmuCfgDir, ConEmuConfig, ConEmuDir, ConEmuDrawHWND, ConEmuDrive, ConEmuHWND, ConEmuHooks, ConEmuPID, ConEmuPalette, ConEmuServerPID, ConEmuTask, ConEmuWorkDir, ConEmuWorkDrive, DataGrip, DriverData, EFC_4500, ESC, FPS_BROWSER_APP_PROFILE_STRING, FPS_BROWSER_USER_PROFILE_STRING, GIT_INSTALL_ROOT, GIT_VERSION_USER, HOME, HOMEDRIVE, HOMEPATH, IntelliJ IDEA Community Edition, JAVA_HOME, LANG, LINES, LOCALAPPDATA, LOGONSERVER, MAVEN_HOME, NUMBER_OF_PROCESSORS, NVM_HOME, NVM_SYMLINK, OLD_PATH, OS, OneDrive, PASSWORD_MS_CONFIG_SERVER, PATHEXT, PLINK_PROTOCOL, POSH_INSTALLER, POSH_THEMES_PATH, PROCESSOR_ARCHITECTURE, PROCESSOR_IDENTIFIER, PROCESSOR_LEVEL, PROCESSOR_REVISION, PROMPT, PSModulePath, PUBLIC, Path, ProgramData, ProgramFiles, ProgramFiles(x86), ProgramW6432, SESSIONNAME, SVN_SSH, SystemDrive, SystemRoot, TEMP, TMP, USERDOMAIN, USERDOMAIN_ROAMINGPROFILE, USERNAME, USERPROFILE, USER_BUILD, USER_MAJOR, USER_MINOR, USER_PATCH, VAULT_ADDR, ZES_ENABLE_SYSMAN, add_path, add_to_path, aliases, architecture_bits, ccall, cexec, clink_architecture, clink_dummy_capture_env, currenArgu, debug_output, depth, fast_init, feFlagName, feNot, find_query, found, full_path, git_executable, git_locale, lib_base, lib_console, lib_git, lib_path, lib_profile, max_depth, nix_tools, path_position, position, print_debug, print_error, print_verbose, print_warning, time_init, user_aliases, verbose_output, windir
              Go Version: go1.22.8
              Listener 1: tcp (addr: "127.0.0.1:8200", cluster address: "127.0.0.1:8201", disable_request_limiter: "false", max_request_duration: "1m30s", max_request_size: "33554432", tls: "disabled")
               Log Level:
                   Mlock: supported: false, enabled: false
           Recovery Mode: false
                 Storage: inmem
                 Version: Vault v1.18.1, built 2024-10-29T14:21:31Z
             Version Sha: f479e5c85462477c9334564bc8f69531cdb03b65

==> Vault server started! Log data will stream in below:

2024-11-07T11:34:43.257-0500 [INFO]  proxy environment: http_proxy="" https_proxy="" no_proxy=""
...
and starts unsealed with a single unseal key. The root token is already
authenticated to the CLI, so you can immediately begin using Vault.

You may need to set the following environment variables:

PowerShell:
    $env:VAULT_ADDR="http://127.0.0.1:8200"
cmd.exe:
    set VAULT_ADDR=http://127.0.0.1:8200

The unseal key and root token are displayed below in case you want to
seal/unseal the Vault or re-authenticate.

Unseal Key: j1Qvvw4JZVVtAc1RXEhd19uZILP0KEPF/a6+Dag11Dc=
Root Token: root

Development mode should NOT be used in production installations!
````

Ahora nos vamos a la dirección de `Vault` `http://localhost:8200`, nos autenticamos con el método `token` y usamos
`root` para iniciar sesión. Recordemos que `root` es el valor que le definimos al `-dev-root-token-id` cuando iniciamos
el servidor.

En el siguiente dashboard, vamos a la opción de `Secrets Engines` y damos click en `Enable new engine` para habilitar un
nuevo motor de secretos.

![01.png](assets/01.png)

Nos aparecerá una nueva pantalla con varias opciones, de las cuales en la sección `Generic` seleccionaremos `KV`. Luego
nos aparecerá la siguiente pantalla donde completamos con los siguientes datos.

![02.png](assets/02.png)

- `Path: spring-microservices`, define el nombre del motor de secretos en Vault donde `Spring Cloud Config Server`
  obtendrá las configuraciones.
- `Maximum number of versions: 2`, definimos que como máximo solo guarde 2 versiones de los secretos.

Luego de dar en el botón `Enable engine` nos aparecerá la siguiente pantalla donde daremos clic en `Create secret +`
para empezar a crear los secretos.

![03.png](assets/03.png)

## Perfil default: Creando secretos para demo-service

En la siguiente pantalla crearemos los secretos para el microservicio `demo-service`, estas propiedades de configuración
serán las propiedades del perfil por `default` para este microservicio.

![04.png](assets/04.png)

- `Path for this secret: demo-service`, aquí definimos el nombre del microservicio que se conecta a
  `Spring Cloud Config Server` para recuperar las configuraciones del perfil por defecto. El microservicio
  `demo-service` será construído más adelante.

Luego de dar en el botón `Save` nos mostrará la siguiente pantalla. Seleccionamos la pestaña `Secret` donde observamos
nuestras configuraciones.

![05.png](assets/05.png)

## Perfil dev: Creando secretos para demo-service

Tenga en cuenta que almacenar un secreto usando la sintaxis `spring-microservices/demo-service,dev` nos permite
almacenar una propiedad específica del perfil `dev` del servicio `demo-service`.

> Recordar que más adelante crearemos un microservicio al cual le llamaremos `demo-service`, es decir, la configuración
> `spring.application.name=demo-service` estará definida en ese microservicio y todas estas configuraciones lo estamos
> haciendo para ese microservicio que se conectará a `Spring Cloud Config Server` y este a su vez tomará las propiedades
> desde `Vault`.

En consecuencia, podemos crear secretos en `Vault` para el perfil `dev` de nuestro microservicio `demo-service`.
Esto lo hacemos de la siguiente manera.

Posicionados en nuestro motor de secretos `spring-microservices` vamos a la opción de `Create secret +`.

![06.png](assets/06.png)

En la siguiente pantalla vamos a crear el perfil usando el `<nombre_del_microservicio>,<perfil>` y a continuación
agregar las configuraciones propias de este perfil que en nuestro caso serán las mismas que definimos en el perfil por
`default` pero los valores sí serán propios del perfil `dev`.

![07.png](assets/07.png)

Luego de dar clic en el botón `Save` nos muestra la siguiente pantalla donde seleccionamos la pestaña `Secret` y
veremos las configuraciones creadas para nuestro perfil `dev`.

![08.png](assets/08.png)

## Perfil prod: Creando secretos para demo-service

De la misma manera que creamos las configuraciones para el perfil `dev`, haremos lo mismo para el perfil `prod`. En la
siguiente imagen observamos dichas configuraciones y en especial observemos cómo es que estamos creando este perfil
`demo-service,prod`.

![09.png](assets/09.png)

Finalmente, si nos posicionamos en la raíz de nuestro motor de secretos `spring-microservices` veremos los tres perfiles
creados para el microservicio `demo-service`: `default`, `dev`, `prod`.

![10.png](assets/10.png)

---

# Spring Cloud Config Server

---

## Dependencias

````xml
<!--Spring Boot 3.3.5-->
<!--Java 21-->
<!--Spring Cloud Version 2023.0.3-->
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
````

## Configuraciones iniciales

A continuación se muestran las configuraciones de nuestro `config-server`, estas incluyen configuraciones para poder
comunicarnos con `Vault`.

````yml
server:
  port: 8888
  error:
    include-message: always

spring:
  application:
    name: config-server
  profiles:
    active: vault

  cloud:
    config:
      server:
        vault:
          host: localhost
          port: 8200
          kvVersion: 2
          backend: spring-microservices
````

- Observemos que estamos activando el perfil `vault` en nuestro `config-server` y a continuación definiendo las
  configuraciones para conectanos a `Vault`.

- La configuración `kvVersion`, establece la versión del motor de secretos `kv`. Se recomienda utilizar
  `kvVersion de 2`.

- Con respecto al `backend: spring-microservices`, se refiere al camino o `mount path` del motor de secretos dentro de
  `HashiCorp Vault` desde donde `Spring Cloud Config Server` obtendrá la configuración. Es decir, este es el nombre que
  le dimos al motor de secretos `KV (Secrets Engine)` cuando lo montamos en `Vault`.

**NOTA**
> Un punto importante aquí es la versión del motor kv secrets. El valor predeterminado para
> `spring.cloud.config.server.kv-version` es `1`. Pero se recomienda usar la `versión 2` cuando usamos `Vault 0.10.0` o
> posterior.

## Configura aplicación como servidor de configuraciones

Para habilitar nuestro `config-server` como un servidor de configuraciones debemos agregar la anotación
`@EnableConfigServer` en la clase principal de la aplicación.

````java

@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

}
````

## Probando perfiles para el microservicio demo-service

Antes de empezar con la creación del microservicio `demo-service` podemos probar los perfiles que le creamos en `Vault`
a través del servidor de configuraciones. Entonces, podemos probar nuestro servidor de configuración mediante una
solicitud HTTP. Aquí puedes usar un comando `cURL` o algún cliente REST como `Postman`.

Empecemos enviando la siguiente petición.

````bash
$ curl -v  http://localhost:8888/demo-service/default | jq
>
* Request completely sent off
< HTTP/1.1 400
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Thu, 07 Nov 2024 19:39:27 GMT
< Connection: close
<
{
  "timestamp": "2024-11-07T19:39:27.536+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Missing required header in HttpServletRequest: X-Config-Token",
  "path": "/demo-service/default"
}
````

Este error indica que el servidor de configuración de `Spring Cloud Config` está esperando el encabezado
`X-Config-Token` en la solicitud HTTP para autenticar el acceso a `Vault`, pero la solicitud curl que realizamos no lo
incluyó.

Entonces, es importante enviar la cabecera `X-Config-Token` con el valor `root` en la petición para que nuestro
servidor de configuraciones autentique correctamente el acceso a `Vault`.

En la siguiente petición vemos los valores obtenidos para el perfil por defecto de nuestro microservicio `demo-service`.

````bash
$ curl -v -H "X-Config-Token: root" http://localhost:8888/demo-service/default | jq
>
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Thu, 07 Nov 2024 18:03:48 GMT
<
{
  "name": "demo-service",
  "profiles": [
    "default"
  ],
  "label": null,
  "version": null,
  "state": null,
  "propertySources": [
    {
      "name": "vault:demo-service",
      "source": {
        "external-api.api-key": "abc-123-default",
        "external-api.username": "username-default"
      }
    }
  ]
}
````

En la siguiente petición vemos los valores obtenidos para el perfil `dev` del microservicio `demo-service`. Es
importante observar que a pesar de que solicitamos el perfil `dev`, el perfil por `default` también se está cargando y
si alguna propiedad no está definida en el perfil `dev`, se tomará el del perfil por `default` si es que allí se
encuentra definido.

````bash
$ curl -v -H "X-Config-Token: root" http://localhost:8888/demo-service/dev | jq
>
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Thu, 07 Nov 2024 18:04:35 GMT
<
{
  "name": "demo-service",
  "profiles": [
    "dev"
  ],
  "label": null,
  "version": null,
  "state": null,
  "propertySources": [
    {
      "name": "vault:demo-service,dev",
      "source": {
        "external-api.api-key": "abc-123-dev",
        "external-api.username": "username-dev"
      }
    },
    {
      "name": "vault:demo-service",
      "source": {
        "external-api.api-key": "abc-123-default",
        "external-api.username": "username-default"
      }
    }
  ]
}
````

En la siguiente petición solicitamos las configuraciones para el perfil `prod` del microservicio `demo-service`.

````bash
$ curl -v -H "X-Config-Token: root" http://localhost:8888/demo-service/prod | jq
>
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Thu, 07 Nov 2024 18:04:53 GMT
<
{
  "name": "demo-service",
  "profiles": [
    "prod"
  ],
  "label": null,
  "version": null,
  "state": null,
  "propertySources": [
    {
      "name": "vault:demo-service,prod",
      "source": {
        "external-api.api-key": "abc-123-prod",
        "external-api.username": "username-prod"
      }
    },
    {
      "name": "vault:demo-service",
      "source": {
        "external-api.api-key": "abc-123-default",
        "external-api.username": "username-default"
      }
    }
  ]
}
````

---

# Spring Boot App: demo-service

---

## Dependencias

````xml
<!--Spring Boot 3.3.5-->
<!--Java 21-->
<!--Spring Cloud Version 2023.0.3-->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
````

## Configuraciones

Las siguientes configuraciones muestran la comunicación con el servidor de configuraciones. Es importante, además,
definir la propiedad `spring.application.name` con el valor `demo-service`, ya que es el nombre de este microservicio
con el que hemos creado las configuraciones y perfiles en `Vault`.

Otro punto importante es que en las configuraciones estamos definiendo el `token` que requiere `Vault` para autenticar
a quien solicite información sobre las configuraciones.

````yml
server:
  port: 8080
  error:
    include-message: always

spring:
  application:
    name: demo-service
  profiles:
    active: dev
  config:
    import: optional:configserver:http://localhost:8888
  # Definimos el token para Vault
  cloud:
    config:
      token: root
````

## Definiendo clases de configuración

Vamos a crear una clase de configuración con el que mapearemos las configuraciones definidas en `Vault`, incluso
podríamos agregar configuraciones en el `application.yml` del propio `demo-service` con el prefijo `external-api`
para mapearlas en esta clase. Este último caso lo veremos más adelante.

````java

@Setter
@Getter
@ConfigurationProperties(prefix = "external-api")
public class ExternalApiConfig {
    private String apiKey;
    private String username;
}
````

Finalmente, en la clase principal `DemoServiceApplication` implementamos la interfaz `CommandLineRunner` para mostrar
por consola los valores de las configuraciones según el perfil seleccionado.

````java

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(ExternalApiConfig.class)
@SpringBootApplication
public class DemoServiceApplication implements CommandLineRunner {

    private final ExternalApiConfig configuration;

    public static void main(String[] args) {
        SpringApplication.run(DemoServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("----------------------------------------");
        log.info("Configuration properties");
        log.info("   external-api.api-key: {}", configuration.getApiKey());
        log.info("   external-api.username: {}", configuration.getUsername());
        log.info("----------------------------------------");
    }
}
````

## Probando Spring Boot App + Spring Cloud Config + Vault

Teniendo levantado nuestro servidor de `Vault` con las configuraciones iniciales, levantamos nuestro servidor de
configuraciones y de la misma manera levantamos nuestro microservicio `demo-service` con un perfil específico. A
continuación se muestra el resultado de los perfiles seleccionados para el `demo-service`.

- Perfil `default`

````bash
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : Starting DemoServiceApplication using Java 21.0.1 with PID 3164 (M:\PERSONAL\PROGRAMACION\DESARROLLO_JAVA_SPRING\15.martin_diaz\spring-boot-vault-projects\microservices\demo-service\target\classes started by USUARIO in M:\PERSONAL\PROGRAMACION\DESARROLLO_JAVA_SPRING\15.martin_diaz\spring-boot-vault-projects)
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : The following 1 profile is active: "default"
[demo-service] [           main] o.s.c.c.c.ConfigServerConfigDataLoader   : Fetching config from server at : http://localhost:8888
[demo-service] [           main] o.s.c.c.c.ConfigServerConfigDataLoader   : Located environment: name=demo-service, profiles=[default], label=null, version=null, state=null
[demo-service] [           main] o.s.cloud.context.scope.GenericScope     : BeanFactory id=c3da43c7-4c8e-386c-b2fe-6df9545deac5
[demo-service] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
[demo-service] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
[demo-service] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.31]
[demo-service] [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
[demo-service] [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 2353 ms
[demo-service] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : Started DemoServiceApplication in 6.358 seconds (process running for 7.37)
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : ----------------------------------------
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : Configuration properties
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   :    external-api.api-key: abc-123-default
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   :    external-api.username: username-default
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : ----------------------------------------
````

- Perfil `dev`

````bash
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : Starting DemoServiceApplication using Java 21.0.1 with PID 16156 (M:\PERSONAL\PROGRAMACION\DESARROLLO_JAVA_SPRING\15.martin_diaz\spring-boot-vault-projects\microservices\demo-service\target\classes started by USUARIO in M:\PERSONAL\PROGRAMACION\DESARROLLO_JAVA_SPRING\15.martin_diaz\spring-boot-vault-projects)
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : The following 1 profile is active: "dev"
[demo-service] [           main] o.s.c.c.c.ConfigServerConfigDataLoader   : Fetching config from server at : http://localhost:8888
[demo-service] [           main] o.s.c.c.c.ConfigServerConfigDataLoader   : Located environment: name=demo-service, profiles=[default], label=null, version=null, state=null
[demo-service] [           main] o.s.c.c.c.ConfigServerConfigDataLoader   : Fetching config from server at : http://localhost:8888
[demo-service] [           main] o.s.c.c.c.ConfigServerConfigDataLoader   : Located environment: name=demo-service, profiles=[dev], label=null, version=null, state=null
[demo-service] [           main] o.s.cloud.context.scope.GenericScope     : BeanFactory id=c3da43c7-4c8e-386c-b2fe-6df9545deac5
[demo-service] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
[demo-service] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
[demo-service] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.31]
[demo-service] [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
[demo-service] [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 2325 ms
[demo-service] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : Started DemoServiceApplication in 5.751 seconds (process running for 6.661)
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : ----------------------------------------
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : Configuration properties
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   :    external-api.api-key: abc-123-dev
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   :    external-api.username: username-dev
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : ----------------------------------------
````

- Perfil `prod`

````bash
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : Starting DemoServiceApplication using Java 21.0.1 with PID 14320 (M:\PERSONAL\PROGRAMACION\DESARROLLO_JAVA_SPRING\15.martin_diaz\spring-boot-vault-projects\microservices\demo-service\target\classes started by USUARIO in M:\PERSONAL\PROGRAMACION\DESARROLLO_JAVA_SPRING\15.martin_diaz\spring-boot-vault-projects)
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : The following 1 profile is active: "prod"
[demo-service] [           main] o.s.c.c.c.ConfigServerConfigDataLoader   : Fetching config from server at : http://localhost:8888
[demo-service] [           main] o.s.c.c.c.ConfigServerConfigDataLoader   : Located environment: name=demo-service, profiles=[default], label=null, version=null, state=null
[demo-service] [           main] o.s.c.c.c.ConfigServerConfigDataLoader   : Fetching config from server at : http://localhost:8888
[demo-service] [           main] o.s.c.c.c.ConfigServerConfigDataLoader   : Located environment: name=demo-service, profiles=[prod], label=null, version=null, state=null
[demo-service] [           main] o.s.cloud.context.scope.GenericScope     : BeanFactory id=c3da43c7-4c8e-386c-b2fe-6df9545deac5
[demo-service] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
[demo-service] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
[demo-service] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.31]
[demo-service] [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
[demo-service] [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 2492 ms
[demo-service] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : Started DemoServiceApplication in 6.009 seconds (process running for 6.937)
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : ----------------------------------------
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : Configuration properties
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   :    external-api.api-key: abc-123-prod
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   :    external-api.username: username-prod
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : ----------------------------------------
````

## Agregando más configuraciones

Solo en el perfil por `default` agregaremos la siguiente configuración `external-api.db-password`.

![11.png](assets/11.png)

Ahora, en el `application.yml` del `demo-service` vamos a agregar la configuración `external-api.url`, dado que
hipotéticamente estoy considerando de que esa configuración no es confidencial.

````yml
external-api:
  url: http://localhost:3020/api/services
````

Ahora, como hemos agregado dos configuraciones más, uno en `Vault` y otro en el propio microservicio `demo-service`,
necesitamos actualizar las propiedades de nuestra clase de configuración.

````java

@Setter
@Getter
@ConfigurationProperties(prefix = "external-api")
public class ExternalApiConfig {
    private String apiKey;
    private String username;
    private String dbPassword; //Nueva configuración en Vault (default)
    private String url; //Nueva configuración en el application.yml de demo-service
}
````

Finalmente, necesitamos modificar la clase principal para imprimir los nuevos valores.

````java

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(ExternalApiConfig.class)
@SpringBootApplication
public class DemoServiceApplication implements CommandLineRunner {

    private final ExternalApiConfig configuration;

    public static void main(String[] args) {
        SpringApplication.run(DemoServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("----------------------------------------");
        log.info("Configuration properties");
        log.info("   external-api.api-key: {}", configuration.getApiKey());
        log.info("   external-api.username: {}", configuration.getUsername());

        log.info("   external-api.db-password: {}", configuration.getDbPassword());
        log.info("   external-api.url: {}", configuration.getUrl());
        log.info("----------------------------------------");
    }
}
````

Ejecutamos `demo-service` con el perfil `spring.profiles.active=dev` y vemos el resultado obtenido.

````bash
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : Starting DemoServiceApplication using Java 21.0.1 with PID 16288 (M:\PERSONAL\PROGRAMACION\DESARROLLO_JAVA_SPRING\15.martin_diaz\spring-boot-vault-projects\microservices\demo-service\target\classes started by USUARIO in M:\PERSONAL\PROGRAMACION\DESARROLLO_JAVA_SPRING\15.martin_diaz\spring-boot-vault-projects)
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : The following 1 profile is active: "dev"
[demo-service] [           main] o.s.c.c.c.ConfigServerConfigDataLoader   : Fetching config from server at : http://localhost:8888
[demo-service] [           main] o.s.c.c.c.ConfigServerConfigDataLoader   : Located environment: name=demo-service, profiles=[default], label=null, version=null, state=null
[demo-service] [           main] o.s.c.c.c.ConfigServerConfigDataLoader   : Fetching config from server at : http://localhost:8888
[demo-service] [           main] o.s.c.c.c.ConfigServerConfigDataLoader   : Located environment: name=demo-service, profiles=[dev], label=null, version=null, state=null
[demo-service] [           main] o.s.cloud.context.scope.GenericScope     : BeanFactory id=c3da43c7-4c8e-386c-b2fe-6df9545deac5
[demo-service] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
[demo-service] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
[demo-service] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.31]
[demo-service] [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
[demo-service] [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 2359 ms
[demo-service] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : Started DemoServiceApplication in 6.086 seconds (process running for 7.01)
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : ----------------------------------------
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : Configuration properties
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   :    external-api.api-key: abc-123-dev
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   :    external-api.username: username-dev
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   :    external-api.db-password: db-password-default
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   :    external-api.url: http://localhost:3020/api/services
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : ----------------------------------------
````

Como observamos, el resultado anterior trae los valores del perfil `dev`, pero como en `Vault` no tenemos la
configuración `db-password` (perfil `dev`), traerá el valor definido en el perfil `default`. Con respecto a la
`url`, como esta configuración no lo tenemos en ningún perfil de `Vault`, pues lo tomará del `application.yml` del
propio microservicio `demo-service`.

## Actualiza propiedades del demo-service usando Spring Boot Actuator + Spring Cloud Config Server

Nuestro microservicio `demo-service` se comunica con `Spring Cloud Config Server` para obtener sus propiedades de
configuración dependiendo del perfil que se haya seleccionado. Además, el `Spring Cloud Config Server` se comunica con
`Vault` para obtener dichas propiedades.

Pero podría darse el caso en el que el microservicio `demo-service` ya se encuentra en ejecución con los valores de las
propiedades tomadas de `Vault`. Pero luego las propiedades definidas en `Vault` son cambiados. **¿Cómo el microservicio
demo-service puede obtener los nuevos valores de las propiedades actualizadas en Vault sin necesidad de reiniciar el
microservicio?**, para eso nos apoyamos de `Spring Actuator` y la anotación `@RefreshScope`.

En el microservicio `demo-service` agregamos la siguiente dependencia en su `pom.xml`, dado que es el microservicio que
está obteniendo las propiedades que necesitamos actualizar y esta dependencia nos ayudará a hacerlo.

````xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
````

En el `application.yml` del microservicio `demo-service` habilitamos los endpoints de `Spring Actuator`. Podemos indicar
separados por coma las url de los endpoints, pero mejor usamos el `*` para indicar que habilite todos los endpoints de
`Spring Actuator`, eso incluye el `/refresh` quien precisamente es el que nos permitirá actualizar todos los componentes
anotados con `@RefreshScope`.

````yml
# Habilita endpoints de Spring Actuator
management:
  endpoints:
    web:
      exposure:
        include: '*'
````

Para usar la anotación `@RefreshScope` en Spring Boot para permitir la recarga dinámica de las propiedades de
configuración obtenidas de un servidor de configuración, debes aplicar la anotación a los beans que quieres recargar
cuando haya una actualización.

````java

@Setter
@Getter
@RefreshScope // Indica que esta clase debe ser recargada cuando se refresque la configuración
@ConfigurationProperties(prefix = "external-api")
public class ExternalApiConfig {
    private String apiKey;
    private String username;
    private String dbPassword; //Nueva configuración en Vault (default)
    private String url; //Nueva configuración en el application.yml de demo-service
}
````

Finalmente, vamos a crear un endpoint a quien realizaremos la llamada para poder verificar que los cambios se han
aplicado correctamente.

````java

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/externals")
public class ExternalController {

    private final ExternalApiConfig configuration;

    @GetMapping
    public void getProperties() {
        log.info("----------------------------------------");
        log.info("Configuration properties");
        log.info("   external-api.api-key: {}", configuration.getApiKey());
        log.info("   external-api.username: {}", configuration.getUsername());

        log.info("   external-api.db-password: {}", configuration.getDbPassword());
        log.info("   external-api.url: {}", configuration.getUrl());
        log.info("----------------------------------------");
    }

}
````

## Prueba actualización de propiedades de manera dinámica

Teniendo levantado nuestro servidor `Vault` con las configuraciones y valores ya almacenados, vamos a levantar a
continuación el `config-server` y finalmente nuestro microservicio `demo-service`.

Al levantar el `demo-service` obtenemos los siguientes valores iniciales de las propiedades. Notar que la propiedad
`external-api.db-password` nos retorna `null`, es porque esta propiedad no lo tenemos definida en `Vault` ni en ningún
otro lado. Bueno, en apartados iniciales sí lo teníamos configurado en `Vault` pero como bajamos y volvimos a levantar
`Vault` en desarrollo se perdieron todas las propiedades configuradas, así que se tuvo que volver a agregar las
configuraciones omitiendo el del `db-password`.

````bash
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : The following 1 profile is active: "dev"
...
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : ----------------------------------------
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : Configuration properties
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   :    external-api.api-key: abc-123-dev-v3
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   :    external-api.username: username-dev-v3
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   :    external-api.db-password: null
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   :    external-api.url: http://localhost:3020/api/services
[demo-service] [           main] d.magadiflo.app.DemoServiceApplication   : ----------------------------------------
````

Luego nos vamos a `Vault` y creamos una nueva versión de las propiedades definidas en el perfil `dev`, donde le vamos
a cambiar el valor de las propiedades. Ahora, para hacer visible esos cambios, debemos ejecutar el endpoint de
`Spring Actuator` que habilitamos en el `application.yml` de nuestro `demo-service`. Esta petición nos permitirá
actualizar las propiedades en nuestro `demo-service`.

````bash
$ curl -v -X POST http://localhost:8080/actuator/refresh | jq
>
< HTTP/1.1 200
< Content-Type: application/vnd.spring-boot.actuator.v3+json
< Transfer-Encoding: chunked
< Date: Fri, 08 Nov 2024 16:13:24 GMT
<
[
  "external-api.api-key",
  "external-api.username"
]
````

Como observamos, luego de ejecutar el endpoint anterior, vemos las configuraciones que han sufrido cambios.

Ahora, para comprobar que nuestro `demo-service` contiene los nuevos valores de las propiedades, vamos a realizar una
petición al endpoint creado en el `demo-service`.

````bash
$ curl -v http://localhost:8080/api/v1/externals | jq
>
< HTTP/1.1 200
< Content-Length: 0
< Date: Fri, 08 Nov 2024 16:14:18 GMT
<
````

Ahora, si nos vamos al log de nuestro IDE veremos que se muestran los nuevos valores que le definimos a las propiedades
del perfil `dev` en `Vault`.

````bash
[demo-service] [nio-8080-exec-3] d.m.app.controller.ExternalController    : ----------------------------------------
[demo-service] [nio-8080-exec-3] d.m.app.controller.ExternalController    : Configuration properties
[demo-service] [nio-8080-exec-3] d.m.app.controller.ExternalController    :    external-api.api-key: abc-123-dev-v4
[demo-service] [nio-8080-exec-3] d.m.app.controller.ExternalController    :    external-api.username: username-dev-v4
[demo-service] [nio-8080-exec-3] d.m.app.controller.ExternalController    :    external-api.db-password: null
[demo-service] [nio-8080-exec-3] d.m.app.controller.ExternalController    :    external-api.url: http://localhost:3020/api/services
[demo-service] [nio-8080-exec-3] d.m.app.controller.ExternalController    : ----------------------------------------
````

